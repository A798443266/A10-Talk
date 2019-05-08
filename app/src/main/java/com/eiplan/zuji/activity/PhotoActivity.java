package com.eiplan.zuji.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.eiplan.zuji.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预览发布问题图片的界面
 */

public class PhotoActivity extends AppCompatActivity {

    @BindView(R.id.viewpager)
    ViewPager viewpager;

    public List<Bitmap> bitmap = new ArrayList<Bitmap>();
    private ArrayList<View> listViews;
    private int index = 0;
    private ReleaseProblemActivity activity;
    private MyPageAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        ButterKnife.bind(this);

        //隐藏顶部的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                // 页面选择响应函数
                index = i;
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });


        for (int i = 0; i < activity.bmp.size(); i++) {
            initListViews(activity.bmp.get(i));
        }

        adapter = new MyPageAdapter(listViews);// 构造adapter
        viewpager.setAdapter(adapter); //设置适配器
        Intent intent = getIntent();
        int id = intent.getIntExtra("ID", 0);
        viewpager.setCurrentItem(id);
    }

    private void initListViews(Bitmap bitmap) {
        if (listViews == null)
            listViews = new ArrayList<View>();
        final ImageView img = new ImageView(this);// 构造textView对象
        img.setImageBitmap(bitmap);
        img.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        // 添加view
        listViews.add(img);
    }

    @OnClick({R.id.rl_back, R.id.rl_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_delete:
                if (listViews.size() > 0) {
                    listViews.remove(index);
                    ReleaseProblemActivity.bmp.remove(index);
                    adapter.notifyDataSetChanged();
                }
                break;
        }
    }



    class MyPageAdapter extends PagerAdapter {

        private ArrayList<View> listViews;// content
        private int size;// 页数
        public MyPageAdapter(ArrayList<View> listViews) {// 构造函数
            // 初始化viewpager的时候给的一个页面
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }

        public void setListViews(ArrayList<View> listViews) {// 自己写的一个方法用来添加数据
            this.listViews = listViews;
            size = listViews == null ? 0 : listViews.size();
        }


        public int getCount() {// 返回数量
            if (listViews != null && listViews.size() > 0) {
                return listViews.size();
            } else {
                return 0;
            }
        }

        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }


        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
            if (listViews.size() == 0) {
                finish();
            }
        }

        public void finishUpdate(View arg0) {

        }

        public Object instantiateItem(ViewGroup container, int position) {
            container.addView(listViews.get(position));
            return listViews.get(position);
        }


        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }
    }
}
