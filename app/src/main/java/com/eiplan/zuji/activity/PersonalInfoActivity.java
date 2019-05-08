package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.UserInfo;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 个人信息页面
 */
public class PersonalInfoActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.titlebar_bg)
    RelativeLayout titlebarBg;
    @BindView(R.id.cv_user_pic)
    CircleImageView cvUserPic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone_start)
    TextView tvPhoneStart;
    @BindView(R.id.tv_phone_end)
    TextView tvPhoneEnd;
    @BindView(R.id.rl_phone)
    RelativeLayout rlPhone;
    @BindView(R.id.rl_update_psw)
    RelativeLayout rlUpdatePsw;
    @BindView(R.id.tv_work_year)
    TextView tvWorkYear;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.rl_company)
    RelativeLayout rlCompany;
    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.rl_industry)
    RelativeLayout rlIndustry;
    @BindView(R.id.rl_head_photo)
    RelativeLayout rlHeadPhoto;

    private int isExpert;
    private String name;
    private String phone;
    private String pic;
    private String workyear;
    private String company;
    private String major;
    private String job;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        ButterKnife.bind(this);

        isExpert = SpUtils.getInt(this, Constant.current_isexpert);
        phone = SpUtils.getString(this, Constant.current_phone);
        pic = SpUtils.getString(this, Constant.current_pic);
        name = SpUtils.getString(this, Constant.current_name);
        if (isExpert == 1) {//专家
            major = SpUtils.getString(this, Constant.current_major);
            workyear = SpUtils.getString(this, Constant.current_workyear);
            job = SpUtils.getString(this, Constant.current_job);
        } else { //用户
            major = SpUtils.getString(this, Constant.current_industry);
            company = SpUtils.getString(this, Constant.current_company);
        }

        initView();

    }

    private void initView() {
        ivBack.setBackgroundResource(R.drawable.icon_back);
        tvRight.setVisibility(View.GONE);
        tvTitle.setText("基本资料");
        tvTitle.setTextColor(UIUtils.getColor(R.color.color_system_blank));
        titlebarBg.setBackgroundColor(UIUtils.getColor(R.color.color_white));

        Glide.with(this).load(pic).error(R.drawable.p).into(cvUserPic);
        tvName.setText(name);
        tvPhoneStart.setText(phone.substring(0, 3));
        tvPhoneEnd.setText(phone.substring(7, 11));
        if (!TextUtils.isEmpty(company)) {
            tvCompany.setText(company);
        }
        if (!TextUtils.isEmpty(major)) {
            tvIndustry.setText(major);
        }
    }

    @OnClick(R.id.rl_head_photo)
    public void onViewClicked() {
        startActivity(new Intent(this, SelectPhoneActivity.class));
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked1() {
        finish();
    }

    @OnClick(R.id.btn_save)
    public void onViewClicked2() {

    }
}
