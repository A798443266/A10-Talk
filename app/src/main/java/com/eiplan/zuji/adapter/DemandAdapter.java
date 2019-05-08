package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.DemandInfo;
import com.eiplan.zuji.bean.DocInfo;

import java.util.List;

public class DemandAdapter extends BaseAdapter {

    private List<DemandInfo> demands;
    private Context context;

    public DemandAdapter(List<DemandInfo> demands, Context context) {
        this.demands = demands;
        this.context = context;
    }

    @Override
    public int getCount() {
        return demands == null ? 0 : demands.size();
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
            convertView = View.inflate(context, R.layout.item_demand, null);
            holder = new ViewHolder();
            holder.tv_expert = convertView.findViewById(R.id.tv_expert);
            holder.tv_city = convertView.findViewById(R.id.tv_city);
            holder.tv_budget = convertView.findViewById(R.id.tv_budget);
            holder.tv_type = convertView.findViewById(R.id.tv_type);
            holder.tv_desc = convertView.findViewById(R.id.tv_desc);
            holder.tv_look = convertView.findViewById(R.id.tv_look);
            holder.tv_time = convertView.findViewById(R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        DemandInfo demand = demands.get(position);

        holder.tv_expert.setText(demand.getExpert());
        holder.tv_city.setText(demand.getCity());
        holder.tv_budget.setText(demand.getBudget());
        holder.tv_look.setText(demand.getLook() + "");
        holder.tv_time.setText(demand.getTime());
        if (!demand.getBackground().equals("无")) {
            holder.tv_type.setText("需求背景：");
            holder.tv_desc.setText("    " + demand.getBackground());
        } else if (!demand.getDetails().equals("无")) {
            holder.tv_type.setText("需求详情：");
            holder.tv_desc.setText("    " + demand.getDetails());
        } else {
            holder.tv_type.setText("要求：");
            holder.tv_desc.setText("    " + demand.getRequirement());
        }

        return convertView;
    }

    static class ViewHolder {
        TextView tv_expert;
        TextView tv_city;
        TextView tv_budget;
        TextView tv_type;
        TextView tv_desc;
        TextView tv_look;
        TextView tv_time;
    }
}
