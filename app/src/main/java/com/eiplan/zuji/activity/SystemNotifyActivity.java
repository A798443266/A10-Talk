package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.SystemNewsAdapter;
import com.eiplan.zuji.bean.SystemNotificationInfo;
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

public class SystemNotifyActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.title)
    RelativeLayout title;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.ll_loading)
    LinearLayout llLoading;
    @BindView(R.id.tv_no)
    TextView tvNo;

    private List<SystemNotificationInfo> msgs;
    private SystemNewsAdapter adapter;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_notify);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {

        data = SpUtils.getString(this, Constant.save_system_news);
        if (!TextUtils.isEmpty(data)) {
            msgs = JsonUtils.parseSystemNews(data);
        }

        OkHttpUtils.post().url(Constant.getSystemNews)
                .addParams("phone", SpUtils.getString(SystemNotifyActivity.this, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SpUtils.putString(SystemNotifyActivity.this, Constant.save_system_news, response);
                        msgs = JsonUtils.parseSystemNews(response);
                        show();
                    }
                });

    }

    private void show() {
        llLoading.setVisibility(View.GONE);
        if (msgs != null && msgs.size() > 0) {
            adapter = new SystemNewsAdapter(msgs, this);
            lv.setAdapter(adapter);
        } else {
            tvNo.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
