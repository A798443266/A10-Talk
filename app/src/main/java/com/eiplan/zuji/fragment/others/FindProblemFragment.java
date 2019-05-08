package com.eiplan.zuji.fragment.others;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.BigPhotoActivity;
import com.eiplan.zuji.activity.GotoAnswerActivity;
import com.eiplan.zuji.activity.MyAnswerActivity;
import com.eiplan.zuji.activity.ProblemDetailsActivity;
import com.eiplan.zuji.activity.ReleaseProblemActivity;
import com.eiplan.zuji.adapter.FindProblemShowAdapter;
import com.eiplan.zuji.bean.ProblemInfo;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.view.MyListViewInCoor;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;

/**
 * 问答论坛
 */

public class FindProblemFragment extends BaseFragment {

    @BindView(R.id.cv_user_pic)
    CircleImageView cvUserPic;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.ll)
    LinearLayout ll;
    @BindView(R.id.listview)
    MyListViewInCoor listview;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.ll_my_answer)
    LinearLayout llMyAnswer;
//    @BindView(R.id.ll_loading)
//    LinearLayout llLoading;

    private List<ProblemInfo> datas;
    private FindProblemShowAdapter adapter;
    private String save;//用来缓存json数据

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (datas != null && datas.size() > 0) {
                adapter = new FindProblemShowAdapter(datas, mContext);
                listview.setAdapter(adapter);

                listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(mContext, ProblemDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("problem", datas.get(position));
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

                adapter.setOnPhotoClickListener(new FindProblemShowAdapter.OnPhotoClickListener() {
                    @Override
                    public void photoClick(String path) {
                        Intent intent = new Intent(mContext, BigPhotoActivity.class);
                        intent.putExtra("path", path);
                        startActivity(intent);
                    }
                });
            }
//            llLoading.setVisibility(View.GONE);
        }
    };

    @Override
    protected void initView() {

        Glide.with(mContext).load(SpUtils.getString(mContext, Constant.current_pic)).error(R.drawable.p).into(cvUserPic);
        tvUsername.setText(SpUtils.getString(mContext, Constant.current_name));
        setListener();
    }

    @Override
    public void initData() {
//        llLoading.setVisibility(View.VISIBLE);
        save = SpUtils.getString(mContext, Constant.save_problems);
        if (!TextUtils.isEmpty(save)) {
            datas = JsonUtils.parseProblems(save);
        }


        OkHttpUtils.post().url(Constant.Prombles)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onResponse(String response, int id) {
                //解析json
                SpUtils.putString(mContext, Constant.save_problems, response);
                datas = JsonUtils.parseProblems(response);
                handler.sendEmptyMessage(1);

            }
        });
    }

    private void setListener() {
        //刷新
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (adapter != null) {
                    initData();
                    Toast.makeText(mContext, "刷新成功", Toast.LENGTH_SHORT).show();
                }
                refreshlayout.finishRefresh();
            }
        });

//        //加载更多
//        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
//            @Override
//            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
//                refreshLayout.finishLoadMore();
//            }
//        });

    }


    @Override
    public int getLayoutId() {
        return R.layout.fragmemt_find_problem;
    }


    @OnClick({R.id.ll_my_answer, R.id.ll_release, R.id.ll_answer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_my_answer:
                startActivity(new Intent(mContext, MyAnswerActivity.class));
                break;
            case R.id.ll_release:
                startActivity(new Intent(mContext, ReleaseProblemActivity.class));
                break;
            case R.id.ll_answer:
                startActivity(new Intent(mContext, GotoAnswerActivity.class));
                break;
        }
    }

}
