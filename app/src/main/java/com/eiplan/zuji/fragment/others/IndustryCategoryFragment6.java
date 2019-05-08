package com.eiplan.zuji.fragment.others;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.IndustryCategoryAdapter;
import com.eiplan.zuji.bean.EventExpert;
import com.eiplan.zuji.bean.EventIndustry;
import com.eiplan.zuji.bean.EventIndustry1;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class IndustryCategoryFragment6 extends BaseFragment {
    @BindView(R.id.gridview)
    GridView gridview;

    private IndustryCategoryAdapter adapter;
    private List<String> datas;
    private List<ExpertInfo> experts;

    private int first;
    private int second;

    @Override
    protected void initView() {
        datas = new ArrayList<>();
        datas.add("节能技术");
        datas.add("新能源技术");
        datas.add("太阳能技术");
        datas.add("汽轮机技术");
        datas.add("热机技术");
        datas.add("内燃机工程");
        datas.add("导热油锅技术");
        datas.add("工锅技术");
        datas.add("动力工程");

        adapter = new IndustryCategoryAdapter(datas, mContext);
        gridview.setAdapter(adapter);

        EventBus.getDefault().register(this);

        setListener();
    }

    private void setListener() {
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.setSelection(position);
                adapter.notifyDataSetChanged();

                //联网请求数据或从自己的本地数据库中获取
                OkHttpUtils.post().url(Constant.SEARCH_EXPERT).addParams("key", datas.get(position)).build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {

                            }

                            @Override
                            public void onResponse(String response, int id) {
                                //发送专家列表到ExpertFragment显示
                                experts = JsonUtils.parseExperts(response);
                                EventBus.getDefault().post(new EventExpert(experts));
                            }
                        });
            }
        });
    }

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent(EventIndustry1 e) {
        first = e.getFirst();
        second = e.getSencond();
        if (first == 5) {
            adapter.setSelection(second);
            adapter.notifyDataSetChanged();
            //联网请求数据或从自己的本地数据库中获取
            OkHttpUtils.post().url(Constant.SEARCH_EXPERT).addParams("key", datas.get(second)).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {

                        }

                        @Override
                        public void onResponse(String response, int id) {
                            //发送专家列表到ExpertFragment显示
                            experts = JsonUtils.parseExperts(response);
                            EventBus.getDefault().post(new EventExpert(experts));
                        }
                    });
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_industry1;
    }
}
