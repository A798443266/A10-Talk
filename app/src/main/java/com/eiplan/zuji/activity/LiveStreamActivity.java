package com.eiplan.zuji.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.LiveRoom;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.NetTypeUtils;
import com.eiplan.zuji.utils.SpUtils;
import com.eiplan.zuji.view.RoomMessagesView;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCmdMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.netease.LSMediaCapture.lsLogUtil;
import com.netease.LSMediaCapture.lsMediaCapture;
import com.netease.LSMediaCapture.lsMessageHandler;
import com.netease.vcloud.video.render.NeteaseView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 主播直播界面
 */

public class LiveStreamActivity extends AppCompatActivity {

    @BindView(R.id.neteaseview)
    NeteaseView neteaseview;
    @BindView(R.id.cv_user_pic)
    CircleImageView cvUserPic;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_look)
    TextView tvLook;
    @BindView(R.id.tv_second)
    TextView tvSecond;
    @BindView(R.id.btn_live)
    Button btnLive;
    @BindView(R.id.message_view)
    RoomMessagesView messageView;
    @BindView(R.id.comment_image)
    ImageView commentImage;
    @BindView(R.id.tv_room_id)
    TextView tvRoomId;

    private int second = 3;

    private String chatRoomId = "76257659256833";//聊天室id
    private EMChatRoom chatroom;//聊天室
    private int watchedCount = 0;//观看人数
    //    protected int membersCount;
//    LinkedList<String> memberList = new LinkedList<>();//聊天室当前的人员
    private static final int MAX_SIZE = 10;//观看人员列表最多显示10个
    private LinearLayoutManager layoutManager = new LinearLayoutManager(this);
    protected boolean isMessageListInited;


    private lsMediaCapture.LsMediaCapturePara lsMediaCapturePara;
    private lsMediaCapture mLsMediaCapture;
    private lsMediaCapture.LiveStreamingPara mLiveStreamingPara;
    private boolean m_liveStreamingInitFinished;//是否初始化推流成功
    private boolean m_startVideoCamera;//是否已经开启摄像头
    private boolean m_liveStreamingOn;//是否已经开启推流
    //推流地址
    private String mliveStreamingURL;

    private LiveRoom room;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            tvSecond.setVisibility(View.VISIBLE);
            tvSecond.setText(second-- + "");
            if (!tvSecond.getText().equals("0")) {
                handler.sendEmptyMessageDelayed(1, 1000);
            } else {
                tvSecond.setVisibility(View.GONE);
                startLive();//开启直播
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_stream);
        ButterKnife.bind(this);

        //隐藏顶部的状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        room = (LiveRoom) getIntent().getSerializableExtra("live");
        if (room != null) {
            Glide.with(this).load(room.getPic()).into(cvUserPic);
            tvName.setText(room.getName());
            tvLook.setText(room.getSee() + "");
            tvRoomId.setText(room.getId() + "");
        }
