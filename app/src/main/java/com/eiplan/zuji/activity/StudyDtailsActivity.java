package com.eiplan.zuji.activity;

/**
 * 课程详情页面
 */

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.StudyInfo;
import com.eiplan.zuji.fragment.others.StudyFragment1;
import com.eiplan.zuji.fragment.others.StudyFragment2;
import com.eiplan.zuji.fragment.others.StudyFragment3;
import com.eiplan.zuji.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.hugeterry.coordinatortablayout.CoordinatorTabLayout;
import cn.hugeterry.coordinatortablayout.listener.OnTabSelectedListener;

public class StudyDtailsActivity extends AppCompatActivity {

    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.coordinatortablayout)
    CoordinatorTabLayout coordinatortablayout;

    private StudyInfo study;

    private int[] mImageArray;
    private List<Fragment> fragments = new ArrayList<>();
    private final String[] mTitles = {"课程简介", "课程目录", "课程评价"};

    private int[] mColorArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_study_dtails);
        ButterKnife.bind(this);

        study = (StudyInfo) getIntent().getSerializableExtra("study");
        initView();
    }


    private void initView() {
        initFragments();
        initViewpager();
        //头部的图片数组
        mImageArray = new int[]{
                R.drawable.subject1,
                R.drawable.subject2,
                R.drawable.subject3};

        mColorArray = new int[]{
                android.R.color.holo_blue_light,
                android.R.color.holo_red_light,
                android.R.color.holo_orange_light};

        coordinatortablayout.setTitle("课程详情")
                .setImageArray(mImageArray, mColorArray)
                .setTranslucentStatusBar(this)
                .setBackEnable(true)
                .setupWithViewPager(vp);
    }

    private void initFragments() {
        fragments.add(new StudyFragment1());
        fragments.add(new StudyFragment2());
        fragments.add(new StudyFragment3());
    }

    private void initViewpager() {
        vp.setOffscreenPageLimit(3);
        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), (ArrayList<Fragment>) fragments, mTitles));

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * 适配器
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mFragments = new ArrayList<>();
        private final String[] mTitles;

        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> mFragments, String[] mTitles) {
            super(fm);
            this.mFragments = mFragments;
            this.mTitles = mTitles;
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }
    }
}
