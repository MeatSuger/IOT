package com.example.chapter10;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity implements SeekBar.OnSeekBarChangeListener {


    protected int rID;
    private int gID;
    private int bID;
    private TextView tvR;
    private TextView tvG;
    private TextView tvB;
    private SeekBar sbR;
    private SeekBar sbG;
    private SeekBar sbB;
    private Bitmap mSrc;
    private ColorMatrix mColorMatrix;
    private ImageView myview;
    private TextView rgBchanelvalue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvR = findViewById(R.id.textViewR);
        tvG = findViewById(R.id.textViewG);
        tvB = findViewById(R.id.textViewB);
        sbR = findViewById(R.id.seekBarR);
        sbG = findViewById(R.id.seekBarG);
        sbB = findViewById(R.id.seekBarB);
        rgBchanelvalue = findViewById(R.id.textView6);
        sbR.setOnSeekBarChangeListener(this);
        sbG.setOnSeekBarChangeListener(this);
        sbB.setOnSeekBarChangeListener(this);
        myview = findViewById(R.id.imageView);
        mSrc = BitmapFactory.decodeResource(getResources(), R.drawable.img);
        mColorMatrix = new ColorMatrix();
        mColorMatrix.reset();
        rID = sbR.getId();
        gID = sbG.getId();
        bID = sbB.getId();

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        tvR.setText("红  (R)  " );
        tvG.setText("绿  (G)  " );
        tvB.setText("蓝  (B)  " );
        transformImageColor(sbR.getProgress(), sbG.getProgress(), sbB.getProgress());
        rgBchanelvalue.setText(String.format("(%s,%s,%s)", sbR.getProgress(), sbG.getProgress(), sbB.getProgress()));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    /**
     * 无法使用
     * USELESS
     */

    private void initialize(int a, int value) {
        Bitmap dstBp = Bitmap.createBitmap(mSrc.getWidth(), mSrc.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(dstBp);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        float[] matrix = new float[]{
                1, 0, 0, 0, 0, // 红色通道
                0, 1, 0, 0, 0, // 绿色通道
                0, 0, 1, 0, 0, // 蓝色通道
                0, 0, 0, 1, 0, // 透明度通道
        };

        if (a == rID) {
            transformImageColor(value, 0, 0);
        } else if (a == gID) {
            transformImageColor(0, value, 0);
        } else if (a == bID) {
            transformImageColor(0, 0, value);
        }
    }

    /**
     * @param r 红色值
     * @param g 绿色值
     * @param b 蓝色值
     * @description 定义一个图片颜色修改的方法，参数r、g、b分别表示红色、绿色和蓝色
     */
    // 定义一个图片颜色修改的方法，参数r、g、b分别表示红色、绿色和
    // 蓝色通道的亮度
    public void transformImageColor(int r, int g, int b) {

        // 从资源中解码图片，得到Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img);

        // 创建一个ColorMatrix对象，并设置其值为一个4x5的浮点数组。这个数组表示了颜色的变换矩阵
        // 这里的变换是将红色、绿色和蓝色通道的亮度分别设置为参数r、g、b，透明度通道不变
        ColorMatrix colorMatrix = new ColorMatrix(new float[]{
                1, 0, 0, 0, r, // 红色通道亮度
                0, 1, 0, 0, g, // 绿色通道亮度
                0, 0, 1, 0, b, // 蓝色通道亮度
                0, 0, 0, 1, 0  // 透明度保持不变
        });

        // 创建一个ColorMatrixColorFilter对象，其颜色变换矩阵为上面创建的colorMatrix
        ColorMatrixColorFilter colorMatrixColorFilter = new ColorMatrixColorFilter(colorMatrix);

        // 创建一个新的Bitmap对象，其宽度和高度与原bitmap相同，颜色配置为ARGB_8888（32位真彩色）
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        // 创建一个Canvas对象，其画布为新创建的Bitmap对象
        Canvas canvas = new Canvas(newBitmap);

        // 创建一个Paint对象，其颜色变换滤镜为上面创建的colorMatrixColorFilter
        Paint paint = new Paint();
        paint.setColorFilter(colorMatrixColorFilter);

        // 在canvas上画出原bitmap，使用变换后的颜色
        canvas.drawBitmap(bitmap, 0, 0, paint);

        // 将新的Bitmap对象设置为myview的图片
        myview.setImageBitmap(newBitmap);
    }
}
















