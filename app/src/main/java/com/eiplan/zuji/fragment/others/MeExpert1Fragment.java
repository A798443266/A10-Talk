package com.eiplan.zuji.fragment.others;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.PingFenActivity;
import com.eiplan.zuji.activity.buyDocActivity;
import com.eiplan.zuji.adapter.MeExpert1Adapter;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.bean.LiveRoom;
import com.eiplan.zuji.bean.MyExpertInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.view.MyListView;
import com.sdsmdg.tastytoast.TastyToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class MeExpert1Fragment extends BaseFragment {
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.lv1)
    MyListView lv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.lv2)
    MyListView lv2;

    private List<MyExpertInfo> datas = new ArrayList<>();
    private List<MyExpertInfo> datas1 = new ArrayList<>();
    private List<MyExpertInfo> datas2 = new ArrayList<>();
    private MeExpert1Adapter adapter1;
    private MeExpert1Adapter adapter2;

    private String data;//缓存json数据
    private SweetAlertDialog dialog;

    private int refresh = 0;


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        if (refresh == 0) {
            data = SpUtils.getString(mContext, Constant.ordering);
            if (!TextUtils.isEmpty(data)) {
                datas = JsonUtils.parseOrdering(data);
            }
        }

        OkHttpUtils.post().url(Constant.getOrdering)
                .addParams("phone", SpUtils.getString(mContext, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SpUtils.putString(mContext, Constant.ordering, response);
                        datas = JsonUtils.parseOrdering(response);
                        show();
                    }
                });

    }

    private void show() {
        if (datas != null && datas.size() > 0) {
            for (int i = 0; i < datas.size(); i++) {
                if (datas.get(i).getState().equals("支付")) {
                    datas1.add(datas.get(i));
                } else {
                    datas2.add(datas.get(i));
                }
            }
            adapter1 = new MeExpert1Adapter(mContext, datas1);
            adapter2 = new MeExpert1Adapter(mContext, datas2);
            lv1.setAdapter(adapter1);
            lv2.setAdapter(adapter2);

            adapter1.setOnItemRightClickListener(new MeExpert1Adapter.OnItemRightClickListener() {
                @Override
                public void itemRightClick(View view, int position) {
                    pay(position);
                }
            });

            adapter2.setOnItemRightClickListener(new MeExpert1Adapter.OnItemRightClickListener() {
                @Override
                public void itemRightClick(View view, int position) {
                    WanCheng(position);
                }
            });
        }
    }

    //完成
    private void WanCheng(final int position) {
        OkHttpUtils.post().url(Constant.payAndwancheng)
                .addParams("id", datas2.get(position).getId() + "")
                .addParams("state", "完成")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                TastyToast.makeText(mContext, "操作失败!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

            @Override
            public void onResponse(final String response, int id) {
                dialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                dialog.setTitleText("提示");
                dialog.setContentText("咨询完成，是否立即评价专家？");
                dialog.setConfirmText("立即评价");
                dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        Intent intent = new Intent(getActivity(), PingFenActivity.class);
                        intent.putExtra("name", datas2.get(position).getName());//传递评价的专家名称
                        intent.putExtra("id", datas2.get(position).getId() + ""); //传递订单id
                        startActivityForResult(intent, 1);
                        sweetAlertDialog.dismiss();
                    }
                });
                dialog.setCancelText("取消");
                dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        refresh = 1;
                        datas.clear();
                        datas1.clear();
                        datas2.clear();
                        initData();
                        //发送消息，让带评价界面刷新
                        EventBus.getDefault().post(new ExpertInfo());
                    }
                });
                dialog.show();

            }
        });
    }

    //支付
    private void pay(int position) {
        Intent intent = new Intent(mContext, buyDocActivity.class);
        intent.putExtra("price", datas1.get(position).getPrice());
        intent.putExtra("action", 1);
        intent.putExtra("id", datas1.get(position).getId() + "");
        startActivity(intent);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                refresh = 1;
                datas.clear();
                datas1.clear();
                datas2.clear();
                initData();
                //发送消息，让已完成界面刷新
                EventBus.getDefault().post(new MyExpertInfo());
            }
        }
    }

    //接收消息,刷新页面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent1(LiveRoom e) {
        refresh = 1;
        datas.clear();
        datas1.clear();
        datas2.clear();
        initData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me_expert1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
