package com.example.chapter02;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.chapter02.util.DBHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class SettingsActivity
        extends AppCompatActivity
        implements View.OnClickListener {
    public static final String ROOM_NAME = "cloud.db";
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
    protected DBHelper dbHelper;
    private SQLiteDatabase writableDatabase;
    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        dbHelper = new DBHelper(SettingsActivity.this, ROOM_NAME, null, 1);
        shared = getSharedPreferences("share", MODE_PRIVATE);
        editor = shared.edit();
        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("设置");
        }
        findViewByIDme();
        setOnClickListenerByMe();


    }


    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent();
        intent.setClass(SettingsActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            editor.putString("ed_cloud_IP",  ed_cloud_IP.getText().toString());
            editor.putString("ed_cloud_com", ed_cloud_com.getText().toString());
            editor.putString("ed_cloud_user" ,ed_cloud_user.getText().toString());
            editor.putString("ed_cloud_pwd", ed_cloud_pwd.getText().toString());
            editor.putString("ed_proj_ID",   ed_proj_ID.getText().toString());
            editor.putString("ed_NET_ID",    ed_NET_ID.getText().toString());
            editor.putString("ed_temp_api",  ed_temp_api.getText().toString());
            editor.putString("ed_hum_api",   ed_hum_api.getText().toString());
            editor.putString("ed_light_api", ed_light_api.getText().toString());
            editor.putString("ed_human_api", ed_human_api.getText().toString());
            editor.commit();
            String s = shared.getString("ed_cloud_IP", "");
            Log.d(TAG, "onClick: test SharedPreferences" + s);
            //连接云平台的后续操作，若未连接，则显示未连接

            ProgressDialog loginMission = new ProgressDialog(this);
            loginMission.setMessage("登录中");
            loginMission.show();
            login();
            while (true) {
                if (shared.getBoolean("token_state", false)) {
                    loginMission.closeOptionsMenu();
                    break;
                } else {
                    Toast.makeText(SettingsActivity.this, "登录失败，请检查密码或用户名是否正确", Toast.LENGTH_SHORT).show();
                }
                break;
            }
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

    private void setOnClickListenerByMe() {
        btn_cancle.setOnClickListener(this);
        btn_save.setOnClickListener(this);
    }

    void login() {
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    // 创建OkHttpClient实例，用于发送HTTP请求
                    OkHttpClient client = new OkHttpClient();

                    // 构建请求体，包含账户和密码信息
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Account", "19033822991")
                            .add("Password", "yuhao1125")
                            .build();

                    // 构建请求对象，指定URL和请求方法，并携带请求体
                    Request request = new Request.Builder()
                            .url("http://api.nlecloud.com/Users/Login")
                            .post(requestBody)
                            .build();

                    // 使用OkHttpClient实例执行请求，并获取响应对象
                    Response response = client.newCall(request).execute();

                    // 从响应对象中获取响应体
                    ResponseBody responseBody = response.body();

                    // 判断响应体是否为空，如果为空则抛出异常
                    assert responseBody != null;

                    // 将响应体内容转换为字符串
                    String str = responseBody.string();
                    String token = praseToken(str);
                    editor.putString("token",  token);
                    if(token != null){
                        editor.putBoolean("token_state",true);
                    }else {
                        editor.putBoolean("token_state",false);
                    }
                    editor.commit();
                    Log.d(TAG, "return token: " + token);
                } catch (IOException e) {
                    // 如果发生IO异常，将其转换为运行时异常并抛出
                    throw new RuntimeException(e);

                }

            }
        }).start(); // 启动线程，开始执行登录操作

    }

    /**
     * 更新键值对
     *
     * @param key       键
     * @param key_value 值
     * @author YuHao
     * @create 2023/10/12 20:52
     **/

    public void RefreshDB_Obj(String key, String key_value) {
        if (writableDatabase == null) {
            writableDatabase = dbHelper.getWritableDatabase();
        }
        ContentValues values = new ContentValues();
        values.put(key, key_value);
        writableDatabase.update("censer", values, null, null);
    }

    /**
     * @param response 登录后返回的JSON字符串
     * @return 解析出response里的token
     */
    public String praseToken(String response) {
        try {
            JSONObject cashes = new JSONObject(response);
            String cash = cashes.getString("ResultObj");
            JSONObject jsonObject = new JSONObject(cash);
            return jsonObject.getString("AccessToken");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}


class DBoperate extends SettingsActivity {

    private Cursor cursor;
    private SQLiteDatabase writableDatabase;


    /**
     * @param str 查询的对象
     * @return 查询到的的结果
     */
    public String SelectDBStoke(String str) {
        dbHelper.onCreate(writableDatabase);
        /*获取数据库写对象*/
        if (writableDatabase == null) {

            writableDatabase = dbHelper.getWritableDatabase();

        }
        try {
            cursor = writableDatabase.query("censer", new String[]{str}, String.format("%s IS NOT NULL", str), null, null, null, null, null);

            if (cursor.moveToFirst()) {
                @SuppressLint("Range")
                String stoke = cursor.getString(cursor.getColumnIndex(str));
                cursor.close();
                return stoke;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }


}