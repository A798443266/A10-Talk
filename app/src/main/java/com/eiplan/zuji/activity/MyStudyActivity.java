package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.eiplan.zuji.R;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyStudyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_study);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }

    @OnClick({R.id.r, R.id.r2})
    public void onViewClicked1(View view) {
        switch (view.getId()) {
            case R.id.r:
                startActivity(new Intent(this,LookSubjectActivity.class));
                break;
            case R.id.r2:
                startActivity(new Intent(this,LookSubjectActivity.class));
                break;
        }
    }
}
