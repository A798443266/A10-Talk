package com.eiplan.zuji.fragment.others;

import android.app.AlertDialog;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.MeCustomerAdapter2;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.bean.MyCustomer;
import com.eiplan.zuji.bean.ProblemInfo;
import com.eiplan.zuji.bean.VideoInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 专家端发送订单模块
 */

public class MeCustomerFragment2 extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;

    private AlertDialog dialog;

    private String price;

    private List<MyCustomer> customers;
    private MeCustomerAdapter2 adapter;


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        OkHttpUtils.post().url(Constant.customerFS)
                .addParams("phone", SpUtils.getString(mContext, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        customers = JsonUtils.parseCustomers(response);
                        show();
                    }
                });
    }

    private void show() {
        if (customers != null && customers.size() > 0) {
            adapter = new MeCustomerAdapter2(mContext, customers);
            lv.setAdapter(adapter);
            adapter.setOnGreeClickListener(new MeCustomerAdapter2.OnGreeClickListener() {
                @Override
                public void itemGreeClick(View view, int position) {
                    //发送订单
                    send(position);
                }
            });
        }
    }

    private void send(final int position) {

        View view = View.inflate(mContext, R.layout.set_price, null);
        Button btn_send = view.findViewById(R.id.btn_send);
        Button btn_cancel = view.findViewById(R.id.btn_cancel);
        final EditText et_price = view.findViewById(R.id.et_price);

        dialog = new AlertDialog.Builder(mContext).create();
        dialog.setView(view);
        dialog.setCancelable(true);
        dialog.show();


        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = et_price.getText().toString();
                if (TextUtils.isEmpty(price)) {
                    Toast.makeText(mContext, "请设置金额", Toast.LENGTH_SHORT).show();
                } else {
                    OkHttpUtils.post().url(Constant.sendDingdan)
                            .addParams("id", customers.get(position).getId() + "")
                            .addParams("price", price)
                            .build()
                            .execute(new StringCallback() {
                                @Override
                                public void onError(Call call, Exception e, int id) {
                                    dialog.dismiss();
                                    Toast.makeText(mContext, "网络出错了", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(String response, int id) {
                                    com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
                                    if (jsonObject.getInteger("code") == 100) {
                                        Toast.makeText(mContext, "订单发送成功", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                        EventBus.getDefault().post(new VideoInfo());
                                        initData();

                                    } else {
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "网络出错了", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }

            }
        });


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    //接收消息,刷新页面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent1(ExpertInfo e) {
        customers.clear();
        initData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me_customer1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
