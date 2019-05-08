package com.eiplan.zuji.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.adapter.ChatMessageAdapter;
import com.eiplan.zuji.bean.ChatMessage;
import com.eiplan.zuji.utils.RobotUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 机器人聊天界面
 */

public class RobotChatActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.iv_send)
    ImageView ivSend;
    @BindView(R.id.et_chat_msg)
    EditText etChatMsg;
    @BindView(R.id.ly_chat_bottom)
    RelativeLayout lyChatBottom;
    @BindView(R.id.lv_chat)
    ListView lvChat;

    //存储聊天消息
    private List<ChatMessage> mDatas;
    private ChatMessageAdapter mAdapter;

    private String pic = "";//用户的头像地址


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            ChatMessage from = (ChatMessage) msg.obj;
            mDatas.add(from);
            mAdapter.notifyDataSetChanged();
            lvChat.setSelection(mDatas.size() - 1);
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_robot_chat);
        ButterKnife.bind(this);

        tvTitle.setText("拓客客服机器人");
        tvRight.setVisibility(View.GONE);
        ivSend.setClickable(false);

        pic = getIntent().getStringExtra("pic");

        etChatMsg.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String msg = etChatMsg.getText().toString().trim();
                if(TextUtils.isEmpty(msg)){
                    ivSend.setClickable(false);
                    ivSend.setBackgroundResource(R.drawable.send_msg);
                }else{
                    ivSend.setClickable(true);
                    ivSend.setBackgroundResource(R.drawable.send_msg1);
                }
            }
        });

        mDatas = new ArrayList<>();
        mDatas.add(new ChatMessage(ChatMessage.Type.INPUT, "我是拓客客服机器人，您要咨询什么问题呢？"));
        mAdapter = new ChatMessageAdapter(this, mDatas,pic);
        lvChat.setAdapter(mAdapter);
    }

    @OnClick(R.id.iv_send)
    public void send() {
        final String msg = etChatMsg.getText().toString();

        ChatMessage to = new ChatMessage(ChatMessage.Type.OUTPUT, msg);
        mDatas.add(to);

        mAdapter.notifyDataSetChanged();
        lvChat.setSelection(mDatas.size() - 1);

        etChatMsg.setText("");

        // 关闭软键盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        // 得到InputMethodManager的实例
        if (imm.isActive()) {
            // 如果开启
            imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT,
                    InputMethodManager.HIDE_NOT_ALWAYS);
            // 关闭软键盘，开启方法相同，这个方法是切换开启与关闭状态的
        }

        new Thread() {
            public void run() {
                ChatMessage from = null;
                try {
                    from = RobotUtils.sendMsg(msg);
                } catch (Exception e) {
                    Log.e("TAG", e.getMessage());
                    from = new ChatMessage(ChatMessage.Type.INPUT, "您的网络好像有问题呢...");
                }
                Message message = Message.obtain();
                message.obj = from;
                mHandler.sendMessage(message);
            }
        }.start();

    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
