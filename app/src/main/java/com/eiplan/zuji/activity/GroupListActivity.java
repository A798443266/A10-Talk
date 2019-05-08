package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.GroupListAdapter;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.UIUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.exceptions.HyphenateException;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的群聊列表界面
 */

public class GroupListActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.titlebar_bg)
    RelativeLayout titlebarBg;
    @BindView(R.id.lv_grouplist)
    ListView lvGrouplist;

    private LinearLayout ll_creatgroup;//创建群的控件
    private GroupListAdapter groupListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_list);
        ButterKnife.bind(this);
        initView();
        initData();
        initListener();
    }

    private void initListener() {

        // listview条目点击事件
        lvGrouplist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0) {//positon=0为listview的头布局
                    return;
                }
                Intent intent = new Intent(GroupListActivity.this, ChatActivity.class);
                // 传递会话类型
                intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                // 群id
                EMGroup emGroup = EMClient.getInstance().groupManager().getAllGroups().get(position - 1);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, emGroup.getGroupId());
                startActivity(intent);
                finish();
            }
        });

        // 跳转到新建群界面
        ll_creatgroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupListActivity.this, NewGroupActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initView() {
        tvRight.setVisibility(View.GONE);
        tvTitle.setText("我的群组");
        titlebarBg.setBackgroundColor(UIUtils.getColor(R.color.color_white));
        ivBack.setImageResource(R.drawable.icon_back);
        tvTitle.setTextColor(UIUtils.getColor(R.color.color_system_blank));
        // 添加头布局
        View headerView = View.inflate(this, R.layout.header_grouplist, null);
        lvGrouplist.addHeaderView(headerView);
        ll_creatgroup = headerView.findViewById(R.id.ll_creatgroup);
    }

    private void initData() {
        // 初始化listview
        groupListAdapter = new GroupListAdapter(this);
        lvGrouplist.setAdapter(groupListAdapter);
        // 从环信服务器获取所有群的信息
        getGroupsFromServer();
    }

    private void getGroupsFromServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    // 从网络获取数据
                    final List<EMGroup> mGroups = EMClient.getInstance().groupManager().getJoinedGroupsFromServer();
                    // 更新页面
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            Toast.makeText(GroupListActivity.this, "加载群信息成功", Toast.LENGTH_SHORT).show();
//                            groupListAdapter.refresh(mGroups);
                            // 刷新
                            refresh();
                        }
                    });
                } catch (HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupListActivity.this, "加载群信息失败", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    // 刷新
    private void refresh() {
        groupListAdapter.refresh(EMClient.getInstance().groupManager().getAllGroups());
    }

    //当页面再次可见的时候刷新页面
    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
