package com.example.chapter09;

import static android.content.ContentValues.TAG;


import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {
    private static final String ROOM_NAME = "stokeData.db";

    private TextView tv_temp;
    private TextView tv_hum;
    public static final String ZigBee_temp = "z_temperature";
    public static final String ZigBee_hum = "z_humidity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_temp = findViewById(R.id.tv1);
        tv_hum = findViewById(R.id.tv2);
        login();
        getvalue(ZigBee_temp);
        getvalue(ZigBee_hum);
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

    private void getvalue(String ApiTag) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    String stoke = SelectDB();
                    if (stoke == null) {
                        stoke = "88FCBD5C26062D7C81FBBBC85C7B2295B407EAB0D0FD43CAD203B22E43B003B9BC6BE75E6D9EF63D31F37BCA070ED0DEA5C959CE0257ADC4998717BDE0D2666D067EED425A5EFDF757316D333BE39BDE43AA8E0CDEC9C58BAFBBAA6BC0B4AF90A9A44C48E3DA2E0D0D5FA9827D6110277E095A680911D5ECCFB067CF020145B1D3DAAC42A76860D310FF959B3D9B9B9294CA4F4EABBA458F3B53B7590B4FBD2AB09510D0DF0DB224D1041185C01A257A95CD08159A9244CE348E648886605E3288F7A17E0169FCF1ACA270C81C547CF0BC77A9BEE75DA198E02D2A695EB1C6C9";
                    }
                    OkHttpClient client = new OkHttpClient();


                    HttpUrl.Builder urlbulider = HttpUrl
                            .parse("http://api.nlecloud.com/devices/{deviceId}/Datas/Grouping")
                            .newBuilder();
                    urlbulider.addQueryParameter("deviceId", "795154");
                    urlbulider.addQueryParameter("ApiTags", ApiTag);
                    urlbulider.addQueryParameter("StartDate", getNowTime(5));

                    System.out.println(stoke);
                    Request request = new Request.Builder()
                            .url(urlbulider.build())
                            .header("AccessToken", stoke)
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        String result = response.body().string();
                        Log.d(TAG, "run: " + result);
                        JSONObject jsonObject = new JSONObject(result);
                        JSONObject resultObj = jsonObject.getJSONObject("ResultObj");
                        JSONArray dataPoints = resultObj.getJSONArray("DataPoints");
                        if (dataPoints.length() > 0) {
                            JSONObject firstDataPoint = dataPoints.getJSONObject(0);
                            JSONArray pointDTOs = firstDataPoint.getJSONArray("PointDTO");
                            JSONObject firstPointDTO = pointDTOs.getJSONObject(0);
                            double value = firstPointDTO.getDouble("Value");

                            if (ApiTag.equals(ZigBee_temp)) {
                                RefreshView_temp("温度\n" + String.valueOf(value) + "℃");
                            } else if (ApiTag.equals(ZigBee_hum)) {
                                RefreshView_hum("湿度\n" + String.valueOf(value) + "%RH");
                            }
                            Log.d(TAG, "run: " + value);
                        } else {

                            continue;
                        }
                        Thread.sleep(1000);
                    } catch (InterruptedException | IOException | JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

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

    private String getNowTime(long nSecondsBefore) {
        String formatDateTime = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDateTime timeBefore = LocalDateTime.now().minusSeconds(nSecondsBefore);
            LocalDateTime time = timeBefore.plusSeconds(nSecondsBefore);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            formatDateTime = time.format(formatter);

        }
        Log.d(TAG, "getNowTime: " + formatDateTime);
        return formatDateTime;
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

    private void RefreshView_temp(String temp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_temp.setText(temp);
            }
        });
    }

    private void RefreshView_hum(String hum) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_hum.setText(hum);
            }
        });
    }

}