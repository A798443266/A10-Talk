package com.eiplan.zuji.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.eiplan.zuji.fragment.ConsultFragment;
import com.eiplan.zuji.fragment.ExpertFragment;
import com.eiplan.zuji.fragment.ForumFragment;
import com.eiplan.zuji.fragment.HomePageFragment;
import com.eiplan.zuji.fragment.MeFragment;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends FragmentActivity {

    @BindView(R.id.fl_main)
    FrameLayout fl_main;
    @BindView(R.id.iv_main_home)
    ImageView ivMainHome;
    @BindView(R.id.tv_main_home)
    TextView tvMainHome;
    @BindView(R.id.ll_main_home)
    LinearLayout llMainHome;
    @BindView(R.id.iv_main_expert)
    ImageView ivMainExpert;
    @BindView(R.id.tv_main_expert)
    TextView tvMainExpert;
    @BindView(R.id.ll_main_expert)
    LinearLayout llMainExpert;
    @BindView(R.id.iv_main_forum)
    ImageView ivMainForum;
    @BindView(R.id.tv_main_forum)
    TextView tvMainForum;
    @BindView(R.id.ll_main_forum)
    LinearLayout llMainForum;
    @BindView(R.id.iv_main_consult)
    ImageView ivMainConsult;
    @BindView(R.id.tv_main_consult)
    TextView tvMainConsult;
    @BindView(R.id.ll_main_consult)
    LinearLayout llMainConsult;
    @BindView(R.id.iv_main_me)
    ImageView ivMainMe;
    @BindView(R.id.tv_main_me)
    TextView tvMainMe;
    @BindView(R.id.ll_main_me)
    LinearLayout llMainMe;

    private HomePageFragment homePageFragment;
    private ExpertFragment expertFragment;
    private ForumFragment forumFragment;
    private ConsultFragment consultFragment;
    private MeFragment meFragment;

    FragmentTransaction transaction;//事务
    private int isExpert = 0;//是否是专家 1为专家 0为普通用户


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initData();
        int i = UIUtils.px2dp(60);
        int i1 = UIUtils.px2dp(8);
        int i2 = UIUtils.px2dp(24);
        Log.e("TAG", i + " " + i1 + " " + i2);
        //申请权限
        MainActivityPermissionsDispatcher.requestPermissionWithPermissionCheck(MainActivity.this);
    }

    private void initData() {
        isExpert = SpUtils.getInt(this, Constant.current_isexpert);
        EventBus.getDefault().register(this);//注册eventbus时间
        //默认显示首页
        setSelect(0);

    }

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent(Integer tab) {
        //显示消息
        setSelect(tab);
    }

    @OnClick({R.id.ll_main_home, R.id.ll_main_expert, R.id.ll_main_forum, R.id.ll_main_consult, R.id.ll_main_me})
    //提供相应的fragment的显示
    public void showTab(View view) {
        switch (view.getId()) {
            case R.id.ll_main_home://首页
                setSelect(0);
                break;
            case R.id.ll_main_expert://专家
                setSelect(1);
                break;
            case R.id.ll_main_forum://发现
                setSelect(2);
                break;
            case R.id.ll_main_consult://咨询
                setSelect(3);
                break;
            case R.id.ll_main_me://我
                setSelect(4);
                break;
        }
    }

    public void setSelect(int select) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        transaction = fragmentManager.beginTransaction();
        //隐藏fragment
        hideFragment();
        //重置颜色
        reSetResourse();
        switch (select) {
            case 0:
                if (homePageFragment == null) {
                    homePageFragment = new HomePageFragment();//创建对象之后，并不会马上调用生命周期方法，而是在commit之后才调用
                    transaction.add(R.id.fl_main, homePageFragment);
                }
                //选中改变字体和图标的颜色
                if (isExpert == 1) {//专家
                    ivMainHome.setImageResource(R.drawable.icon_home11);
                    tvMainHome.setTextColor(UIUtils.getColor(R.color.bottom_text_select1));
                } else {
                    ivMainHome.setImageResource(R.drawable.icon_home1);
                    tvMainHome.setTextColor(UIUtils.getColor(R.color.bottom_text_select));
                }
                transaction.show(homePageFragment);
                break;

            case 1:
                if (expertFragment == null) {
                    expertFragment = new ExpertFragment();
                    transaction.add(R.id.fl_main, expertFragment);
                }
                transaction.show(expertFragment);
                //选中改变字体和图标的颜色
                if (isExpert == 1) {
                    ivMainExpert.setImageResource(R.drawable.icon_exper11);
                    tvMainExpert.setTextColor(UIUtils.getColor(R.color.bottom_text_select1));
                } else {
                    ivMainExpert.setImageResource(R.drawable.icon_exper1);
                    tvMainExpert.setTextColor(UIUtils.getColor(R.color.bottom_text_select));
                }
                break;

            case 2:
                if (forumFragment == null) {
                    forumFragment = new ForumFragment();
                    transaction.add(R.id.fl_main, forumFragment);
                }
                transaction.show(forumFragment);
                //选中改变字体和图标的颜色
                if (isExpert == 1) {
                    ivMainForum.setImageResource(R.drawable.icon_forum11);
                    tvMainForum.setTextColor(UIUtils.getColor(R.color.bottom_text_select1));
                } else {
                    ivMainForum.setImageResource(R.drawable.icon_forum1);
                    tvMainForum.setTextColor(UIUtils.getColor(R.color.bottom_text_select));
                }
                break;

            case 3:
                if (consultFragment == null) {
                    consultFragment = new ConsultFragment();
                    transaction.add(R.id.fl_main, consultFragment);
                }
                transaction.show(consultFragment);
                //选中改变字体和图标的颜色
                if (isExpert == 1) {
                    ivMainConsult.setImageResource(R.drawable.icon_zixun11);
                    tvMainConsult.setTextColor(UIUtils.getColor(R.color.bottom_text_select1));
                } else {
                    ivMainConsult.setImageResource(R.drawable.icon_zixun1);
                    tvMainConsult.setTextColor(UIUtils.getColor(R.color.bottom_text_select));
                }
                break;

            case 4:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    transaction.add(R.id.fl_main, meFragment);
                }
                transaction.show(meFragment);
                //选中改变字体和图标的颜色
                if (isExpert == 1) {
                    ivMainMe.setImageResource(R.drawable.icon_me11);
                    tvMainMe.setTextColor(UIUtils.getColor(R.color.bottom_text_select1));
                } else {
                    ivMainMe.setImageResource(R.drawable.icon_me1);
                    tvMainMe.setTextColor(UIUtils.getColor(R.color.bottom_text_select));
                }
                break;

        }
        transaction.commit();
    }

    //重置颜色图标
    private void reSetResourse() {
        ivMainHome.setImageResource(R.drawable.icon_home);
        ivMainExpert.setImageResource(R.drawable.icon_expert);
        ivMainForum.setImageResource(R.drawable.icon_forum);
        ivMainConsult.setImageResource(R.drawable.icon_zixun);
        ivMainMe.setImageResource(R.drawable.icon_me);

        tvMainHome.setTextColor(UIUtils.getColor(R.color.bottom_text_unselect));
        tvMainExpert.setTextColor(UIUtils.getColor(R.color.bottom_text_unselect));
        tvMainForum.setTextColor(UIUtils.getColor(R.color.bottom_text_unselect));
        tvMainConsult.setTextColor(UIUtils.getColor(R.color.bottom_text_unselect));
        tvMainMe.setTextColor(UIUtils.getColor(R.color.bottom_text_unselect));
    }

    //隐藏fragment
    private void hideFragment() {
        if (homePageFragment != null) {
            transaction.hide(homePageFragment);
        }
        if (expertFragment != null) {
            transaction.hide(expertFragment);
        }
        if (forumFragment != null) {
            transaction.hide(forumFragment);
        }
        if (consultFragment != null) {
            transaction.hide(consultFragment);
        }
        if (meFragment != null) {
            transaction.hide(meFragment);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == WHAT_BACK) {
                flag = true;
            }
        }
    };

    private boolean flag = true;
    private static final int WHAT_BACK = 1;

    //连续点击两次返回键退出
    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && flag) {
            Toast.makeText(MainActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
            flag = false;
            handler.sendEmptyMessageDelayed(WHAT_BACK, 2000);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //为了避免内存泄露，需要在ondestroy中移除所有消息
        handler.removeCallbacksAndMessages(null);
        EventBus.getDefault().unregister(this);
    }


    //获取权限成功回调
    @NeedsPermission({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void requestPermission() {
        //Toast.makeText(this, "开启权限成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void permissionDenied() {//当用户拒绝了权限请求时
    }

    //点击不在询问后的回调
    @OnNeverAskAgain({Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
    void permissionNeverAgain() {
    }
}
