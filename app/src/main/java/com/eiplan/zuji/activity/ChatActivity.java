package com.eiplan.zuji.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseChatFragment;
import com.hyphenate.easeui.widget.chatrow.EaseCustomChatRowProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChatActivity extends FragmentActivity {
    @BindView(R.id.fl_chat)
    FrameLayout flChat;

    private String mHxid;//环信id或者群id(主要看传递过来的是什么)
    private EaseChatFragment easeChatFragment;
    private int mChatType;//会话类型，单聊还是群聊
    private LocalBroadcastManager mLBM;

    private BroadcastReceiver ExitGroupReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (mHxid.equals(intent.getStringExtra(Constant.GROUP_ID))) {//如果收到退群或者解散群的广播
                // 结束当前页面
                Toast.makeText(context, "该群已被群主解散", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    };

    private String name = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.bind(this);

        name = getIntent().getStringExtra("name"); //获取用户名称
        initData();
        initListener();

    }

    private void initData() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // 创建一个会话的fragment（用EaseUi的fragment）
        mHxid = getIntent().getStringExtra(EaseConstant.EXTRA_USER_ID);//获取传递过来的环信id
        if (TextUtils.isEmpty(name)) {
            easeChatFragment = new EaseChatFragment(mHxid);
        } else
            easeChatFragment = new EaseChatFragment(name);
        //设置fragment的参数
        easeChatFragment.setArguments(getIntent().getExtras());
        // 替换fragment
        transaction.replace(R.id.fl_chat, easeChatFragment).commit();

        // 获取聊天类型
        mChatType = getIntent().getExtras().getInt(EaseConstant.EXTRA_CHAT_TYPE);
        // 获取发送广播的管理者
        mLBM = LocalBroadcastManager.getInstance(ChatActivity.this);
    }

    private void initListener() {
        easeChatFragment.setChatFragmentHelper(new EaseChatFragment.EaseChatFragmentHelper() {
            @Override
            public void onSetMessageAttributes(EMMessage message) {
                //设置要发送扩展消息用户昵称
                message.setAttribute("name", SpUtils.getString(ChatActivity.this, Constant.current_name));
//                message.setAttribute(Constant.USER, EMClient.getInstance().getCurrentUser());
                //设置要发送扩展消息用户头像
                message.setAttribute("picturl", SpUtils.getString(ChatActivity.this, Constant.current_pic));
            }

            //点击标题栏右边图标进入群详情页面
            @Override
            public void onEnterToChatDetails() {
                Intent intent = new Intent(ChatActivity.this, GroupDetailActivity.class);
                // 群id
                intent.putExtra(Constant.GROUP_ID, mHxid);
                startActivity(intent);
            }

            //头像被点击
            @Override
            public void onAvatarClick(String username) {
                Toast.makeText(ChatActivity.this, "头像被点击了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAvatarLongClick(String username) {

            }

            @Override
            public boolean onMessageBubbleClick(EMMessage message) {
                return false;
            }

            //点击消息框
            @Override
            public void onMessageBubbleLongClick(EMMessage message) {

            }

            @Override
            public boolean onExtendMenuItemClick(int itemId, View view) {
                return false;
            }

            @Override
            public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
                return null;
            }
        });

        // 如果当前类型为群聊
        if (mChatType == EaseConstant.CHATTYPE_GROUP) {
//            // 注册退群广播
            mLBM.registerReceiver(ExitGroupReceiver, new IntentFilter(Constant.EXIT_GROUP));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mChatType == EaseConstant.CHATTYPE_GROUP) {
            mLBM.unregisterReceiver(ExitGroupReceiver);//如果注册了广播要解注册
        }
    }
}
