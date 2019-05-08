package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;

import java.io.File;
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
 * 发布视频界面
 */

public class ReleaseVideoActivity extends AppCompatActivity {

    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.menu)
    DropDownMenu menu;
    @BindView(R.id.tv_major)
    TextView tvMajor;

    private String[] title = new String[]{"全部分类"};
    private String[] list1 = new String[]{"石油和天然气工业", "化学工业", "电工技术", "金属工业", "机械工业", "交通运输"
            , "建筑科学", "冶金工业", "水利工程", "能源工程", "自动工程", "环境工程"};
    private List<String[]> list = new ArrayList<>();

    private String major;//选择的分类
    private String title1;//标题
    private String introduce;//描述
    private String path; //视频路径
    private String thumPath;//视频缩略图地址

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_video);
        ButterKnife.bind(this);
        initView();

        list.add(list1);
        path = getIntent().getStringExtra("path");
        thumPath = getIntent().getStringExtra("thumPath");

        Glide.with(this).load(thumPath).error(R.drawable.vedio_bg).into(ivCover);
    }

    private void initView() {
        menu.setDefaultMenuTitle(title);
        menu.setmMenuCount(1);
        menu.setShowCheck(true);
        menu.setmMenuTitleTextSize(14);//Menu的文字大小
        menu.setmMenuTitleTextColor(UIUtils.getColor(R.color.color_system_blank));//Menu的文字颜色
        menu.setmMenuListTextSize(12);//Menu展开list的文字大小
        menu.setmMenuListTextColor(UIUtils.getColor(R.color.color_system_blank));//Menu展开list的文字颜色
        menu.setmMenuBackColor(UIUtils.getColor(R.color.color_system_gray1));//Menu的背景颜色
//        menu.setmMenuPressedBackColor(Color.WHITE);//Menu按下的背景颜色
        menu.setmMenuItems(list);

        menu.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {
                tvMajor.setText("已选择：" + list.get(ColumnIndex)[RowIndex]);
                major = list.get(ColumnIndex)[RowIndex];
            }
        });
    }

    @OnClick({R.id.tv_cancel, R.id.btn_submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:
                finish();
                break;
            case R.id.btn_submit:
                release();
                break;
        }
    }

    //发布
    private void release() {
        if (TextUtils.isEmpty(path)) {
            Toast.makeText(this, "视频出错啦！", Toast.LENGTH_SHORT).show();
            return;
        }
        title1 = etTitle.getText().toString().trim();
        introduce = etDesc.getText().toString().trim();
        if (TextUtils.isEmpty(major) || TextUtils.isEmpty(title1) || TextUtils.isEmpty(introduce)) {
            Toast.makeText(this, "请填写视频详细信息！", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(this, UpdateVideoActivity.class);
        intent.putExtra("path", path);
        intent.putExtra("title", title1);
        intent.putExtra("major", major);
        intent.putExtra("introduce", introduce);
        intent.putExtra("thumPath", thumPath);

        intent.putExtra("is", 1);

        startActivity(intent);
        finish();

    }
}
