package com.eiplan.zuji.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.LiveRoom;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.Callback;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 开启直播前设置参数页面
 */
public class RoomSettingActivity extends AppCompatActivity {

    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.et_desc)
    EditText etDesc;
    @BindView(R.id.btn_start)
    Button btnStart;
    @BindView(R.id.iv_add)
    ImageView ivAdd;
    @BindView(R.id.iv_cover)
    ImageView ivCover;
    @BindView(R.id.rl)
    RelativeLayout rl;

    private LiveRoom room;
    private LinearLayout ll_popup;
    private String path; //封面的路径
    private Bitmap bitmap; //封面的Bitmap对象

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_setting);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_start)
    public void onViewClicked() {
        String title = etTitle.getText().toString();
        String content = etDesc.getText().toString();
        String phone = SpUtils.getString(this, Constant.current_phone);
        if (TextUtils.isEmpty(title) || TextUtils.isEmpty(content)) {
            Toast.makeText(this, "请输入直播内容", Toast.LENGTH_SHORT).show();
            return;
        }

        File file = new File(path);//图片
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "mutipart/form-data");
        int num = (int) (Math.random() * 1000000 + 1);
        OkHttpUtils.post().url(Constant.startLive)
                .headers(headers)
                .addFile("file", num + ".png", file)
                .addParams("phone", phone)
                .addParams("title", title)
                .addParams("content", content)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(RoomSettingActivity.this, "网络出错了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Log.e("TAG", response);
                        room = JsonUtils.parseLive(response);
                        Intent intent = new Intent(RoomSettingActivity.this, LiveStreamActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("live", room);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                });
                /*.execute(new Callback() {
                    @Override
                    public Object parseNetworkResponse(Response response, int id) throws Exception {
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Toast.makeText(RoomSettingActivity.this, "网络出错了", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Object response, int id) {
                        Log.e("TAG", (String) response);
                        room = JsonUtils.parseLive((String) response);
                        Intent intent = new Intent(RoomSettingActivity.this, LiveStreamActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("live", room);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });*/
    }

    @OnClick({R.id.iv_add, R.id.iv_cover, R.id.iv_cha})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                new PopupWindows(RoomSettingActivity.this, btnStart).init();
                break;
            case R.id.iv_cover:
                if (bitmap != null) {
                    Intent intent = new Intent(this, BigPhotoActivity.class);
                    intent.putExtra("path", path);
                    startActivity(intent);
                }
                break;

            case R.id.iv_cha:

                break;
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
            WindowManager.LayoutParams params = RoomSettingActivity.this.getWindow().getAttributes();
            params.alpha = 0.7f;
            RoomSettingActivity.this.getWindow().setAttributes(params);
        }

        public void close() {
            WindowManager.LayoutParams params = RoomSettingActivity.this.getWindow().getAttributes();
            params.alpha = 1;
            RoomSettingActivity.this.getWindow().setAttributes(params);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
//            case TAKE_PICTURE:
//                if (resultCode == RESULT_OK) {
//                    startPhotoZoom(picturePath);
//                }
//                break;
            case 1:
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

    private void startPhotoZoom(String path) {
        bitmap = getLoacalBitmap(path);
        if (bitmap != null) {
            ivAdd.setVisibility(View.GONE);
            rl.setVisibility(View.VISIBLE);
            ivCover.setImageBitmap(bitmap);
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
}
