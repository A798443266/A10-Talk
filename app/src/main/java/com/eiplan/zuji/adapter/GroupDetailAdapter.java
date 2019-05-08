package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.ContactInfo;
import com.eiplan.zuji.bean.UserInfo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GroupDetailAdapter extends BaseAdapter {
    private Context mContext;
    private boolean mIsCanModify;// 是否允许添加和删除群成员
    private List<ContactInfo> mUsers = new ArrayList<>();
    private boolean mIsDeleteModel;// 删除模式  true：表示可以删除； false:表示不可以删除
    private OnGroupDetailListener mOnGroupDetailListener;

    public GroupDetailAdapter(Context context, boolean isCanModify, OnGroupDetailListener onGroupDetailListener) {
        mContext = context;
        mIsCanModify = isCanModify;

        mOnGroupDetailListener = onGroupDetailListener;
    }

    // 获取当前的删除模式
    public boolean ismIsDeleteModel() {
        return mIsDeleteModel;
    }

    // 设置当前的删除模式
    public void setmIsDeleteModel(boolean mIsDeleteModel) {
        this.mIsDeleteModel = mIsDeleteModel;
    }

    // 刷新数据
    public void refresh(List<ContactInfo> users) {

        if (users != null && users.size() >= 0) {
            mUsers.clear();
            // 添加加号和减号
            initUsers();
            mUsers.addAll(0, users);
        }

        notifyDataSetChanged();
    }

    private void initUsers() {
        ContactInfo add = new ContactInfo();
        add.setName("邀请");
        ContactInfo delete = new ContactInfo();
        delete.setName("删除");

        //保证加号一定在减号前面
        mUsers.add(delete);
        mUsers.add(0, add);
    }

    @Override
    public int getCount() {
        return mUsers == null ? 0 : mUsers.size();
    }

    @Override
    public Object getItem(int position) {
        return mUsers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 获取或创建viewholder
        ViewHolder holder = null;
        if(convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(mContext, R.layout.item_groupdetail,null);
            holder.cv_user_pic = convertView.findViewById(R.id.cv_user_pic);
            holder.delete = (ImageView) convertView.findViewById(R.id.iv_delete);
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);

            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }
        // 获取当前item数据
        final ContactInfo userInfo = mUsers.get(position);
        // 显示数据
        if(mIsCanModify) {// 群主或开放了群权限
            // 布局的处理
            if(position == getCount() - 1) {// 减号处理
                // 删除模式判断
                if(mIsDeleteModel) {
                    convertView.setVisibility(View.INVISIBLE);
                }else {
                    convertView.setVisibility(View.VISIBLE);
                    holder.cv_user_pic.setImageResource(R.drawable.em_smiley_minus_btn_pressed);
                    holder.delete.setVisibility(View.GONE);
                    holder.tv_name.setVisibility(View.INVISIBLE);
                }
            }else if(position == getCount() - 2) {// 加号的处理
                // 删除模式判断
                if(mIsDeleteModel) {
                    convertView.setVisibility(View.INVISIBLE);
                }else {
                    convertView.setVisibility(View.VISIBLE);
                    holder.cv_user_pic.setImageResource(R.drawable.em_smiley_add_btn_pressed);
                    holder.delete.setVisibility(View.GONE);
                    holder.tv_name.setVisibility(View.INVISIBLE);
                }
            }else {// 群成员
                convertView.setVisibility(View.VISIBLE);
                holder.tv_name.setVisibility(View.VISIBLE);
                holder.tv_name.setText(userInfo.getName());
//                holder.cv_user_pic.setImageResource(R.drawable.em_default_avatar);
                Glide.with(mContext).load(userInfo.getPic()).error(R.drawable.em_default_avatar).into(holder.cv_user_pic);
                if(mIsDeleteModel) {
                    holder.delete.setVisibility(View.VISIBLE);
                }else {
                    holder.delete.setVisibility(View.GONE);
                }
            }

            // 点击事件的处理
            if(position == getCount() - 1) {// 减号
                holder.cv_user_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!mIsDeleteModel) {
                            mIsDeleteModel = true;
                            notifyDataSetChanged();//刷新到删除模式
                        }
                    }
                });
            }else if(position == getCount() - 2) {// 加号的位置
                holder.cv_user_pic.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnGroupDetailListener.onAddMembers();
                    }
                });
            }else {
                holder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mOnGroupDetailListener.onDeleteMember(userInfo);
                    }
                });
            }
        }else {// 普通的群成员（没有开放群邀请权限）
            if(position == getCount() - 1 || position == getCount() - 2) {
                convertView.setVisibility(View.GONE);
            }else {
                convertView.setVisibility(View.VISIBLE);
                // 名称
                holder.tv_name.setText(userInfo.getName());
                // 头像
//                holder.cv_user_pic.setImageResource(R.drawable.em_default_avatar);
                Glide.with(mContext).load(userInfo.getPic()).error(R.drawable.em_default_avatar).into(holder.cv_user_pic);
                // 删除
                holder.delete.setVisibility(View.GONE);
            }
        }

        // 返回view
        return convertView;
    }

    private class  ViewHolder{
        private ImageView delete;
        private TextView tv_name;
        private CircleImageView cv_user_pic;
    }

    public interface OnGroupDetailListener{
        // 添加群成员方法
        void onAddMembers();
        // 删除群成员方法
        void onDeleteMember(ContactInfo user);
    }
}
