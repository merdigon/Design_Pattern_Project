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
import pl.edu.agh.librarian.tools.ToastShow;

public class BrowseBooksActivity extends Activity implements SearchView.OnQueryTextListener{

    private BookBrowseAdapter adapter;
    private ArrayList<Map<String, String>> booksList = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_books);
        booksList = new ArrayList<>();
        if (savedInstanceState != null) {
            booksList = (ArrayList<Map<String, String>>) savedInstanceState.getSerializable("books");
        }
        initList();
        initSearch();
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
        ListView scannedBooksListView = (ListView) findViewById(R.id.browse_books_list);
        adapter = new BookBrowseAdapter(BrowseBooksActivity.this, booksList);
        scannedBooksListView.setAdapter(adapter);
        scannedBooksListView.setTextFilterEnabled(true);
    }

    private void initSearch() {
        SearchView search = (SearchView) findViewById(R.id.browse_books_search);
        search.setIconified(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                adapter.getFilter().filter(s.toLowerCase());
                return true;
            }
        });

        search.setQueryHint("Title or Author");
    }

    private void refreshListView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
            List<NameValuePair> params = new LinkedList<>();
            try {
                String response = ServerAPI.GET("/rest/getBooks/", params);
                JSONArray books = new JSONArray(response);
                for (int i = 0; i < books.length(); i++) {
                    JSONObject book = books.getJSONObject(i);
                    Map<String, String> m = new HashMap<>();
                    m.put("title", book.getString("title"));
                    m.put("year", book.getString("year"));
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
                ToastShow.showToastOnUIThread(BrowseBooksActivity.this, getString(R.string.token_expired), Toast.LENGTH_LONG);
                new Logout(BrowseBooksActivity.this).logout();
            } catch (Exception500 | Exception404 e) { //else
                ToastShow.showToastOnUIThread(BrowseBooksActivity.this, getString(R.string.server_error)
                        + " " + e.getMessage(), Toast.LENGTH_LONG);
            } catch (ConnectTimeoutException e) {
                ToastShow.showToastOnUIThread(BrowseBooksActivity.this, getString(R.string.timeout_error), Toast.LENGTH_SHORT);
            } catch (JSONException e) {
                Log.e("BrowseBooksJSON", e.toString());
            }
        }
    }
}
