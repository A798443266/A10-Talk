package com.eiplan.zuji.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eiplan.zuji.R;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.xyz.step.FlowViewHorizontal;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

/**
 * 认证专家界面
 */

public class BecomeExpertActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.titlebar_bg)
    RelativeLayout titlebarBg;
    @BindView(R.id.flowview)
    FlowViewHorizontal flowview;
    @BindView(R.id.ll_step1)
    LinearLayout llStep1;
    @BindView(R.id.ll_step2)
    LinearLayout llStep2;
    @BindView(R.id.btn)
    Button btn;
    @BindView(R.id.ll_step3)
    LinearLayout llStep3;
    @BindView(R.id.ll_step4)
    RelativeLayout llStep4;
    @BindView(R.id.rl)
    RelativeLayout rl;
    @BindView(R.id.tv_major)
    TextView tvMajor;
    @BindView(R.id.menu)
    DropDownMenu menu;
    @BindView(R.id.iv_add1)
    ImageView ivAdd1;
    @BindView(R.id.delete1)
    ImageView delete1;
    @BindView(R.id.iv_add2)
    ImageView ivAdd2;
    @BindView(R.id.delete2)
    ImageView delete2;
    @BindView(R.id.iv_add3)
    ImageView ivAdd3;
    @BindView(R.id.delete3)
    ImageView delete3;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_work_year)
    EditText etWorkYear;
    @BindView(R.id.et_compnay)
    EditText etCompnay;
    @BindView(R.id.et_city)
    EditText etCity;
    @BindView(R.id.et_job)
    EditText etJob;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.view)
    View view;

    private String[] title1 = new String[]{"全部分类"};
    private String[] list1 = new String[]{"石油和天然气工业", "化学工业", "电工技术", "金属工业", "机械工业", "交通运输"
            , "建筑科学", "冶金工业", "水利工程", "能源工程", "自动工程", "环境工程"};
    private List<String[]> list = new ArrayList<>();
    private String title[] = {"基本资料", "擅长方面", "上传证明", "申请完成"};
    private String step[] = {"1", "2", "3", "4"};
    private int currentStep = 1;//当前步骤
    private String major;//专业
    private String path1;//图片地址1
    private String path2;//图片地址2
    private String path3;//图片地址3
    private String name;//名字
    private String workyear;//工作年限
    private String compnay;//公司
    private String city;//城市
    private String job;//职位
    private List<Integer> judge = new ArrayList<>();//用来判断是否已经有照片
    private int SHOW = 1;

    private SweetAlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_become_expert);
        ButterKnife.bind(this);

        tvRight.setVisibility(View.GONE);
        tvTitle.setText("基本资料");
        tvTitle.setTextColor(UIUtils.getColor(R.color.color_system_blank));
        ivBack.setImageResource(R.drawable.icon_back);
        titlebarBg.setBackgroundColor(UIUtils.getColor(R.color.color_white));

        for (int i = 0; i < 3; i++) {
            judge.add(0);
        }

        initView();
    }

    private void initView() {
        flowview.setProgress(1, 4, title, step);

        list.add(list1);
        menu.setDefaultMenuTitle(title1);
        menu.setmMenuCount(1);
        menu.setShowCheck(true);
        menu.setmMenuTitleTextSize(15);//Menu的文字大小
        menu.setmMenuTitleTextColor(UIUtils.getColor(R.color.color_white));//Menu的文字颜色
        menu.setmMenuListTextSize(12);//Menu展开list的文字大小
        menu.setmMenuListTextColor(UIUtils.getColor(R.color.color_system_blank));//Menu展开list的文字颜色
        menu.setmMenuBackColor(UIUtils.getColor(android.R.color.transparent));//Menu的背景颜色
//        menu.setBackgroundResource(UIUtils.getColor(android.R.color.transparent));//Menu按下的背景颜色
        menu.setmMenuItems(list);

        menu.setMenuSelectedListener(new OnMenuSelectedListener() {
            @Override
            //Menu展开的list点击事件  RowIndex：list的索引  ColumnIndex：menu的索引
            public void onSelected(View listview, int RowIndex, int ColumnIndex) {
                tvMajor.setText("已选择：" + list.get(ColumnIndex)[RowIndex]);
                major = list.get(ColumnIndex)[RowIndex];
            }
        });

        btn1.setVisibility(View.GONE);
        view.setVisibility(View.GONE);

    }

    @OnClick(R.id.btn1)
    public void onViewClicked1() {//上一步
        if (currentStep == 2) {
            llStep1.setVisibility(View.VISIBLE);
            llStep2.setVisibility(View.GONE);
            llStep3.setVisibility(View.GONE);
            llStep4.setVisibility(View.GONE);
            tvTitle.setText("基本资料");
            flowview.setProgress(1, 4, title, step);
            btn.setText("下一步");
            currentStep = 1;
        } else if (currentStep == 3) {
            llStep1.setVisibility(View.GONE);
            llStep2.setVisibility(View.VISIBLE);
            llStep3.setVisibility(View.GONE);
            llStep4.setVisibility(View.GONE);
            tvTitle.setText("擅长方向");
            btn.setText("下一步");
            flowview.setProgress(2, 4, title, step);
            currentStep = 2;
        }
    }

    @OnClick(R.id.btn)
    public void onViewClicked() {//下一步
        if (currentStep == 1) {
            llStep1.setVisibility(View.GONE);
            llStep2.setVisibility(View.VISIBLE);
            llStep3.setVisibility(View.GONE);
            llStep4.setVisibility(View.GONE);
            tvTitle.setText("擅长方向");
            flowview.setProgress(2, 4, title, step);
            currentStep = 2;
            btn1.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
        } else if (currentStep == 2) {
            llStep1.setVisibility(View.GONE);
            llStep2.setVisibility(View.GONE);
            llStep3.setVisibility(View.VISIBLE);
            llStep4.setVisibility(View.GONE);
            tvTitle.setText("上传证明");
            flowview.setProgress(3, 4, title, step);
            btn.setText("提交申请");
            currentStep = 3;
        } else if (currentStep == 3) {
            submit();
//            llStep1.setVisibility(View.GONE);
//            llStep2.setVisibility(View.GONE);
//            llStep3.setVisibility(View.GONE);
//            llStep4.setVisibility(View.VISIBLE);
//            tvTitle.setText("申请完成");
//            rl.setBackgroundColor(UIUtils.getColor(R.color.color_white));
//            flowview.setProgress(4, 4, title, step);
//            btn.setText("完成");
//            view.setVisibility(View.GONE);
//            btn1.setVisibility(View.GONE);
        } else if (currentStep == 4) {
            finish();
        }
    }


    private void submit() {

        name = etName.getText().toString().trim();
        workyear = etWorkYear.getText().toString().trim();
        compnay = etCompnay.getText().toString().trim();
        city = etCity.getText().toString().trim();
        job = etJob.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(workyear) || TextUtils.isEmpty(compnay)
                || TextUtils.isEmpty(city) || TextUtils.isEmpty(job) || TextUtils.isEmpty(major)
                || TextUtils.isEmpty(path1) || TextUtils.isEmpty(path2) || TextUtils.isEmpty(path3)) {
            Toast.makeText(this, "您输入的信息不完整", Toast.LENGTH_SHORT).show();
            return;
        }

        dialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        dialog.getProgressHelper().setBarColor(Color.parseColor("#2694e9"));
        dialog.setTitleText("正在提交请稍后...");
