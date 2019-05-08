package com.eiplan.zuji.fragment.others;

import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.CollectionExpertActivity;
import com.eiplan.zuji.adapter.MeDocAdapter2;
import com.eiplan.zuji.bean.DocInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import cn.pedant.SweetAlert.SweetAlertDialog;
import okhttp3.Call;

public class MeDoc2Fragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.tv_no)
    TextView tvNo;

    private List<DocInfo> docs;
    private MeDocAdapter2 adapter;
    private String data;//用来存放缓存数据

    private SweetAlertDialog dialog;


    @Override
    protected void initView() {
    }

    @Override
    public void initData() {
        data = SpUtils.getString(mContext, Constant.my_collect_docs);
        if (!TextUtils.isEmpty(data)) {
            docs = JsonUtils.parseMyCollectAndBuyDocs(data);
        }

        OkHttpUtils.post().url(Constant.myCollectfiles)
                .addParams("phone", SpUtils.getString(mContext, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SpUtils.putString(mContext, Constant.my_collect_docs, response);
                        //解析数据
                        docs = JsonUtils.parseMyCollectAndBuyDocs(response);
                        show();
                    }
                });
    }

    private void show() {
        if (docs != null && docs.size() > 0) {
            tvNo.setVisibility(View.GONE);
            adapter = new MeDocAdapter2(docs, mContext);
            lv.setAdapter(adapter);

            adapter.setOnItemRightClickListener(new MeDocAdapter2.OnItemRightClickListener() {
                @Override
                public void itemRightClick(View view, final int position) {
                    new SweetAlertDialog(mContext, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示")
                            .setContentText("您确定要取消收藏该文档？")
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    //去服务器中取消
                                    cancelDoc(position);
                                    /*docs.remove(position);
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(mContext, "取消成功", Toast.LENGTH_SHORT).show();
                                    sweetAlertDialog.dismiss();
                                    if (docs.size() == 0) {
                                        tvNo.setVisibility(View.VISIBLE);
                                    }*/
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .setCancelText("取消")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                }
            });
        } else {
            tvNo.setVisibility(View.VISIBLE);
        }
    }

    private void cancelDoc(final int position) {
        OkHttpUtils.post().url(Constant.cancelCollectfile)
                .addParams("fileId", docs.get(position).getId() + "")
                .addParams("phone", SpUtils.getString(mContext, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("取消收藏失败!")
                                .show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        JSONObject jsonObject = JSON.parseObject(response);
                        if (jsonObject.getInteger("code") == 100) {
                            dialog = new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE);
                            new SweetAlertDialog(mContext, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("取消收藏成功!")
                                    .show();
                            docs.remove(position);
                            adapter.notifyDataSetChanged();
                        } else {
                            new SweetAlertDialog(mContext, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("取消收藏失败!")
                                    .show();
                        }
                        initData();
                    }
                });
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me_doc2;
    }

}
