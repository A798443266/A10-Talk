package com.eiplan.zuji.fragment.others;

import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.MeCustomerAdapter1;
import com.eiplan.zuji.bean.MyCustomer;
import com.eiplan.zuji.bean.ProblemInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

/**
 * 专家端客户预约模块
 */
public class MeCustomerFragment1 extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;

    private List<MyCustomer> customers;
    private MeCustomerAdapter1 adapter;


    @Override
    protected void initView() {

    }

    @Override
    public void initData() {
        OkHttpUtils.post().url(Constant.customerYY)
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
            adapter = new MeCustomerAdapter1(mContext, customers);
            lv.setAdapter(adapter);
            adapter.setOnGreeClickListener(new MeCustomerAdapter1.OnGreeClickListener() { //同意
                @Override
                public void itemGreeClick(View view, int position) {
//                    Toast.makeText(mContext, "同意" + position, Toast.LENGTH_SHORT).show();
                    agree(position);
                }
            });

            adapter.setOnRejectClickListener(new MeCustomerAdapter1.OnRejectClickListener() { //拒绝
                @Override
                public void itemRejecttClick(View view, int position) {
//                    Toast.makeText(mContext, "拒绝" + position, Toast.LENGTH_SHORT).show();
                    disagree(position);
                }
            });
        }
    }

    //拒绝
    private void disagree(int position) {
        OkHttpUtils.post().url(Constant.Agree)
                .addParams("id", customers.get(position).getId() + "")
                .addParams("action", 0 + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(mContext, "网络出错了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
                        if (jsonObject.getInteger("code") == 100) {
                            customers.clear();
                            initData();
                        } else {
                            Toast.makeText(mContext, "网络出错了", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //同意
    private void agree(int position) {
        OkHttpUtils.post().url(Constant.Agree)
                .addParams("id", customers.get(position).getId() + "")
                .addParams("action", 1 + "")
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(mContext, "网络出错了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        com.alibaba.fastjson.JSONObject jsonObject = JSON.parseObject(response);
                        if (jsonObject.getInteger("code") == 100) {
                            Toast.makeText(mContext,"已同意",Toast.LENGTH_SHORT).show();
                            customers.clear();
                            EventBus.getDefault().post(new ProblemInfo());
                            initData();
                        } else {
                            Toast.makeText(mContext, "网络出错了", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me_customer1;
    }

}
