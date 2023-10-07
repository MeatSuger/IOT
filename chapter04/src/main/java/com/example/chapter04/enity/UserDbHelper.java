package com.example.chapter04.enity;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UserDbHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "rfid.db";
    public static final String DB_VERSION = "1";
    public static final String TABLE_NAME = "rfid_info";
    private SQLiteDatabase mDB = null;
    private static UserDbHelper mHelper = null;

    private UserDbHelper(Context context) {
        super(context, DB_NAME, null, Integer.parseInt(DB_VERSION));
    }

    private UserDbHelper(Context context, int version) {
        super(context, DB_NAME, null, version);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String drop_sql = " DROP TABLE IF EXISTS " + TABLE_NAME + ";";
        sqLiteDatabase.execSQL(drop_sql);
        String create_sql = " CREATE TABLE " + TABLE_NAME + "(" +
                " rfid  INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL ," +
                " name TEXT NOT NULL," +
                " price REAL NOT NULL," +
                " bar_code FLOAT  " +
                ");";
        sqLiteDatabase.execSQL(create_sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static UserDbHelper getInstance(Context context, int version) {
        if (version > 0 && mHelper == null) {
            mHelper = new UserDbHelper(context, version);
        } else if (mHelper == null) {
            mHelper = new UserDbHelper(context);
        }
        return mHelper;
    }

    public static UserDbHelper getInstance(Context context) {
        if (mHelper == null) {
            mHelper = new UserDbHelper(context);
        }
        return mHelper;
    }

    public SQLiteDatabase openReadLink() {
        if (mDB == null || !mDB.isOpen()) {
            mDB = mHelper.getReadableDatabase();
        }
        return mDB;
    }

    public SQLiteDatabase openWriteLink() {
        if (mDB == null || !mDB.isOpen()) {
            mDB = mHelper.getWritableDatabase();
        }
        return mDB;
    }

    public void closeLink() {
        mDB.close();
        mDB = null;
    }

}
