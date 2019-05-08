package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.ContactInfo;
import com.eiplan.zuji.bean.InvationInfo;
import com.eiplan.zuji.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class InviteAdapter extends BaseAdapter {
    private Context mContext;
    private List<InvationInfo> mInvitationInfos = new ArrayList<>();
    private InvationInfo invationInfo;
    private OnInviteListener mOnInviteListener;

    public InviteAdapter(Context context, OnInviteListener onInviteListener) {
        this.mContext = context;
        this.mOnInviteListener = onInviteListener;
    }


    // 刷新数据的方法
    public void refresh(List<InvationInfo> invationInfos) {
        if (invationInfos != null && invationInfos.size() >= 0) {
            mInvitationInfos.clear();
            mInvitationInfos.addAll(invationInfos);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mInvitationInfos == null ? 0 : mInvitationInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return mInvitationInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHodler hodler = null;
        if (hodler == null) {
            hodler = new ViewHodler();
            convertView = View.inflate(mContext, R.layout.item_invite, null);
            hodler.name = (TextView) convertView.findViewById(R.id.tv_invite_name);
            hodler.reason = (TextView) convertView.findViewById(R.id.tv_invite_reason);
            hodler.accept = (Button) convertView.findViewById(R.id.bt_invite_accept);
            hodler.reject = (Button) convertView.findViewById(R.id.bt_invite_reject);
            hodler.cv_user_pic = convertView.findViewById(R.id.cv_user_pic);
            convertView.setTag(hodler);
        } else {
            hodler = (ViewHodler) convertView.getTag();
        }
        // 2 获取当前item数据
        invationInfo = mInvitationInfos.get(position);
        // 3 显示当前item数据
        ContactInfo contact = invationInfo.getContact();
        if (contact != null) {// 联系人
            // 名称的展示
            hodler.name.setText(invationInfo.getContact().getName());
            hodler.accept.setVisibility(View.GONE);
            hodler.reject.setVisibility(View.GONE);
            Glide.with(mContext).load(contact.getPic()).error(R.drawable.logo).into(hodler.cv_user_pic);
            // 原因
            if (invationInfo.getStatus() == InvationInfo.InvitationStatus.NEW_INVITE) {// 新的邀请
                if (invationInfo.getReason() == null) {
                    hodler.reason.setText("请求添加好友");
                } else {
                    hodler.reason.setText(invationInfo.getReason());
                }
                hodler.accept.setVisibility(View.VISIBLE);
                hodler.reject.setVisibility(View.VISIBLE);

            } else if (invationInfo.getStatus() == InvationInfo.InvitationStatus.INVITE_ACCEPT) {// 接受邀请
                if (invationInfo.getReason() == null) {
                    hodler.reason.setText("对方已同意您的好友申请");
                } else {
                    hodler.reason.setText(invationInfo.getReason());
                }
            } else if (invationInfo.getStatus() == InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER) {// 邀请被接受
                if (invationInfo.getReason() == null) {
                    hodler.reason.setText("对方已同意您好友请求");
                } else {
                    hodler.reason.setText(invationInfo.getReason());
                }
            }
            // 按钮的处理
            hodler.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onAccept(invationInfo);
                }
            });

            // 拒绝按钮的点击事件处理
            hodler.reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnInviteListener.onReject(invationInfo);
                }
            });

        } else {//群主邀请
            // 显示名称
            hodler.name.setText(invationInfo.getGroup().getInvatePerson());
            Glide.with(mContext).load(R.drawable.em_create_group).into(hodler.cv_user_pic);
            hodler.accept.setVisibility(View.GONE);//先隐藏按钮，需要显示时再打开
            hodler.reject.setVisibility(View.GONE);

            // 显示原因
            switch(invationInfo.getStatus()){
                // 您的群申请请已经被接受
                case GROUP_APPLICATION_ACCEPTED:
                    hodler.reason.setText("您的群申请已经被同意");
                    break;
                //  您的群邀请已经被接收
                case GROUP_INVITE_ACCEPTED:
                    hodler.reason.setText("对方已经同意了您的群邀请");
//                    hodler.reason.setText("我接受了你的群邀请");
                    break;

                // 你的群申请已经被拒绝
                case GROUP_APPLICATION_DECLINED:
                    hodler.reason.setText("你的群申请已经被拒绝");
                    break;

                // 您的群邀请已经被拒绝
                case GROUP_INVITE_DECLINED:
//                    hodler.reason.setText("您的群邀请已经被拒绝");
                    hodler.reason.setText("对方决绝了您的群邀请");
                    break;

                // 您收到了群邀请
                case NEW_GROUP_INVITE:
                    hodler.accept.setVisibility(View.VISIBLE);
                    hodler.reject.setVisibility(View.VISIBLE);

                    // 接受邀请
                    hodler.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onInviteAccept(invationInfo);
                        }
                    });

                    // 拒绝邀请
                    hodler.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onInviteReject(invationInfo);
                        }
                    });

                    hodler.reason.setText("邀请你加入群：" + invationInfo.getGroup().getGroupName());
                    break;

                // 您收到了群申请
                case NEW_GROUP_APPLICATION:
                    hodler.accept.setVisibility(View.VISIBLE);
                    hodler.reject.setVisibility(View.VISIBLE);

                    // 接受申请
                    hodler.accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onApplicationAccept(invationInfo);
                        }
                    });

                    // 拒绝申请
                    hodler.reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnInviteListener.onApplicationReject(invationInfo);
                        }
                    });

                    hodler.reason.setText("申请加群");
                    break;

                // 你接受了群邀请
                case GROUP_ACCEPT_INVITE:
                    hodler.reason.setText("已接受");
                    break;

                // 您批准了群申请
                case GROUP_ACCEPT_APPLICATION:
                    hodler.reason.setText("已同意");
                    break;

                // 您拒绝了群邀请
                case GROUP_REJECT_INVITE:
                    hodler.reason.setText("已拒绝");
                    break;

                // 您拒绝了群申请
                case GROUP_REJECT_APPLICATION:
                    hodler.reason.setText("已拒绝");
                    break;
            }
        }
        return convertView;
    }

    class ViewHodler {
        private TextView name;
        private TextView reason;
        private CircleImageView cv_user_pic;

        private Button accept;
        private Button reject;
    }

    public interface OnInviteListener {//按钮点击的回调接口

        /**
         * 联系人接受按钮的点击回调
         *
         * @param invationInfo
         */
        void onAccept(InvationInfo invationInfo);

        /**
         * 联系人拒绝按钮的点击事件
         *
         * @param invationInfo
         */
        void onReject(InvationInfo invationInfo);

        // 接受邀请按钮处理（群）
        void onInviteAccept(InvationInfo invationInfo);
        // 拒绝邀请按钮处理（群）
        void onInviteReject(InvationInfo invationInfo);

        // 接受申请按钮处理（群）
        void onApplicationAccept(InvationInfo invationInfo);
        // 拒绝申请按钮处理（群）
        void onApplicationReject(InvationInfo invationInfo);
    }
}
