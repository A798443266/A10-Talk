package com.eiplan.zuji.fragment.others;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.MeDocAdapter3;
import com.eiplan.zuji.bean.DocInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.white.progressview.CircleProgressView;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.Unbinder;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class MeDoc3Fragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_no)
    TextView tvNo;

    private List<DocInfo> docs;
    private MeDocAdapter3 adapter;
    private SweetAlertDialog dialog;
    private String basePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/downloadFile";
    private Map<Integer, String> map = new HashMap<>();//用来存放下载文件的地址

    private String data;


    @Override
    protected void initView() {
        tvNo.setText("您还没有购买任何文档");
    }

    @Override
    public void initData() {
        data = SpUtils.getString(mContext, Constant.my_buy_docs);
        if (!TextUtils.isEmpty(data)) {
            docs = JsonUtils.parseMyCollectAndBuyDocs(data);
        }
        OkHttpUtils.post().url(Constant.myBuyfiles)
                .addParams("phone", SpUtils.getString(mContext, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {

                        SpUtils.putString(mContext, Constant.my_buy_docs, response);
                        //解析数据
                        docs = JsonUtils.parseMyCollectAndBuyDocs(response);
                        show();
                    }
                });
    }

    private void show() {
        if (docs != null && docs.size() > 0) {
            tvNo.setVisibility(View.GONE);
            adapter = new MeDocAdapter3(docs, mContext);
            lv.setAdapter(adapter);
            adapter.setOnItemRightClickListener(new MeDocAdapter3.OnItemRightClickListener() {
                @Override
                public void itemRightClick(final View view, final int position, final CircleProgressView fillInner, final Button btn_open) {
                    view.setVisibility(View.GONE);
                    fillInner.setVisibility(View.VISIBLE);
                    dialog = new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE);
                    dialog.setTitleText("提示");
                    dialog.setContentText("您确定要下载该文档？");
                    dialog.setConfirmText("确定");
                    dialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            download(view, position, fillInner, btn_open);

                        }
                    });
                    dialog.setCancelText("取消");
                    dialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sDialog) {
                            sDialog.dismiss();
                        }
                    });
                    dialog.show();
                }
            });

            //打开文件
            adapter.setOnItemOpenClickListener(new MeDocAdapter3.OnItemOpenClickListener() {
                @Override
                public void itemOpenClick(int position) {
                    String path = map.get(position);
                    Toast.makeText(mContext, path, Toast.LENGTH_SHORT).show();
                }
            });

        } else {
            tvNo.setVisibility(View.VISIBLE);
        }
    }

    //开始下载
    private void download(final View view, final int position, final CircleProgressView fillInner, final Button btn_open) {
        dialog.dismiss();
        String link = docs.get(position).getLink(); //文件下载地址
        final String type = link.substring(link.lastIndexOf("."), link.length()); //获取文件类型
        final String fileName = docs.get(position).getName() + type; //拼成文件名
        OkHttpUtils.get().url(docs.get(position).getLink()).build()
                .execute(new FileCallBack(basePath, fileName) {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        Log.e("TAG", "下载错误");
                        Toast.makeText(mContext, "下载失败，请检查网络", Toast.LENGTH_SHORT).show();
                        fillInner.setVisibility(View.GONE);
                        view.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void inProgress(float progress, long total, int id) {
                        int process = (int) (100.0 * progress);
                        Log.e("info: ", progress + "");
                        fillInner.setProgress(process);

                    }

                    @Override
                    public void onResponse(File response, int id) {
                        Log.e("TAG", "下载完成");
                        map.put(position, basePath + "/" + fileName);
                        SystemClock.sleep(200);
                        fillInner.setVisibility(View.GONE);
                        btn_open.setVisibility(View.VISIBLE);
                        btn_open.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (type.equals(".doc") || type.equals(".docx")) {
                                    openFile(basePath + "/" + fileName, 1);//打开word

                                } else if (type.equals(".pdf")) {
                                    openFile(basePath + fileName, 2);//打开pdf
                                }
                            }

                        });
                    }
                });
    }

    private void openFile(String s, int type) {
        File file = new File(s);
        if (file.exists()) {
            Uri uri = Uri.fromFile(file);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (type == 1)
                intent.setDataAndType(uri, "application/msword");
            else if (type == 2)
                intent.setDataAndType(uri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(mContext, "该文档出现问题了，无法打开", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(mContext, "该文档出现问题了，无法打开", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me_doc2;
    }

}
