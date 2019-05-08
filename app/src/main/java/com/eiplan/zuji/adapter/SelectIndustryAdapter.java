package com.eiplan.zuji.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;


public class SelectIndustryAdapter extends BaseAdapter {

    private Context context;
    private String[] datas;
    private int[] icons;

    public SelectIndustryAdapter(Context context, String[] datas,int[] icons) {
        this.context = context;
        this.datas = datas;
        this.icons = icons;
    }

    @Override
    public int getCount() {
        return datas.length;
    }

    @Override
    public Object getItem(int position) {
        return datas[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context,R.layout.item_select_industry,null);
            holder = new ViewHolder();
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.iv_icon = convertView.findViewById(R.id.iv_icon);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_name.setText(datas[position]);
        holder.iv_icon.setBackgroundResource(icons[position]);
        return convertView;
    }

    static class ViewHolder {
        TextView tv_name;
        ImageView iv_icon;
    }
}
