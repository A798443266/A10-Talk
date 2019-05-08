package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.eiplan.zuji.R;
import com.eiplan.zuji.fragment.others.MeDemandFragment1;
import com.eiplan.zuji.fragment.others.MeDemandFragment2;
import com.eiplan.zuji.fragment.others.MeExpert1Fragment;
import com.eiplan.zuji.fragment.others.MeExpert2Fragment;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的问答
 */

public class MyDemandsActivity extends AppCompatActivity {

    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;

    private String[] titles = new String[]{"进行中", "已解决"};
    private ArrayList<Fragment> fragments;
    private MeDemandFragment1 fragment1;
    private MeDemandFragment2 fragment2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_demands);
        ButterKnife.bind(this);

        fragments = new ArrayList<>();
        fragment1 = new MeDemandFragment1();
        fragment2 = new MeDemandFragment2();
        fragments.add(fragment1);
        fragments.add(fragment2);

        vp.clearDisappearingChildren();
        tabLayout.setViewPager(vp, titles, this, fragments);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
