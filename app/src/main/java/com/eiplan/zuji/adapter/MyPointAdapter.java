package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.PointInfo;

import java.util.List;

/**
 * 我的积分明细适配器
 */

public class MyPointAdapter extends BaseAdapter {

    private List<PointInfo> datas;
    private Context context;

    public MyPointAdapter(List<PointInfo> datas, Context context) {
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
            convertView = View.inflate(context, R.layout.item_mypoint,null);
            holder = new ViewHolder();
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_point = convertView.findViewById(R.id.tv_point);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        PointInfo pointInfo = datas.get(position);

        holder.tv_type.setText(pointInfo.getContent());
        holder.tv_time.setText(pointInfo.getTime());
        holder.tv_point.setText(pointInfo.getPoint());
        return convertView;
    }

    static class ViewHolder {
        TextView tv_type;
        TextView tv_time;
        TextView tv_point;
    }
}
