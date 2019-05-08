package com.eiplan.zuji.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.utils.UIUtils;
import com.github.chrisbanes.photoview.PhotoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

//文档预览界面
public class DocViewActivity extends Activity {

    @BindView(R.id.photoview)
    PhotoView photoview;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_view);
        ButterKnife.bind(this);

        path = getIntent().getStringExtra("path");//得到预览地址
        photoview.setBackgroundColor(UIUtils.getColor(android.R.color.black));
        Glide.with(this).load(path).error(R.drawable.ease_default_image).into(photoview);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
