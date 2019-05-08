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

public class RankItemAdapter extends BaseAdapter {
    private List<ExpertInfo> experts;
    private Context context;
    private int leixin;

    public RankItemAdapter(List<ExpertInfo> experts, Context context,int leixin) {
        this.experts = experts;
        this.context = context;
        this.leixin = leixin;
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
        if( convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context,R.layout.item_rank,null);
            holder.cv_user_pic = convertView.findViewById(R.id.cv_user_pic);
            holder.iv_jiangpai = convertView.findViewById(R.id.iv_jiangpai);
            holder.tv_rank = convertView.findViewById(R.id.tv_rank);
            holder.tv_job = convertView.findViewById(R.id.tv_job);
            holder.tv_major = convertView.findViewById(R.id.tv_major);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_leixing = convertView.findViewById(R.id.tv_leixing);
            holder.tv_point = convertView.findViewById(R.id.tv_point);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        ExpertInfo expert = experts.get(position);
        if(position == 0){
            holder.iv_jiangpai.setVisibility(View.VISIBLE);
            holder.iv_jiangpai.setBackgroundResource(R.drawable.icon_rank1);
            holder.tv_rank.setVisibility(View.GONE);
        }else if(position == 1){
            holder.iv_jiangpai.setVisibility(View.VISIBLE);
            holder.iv_jiangpai.setBackgroundResource(R.drawable.icon_rank2);
            holder.tv_rank.setVisibility(View.GONE);
        }else if(position == 2){
            holder.iv_jiangpai.setVisibility(View.VISIBLE);
            holder.iv_jiangpai.setBackgroundResource(R.drawable.icon_rank3);
            holder.tv_rank.setVisibility(View.GONE);
        }else {
            holder.iv_jiangpai.setVisibility(View.GONE);
            holder.tv_rank.setVisibility(View.VISIBLE);
        }

        Glide.with(context).load(expert.getPic()).error(R.drawable.p).into(holder.cv_user_pic);
        holder.tv_rank.setText((position + 1) + "");
        holder.tv_job.setText(expert.getJob());
        holder.tv_major.setText(expert.getMajor());
        holder.tv_name.setText(expert.getName());
        if(leixin == 1){
            holder.tv_leixing.setText("服务评分");
            holder.tv_point.setText(expert.getEvaluate() + "");
        }else{
            holder.tv_leixing.setText("浏览次数");
            holder.tv_point.setText(expert.getLook() + "");
        }

        return convertView;
    }

    static class ViewHolder {
        CircleImageView cv_user_pic;
        ImageView iv_jiangpai;
        TextView tv_rank;
        TextView tv_job;
        TextView tv_major;
        TextView tv_name;
        TextView tv_leixing;
        TextView tv_point;
    }
}
