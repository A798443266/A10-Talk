package com.eiplan.zuji.fragment.others;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.ReleaseDocActivity;
import com.eiplan.zuji.adapter.MeDocAdapter;
import com.eiplan.zuji.bean.ReleaseDocInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

public class MeDoc1Fragment extends BaseFragment {

    @BindView(R.id.lv)
    ListView lv;
    @BindView(R.id.btn_release)
    Button btn_release;
    @BindView(R.id.tv_no)
    TextView tvNo;

    private List<ReleaseDocInfo> docs;

    private MeDocAdapter adapter;
    private String data;//用来存放缓存数据


    @Override
    protected void initView() {

    }

    @Override
    public void initData() {
        data = SpUtils.getString(mContext, Constant.my_release_docs);
        if (!TextUtils.isEmpty(data)) {
            docs = JsonUtils.parseMyReleaseDocs(data);
        }
        OkHttpUtils.post().url(Constant.myReleaseDocs)
                .addParams("phone", SpUtils.getString(mContext, Constant.current_phone))
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
//                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        SpUtils.putString(mContext, Constant.my_release_docs, response);
                        //解析数据
                        docs = JsonUtils.parseMyReleaseDocs(response);
                        show();
                    }
                });
    }

    private void show() {
        if (docs != null && docs.size() > 0) {
            adapter = new MeDocAdapter(docs, mContext);
            lv.setAdapter(adapter);
        } else {
            tvNo.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_me_doc1;
    }


    @OnClick(R.id.btn_release)
    public void onViewClicked() {
        startActivity(new Intent(mContext, ReleaseDocActivity.class));
    }

}
