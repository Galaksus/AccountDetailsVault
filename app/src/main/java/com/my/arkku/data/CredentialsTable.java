package com.my.arkku.data;

import android.provider.BaseColumns;

public class CredentialsTable {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private CredentialsTable() {}

    // Inner class that defines the table contents
    public static class Entry implements BaseColumns {
        public static final String TABLE_NAME = "credentials";
        public static final String ID = "id";
        public static final String CATEGORY_NAME = "category_name";
        public static final String EMAIL = "email";
        public static final String USERNAME = "username";
        public static final String PASSWORD = "password";
    }
}

