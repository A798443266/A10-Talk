package com.eiplan.zuji.activity;
/**
 * 交易记录
 */

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.TradeAdapter;
import com.eiplan.zuji.bean.TradeInfo;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class TradeRecordActivity extends AppCompatActivity {

    @BindView(R.id.menu1)
    DropDownMenu menu1;
    @BindView(R.id.menu2)
    DropDownMenu menu2;
    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.pb_load)
    ProgressBar pbLoad;

    private String[] types = new String[]{"购买文档", "购买课程", "专家付费"};
    private String[] times = new String[]{"7天内", "15天内", "一个月内", "一个月前"};
    private List<String[]> list1 = new ArrayList<>();
    private List<String[]> list2 = new ArrayList<>();
    private TradeAdapter adapter;
    private List<TradeInfo> trades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_record);
        ButterKnife.bind(this);
        list1.add(types);
        list2.add(times);
        initMenu();
        initData();
    }

    private void initData() {
        OkHttpUtils.post().url(Constant.getTradeByPhone)
                .addParams("phone", SpUtils.getString(TradeRecordActivity.this, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        trades = JsonUtils.parseTrases(response);
                        show();
                    }
                });
    }

    private void show() {
        pbLoad.setVisibility(View.GONE);
        if (trades != null && trades.size() > 0) {
            adapter = new TradeAdapter(this, trades);
            lv.setAdapter(adapter);
        } else {
            tvNo.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.iv_back, R.id.ic_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ic_delete:
                break;
        }
    }


    public void initMenu() {
        menu1.setDefaultMenuTitle(new String[]{"全部类型"});
        menu2.setDefaultMenuTitle(new String[]{"时间"});

        menu1.setmMenuCount(1);
        menu2.setmMenuCount(1);

        menu1.setShowCheck(true);
        menu2.setShowCheck(true);

        menu1.setmMenuTitleTextSize(14);//Menu的文字大小
        menu2.setmMenuTitleTextSize(14);//Menu的文字大小

        menu1.setmMenuBackColor(Color.WHITE);//Menu的背景颜色
        menu2.setmMenuBackColor(Color.WHITE);//Menu的背景颜色

        menu1.setmMenuPressedBackColor(UIUtils.getColor(R.color.color_white));
        menu2.setmMenuPressedBackColor(UIUtils.getColor(R.color.color_white));

        menu1.setmMenuTitleTextColor(UIUtils.getColor(R.color.color_system_blank));//Menu的文字颜色
        menu2.setmMenuTitleTextColor(UIUtils.getColor(R.color.color_system_blank));//Menu的文字颜色

        menu1.setmMenuListTextSize(12);//Menu展开list的文字大小
        menu2.setmMenuListTextSize(12);//Menu展开list的文字大小

        menu1.setmMenuListTextColor(UIUtils.getColor(R.color.color_system_blank));//Menu展开list的文字颜色
        menu2.setmMenuListTextColor(UIUtils.getColor(R.color.color_system_blank));//Menu展开list的文字颜色

        menu1.setmMenuItems(list1);
        menu2.setmMenuItems(list2);
//        menu1.setmDownArrow(R.drawable.icon_xiangxia);

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
    }
}
