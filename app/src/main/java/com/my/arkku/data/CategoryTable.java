package com.my.arkku.data;

import android.provider.BaseColumns;

public final class CategoryTable {
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private CategoryTable() {}

    // Inner class that defines the table contents
    public static class Entry implements BaseColumns {
        public static final String TABLE_NAME = "categories";
        public static final String NAME = "category_name";
    }
}
