package pl.edu.agh.librarian.db;

import android.content.Context;

import java.util.List;

// TABLE MODULE PATTERN
public class BorrowedBooksManager {

    private DBManager dbManager;

    public BorrowedBooksManager(Context context) {
        dbManager = new DBManager(context);
    }

    public void addBook(String title, String dueDate) {
        dbManager.addBorrowedEntry(title, dueDate);
    }

    public List<List<Object>> getBooks() {
        return dbManager.getBorrowedEntries();
    }

    public void deleteBooks() {
        dbManager.deleteAllBorrowedEntries();
    }

}
