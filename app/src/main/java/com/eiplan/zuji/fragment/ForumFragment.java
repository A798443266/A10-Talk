package com.eiplan.zuji.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.UpdateVideoActivity;
import com.eiplan.zuji.fragment.others.FindDemandFragment;
import com.eiplan.zuji.fragment.others.FindLiveFragment;
import com.eiplan.zuji.fragment.others.FindProblemFragment;
import com.eiplan.zuji.fragment.others.FindvedioFragment;
import com.eiplan.zuji.view.ParentViewPager;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 发现
 */
public class ForumFragment extends BaseFragment {

    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp_find)
    ParentViewPager vpFind;

    private String[] titles = {"问答论坛", "需求广场", "技术视频", "专家直播"};
//    private String[] titles = {"问答论坛", "技术视频", "专家直播"};
    private ArrayList<Fragment> fragments;
    private FindProblemFragment fragment1;
    private FindDemandFragment fragment2;
    private FindvedioFragment fragment3;
    private FindLiveFragment fragment4;

    @Override
    protected void initView() {
        fragments = new ArrayList<>();
        fragment1 = new FindProblemFragment();
        fragment2 = new FindDemandFragment();
        fragment3 = new FindvedioFragment();
        fragment4 = new FindLiveFragment();

//        fragments.add(fragment1);
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);

        vpFind.clearDisappearingChildren();
        tabLayout.setViewPager(vpFind, titles, (FragmentActivity) mContext, fragments);


//        tabLayout.setCurrentTab(2);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_forum;
    }


    @OnClick(R.id.iv_right)
    public void onViewClicked() {
        startActivity(new Intent(mContext, UpdateVideoActivity.class));
    }
}
