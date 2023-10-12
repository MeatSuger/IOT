package com.example.chapter02.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.chapter02.SettingsActivity;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 数据库操作类
 * @author YuHao
 * @create 2023/10/12 21:00
 **/
public class DBoperate  extends SettingsActivity{

    private Cursor cursor;
    private SQLiteDatabase writableDatabase;



    /**
     *
     * @param str   查询的对象
     * @return 查询到的的结果
     */
    public  String SelectDBStoke(String str) {

        /*获取数据库写对象*/
        if (writableDatabase  == null) {
           getOpenDBobj();
        }
        try{
            cursor = writableDatabase.query("censer", new String[]{str}, String.format("%s IS NOT NULL", str), null, null, null, null, null);

        if(cursor.moveToFirst()) {
            @SuppressLint("Range")
            String stoke = cursor.getString(cursor.getColumnIndex(str));
            cursor.close();
            return stoke;
        }
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
    return null;
    }
    /**
     *更新键值对
     * @param key 键
     * @param key_value 值
     * @return
     * @author YuHao
     * @create 2023/10/12 20:52
     **/
    public void RefreshDB_Obj(String key,String key_value){
        if (writableDatabase  == null) {
            getOpenDBobj();
        }
        ContentValues values = new ContentValues();
        values.put(key,key_value);
        writableDatabase.update("censer",values,null,null);
    }
    /**
     *
     * @param response 登录后返回的JSON字符串
     * @return 解析出response里的token
     */
    public String praseToken(String response){
        try {
            JSONObject cashes = new JSONObject(response);
            String cash = cashes.getString("ResultObj");
            JSONObject jsonObject = new JSONObject(cash);
            String STOKE = jsonObject.getString("AccessToken");
            return STOKE;
        }catch (JSONException e){
            throw new RuntimeException(e);
        }
    }
}