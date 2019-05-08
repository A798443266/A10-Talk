package com.eiplan.zuji.adapter;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.ChatMessage;

import java.io.File;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 机器人聊天消息适配器
 */
public class ChatMessageAdapter extends BaseAdapter {
    private LayoutInflater mInflater;
    private List<ChatMessage> mDatas;
    private Context context;
    private String pic;


    public ChatMessageAdapter(Context context, List<ChatMessage> datas, String pic) {
        mInflater = LayoutInflater.from(context);
        mDatas = datas;
        this.pic = pic;
        this.context = context;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 接受到消息为1，发送消息为0
     */
    @Override
    public int getItemViewType(int position) {
        ChatMessage msg = mDatas.get(position);
        return msg.getType() == ChatMessage.Type.INPUT ? 1 : 0;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessage = mDatas.get(position);

        ViewHolder viewHolder = null;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (chatMessage.getType() == ChatMessage.Type.INPUT) {
                convertView = mInflater.inflate(R.layout.left, parent, false);
                viewHolder.createDate = (TextView) convertView.findViewById(R.id.chat_from_createDate);
                viewHolder.content = (TextView) convertView.findViewById(R.id.chat_from_content);
                convertView.setTag(viewHolder);
            } else {
                convertView = mInflater.inflate(R.layout.right, null);
                viewHolder.createDate = (TextView) convertView.findViewById(R.id.chat_from_createDate);
                viewHolder.content = (TextView) convertView.findViewById(R.id.chat_from_content);
                viewHolder.cl_pic = convertView.findViewById(R.id.cl_pic);
                convertView.setTag(viewHolder);
            }

        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.content.setText(chatMessage.getMsg());
        viewHolder.createDate.setText(chatMessage.getDateStr());
        if (chatMessage.getType() == ChatMessage.Type.OUTPUT) {

            Glide.with(context).load(pic).error(R.drawable.p).into(viewHolder.cl_pic);

        }

        return convertView;
    }

    private class ViewHolder {
        public TextView createDate;
        public TextView content;
        CircleImageView cl_pic;
    }

}
