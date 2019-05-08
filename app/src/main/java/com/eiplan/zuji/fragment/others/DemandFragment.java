package com.eiplan.zuji.fragment.others;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import okhttp3.Call;

public class DemandFragment extends BaseFragment {
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.lv)
    PullToRefreshListView lv;

    private DemandAdapter adapter;
    private List<DemandInfo> demands;
    private String data;

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {
        String data = SpUtils.getString(mContext, Constant.save_demans);
        if (!TextUtils.isEmpty(data)) {
            demands = JsonUtils.parseDemands(data);
        }
        OkHttpUtils.get().url(Constant.getDemands)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                show();
            }

            @Override
            public void onResponse(String response, int id) {
                SpUtils.putString(mContext, Constant.save_demans, response);
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

                }
            });
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(mContext, DemandDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("demand", demands.get(position - 1));//pulltorefresh占了一个头布局
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            setListener();
        } else {

        }
    }

    private void setListener() {
        //支持上拉和下拉
        lv.setMode(PullToRefreshBase.Mode.BOTH);
        // 设置上下刷新 使用 OnRefreshListener2
        lv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                // 主线程
                // 模拟数据加载,,加载时间不要太短 否则,会出现一直刷新的情况.
                lv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        initData();
                        lv.onRefreshComplete();//加载完记得调用onRefreshComplete();否则将无法结束加载.
                    }
                }, 1500);
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                lv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        lv.onRefreshComplete();//加载完记得调用onRefreshComplete();否则将无法结束加载.
                    }
                }, 1500);
            }
        });

        // 设置PullRefreshListView上提加载时的加载提示
        lv.getLoadingLayoutProxy(false, true).setPullLabel("上拉加载更多.");
        lv.getLoadingLayoutProxy(false, true).setRefreshingLabel("正在加载数据...");
        lv.getLoadingLayoutProxy(false, true).setReleaseLabel("松开加载更多...");
        // 设置PullRefreshListView下拉加载时的加载提示
        lv.getLoadingLayoutProxy(true, false).setPullLabel("下拉刷新...");
        lv.getLoadingLayoutProxy(true, false).setRefreshingLabel("正在刷新...");
        lv.getLoadingLayoutProxy(true, false).setReleaseLabel("松开刷新...");

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_demand;
    }

}
