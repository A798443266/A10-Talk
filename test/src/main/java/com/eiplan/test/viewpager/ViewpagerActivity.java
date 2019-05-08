package com.eiplan.test.viewpager;

import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eiplan.test.R;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerActivity extends AppCompatActivity {

    private ViewPager viewpager;
    private List<View> views;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        viewpager = findViewById(R.id.viewpager);

        LayoutInflater lf = getLayoutInflater();
        views = new ArrayList<>();
        views.add(lf.inflate(R.layout.activity_layout1, null));
        views.add(lf.inflate(R.layout.activity_layout2, null));
        views.add(lf.inflate(R.layout.activity_layout3, null));

        tv1 = views.get(0).findViewById(R.id.tv1);
        tv2 = views.get(1).findViewById(R.id.tv2);
        tv3 = views.get(2).findViewById(R.id.tv3);

        tv1.setText("这里是第一个");
        tv2.setText("这里是第二个");
        tv3.setText("这里是第二个");

        PagerAdapter adapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return views.size();
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
                return view == o;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView(views.get(position));
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                container.addView(views.get(position));
                return  views.get(position);
            }
        };
        viewpager.setAdapter(adapter);
    }
}
