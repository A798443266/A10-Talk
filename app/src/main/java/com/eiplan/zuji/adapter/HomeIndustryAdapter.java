package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eiplan.zuji.R;

import java.util.List;

/**
 * 首页工业分类适配器
 */

public class HomeIndustryAdapter extends BaseAdapter {

    private List<String> datas;
    private Context context;

    public HomeIndustryAdapter(List<String> datas, Context context) {
        this.datas = datas;
        this.context = context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context,R.layout.home_industry,null);
            holder = new ViewHolder();
            holder.tv_industry_name = convertView.findViewById(R.id.tv_industry_name);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_industry_name.setText(datas.get(position));
        return convertView;
    }

    static class ViewHolder {
        TextView tv_industry_name;
    }
}
