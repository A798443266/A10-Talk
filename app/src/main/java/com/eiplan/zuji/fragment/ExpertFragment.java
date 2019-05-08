package com.eiplan.zuji.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.ExpertDetailsActivity;
import com.eiplan.zuji.activity.RankActivity;
import com.eiplan.zuji.activity.SearchActivity;
import com.eiplan.zuji.adapter.HomeExpertAdapter;
import com.eiplan.zuji.bean.EventExpert;
import com.eiplan.zuji.bean.EventIndustry;
import com.eiplan.zuji.bean.EventIndustry1;
import com.eiplan.zuji.bean.ExpertInfo;
import com.eiplan.zuji.fragment.others.IndustryCategoryFragment1;
import com.eiplan.zuji.fragment.others.IndustryCategoryFragment2;
import com.eiplan.zuji.fragment.others.IndustryCategoryFragment3;
import com.eiplan.zuji.fragment.others.IndustryCategoryFragment4;
import com.eiplan.zuji.fragment.others.IndustryCategoryFragment5;
import com.eiplan.zuji.fragment.others.IndustryCategoryFragment6;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.flyco.tablayout.SlidingTabLayout;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 专家
 */
public class ExpertFragment extends Fragment {
    @BindView(R.id.ll_search)
    LinearLayout llSearch;
    @BindView(R.id.ll_rank)
    LinearLayout llRank;
    @BindView(R.id.tab_layout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.vp_industry)
    ViewPager vpIndustry;
    @BindView(R.id.lv_expert)
    ListView lvExpert;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.tv_no)
    View tvNo;

    Unbinder unbinder;
    @BindView(R.id.menu1)
    DropDownMenu menu1;
    @BindView(R.id.menu2)
    DropDownMenu menu2;
    @BindView(R.id.menu3)
    DropDownMenu menu3;
    @BindView(R.id.ll)
    LinearLayout ll;
    private Context mContext;

    private HomeExpertAdapter expertAdapter;//专家适配器
    private int lastVisibleItemPosition = 0;// 标记上次滑动位置
    private List<ExpertInfo> experts = new ArrayList<>();//专家列表
    private int first;//用于切换工业分类
    private int second;
    private String data; //全部专家的缓存json数据

    private ArrayList<Fragment> fragments;
    private IndustryCategoryFragment1 fragment1;
    private IndustryCategoryFragment2 fragment2;
    private IndustryCategoryFragment3 fragment3;
    private IndustryCategoryFragment4 fragment4;
    private IndustryCategoryFragment5 fragment5;
    private IndustryCategoryFragment6 fragment6;
    private String[] titles = new String[]{"金属工艺", "冶金工艺", "机械工业", "化学工业", "建筑科学", "能源与动力工程"};

    private String[] majors = new String[]{"全部", "2年以下", "2-5年", "5-7年", "7-10年", "10-15年", "15年以上"};
    private String[] citys = new String[]{"全部", "北京", "上海", "广州", "深圳", "杭州", "南京", "成都", "沈阳", "天津", "西安", "其他"};
    private String[] jobs = new String[]{"全部", "高级工程师", "轨道工程师", "建筑工程师", "仿真工程师", "运输工程师", "项目经理", "其他"};
    private List<String[]> list1 = new ArrayList<>();
    private List<String[]> list2 = new ArrayList<>();
    private List<String[]> list3 = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(mContext, R.layout.fragment_expert, null);
        unbinder = ButterKnife.bind(this, view);

        list1.add(majors);
        list2.add(citys);
        list3.add(jobs);
        initView();
        initMenu();
        initData();
        return view;
    }

    private void initData() {

        data = SpUtils.getString(mContext, Constant.All_Experts);
        if (!TextUtils.isEmpty(data)) {
            experts = JsonUtils.parseAllExperts(data);
        }

        OkHttpUtils.post().url(Constant.getAllExperts)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        //
                        SpUtils.putString(mContext, Constant.All_Experts, response);
                        //解析
                        experts = JsonUtils.parseAllExperts(response);
                        show();
                    }
                });
    }

    //显示
    private void show() {
        expertAdapter = new HomeExpertAdapter(experts, mContext);
        lvExpert.setAdapter(expertAdapter);
    }


    protected void initView() {

        EventBus.getDefault().register(this);

        fragments = new ArrayList<>();
        fragment1 = new IndustryCategoryFragment1();
        fragment2 = new IndustryCategoryFragment2();
        fragment3 = new IndustryCategoryFragment3();
        fragment4 = new IndustryCategoryFragment4();
        fragment5 = new IndustryCategoryFragment5();
        fragment6 = new IndustryCategoryFragment6();
        fragments.add(fragment1);
        fragments.add(fragment2);
        fragments.add(fragment3);
        fragments.add(fragment4);
        fragments.add(fragment5);
        fragments.add(fragment6);

        vpIndustry.clearDisappearingChildren();
        tabLayout.setViewPager(vpIndustry, titles, (FragmentActivity) mContext, fragments);

        setListener();
    }

    private void setListener() {
        lvExpert.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(mContext, ExpertDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("expert", experts.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        lvExpert.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE:// 是当屏幕停止滚动时
                        // 判断滚动到顶部
                        if (lvExpert.getFirstVisiblePosition() == 0) {
                            ll.setVisibility(View.GONE);
                            vpIndustry.setVisibility(View.VISIBLE);
                            tabLayout.setVisibility(View.VISIBLE);
                            view2.setVisibility(View.VISIBLE);
                            view3.setVisibility(View.VISIBLE);
                        }
                        break;
                    case SCROLL_STATE_TOUCH_SCROLL://滚动时
                        break;
                    case SCROLL_STATE_FLING:// 是当用户由于之前划动屏幕并抬起手指，屏幕产生惯性滑动时
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > lastVisibleItemPosition) {//下滑
                    vpIndustry.setVisibility(View.GONE);
                    tabLayout.setVisibility(View.GONE);
                    view2.setVisibility(View.GONE);
                    view3.setVisibility(View.GONE);
                    ll.setVisibility(View.VISIBLE);
                }
                lastVisibleItemPosition = firstVisibleItem;
            }
        });
    }

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent(EventExpert e) {
        experts = e.getExperts();
        Log.e("TAG", experts.size() + "");
        if (experts.size() > 0 && experts != null) {
            expertAdapter = new HomeExpertAdapter(experts, mContext);
            lvExpert.setAdapter(expertAdapter);
            lvExpert.setVisibility(View.VISIBLE);
            tvNo.setVisibility(View.GONE);
        } else {
            tvNo.setVisibility(View.VISIBLE);
            lvExpert.setVisibility(View.GONE);
        }
    }

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent1(EventIndustry e) {
        first = e.getFirst();
        second = e.getSencond();
        tabLayout.setCurrentTab(first);
        EventBus.getDefault().post(new EventIndustry1(first, second));
    }


    @OnClick({R.id.ll_search, R.id.ll_rank})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_search:
                startActivity(new Intent(mContext, SearchActivity.class));
                break;
            case R.id.ll_rank:
                Intent intent = new Intent(mContext, RankActivity.class);
                startActivity(intent);
                break;
        }
    }




    public void initMenu() {
        menu1.setDefaultMenuTitle(new String[]{"工作年限"});
        menu2.setDefaultMenuTitle(new String[]{"坐标城市"});
        menu3.setDefaultMenuTitle(new String[]{"职业"});

        menu1.setmMenuCount(1);
        menu2.setmMenuCount(1);
        menu3.setmMenuCount(1);

        menu1.setShowCheck(true);
        menu2.setShowCheck(true);
        menu3.setShowCheck(true);

        menu1.setmMenuTitleTextSize(14);//Menu的文字大小
        menu2.setmMenuTitleTextSize(14);//Menu的文字大小
        menu3.setmMenuTitleTextSize(14);//Menu的文字大小

        menu1.setmMenuBackColor(Color.WHITE);//Menu的背景颜色
        menu2.setmMenuBackColor(Color.WHITE);//Menu的背景颜色
        menu3.setmMenuBackColor(Color.WHITE);//Menu的背景颜色

        menu1.setmMenuPressedBackColor(UIUtils.getColor(R.color.color_system_gray1));
        menu2.setmMenuPressedBackColor(UIUtils.getColor(R.color.color_system_gray1));
        menu3.setmMenuPressedBackColor(UIUtils.getColor(R.color.color_system_gray1));

        menu1.setmMenuTitleTextColor(UIUtils.getColor(R.color.color_system_gray));//Menu的文字颜色
        menu2.setmMenuTitleTextColor(UIUtils.getColor(R.color.color_system_gray));//Menu的文字颜色
        menu3.setmMenuTitleTextColor(UIUtils.getColor(R.color.color_system_gray));//Menu的文字颜色

        menu1.setmMenuListTextSize(12);//Menu展开list的文字大小
        menu2.setmMenuListTextSize(12);//Menu展开list的文字大小
        menu3.setmMenuListTextSize(12);//Menu展开list的文字大小

        menu1.setmMenuListTextColor(UIUtils.getColor(R.color.color_system_blank));//Menu展开list的文字颜色
        menu2.setmMenuListTextColor(UIUtils.getColor(R.color.color_system_blank));//Menu展开list的文字颜色
        menu3.setmMenuListTextColor(UIUtils.getColor(R.color.color_system_blank));//Menu展开list的文字颜色

        menu1.setmMenuItems(list1);
        menu2.setmMenuItems(list2);
        menu3.setmMenuItems(list3);

        menu1.setmDownArrow(R.drawable.icon_xia);
        menu2.setmDownArrow(R.drawable.icon_xia);
        menu3.setmDownArrow(R.drawable.icon_xia);
        menu1.setmUpArrow(R.drawable.icon_shang);
        menu2.setmUpArrow(R.drawable.icon_shang);
        menu3.setmUpArrow(R.drawable.icon_shang);

        menu1.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {

            }
        });
        menu2.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {

            }
        });
        menu3.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {

            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (unbinder != null)
            unbinder.unbind();
        EventBus.getDefault().unregister(this);
    }


    @OnClick({R.id.menu1, R.id.menu2, R.id.menu3})
    public void onViewClicked1(View view) {
        switch (view.getId()) {
            case R.id.menu1:
                break;
            case R.id.menu2:
                break;
            case R.id.menu3:
                break;
        }
    }

}
