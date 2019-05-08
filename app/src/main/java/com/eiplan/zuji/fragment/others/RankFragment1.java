package com.eiplan.zuji.fragment.others;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.ExpertDetailsActivity;
import com.eiplan.zuji.adapter.RankItemAdapter;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.view.MyListViewInCoor;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

//服务评价排行fragment

@SuppressLint("ValidFragment")
public class RankFragment1 extends BaseFragment {
    @BindView(R.id.listview)
    MyListViewInCoor listview;

    private List<ExpertInfo> experts = new ArrayList<>();
    private RankItemAdapter adapter;

    public RankFragment1(List<ExpertInfo> experts) {
        this.experts = experts;
    }

    @Override
    protected void initView() {

        adapter = new RankItemAdapter(experts, mContext, 1);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExpertInfo expert = experts.get(position);
                if (expert == null)
                    return;
                Intent intent = new Intent(mContext, ExpertDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("expert", expert);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_rank1;
    }

}
