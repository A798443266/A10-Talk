package com.eiplan.zuji.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.ExpertCommentAdapter;
import com.eiplan.zuji.adapter.HomeDocAdapter;
import com.eiplan.zuji.adapter.UpdateVideoAdapter;
import com.eiplan.zuji.bean.DocInfo;
import com.eiplan.zuji.bean.ExpertComment;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.bean.VideoInfo;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.view.MyListView;
import com.eiplan.zuji.view.MyListViewInCoor;
import com.hyphenate.easeui.EaseConstant;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 专家详情页面
 */

public class ExpertDetailsActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rl_connect)
    RelativeLayout rlConnect;
    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.iv_cover)
    ImageView iv_cover;
    @BindView(R.id.cl_user_pic)
    CircleImageView clUserPic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_zixun)
    TextView tvZixun;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_job)
    TextView tvJob;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_work_year)
    TextView tvWorkYear;
    @BindView(R.id.tv_major)
    TextView tvMajor;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;
    @BindView(R.id.iv_yuyue)
    ImageView ivYuyue;
    @BindView(R.id.ll_yuyue)
    LinearLayout llYuyue;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    @BindView(R.id.tv_yuyue)
    TextView tvYuyue;
    @BindView(R.id.lv)
    MyListView lv;

    private ExpertInfo expert;
    private boolean isCollect;//判断专家是否收藏
    private boolean isYuyue;//判断专家是否预约
    private SweetAlertDialog dialog;

    private List<ExpertComment> comments = new ArrayList<>();
    private ExpertCommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expert_details);
        ButterKnife.bind(this);

        expert = (ExpertInfo) getIntent().getSerializableExtra("expert");//获取传递过来的专家对象
        initView();
        getCollect();//判断专家是否收藏和预约
        initData();
    }

    private void initData() {

        //获取专家的评论
        OkHttpUtils.post().url(Constant.getExpertComments)
                .addParams("phone", expert.getPhone())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //解析json数据
                        List<ExpertComment> c = new ArrayList<>();
                        c = JsonUtils.parseExpertComments(response);
                        if (c.size() > 3) {
                            for (int i = 0; i < 3; i++) {
                                comments.add(c.get(i));
                            }
                        } else {
                            comments.addAll(c);
                        }
                        show();
                    }
                });
    }

    private void show() {
        if (comments != null && comments.size() > 0) {
            adapter = new ExpertCommentAdapter(this, comments);
            lv.setAdapter(adapter);
        }
    }

    private void getCollect() {
        //从数据库中获取信息
        if (isCollect) {
            ivCollect.setBackgroundResource(R.drawable.icon_collection1);
        } else {
            ivCollect.setBackgroundResource(R.drawable.icon_collection);
        }

        if (isYuyue) {
            ivYuyue.setBackgroundResource(R.drawable.icon_yuyue1);
            tvYuyue.setText("取消预约");
        } else
            ivYuyue.setBackgroundResource(R.drawable.icon_yuyue);
    }

    private void initView() {
        //自定义沉浸式方法
        setStatusBarTransparent(this);
        setSupportActionBar(toolbar);
        //设置返回箭头
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        toolbar.setTitle(expert.getName());
        tvName.setText(expert.getName());
        tvAddress.setText(expert.getAddress());
        tvIntroduce.setText("    " + expert.getIntroduce());
        tvJob.setText(expert.getJob());
        tvUsername.setText(expert.getName());
        tvWorkYear.setText(expert.getWorkyear());
        tvZixun.setText(expert.getAsk() + "");
        tvMajor.setText(expert.getMajor());

        Glide.with(this).load(expert.getPic()).error(R.drawable.p).into(clUserPic);
        Glide.with(this).load("http://luo.easy.echosite.cn/A13/static/vedio/cover/4.jpg").into(iv_cover);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //返回箭头的id为android.R.id.home
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    //实现状态栏沉浸式
    public void setStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @OnClick(R.id.rl_connect)
    public void onViewClicked() {
        Intent intent = new Intent(this, ChatActivity.class);
        // 传递环信id，即电话号码
        intent.putExtra(EaseConstant.EXTRA_USER_ID, expert.getPhone());
        intent.putExtra("name", expert.getName()); //传递名称过去
        startActivity(intent);
    }

    @OnClick(R.id.ll_yuyue)
    public void onViewClicked1() {

        dialog = new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE);
        dialog.setTitleText("提示");
        dialog.setContentText("请确保已经跟专家商量好价格,预约之后专家可以向您发送订单,您确定要预约该专家吗");
        dialog.setConfirmText("确定");
        dialog.setCancelText("取消");
        dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismiss();
                yuyue();
            }
        });
        dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                dialog.dismiss();
            }
        });
        dialog.show();

       /* if (!isYuyue) {
            isYuyue = !isYuyue;
            ivYuyue.setBackgroundResource(R.drawable.icon_yuyue1);
        } else {
            isYuyue = !isYuyue;
            ivYuyue.setBackgroundResource(R.drawable.icon_yuyue);
        }*/

    }

    private void yuyue() {
        OkHttpUtils.post().url(Constant.yuyue)
                .addParams("uphone", SpUtils.getString(ExpertDetailsActivity.this, Constant.current_phone))
                .addParams("ephone", expert.getPhone())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = JSON.parseObject(response);
                        if (jsonObject.getInteger("code") == 100) {
                            dialog = new SweetAlertDialog(ExpertDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            new SweetAlertDialog(ExpertDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("预约成功!")
                                    .show();
                            isYuyue = !isYuyue;
                            ivYuyue.setBackgroundResource(R.drawable.icon_yuyue1);
                        } else {
                            new SweetAlertDialog(ExpertDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("预约失败!")
                                    .show();
                        }
                    }
                });
    }

    @OnClick(R.id.iv_collect)
    public void onViewClicked2() {
        collection();
        /*if (!isCollect) {
            isCollect = !isCollect;
            ivCollect.setBackgroundResource(R.drawable.icon_collection1);
            Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
        } else {
            isCollect = !isCollect;
            ivCollect.setBackgroundResource(R.drawable.icon_collection);
            Toast.makeText(this, "取消收藏", Toast.LENGTH_SHORT).show();
        }*/
    }

    private void collection() {
        OkHttpUtils.post().url(Constant.CollectExp)
                .addParams("phone", SpUtils.getString(ExpertDetailsActivity.this, Constant.current_phone))
                .addParams("ephone", expert.getPhone())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = JSON.parseObject(response);
                        if (jsonObject.getInteger("code") == 100) {
                            dialog = new SweetAlertDialog(ExpertDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            new SweetAlertDialog(ExpertDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("收藏成功!")
                                    .show();
                            isCollect = !isCollect;
                            ivCollect.setBackgroundResource(R.drawable.icon_collection1);
                        } else {
                            new SweetAlertDialog(ExpertDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("收藏失败!")
                                    .show();
                        }
                    }
                });
    }
}
