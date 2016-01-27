package pl.edu.agh.librarian;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
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
import pl.edu.agh.librarian.tools.BookBrowseAdapter;
import pl.edu.agh.librarian.tools.Logout;
import pl.edu.agh.librarian.tools.ServerAPI;
import pl.edu.agh.librarian.tools.SettingsManager;
import pl.edu.agh.librarian.tools.ToastShow;

public class BorrowedBooksActivity extends Activity{

    private SimpleAdapter adapter;
    private ArrayList<Map<String, String>> booksList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrowed_books);
        booksList = new ArrayList<>();
        if (savedInstanceState != null) {
            booksList = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("books");
        }
        initList();
        if(savedInstanceState == null) {
            new GetBooksTask().execute();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("books", booksList);
    }

    private void initList() {
        ListView scannedBooksListView = (ListView) findViewById(R.id.borrowed_books_list);
        String[] from = new String[] {"title", "author", "date"};
        int[] to = new int[] { R.id.item_book_title_browse, R.id.item_book_author_browse,
                R.id.item_book_year_browse };
        adapter = new SimpleAdapter(BorrowedBooksActivity.this, booksList, R.layout.browse_books_item, from, to);
        scannedBooksListView.setAdapter(adapter);
    }

    private void refreshListView() {
        adapter.notifyDataSetChanged();
    }

    /////////////////////////////

    public class GetBooksTask extends AsyncTask<Void, Void, Void> {

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
            try {
                List<NameValuePair> params = new LinkedList<>();
                params.add(new BasicNameValuePair("token", SettingsManager.getToken()));
                String response = ServerAPI.GET("/rest/getUserBooksToken/", params);
                JSONArray books = new JSONArray(response);
                for (int i = 0; i < books.length(); i++) {
                    JSONObject book = books.getJSONObject(i);
                    Map<String, String> m = new HashMap<>();
                    m.put("title", book.getString("title"));
                    JSONArray dates = book.getJSONArray("dates");
                    JSONObject date = dates.getJSONObject(0);
                    String date_real = date.getString("planningReturnDate").replace(",", "-").replace("[","").replace("]","");
                    m.put("date", date_real);
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
                ToastShow.showToastOnUIThread(BorrowedBooksActivity.this, getString(R.string.token_expired), Toast.LENGTH_LONG);
                new Logout(BorrowedBooksActivity.this).logout();
            } catch (Exception500 | Exception404 e) { //else
                ToastShow.showToastOnUIThread(BorrowedBooksActivity.this, getString(R.string.server_error)
                        + " " + e.getMessage(), Toast.LENGTH_LONG);
            } catch (ConnectTimeoutException e) {
                ToastShow.showToastOnUIThread(BorrowedBooksActivity.this, getString(R.string.timeout_error), Toast.LENGTH_SHORT);
            } catch (JSONException e) {
                Log.e("BorrowedBooksJSON", e.toString());
            }
        }
    }
}
