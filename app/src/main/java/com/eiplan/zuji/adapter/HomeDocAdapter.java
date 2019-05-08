package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.DocInfo;

import java.util.List;

public class HomeDocAdapter extends BaseAdapter {

    private List<DocInfo> docs;
    private Context context;

    public HomeDocAdapter(List<DocInfo> docs, Context context) {
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
        if(convertView == null){
            convertView = View.inflate(context, R.layout.home_doc,null);
            holder = new ViewHolder();
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_major = convertView.findViewById(R.id.tv_major);
            holder.tv_buy = convertView.findViewById(R.id.tv_buy);
            holder.tv_updatetime = convertView.findViewById(R.id.tv_updatetime);
            holder.tv_price = convertView.findViewById(R.id.tv_price);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        DocInfo doc = docs.get(position);

        holder.tv_name.setText(docs.get(position).getName());
        holder.tv_major.setText(docs.get(position).getMajor());
        holder.tv_buy.setText(docs.get(position).getBuy() + "");
        holder.tv_updatetime.setText(docs.get(position).getUpdatetime());
        holder.tv_price.setText(docs.get(position).getPrice()+"");
        return convertView;
    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_major;
        TextView tv_buy;
        TextView tv_updatetime;
        TextView tv_price;
    }
}
