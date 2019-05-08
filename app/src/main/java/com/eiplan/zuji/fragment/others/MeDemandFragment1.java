package com.eiplan.zuji.fragment.others;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.DemandAdapter;
import com.eiplan.zuji.bean.DemandInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 正在进行中的需求
 */

public class MeDemandFragment1 extends BaseFragment {

    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.lv)
    ListView lv;

    private List<DemandInfo> demands;
    private String data;
    private DemandAdapter adapter;

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {
        data = SpUtils.getString(mContext, Constant.save_my_demand1);
        if (!TextUtils.isEmpty(data)) {
            demands = JsonUtils.parseDemands(data);
        }
        OkHttpUtils.post().url(Constant.getMyDemands1)
                .addParams("phone", SpUtils.getString(mContext, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SpUtils.putString(mContext, Constant.save_my_demand1, response);
                        demands = JsonUtils.parseDemands(response);
                        show();
                    }
                });

    }

    private void show() {
        llLoading.setVisibility(View.GONE);
        if (demands != null && demands.size() > 0) {
            adapter = new DemandAdapter(demands, mContext);
            lv.setAdapter(adapter);
        } else
            tvNo.setVisibility(View.VISIBLE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me_demand;
    }

}
