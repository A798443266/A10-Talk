package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.SelectIndustryAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectIndustryActivity extends AppCompatActivity {

    @BindView(R.id.gv)
    GridView gv;

    private String[] names = {"石油和天然气工业", "化学工业", "电工技术", "金属工业"
            , "机械工业", "交通运输", "建筑科学", "冶金工业", "水利工程", "能源工程", "自动工程", "环境安全"};

    private int[] icons = {R.drawable.icon_industry1, R.drawable.icon_industry2, R.drawable.icon_industry3, R.drawable.icon_industry4
            , R.drawable.icon_industry5, R.drawable.icon_industry6, R.drawable.icon_industry7, R.drawable.icon_industry8,
            R.drawable.icon_industry9, R.drawable.icon_industry10, R.drawable.icon_industry11, R.drawable.icon_industry12};

    private SelectIndustryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_industry);
        ButterKnife.bind(this);

        initData();
    }

    private void initData() {
        adapter = new SelectIndustryAdapter(this, names, icons);
        gv.setAdapter(adapter);
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @OnClick(R.id.tv_ignore)
    public void onViewClicked() {
        finish();
    }
}