//        pDialog.setCancelable(false);
        dialog.show();

        File file1 = new File(path1);
        File file2 = new File(path2);
        File file3 = new File(path3);
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "mutipart/form-data");
        OkHttpUtils.post().url(Constant.becomeExpert)
                .addFile("idcard", (int) (Math.random() * 1000000 + 1) + ".png", file1)
                .addFile("idcard2", (int) (Math.random() * 1000000 + 1) + ".png", file2)
                .addFile("approve", (int) (Math.random() * 1000000 + 1) + ".png", file3)
                .addParams("phone", SpUtils.getString(this, Constant.current_phone))
                .addParams("job", job)
                .addParams("address", city)
                .addParams("workyear", workyear)
                .addParams("major", major)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        dialog.dismiss();
                        Toast.makeText(BecomeExpertActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = JSON.parseObject(response);
                        if (jsonObject.getInteger("code") == 100) {
                            currentStep = 4;
                            dialog.dismiss();
                            llStep1.setVisibility(View.GONE);
                            llStep2.setVisibility(View.GONE);
                            llStep3.setVisibility(View.GONE);
                            llStep4.setVisibility(View.VISIBLE);
                            tvTitle.setText("申请完成");
                            rl.setBackgroundColor(UIUtils.getColor(R.color.color_white));
                            flowview.setProgress(4, 4, title, step);
                            btn.setText("完成");
                            view.setVisibility(View.GONE);
                            btn1.setVisibility(View.GONE);
                        } else {
                            dialog.dismiss();
                            Toast.makeText(BecomeExpertActivity.this, "提交失败", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @OnClick({R.id.iv_add1, R.id.delete1, R.id.iv_add2, R.id.delete2, R.id.iv_add3, R.id.delete3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add1:
                if (judge.get(0) == 0) {//如果还没有设置图片
                    SHOW = 1;
                    new PopupWindows(BecomeExpertActivity.this, btn).init();
                } else {//如果已经设置了图片
                    Intent intent = new Intent(this, BigPhotoActivity.class);
                    intent.putExtra("path", path1);
                    startActivity(intent);
                }
                break;

            case R.id.delete1:
                ivAdd1.setImageResource(R.drawable.icon_addpic_unfocused);
                path1 = "";
                judge.set(0, 0);//还原标记
                delete1.setVisibility(View.GONE);
                break;

            case R.id.iv_add2:
                if (judge.get(1) == 0) {
                    SHOW = 2;
                    new PopupWindows(BecomeExpertActivity.this, btn).init();
                } else {
                    Intent intent = new Intent(this, BigPhotoActivity.class);
                    intent.putExtra("path", path2);
                    startActivity(intent);
                }
                break;

            case R.id.delete2:
                ivAdd2.setImageResource(R.drawable.icon_addpic_unfocused);
                path2 = "";
                judge.set(1, 0);//还原标记
                delete2.setVisibility(View.GONE);
                break;

            case R.id.iv_add3:
                if (judge.get(2) == 0) {
                    SHOW = 3;
                    new PopupWindows(BecomeExpertActivity.this, btn).init();
                } else {
                    Intent intent = new Intent(this, BigPhotoActivity.class);
                    intent.putExtra("path", path3);
                    startActivity(intent);
                }
                break;

            case R.id.delete3:
                ivAdd3.setImageResource(R.drawable.icon_addpic_unfocused);
                path3 = "";
                judge.set(2, 0);//还原标记
                delete3.setVisibility(View.GONE);
                break;
        }
    }

    @OnClick(R.id.iv_back)
    public void onViewClicke() {
        finish();
    }


    private class PopupWindows extends PopupWindow {
        private PopupWindows(Context mContext, View parent) {
            View view = View.inflate(mContext, R.layout.item_popupwindows, null);
            LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
            setFocusable(false);
            setOutsideTouchable(true);
            setContentView(view);

            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();
            LinearLayout ll_alblum = view.findViewById(R.id.ll_alblum);
            LinearLayout ll_takephoto = view.findViewById(R.id.ll_takephoto);
            LinearLayout ll_cancel = view.findViewById(R.id.ll_cancel);

            //拍照
            ll_takephoto.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
//                    photo();
                    dismiss();
                    close();
                }
            });

            //相册
            ll_alblum.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, 1);
                    dismiss();
                    close();
                }
            });

            ll_cancel.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    dismiss();
                    close();
                }
            });
        }

        public void init() {
            WindowManager.LayoutParams params = BecomeExpertActivity.this.getWindow().getAttributes();
            params.alpha = 0.7f;
            BecomeExpertActivity.this.getWindow().setAttributes(params);
        }

        public void close() {
            WindowManager.LayoutParams params = BecomeExpertActivity.this.getWindow().getAttributes();
            params.alpha = 1;
            BecomeExpertActivity.this.getWindow().setAttributes(params);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 0: //拍照
                if (resultCode == RESULT_OK) {
//                    startPhotoZoom(picturePath);
                }
                break;
            case 1:   //相册
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String[] files = {MediaStore.Images.Media.DATA};
                    Cursor c = this.getContentResolver().query(uri, files, null, null, null);
                    c.moveToFirst();
                    int ii = c.getColumnIndex(files[0]);
                    String realPath = c.getString(ii);
                    c.close();
                    if (SHOW == 1) {
                        path1 = realPath;
                        startPhotoZoom(path1, ivAdd1, 1);
                    } else if (SHOW == 2) {
                        path2 = realPath;
                        startPhotoZoom(path2, ivAdd2, 2);
                    } else {
                        path3 = realPath;
                        startPhotoZoom(path3, ivAdd3, 3);
                    }


                }
                break;
        }
    }


    //把地址转化为bitmap
    private void startPhotoZoom(String path, ImageView imageView, int SHOW) {
        Bitmap bitmap;
        try {
            FileInputStream fis = new FileInputStream(path);
            bitmap = BitmapFactory.decodeStream(fis);
            imageView.setImageBitmap(bitmap);
            judge.set(SHOW - 1, 1);//设置图片已经显示标记
            switch (SHOW) {
                case 1:
                    delete1.setVisibility(View.VISIBLE);
                    break;
                case 2:
                    delete2.setVisibility(View.VISIBLE);
                    break;
                case 3:
                    delete3.setVisibility(View.VISIBLE);
                    break;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

}
