package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.DemandInfo;
import com.hyphenate.easeui.EaseConstant;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DemandDetailsActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.tv_expert)
    TextView tvExpert;
    @BindView(R.id.tv_budget)
    TextView tvBudget;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.tv_city)
    TextView tvCity;
    @BindView(R.id.tv_company)
    TextView tvCompany;
    @BindView(R.id.ll_company)
    LinearLayout llCompany;
    @BindView(R.id.tv_details)
    TextView tvDetails;
    @BindView(R.id.ll_background)
    LinearLayout llBackground;
    @BindView(R.id.ll_details)
    LinearLayout llDetails;
    @BindView(R.id.tv_requst)
    TextView tvRequst;
    @BindView(R.id.ll_requst)
    LinearLayout llRequst;
    @BindView(R.id.tv_look)
    TextView tvLook;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_background)
    TextView tvBackground;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.ll_find)
    LinearLayout llFind;

    private DemandInfo demand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demand_details);
        ButterKnife.bind(this);

        demand = (DemandInfo) getIntent().getSerializableExtra("demand");
        initView();
    }

    private void initView() {
        tvExpert.setText(demand.getExpert());
        tvCity.setText(demand.getCity());
        tvCompany.setText(demand.getCompany());
        tvBudget.setText(demand.getBudget());
        tvRequst.setText(demand.getRequirement());
        if (!demand.getBackground().equals("无")) {
            tvBackground.setText("     " + demand.getBackground());
        } else
            llBackground.setVisibility(View.GONE);
        if (!demand.getDetails().equals("无")) {
            tvDetails.setText("     " + demand.getDetails());
        } else
            llDetails.setVisibility(View.GONE);
        if (!demand.getRequirement().equals("无")) {
            tvBackground.setText("     " + demand.getRequirement());
        } else
            llRequst.setVisibility(View.GONE);
        if (demand.getState() == 0) {
            tvState.setText("寻找中");
            llFind.setVisibility(View.GONE);
        } else {
            tvState.setText("已找到");
            llFind.setVisibility(View.VISIBLE);
            btn.setVisibility(View.GONE);
        }
        tvLook.setText(demand.getLook() + "");
        tvTime.setText(demand.getTime());

    }

    @OnClick({R.id.iv_back, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn:
                Intent intent = new Intent(this, ChatActivity.class);
                // 传递环信id，即电话号码
                intent.putExtra(EaseConstant.EXTRA_USER_ID, demand.getPhone());
//                intent.putExtra("name", demand.getName()); //传递名称过去
                startActivity(intent);
                break;
        }
    }
}
