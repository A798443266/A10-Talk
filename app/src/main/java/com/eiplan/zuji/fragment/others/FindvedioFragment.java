package com.eiplan.zuji.fragment.others;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.AllKindsActivity;
import com.eiplan.zuji.activity.ReleaseVideoActivity;
import com.eiplan.zuji.activity.VedioDetailsActivity;
import com.eiplan.zuji.adapter.VedioAdapter;
import com.eiplan.zuji.bean.VideoInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.study.fileselectlibrary.LocalFileActivity;
import com.study.fileselectlibrary.bean.FileItem;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

import static android.view.TouchDelegate.BELOW;
import static android.view.TouchDelegate.TO_LEFT;

/**
 * 技术视频
 */

public class FindvedioFragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_no)
    TextView tvNo;
    @BindView(R.id.ll_start)
    LinearLayout llStart;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    Unbinder unbinder;

    private List<VideoInfo> videos;
    private VedioAdapter adapter;
    private String data; //用于没网时缓存数据

    private boolean refresh = false; //判断是否有下拉刷新

    //视频缩略图地址
    private String thumPath;

    @Override
    protected void initView() {

        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                initData();//重新联网请求
                refreshlayout.finishRefresh();
                refresh = true;
            }
        });

        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                VideoInfo video = videos.get(position);
                Intent intent = new Intent(mContext, VedioDetailsActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("video", video);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragmemt_find_vedio;
    }

    @Override
    public void initData() {

        data = SpUtils.getString(mContext, Constant.Videos);
        if (!TextUtils.isEmpty(data)) {
            videos = JsonUtils.parseVideos(data);
        }
        //从服务器获取数据
        OkHttpUtils.get().url(Constant.getVideos).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(String response, int id) {
                //存入最新数据
                SpUtils.putString(mContext, Constant.Videos, response);
                //解析数据
                videos = JsonUtils.parseVideos(response);
                handler.sendEmptyMessage(1);

            }
        });

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                if (videos != null && videos.size() > 0) {
                    tvNo.setVisibility(View.GONE);
                    adapter = new VedioAdapter(mContext, videos);
                    lv.setAdapter(adapter);
                    if (refresh) {
                        Toast.makeText(mContext, "刷新成功", Toast.LENGTH_SHORT).show();
                        refresh = false;
                    }
                } else {
                    lv.setVisibility(View.GONE);
                    tvNo.setVisibility(View.VISIBLE);
                }
            }

        }
    };

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    private void pop() {
        backgroundAlpha((float) 0.7);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_faxian, null, false);
        final PopupWindow window = new PopupWindow(contentView, UIUtils.dp2px(120), UIUtils.dp2px(130), true);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.showAsDropDown(llStart, TO_LEFT, BELOW);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //恢复透明度
                backgroundAlpha(1);
            }
        });

        //增加点击监听
        LinearLayout ll1 = contentView.findViewById(R.id.ll1);
        LinearLayout ll2 = contentView.findViewById(R.id.ll2);

        //本地视频
        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (Build.VERSION.SDK_INT < 19) {
                    intent = new Intent(Intent.ACTION_GET_CONTENT);
                    intent.setType("video/*");

                } else {
                    intent = new Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                }
                startActivityForResult(intent, 1);

