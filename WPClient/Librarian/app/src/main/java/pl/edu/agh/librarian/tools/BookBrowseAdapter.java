package pl.edu.agh.librarian.tools;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.edu.agh.librarian.R;

public class BookBrowseAdapter extends BaseAdapter implements Filterable {

    private Context context;
    private List<Map<String, String>> books;
    private List<Map<String, String>> orig;

    public BookBrowseAdapter(Context context, List<Map<String, String>> books){
        this.context = context;
        this.books = books;
    }

    public Filter getFilter() {
        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                final FilterResults oReturn = new FilterResults();
                final List<Map<String, String>> results = new ArrayList<>();
                if (orig == null)
                    orig = books;
                if (constraint != null) {
                    if (orig != null && orig.size() > 0) {
                        for (final Map<String, String> b : orig) {
                            if (b.get("title").toLowerCase().contains(constraint.toString()) ||
                                    b.get("author").toLowerCase().contains(constraint.toString()))
                                results.add(b);
                        }
                    }
                    oReturn.values = results;
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence constraint,
                                          FilterResults results) {
                books = (List<Map<String, String>>) results.values;
                notifyDataSetChanged();
            }
        };
    }

    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Map<String, String> getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BookHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.browse_books_item, parent, false);
            holder = new BookHolder();
            holder.title = (TextView) convertView.findViewById(R.id.item_book_title_browse);
            holder.author = (TextView) convertView.findViewById(R.id.item_book_author_browse);
            holder.year = (TextView) convertView.findViewById(R.id.item_book_year_browse);
            convertView.setTag(holder);
        } else {
            holder = (BookHolder) convertView.getTag();
        }

        holder.title.setText(books.get(position).get("title"));
        holder.author.setText(books.get(position).get("author"));
        holder.year.setText(books.get(position).get("year"));


        return convertView;

    }

    public class BookHolder {
        TextView title;
        TextView author;
        TextView year;
    }

}