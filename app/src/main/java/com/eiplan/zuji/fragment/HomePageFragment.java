package com.eiplan.zuji.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.DocDetailsActivity;
import com.eiplan.zuji.activity.ExpertDetailsActivity;
import com.eiplan.zuji.activity.RecommendDocActivity;
import com.eiplan.zuji.activity.ReleaseProblemActivity;
import com.eiplan.zuji.activity.SearchActivity;
import com.eiplan.zuji.activity.SelectIndustryActivity;
import com.eiplan.zuji.activity.VedioDetailsActivity;
import com.eiplan.zuji.adapter.HomeDocAdapter;
import com.eiplan.zuji.adapter.HomeExpertAdapter;
import com.eiplan.zuji.adapter.HomeIndustryAdapter;
import com.eiplan.zuji.adapter.HomeProblemAdapter;
import com.eiplan.zuji.adapter.HomeVideoAdapter;
import com.eiplan.zuji.adapter.LiveRoomAdapter;
import com.eiplan.zuji.bean.DocInfo;
import com.eiplan.zuji.bean.EventIndustry;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.bean.LiveRoom;
import com.eiplan.zuji.bean.VideoInfo;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.eiplan.zuji.view.MoreWindow;
import com.eiplan.zuji.view.MyGridView;
import com.eiplan.zuji.view.MyListView;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;
import com.zhy.magicviewpager.transformer.ScaleInTransformer;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 首页
 */
