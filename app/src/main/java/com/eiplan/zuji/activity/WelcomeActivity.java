package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.eiplan.zuji.R;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.hyphenate.chat.EMClient;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WelcomeActivity extends AppCompatActivity {

    @BindView(R.id.ll_logo)
    LinearLayout llLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        //隐藏顶部的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        showAnimation();
        // 发送2s钟的延时消息
        handler.sendMessageDelayed(Message.obtain(), 2000);
    }

    private void showAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(1500);
        alphaAnimation.setInterpolator(new AccelerateInterpolator());//设置动画的变化率

        ScaleAnimation scaleAnimation = new ScaleAnimation(0, 1, 0, 1,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(1000);

        AnimationSet animationSet = new AnimationSet(true);

        animationSet.addAnimation(alphaAnimation);
        animationSet.addAnimation(scaleAnimation);
        llLogo.startAnimation(animationSet);
    }

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            // 如果当前activity已经退出，那么我就不处理handler中的消息
            if (isFinishing()) {
                return;
            }
            // 判断进入主页面还是登录页面
            toMainOrLogin();
        }
    };

    private void toMainOrLogin() {

        int isFirst = SpUtils.getInt(this, Constant.GUIDE);
        if (isFirst == 1) {
            SpUtils.putInt(this, Constant.GUIDE, 0);
            startActivity(new Intent(this, GuideActivity.class));
        } else {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    // 判断当前账号是否已经登录过
                    if (EMClient.getInstance().isLoggedInBefore()) {// 登录过
                        // 跳转到主页面
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                    } else {// 没登录过
                        // 跳转到登录页面
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            });
        }
        finish();
    }


}
