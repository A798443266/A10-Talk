package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.fragment.others.MySubjectFragment1;
import com.eiplan.zuji.fragment.others.MySubjectFragment2;
import com.eiplan.zuji.fragment.others.MySubjectFragment3;
import com.eiplan.zuji.view.MoreWindow1;
import com.flyco.tablayout.SlidingTabLayout;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * 学习课程的页面
 */
public class LookSubjectActivity extends AppCompatActivity {

    @BindView(R.id.videoplayer)
    JZVideoPlayerStandard videoplayer;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private String[] titles = {"章节", "详情", "评价"};
    private ArrayList<Fragment> fragments;
    private MySubjectFragment1 fragment1;
    private MySubjectFragment2 fragment2;
    private MySubjectFragment3 fragment3;

    private String playUrl = "http://luo.easy.echosite.cn/A13/static/live/2.mp4";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_look_subject);
        ButterKnife.bind(this);

        fragments = new ArrayList<>();
        fragment1 = new MySubjectFragment1();
        fragment2 = new MySubjectFragment2();
        fragment3 = new MySubjectFragment3();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);

        vp.clearDisappearingChildren();
        tabLayout.setViewPager(vp, titles, this, fragments);

        videoplayer.setUp(playUrl, JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "ANSYS初级教程7");
        Glide.with(this).load(R.drawable.icon_mystudy1).into(videoplayer.thumbImageView);
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @OnClick(R.id.fab)
    public void onViewClicked() {
        MoreWindow1 mMoreWindow = new MoreWindow1(this);
        mMoreWindow.init();
        mMoreWindow.showMoreWindow(fab, 0);
    }
}
