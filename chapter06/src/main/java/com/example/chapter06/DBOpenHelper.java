package com.example.chapter06;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper{

        public DBOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            String CREATE_STOKE = "CREATE TABLE \"main\".\"stoke\" (\"stoke\" TEXT)";
            String INSERT_TABLE = "INSERT into stoke(stoke) values (null)";
            db.execSQL(CREATE_STOKE);
            db.execSQL(INSERT_TABLE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
