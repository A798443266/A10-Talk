package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.white.progressview.CircleProgressView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 我上传的文档界面
 */

public class UpdateDocActivity extends AppCompatActivity {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_major)
    TextView tvMajor;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.fillInner)
    CircleProgressView fillInner;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.ll_now)
    LinearLayout llNow;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.ll_record)
    LinearLayout llRecord;

    private int IS = 0;//判断是不是从上传文件界面过来的
    private String major;//选择的分类
    private String name;//标题
    private String introduce;//描述
    private String path; //文件路径
    private String price; //文件价格

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_doc);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            major = intent.getStringExtra("major");
            name = intent.getStringExtra("name");
            introduce = intent.getStringExtra("introduce");
            path = intent.getStringExtra("path");
            price = intent.getStringExtra("price");
            IS = intent.getIntExtra("is", 0);
        }

        initView();
    }

    private void initView() {
        if (IS == 0) {
            llNow.setVisibility(View.GONE);
        } else {
            llNow.setVisibility(View.VISIBLE);
            fillInner.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.GONE);
            tvMajor.setText("分类：" + major);
            tvTitle.setText(name);
            tvTime.setVisibility(View.GONE);//到上传成功才显示时间
            update();
        }
    }

    private void update() {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "mutipart/form-data");

        String type = path.substring(path.lastIndexOf("."), path.length());
        int num = (int) (Math.random() * 1000000 + 1);
        File file = new File(path);
        OkHttpUtils.post().url(Constant.upFile)
                .headers(headers)
                .addFile("file", num + type, file)
                .addParams("phone", SpUtils.getString(UpdateDocActivity.this, Constant.current_phone))
                .addParams("major", major)
                .addParams("name", name)
                .addParams("introduce", introduce)
                .addParams("price", price)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "失败");
                        tvError.setVisibility(View.VISIBLE);
                        fillInner.setVisibility(View.GONE);

                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        int p = (int) (progress * 100.0);
                        Log.e("TAG", p + "");
                        //改变进度条
                        fillInner.setProgress(p);

                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.e("TAG", "成功");
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();
                        String str = sdf.format(date);
                        SystemClock.sleep(5000);
                        tvTime.setVisibility(View.VISIBLE);
                        tvTime.setText(str);
                        fillInner.setVisibility(View.GONE);
                        tvError.setVisibility(View.VISIBLE);
                        tvError.setText("上传成功！");
                        tvError.setTextColor(UIUtils.getColor(R.color.color_system_blank));

                    }
                });
    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finish();
    }
}
