package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.ExpertInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeExpertAdapter extends BaseAdapter {

    private List<ExpertInfo> experts;
    private Context context;

    public HomeExpertAdapter(List<ExpertInfo> experts, Context context) {
        this.experts = experts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return experts == null ? 0 : experts.size();
    }

    @Override
    public Object getItem(int position) {
        return experts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.home_expert, null);
            holder = new ViewHolder();
            holder.cl_user_pic = convertView.findViewById(R.id.cl_user_pic);
            holder.tv_username = convertView.findViewById(R.id.tv_username);
            holder.tv_job = convertView.findViewById(R.id.tv_job);
            holder.tv_major = convertView.findViewById(R.id.tv_major);
            holder.tv_work_year = convertView.findViewById(R.id.tv_work_year);
            holder.tv_look = convertView.findViewById(R.id.tv_look);
            holder.iv_you = convertView.findViewById(R.id.iv_you);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ExpertInfo expertInfo = experts.get(position);

        holder.tv_username.setText(expertInfo.getName());
        holder.tv_job.setText(expertInfo.getJob());
        holder.tv_major.setText(expertInfo.getMajor());
        holder.tv_work_year.setText(expertInfo.getWorkyear());
        holder.tv_look.setText(expertInfo.getLook() + "");
        if(position == 0 || position == 1 || position == 2){
            holder.iv_you.setVisibility(View.VISIBLE);
        }else{
            holder.iv_you.setVisibility(View.GONE);
        }
        Glide.with(context).load(expertInfo.getPic()).error(R.drawable.p).into(holder.cl_user_pic);
        return convertView;
    }

    static class ViewHolder {
        CircleImageView cl_user_pic;
        TextView tv_username;
        TextView tv_job;
        TextView tv_major;
        TextView tv_work_year;
        TextView tv_look;
        ImageView iv_you;
    }
}
