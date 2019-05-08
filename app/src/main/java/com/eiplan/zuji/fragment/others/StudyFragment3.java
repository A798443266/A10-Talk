package com.eiplan.zuji.fragment.others;


import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.StudyCommendAdapter;
import com.eiplan.zuji.bean.StudyCommendInfo;
import com.eiplan.zuji.bean.VideoInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.view.MyListView;
import com.eiplan.zuji.view.MyListViewInCoor;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class StudyFragment3 extends BaseFragment {
    @BindView(R.id.lv)
    MyListView lv;

    private StudyCommendAdapter adapter;
    private List<StudyCommendInfo> datas = new ArrayList<>();

    @Override
    protected void initView() {

    }

    @Override
    public void initData() {
        OkHttpUtils.get().url(Constant.getStudyCommends)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        show();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        datas = JsonUtils.parseStudyCommends(response);
                        show();
                    }
                });
    }

    private void show() {
        if (datas != null && datas.size() > 0) {
            adapter = new StudyCommendAdapter(mContext, datas);
            lv.setAdapter(adapter);
        } else {
            Toast.makeText(mContext, "暂无评价", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_study3;
    }

}
