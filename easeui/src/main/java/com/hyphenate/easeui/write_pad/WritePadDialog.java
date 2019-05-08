package com.hyphenate.easeui.write_pad;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.hyphenate.easeui.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class WritePadDialog extends Dialog {
    private Context mContext;
    private WriteDilogListener mWriteDialogListener;
    private PaintView mPaintView;
    private FrameLayout mFrameLayout;
    private ImageView iv1, iv2, iv3, iv4, iv5;
    private SeekBar seekBar;
    private String path;
    private Bitmap bitmap;


    private int paintWidth;

    public WritePadDialog(Context context, String path, WriteDilogListener writeDialogListener) {
        super(context);
        this.mContext = context;
        this.mWriteDialogListener = writeDialogListener;
        this.path = path;

        if (!TextUtils.isEmpty(path)) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(path);
                bitmap = BitmapFactory.decodeStream(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //无标题
        setContentView(R.layout.write_pad);
        mFrameLayout = (FrameLayout) findViewById(R.id.tablet_view);
        // 获取屏幕尺寸
        DisplayMetrics mDisplayMetrics = new DisplayMetrics();
        getWindow().getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        int screenWidth = mDisplayMetrics.widthPixels;
        int screenHeight = mDisplayMetrics.heightPixels;

        mPaintView = new PaintView(mContext, bitmap, screenWidth, screenHeight);
        mFrameLayout.addView(mPaintView);
        mPaintView.requestFocus();
        iv5 = findViewById(R.id.iv5);
        iv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPaintView.getPath().isEmpty()) {
                    Toast.makeText(mContext, "不能发送空的内容", Toast.LENGTH_SHORT).show();
                    return;
                }
                mWriteDialogListener.onPaintDone(mPaintView.getPaintBitmap());
                dismiss();
            }
        });
        iv3 = findViewById(R.id.iv3);
        iv3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaintView.clear();
            }
        });
        iv4 = findViewById(R.id.iv4);
        iv4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        //橡皮
        iv2 = findViewById(R.id.iv2);
        iv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        findViewById(R.id.iv_blue).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaintView.setPaintColor(Color.BLUE);
            }
        });
        findViewById(R.id.iv_red).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaintView.setPaintColor(Color.RED);
            }
        });
        findViewById(R.id.iv_greed).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaintView.setPaintColor(Color.GREEN);
            }
        });
        findViewById(R.id.iv_blank).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPaintView.setPaintColor(Color.BLACK);
            }
        });
        seekBar = findViewById(R.id.seekbar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekBar.getProgress();
                paintWidth = (int) ((progress * 1.0 / 100) * 50);
                if (paintWidth != 0 && paintWidth > 5)
                    mPaintView.setPaintWidth(paintWidth);
                else
                    mPaintView.setPaintWidth(5);
            }
        });

    }


}

