package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.fragment.others.MeExpert1Fragment;
import com.eiplan.zuji.fragment.others.MeExpert2Fragment;
import com.eiplan.zuji.fragment.others.MeExpert3Fragment;
import com.eiplan.zuji.utils.UIUtils;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyExpertActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.titlebar_bg)
    RelativeLayout titlebarBg;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp_industry)
    ViewPager vpIndustry;

    private String[] titles = new String[]{"进行中", "待评价", "已结束"};
    private ArrayList<Fragment> fragments;
    private MeExpert1Fragment meExpert1;
    private MeExpert2Fragment meExpert2;
    private MeExpert3Fragment meExpert3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_expert);
        ButterKnife.bind(this);


        fragments = new ArrayList<>();
        meExpert1 = new MeExpert1Fragment();
        meExpert2 = new MeExpert2Fragment();
        meExpert3 = new MeExpert3Fragment();

        tvRight.setVisibility(View.GONE);
        tvTitle.setText("我的专家");
        tvTitle.setTextColor(UIUtils.getColor(R.color.color_system_blank));
        titlebarBg.setBackgroundColor(UIUtils.getColor(R.color.color_white));
        ivBack.setImageResource(R.drawable.icon_back);

        initView();
    }

    private void initView() {

        fragments.add(meExpert1);
        fragments.add(meExpert2);
        fragments.add(meExpert3);
        vpIndustry.clearDisappearingChildren();
        tabLayout.setViewPager(vpIndustry, titles, this, fragments);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
