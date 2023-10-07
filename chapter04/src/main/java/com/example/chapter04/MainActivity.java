package com.example.chapter04;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.chapter04.enity.MTTSDEMO;
import com.example.chapter04.enity.UserDbHelper;
import com.nle.mylibrary.claimer.rfid.SingleEpcListener;
import com.nle.mylibrary.databus.DataBusFactory;
import com.nle.mylibrary.device.RFID;
import com.nle.mylibrary.device.listener.ConnectResultListener;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, TextToSpeech.OnInitListener {

    private TextView tv1;
    private TextView tv2;
    private EditText et1;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;
    private TextView tv7;
    private boolean b;
    private TextToSpeech mTTS;
    private Context mContext;
    private UserDbHelper mHelper;
    private View btn1;
    private MTTSDEMO mttsdemo;
    private String oldone = null;
    private String newone = null;
    private boolean c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv1 = findViewById(R.id.tv_1);
        tv2 = findViewById(R.id.tv_2);
        et1 = findViewById(R.id.et_1);
        tv3 = findViewById(R.id.tv_3);
        tv4 = findViewById(R.id.tv_4);
        tv5 = findViewById(R.id.tv_5);
        tv6 = findViewById(R.id.tv_6);
        tv7 = findViewById(R.id.tv_7);
        btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(this);
        mttsdemo = new MTTSDEMO(this);
        mTTS = new TextToSpeech(this, this);
        Productinfo productinfo1;
        Productinfo productinfo2;
        Productinfo productinfo3;
        productinfo1 = new Productinfo("E2 00 41 48 53 16 02 11 26 90 0A 27", "华为", "3999.99");
        productinfo2 = new Productinfo("E2 00 20 75 58 13 02 64 20 90 3B C1", "小米", "2999.99");
        productinfo3 = new Productinfo("E2 00 51 80 03 08 02 67 11 40 9C F9", "IPhone", "8999.99");
        Map map = new HashMap();
        map.put(productinfo1.rfid, productinfo1);
        map.put(productinfo2.rfid, productinfo2);
        map.put(productinfo3.rfid, productinfo3);


        RFID rfid;
        rfid = new RFID(DataBusFactory.newSocketDataBus("172.18.30.15", 6001), new ConnectResultListener() {
            @Override
            public void onConnectResult(boolean b) {
                if (!b) {
                    return;
                }
                Toast.makeText(MainActivity.this, "链接成功", Toast.LENGTH_SHORT).show();
            }
        });
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        rfid.readSingleEpc(new SingleEpcListener() {
                            @Override
                            public void onVal(String s) {
                                et1.setText(s);
                                b = (s.equals(productinfo1.rfid) || s.equals(productinfo2.rfid) || s.equals(productinfo3.rfid));
                                if (b) {
                                    changer(s);
                                    Productinfo info = (Productinfo) map.get(s);
                                    tv4.setText(info.name);
                                    tv6.setText(info.price);
                                } else {
                                    tv4.setText("");
                                    tv6.setText("");
                                }
                            }

                            @Override
                            public void onFail(Exception e) {
                                Log.d(TAG, "读取失败");
                            }
                        });

                        Thread.sleep(3000);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (!c) {
                        mTTS.speak("商品" + tv4.getText() + "价格" + tv6.getText() + "", TextToSpeech.QUEUE_FLUSH, null );
                    }
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();

    }

    @Override
    protected void onStart() {
        super.onStart();
        mHelper = UserDbHelper.getInstance(this);
        mHelper.openReadLink();
        mHelper.openWriteLink();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mHelper.closeLink();
    }


    @Override
    public void onClick(View view) {
                mTTS.speak("商品" + tv4.getText() + "价格" + tv6.getText(), TextToSpeech.QUEUE_FLUSH, null);
    }


    @Override
    public void onInit(int i) {
        if (i == TextToSpeech.SUCCESS) {
        }
        mTTS.setOnUtteranceProgressListener(new UtteranceProgressListener() {
            @Override
            public void onStart(String s) {

            }

            @Override
            public void onDone(String s) {
                mTTS.stop();
            }


            @Override
            public void onError(String s) {

            }
        });
    }

    public void changer(String rfid) {

        if (newone == null) {
            newone = rfid;
            c = true;
        } else {
            newone = rfid;
            if (newone.equals(oldone)) {
                c = true;
            } else {
                c = false;
            }
        }
        oldone = newone;
    }
}