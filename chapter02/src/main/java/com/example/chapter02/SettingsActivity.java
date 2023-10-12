package com.example.chapter02;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toolbar;

import com.example.chapter02.util.DBHelper;
import com.example.chapter02.util.DBoperate;

public class SettingsActivity
        extends AppCompatActivity
        implements View.OnClickListener {

    private ImageButton btn_save;
    private ImageButton btn_cancle;
    private EditText ed_cloud_IP;
    private EditText ed_cloud_com;
    private EditText ed_cloud_user;
    private EditText ed_cloud_pwd;
    private EditText ed_proj_ID;
    private EditText ed_NET_ID;
    private EditText ed_temp_api;
    private EditText ed_hum_api;
    private EditText ed_light_api;
    private EditText ed_human_api;
    public static final String ROOM_NAME = "cloud.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("设置");
        }
        findViewByIDme();
        btn_save.setOnClickListener(this);
        btn_cancle.setOnClickListener(this);


    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent();
        intent.setClass(SettingsActivity.this, MainActivity.class);
        setIntent(intent);
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            //连接云平台的后续操作，若未连接，则显示未连接
            DBoperate dBoperate = new DBoperate();


        }
    }

    private void findViewByIDme() {
        ed_cloud_IP = findViewById(R.id.ed01);
        ed_cloud_com = findViewById(R.id.ed02);
        ed_cloud_user = findViewById(R.id.ed03);
        ed_cloud_pwd = findViewById(R.id.ed04);
        ed_proj_ID = findViewById(R.id.ed05);
        ed_NET_ID = findViewById(R.id.ed06);
        ed_temp_api = findViewById(R.id.ed07);
        ed_hum_api = findViewById(R.id.ed08);
        ed_light_api = findViewById(R.id.ed09);
        ed_human_api = findViewById(R.id.ed10);
        btn_save = findViewById(R.id.btn_save);
        btn_cancle = findViewById(R.id.btn_cancle);
    }
    /**
     * 创建并获取写数据库对象
     */
    public SQLiteDatabase getOpenDBobj() {
        DBHelper dbHelper = new DBHelper(SettingsActivity.this, ROOM_NAME, null, 1);
        SQLiteDatabase writableDatabase = dbHelper.getWritableDatabase();
        return writableDatabase;
    }
}