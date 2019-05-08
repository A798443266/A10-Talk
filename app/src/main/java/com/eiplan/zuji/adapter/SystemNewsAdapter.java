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
import com.eiplan.zuji.bean.SystemNotificationInfo;

import java.util.List;

public class SystemNewsAdapter extends BaseAdapter {
    private List<SystemNotificationInfo> datas;
    private Context context;

    public SystemNewsAdapter(List<SystemNotificationInfo> datas, Context context) {
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
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_system_news, null);
            holder = new ViewHolder();
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            holder.tv_type = convertView.findViewById(R.id.tv_type);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SystemNotificationInfo msg = datas.get(position);

        holder.tv_time.setText(msg.getTime());
        holder.tv_content.setText(msg.getContent());
        holder.tv_type.setText(msg.getType());
        return convertView;
    }

    static class ViewHolder {
        TextView tv_time;
        TextView tv_type;
        TextView tv_content;
    }
}
