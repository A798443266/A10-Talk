package com.eiplan.zuji.fragment.others;

import android.widget.ListView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.MeCustomerAdapter3;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.bean.MyCustomer;
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


public class MeCustomerFragment3 extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;

    private List<MyCustomer> customers;
    private MeCustomerAdapter3 adapter;


    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        OkHttpUtils.post().url(Constant.customerZF)
                .addParams("phone", SpUtils.getString(mContext, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        customers = JsonUtils.parseCustomersZF(response);
                        show();
                    }
                });
    }

    //接收消息,刷新页面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent1(VideoInfo e) {
        customers.clear();
        initData();
    }

    private void show() {
        if (customers != null && customers.size() > 0) {
            adapter = new MeCustomerAdapter3(mContext, customers);
            lv.setAdapter(adapter);
        }
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
