package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.utils.UIUtils;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 大图查看页面
 */

public class BigPhotoActivity extends AppCompatActivity {

    @BindView(R.id.photoview)
    PhotoView photoview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_photo);
        ButterKnife.bind(this);

        //隐藏顶部的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        String path = getIntent().getStringExtra("path");

        photoview.setBackgroundColor(UIUtils.getColor(android.R.color.black));
        Glide.with(this).load(path).error(R.drawable.ease_default_image).into(photoview);
    }

    @OnClick(R.id.rl_back)
    public void onViewClicked() {
        finish();
    }
}
