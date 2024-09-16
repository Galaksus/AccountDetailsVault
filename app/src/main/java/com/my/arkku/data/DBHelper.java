package com.my.arkku.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


import com.my.arkku.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SalasanaArkku.db";
    private static final String TAG = "DBHelper";

    // Variables to hold the SQL queries from the files
    private String SQL_CREATE_CATEGORY_ENTRIES;
    private String SQL_CREATE_CREDENTIALS_ENTRIES;

    // SQL statements for deleting the tables
    private static final String SQL_DELETE_CATEGORY_ENTRIES =
            "DROP TABLE IF EXISTS " + CategoryTable.Entry.TABLE_NAME;
    private static final String SQL_DELETE_CREDENTIALS_ENTRIES =
            "DROP TABLE IF EXISTS " + CredentialsTable.Entry.TABLE_NAME;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // Load SQL from the files
        SQL_CREATE_CATEGORY_ENTRIES = loadSQLFromFile(context, R.raw.create_category_entries);
        SQL_CREATE_CREDENTIALS_ENTRIES = loadSQLFromFile(context, R.raw.create_credentials_entries);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Execute the SQL statements
        db.execSQL(SQL_CREATE_CATEGORY_ENTRIES);
        db.execSQL(SQL_CREATE_CREDENTIALS_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables if they exist
        db.execSQL(SQL_DELETE_CATEGORY_ENTRIES);
        db.execSQL(SQL_DELETE_CREDENTIALS_ENTRIES);
        // Recreate the tables
        onCreate(db);
    }

    // Method to read the SQL file from raw resources
    private String loadSQLFromFile(Context context, int resourceId) {
        StringBuilder stringBuilder = new StringBuilder();
        InputStream inputStream = context.getResources().openRawResource(resourceId);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append("\n");
                Log.d(TAG, "Read line: " + line);  // Log each line read
            }
            reader.close();
        } catch (IOException e) {
            Log.e(TAG, "Error reading SQL file", e);  // Log any IOExceptions that occur
        }

        String sql = stringBuilder.toString();
        Log.d(TAG, "Full SQL loaded: " + sql);  // Log the full SQL after reading
        return sql;
    }
    // Method to check if the database and tables are created correctly
    public boolean isDatabaseCreatedCorrectly() {
        SQLiteDatabase db = null;
        boolean isCreatedCorrectly = false;

        try {
            db = getReadableDatabase();
            // Check if 'categories' table exists
            Cursor cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='categories'", null);
            if (cursor.getCount() > 0) {
                // Check if 'credentials' table exists
                cursor = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='credentials'", null);
                if (cursor.getCount() > 0) {
                    isCreatedCorrectly = true; // Both tables exist
                }
            }
            cursor.close();
        } catch (SQLiteException e) {
            Log.e(TAG, "Database check failed", e);
        } finally {
            if (db != null && db.isOpen()) {
                db.close();
            }
        }

        return isCreatedCorrectly;
    }
}

