package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.menu.FirstAdapter;
import com.eiplan.zuji.menu.FirstItem;
import com.eiplan.zuji.menu.SecondAdapter;
import com.eiplan.zuji.menu.SecondItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 全部分类
 */
public class AllKindsActivity extends AppCompatActivity {

    @BindView(R.id.lv_left)
    ListView lvLeft;
    @BindView(R.id.lv_right)
    ListView lvRight;
    private List<FirstItem> firstItems = new ArrayList<>();
    private List<SecondItem> secondItems = new ArrayList<>();
    private FirstAdapter adapter1;
    private SecondAdapter adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_kinds);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    private void initView() {
        adapter1 = new FirstAdapter(this, firstItems);
        lvLeft.setAdapter(adapter1);

        lvLeft.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<SecondItem> list2 = firstItems.get(position).getSecondItems();
                if (list2 == null || list2.size() == 0) {//如果没有二级
                    return;
                }

                adapter1 = (FirstAdapter) parent.getAdapter();
                //如果上次点击的就是这一个item，则不进行任何操作
                if(adapter1.getSelectedPosition() == position){
                    return;
                }
                adapter1.setSelectedPosition(position);
                adapter1.notifyDataSetChanged();
                adapter2 = new SecondAdapter(AllKindsActivity.this,list2);
                lvRight.setAdapter(adapter2);
                //显示右侧二级分类
            }
        });

        lvRight.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter2.setSelectedPosition(position);
                adapter2.notifyDataSetChanged();
            }
        });
    }


    private void initData() {
        firstItems.add(new FirstItem(0, "全部", new ArrayList<SecondItem>()));
        //1
        ArrayList<SecondItem> secondList1 = new ArrayList<SecondItem>();
        secondList1.add(new SecondItem(101, "全部"));
        secondList1.add(new SecondItem(101, "锻压技术"));
        secondList1.add(new SecondItem(102, "电加工技术"));
        secondList1.add(new SecondItem(103, "热技工"));
        secondList1.add(new SecondItem(104, "铸造工程"));
        secondList1.add(new SecondItem(105, "焊接技术"));
        secondList1.add(new SecondItem(106, "表面处理与涂料"));
        secondList1.add(new SecondItem(107, "机床技术"));
        secondList1.add(new SecondItem(108, "铝加工技术"));
        secondList1.add(new SecondItem(109, "精密制造"));
        secondList1.add(new SecondItem(110, "自动化加工"));
        firstItems.add(new FirstItem(1, "金属工艺", secondList1));

        //2
        ArrayList<SecondItem> secondList2 = new ArrayList<SecondItem>();
        secondList2.add(new SecondItem(201, "全部"));
        secondList2.add(new SecondItem(201, "钢铁工艺"));
        secondList2.add(new SecondItem(202, "化工冶金"));
        secondList2.add(new SecondItem(203, "黄金技术"));
        secondList2.add(new SecondItem(204, "热喷涂技术"));
        secondList2.add(new SecondItem(205, "冶金自动化技术"));
        secondList2.add(new SecondItem(206, "炼铁技术"));
        secondList2.add(new SecondItem(207, "现代冶金技术"));
        secondList2.add(new SecondItem(208, "铸铁的熔炼设备和技术"));
        firstItems.add(new FirstItem(2, "冶金工艺", secondList2));

        //3
        ArrayList<SecondItem> secondList3 = new ArrayList<SecondItem>();
        secondList3.add(new SecondItem(301, "全部"));
        secondList3.add(new SecondItem(302, "电子仪表技术"));
        secondList3.add(new SecondItem(303, "电子机械工程"));
        secondList3.add(new SecondItem(304, "风机技术"));
        secondList3.add(new SecondItem(305, "广义技术"));
        secondList3.add(new SecondItem(306, "液压工业"));
        secondList3.add(new SecondItem(307, "机械工业自动化"));
        secondList3.add(new SecondItem(308, "机电一体化技术"));
        secondList3.add(new SecondItem(309, "水泵技术"));
        secondList3.add(new SecondItem(310, "紧固件技术"));
        secondList3.add(new SecondItem(311, "轴承技术"));
        firstItems.add(new FirstItem(3, "机械工业", secondList3));

        //4
        ArrayList<SecondItem> secondList4 = new ArrayList<SecondItem>();
        secondList4.add(new SecondItem(401, "全部"));
        secondList4.add(new SecondItem(401, "表面活性剂工业"));
        secondList4.add(new SecondItem(402, "化工生产与技术"));
        secondList4.add(new SecondItem(403, "玻璃技术"));
        secondList4.add(new SecondItem(404, "化工装备技术"));
        secondList4.add(new SecondItem(405, "无机盐技术"));
        secondList4.add(new SecondItem(406, "水处理技术"));
        secondList4.add(new SecondItem(407, "纯碱工业"));
        secondList4.add(new SecondItem(408, "染料技术"));
        secondList4.add(new SecondItem(409, "气体净化"));
        secondList4.add(new SecondItem(410, "碳材料科学与工艺"));
        secondList4.add(new SecondItem(411, "塑料工业"));
        firstItems.add(new FirstItem(4, "化学工业", secondList4));
        //5
        ArrayList<SecondItem> secondList5 = new ArrayList<SecondItem>();
        secondList5.add(new SecondItem(501, "全部"));
        secondList5.add(new SecondItem(501, "城市规划"));
        secondList5.add(new SecondItem(502, "地下空间技术"));
        secondList5.add(new SecondItem(503, "城市害虫防治技术"));
        secondList5.add(new SecondItem(504, "城市照明技术"));
        secondList5.add(new SecondItem(505, "景观设计"));
        secondList5.add(new SecondItem(506, "低温建筑技术"));
        secondList5.add(new SecondItem(507, "电梯工业"));
        secondList5.add(new SecondItem(508, "给水技术"));
        secondList5.add(new SecondItem(509, "地基处理技术"));
        secondList5.add(new SecondItem(510, "城市方正减灾技术"));
        firstItems.add(new FirstItem(5, "建筑科学", secondList5));
        //6
        ArrayList<SecondItem> secondList6 = new ArrayList<SecondItem>();
        secondList6.add(new SecondItem(601, "全部"));
        secondList6.add(new SecondItem(601, "城市规划"));
        secondList6.add(new SecondItem(602, "地下空间技术"));
        secondList6.add(new SecondItem(603, "城市害虫防治技术"));
        secondList6.add(new SecondItem(604, "城市照明技术"));
        secondList6.add(new SecondItem(605, "景观设计"));
        secondList6.add(new SecondItem(606, "低温建筑技术"));
        secondList6.add(new SecondItem(607, "电梯工业"));
        secondList6.add(new SecondItem(608, "给水技术"));
        secondList6.add(new SecondItem(609, "地基处理技术"));
        secondList6.add(new SecondItem(610, "城市方正减灾技术"));
        firstItems.add(new FirstItem(6, "能源与动力工程", secondList6));

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
