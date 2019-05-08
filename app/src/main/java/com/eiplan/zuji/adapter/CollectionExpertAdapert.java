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

/**
 * 收藏专家的适配器
 */

public class CollectionExpertAdapert extends BaseAdapter {

    private List<ExpertInfo> experts;
    private Context context;

    public CollectionExpertAdapert(List<ExpertInfo> experts, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_collection_expert, null);
            holder.cl_user_pic = convertView.findViewById(R.id.cl_user_pic);
            holder.tv_username = convertView.findViewById(R.id.tv_username);
            holder.tv_job = convertView.findViewById(R.id.tv_job);
            holder.tv_major = convertView.findViewById(R.id.tv_major);
            holder.tv_work_year = convertView.findViewById(R.id.tv_work_year);
            holder.iv_cancel = convertView.findViewById(R.id.iv_cancel);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ExpertInfo expert = experts.get(position);
        holder.tv_username.setText(expert.getName());
        holder.tv_job.setText(expert.getJob());
        holder.tv_major.setText(expert.getMajor());
        holder.tv_work_year.setText(expert.getWorkyear());
        Glide.with(context).load(expert.getPic()).error(R.drawable.p).into(holder.cl_user_pic);

        holder.iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onCancelClickListener != null)
                    onCancelClickListener.cancelClick(v, position);
            }
        });

        return convertView;
    }

    class ViewHolder {
        CircleImageView cl_user_pic;
        TextView tv_username;
        TextView tv_job;
        TextView tv_major;
        TextView tv_work_year;
        ImageView iv_cancel;
    }


    private OnCancelClickListener onCancelClickListener;


    //实现接口可以点击item内部的控件
    public interface OnCancelClickListener {
        void cancelClick(View view, int position);
    }

    public void setOnCancelClickListener(OnCancelClickListener onCancelClickListener) {
        this.onCancelClickListener = onCancelClickListener;
    }
}
