package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.DocInfo;
import com.eiplan.zuji.bean.ReleaseDocInfo;
import com.eiplan.zuji.utils.UIUtils;

import java.util.List;

/**
 * 收藏的文档适配器
 */

public class MeDocAdapter2 extends BaseAdapter {

    private List<DocInfo> docs;
    private Context context;

    public MeDocAdapter2(List<DocInfo> docs, Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_me_doc2, null);
            holder = new ViewHolder();
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_major = convertView.findViewById(R.id.tv_major);
            holder.iv = convertView.findViewById(R.id.iv);
            holder.tv_time = convertView.findViewById(R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DocInfo doc = docs.get(position);

        holder.tv_major.setText(doc.getMajor());
        holder.tv_name.setText(doc.getName());
        holder.tv_time.setText(doc.getUpdatetime());

        holder.iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemRightClickListener != null) {
                    onItemRightClickListener.itemRightClick(v, position);
                }
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_major;
        TextView tv_time;
        ImageView iv;

    }

    private OnItemRightClickListener onItemRightClickListener;

    //实现接口可以点击item内部的控件
    public interface OnItemRightClickListener {
        void itemRightClick(View view, int position);
    }

    public void setOnItemRightClickListener(OnItemRightClickListener onItemRightClickListener) {
        this.onItemRightClickListener = onItemRightClickListener;
    }
}
