package com.eiplan.zuji.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.GroupDetailAdapter;
import com.eiplan.zuji.bean.ContactInfo;
import com.eiplan.zuji.model.Model;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.JsonUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.exceptions.HyphenateException;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

/**
 * 群组详情界面
 */

public class GroupDetailActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.titlebar_bg)
    RelativeLayout titlebarBg;
    @BindView(R.id.gv_groupdetail)
    GridView gvGroupdetail;
    @BindView(R.id.btn_out)
    Button btnOut;
    @BindView(R.id.tv_desc)
    TextView tvDesc;

    private EMGroup mGroup;//从会话页面点击传递过来的群
    private GroupDetailAdapter groupDetailAdapter;
    private List<ContactInfo> mUsers;//群已有的成员
    private String description;//群描述
    private List<ContactInfo> allContacts = new ArrayList<>();//用来存放从自己服务器中获取的所有联系人信息，名称和头像，手机号等

    private GroupDetailAdapter.OnGroupDetailListener mOnGroupDetailListener = new GroupDetailAdapter.OnGroupDetailListener() {
        @Override
        public void onAddMembers() {//添加新成员的回调
            // 跳转到选择联系人页面
            Intent intent = new Intent(GroupDetailActivity.this, PickContactActivity.class);
            // 传递群id
            intent.putExtra(Constant.GROUP_ID, mGroup.getGroupId());
            startActivityForResult(intent, 2);
        }

        @Override
        public void onDeleteMember(final ContactInfo user) {
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 从环信服务器中删除此人
                        EMClient.getInstance().groupManager().removeUserFromGroup(mGroup.getGroupId(), user.getPhone());
                        // 更新页面
                        getMembersFromHxServer();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "删除成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "删除失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);
        ButterKnife.bind(this);

        titlebarBg.setBackgroundColor(UIUtils.getColor(R.color.color_white));
        tvRight.setVisibility(View.GONE);
        tvTitle.setText("群组资料");
        tvTitle.setTextColor(UIUtils.getColor(R.color.color_system_blank));
        ivBack.setBackgroundResource(R.drawable.icon_back);

        getData();
        initData();
        // 初始化button显示
        initButtonDisplay();
        initListener();
    }

    //当点击了减号之后，再点击一次就可以取消删除模式
    private void initListener() {
        gvGroupdetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        // 判断当前是否是删除模式,如果是删除模式
                        if (groupDetailAdapter.ismIsDeleteModel()) {
                            // 切换为非删除模式
                            groupDetailAdapter.setmIsDeleteModel(false);
                            // 刷新页面
                            groupDetailAdapter.notifyDataSetChanged();
                        }
                        break;
                }
                return false;
            }
        });
    }

    private void initData() {
        // 初始化button显示
        initButtonDisplay();
        // 初始化gridview
        initGridview();
        //从自己的服务器中获取全部的用户信息,接着在从环信服务器中获取群用户信息，然后比对手机号，刷新适配器
        getContact();
//        getMembersFromHxServer();

    }

    // 从环信服务器获取所有的群成员
    private void getMembersFromHxServer() {
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                List<String> memberList = new ArrayList<>();
                EMCursorResult<String> result = null;
                try {
                    result = EMClient.getInstance().groupManager().fetchGroupMembers(mGroup.getGroupId(),
                            result != null ? result.getCursor() : "", 20);
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
                memberList.addAll(result.getData());
                Log.e("TAG",memberList.size() + "");
                if (memberList.size() > 0){
                    Log.e("TAG", memberList.get(0));
                }

                try {
                    // 从环信服务器获取所有的群成员信息
                    EMGroup emGroup = EMClient.getInstance().groupManager().getGroupFromServer(mGroup.getGroupId());
                    final List<String> members = emGroup.getMembers();

                    Log.e("TAG", emGroup.getGroupId());
                    Log.e("TAG", emGroup.getGroupName());
                    Log.e("TAG", members == null ? "0" : members.size() + "");
                    description = emGroup.getDescription();//获取群聊介绍
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!TextUtils.isEmpty(description)) {
                                tvDesc.setText(description);
                            }
                        }
                    });

                    if (members != null && members.size() >= 0) {
                        mUsers = new ArrayList<ContactInfo>();
                        //找出
                        for (String member : members) {
                            ContactInfo userInfo = new ContactInfo();
                            for (ContactInfo u : allContacts) {//找出该环信id的用户信息
                                if (u.getPhone().equals(member)) {
                                    userInfo.setPhone(member);
                                    userInfo.setName(u.getName());
                                    userInfo.setPic(u.getPic());
                                    mUsers.add(userInfo);
                                    break;
                                }
                            }
                        }

                    }
                    // 更新页面
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // 刷新适配器
                            groupDetailAdapter.refresh(mUsers);
                            Log.e("TAG", mUsers == null ? "0" : mUsers.size() + "");
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(GroupDetailActivity.this, "获取群信息失败" + e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void getContact() {

        //从自己的服务器中取出全部用户的头像名称信息，包括专家
        OkHttpUtils.get().url(Constant.getAllContact).build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

            }

            @Override
            public void onResponse(String response, int id) {
                //解析json，得到所有用户的信息
                allContacts = JsonUtils.parseAllContacts(response);
                // 从环信服务器获取所有的群成员
                getMembersFromHxServer();
            }
        });

    }

    private void initGridview() {
        // 当前用户是群组 || 群公开了
        boolean isCanModify = EMClient.getInstance().getCurrentUser().equals(mGroup.getOwner()) || mGroup.isPublic();
        groupDetailAdapter = new GroupDetailAdapter(this, isCanModify, mOnGroupDetailListener);
        gvGroupdetail.setAdapter(groupDetailAdapter);
    }


    @OnClick({R.id.iv_back})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    //初始化button监听
    private void initButtonDisplay() {
        // 判断当前用户是否是群主
        if (EMClient.getInstance().getCurrentUser().equals(mGroup.getOwner())) {// 群主
            btnOut.setText("解散该群");
            btnOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 去环信服务器解散群
                                EMClient.getInstance().groupManager().destroyGroup(mGroup.getGroupId());
                                // 发送退群的广播
                                exitGroupBroatCast();
                                // 更新页面
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "解散群成功", Toast.LENGTH_SHORT).show();
                                        // 结束当前页面
                                        finish();
                                    }
                                });
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "解散群失败" + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        } else {// 群成员
            btnOut.setText("退出该群");
            btnOut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                // 告诉环信服务器退群
                                EMClient.getInstance().groupManager().leaveGroup(mGroup.getGroupId());
                                // 发送退群广播
                                exitGroupBroatCast();
                                // 更新页面
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "退群成功", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });
                            } catch (final HyphenateException e) {
                                e.printStackTrace();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(GroupDetailActivity.this, "退群失败" + e.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    });
                }
            });
        }
    }

    // 发送退群和解散群广播
    private void exitGroupBroatCast() {
        LocalBroadcastManager mLBM = LocalBroadcastManager.getInstance(GroupDetailActivity.this);
        Intent intent = new Intent(Constant.EXIT_GROUP);
        intent.putExtra(Constant.GROUP_ID, mGroup.getGroupId());//传递群id
        mLBM.sendBroadcast(intent);
    }

    // 获取传递过来的数据
    private void getData() {
        Intent intent = getIntent();
        String groupId = intent.getStringExtra(Constant.GROUP_ID);
        if (groupId == null) {
            return;
        } else {
            mGroup = EMClient.getInstance().groupManager().getGroup(groupId);
        }
    }

    //收到返回添加新成员的数据
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            // 获取返回的准备邀请的群成员信息
            final String[] memberses = data.getStringArrayExtra("members");
            Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        // 去环信服务器，发送邀请信息
                        EMClient.getInstance().groupManager().addUsersToGroup(mGroup.getGroupId(), memberses);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "发送邀请成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } catch (final HyphenateException e) {
                        e.printStackTrace();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(GroupDetailActivity.this, "发送邀请失败" + e.toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            });
        }
    }
}
