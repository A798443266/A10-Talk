package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bruce.pickerview.popwindow.DatePickerPopWin;
import com.eiplan.zuji.R;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 发布需求页面
 */

public class ReleaseDemandActivity extends AppCompatActivity {

    @BindView(R.id.et_expert)
    EditText etExpert;
    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.et_compnay)
    EditText etCompnay;
    @BindView(R.id.et_budget)
    EditText etBudget;
    @BindView(R.id.et_time)
    TextView etTime;
    @BindView(R.id.step1)
    LinearLayout step1;
    @BindView(R.id.et_details)
    EditText etDetails;
    @BindView(R.id.et_background)
    EditText etBackground;
    @BindView(R.id.et_request)
    EditText etRequest;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.step2)
    ScrollView step2;
    @BindView(R.id.et_gongqi)
    TextView etGongqi;
    @BindView(R.id.pb)
    ProgressBar pb;

    private DatePickerPopWin pickerPopWin;
    private String time;
    private String expert;
    private String city;
    private String budget = "商议确定";
    private String company;
    private String requst = "无";
    private String details;
    private String background = "无";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_demand);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.iv_back, R.id.tv_next, R.id.tv_back, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_next:
                step1.setVisibility(View.GONE);
                step2.setVisibility(View.VISIBLE);
                break;
            case R.id.tv_back:
                step1.setVisibility(View.VISIBLE);
                step2.setVisibility(View.GONE);
                break;
            case R.id.btn_submit:
                submit();
                break;
        }
    }

    private void submit() {
        expert = etExpert.getText().toString().trim();
        city = etCity.getText().toString().trim();
        company = etCompnay.getText().toString().trim();
        budget = etBudget.getText().toString().trim();
        time = etTime.getText().toString().trim();
        requst = etRequest.getText().toString().trim();
        details = etDetails.getText().toString().trim();
        background = etBackground.getText().toString().trim();
        if (TextUtils.isEmpty(time) || TextUtils.isEmpty(city) || TextUtils.isEmpty(expert) || TextUtils.isEmpty(details)) {
            Toast.makeText(this, "输入的信息不完整", Toast.LENGTH_SHORT).show();
            return;
        }
        pb.setVisibility(View.VISIBLE);
        OkHttpUtils.post().url(Constant.upDemand)
                .addParams("expert", expert)
                .addParams("phone", SpUtils.getString(this, Constant.current_phone))
                .addParams("company", company)
                .addParams("city", city)
                .addParams("budget", budget)
                .addParams("validity", time)
                .addParams("background", background)
                .addParams("details", details)
                .addParams("requirement", requst)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        pb.setVisibility(View.GONE);
                        Toast.makeText(ReleaseDemandActivity.this, "网络出错了哦", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = JSON.parseObject(response);
                        pb.setVisibility(View.GONE);
                        if (jsonObject.getInteger("code") == 100) {
                            Toast.makeText(ReleaseDemandActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(ReleaseDemandActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


    @OnClick({R.id.et_gongqi, R.id.et_time})
    public void onViewClicked1(View view) {
        switch (view.getId()) {
            case R.id.et_gongqi:
                pickerPopWin = new DatePickerPopWin.Builder(ReleaseDemandActivity.this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        etGongqi.setText(dateDesc);
                    }
                }).textConfirm("确定") //text of confirm button
                        .textCancel("取消") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorCancel(UIUtils.getColor(R.color.color_system_gray)) //color of cancel button
                        .colorConfirm(UIUtils.getColor(R.color.system_blue))//color of confirm button
                        .maxYear(2020) // max year in loop
                        .build();
                pickerPopWin.showPopWin(ReleaseDemandActivity.this);
                break;

            case R.id.et_time:
                pickerPopWin = new DatePickerPopWin.Builder(ReleaseDemandActivity.this, new DatePickerPopWin.OnDatePickedListener() {
                    @Override
                    public void onDatePickCompleted(int year, int month, int day, String dateDesc) {
                        etTime.setText(dateDesc);
                        time = etTime.getText().toString();
                    }
                }).textConfirm("确定") //text of confirm button
                        .textCancel("取消") //text of cancel button
                        .btnTextSize(16) // button text size
                        .viewTextSize(25) // pick view text size
                        .colorCancel(UIUtils.getColor(R.color.color_system_gray)) //color of cancel button
                        .colorConfirm(UIUtils.getColor(R.color.system_blue))//color of confirm button
                        .maxYear(2020) // max year in loop
                        .build();
                pickerPopWin.showPopWin(ReleaseDemandActivity.this);
                break;
        }
    }


}
