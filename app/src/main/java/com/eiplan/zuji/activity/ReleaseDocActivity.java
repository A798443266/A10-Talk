package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.utils.UIUtils;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.study.fileselectlibrary.LocalFileActivity;
import com.study.fileselectlibrary.bean.FileItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布文档
 */

public class ReleaseDocActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.menu)
    DropDownMenu menu;
    @BindView(R.id.tv_major)
    TextView tvMajor;
    @BindView(R.id.et_jifen)
    EditText etJifen;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.btn_submit)
    Button btnSubmit;
    @BindView(R.id.ll_docs)
    LinearLayout llDocs;
    @BindView(R.id.tv_name)
    TextView tvName;

    private String major;
    private String name;
    private String introduce;
    private String price;
    private String realPath;

    private String[] title = new String[]{"全部分类"};
    private String[] list1 = new String[]{"石油和天然气工业", "化学工业", "电工技术", "金属工业", "机械工业", "交通运输"
            , "建筑科学", "冶金工业", "水利工程", "能源工程", "自动工程", "环境工程"};
    private List<String[]> list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_doc);
        ButterKnife.bind(this);

        list.add(list1);
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

        llDocs.setVisibility(View.GONE);
    }

    @OnClick({R.id.iv_back, R.id.btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn:

                Intent intent = new Intent(this, LocalFileActivity.class);
                startActivityForResult(intent, 200);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 200) {
            if (requestCode == 200) {
                ArrayList<FileItem> resultFileList = data.getParcelableArrayListExtra("file");
                if (resultFileList == null && resultFileList.size() == 0) {
                    Toast.makeText(this, "还没有选择文件", Toast.LENGTH_SHORT).show();
                    return;
                }

                realPath = resultFileList.get(0).getPath();
                String docName = realPath.substring(realPath.lastIndexOf("/") + 1, realPath.length());
                llDocs.setVisibility(View.VISIBLE);
                tvName.setText(docName);
            }
        }
    }

    @OnClick(R.id.btn_submit)
    public void onViewClicked() {
        name = etName.getText().toString().trim();
        introduce = etDesc.getText().toString().trim();
        price = etJifen.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(introduce) || TextUtils.isEmpty(major)) {
            Toast.makeText(this, "请输入完整信息", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(realPath)) {
            Toast.makeText(this, "请先添加文件", Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent = new Intent(this, UpdateDocActivity.class);
        intent.putExtra("path", realPath);
        intent.putExtra("name", name);
        intent.putExtra("price", price);
        intent.putExtra("major", major);
        intent.putExtra("introduce", introduce);
        intent.putExtra("is", 1);

        startActivity(intent);
        finish();

    }
}

