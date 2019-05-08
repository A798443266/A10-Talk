package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.HomeDocAdapter;
import com.eiplan.zuji.bean.DocInfo;
import com.eiplan.zuji.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecommendDocActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.titlebar_bg)
    RelativeLayout titlebarBg;
    @BindView(R.id.lv_doc)
    ListView lvDoc;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private List<DocInfo> docs;
    private HomeDocAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_doc);
        ButterKnife.bind(this);

        docs = (List<DocInfo>) getIntent().getSerializableExtra("docs");

        initView();

    }

    private void initView() {
        titlebarBg.setBackgroundColor(UIUtils.getColor(R.color.color_white));
        tvTitle.setText("推荐文档");
        tvTitle.setTextColor(UIUtils.getColor(R.color.color_system_blank));
        tvRight.setVisibility(View.GONE);
        ivBack.setImageResource(R.drawable.icon_back);

        adapter = new HomeDocAdapter(docs, this);
        lvDoc.setAdapter(adapter);

        lvDoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(RecommendDocActivity.this,DocDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("doc",docs.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
