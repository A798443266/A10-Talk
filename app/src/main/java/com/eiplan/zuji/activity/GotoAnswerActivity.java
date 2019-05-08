package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.FindProblemShowAdapter;
import com.eiplan.zuji.bean.ProblemInfo;
import com.eiplan.zuji.bean.UserInfo;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 去回答问题的界面
 */

public class GotoAnswerActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    private List<ProblemInfo> datas;
    private FindProblemShowAdapter adapter;
    private String data;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            llLoading.setVisibility(View.GONE);
            if (datas != null && datas.size() > 0) {
                adapter = new FindProblemShowAdapter(datas, GotoAnswerActivity.this);
                lv.setAdapter(adapter);

                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(GotoAnswerActivity.this, ProblemDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("problem", datas.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            } else {
                tvNo.setVisibility(View.VISIBLE);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goto_answer);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        data = SpUtils.getString(this, Constant.recommendForme_problems);
        if (!TextUtils.isEmpty(data)) {
            datas = JsonUtils.parseProblems(data);
        }

        String major = "";
        int isExpert = SpUtils.getInt(this, Constant.current_isexpert);
        if (isExpert == 1) {//专家
            major = SpUtils.getString(this, Constant.current_major);
        } else { //用户
            major = SpUtils.getString(this, Constant.current_industry);
        }
        OkHttpUtils.post().url(Constant.gotoAnswer)
                .addParams("major", major)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        handler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //解析json
                        SpUtils.putString(GotoAnswerActivity.this, Constant.recommendForme_problems, response);
                        datas = JsonUtils.parseProblems(response);
                        handler.sendEmptyMessage(1);

                    }
                });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
