package com.eiplan.zuji.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.StudyAdapter;
import com.eiplan.zuji.bean.StudyInfo;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.view.MyListViewInCoor;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 课程
 */

public class StudyActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_hot)
    TextView tvHot;
    @BindView(R.id.tv_new)
    TextView tvNew;
    @BindView(R.id.lv)
    MyListViewInCoor lv;

    private StudyAdapter adapter;
    private List<StudyInfo> studys;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study);
        ButterKnife.bind(this);

        initBaner();
        initData();
    }

    private void initData() {
        data = SpUtils.getString(this,Constant.save_study);
        if(!TextUtils.isEmpty(data)){
            studys = JsonUtils.parseStudys(data);
        }

        OkHttpUtils.get().url(Constant.getstudys)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SpUtils.putString(StudyActivity.this,Constant.save_study,response);
                        studys = JsonUtils.parseStudys(response);
                        show();
                    }
                });
    }

    private void show() {
        if (studys != null && studys.size() > 0) {
            adapter = new StudyAdapter(this, studys);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(StudyActivity.this, StudyDtailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("study", studys.get(position));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(this, "网络出现故障了", Toast.LENGTH_SHORT).show();
        }
    }

    private void initBaner() {
        //设置Banner的数据
        //得到图片集合地址
        List<Integer> imagesUrl = new ArrayList<Integer>();
        imagesUrl.add(R.drawable.study1);
        imagesUrl.add(R.drawable.study2);
        imagesUrl.add(R.drawable.study3);
        imagesUrl.add(R.drawable.study4);
        banner.setImages(imagesUrl);
        //设置循环指示点
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置轮播时间
        banner.setDelayTime(3500);
        //设置翻转动画效果
//        banner.setBannerAnimation(Transformer.Tablet);
        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(StudyActivity.this).load(path).into(imageView);
            }
        });
        //设置item的点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(StudyActivity.this, "position==" + position, Toast.LENGTH_SHORT).show();
            }
        });
        banner.start();
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick(R.id.ll_all)
    public void onViewClicked1() {
        startActivity(new Intent(this, AllKindsActivity.class));
//        startActivity(new Intent(this, StudyDtailsActivity.class));

    }

    @OnClick(R.id.tv_mystudy)
    public void onViewClicked2() {
        startActivity(new Intent(this, MyStudyActivity.class));
    }
}
