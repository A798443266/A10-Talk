package com.eiplan.zuji.model;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.eiplan.zuji.bean.ContactInfo;
import com.eiplan.zuji.bean.GroupInfo;
import com.eiplan.zuji.bean.InvationInfo;
import com.eiplan.zuji.bean.UserInfo;
import com.eiplan.zuji.utils.Constant;
import com.eiplan.zuji.utils.SpUtils;
import com.hyphenate.EMContactListener;
import com.hyphenate.EMGroupChangeListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMucSharedFile;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

// 全局事件监听类
public class EventListener {

    private Context mContext;
    private final LocalBroadcastManager mLBM;
    private String pic="";
    private String name = "";

    public EventListener(Context context) {
        mContext = context;
        // 创建一个发送广播的管理者对象
        mLBM = LocalBroadcastManager.getInstance(mContext);

        // 注册一个联系人变化的监听
        EMClient.getInstance().contactManager().setContactListener(emContactListener);
        // 注册一个群信息变化的监听
        EMClient.getInstance().groupManager().addGroupChangeListener(eMGroupChangeListener);
    }


    //群邀请变化的监听
    private final EMGroupChangeListener eMGroupChangeListener = new EMGroupChangeListener() {
        //收到 群邀请
        @Override
        public void onInvitationReceived(String groupId, String groupName, String inviter, String reason) {
            // 数据更新
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setReason(reason);
            //从数据库中查询出邀请人头像地址

            invitationInfo.setGroup(new GroupInfo(groupName, groupId, inviter,""));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.NEW_GROUP_INVITE);
            //添加群邀请信息到数据库中
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);

            // 红点处理,保存红点的状态到内存中
            SpUtils.putBoolean(mContext,Constant.IS_NEW_INVITE,true);

