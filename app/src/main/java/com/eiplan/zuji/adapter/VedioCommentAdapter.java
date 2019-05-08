package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.CommentInfo;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class VedioCommentAdapter extends BaseAdapter {

    private Context context;
    private List<CommentInfo> comments;

    public VedioCommentAdapter(Context context, List<CommentInfo> comments) {
        this.context = context;
        this.comments = comments;
    }
    public VedioCommentAdapter(Context context) {
        this.context = context;
        comments = new ArrayList<>();
    }

    public void refresh(List<CommentInfo> comments){
        if(comments != null && comments.size() > 0){
            this.comments.clear();
            this.comments.addAll(comments);
            notifyDataSetChanged();
        }

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
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_comment,null);
            holder = new ViewHolder();
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            holder.tv_pinglun = convertView.findViewById(R.id.tv_pinglun);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_zan = convertView.findViewById(R.id.tv_zan);
            holder.btn_caina = convertView.findViewById(R.id.btn_caina);
            holder.cv_user_pic = convertView.findViewById(R.id.cv_user_pic);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        CommentInfo commnet = comments.get(position);
        holder.btn_caina.setVisibility(View.GONE);
        holder.tv_content.setText(commnet.getContent());
        holder.tv_time.setText(commnet.getTime());
        holder.tv_name.setText(commnet.getUserName());
        holder.tv_pinglun.setText(commnet.getReply() + "");
        holder.tv_zan.setText(commnet.getGood() + "");

        Glide.with(context).load(commnet.getUserPic()).error(R.drawable.logo).into(holder.cv_user_pic);

        return convertView;
    }

    static class ViewHolder {
        TextView tv_content;
        TextView tv_pinglun;
        TextView tv_name;
        TextView tv_time;
        TextView tv_zan;
        Button btn_caina;
        CircleImageView cv_user_pic;
    }
}
