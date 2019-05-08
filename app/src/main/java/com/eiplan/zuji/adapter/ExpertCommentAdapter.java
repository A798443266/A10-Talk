package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.ExpertComment;
import com.eiplan.zuji.bean.VideoInfo;
import com.wx.goodview.GoodView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExpertCommentAdapter extends BaseAdapter {
    private Context context;
    private List<ExpertComment> comments;

    public ExpertCommentAdapter(Context context, List<ExpertComment> comments) {
        this.context = context;
        this.comments = comments;
    }

    @Override
    public int getCount() {
        return comments == null ? 0 : comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_expert_comment, null);
            holder = new ViewHolder();
            holder.tv_username = convertView.findViewById(R.id.tv_username);
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.cv_user_pic = convertView.findViewById(R.id.cv_user_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ExpertComment comment = comments.get(position);
        holder.tv_username.setText(comment.getUserName());
        holder.tv_time.setText(comment.getTime());
        holder.tv_content.setText(comment.getContent());
        holder.tv_time.setText(comment.getTime());

        Glide.with(context).load(comment.getUserPic()).error(R.drawable.logo).into(holder.cv_user_pic);

        return convertView;
    }

    static class ViewHolder {
        TextView tv_username;
        TextView tv_time;
        TextView tv_content;
        CircleImageView cv_user_pic;
    }
}