            // 发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群申请通知
        @Override
        public void onRequestToJoinReceived(String groupId, String groupName, String applicant, String reason) {

            // 数据更新
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroup(new GroupInfo(groupName, groupId, applicant,""));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.NEW_GROUP_APPLICATION);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);

            // 红点处理
            SpUtils.putBoolean(mContext,Constant.IS_NEW_INVITE,true);

            // 发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onRequestToJoinAccepted(String s, String s1, String s2) {

        }

        @Override
        public void onRequestToJoinDeclined(String s, String s1, String s2, String s3) {

        }

        //收到 群邀请被同意
        @Override
        public void onInvitationAccepted(String groupId, String inviter, String reason) {
            // 更新数据
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroup(new GroupInfo(groupId, groupId, inviter,""));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            // 红点处理
            SpUtils.putBoolean(mContext,Constant.IS_NEW_INVITE,true);
            // 发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群邀请被拒绝
        @Override
        public void onInvitationDeclined(String groupId, String inviter, String reason) {
            // 更新数据
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setReason(reason);
            invitationInfo.setGroup(new GroupInfo(groupId, groupId, inviter,""));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_DECLINED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            // 红点处理
            SpUtils.putBoolean(mContext,Constant.IS_NEW_INVITE,true);
            // 发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        //收到 群成员被删除
        @Override
        public void onUserRemoved(String s, String s1) {

        }

        //收到 群被解散
        @Override
        public void onGroupDestroyed(String s, String s1) {

        }

        //收到 群邀请被自动接受
        @Override
        public void onAutoAcceptInvitationFromGroup(String groupId, String inviter, String inviteMessage) {
            // 更新数据
            InvationInfo invitationInfo = new InvationInfo();
            invitationInfo.setReason(inviteMessage);
            invitationInfo.setGroup(new GroupInfo(groupId, groupId, inviter,""));
            invitationInfo.setStatus(InvationInfo.InvitationStatus.GROUP_INVITE_ACCEPTED);
            Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
            // 红点处理
            SpUtils.putBoolean(mContext,Constant.IS_NEW_INVITE,true);
            // 发送广播
            mLBM.sendBroadcast(new Intent(Constant.GROUP_INVITE_CHANGED));
        }

        @Override
        public void onMuteListAdded(String s, List<String> list, long l) {

        }

        @Override
        public void onMuteListRemoved(String s, List<String> list) {

        }

        @Override
        public void onAdminAdded(String s, String s1) {

        }

        @Override
        public void onAdminRemoved(String s, String s1) {

        }

        @Override
        public void onOwnerChanged(String s, String s1, String s2) {

        }

        @Override
        public void onMemberJoined(String s, String s1) {

        }

        @Override
        public void onMemberExited(String s, String s1) {

        }

        @Override
        public void onAnnouncementChanged(String s, String s1) {

        }

        @Override
        public void onSharedFileAdded(String s, EMMucSharedFile emMucSharedFile) {

        }

        @Override
        public void onSharedFileDeleted(String s, String s1) {

        }
    };

    // 注册一个联系人变化的监听
    private final EMContactListener emContactListener = new EMContactListener() {
        // 联系人增加后执行的方法
        @Override
        public void onContactAdded(final String hxid) {

            final ContactInfo contact = new ContactInfo();
            OkHttpUtils.post().url(Constant.LOGIN).addParams("phone",hxid).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    contact.setPhone(hxid);
                    // 数据库更新
                    InvationInfo invitationInfo = new InvationInfo();
                    invitationInfo.setContact(contact);
                    // 数据更新
                    Model.getInstance().getDbManager().getContactTableDao().saveContact(contact, true);
                    // 发送联系人变化的广播
                    mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
                }

                @Override
                public void onResponse(String response, int id) {
                    //解析json
                    JSONObject jsonObject1 = JSON.parseObject(response).getJSONObject("extend").getJSONObject("user");

                    contact.setName(jsonObject1.getString("name"));
                    contact.setPic(jsonObject1.getString("pic"));
                    contact.setPhone(hxid);
                    // 数据库更新
                    Model.getInstance().getDbManager().getContactTableDao().saveContact(contact, true);
                    // 发送邀请信息变化的广播
                    mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
                }
            });
        }

        // 联系人删除后执行的方法
        @Override
        public void onContactDeleted(String hxid) {
            // 数据更新
            Model.getInstance().getDbManager().getContactTableDao().deleteContactByHxId(hxid);
            Model.getInstance().getDbManager().getInviteTableDao().removeInvitation(hxid);
            // 发送联系人变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_CHANGED));
        }

        // 接受到联系人的新邀请
        @Override
        public void onContactInvited(final String hxid, final String reason) {

            final ContactInfo contact = new ContactInfo();
            OkHttpUtils.post().url(Constant.LOGIN).addParams("phone",hxid).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    contact.setPhone(hxid);
                    // 数据库更新
                    InvationInfo invitationInfo = new InvationInfo();
                    invitationInfo.setContact(contact);
                    invitationInfo.setStatus(InvationInfo.InvitationStatus.NEW_INVITE);//
                    Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
                    // 红点的处理
                    SpUtils.putBoolean(mContext,Constant.IS_NEW_INVITE,true);
                    // 发送邀请信息变化的广播
                    mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
                }

                @Override
                public void onResponse(String response, int id) {
                    //解析json
                    JSONObject jsonObject1 = JSON.parseObject(response).getJSONObject("extend").getJSONObject("user");

                    contact.setName(jsonObject1.getString("name"));
                    contact.setPic(jsonObject1.getString("pic"));
                    contact.setPhone(hxid);
                    // 数据库更新
                    InvationInfo invitationInfo = new InvationInfo();
                    invitationInfo.setContact(contact);
                    invitationInfo.setReason(reason);
                    invitationInfo.setStatus(InvationInfo.InvitationStatus.NEW_INVITE);// 新邀请
                    Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
                    // 红点的处理
                    SpUtils.putBoolean(mContext,Constant.IS_NEW_INVITE,true);
                    // 发送邀请信息变化的广播
                    mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
                }
            });
        }

        // 别人同意了你的好友邀请
        @Override
        public void onFriendRequestAccepted(final String hxid) {
            final ContactInfo contact = new ContactInfo();
            OkHttpUtils.post().url(Constant.LOGIN).addParams("phone",hxid).build().execute(new StringCallback() {
                @Override
                public void onError(Call call, Exception e, int id) {
                    contact.setPhone(hxid);
                    // 数据库更新
                    InvationInfo invitationInfo = new InvationInfo();
                    invitationInfo.setContact(contact);
                    invitationInfo.setStatus(InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);// 别人同意了你的邀请
                    Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
                    // 红点的处理
                    SpUtils.putBoolean(mContext,Constant.IS_NEW_INVITE,true);
                    // 发送邀请信息变化的广播
                    mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
                }

                @Override
                public void onResponse(String response, int id) {
                    //解析json
                    JSONObject jsonObject1 = JSON.parseObject(response).getJSONObject("extend").getJSONObject("user");

                    contact.setName(jsonObject1.getString("name"));
                    contact.setPic(jsonObject1.getString("pic"));
                    contact.setPhone(hxid);
                    // 数据库更新
                    InvationInfo invitationInfo = new InvationInfo();
                    invitationInfo.setContact(contact);
                    invitationInfo.setStatus(InvationInfo.InvitationStatus.INVITE_ACCEPT_BY_PEER);// 别人同意了你的邀请
                    Model.getInstance().getDbManager().getInviteTableDao().addInvitation(invitationInfo);
                    // 红点的处理
                    SpUtils.putBoolean(mContext,Constant.IS_NEW_INVITE,true);
                    // 发送邀请信息变化的广播
                    mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
                }
            });

        }

        // 别人拒绝了你好友邀请
        @Override
        public void onFriendRequestDeclined(String s) {
            // 红点的处理
            SpUtils.putBoolean(mContext,Constant.IS_NEW_INVITE,true);
            // 发送邀请信息变化的广播
            mLBM.sendBroadcast(new Intent(Constant.CONTACT_INVITE_CHANGED));
        }
    };

}
