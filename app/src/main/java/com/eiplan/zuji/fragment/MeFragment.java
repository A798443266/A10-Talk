package com.eiplan.zuji.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.BecomeExpertActivity;
import com.eiplan.zuji.activity.CollectionExpertActivity;
import com.eiplan.zuji.activity.MyAnswerActivity;
import com.eiplan.zuji.activity.MyCustomerActivity;
import com.eiplan.zuji.activity.MyDemandsActivity;
import com.eiplan.zuji.activity.MyDocActivity;
import com.eiplan.zuji.activity.MyExpertActivity;
import com.eiplan.zuji.activity.MyStudyActivity;
import com.eiplan.zuji.activity.MyWalletActivity;
import com.eiplan.zuji.activity.MypointActivity;
import com.eiplan.zuji.activity.PersonalInfoActivity;
import com.eiplan.zuji.activity.QianDaoActivity;
import com.eiplan.zuji.activity.SettingActivity;
import com.eiplan.zuji.activity.TradeRecordActivity;
import com.eiplan.zuji.activity.UpdateVideoActivity;
import com.eiplan.zuji.bean.UserInfo;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import de.hdodenhof.circleimageview.CircleImageView;

public class MeFragment extends Fragment implements View.OnClickListener {

    private UserInfo user;
    private Context mContext;
    private int isExpert;//是否是专家
    private String major;
    private String pic;
    private String name;
    private View mView;

    private ImageView ivSignin;
    private CircleImageView clUserPic;
    private TextView tvName;
    private TextView tvIndustry;
    private RelativeLayout rlInfo;
    private RelativeLayout rl1;
    private RelativeLayout rl2;
    private RelativeLayout rl3;
    private RelativeLayout rl4;
    private RelativeLayout rl5;
    private RelativeLayout rl6;
    private RelativeLayout rl7;
    private RelativeLayout rl8;
    private RelativeLayout rl9;
    private RelativeLayout rl11;
    private RelativeLayout rl12;
    private RelativeLayout rl13;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        isExpert();
        if (isExpert == 0) {//普通用户
            view = View.inflate(mContext, R.layout.fragment_me, null);
        } else {//专家
            view = View.inflate(mContext, R.layout.fragment_me1, null);
        }
        mView = view;
        initView();
        return view;
    }

    private void initView() {

        ivSignin = mView.findViewById(R.id.iv_signin);
        clUserPic = mView.findViewById(R.id.cl_user_pic);
        tvName = mView.findViewById(R.id.tv_name);
        tvIndustry = mView.findViewById(R.id.tv_industry);

        rlInfo = mView.findViewById(R.id.rl_info);
        rl1 = mView.findViewById(R.id.rl1);
        rl2 = mView.findViewById(R.id.rl2);
        rl3 = mView.findViewById(R.id.rl3);
        rl4 = mView.findViewById(R.id.rl4);
        rl5 = mView.findViewById(R.id.rl5);
        rl6 = mView.findViewById(R.id.rl6);
        rl7 = mView.findViewById(R.id.rl7);
        rl8 = mView.findViewById(R.id.rl8);
        rl9 = mView.findViewById(R.id.rl9);
        rl11 = mView.findViewById(R.id.rl11);
        rl12 = mView.findViewById(R.id.rl12);
        rl13 = mView.findViewById(R.id.rl13);

        rl1.setOnClickListener(this);
        rl2.setOnClickListener(this);
        rl3.setOnClickListener(this);
        rl4.setOnClickListener(this);
        rl5.setOnClickListener(this);
        rl6.setOnClickListener(this);
        rl7.setOnClickListener(this);
        rl8.setOnClickListener(this);
        rl9.setOnClickListener(this);
        rl11.setOnClickListener(this);
        rl12.setOnClickListener(this);
        rl13.setOnClickListener(this);
        rlInfo.setOnClickListener(this);
        ivSignin.setOnClickListener(this);


        //显示信息
        pic = SpUtils.getString(mContext, Constant.current_pic);
        name = SpUtils.getString(mContext, Constant.current_name);

        if (isExpert == 1) {//专家
            major = SpUtils.getString(mContext, Constant.current_major);
            if (TextUtils.isEmpty(major)) {
                tvIndustry.setText("您还没有填写行业");
            } else {
                tvIndustry.setText(major);
            }
        } else { //用户
            major = SpUtils.getString(mContext, Constant.current_industry);
            if (TextUtils.isEmpty(major)) {
                tvIndustry.setText("您还没有填写行业");
            } else {
                if (major.equals("0"))
                    tvIndustry.setText("您还没有填写行业");
                else
                    tvIndustry.setText(major);
            }
        }
        tvName.setText(name);
        Glide.with(mContext).load(pic).error(R.drawable.logo).into(clUserPic);

    }


    public void isExpert() {
        isExpert = SpUtils.getInt(mContext, Constant.current_isexpert);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl1:
                startActivity(new Intent(mContext, MyExpertActivity.class));
                break;

            case R.id.rl2:
                startActivity(new Intent(mContext, MyWalletActivity.class));
                break;

            case R.id.rl3:
                startActivity(new Intent(mContext, MyDocActivity.class));
                break;

            case R.id.rl4:
                startActivity(new Intent(mContext, CollectionExpertActivity.class));
                break;
            case R.id.rl5:
                startActivity(new Intent(mContext, MypointActivity.class));
                break;

            case R.id.rl6:
                if (isExpert == 0)
                    startActivity(new Intent(mContext, BecomeExpertActivity.class));
                else
                    startActivity(new Intent(mContext, MyCustomerActivity.class));
                break;

            case R.id.rl7:
                startActivity(new Intent(mContext, SettingActivity.class));
                break;
            case R.id.rl8:
                startActivity(new Intent(mContext, MyDemandsActivity.class));
                break;
            case R.id.rl9:
                startActivity(new Intent(mContext, MyAnswerActivity.class));
                break;
            case R.id.rl11:
                startActivity(new Intent(mContext, UpdateVideoActivity.class));
                break;
            case R.id.rl12:
                startActivity(new Intent(mContext, TradeRecordActivity.class));
                break;
            case R.id.rl13:
                startActivity(new Intent(mContext, MyStudyActivity.class));
                break;

            case R.id.iv_signin:
                startActivity(new Intent(mContext, QianDaoActivity.class));
                break;

            case R.id.rl_info:
                Intent intent1 = new Intent(mContext, PersonalInfoActivity.class);
                startActivity(intent1);
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}




