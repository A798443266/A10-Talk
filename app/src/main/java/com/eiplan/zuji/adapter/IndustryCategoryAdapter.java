package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.eiplan.zuji.R;

import java.util.List;

/**
 * 专家页的工业分类的适配器
 */

public class IndustryCategoryAdapter extends BaseAdapter {

    private List<String> datas;
    private Context context;

    public IndustryCategoryAdapter(List<String> datas, Context context) {
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
            convertView = View.inflate(context,R.layout.expert_industry_category,null);
            holder = new ViewHolder();
            holder.tv_industry_name = convertView.findViewById(R.id.tv_industry_name);
            holder.ll_bg = convertView.findViewById(R.id.ll_bg);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_industry_name.setText(datas.get(position));
        if(clickTemp == position ){
            holder.ll_bg.setBackgroundResource(R.drawable.industry_category_bg1);
        }else{
            holder.ll_bg.setBackgroundResource(R.drawable.industry_category_bg);
        }

        return convertView;
    }

    private int clickTemp = -1;

    public void setSelection(int position) {
        clickTemp = position;
    }

    static class ViewHolder {
        TextView tv_industry_name;
        LinearLayout ll_bg;
    }
}
