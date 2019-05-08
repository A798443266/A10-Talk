package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.RecommendMsgAdapter;
import com.eiplan.zuji.bean.RecommendMSG;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 推荐的讯息界面
 */

public class RecommendMSGActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;

    private List<RecommendMSG> msgs;
    private RecommendMsgAdapter adapter;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_msg);
        ButterKnife.bind(this);

        msgs = new ArrayList<>();

        initData();
    }

    private void initData() {

        data = SpUtils.getString(this, Constant.save_recommend_msgs);
        if (!TextUtils.isEmpty(data)) {
            msgs = JsonUtils.parseRecommends(data);
        }

        OkHttpUtils.get().url(Constant.recommends)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                show();
            }

            @Override
            public void onResponse(String response, int id) {
                SpUtils.putString(RecommendMSGActivity.this, Constant.save_recommend_msgs, response);
                msgs = JsonUtils.parseRecommends(response);
                show();
            }
        });

    }

    private void show() {
        llLoading.setVisibility(View.GONE);
        if (msgs != null && msgs.size() > 0) {
            adapter = new RecommendMsgAdapter(msgs, this);
            lv.setAdapter(adapter);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    RecommendMSG msg = msgs.get(position);
                    if (msg == null)
                        return;
                    Intent intent = new Intent(RecommendMSGActivity.this, MsgDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("msg", msg);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
