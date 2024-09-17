package com.my.arkku.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

public class DataAccessObject {

    private DBHelper dbHelper;
    private static final String TAG = "DBAccessObject";

    public DataAccessObject(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Method to insert a category into the 'categories' table
    public boolean insertCategory(String name) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CategoryTable.Entry.NAME, name);  // Use the correct column name here

        long newRowId = -1;
        try {
            newRowId = db.insert(CategoryTable.Entry.TABLE_NAME, null, values);  // Use the correct table name here
            if (newRowId == -1) {
                Log.e(TAG, "Failed to insert category: " + name);
                return false;
            } else {
                Log.d(TAG, "Inserted category with ID: " + newRowId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error inserting category", e);
        } finally {
            db.close();
        }
        return true;
    }

    // Method to insert credentials into the 'credentials' table
    public boolean insertCredential(String categoryName, String email, String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(CredentialsTable.Entry.CATEGORY_NAME, categoryName);
        values.put(CredentialsTable.Entry.EMAIL, email);
        values.put(CredentialsTable.Entry.USERNAME, username);
        values.put(CredentialsTable.Entry.PASSWORD, password);
        long newRowId = -1;
        try {
            newRowId = db.insert(CredentialsTable.Entry.TABLE_NAME, null, values);
            if (newRowId == -1) {
                Log.e(TAG, "Failed to insert credential for category name: " + categoryName);
                return false;
            } else {
                Log.d(TAG, "Inserted credential with ID: " + newRowId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error inserting credential", e);
        } finally {
            db.close();
        }
        return true;
    }

// Method to fetch a category by row number and return it as JSON
    public String getCategoryByRowNumber(int rowNumber) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        String jsonCategory =  "";

        try {
            // Query the 'categories' table to get the category at the specified row number
            String query = "SELECT * FROM categories LIMIT 1 OFFSET ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(rowNumber)});

            // Check if a record is found
            if (cursor != null && cursor.moveToFirst()) {
                // Extract data from the cursor
                int categoryId = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(CategoryTable.Entry.NAME));

                // Construct JSON string manually
                jsonCategory = String.format("{\"categoryId\":%d,\"category_name\":\"%s\"}",
                        categoryId, escapeJson(categoryName));
                Log.d(TAG, "jsonCategory: " + jsonCategory);

            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving category by row number", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return jsonCategory;
    }

    // Method to fetch a credential by row number and return JSON data as a string
    public String getCredentialByRowNumber(int rowNumber) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = null;
        String jsonCredential = "";

        try {
            // Query to get the credential based on the provided row number
            String query = "SELECT * FROM credentials LIMIT 1 OFFSET ?";
            cursor = db.rawQuery(query, new String[]{String.valueOf(rowNumber)});

            // Check if a record is found
            if (cursor != null && cursor.moveToFirst()) {
                // Extract data from the cursor
                int credentialId = cursor.getInt(cursor.getColumnIndexOrThrow(CredentialsTable.Entry.ID));
                String category_name = cursor.getString(cursor.getColumnIndexOrThrow(CredentialsTable.Entry.CATEGORY_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(CredentialsTable.Entry.EMAIL));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(CredentialsTable.Entry.USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(CredentialsTable.Entry.PASSWORD));

                // Construct JSON string manually
                jsonCredential = String.format("{\"id\":%d,\"category_name\":\"%s\",\"email\":\"%s\",\"username\":\"%s\",\"password\":\"%s\"}",
                        credentialId, escapeJson(category_name), escapeJson(email), escapeJson(username), escapeJson(password));
                Log.d(TAG, "jsonCredential: " + jsonCredential);

            }
        } catch (SQLiteException e) {
            Log.e(TAG, "Error retrieving credential by row number", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return jsonCredential;
    }

    public String getCredentialRowByID(int id) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String jsonCredential = "{}";
        Cursor cursor = null;

        try {
            // Define the query
            String query = "SELECT id, category_name, email, username, password FROM credentials WHERE id = ?";
            // Execute the query
            cursor = db.rawQuery(query, new String[]{String.valueOf(id)});

            // Check if we have results
            if (cursor != null && cursor.moveToFirst()) {
                // Retrieve the data
                int credentialId = cursor.getInt(cursor.getColumnIndexOrThrow(CredentialsTable.Entry.ID));
                String categoryName = cursor.getString(cursor.getColumnIndexOrThrow(CredentialsTable.Entry.CATEGORY_NAME));
                String email = cursor.getString(cursor.getColumnIndexOrThrow(CredentialsTable.Entry.EMAIL));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(CredentialsTable.Entry.USERNAME));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(CredentialsTable.Entry.PASSWORD));

                // Construct JSON string manually
                jsonCredential = String.format("{\"id\":%d,\"category_name\":\"%s\",\"email\":\"%s\",\"username\":\"%s\",\"password\":\"%s\"}",
                        credentialId, escapeJson(categoryName), escapeJson(email), escapeJson(username), escapeJson(password));

                Log.d(TAG, "jsonCredential: " + jsonCredential);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error retrieving credential row by ID", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return jsonCredential;
    }

    // Helper method to escape special characters in JSON strings
    private String escapeJson(String value) {
        if (value == null) {
            return "";
        }
        return value.replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\b", "\\b")
                .replace("\f", "\\f")
                .replace("\n", "\\n")
                .replace("\r", "\\r")
                .replace("\t", "\\t");
    }

    // Method to get the number of rows from any table
    public int getNumberOfRowsFromTable(String tableName) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        int rowCount = 0;  // Default to 0 in case the table is empty or an error occurs
        Cursor cursor = null;

        try {
            // Query to count the number of rows in the given table
            String query = "SELECT COUNT(*) AS count FROM " + tableName;
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Get the row count from the 'count' column
                rowCount = cursor.getInt(cursor.getColumnIndexOrThrow("count"));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error counting rows in table: " + tableName, e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return rowCount;
    }

    public boolean checkIfGivenCategoryExists(String category, String table_name, String column_name) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String query = "SELECT 1 FROM " + table_name + " WHERE " + column_name + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{category});
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }

    public boolean updateRowById(int id, String categoryName, String email, String username, String password) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("category_name", categoryName);
        values.put("email", email);
        values.put("username", username);
        values.put("password", password);

        String whereClause = "id = ?";
        String[] whereArgs = new String[] { String.valueOf(id) };

        int rowsAffected = db.update("credentials", values, whereClause, whereArgs);
        db.close();
        return rowsAffected > 0;
    }

    public boolean deleteRowById(int id,  String table_name, String column_name) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();

            String whereClause = column_name + " = ?";
            String[] whereArgs = new String[] { String.valueOf(id) };

            Log.d("DatabaseOperation", "Executing delete with whereClause: " + whereClause + " and whereArgs: " + whereArgs[0]);

            int rowsAffected = db.delete(table_name, whereClause, whereArgs);

            Log.d("DatabaseOperation", "Rows affected: " + rowsAffected);

            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e("DatabaseOperation", "Error deleting row by ID", e);
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }

    public boolean deleteRowByCategory(String category,  String table_name, String column_name) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();

            String whereClause = column_name + " = ?";
            String[] whereArgs = new String[] { String.valueOf(category) };

            Log.d("DatabaseOperation", "Executing delete with whereClause: " + whereClause + " and whereArgs: " + whereArgs[0]);

            int rowsAffected = db.delete(table_name, whereClause, whereArgs);

            Log.d("DatabaseOperation", "Rows affected: " + rowsAffected);

            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e("DatabaseOperation", "Error deleting row by ID", e);
            return false;
        } finally {
            if (db != null) {
                db.close();
            }
        }
    }
}