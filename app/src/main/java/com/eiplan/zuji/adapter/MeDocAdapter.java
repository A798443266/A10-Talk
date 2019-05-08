package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.ChatMessage;
import com.eiplan.zuji.bean.DocInfo;
import com.eiplan.zuji.bean.ReleaseDocInfo;
import com.eiplan.zuji.utils.UIUtils;

import java.util.List;

/**
 * 文档管理适配器
 */

public class MeDocAdapter extends BaseAdapter {

    private List<ReleaseDocInfo> docs;
    private Context context;

    public MeDocAdapter(List<ReleaseDocInfo> docs, Context context) {
        this.docs = docs;
        this.context = context;
    }


    @Override
    public int getCount() {
        return docs == null ? 0 : docs.size();
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
            convertView = View.inflate(context, R.layout.item_me_doc, null);
            holder = new ViewHolder();
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_major = convertView.findViewById(R.id.tv_major);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.tv_time = convertView.findViewById(R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ReleaseDocInfo doc = docs.get(position);

        if (doc.getType() == 1) {
            holder.tv_type.setText("已发布");
        } else {
            holder.tv_type.setText("审核中");
            holder.tv_type.setTextColor(UIUtils.getColor(R.color.color_zhifu));
        }

        holder.tv_major.setText(doc.getDocInfo().getMajor());
        holder.tv_name.setText(doc.getDocInfo().getName());
        holder.tv_time.setText(doc.getDocInfo().getUpdatetime());


        return convertView;
    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_major;
        TextView tv_type;
        TextView tv_time;

    }

}
