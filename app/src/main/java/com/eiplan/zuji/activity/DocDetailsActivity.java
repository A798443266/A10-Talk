package com.eiplan.zuji.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.DocInfo;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 文档详情页面
 */

public class DocDetailsActivity extends AppCompatActivity {

    @BindView(R.id.imageview)
    ImageView imageview;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_doc_name)
    TextView tvDocName;
    @BindView(R.id.cv_user_pic)
    CircleImageView cvUserPic;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.ll_user_info)
    LinearLayout llUserInfo;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_major)
    TextView tvMajor;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_updatetime)
    TextView tvUpdatetime;
    @BindView(R.id.collapsing_toorbar)
    CollapsingToolbarLayout collapsingToorbar;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.iv_collect)
    ImageView ivCollect;
    @BindView(R.id.iv_zan)
    ImageView ivZan;
    @BindView(R.id.tv_good)
    TextView tvGood;
    @BindView(R.id.rl_buy)
    RelativeLayout rlBuy;

    private SweetAlertDialog dialog;
    private DocInfo doc;
    private boolean isCollect;//判断文档是否收藏
    private boolean isZan;//判断文档是否点赞

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_details);
        ButterKnife.bind(this);

        doc = (DocInfo) getIntent().getSerializableExtra("doc");//获取传递过来的专家对象

        //自定义沉浸式方法
        setStatusBarTransparent(this);
        getCollect();//判断文档是否收藏和点赞
        initView();

    }

    private void getCollect() {
        //从数据库中获取信息

        if (isCollect) {
            ivCollect.setBackgroundResource(R.drawable.icon_collection1);
        } else {
            ivCollect.setBackgroundResource(R.drawable.icon_collection);
        }

        if (isZan)
            ivZan.setBackgroundResource(R.drawable.icon_zan1);
        else
            ivZan.setBackgroundResource(R.drawable.icon_zan);

        //获取点赞人数

    }

    private void initView() {
        tvDocName.setText(doc.getName());
        tvBuy.setText(doc.getBuy() + "");
        tvGood.setText(doc.getGood() + "");
        tvMajor.setText(doc.getMajor());
        tvContent.setText(doc.getIntroduce());
        tvUpdatetime.setText(doc.getUpdatetime());
        tvUsername.setText(doc.getWriter());
        tvPrice.setText(doc.getPrice() + "");
        Glide.with(this).load(doc.getPic()).error(R.drawable.p).into(cvUserPic);
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

    @OnClick(R.id.rl_buy)
    public void buy() {
        Intent intent = new Intent(this, buyDocActivity.class);
        intent.putExtra("docname", doc.getName());
        intent.putExtra("price", doc.getPrice());
        intent.putExtra("fileId",doc.getId()+"");
        startActivity(intent);
    }

    @OnClick({R.id.iv_back, R.id.iv_collect, R.id.iv_zan})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_collect://收藏
                collectDoc();
               /* if (!isCollect) {
                    isCollect = !isCollect;
                    ivCollect.setBackgroundResource(R.drawable.icon_collection1);
                    Toast.makeText(DocDetailsActivity.this, "已收藏", Toast.LENGTH_SHORT).show();
                } else {
                    isCollect = !isCollect;
                    ivCollect.setBackgroundResource(R.drawable.icon_collection);
                    Toast.makeText(DocDetailsActivity.this, "取消收藏", Toast.LENGTH_SHORT).show();
                }*/
                break;
            case R.id.iv_zan:
                if (!isZan) {
                    isZan = !isZan;
                    ivZan.setBackgroundResource(R.drawable.icon_zan1);

                    //更新数据库
                    tvGood.setText((Integer.parseInt(tvGood.getText().toString()) + 1) + "");
//                    Toast.makeText(DocDetailsActivity.this,"点赞成功",Toast.LENGTH_SHORT).show();
                } else {
                    isZan = !isZan;
                    ivZan.setBackgroundResource(R.drawable.icon_zan);
                    //更新数据库
                    tvGood.setText((Integer.parseInt(tvGood.getText().toString()) - 1) + "");
//                    Toast.makeText(DocDetailsActivity.this,"取消点赞",Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void collectDoc() {
        OkHttpUtils.post().url(Constant.Collectfile)
                .addParams("phone", SpUtils.getString(DocDetailsActivity.this, Constant.current_phone))
                .addParams("fileId", doc.getId() + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        new SweetAlertDialog(DocDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("收藏失败!")
                                .show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = JSON.parseObject(response);
                        if (jsonObject.getInteger("code") == 100) {
                            dialog = new SweetAlertDialog(DocDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            new SweetAlertDialog(DocDetailsActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("收藏成功!")
                                    .show();
                            isCollect = !isCollect;
                            ivCollect.setBackgroundResource(R.drawable.icon_collection1);
                        } else {
                            new SweetAlertDialog(DocDetailsActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("收藏失败!")
                                    .show();
                        }
                    }
                });
    }

    //预览
    @OnClick(R.id.btn_view)
    public void onViewClicked() {
        Intent intent = new Intent(this, DocViewActivity.class);
        intent.putExtra("path", doc.getView());
        startActivity(intent);
    }
}
