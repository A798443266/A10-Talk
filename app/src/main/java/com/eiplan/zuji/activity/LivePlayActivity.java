package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.LiveRoom;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.view.FlowLikeView;
import com.eiplan.zuji.view.RoomMessagesView;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 观看直播的界面
 */

public class LivePlayActivity extends AppCompatActivity {

    @BindView(R.id.video_view)
    TXCloudVideoView videoView;
    @BindView(R.id.cv_user_pic)
    CircleImageView cvUserPic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_look)
    TextView tvLook;
    @BindView(R.id.tv_room_id)
    TextView tvRoomId;
    @BindView(R.id.iv_send)
    ImageView ivSend;
    @BindView(R.id.et_chat_msg)
    EditText etChatMsg;
    @BindView(R.id.rl_chat_bottom)
    RelativeLayout rlChatBottom;
    @BindView(R.id.message_view)
    RoomMessagesView messageView;
    @BindView(R.id.comment_image)
    ImageView commentImage;
    @BindView(R.id.flowlikeview)
    FlowLikeView flowlikeview;

    private String chatRoomId = "76257659256833";//聊天室id
    private EMChatRoom chatroom;//聊天室
    private int watchedCount = 0;//观看人数
    private TXLivePlayer mLivePlayer;//显示的界面
    private LiveRoom room; //传递过来的直播间
    private String flvUrl; //播放地址

    private static final int MAX_SIZE = 10;//观看人员列表最多显示10个
    private LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    protected boolean isMessageListInited;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_play);
        ButterKnife.bind(this);

        //隐藏顶部的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        room = (LiveRoom) getIntent().getSerializableExtra("live");

        tvLook.setText(room.getSee() + "");
        tvName.setText(room.getName());
        tvRoomId.setText(room.getId() + "");
        Glide.with(this).load(room.getPic()).error(R.drawable.p).into(cvUserPic);
//        flvUrl = room.getPlayurl();
        flvUrl = "http://192.168.0.146:8787/A13/static/live/4.mp4";

        //创建 player 对象
        mLivePlayer = new TXLivePlayer(this);
        //关键 player 对象与界面 view
        mLivePlayer.setPlayerView(videoView);
