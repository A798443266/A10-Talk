package com.eiplan.zuji.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.fragment.others.RankFragment1;
import com.eiplan.zuji.fragment.others.RankFragment2;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 排行榜
 */
public class RankActivity extends AppCompatActivity {

    @BindView(R.id.imagview)
    ImageView imagview;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.app_barlayout)
    AppBarLayout appBarlayout;


    private ArrayList<Fragment> fragments = new ArrayList<>();
    private RankFragment1 fragment1;
    private RankFragment2 fragment2;
    private String[] titles = new String[]{"服务评价排行", "浏览次数排行"};
    private List<ExpertInfo> experts1 = new ArrayList<>(); //服务评价专家列表
    private List<ExpertInfo> experts2 = new ArrayList<>();//浏览次数专家列表

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            fragment1 = new RankFragment1(experts1);
            fragment2 = new RankFragment2(experts2);

            fragments.add(fragment1);
            fragments.add(fragment2);
            tabLayout.setViewPager(viewpager, titles, RankActivity.this, fragments);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank);
        ButterKnife.bind(this);

        //从服务器获取专家信息
        getDataFromNet();
        //自定义的沉浸的方法
        setStatusBarTransparent(this);

    }

    private void getDataFromNet() {
        //先从从本地获取缓存的json数据
        String s = SpUtils.getString(this, Constant.Rank_Eva);
        String s1 = SpUtils.getString(this, Constant.Rank_Look);
        if (!TextUtils.isEmpty(s)) {
            experts1 = JsonUtils.parseExpertsByEva(s);
            if (!TextUtils.isEmpty(s1)) {
                experts2 = JsonUtils.parseExpertsByLook(s1);
            }
        }

        OkHttpUtils.get().url(Constant.expByEva).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                //把最新的放入缓存
                SpUtils.putString(RankActivity.this, Constant.Rank_Eva, response);
                //解析json
                experts1 = JsonUtils.parseExpertsByEva(response);
                Log.e("TAG", experts1.size() + "exp1");
                OkHttpUtils.get().url(Constant.expByLook).build().execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //把最新的放入缓存
                        SpUtils.putString(RankActivity.this, Constant.Rank_Look, response);
                        //解析json
                        experts2 = JsonUtils.parseExpertsByLook(response);
                        Log.e("TAG", experts1.size() + "exp2");
                    }
                });
            }
        });

        handler.sendEmptyMessage(1);

    }

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}
