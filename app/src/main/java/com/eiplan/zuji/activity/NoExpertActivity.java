package com.eiplan.zuji.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.eiplan.zuji.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NoExpertActivity extends Activity {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_digree)
    TextView tvDigree;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_expert);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.tv_cancel, R.id.tv_digree})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.tv_digree:
                startActivity(new Intent(this,BecomeExpertActivity.class));
                finish();
                break;
        }
    }
}
