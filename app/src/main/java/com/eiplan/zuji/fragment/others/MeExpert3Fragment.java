package com.eiplan.zuji.fragment.others;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.PingFenActivity;
import com.eiplan.zuji.adapter.MeExpert1Adapter;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.bean.MyExpertInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class MeExpert3Fragment extends BaseFragment {
    @BindView(R.id.lv)
    ListView lv;

    private List<MyExpertInfo> datas;
    private MeExpert1Adapter adapter;
    private String data;

    private int refresh = 0;

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        if (refresh == 0) {
            data = SpUtils.getString(mContext, Constant.orderEnd);
            if (!TextUtils.isEmpty(data)) {
                datas = JsonUtils.parseOrdering(data);
            }
        }

        OkHttpUtils.post().url(Constant.getOrderEnd)
                .addParams("phone", SpUtils.getString(mContext, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SpUtils.putString(mContext, Constant.orderEnd, response);
                        datas = JsonUtils.parseOrdering(response);
                        show();
                    }
                });
    }

    private void show() {
        if (datas != null && datas.size() > 0) {
            adapter = new MeExpert1Adapter(mContext, datas);
            adapter.setType(1);//设置结束标志，取消的字体框背景，设置字体为黑色
            lv.setAdapter(adapter);

        } else {

        }
    }

    //接收消息,刷新页面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent1(MyExpertInfo e) {
        refresh = 1;
        datas.clear();
        initData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me_expert2;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
