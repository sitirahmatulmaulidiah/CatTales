package com.example.latihan_praktikum_7.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.latihan_praktikum_7.data.entity.Cat;
import com.example.latihan_praktikum_7.util.NotificationHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "cats.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DatabaseContract.CatEntry.TABLE_NAME + " (" +
                    DatabaseContract.CatEntry.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    DatabaseContract.CatEntry.COLUMN_NAME + " TEXT, " +
                    DatabaseContract.CatEntry.COLUMN_ORIGIN + " TEXT, " +
                    DatabaseContract.CatEntry.COLUMN_DESCRIPTION + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DatabaseContract.CatEntry.TABLE_NAME;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public List<Cat> searchCats(String keyword) {
        List<Cat> result = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selection = DatabaseContract.CatEntry.COLUMN_NAME + " LIKE ?";
        String[] selectionArgs = new String[]{"%" + keyword + "%"};

        Cursor cursor = db.query(
                DatabaseContract.CatEntry.TABLE_NAME,
                null,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(DatabaseContract.CatEntry.COLUMN_ID));
                String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CatEntry.COLUMN_NAME));
                String origin = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CatEntry.COLUMN_ORIGIN));
                String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.CatEntry.COLUMN_DESCRIPTION));

                Cat cat = new Cat(id, name, origin, description);
                result.add(cat);
            } while (cursor.moveToNext());

            cursor.close();
        }

        return result;
    }

    // Method insertCat baru
    public void insertCat(String name, String origin, String description, Context context) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseContract.CatEntry.COLUMN_NAME, name);
        values.put(DatabaseContract.CatEntry.COLUMN_ORIGIN, origin);
        values.put(DatabaseContract.CatEntry.COLUMN_DESCRIPTION, description);

        db.insert(DatabaseContract.CatEntry.TABLE_NAME, null, values);
        db.close();

        // Kirim notifikasi setelah data ditambahkan
        NotificationHelper.showNotification(context, "New Cat Added!", "Kucing " + name + " telah ditambahkan!");
    }
}
