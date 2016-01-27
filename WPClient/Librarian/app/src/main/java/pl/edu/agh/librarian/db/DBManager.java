package pl.edu.agh.librarian.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.LinkedList;
import java.util.List;

public class DBManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "LibrarianDB";

    private static final String TABLE_BORROWED = "borrowed";

    private static final String KEY_ID = "id";
    private static final String KEY_TITLE = "title";
    private static final String KEY_DUE_DATE = "due_date";


    private static final String[] COLUMNS_BORROWED = {KEY_ID, KEY_TITLE, KEY_DUE_DATE};


    public DBManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_BORROWED_TABLE = "CREATE TABLE " + TABLE_BORROWED + " ( " +
                KEY_ID + " TEXT AUTO INCREMENT, " +
                KEY_TITLE + " TEXT, " +
                KEY_DUE_DATE + " DATE," +
                "PRIMARY KEY(id))";

        db.execSQL(CREATE_BORROWED_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion) {

        }
    }

    /////////////////

    public void addBorrowedEntry(String title, String dueDate) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, title);
        values.put(KEY_DUE_DATE, dueDate);

        db.insertWithOnConflict(TABLE_BORROWED,
                null,
                values,
                SQLiteDatabase.CONFLICT_REPLACE);

        db.close();
    }

    public List<List<Object>> getBorrowedEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor =
                db.query(TABLE_BORROWED,
                        COLUMNS_BORROWED,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null);

        if (cursor.moveToFirst()) {
            List<List<Object>> lst = new LinkedList<>();
            List<Object> lst2;
            do {
                lst2 = new LinkedList<>();
                lst2.add(cursor.getString(1));
                lst2.add(cursor.getString(2));
                lst.add(lst2);
            } while (cursor.moveToNext());
            cursor.close();
            return lst;
        } else {
            cursor.close();
            return null;
        }
    }

    public void deleteAllBorrowedEntries() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BORROWED, null, null);
    }
}
