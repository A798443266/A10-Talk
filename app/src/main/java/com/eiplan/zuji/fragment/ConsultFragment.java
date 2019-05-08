package com.eiplan.zuji.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.activity.AddFriendActivity;
import com.eiplan.zuji.activity.ChatActivity;
import com.eiplan.zuji.activity.MainActivity;
import com.eiplan.zuji.activity.MyFriendsActivity;
import com.eiplan.zuji.activity.RobotChatActivity;
import com.eiplan.zuji.activity.SystemNotifyActivity;
import com.eiplan.zuji.adapter.SearchRecordAdapter;
import com.eiplan.zuji.single.CallManager;
import com.eiplan.zuji.single.VideoCallActivity;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.utils.UIUtils;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.ui.EaseConversationListFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

import static android.view.TouchDelegate.BELOW;
import static android.view.TouchDelegate.TO_LEFT;

/**
 * 咨询
 */
public class ConsultFragment extends BaseFragment {
    @BindView(R.id.fl)
    FrameLayout fl;
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    @Override
    protected void initView() {
        FragmentTransaction transaction = ConsultFragment.this.getActivity().getSupportFragmentManager().beginTransaction();
//        transaction.replace(R.id.fl, MyFragment.getIntence());
        transaction.replace(R.id.fl, new MyFragment());
        transaction.commit();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_consult;
    }


    @OnClick({R.id.iv_add, R.id.fab_chat_robot})
    public void onViewClicked1(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                pop();
                break;
            case R.id.fab_chat_robot:
                Intent intent = new Intent(mContext, RobotChatActivity.class);
                intent.putExtra("pic", SpUtils.getString(mContext, Constant.current_pic)); //传递过去用户的头像地址

//                Intent intent = new Intent(mContext, VideoCallActivity.class);
//                CallManager.getInstance().setChatId("15988817032");
//                CallManager.getInstance().setInComingCall(false);
//                CallManager.getInstance().setCallType(CallManager.CallType.VIDEO);
                startActivity(intent);
                break;
        }
    }


    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    private void pop() {
        backgroundAlpha((float) 0.7);
        View contentView = LayoutInflater.from(mContext).inflate(R.layout.popupwindow_zixun, null, false);
        final PopupWindow window = new PopupWindow(contentView, UIUtils.dp2px(120), UIUtils.dp2px(130), true);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        window.setOutsideTouchable(true);
        window.setTouchable(true);
        window.showAsDropDown(ivAdd, TO_LEFT, BELOW);

        window.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //恢复透明度
                backgroundAlpha(1);
            }
        });

        //增加点击监听
        LinearLayout ll1 = contentView.findViewById(R.id.ll1);
        LinearLayout ll2 = contentView.findViewById(R.id.ll2);
        LinearLayout ll3 = contentView.findViewById(R.id.ll3);

        ll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(mContext, AddFriendActivity.class));
                window.dismiss();
            }
        });

        ll2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "item2", Toast.LENGTH_SHORT).show();
                window.dismiss();
            }
        });

        ll3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "item3", Toast.LENGTH_SHORT).show();
                window.dismiss();
            }
        });

    }


    @OnClick({R.id.ll_system_msg, R.id.ll_my_friend, R.id.ll_dongtai})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_system_msg:
                startActivity(new Intent(mContext, SystemNotifyActivity.class));
                break;
            case R.id.ll_my_friend:
                startActivity(new Intent(mContext, MyFriendsActivity.class));
                break;
            case R.id.ll_dongtai:
                break;
        }
    }


    @SuppressLint("ValidFragment")
    static class MyFragment extends EaseConversationListFragment {
        /*private static MyFragment myFragment;

        public static MyFragment getIntence() {
            if (myFragment == null) {
                myFragment = new MyFragment();
            }
            return myFragment;
        }*/

        public MyFragment() {

        }

        @Override
        protected void initView() {
            super.initView();

            // 跳转到会话详情页面
            setConversationListItemClickListener(new EaseConversationListItemClickListener() {
                @Override
                public void onListItemClicked(EMConversation conversation) {
                    Intent intent = new Intent(getActivity(), ChatActivity.class);
                    // 传递参数
                    intent.putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId());
                    if(conversation.conversationId().equals("13627807142"))
                    intent.putExtra("name","吴伟");
                    // 是否是否群聊
                    if (conversation.getType() == EMConversation.EMConversationType.GroupChat) {
                        intent.putExtra(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_GROUP);
                    }
                    startActivity(intent);
                }
            });

            // 请空集合数据(针对5.0以下版本)
            conversationList.clear();
            // 监听会话消息
            EMClient.getInstance().chatManager().addMessageListener(emMessageListener);
        }

        //实现会话界面的监听
        private EMMessageListener emMessageListener = new EMMessageListener() {
            //当收到消息时
            @Override
            public void onMessageReceived(List<EMMessage> list) {
//            // 设置数据
                EaseUI.getInstance().getNotifier().notify(list);
                // 刷新页面
                refresh();
            }

            @Override
            public void onCmdMessageReceived(List<EMMessage> list) {

            }

            @Override
            public void onMessageRead(List<EMMessage> list) {

            }

            @Override
            public void onMessageDelivered(List<EMMessage> list) {

            }

            @Override
            public void onMessageRecalled(List<EMMessage> list) {

            }

            @Override
            public void onMessageChanged(EMMessage emMessage, Object o) {

            }
        };
    }
}
