package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.UpdateVideoAdapter;
import com.eiplan.zuji.bean.VideoInfo;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.white.progressview.CircleProgressView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 上传视频列表的界面
 */

public class UpdateVideoActivity extends AppCompatActivity {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.fillInner)
    CircleProgressView fillInner;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_major)
    TextView tvMajor;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_error)
    TextView tvError;
    @BindView(R.id.ll_now)
    LinearLayout llNow;
    @BindView(R.id.ll_record)
    LinearLayout llRecord;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.iv_cover)
    ImageView ivCover;

    private UpdateVideoAdapter adapter;
    private String major;//选择的分类
    private String title;//标题
    private String introduce;//描述
    private String path; //视频路径
    private String thumPath;//视频缩略图地址
    private int IS = 0;//判断是不是从上传文件界面过来的

    private String data;
    private List<VideoInfo> videos = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_video);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        if (intent != null) {
            major = intent.getStringExtra("major");
            title = intent.getStringExtra("title");
            introduce = intent.getStringExtra("introduce");
            path = intent.getStringExtra("path");
            thumPath = intent.getStringExtra("thumPath");
            IS = intent.getIntExtra("is", 0);
        }
        initView();
        initData();

    }

    private void initData() {
        data = SpUtils.getString(this, Constant.my_up_videos);
        if (!data.isEmpty()) {
            videos = JsonUtils.parseVideos(data);
        }

        OkHttpUtils.post().url(Constant.getMyUpVedios)
                .addParams("phone", SpUtils.getString(UpdateVideoActivity.this, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SpUtils.putString(UpdateVideoActivity.this, Constant.my_up_videos, response);
                        //解析数据
                        videos = JsonUtils.parseVideos(response);
                        show();
                    }
                });

    }

    private void show() {
        if (videos != null && videos.size() > 0) {
            adapter = new UpdateVideoAdapter(this, videos);
            lv.setAdapter(adapter);
        } else {
            tvNo.setVisibility(View.VISIBLE);
        }
    }

    private void initView() {
        if (IS == 0) {
            llNow.setVisibility(View.GONE);
        } else {
            fillInner.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.GONE);
            tvMajor.setText("分类：" + major);
            tvTitle.setText(title);
            Glide.with(this).load(thumPath).error(R.drawable.vedio_bg).into(ivCover);
            tvTime.setVisibility(View.GONE);//到上传成功才显示时间
            update();
        }

    }

    private void update() {
        //向服务器上传信息
        File file = new File(path);//位置
        File file1 = new File(thumPath);//封面图
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "mutipart/form-data");
        int num = (int) (Math.random() * 1000000 + 1);
        OkHttpUtils.post().url(Constant.updateVideo)
                .headers(headers)
                .addFile("file", num + ".mp4", file)
                .addFile("cover", num + ".png", file1)
                .addParams("phone", SpUtils.getString(UpdateVideoActivity.this, Constant.current_phone))
                .addParams("title", title)
                .addParams("major", major)
                .addParams("introduce", introduce)
                .build()
                .execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        return null;
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        int p = (int) (progress * 100.0);
                        Log.e("TAG", p + "");
                        //改变进度条
                        fillInner.setProgress(p);

                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "失败");
                        tvError.setVisibility(View.VISIBLE);
                        fillInner.setVisibility(View.GONE);
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.e("TAG", "成功");

                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = new Date();
                        String str = sdf.format(date);
                        SystemClock.sleep(500);
                        tvTime.setVisibility(View.VISIBLE);
                        tvTime.setText(str);

                        fillInner.setVisibility(View.GONE);
                        tvError.setVisibility(View.VISIBLE);
                        tvError.setText("已完成");
                        tvError.setTextColor(UIUtils.getColor(R.color.color_system_blank));

                    }
                });


    }

    @OnClick(R.id.ll_back)
    public void onViewClicked() {
        finish();
    }
}
