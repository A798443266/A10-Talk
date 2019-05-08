package com.eiplan.zuji.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.MyPointAdapter;
import com.eiplan.zuji.bean.PointInfo;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.view.MyListView;
import com.eiplan.zuji.view.MyListViewInCoor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

/**
 * 我的积分
 */import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class MypointActivity extends AppCompatActivity {

    @BindView(R.id.tv_point)
    TextView tvPoint;
    @BindView(R.id.lv)
    MyListView lv;

    private MyPointAdapter adapter;
    private List<PointInfo> datas;
    private String data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypoint);
        ButterKnife.bind(this);
        //自定义沉浸式方法
        setStatusBarTransparent(this);
        initData();
    }

    private void initData() {

        data = SpUtils.getString(this,Constant.save_my_points);
        if(!TextUtils.isEmpty(data)){
            datas = JsonUtils.parseMyPoints(data);
        }
        OkHttpUtils.post().url(Constant.getPoints)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SpUtils.putString(MypointActivity.this,Constant.save_my_points,response);
                        datas = JsonUtils.parseMyPoints(response);
                        show();
                    }
                });
    }

    private void show() {
        if(datas != null && datas.size() > 0){
            adapter = new MyPointAdapter(datas,this);
            lv.setAdapter(adapter);
        }
    }

    //实现状态栏沉浸式
    public void setStatusBarTransparent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
