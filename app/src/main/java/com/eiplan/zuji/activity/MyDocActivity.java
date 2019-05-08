package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.fragment.others.MeDoc1Fragment;
import com.eiplan.zuji.fragment.others.MeDoc2Fragment;
import com.eiplan.zuji.fragment.others.MeDoc3Fragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyDocActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp_industry)
    ViewPager vpIndustry;

    private String[] titles = new String[]{"我的文档", "收藏的文档", "购买的文档"};
    private ArrayList<Fragment> fragments;
    private MeDoc1Fragment fragment1;
    private MeDoc2Fragment fragment2;
    private MeDoc3Fragment fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doc);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragment1 = new MeDoc1Fragment();
        fragment2 = new MeDoc2Fragment();
        fragment3 = new MeDoc3Fragment();

        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);

        vpIndustry.clearDisappearingChildren();
        tabLayout.setViewPager(vpIndustry, titles, this, fragments);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

//    @OnClick(R.id.iv_updocs)
//    public void onViewClicked1() {
//        startActivity(new Intent(this,UpdateDocActivity.class));
//    }
}
