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
import com.eiplan.zuji.fragment.others.MeCustomerFragment1;
import com.eiplan.zuji.fragment.others.MeCustomerFragment2;
import com.eiplan.zuji.fragment.others.MeCustomerFragment3;
import com.eiplan.zuji.fragment.others.MeDoc1Fragment;
import com.eiplan.zuji.fragment.others.MeDoc2Fragment;
import com.eiplan.zuji.fragment.others.MeDoc3Fragment;
import com.eiplan.zuji.utils.UIUtils;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的客户
 */

public class MyCustomerActivity extends AppCompatActivity {

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

    private String[] titles = new String[]{"客户预约", "发送订单", "支付状态"};
    private ArrayList<Fragment> fragments;
    private MeCustomerFragment1 fragment1;
    private MeCustomerFragment2 fragment2;
    private MeCustomerFragment3 fragment3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_customer);
        ButterKnife.bind(this);

        tvRight.setVisibility(View.GONE);
        tvTitle.setText("我的客户");
        tvTitle.setTextColor(UIUtils.getColor(R.color.color_system_blank));
        titlebarBg.setBackgroundColor(UIUtils.getColor(R.color.color_white));
        ivBack.setImageResource(R.drawable.icon_back);

        initData();
    }

    private void initData() {
        fragments = new ArrayList<>();
        fragment1 = new MeCustomerFragment1();
        fragment2 = new MeCustomerFragment2();
        fragment3 = new MeCustomerFragment3();

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
}
