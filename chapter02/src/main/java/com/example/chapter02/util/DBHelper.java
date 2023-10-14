package com.example.chapter02.util;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_STOKE = "CREATE TABLE censer (stoke TEXT, ed_cloud_IP TEXT, ed_cloud_com TEXT, ed_cloud_user TEXT , ed_cloud_pwd TEXT, ed_proj_ID TEXT,  ed_NET_ID TEXT , ed_temp_api TEXT,ed_light_api TEXT, ed_hum_api TEXT,  ed_human_api  TEXT)";
        String INSERT_TABLE = "INSERT into censer(stoke) values (null)";
        db.execSQL(CREATE_STOKE);
        db.execSQL(INSERT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

