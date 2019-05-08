package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.MyExpertInfo;
import com.eiplan.zuji.utils.UIUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


//预约专家的适配器
public class MeExpert1Adapter extends BaseAdapter {

    private List<MyExpertInfo> datas;
    private Context context;
    private int type = 0;

    public MeExpert1Adapter(Context context, List<MyExpertInfo> datas) {
        this.context = context;
        this.datas = datas;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
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
            convertView = View.inflate(context, R.layout.item_me_expert, null);
            holder.cl_user_pic = convertView.findViewById(R.id.cl_user_pic);
            holder.tv_username = convertView.findViewById(R.id.tv_username);
            holder.tv_job = convertView.findViewById(R.id.tv_job);
            holder.tv_major = convertView.findViewById(R.id.tv_major);
            holder.tv_work_year = convertView.findViewById(R.id.tv_work_year);
            holder.tv_zhifu = convertView.findViewById(R.id.tv_zhifu);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyExpertInfo myExpertInfo = datas.get(position);
        holder.tv_username.setText(myExpertInfo.getName());
        holder.tv_job.setText(myExpertInfo.getJob());
        holder.tv_major.setText(myExpertInfo.getMajor());
        holder.tv_work_year.setText(myExpertInfo.getWorkyear());
        holder.tv_zhifu.setText(myExpertInfo.getState());
        holder.tv_time.setText(myExpertInfo.getTime());
        Glide.with(context).load(myExpertInfo.getPic()).error(R.drawable.p).into(holder.cl_user_pic);

        if(type == 1){
            holder.tv_zhifu.setTextColor(UIUtils.getColor(R.color.color_system_blank));
            holder.tv_zhifu.setBackgroundColor(UIUtils.getColor(R.color.color_white));
        }
        holder.tv_zhifu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemRightClickListener.itemRightClick(v,position);
            }
        });

        return convertView;
    }

    public void setType(int type) {
        this.type = type;
    }

    class ViewHolder {
        CircleImageView cl_user_pic;
        TextView tv_username;
        TextView tv_job;
        TextView tv_major;
        TextView tv_work_year;
        TextView tv_zhifu;
        TextView tv_time;
    }

    private OnItemRightClickListener onItemRightClickListener;


    //实现接口可以点击item内部的控件
    public interface OnItemRightClickListener {
        void itemRightClick(View view,int position);
    }

    public void setOnItemRightClickListener(OnItemRightClickListener onItemRightClickListener) {
        this.onItemRightClickListener = onItemRightClickListener;
    }
}
