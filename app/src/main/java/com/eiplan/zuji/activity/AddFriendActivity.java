package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiplan.zuji.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 添加好友界面
 */

public class AddFriendActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.titlebar_bg)
    RelativeLayout titlebarBg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        ButterKnife.bind(this);

        tvRight.setVisibility(View.GONE);
        tvTitle.setText("添加");
    }

    @OnClick(R.id.ll)
    public void onViewClicked() {
        startActivity(new Intent(this, AddFriend2Activity.class));
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked1() {
        finish();
    }
}
