package com.eiplan.zuji.fragment.others;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.DemandDetailsActivity;
import com.eiplan.zuji.adapter.DemandAdapter;
import com.eiplan.zuji.bean.DemandInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class DemandFragment1 extends BaseFragment {
    @BindView(R.id.lv)
    PullToRefreshListView lv;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    private DemandAdapter adapter;
    private List<DemandInfo> demands;

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {
        String data = SpUtils.getString(mContext, Constant.save_demans1);
        if (!TextUtils.isEmpty(data)) {
            demands = JsonUtils.parseDemands(data);
        }

        OkHttpUtils.get().url(Constant.getDemandsFinish)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                show();
            }

            @Override
            public void onResponse(String response, int id) {
                SpUtils.putString(mContext, Constant.save_demans1, response);
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

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, DemandDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("demand", demands.get(position-1));//pulltorefresh占了一个头布局
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        } else {

        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_demand;
    }
}