public class HomePageFragment extends BaseFragment {
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.app_barlayout)
    AppBarLayout appBarlayout;
    @BindView(R.id.ll_bg_title)
    LinearLayout llBgTitle;
    @BindView(R.id.tv_expert_all)
    TextView tvExpertAll;
    @BindView(R.id.ll_findall_expert)
    LinearLayout llFindallExpert;
    @BindView(R.id.gv_home_industry)
    GridView gvHomeIndustry;
    @BindView(R.id.lv_home_doc)
    MyListView lvHomeDoc;
    @BindView(R.id.lv_home_expert)
    MyListView lvHomeExpert;
    @BindView(R.id.lv_home_problem)
    MyListView lvHomeProblem;
    @BindView(R.id.ll_find_expert)
    LinearLayout llFindExpert;
    @BindView(R.id.ll_problems)
    LinearLayout llProblems;
    @BindView(R.id.ll_find_doc)
    LinearLayout llFindDoc;
    @BindView(R.id.ll_tuijian)
    LinearLayout llTuijian;
    @BindView(R.id.ll_all_doc)
    LinearLayout llAllDoc;
    @BindView(R.id.iv_refresh)
    ImageView ivRefresh;
    @BindView(R.id.ll_all_video)
    LinearLayout llAllVideo;
    @BindView(R.id.viewpager)
    ViewPager viewpager;
    @BindView(R.id.ll_point_group)
    LinearLayout llPointGroup;
    @BindView(R.id.gv_live)
    MyGridView gvLive;
    @BindView(R.id.iv_refresh1)
    ImageView ivRefresh1;

    private List<ExpertInfo> experts = new ArrayList<>();//专家列表
    private List<DocInfo> docs = new ArrayList<>();//首页显示的推荐文档列表（最多五个）
    private List<DocInfo> docsAll = new ArrayList<>();//推荐文档列表（全部）
    private List<VideoInfo> vidios = new ArrayList<>();//推荐文档列表（5个）
    private List<LiveRoom> rooms = new ArrayList<>();//推荐直播

    private LayoutInflater lf;
    private List<View> views = new ArrayList<>();
    private TextView[] video_titles = new TextView[5];
    private ImageView[] video_images = new ImageView[5];
    private HomeVideoAdapter homeVideoAdapter;
    private LiveRoomAdapter homeLiveAdapter;

    private String major;
    private int isExpert;

    /**
     * 上一次高亮显示的位置
     */
    private int prePosition = 0;
    /**
     * 是否已经滚动
     */
    private boolean isDragging = false;

    @Override
    protected void initView() {
        changeToolBar();
        setListener();//设置各个点击监听
        lf = getLayoutInflater();
        int a = SpUtils.getInt(mContext, Constant.INDUSTRY);
        if (a == 1) {
            startActivity(new Intent(mContext, SelectIndustryActivity.class));
        }


        //获取用户行业
        isExpert = SpUtils.getInt(mContext, Constant.current_isexpert);
        if (isExpert == 1) {//专家
            major = SpUtils.getString(mContext, Constant.current_major);
        } else { //用户
            major = SpUtils.getString(mContext, Constant.current_industry);
        }
    }

    private void setListener() {
        rlTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchActivity.class);
                intent.putExtra("content", 1); //1代表用户要搜索的是专家
                startActivity(intent);
            }
        });

        lvHomeDoc.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DocInfo doc = docs.get(position);
                if (doc == null)
                    return;
                Intent intent = new Intent(mContext, DocDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("doc", doc);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        lvHomeExpert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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

        llFindallExpert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(1);//发送eventbus消息，让界面转到专家界面
            }
        });
    }

    private void changeToolBar() {
        //滑动改变搜索框的透明度和按钮的变化
        appBarlayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset == 0) {
                    llBgTitle.setBackgroundColor(Color.argb(0, 221, 48, 10));
                } else if (Math.abs(verticalOffset) >= appBarLayout.getTotalScrollRange()) {
                    llBgTitle.setBackgroundColor(Color.argb(255, 39, 148, 233));
                } else {
                    int alpha = (int) (255 - verticalOffset / (float) appBarLayout.getTotalScrollRange() * 255);
                    llBgTitle.setBackgroundColor(Color.argb(alpha, 39, 148, 233));
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_homepage;
    }

    @Override
    public void initData() {
        super.initData();
        initBanner();
        initIndustry();//设置工业分类信息
        initVideo();//设置推荐视频信息
        initDoc();//初始化文档信息
        initExpert();//初始化推荐专家
        initProblem();//初始化推荐问题
        initLive();//初始化推荐直播

    }

    private void initVideo() {

        String data = SpUtils.getString(mContext, Constant.Videos);
        if (!TextUtils.isEmpty(data)) {
            List<VideoInfo> videos1 = JsonUtils.parseVideos(data);
            if (videos1 != null) {
                if (videos1.size() <= 5) {
                    vidios.addAll(videos1);//首页只显示最多5个推荐
                } else {
                    for (int i = 0; i < 5; i++) {
                        vidios.add(videos1.get(i));
                    }
                }
            }
        }
        //从服务器获取数据
        OkHttpUtils.get().url(Constant.getVideos).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                showVideos();
            }

            @Override
            public void onResponse(String response, int id) {
                //存入最新数据
                SpUtils.putString(mContext, Constant.Videos, response);
                //解析数据
                List<VideoInfo> videos1 = JsonUtils.parseVideos(response);
                vidios.clear();
                if (videos1 != null) {
                    if (videos1.size() <= 5) {
                        vidios.addAll(videos1);//首页只显示最多5个推荐
                    } else {
                        for (int i = 0; i < 5; i++) {
                            vidios.add(videos1.get(i));
                        }
                    }
                }
                showVideos();


            }
        });
    }

    private void showVideos() {
        //显示viewpager
        for (int i = 0; i < vidios.size(); i++) {
            views.add(lf.inflate(R.layout.home_video, null));
            video_titles[i] = views.get(i).findViewById(R.id.tv_title);
            video_images[i] = views.get(i).findViewById(R.id.iv_cover);
            video_titles[i].setText(vidios.get(i).getTitle());
            Glide.with(mContext).load(vidios.get(i).getCover()).error(R.drawable.p).into(video_images[i]);
            //添加指示器
            ImageView point = new ImageView(mContext);
            point.setBackgroundResource(R.drawable.point_selector);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(UIUtils.dp2px(10), UIUtils.dp2px(2));//设置指示器的高宽（像素）
            if (i == 0) {
                point.setEnabled(true); //显示红色
            } else {
                point.setEnabled(false);//显示灰色
                params.leftMargin = UIUtils.dp2px(4);
            }
            point.setLayoutParams(params);
            llPointGroup.addView(point);
        }

        homeVideoAdapter = new HomeVideoAdapter(views);
        viewpager.setPageMargin(10);
        viewpager.setOffscreenPageLimit(3);//>=3
        viewpager.setPageTransformer(true, new ScaleInTransformer());
        if (views != null && views.size() > 0) {
            viewpager.setAdapter(homeVideoAdapter);
            //增加监听
            viewpager.addOnPageChangeListener(new MyOnPageChangeListener());
            homeVideoAdapter.setOnItemClickListenr(new HomeVideoAdapter.OnItemClickListenr() {
                @Override
                public void onClick(View v, int position) {
                    VideoInfo video = vidios.get(position);
                    Intent intent = new Intent(mContext, VedioDetailsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("video", video);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

    }

    private void initProblem() {
        List<String[]> datas = new ArrayList<>();
        datas.add(new String[]{"abaqus模拟出现以下警告,但status是caompleted,衬套没有完全压入，只是一小部分怎么办？", "暂无回答，期待您的回答"});
        datas.add(new String[]{"Abaqus如何加力矩随转角变化的载荷？", "建立一个柱坐标系，然后载荷力矩里面定义函数随角度变化"});
        HomeProblemAdapter adapter = new HomeProblemAdapter(datas, mContext);
        lvHomeProblem.setAdapter(adapter);
    }

    private void initExpert() {

        //先从从本地获取缓存的json数据
        String s = SpUtils.getString(mContext, Constant.Recommend_experts);
        if (!TextUtils.isEmpty(s)) {
            List<ExpertInfo> expert1 = JsonUtils.parseExpertsByMajor(s);
            if (expert1 != null) {
                if (expert1.size() <= 5) {
                    experts.addAll(expert1);//首页只显示最多5个推荐
                } else {
                    for (int i = 0; i < 5; i++) {
                        experts.add(expert1.get(i));
                    }
                }
            }

        }

        OkHttpUtils.post().url(Constant.recommendExperts)
                .addParams("key", major)
                .addParams("phone", SpUtils.getString(mContext, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HomeExpertAdapter adapter = new HomeExpertAdapter(experts, mContext);
                        lvHomeExpert.setAdapter(adapter);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", response);
                        //把最新的放入缓存
                        SpUtils.putString(mContext, Constant.Recommend_experts, response);
                        List<ExpertInfo> expert1 = JsonUtils.parseExpertsByMajor(response);
                        experts.clear();
                        if (expert1.size() < 5) {
                            experts.addAll(expert1);//首页只显示最多5个推荐
                        } else {
                            for (int i = 0; i < 5; i++) {
                                experts.add(expert1.get(i));
                            }
                        }
                        HomeExpertAdapter adapter = new HomeExpertAdapter(experts, mContext);
                        lvHomeExpert.setAdapter(adapter);
                    }
                });

    }

    private void initDoc() {
        //先从从本地获取缓存的json数据
        String s = SpUtils.getString(mContext, Constant.Recommend_docs);
        if (!TextUtils.isEmpty(s)) {
            docsAll = JsonUtils.parseDocsByMajor(s);
            if (docsAll != null) {
                if (docsAll.size() <= 5) {
                    docs.addAll(docsAll);//首页只显示最多5个推荐
                } else {
                    for (int i = 0; i < 5; i++) {
                        docs.add(docsAll.get(i));
                    }
                }
            }

        }
        OkHttpUtils.post().url(Constant.recommendFile).addParams("key", major).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        HomeDocAdapter adapter = new HomeDocAdapter(docs, mContext);
                        lvHomeDoc.setAdapter(adapter);
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", response);
                        //把最新的放入缓存
                        SpUtils.putString(mContext, Constant.Recommend_docs, response);
                        List<DocInfo> doc1 = JsonUtils.parseDocsByMajor(response);
                        docs.clear();
                        if (doc1.size() <= 5) {
                            docs.addAll(doc1);//首页只显示最多5个推荐
                        } else {
                            for (int i = 0; i < 5; i++) {
                                docs.add(doc1.get(i));
                            }
                        }
                        HomeDocAdapter adapter = new HomeDocAdapter(docs, mContext);
                        lvHomeDoc.setAdapter(adapter);
                    }
                });


    }

    //初始化推荐直播
    private void initLive() {
        OkHttpUtils.get().url(Constant.getlives)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        List<LiveRoom> rooms1 = JsonUtils.parseLiveRooms(response);
                        if (rooms1 != null && rooms1.size() > 0) {
                            if (rooms1.size() > 4) {
                                for (int i = 0; i < 4; i++) {
                                    rooms.add(rooms1.get(i));
                                }
                            } else {
                                rooms.addAll(rooms1);
                            }
                            homeLiveAdapter = new LiveRoomAdapter(rooms, mContext);
                            gvLive.setAdapter(homeLiveAdapter);
                        }
                    }
                });
    }

    private void initIndustry() {
        List<String> datas = new ArrayList<>();
        datas.add("气体净化");
        datas.add("动力工程");
        datas.add("液压工业");
        datas.add("化工冶金");
        datas.add("自动化加工");
        datas.add("太阳能技术");
        datas.add("城市规划");
        datas.add("机床技术");
        datas.add("热加工");

        HomeIndustryAdapter adapter = new HomeIndustryAdapter(datas, mContext);
        gvHomeIndustry.setAdapter(adapter);

        gvHomeIndustry.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                    EventBus.getDefault().post(new EventIndustry(3, 8));
                else if (position == 1)
                    EventBus.getDefault().post(new EventIndustry(5, 8));
                else if (position == 2)
                    EventBus.getDefault().post(new EventIndustry(2, 5));
                else if (position == 3)
                    EventBus.getDefault().post(new EventIndustry(1, 1));
                else if (position == 4)
                    EventBus.getDefault().post(new EventIndustry(0, 9));
                else if (position == 5)
                    EventBus.getDefault().post(new EventIndustry(5, 2));
                else if (position == 6)
                    EventBus.getDefault().post(new EventIndustry(4, 0));
                else if (position == 7)
                    EventBus.getDefault().post(new EventIndustry(0, 6));
                else if (position == 8)
                    EventBus.getDefault().post(new EventIndustry(0, 2));

                EventBus.getDefault().post(1);//转到专家界面
            }
        });

    }

    private void initBanner() {

        //设置Banner的数据
        //得到图片集合地址
        List<Integer> imagesUrl = new ArrayList<Integer>();
        imagesUrl.add(R.drawable.home_banner1);
        imagesUrl.add(R.drawable.home_banner3);
        imagesUrl.add(R.drawable.home_banner2);
        banner.setImages(imagesUrl);
        //设置循环指示点
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //设置轮播时间
        banner.setDelayTime(3500);
        //设置翻转动画效果
//        banner.setBannerAnimation(Transformer.ZoomOutSlide);
        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                Glide.with(mContext).load(path).into(imageView);
            }
        });
        //设置item的点击事件
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Toast.makeText(mContext, "position==" + position, Toast.LENGTH_SHORT).show();
            }
        });
        banner.start();
    }


    @OnClick({R.id.ll_find_expert, R.id.ll_problems, R.id.ll_find_doc, R.id.ll_tuijian, R.id.ll_all_doc})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_find_expert:
                EventBus.getDefault().post(1);
                break;
            case R.id.ll_problems:
                startActivity(new Intent(mContext, ReleaseProblemActivity.class));
                break;
            case R.id.ll_find_doc:
                Intent intent1 = new Intent(mContext, SearchActivity.class);
                intent1.putExtra("content", 2);//传过去2代表用户要搜索的是文档
                startActivity(intent1);
                break;
            case R.id.ll_tuijian:
