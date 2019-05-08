package com.eiplan.zuji.fragment.others;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.PingFenActivity;
import com.eiplan.zuji.adapter.MeExpert1Adapter;
import com.eiplan.zuji.bean.EventIndustry;
import com.eiplan.zuji.bean.EventIndustry1;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.bean.MyExpertInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.view.MyRoundLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

public class MeExpert2Fragment extends BaseFragment {
    @BindView(R.id.lv)
    ListView lv;

    private List<MyExpertInfo> datas;
    private String data;
    private MeExpert1Adapter adapter;

    private int refresh = 0;

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        if (refresh == 0) {
            data = SpUtils.getString(mContext, Constant.orderWaitEva);
            if (!TextUtils.isEmpty(data)) {
                datas = JsonUtils.parseOrdering(data);
            }
        }

        OkHttpUtils.post().url(Constant.getOrderWaitEva)
                .addParams("phone", SpUtils.getString(mContext, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SpUtils.putString(mContext, Constant.orderWaitEva, response);
                        datas = JsonUtils.parseOrdering(response);
                        show();
                    }
                });
    }

    private void show() {
        if (datas != null && datas.size() > 0) {
            adapter = new MeExpert1Adapter(mContext, datas);
            lv.setAdapter(adapter);

            adapter.setOnItemRightClickListener(new MeExpert1Adapter.OnItemRightClickListener() {
                @Override
                public void itemRightClick(View view, int position) {
                    Intent intent = new Intent(getActivity(), PingFenActivity.class);
                    intent.putExtra("name", datas.get(position).getName());//传递评价的专家名称
                    intent.putExtra("id", datas.get(position).getId() + ""); //传递订单id
                    startActivityForResult(intent, 1);
                }
            });

        } else {

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                refresh = 1;
                datas.clear();
                initData();
                //发送消息，让已完成界面刷新
                EventBus.getDefault().post(new MyExpertInfo());
            }
        }
    }

    //接收消息,刷新页面
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent1(ExpertInfo e) {
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
