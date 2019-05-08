package com.eiplan.zuji.fragment.others;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.ReleaseDemandActivity;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.view.MyViewPager;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 需求
 */

public class FindDemandFragment extends BaseFragment {
    @BindView(R.id.tab_layout)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    MyViewPager viewpager;

    private String[] titles = {"寻求中", "已确定"};
    private ArrayList<Fragment> fragments;
    private DemandFragment fragment1;
    private DemandFragment1 fragment2;

    @Override
    protected void initView() {
        fragments = new ArrayList<>();
        fragment1 = new DemandFragment();
        fragment2 = new DemandFragment1();

        fragments.add(fragment1);
        fragments.add(fragment2);

        MyPagerAdapter adapter = new MyPagerAdapter(getChildFragmentManager());
        viewpager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewpager);
        viewpager.setOffscreenPageLimit(2);

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragmemt_find_demand;
    }


    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        startActivity(new Intent(mContext, ReleaseDemandActivity.class));
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
