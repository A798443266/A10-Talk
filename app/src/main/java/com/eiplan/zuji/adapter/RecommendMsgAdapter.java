package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.RecommendMSG;

import java.util.List;

public class RecommendMsgAdapter extends BaseAdapter {
    private List<RecommendMSG> datas;
    private Context context;

    public RecommendMsgAdapter(List<RecommendMSG> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_recommend_msg,null);
            holder = new ViewHolder();
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_desc = convertView.findViewById(R.id.tv_desc);
            holder.iv_face = convertView.findViewById(R.id.iv_face);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        RecommendMSG msg = datas.get(position);

        holder.tv_title.setText(msg.getTitle());
        holder.tv_desc.setText(msg.getIntroduce());
        Glide.with(context).load(msg.getPic()).error(R.drawable.p).into(holder.iv_face);
        return convertView;
    }

    static class ViewHolder {
        TextView tv_title;
        TextView tv_desc;
        ImageView iv_face;
    }
}
