package com.eiplan.zuji.fragment.others;

import android.widget.ListView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.StudyCommendAdapter;
import com.eiplan.zuji.bean.StudyCommendInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import okhttp3.Call;

public class MySubjectFragment3 extends BaseFragment {
    @BindView(R.id.lv)
    ListView lv;

    private StudyCommendAdapter adapter;
    private List<StudyCommendInfo> datas = new ArrayList<>();

    @Override
    protected void initView() {
        EventBus.getDefault().register(this);
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
        return R.layout.fragment_my_subject3;
    }

    //接收消息
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMessageEvent(StudyCommendInfo s) {
        datas.add(0, s);
        adapter = new StudyCommendAdapter(mContext, datas);
        lv.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }
}