//        mliveStreamingURL = room.getStreamingurl();
        mliveStreamingURL = "rtmp://p021d26f3.live.126.net/live/6a6747863c174e778aa7b1ba882ca825?wsSecret=cd50d5f29215fa050fb6b339341f5b68&wsTime=1548313358";
        initData();
    }

    //设置直播参数
    private void initData() {
        //1、创建直播实例
        lsMediaCapturePara = new lsMediaCapture.LsMediaCapturePara();
        lsMediaCapturePara.setContext(this);//设置SDK上下文（建议使用ApplicationContext）
        lsMediaCapturePara.setMessageHandler(new lsMessageHandler() {//设置SDK消息回调

            @Override
            public void handleMessage(int i, Object o) {

            }
        });
        lsMediaCapturePara.setLogLevel(lsLogUtil.LogLevel.INFO);//日志级别
        lsMediaCapturePara.setUploadLog(false);//是否上传SDK日志
        mLsMediaCapture = new lsMediaCapture(lsMediaCapturePara);

        //2、设置直播参数
        mLiveStreamingPara = new lsMediaCapture.LiveStreamingPara();
        mLiveStreamingPara.setStreamType(lsMediaCapture.StreamType.AV); // 推流类型 AV、AUDIO、VIDEO
        mLiveStreamingPara.setFormatType(lsMediaCapture.FormatType.RTMP); // 推流格式 RTMP、MP4、RTMP_AND_MP4
        //mLiveStreamingPara.setRecordPath(publishParam.recordPath);//formatType 为 MP4 或 RTMP_AND_MP4 时有效
        mLiveStreamingPara.setQosOn(true);
        //mLiveStreamingPara.setSyncTimestamp(true,false);//（直播答题使用）网易云透传时间戳，不依赖CDN方式，不需要额外开通(必须包含视频流)
        //mLiveStreamingPara.setStreamTimestampPassthrough(true); //（直播答题使用）网易云透传时间戳，但完全透传功能需要联系网易云开通，支持纯音频

        //3、 预览参数设置
        boolean frontCamera = false; // 是否前置摄像头
        boolean mScale_16x9 = true; //是否强制16:9
        if (mLiveStreamingPara.getStreamType() != lsMediaCapture.StreamType.AUDIO) {  //如果不是音频就开启预览
            lsMediaCapture.VideoQuality videoQuality = lsMediaCapture.VideoQuality.SUPER_HIGH; //视频模板（SUPER_HIGH 1280*720、SUPER 960*540、HIGH 640*480、MEDIUM 480*360、LOW 352*288）
            mLsMediaCapture.startVideoPreview(neteaseview, frontCamera, true, videoQuality, mScale_16x9);
        }

        m_startVideoCamera = true;

        //显示打开发弹幕的按钮
        messageView.setShowMessage(new RoomMessagesView.ShowMessage() {
            @Override
            public void onClick() {
                commentImage.setVisibility(View.VISIBLE);
            }
        });

//        handler.sendEmptyMessageDelayed(1, 1000);
    }

    @OnClick({R.id.iv_switch, R.id.iv_close, R.id.btn_live})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_live:
                //判断网络是否可用
                if (NetTypeUtils.getNetType(this) == 0) {
                    Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
                    return;
                } else if (NetTypeUtils.getNetType(this) == 2) {
                    new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("提示")
                            .setContentText("当前使用的是移动网络，开启直播会消耗流量，您确定要开启吗？")
                            .setConfirmText("确定")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    btnLive.setVisibility(View.GONE);
                                    handler.sendEmptyMessageDelayed(1, 1000);
                                    sweetAlertDialog.dismiss();
                                }
                            })
                            .setCancelText("取消")
                            .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            })
                            .show();
                } else {
                    btnLive.setVisibility(View.GONE);
                    handler.sendEmptyMessageDelayed(1, 1000);
                }

                break;

            case R.id.iv_switch:
                if (mLsMediaCapture != null) {
                    //切换摄像头
                    mLsMediaCapture.switchCamera();
                }
                break;

            case R.id.iv_close:
                if (m_liveStreamingOn)
                    stopLive();//如果已经开启推流就显示停止推流的对话框
                else
                    finish(); //如果没有开启推流就退出本页面
                break;
        }
    }

    private void startLive() {
        //6、初始化直播
        m_liveStreamingInitFinished = mLsMediaCapture.initLiveStream(mLiveStreamingPara, mliveStreamingURL);
        if (mLsMediaCapture != null && m_liveStreamingInitFinished) {
            //7、开始直播
            mLsMediaCapture.startLiveStreaming();
            m_liveStreamingOn = true;
        }

        //加入聊天室
        EMClient.getInstance().chatroomManager().joinChatRoom(chatRoomId, new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(EMChatRoom emChatRoom) {
                chatroom = emChatRoom;
                onMessageListInit();
                addChatRoomChangeListener();
                Toast.makeText(LiveStreamActivity.this, "加入聊天室成功！", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "加入聊天室成功");
            }

            @Override
            public void onError(int i, String s) {
                Toast.makeText(LiveStreamActivity.this, "加入聊天室失败", Toast.LENGTH_SHORT).show();
            }
        });
        Toast.makeText(this, "直播已经开启", Toast.LENGTH_SHORT).show();
    }

    private void stopLive() {

        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("提示")
                .setContentText("您确定要关闭当前直播吗？")
                .setConfirmText("确定")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        //停止直播调用相关API接口
                        if (mLsMediaCapture != null && m_liveStreamingOn) {
                            //停止直播，释放资源
                            mLsMediaCapture.stopLiveStreaming();
                            //如果音视频或者单独视频直播，需要关闭视频预览
                            if (m_startVideoCamera) {
                                mLsMediaCapture.stopVideoPreview();
                                //消耗第三方滤镜
                                //releaseSenseEffect();
                                mLsMediaCapture.destroyVideoPreview();
                            }
                            //反初始化推流实例，当它与stopLiveStreaming连续调用时，参数为false
                            mLsMediaCapture.uninitLsMediaCapture(false);
                            mLsMediaCapture = null;
                            m_liveStreamingOn = false;
                            Toast.makeText(LiveStreamActivity.this, "直播已经关闭", Toast.LENGTH_SHORT).show();
                            sweetAlertDialog.dismiss();
                            second = 3;//重置倒计时
                        }
                    }
                })
                .setCancelText("点错了")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                    }
                })
                .show();

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (m_liveStreamingOn)
                stopLive();//如果已经开启推流就显示停止推流的对话框
            else {
                finish(); //如果没有开启推流就退出本页面
            }
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EMClient.getInstance().chatroomManager().leaveChatRoom(SpUtils.getString(this, Constant.current_phone));
        EMClient.getInstance().chatManager().removeMessageListener(msgListener);
        handler.removeCallbacksAndMessages(null);
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
                        Toast.makeText(LiveStreamActivity.this, "你已被移除出此房间", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        //                        showChatroomToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
//                        onRoomMemberExited(participant);
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
//        if (!memberList.contains(name)) {//若果当前聊天室人员不存在，则添加
//            membersCount++;
//            if (memberList.size() >= MAX_SIZE)
//                memberList.removeLast();
//            memberList.addFirst(name);
//            showMemberChangeEvent(name, "来了");
//            Log.e("TAG", name + "added");
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    tvLook.setText(String.valueOf(membersCount));
//                    //刷新列表
////                    notifyDataSetChanged();
//                }
//            });
//        }
        if (name.equals("13627807142"))
            showMemberChangeEvent(name, "进入直播间");
        Log.e("TAG", name + "added");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLook.setText(String.valueOf(watchedCount));
            }
        });

    }

    private void notifyDataSetChanged() {
//        if (memberList.size() > 4) {
//            layoutManager.setStackFromEnd(false);
//        } else {
//            layoutManager.setStackFromEnd(true);
//        }
//        horizontalRecyclerView.getAdapter().notifyDataSetChanged();
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
        watchedCount--;
        Log.e("TAG", name + "exited");
        showMemberChangeEvent(name, "离开");
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvLook.setText(String.valueOf(watchedCount));
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
                                Toast.makeText(LiveStreamActivity.this, "消息发送失败！", Toast.LENGTH_SHORT).show();
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
            }
        });

        //注册消息监听
        EMClient.getInstance().chatManager().addMessageListener(msgListener);
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

    @OnClick(R.id.comment_image)
    public void onViewClicked() {
        messageView.setShowInputView(true);
        commentImage.setVisibility(View.GONE);

    }

}