//                startActivity(new Intent(mContext, RecommendMSGActivity.class));
                MoreWindow mMoreWindow = new MoreWindow((Activity) mContext);
                mMoreWindow.init();
                mMoreWindow.showMoreWindow(view, 100);
                break;
            case R.id.ll_all_doc:
                Intent intent = new Intent(mContext, RecommendDocActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("docs", (Serializable) docsAll);//传递所有推荐文档
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }


    @OnClick({R.id.ll_all_vedio, R.id.ll_all_liveroom})
    public void onViewClicked1(View view) {
        Animation animation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(500);
        //设置线性变化
        animation.setInterpolator(new LinearInterpolator());//线性变换，更加流畅
        //设置动画重复次数
        animation.setRepeatCount(5);
        switch (view.getId()) {
            case R.id.ll_all_vedio:
                ivRefresh1.startAnimation(animation);
                break;
            case R.id.ll_all_liveroom:
                ivRefresh.startAnimation(animation);
                break;
        }
    }



    class MyOnPageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int i, float v, int i1) {
        }

        @Override
        public void onPageSelected(int i) {
            //把上一个高亮的设置默认-灰色
            llPointGroup.getChildAt(prePosition).setEnabled(false);
            //当前的设置为高亮-红色
            llPointGroup.getChildAt(i).setEnabled(true);
            prePosition = i;
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }
    }


}
