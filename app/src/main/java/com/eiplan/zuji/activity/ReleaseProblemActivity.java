package com.eiplan.zuji.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.ReleasePhotoAdapter;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.eiplan.zuji.view.MyGridView;
import com.jayfang.dropdownmenu.DropDownMenu;
import com.jayfang.dropdownmenu.OnMenuSelectedListener;
import com.sdsmdg.tastytoast.TastyToast;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import net.steamcrafted.loadtoast.LoadToast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
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
 * 发布问题界面
 */

public class ReleaseProblemActivity extends Activity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.et_jifen)
    EditText etJifen;
    @BindView(R.id.gridview)
    MyGridView gridview;
    @BindView(R.id.menu)
    DropDownMenu menu;
    @BindView(R.id.tv_major)
    TextView tvMajor;

    private LinearLayout ll_popup;
    private ReleasePhotoAdapter adapter;
    private String name;
    private String desc;
    private String jifen;
    private String major;

    private LoadToast lt;

    public static List<Bitmap> bmp = new ArrayList<Bitmap>();//图片
    public static List<String> add = new ArrayList<String>();//图片地址
    private String[] title = new String[]{"全部分类"};
    private String[] list1 = new String[]{"石油和天然气工业", "化学工业", "电工技术", "金属工业", "机械工业", "交通运输"
            , "建筑科学", "冶金工业", "水利工程", "能源工程", "自动工程", "环境工程"};
    private List<String[]> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_problem);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        adapter = new ReleasePhotoAdapter(this, bmp);
        adapter.setSelectedPosition(0);
        gridview.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(ReleaseProblemActivity.this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (position == bmp.size()) {
                    String sdcardState = Environment.getExternalStorageState();
                    if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
                        if (bmp.size() < 3) {
                            new PopupWindows(ReleaseProblemActivity.this, gridview).init();
                        } else {
                            Toast.makeText(ReleaseProblemActivity.this, "一次发布3张图片", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "sdcard已拔出，不能选择照片", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Intent intent = new Intent(ReleaseProblemActivity.this, PhotoActivity.class);
                    intent.putExtra("ID", position);
                    startActivity(intent);
                }
            }
        });

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
    }

    @OnClick({R.id.iv_back, R.id.ll_release})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.ll_release:
                release();
                break;
        }
    }

    private void release() {
        name = etName.getText().toString().trim();
        desc = etDesc.getText().toString().trim();
        jifen = etJifen.getText().toString().trim();
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(desc)) {
            Toast.makeText(this, "请输入问题的具体信息", Toast.LENGTH_SHORT).show();
            return;
        }

        List<File> files = new ArrayList<>();

        lt = new LoadToast(this);
        lt.setText("正在发布，请稍后...");
        lt.show();
        lt.setTranslationY(100);
        lt.setBackgroundColor(R.color.color_system_gray1).setProgressColor(R.color.system_blue);

        if (add.size() > 0) { //有图片发布
            Log.e("TAG", add.get(0));
            for (int i = 0; i < add.size(); i++) {
                files.add(new File(add.get(i)));
            }

            Map<String, String> headers = new HashMap<>();
            headers.put("Content-Type", "mutipart/form-data");
            int num = (int) (Math.random() * 1000000 + 1);
            OkHttpUtils.post().url(Constant.updatePromble)
                    .headers(headers)
                    .addFile("file", num + ".png", files.get(0))
                    .addParams("phone", SpUtils.getString(ReleaseProblemActivity.this, Constant.current_phone))
                    .addParams("question", name)
                    .addParams("content", desc)
                    .addParams("point", jifen)
                    .addParams("major", major)
                    .build()
                    .execute(new Callback() {
                        @Override
                        public Object parseNetworkResponse(Response response, int id) throws Exception {
                            return null;
                        }

                        @Override
                        public void inProgress(float progress, long total, int id) {
                            Log.e("TAG", (int) progress * 100.0 + "");
                        }

                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.e("TAG", "失败");
                            lt.error();
                            lt.hide();
                        }

                        @Override
                        public void onResponse(Object response, int id) {
                            Log.e("TAG", "成功");
                            lt.success();
                            lt.hide();
                            TastyToast.makeText(getApplicationContext(), "发布成功!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            finish();
                        }
                    });


        } else {//没有图片发布
            OkHttpUtils.post().url(Constant.updatePrombleWithoutPic)
                    .addParams("phone", SpUtils.getString(ReleaseProblemActivity.this, Constant.current_phone))
                    .addParams("question", name)
                    .addParams("content", desc)
                    .addParams("point", jifen)
                    .addParams("major", major)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            Log.e("TAG", "失败");
                            lt.error();
                            lt.hide();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Log.e("TAG", "成功");
                            lt.success();
                            lt.hide();
                            TastyToast.makeText(getApplicationContext(), "发布成功!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                            finish();
                        }
                    });
        }

    }


    private class PopupWindows extends PopupWindow {
        private PopupWindows(Context mContext, View parent) {
            View view = View.inflate(mContext, R.layout.item_popupwindows, null);
            ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
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
                    photo();
                    dismiss();
                    close();
                }
            });

            //相册
            ll_alblum.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
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
            WindowManager.LayoutParams params = ReleaseProblemActivity.this.getWindow().getAttributes();
            params.alpha = 0.7f;
            ReleaseProblemActivity.this.getWindow().setAttributes(params);
        }

        public void close() {
            WindowManager.LayoutParams params = ReleaseProblemActivity.this.getWindow().getAttributes();
            params.alpha = 1;
            ReleaseProblemActivity.this.getWindow().setAttributes(params);
        }
    }


    private static final int TAKE_PICTURE = 0;
    private static final int RESULT_LOAD_IMAGE = 1;
    private String path = "";
    private Uri mOutPutFileUri;
    String picturePath;

    //拍照
    private void photo() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        mOutPutFileUri = Uri.fromFile(file);
        picturePath = file.getAbsolutePath();
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mOutPutFileUri);
        startActivityForResult(intent, TAKE_PICTURE);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PICTURE:
                if (resultCode == RESULT_OK) {
                    startPhotoZoom(picturePath);
                }
                break;
            case RESULT_LOAD_IMAGE:
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    String[] files = {MediaStore.Images.Media.DATA};
                    Cursor c = this.getContentResolver().query(uri, files, null, null, null);
                    c.moveToFirst();
                    int ii = c.getColumnIndex(files[0]);
                    path = c.getString(ii);
                    c.close();
                    startPhotoZoom(path);
                }
                break;
        }
    }

    private Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis); // /把流转化为Bitmap图片
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    private Bitmap createFramedPhoto(int x, int y, Bitmap image, float outerRadiusRat) {
        Drawable imageDrawable = new BitmapDrawable(image);
        Bitmap output = Bitmap.createBitmap(x, y, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        RectF outerRect = new RectF(0, 0, x, y);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.RED);
        canvas.drawRoundRect(outerRect, outerRadiusRat, outerRadiusRat, paint);
        //		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        imageDrawable.setBounds(0, 0, x, y);
        canvas.saveLayer(outerRect, paint, Canvas.ALL_SAVE_FLAG);
        imageDrawable.draw(canvas);
        canvas.restore();
        return output;
    }

    private void startPhotoZoom(String uri) {
        add.add(uri);
        Bitmap bitmap = getLoacalBitmap(add.get(add.size() - 1));
        bmp.add(bitmap);
        init();
    }

    @Override
    protected void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        bmp.clear();
        add.clear();
        super.onDestroy();
    }
}
