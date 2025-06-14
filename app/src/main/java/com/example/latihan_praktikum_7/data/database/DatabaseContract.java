package com.example.latihan_praktikum_7.data.database;

import android.provider.BaseColumns;

public class DatabaseContract {
    private DatabaseContract() {}

    public static class CatEntry implements BaseColumns {
        public static final String TABLE_NAME = "cats";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_ORIGIN = "origin";
        public static final String COLUMN_DESCRIPTION = "description";
    }
}