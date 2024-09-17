package com.my.arkku.main;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import com.my.arkku.data.Category;
import com.my.arkku.data.CategoryTable;
import com.my.arkku.data.Credential;
import com.my.arkku.data.CredentialsTable;
import com.my.arkku.data.DataAccessObject;

public class JavaScriptInterface {
    Context mContext;
    Activity mActivity;
    private DataAccessObject DataAccessObject;

    // Instantiate the interface and set the context
    public JavaScriptInterface(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
        DataAccessObject = new DataAccessObject(context);
    }

    void createUIElements() {
        int numOfCategories = DataAccessObject.getNumberOfRowsFromTable(CategoryTable.Entry.TABLE_NAME);
        int numOfCredentials = DataAccessObject.getNumberOfRowsFromTable(CredentialsTable.Entry.TABLE_NAME);

        Log.d("omadebugggi", "numOfCategories: " + numOfCategories + " numOfCredentials: " +numOfCredentials);

        for (int categoryRow = 0; categoryRow < numOfCategories; categoryRow++) {
           String category = DataAccessObject.getCategoryByRowNumber(categoryRow);
           callJavaScriptFunction(("createOuterElements('"+category+"');"));
        }

        for (int credentialRow = 0; credentialRow < numOfCredentials; credentialRow++) {
            String credential = DataAccessObject.getCredentialByRowNumber(credentialRow);
            callJavaScriptFunction(("createInnerElements('"+credential+"');"));
        }
    }

    // Show a toast from the web page using JavaScript
    @JavascriptInterface
    public void showToast(String message) {
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    // Show a toast from the web page using JavaScript
    @JavascriptInterface
    public boolean addCredentialsToDatabase(String category, String username, String email, String password) {
        Log.d("debugtagi", category + " " + username + " " + email + " " + password);

        boolean exists = DataAccessObject.checkIfGivenCategoryExists(category, CategoryTable.Entry.TABLE_NAME, CategoryTable.Entry.NAME);
        if (!exists) {
            DataAccessObject.insertCategory(category);
        }

        boolean success = DataAccessObject.insertCredential(category, email, username, password);

        return success;
    }

    @JavascriptInterface
    public boolean updateRowById(int id, String oldCategoryName, String categoryName, String email, String username, String password) {
        // Check if category name exists in Categories table
        boolean exists = DataAccessObject.checkIfGivenCategoryExists(categoryName, CategoryTable.Entry.TABLE_NAME, CategoryTable.Entry.NAME);
        if (!exists) {
            DataAccessObject.insertCategory(categoryName);
        }


        Boolean success = DataAccessObject.updateRowById(id, categoryName, email, username, password);

        // Check if oldCategoryName exists in Credentials table
        exists = DataAccessObject.checkIfGivenCategoryExists(oldCategoryName, CredentialsTable.Entry.TABLE_NAME, CredentialsTable.Entry.CATEGORY_NAME);
        Log.d("omaddd", "exists: " + exists + " success: "+ success);
        if (success && !exists) {
            Log.d("omaddd", "trying to delete:  " + oldCategoryName);

            success = DataAccessObject.deleteRowByCategory(oldCategoryName, CategoryTable.Entry.TABLE_NAME, CategoryTable.Entry.NAME);
            Log.d("omaddd", "success ppävitett: " + success);
        }

        return success;
    }
    @JavascriptInterface
    public boolean deleteRow(int id, String category) {
        boolean success = DataAccessObject.deleteRowById(id, CredentialsTable.Entry.TABLE_NAME, CredentialsTable.Entry.ID);
        boolean exists = DataAccessObject.checkIfGivenCategoryExists(category, CredentialsTable.Entry.TABLE_NAME, CredentialsTable.Entry.CATEGORY_NAME);
        if (!exists) {
            success = DataAccessObject.deleteRowByCategory(category, CategoryTable.Entry.TABLE_NAME, CategoryTable.Entry.NAME);
        }

        return success;
    }
    @JavascriptInterface
    public String getCredentialRowByID(int id) {
        Log.d("debugtagi", "käuys ja id: " + id);
        String data = DataAccessObject.getCredentialRowByID(id);
        Log.d("debugtagi", data);

        return data;
    }

    public static void callJavaScriptFunction(String javascriptCode) {
        // Post to main thread
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                MainActivity.mywebView.evaluateJavascript(javascriptCode, null);
            }
        });
    }
}
