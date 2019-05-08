package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.CollectionExpertAdapert;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

/**
 * 收藏专家界面呢
 */

public class CollectionExpertActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.tv_no)
    TextView tvNo;

    private List<ExpertInfo> experts;
    private CollectionExpertAdapert adapert;
    private SweetAlertDialog dialog;
    private String data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection_expert);
        ButterKnife.bind(this);
        initView();
        initData();
    }

    private void initView() {
    }

    private void initData() {

        data = SpUtils.getString(this, Constant.save_collect_experts);
        if (!TextUtils.isEmpty(data)) {
            experts = JsonUtils.parseMyExperts(data);
        }

        OkHttpUtils.post().url(Constant.getCollectExpers)
                .addParams("phone", SpUtils.getString(this, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        llLoading.setVisibility(View.GONE);
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SpUtils.putString(CollectionExpertActivity.this, Constant.save_collect_experts, response);
                        experts = JsonUtils.parseMyExperts(response);
                        Log.e("TAG", experts.size() + "");
                        llLoading.setVisibility(View.GONE);
                        show();
                    }
                });
    }

    private void show() {
        if (experts != null && experts.size() > 0) {
            adapert = new CollectionExpertAdapert(experts, this);
            lv.setAdapter(adapert);
            adapert.setOnCancelClickListener(new CollectionExpertAdapert.OnCancelClickListener() {
                @Override
                public void cancelClick(View view, final int position) {
                    dialog = new SweetAlertDialog(CollectionExpertActivity.this, SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText("提示");
                    dialog.setContentText("您确定要取消收藏该专家吗");
                    dialog.setConfirmText("确定");
                    dialog.setCancelText("点错了");
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            dialog.dismiss();
                            cancelCollection(position);
                        }
                    });
                    dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });
        } else {
            tvNo.setVisibility(View.VISIBLE);
        }
    }


    private void cancelCollection(int position) {
        OkHttpUtils.post().url(Constant.cancelCollectExpers)
                .addParams("phone", SpUtils.getString(CollectionExpertActivity.this, Constant.current_phone))
                .addParams("ephone", experts.get(position).getPhone())
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        new SweetAlertDialog(CollectionExpertActivity.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("取消收藏失败!")
                                .show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = JSON.parseObject(response);
                        if (jsonObject.getInteger("code") == 100) {
                            dialog = new SweetAlertDialog(CollectionExpertActivity.this, SweetAlertDialog.SUCCESS_TYPE);
                            new SweetAlertDialog(CollectionExpertActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("取消收藏成功!")
                                    .show();
                        } else {
                            new SweetAlertDialog(CollectionExpertActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("取消收藏失败!")
                                    .show();
                        }
                        initData();
                    }
                });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