//                Intent intent = new Intent(mContext, LocalFileActivity.class);
//                startActivityForResult(intent, 200);
//                getActivity().overridePendingTransition(R.anim.enter, R.anim.exit);
                window.dismiss();
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 拍摄视频
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
                // 录制视频最大时长120s
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 120);
                startActivityForResult(intent, 2);
                window.dismiss();
            }
        });

    }

    @OnClick(R.id.ll_start)
    public void onViewClicked(View v) {
        pop();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) { //系统的选择视频
                if (data != null) {
                    Uri uri = data.getData();
                    String path = getRealVideoPath(uri);
                    Log.e("TAG", path);
                    if (!TextUtils.isEmpty(path)) {
                        MediaMetadataRetriever media = new MediaMetadataRetriever();
                        media.setDataSource(path);
                        Bitmap bitmap = media.getFrameAtTime();
                        saveBitmapAsPng(bitmap);
                        Intent intent = new Intent(mContext, ReleaseVideoActivity.class);
                        intent.putExtra("path", path); //视频地址
                        intent.putExtra("thumPath", thumPath); //缩略图地址
                        startActivity(intent);
                    }

                }
            }
        }
        //拍摄视频的返回
        if (requestCode == 2) {
            Uri uri = data.getData();
            Cursor cursor = mContext.getContentResolver().query(uri, null, null, null, null);
            if (cursor != null && cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndex(MediaStore.Video.VideoColumns._ID));
                // 视频路径
                String filePath = cursor.getString(cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA));
                MediaMetadataRetriever media = new MediaMetadataRetriever();
                media.setDataSource(filePath);
                Bitmap bitmap = media.getFrameAtTime();
                saveBitmapAsPng(bitmap);
                cursor.close();
                Log.e("TAG", filePath);
                Intent intent = new Intent(mContext, ReleaseVideoActivity.class);
                intent.putExtra("path", filePath);
                intent.putExtra("thumPath", thumPath);
                startActivity(intent);
            }

        } else if (requestCode == 200) { //自定义选择视频返回
            if (requestCode == 200) {
                ArrayList<FileItem> resultFileList = data.getParcelableArrayListExtra("file");
                if (resultFileList != null && resultFileList.size() > 0) {
                    String filePath = resultFileList.get(0).getPath();
                    // ThumbnailUtils类2.2以上可用  Todo 获取视频缩略图
                    Bitmap bitmap = ThumbnailUtils.createVideoThumbnail(filePath, MediaStore.Images.Thumbnails.FULL_SCREEN_KIND);
                    // 图片Bitmap转file
                    saveBitmapAsPng(bitmap);
                    Intent intent = new Intent(mContext, ReleaseVideoActivity.class);
                    intent.putExtra("path", resultFileList.get(0).getPath());
                    intent.putExtra("thumPath", thumPath);
                    startActivity(intent);
                }

            }
        }
    }

    //根据视频的url获取视频的真实路径
    private String getRealVideoPath(Uri uri) {
        String[] filePathColumn = {MediaStore.Video.Media.DATA, MediaStore.Video.Media.DURATION};
        Cursor cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
        String videoPath = "";
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            int columnIndex1 = cursor.getColumnIndex(filePathColumn[1]);
            videoPath = cursor.getString(columnIndex);
            long lengh = cursor.getLong(columnIndex1);//视频的大小
            cursor.close();
            cursor = null;
            if (videoPath == null || videoPath.equals("null")) {
                Toast toast = Toast.makeText(getActivity(), "找不到视频", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        }
        return videoPath;
    }

    //把bitmap转化为png图片
    public void saveBitmapAsPng(Bitmap bmp) {
        int num = (int) (Math.random() * 1000000 + 1);
        try {
            thumPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + num + "thumPath.png";
            Log.e("TAG", thumPath);
            File f = new File(thumPath);
            if (f.exists()) {
                f.delete();
            } else {
                f.createNewFile();
            }
            FileOutputStream out = new FileOutputStream(f);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out);
            Log.e("TAG", f.getAbsolutePath());
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //根据url获取图片文件的绝对路径
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String getRealFilePath(Context context, Uri uri) {
        String filePath = null;
        String wholeID = DocumentsContract.getDocumentId(uri);

        // 使用':'分割
        String id = wholeID.split(":")[1];

        String[] projection = {MediaStore.Images.Media.DATA};
        String selection = MediaStore.Images.Media._ID + "=?";
        String[] selectionArgs = {id};

        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//
                projection, selection, selectionArgs, null);
        int columnIndex = cursor.getColumnIndex(projection[0]);
        if (cursor.moveToFirst()) filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    @OnClick(R.id.ll_all)
    public void onViewClicked() {
        startActivity(new Intent(mContext, AllKindsActivity.class));
    }
}