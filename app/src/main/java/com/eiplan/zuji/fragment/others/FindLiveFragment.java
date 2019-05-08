package com.eiplan.zuji.fragment.others;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.LivePlayActivity;
import com.eiplan.zuji.activity.NoExpertActivity;
import com.eiplan.zuji.activity.ReLivePlayActivity;
import com.eiplan.zuji.activity.RoomSettingActivity;
import com.eiplan.zuji.adapter.LiveReRoomAdapter;
import com.eiplan.zuji.adapter.LiveRoomAdapter;
import com.eiplan.zuji.bean.LiveRoom;
import com.eiplan.zuji.bean.ReLiveRoom;
import com.eiplan.zuji.fragment.BaseFragment;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.view.MyGridView;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * 直播
 */

public class FindLiveFragment extends BaseFragment {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.gv_lives)
    MyGridView gvLives;
    @BindView(R.id.gv_re)
    MyGridView gvRe;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;


    private String chatRoomId = "72528353624065";//聊天室id
    private LiveRoomAdapter adapter;
    private LiveReRoomAdapter adapter1;
    private List<LiveRoom> rooms;
    private List<ReLiveRoom> rerooms;
    private LiveRoom room;
    private int isExpert; //判断是否是专家


    @Override
    protected void initView() {
        //让软键盘出现搜索
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                    search();
                    return true;
                }
                return false;
            }
        });

        isExpert = SpUtils.getInt(mContext, Constant.current_isexpert);

        rooms = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            rooms.add(new LiveRoom());
        }
        adapter = new LiveRoomAdapter(rooms, mContext);

        gvLives.setAdapter(adapter);
        gvRe.setAdapter(adapter);
        gvLives.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                LiveRoom room = rooms.get(position);
                Intent intent = new Intent(mContext, LivePlayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("live", room);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        setListener();
    }

    private void search() {
        String id = etSearch.getText().toString().trim();
        if (TextUtils.isEmpty(id)) {
            Toast.makeText(mContext, "搜索内容不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpUtils.get().url(Constant.seeLive)
                .addParams("id", id)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        room = JsonUtils.parseSearchLive(response);
                        rooms.add(room);
                        adapter = new LiveRoomAdapter(rooms, mContext);
                        gvLives.setAdapter(adapter);
                        gvLives.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                LiveRoom room = rooms.get(position);
                                Intent intent = new Intent(mContext, LivePlayActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("live", room);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                    }
                });
    }

    @Override
    public void initData() {
        OkHttpUtils.get().url(Constant.getlives)
                .build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(String response, int id) {
                        rooms = JsonUtils.parseLiveRooms(response);
                        rerooms = JsonUtils.parseReLiveRooms(response);
                        show();
                    }
                });
    }

    private void show() {
        if (rooms != null && rooms.size() > 0) {
            adapter = new LiveRoomAdapter(rooms, mContext);
            gvLives.setAdapter(adapter);
            gvLives.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    LiveRoom room = rooms.get(position);
                    Intent intent = new Intent(mContext, LivePlayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("live", room);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });

        }
        if (rerooms != null && rerooms.size() > 0) {
            adapter1 = new LiveReRoomAdapter(rerooms, mContext);
            gvRe.setAdapter(adapter1);
            gvRe.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ReLiveRoom reroom = rerooms.get(position);
                    Intent intent = new Intent(mContext, ReLivePlayActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("reroom", reroom);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.fragmemt_find_live;
    }


    @OnClick(R.id.ll_start)
    public void onViewClicked() {

        if (isExpert == 1) {
            startActivity(new Intent(mContext, RoomSettingActivity.class));
//            startActivity(new Intent(mContext, LiveStreamActivity.class));
        } else {
            startActivity(new Intent(mContext, NoExpertActivity.class));
        }
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

        //加载更多
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore();
            }
        });

    }


}
