package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的钱包
 */

public class MyWalletActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_my_wallet);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvRight.setVisibility(View.GONE);
        tvTitle.setText("我的钱包");
        tvTitle.setTextColor(UIUtils.getColor(R.color.color_system_blank));
        ivBack.setImageResource(R.drawable.icon_back);
        titlebarBg.setBackgroundColor(UIUtils.getColor(R.color.color_white));
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