//        mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_LIVE_RTMP); //推荐 FLV
        mLivePlayer.startPlay(flvUrl, TXLivePlayer.PLAY_TYPE_VOD_MP4); //推荐 FLV

        mLivePlayer.setPlayListener(new ITXLivePlayListener() {
            @Override
            public void onPlayEvent(int i, Bundle bundle) {
                if (i == 2006) {
                    Toast.makeText(LivePlayActivity.this, "直播已经结束", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNetStatus(Bundle bundle) {

            }
        });

        //加入聊天室
        EMClient.getInstance().chatroomManager().joinChatRoom(chatRoomId, new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(EMChatRoom emChatRoom) {
                chatroom = emChatRoom;
                onMessageListInit();
                addChatRoomChangeListener();
//                Toast.makeText(LivePlayActivity.this, "加入聊天室成功！", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "加入聊天室成功");
            }

            @Override
            public void onError(int i, String s) {
//                Toast.makeText(LivePlayActivity.this, "加入聊天室失败", Toast.LENGTH_SHORT).show();
            }
        });

        //显示打开发弹幕的按钮
        messageView.setShowMessage(new RoomMessagesView.ShowMessage() {
            @Override
            public void onClick() {
                commentImage.setVisibility(View.VISIBLE);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatroomManager().leaveChatRoom(SpUtils.getString(this, Constant.current_phone));
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        mLivePlayer.stopPlay(false); // true 代表清除最后一帧画面
        videoView.onDestroy();
    }

    @OnClick({R.id.iv_close, R.id.comment_image, R.id.iv_like})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.comment_image:
                messageView.setShowInputView(true);
                commentImage.setVisibility(View.GONE);
                break;
            case R.id.iv_like:
                flowlikeview.addLikeView();
                break;
        }
    }


    //聊天室人员变动的监听
    protected void addChatRoomChangeListener() {
        EMChatRoomChangeListener chatRoomChangeListener = new EMChatRoomChangeListener() {

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(chatRoomId)) {
                    finish();
                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                onRoomMemberAdded(participant);
            }

            //当有人退出聊天室
            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                //                showChatroomToast("member : " + participant + " leave the room : " + roomId + " room name : " + roomName);
                onRoomMemberExited(participant);
            }


            @Override
            public void onRemovedFromChatRoom(int i, String roomId, String roomName, String participant) {
                if (roomId.equals(chatRoomId)) {
                    String curUser = EMClient.getInstance().getCurrentUser();
                    if (curUser.equals(participant)) {
                        EMClient.getInstance().chatroomManager().leaveChatRoom(roomId);
                        //postUserChangeEvent(StatisticsType.LEAVE, curUser);
                        Toast.makeText(LivePlayActivity.this, "你已被移除出此房间", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {

                    }
                }
            }

            @Override
            public void onMuteListAdded(String chatRoomId, List<String> mutes, long expireTime) {
                for (String name : mutes) {
                    showMemberChangeEvent(name, "被禁言");
                }
            }

            @Override
            public void onMuteListRemoved(String chatRoomId, List<String> mutes) {
                for (String name : mutes) {
                    showMemberChangeEvent(name, "被解除禁言");
                }
            }

            @Override
            public void onAdminAdded(String chatRoomId, String admin) {
                if (admin.equals(EMClient.getInstance().getCurrentUser())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            userManagerView.setVisibility(View.VISIBLE);
                        }
                    });
                }
                showMemberChangeEvent(admin, "被提升为房管");
            }

            @Override
            public void onAdminRemoved(String chatRoomId, String admin) {
                if (admin.equals(EMClient.getInstance().getCurrentUser())) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
//                            userManagerView.setVisibility(View.INVISIBLE);
                        }
                    });
                }
                showMemberChangeEvent(admin, "被解除房管");
            }

            @Override
            public void onOwnerChanged(String chatRoomId, String newOwner, String oldOwner) {

            }

            @Override
            public void onAnnouncementChanged(String s, String s1) {

            }
        };
        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
    }


    //有人加入聊天室
    private synchronized void onRoomMemberAdded(String name) {
        watchedCount++;
//        showMemberChangeEvent(name, "进入直播间");
        Log.e("TAG", name + "added");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLook.setText(String.valueOf(watchedCount));
            }
        });

    }


    //发布公告事件
    private void showMemberChangeEvent(String username, String event) {  //username是环信id，本项目中即手机号
        EMMessage message = EMMessage.createReceiveMessage(EMMessage.Type.TXT);
        message.setTo(chatRoomId);
        message.setFrom(username);
        EMTextMessageBody textMessageBody = new EMTextMessageBody(event);
        message.addBody(textMessageBody);
        message.setChatType(EMMessage.ChatType.ChatRoom);
        EMClient.getInstance().chatManager().saveMessage(message);
        messageView.refreshSelectLast();
    }


    //有人退出聊天室
    private synchronized void onRoomMemberExited(final String name) {
//        memberList.remove(name);
//        watchedCount--;
//        Log.e("TAG", name + "exited");
//        showMemberChangeEvent(name, "离开");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                tvLook.setText(String.valueOf(watchedCount));
                //刷新聊天室人员列表
//                horizontalRecyclerView.getAdapter().notifyDataSetChanged();
                //若果退出的是当前主播
//                if(name.equals(anchorId)){
//                    Toast.makeText(LiveStreamActivity.this,"主播已结束直播",Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }


    protected void onMessageListInit() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageView.init(chatRoomId);
                messageView.setMessageViewListener(new RoomMessagesView.MessageViewListener() {
                    @Override
                    public void onMessageSend(String content) {
                        EMMessage message = EMMessage.createTxtSendMessage(content, chatRoomId);
//                        if (messageView.isBarrageShow) {
//                            message.setAttribute(DemoConstants.EXTRA_IS_BARRAGE_MSG, true);
//                            barrageLayout.addBarrage(content,
//                                    EMClient.getInstance().getCurrentUser());
//                        }
                        message.setChatType(EMMessage.ChatType.ChatRoom);
                        EMClient.getInstance().chatManager().sendMessage(message);
                        message.setMessageStatusCallback(new EMCallBack() {
                            @Override
                            public void onSuccess() {
                                //刷新消息列表
                                messageView.refreshSelectLast();
                            }

                            @Override
                            public void onError(int i, String s) {
                                Toast.makeText(LivePlayActivity.this, "消息发送失败！", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onProgress(int i, String s) {

                            }
                        });
                    }

                    @Override
                    public void onItemClickListener(final EMMessage message) {
                        //if(message.getFrom().equals(EMClient.getInstance().getCurrentUser())){
                        //    return;
                        //}
                        String clickUsername = message.getFrom();
//                        showUserDetailsDialog(clickUsername);
                    }

                    @Override
                    public void onHiderBottomBar() {
//                        bottomBar.setVisibility(View.VISIBLE);
                    }
                });
                messageView.setVisibility(View.VISIBLE);
                isMessageListInited = true;
                //注册消息监听
                EMClient.getInstance().chatManager().addMessageListener(msgListener);
            }
        });
    }

    EMMessageListener msgListener = new EMMessageListener() {
        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            for (EMMessage message : messages) {
                String username = null;
                // 群组消息
                if (message.getChatType() == EMMessage.ChatType.GroupChat
                        || message.getChatType() == EMMessage.ChatType.ChatRoom) {
                    username = message.getTo();
                } else {
                    // 单聊消息
                    username = message.getFrom();
                }
                // 如果是当前会话的消息，刷新聊天页面
                if (username.equals(chatRoomId)) {
                    messageView.refreshSelectLast();
                } else {

                }
            }
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
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
            if (isMessageListInited) {
                messageView.refresh();
            }
        }
    };
}
