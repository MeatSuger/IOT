package com.example.chapter06;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.nle.mylibrary.forUse.zigbee.ZigBee;
import com.nle.mylibrary.transfer.ConnectResultListener;
import com.nle.mylibrary.transfer.DataBusFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class MainActivity extends AppCompatActivity {
    private static final String ROOM_NAME = "stokeData.db";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ZigBee zigbee = new ZigBee(DataBusFactory.newSerialDataBus(2000,1200 ),

                new ConnectResultListener() {
            @Override
            public void onConnectResult(boolean b) {
                if (!b) {
                    Toast.makeText(MainActivity.this,"FAILE",Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(MainActivity.this,"SUCCESS",Toast.LENGTH_SHORT).show();
            }
        });
        try {
            zigbee.getLight();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
    private void login() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("Account", "19033822991")
                            .add("Password", "yuhao1125")
                            .build();
                    Request request = new Request.Builder()
                            .url("http://api.nlecloud.com/Users/Login")
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    ResponseBody responseBody = response.body();
                    assert responseBody != null;
                    String str = responseBody.string();
                    System.out.println(str);
                    String adasda = praseToken(str);
                    RenameDB(adasda);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            }
        }).start();


    }
    private void RenameDB(String stoke) {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(MainActivity.this, ROOM_NAME, null, 1);
        SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("stoke", stoke);

        db.update("stoke", values, null, null);
//        db.close();
    }

    private String SelectDB() {
        DBOpenHelper dbOpenHelper = new DBOpenHelper(MainActivity.this, ROOM_NAME, null, 1);
        SQLiteDatabase sqLiteDatabase = dbOpenHelper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("stoke", new String[]{"stoke"}, "stoke IS NOT NULL", null, null, null, null);
        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String stoke = cursor.getString(cursor.getColumnIndex("stoke"));
            cursor.close();
            return stoke;
        } else {
            cursor.close();
            return null;
        }
    }
    private String praseToken(String response) {
        try {
            JSONObject cashes = new JSONObject(response);
            String cash = cashes.getString("ResultObj");
            JSONObject jsonObject = new JSONObject(cash);
            String STOKE = jsonObject.getString("AccessToken");
            return STOKE;
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}