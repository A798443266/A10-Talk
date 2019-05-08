package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.FindProblemShowAdapter;
import com.eiplan.zuji.adapter.MyAnswerAdapter;
import com.eiplan.zuji.adapter.MyQuestionAdapter;
import com.eiplan.zuji.bean.MyAnswerProblem;
import com.eiplan.zuji.bean.MyReleaseProblem;
import com.eiplan.zuji.bean.ProblemInfo;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 我的问答界面
 */

public class MyAnswerActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_myproblem)
    TextView tvMyproblem;
    @BindView(R.id.tv_myanswer)
    TextView tvMyanswer;
    @BindView(R.id.lv_problem)
    ListView lvProblem;
    @BindView(R.id.ll_problem)
    LinearLayout llProblem;
    @BindView(R.id.lv_anwser)
    ListView lvAnwser;
    @BindView(R.id.ll_answer)
    LinearLayout llAnswer;

    private MyQuestionAdapter adapter;
    private MyAnswerAdapter adapter1;

    private List<MyReleaseProblem> datas;

    private List<MyAnswerProblem> datas1;

    private String save;
    private String save1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_answer);
        ButterKnife.bind(this);

        tvTitle.setText("我的问答");
        tvRight.setVisibility(View.GONE);
        setListener();

        initView();
        initData();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (datas != null && datas.size() > 0) {
                    adapter = new MyQuestionAdapter(datas, MyAnswerActivity.this);
                    lvProblem.setAdapter(adapter);
                    lvProblem.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(MyAnswerActivity.this, ProblemDetailsActivity.class);
                            Bundle bundle = new Bundle();
                            intent.putExtra("caina",1);
                            bundle.putSerializable("problem", datas.get(position).getProblemInfo());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });
                }
            } else if (msg.what == 2) {
                if (datas1 != null && datas1.size() > 0) {
                    adapter1 = new MyAnswerAdapter(datas1, MyAnswerActivity.this);
                    lvAnwser.setAdapter(adapter1);
                }
            }

        }
    };

    private void initView() {

    }

    private void initData() {
        save = SpUtils.getString(this, Constant.my_save_problems);
        save1 = SpUtils.getString(this, Constant.my_save_answer_problems);
        if (!TextUtils.isEmpty(save)) {
            datas = JsonUtils.parseMyReleaseProblems(save);
        }
        if (!TextUtils.isEmpty(save1)) {
            datas1 = JsonUtils.parseMyAnswerProblems(save1);
        }

        //从服务器获取数据

        //我发布的问题
        OkHttpUtils.post().url(Constant.my_release_Prombles)
                .addParams("phone", SpUtils.getString(MyAnswerActivity.this, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "获取我的问题失败");
                        handler.sendEmptyMessage(1);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //解析json
                        SpUtils.putString(MyAnswerActivity.this, Constant.my_save_problems, response);
                        Log.e("TAG", response);
                        datas = JsonUtils.parseMyReleaseProblems(response);
                        handler.sendEmptyMessage(1);

                    }
                });

        //我回答的问题
        OkHttpUtils.post().url(Constant.my_answer_Prombles)
                .addParams("phone", SpUtils.getString(MyAnswerActivity.this, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "获取我的回答失败");
                        handler.sendEmptyMessage(2);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //解析json
                        Log.e("TAG", response);

                        SpUtils.putString(MyAnswerActivity.this, Constant.my_save_answer_problems, response);
                        datas1 = JsonUtils.parseMyAnswerProblems(response);
                        handler.sendEmptyMessage(2);

                    }
                });
    }

    private void setListener() {
        tvMyproblem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llProblem.setVisibility(View.VISIBLE);
                llAnswer.setVisibility(View.GONE);
                tvMyproblem.setTextColor(UIUtils.getColor(R.color.system_blue));
                tvMyproblem.setBackgroundResource(R.drawable.my_promblem_anwser_bg1);
                tvMyanswer.setTextColor(UIUtils.getColor(R.color.color_system_gray));
                tvMyanswer.setBackgroundResource(R.drawable.my_promblem_anwser_bg);
            }
        });

        tvMyanswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                llAnswer.setVisibility(View.VISIBLE);
                llProblem.setVisibility(View.GONE);
                tvMyanswer.setTextColor(UIUtils.getColor(R.color.system_blue));
                tvMyanswer.setBackgroundResource(R.drawable.my_promblem_anwser_bg1);
                tvMyproblem.setTextColor(UIUtils.getColor(R.color.color_system_gray));
                tvMyproblem.setBackgroundResource(R.drawable.my_promblem_anwser_bg);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
