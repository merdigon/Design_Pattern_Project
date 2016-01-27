package pl.edu.agh.librarian;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import pl.edu.agh.librarian.Exceptions.Exception401;
import pl.edu.agh.librarian.Exceptions.Exception404;
import pl.edu.agh.librarian.Exceptions.Exception500;
import pl.edu.agh.librarian.tools.Logout;
import pl.edu.agh.librarian.tools.ServerAPI;
import pl.edu.agh.librarian.tools.SettingsManager;
import pl.edu.agh.librarian.tools.ToastShow;

public class BorrowABookActivity extends Activity {

    private JSONObject userInfo;
    private ArrayList<HashMap<String, String>> scannedBooksList = null;
    private SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String response;


        if ((response = getIntent().getStringExtra("response")).length() > 0) {
            try {
                userInfo = new JSONObject(response);
            } catch (JSONException e) {
                finish();
            }
        } else {
            finish();
        }
        setContentView(R.layout.activity_borrow_abook);
        scannedBooksList = new ArrayList<>();
        if (savedInstanceState != null) {
            scannedBooksList = (ArrayList<HashMap<String, String>>) savedInstanceState.getSerializable("scanned");
        }
        initList();
        initText();
        initButtons();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("scanned", scannedBooksList);
    }

    private void initText() {
        TextView firstName = (TextView) findViewById(R.id.first_name_borrow);
        try {
            firstName.setText(userInfo.getString("name"));
        } catch (JSONException e) {
            firstName.setText("NULL");
        }
        TextView lastName = (TextView) findViewById(R.id.last_name_borrow);
        try {
            lastName.setText(userInfo.getString("surname"));
        } catch (JSONException e) {
            lastName.setText("NULL");
        }
        TextView cardNumber = (TextView) findViewById(R.id.card_number_borrow);
        try {
            cardNumber.setText(userInfo.getString("idNumber"));
        } catch (JSONException e) {
            cardNumber.setText("NULL");
        }
    }

    private void initList() {
        ListView scannedBooksListView = (ListView) findViewById(R.id.scanned_books_borrow);
        String[] from = new String[]{"title", "author", "year"};
        int[] to = new int[]{R.id.item_book_title_borrow, R.id.item_book_author_borrow, R.id.item_book_year_borrow};
        adapter = new SimpleAdapter(this, scannedBooksList, R.layout.scanned_item_borrow, from, to);
        scannedBooksListView.setAdapter(adapter);
    }

    private void initButtons() {
        Button scan = (Button) findViewById(R.id.scan_button_borrow);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startScan(0);
            }
        });

        Button cancel = (Button) findViewById(R.id.cancel_button_borrow);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button borrow = (Button) findViewById(R.id.borrow_button_borrow);//TODO: after click, waiting dialog
        borrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> uuids = new ArrayList<>();
                for (Map<String, String> m : scannedBooksList) {
                    uuids.add(m.get("uuid"));
                }
                new BorrowBooksTask(uuids).execute();
            }
        });
    }

    public void startScan(int intent_code) {
        Intent intent = new Intent("com.google.zxing.client.android.SCAN");
        intent.putExtra("SAVE_HISTORY", false);
        startActivityForResult(intent, intent_code);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents;

                if (!intent.getStringExtra("SCAN_RESULT").isEmpty()) {
                    contents = intent.getStringExtra("SCAN_RESULT");
                    Log.i("contents", contents);
                    new GetBookTask(contents).execute();
                } else {
                    finish();
                    Toast.makeText(BorrowABookActivity.this, getString(R.string.scan_canceled), Toast.LENGTH_SHORT).show();
                }

            } else if (resultCode == RESULT_CANCELED) {
                finish();
                Toast.makeText(BorrowABookActivity.this, getString(R.string.scan_canceled), Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void addAndRefreshList(String response) {
        try {
            JSONObject book = new JSONObject(response);
            HashMap<String, String> map = new HashMap<>();
            map.put("uuid", book.getString("uuid"));
            map.put("title", book.getString("title"));
            map.put("year", book.getString("year"));
            JSONArray authors = book.getJSONArray("authors");
            StringBuilder authorsStr = new StringBuilder();
            for (int i = 0; i < authors.length(); i++) {
                JSONObject author = authors.getJSONObject(i);
                authorsStr.append(author.get("name"));
                authorsStr.append(" ");
                authorsStr.append(author.get("surname"));
                authorsStr.append(", ");
            }
            map.put("author", authorsStr.toString());
            scannedBooksList.add(map);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            Log.e("BorrowABookJSON", e.toString());
        }
    }

    public class GetBookTask extends AsyncTask<Void, Void, Void> {

        private String uuid;

        GetBookTask(String uuid) {
            this.uuid = uuid;
        }

        @Override
        protected Void doInBackground(Void... params) {

            getBook();
            return null;

        }

        private void getBook() {
            List<NameValuePair> params = new LinkedList<>();//TODO: check if book is available
            params.add(new BasicNameValuePair("uuid", uuid));
            try {
                final String response = ServerAPI.GET("/rest/getBook/", params);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        addAndRefreshList(response);
                    }
                });
            } catch (Exception401 e) { //token expired
                ToastShow.showToastOnUIThread(BorrowABookActivity.this, getString(R.string.token_expired), Toast.LENGTH_LONG);
                new Logout(BorrowABookActivity.this).logout();
            } catch (Exception404 e) { //no book with uuid
                ToastShow.showToastOnUIThread(BorrowABookActivity.this, getString(R.string.wrong_book_uuid), Toast.LENGTH_LONG);
            } catch (Exception500 e) { //else
                ToastShow.showToastOnUIThread(BorrowABookActivity.this, getString(R.string.server_error)
                        + " " + e.getMessage(), Toast.LENGTH_LONG);
            } catch (ConnectTimeoutException e) {
                ToastShow.showToastOnUIThread(BorrowABookActivity.this, getString(R.string.timeout_error), Toast.LENGTH_SHORT);
            }
        }
    }

    public class BorrowBooksTask extends AsyncTask<Void, Void, Void> {

        private List<String> uuids;

        BorrowBooksTask(List<String> uuids) {
            this.uuids = uuids;
            new SettingsManager(BorrowABookActivity.this);
        }

        @Override
        protected Void doInBackground(Void... params) {
            boolean noError = true;
            for (String uuid : uuids) {
                if (noError)
                    noError = sendBooks(uuid);
            }
            if (noError)
                finish();
            return null;

        }

        private boolean sendBooks(String uuid) {
            List<NameValuePair> params = new LinkedList<>();
            params.add(new BasicNameValuePair("bookUuid", uuid));
            params.add(new BasicNameValuePair("token", SettingsManager.getToken()));
            try {
                params.add(new BasicNameValuePair("idNumber", userInfo.getString("idNumber")));
                try {
                    final String response = ServerAPI.POST("/rest/borrowBook/", params);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            addAndRefreshList(response);
                        }
                    });
                    return true;
                } catch (Exception401 e) { //token expired
                    ToastShow.showToastOnUIThread(BorrowABookActivity.this, getString(R.string.token_expired), Toast.LENGTH_LONG);
                    new Logout(BorrowABookActivity.this).logout();
                    return false;
                } catch (Exception500 | Exception404 e) { //else
                    ToastShow.showToastOnUIThread(BorrowABookActivity.this, getString(R.string.server_error)
                            + " " + e.getMessage(), Toast.LENGTH_LONG);
                    return false;
                } catch (ConnectTimeoutException e) {
                    ToastShow.showToastOnUIThread(BorrowABookActivity.this, getString(R.string.timeout_error), Toast.LENGTH_SHORT);
                    return false;
                }
            } catch (JSONException e) {
                ToastShow.showToastOnUIThread(BorrowABookActivity.this, getString(R.string.server_error)
                        + " " + e.getMessage(), Toast.LENGTH_LONG);
                return false;
            }
        }
    }
}
