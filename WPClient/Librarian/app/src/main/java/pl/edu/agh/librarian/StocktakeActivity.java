package pl.edu.agh.librarian;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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

public class StocktakeActivity extends Activity {

    private SimpleAdapter adapter;
    private ArrayList<Map<String, String>> booksList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocktake);
        booksList = new ArrayList<>();
        if (savedInstanceState != null) {
            booksList = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("books");
        }
        initList();
        if (savedInstanceState == null) {
            new GetSectionsTask().execute();
//
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("books", booksList);
    }

    private void initList() {
        ListView booksListView = (ListView) findViewById(R.id.stocktake_list_view);
        String[] from = new String[]{"title", "author", "year"};
        int[] to = new int[]{R.id.item_book_title_browse, R.id.item_book_author_browse,
                R.id.item_book_year_browse};
        adapter = new SimpleAdapter(StocktakeActivity.this, booksList, R.layout.browse_books_item, from, to);
        booksListView.setAdapter(adapter);
        booksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showConditionDialog(booksList.get(position).get("uuid"));
                    }
                });
            }
        });
    }

    private void showSectionDialog(final String[] types) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        AlertDialog.Builder b = new AlertDialog.Builder(StocktakeActivity.this);
        b.setTitle("Select section");
        b.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        });
        b.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                new GetBooksTask(types[which]).execute();

            }
        });
        b.show();

    }

    private void showConditionDialog(final String book) {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LOCKED);
        AlertDialog.Builder b = new AlertDialog.Builder(StocktakeActivity.this);
        b.setTitle("Change condition");
        b.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        });
        final String[] types = {"Available", "Reserved", "Borrowed", "Missing", "Damaged", "Destroyed"};
        b.setItems(types, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
                new Stocktake(book, types[which]).execute();
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
            }
        });
        b.show();
    }

    private void refreshListView() {
        adapter.notifyDataSetChanged();
    }

    ////////////////////////////////////
    public class GetBooksTask extends AsyncTask<Void, Void, Void> {

        private String section;

        public GetBooksTask(String section) {
            this.section = section;
        }

        @Override
        protected Void doInBackground(Void... params) {

            getBooks();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refreshListView();
                }
            });
            return null;

        }

        private void getBooks() {
            List<NameValuePair> params = new LinkedList<>();
            params.add(new BasicNameValuePair("section", section));
            try {
                String response = ServerAPI.GET("/rest/getBooksFromSection/", params);
                JSONArray books = new JSONArray(response);
                for (int i = 0; i < books.length(); i++) {
                    JSONObject book = books.getJSONObject(i);
                    Map<String, String> m = new HashMap<>();
                    m.put("title", book.getString("title"));
                    m.put("year", book.getString("year"));
                    m.put("uuid", book.getString("uuid"));
                    JSONArray authors = book.getJSONArray("authors");
                    StringBuilder authorsStr = new StringBuilder();
                    for (int j = 0; j < authors.length(); j++) {
                        JSONObject author = authors.getJSONObject(j);
                        authorsStr.append(author.get("name"));
                        authorsStr.append(" ");
                        authorsStr.append(author.get("surname"));
                        authorsStr.append(", ");
                    }
                    m.put("author", authorsStr.toString());
                    booksList.add(m);
                }

            } catch (Exception401 e) { //token expired
                ToastShow.showToastOnUIThread(StocktakeActivity.this, getString(R.string.token_expired), Toast.LENGTH_LONG);
                new Logout(StocktakeActivity.this).logout();
            } catch (Exception500 | Exception404 e) { //else
                ToastShow.showToastOnUIThread(StocktakeActivity.this, getString(R.string.server_error)
                        + " " + e.getMessage(), Toast.LENGTH_LONG);
            } catch (ConnectTimeoutException e) {
                ToastShow.showToastOnUIThread(StocktakeActivity.this, getString(R.string.timeout_error), Toast.LENGTH_SHORT);
            } catch (JSONException e) {
                Log.e("BrowseBooksJSON", e.toString());
            }
        }
    }

    /////////////////////////
    public class GetSectionsTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            getSections();
            return null;

        }

        private void getSections() {
            List<NameValuePair> params = new LinkedList<>();
            try {
                String response = ServerAPI.GET("/rest/getAllSection/", params);
                JSONArray sections = new JSONArray(response);
                final ArrayList<String> sections_dialog = new ArrayList<>();
                for (int i = 0; i < sections.length(); i++) {
                    JSONObject section = sections.getJSONObject(i);
                    sections_dialog.add(section.getString("name"));
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showSectionDialog(sections_dialog.toArray(new String[sections_dialog.size()]));
                    }
                });

            } catch (Exception401 e) { //token expired
                ToastShow.showToastOnUIThread(StocktakeActivity.this, getString(R.string.token_expired), Toast.LENGTH_LONG);
                new Logout(StocktakeActivity.this).logout();
            } catch (Exception500 | Exception404 e) { //else
                ToastShow.showToastOnUIThread(StocktakeActivity.this, getString(R.string.server_error)
                        + " " + e.getMessage(), Toast.LENGTH_LONG);
            } catch (ConnectTimeoutException e) {
                ToastShow.showToastOnUIThread(StocktakeActivity.this, getString(R.string.timeout_error), Toast.LENGTH_SHORT);
            } catch (JSONException e) {
                Log.e("BrowseBooksJSON", e.toString());
            }
        }
    }

    /////////////////////////////////

    public class Stocktake extends AsyncTask<Void, Void, Void> {

        private String uuid;
        private String state;

        public Stocktake(String uuid, String state) {
            this.uuid = uuid;
            this.state = state;
        }

        @Override
        protected Void doInBackground(Void... params) {

            sendStatus();
            return null;

        }

        private void sendStatus() {
            List<NameValuePair> params = new LinkedList<>();
            params.add(new BasicNameValuePair("bookId", uuid));
            params.add(new BasicNameValuePair("state", state));
            params.add(new BasicNameValuePair("token", SettingsManager.getToken()));
            try {
                String response = ServerAPI.GET("/rest/inventory/", params);
                for (Map<String, String> book : booksList) {
                    if (book.get("uuid").equals(uuid)) {
                        booksList.remove(book);
                        break;
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        refreshListView();
                    }
                });

            } catch (Exception401 e) { //token expired
                ToastShow.showToastOnUIThread(StocktakeActivity.this, getString(R.string.token_expired), Toast.LENGTH_LONG);
                new Logout(StocktakeActivity.this).logout();
            } catch (Exception500 | Exception404 e) { //else
                ToastShow.showToastOnUIThread(StocktakeActivity.this, getString(R.string.server_error)
                        + " " + e.getMessage(), Toast.LENGTH_LONG);
            } catch (ConnectTimeoutException e) {
                ToastShow.showToastOnUIThread(StocktakeActivity.this, getString(R.string.timeout_error), Toast.LENGTH_SHORT);
            }
        }
    }
}
