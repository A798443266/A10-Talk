package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.StudyInfo;
import com.eiplan.zuji.bean.TradeInfo;

import java.util.List;

/**
 * 交易记录适配器
 */
public class TradeAdapter extends BaseAdapter {
    private Context context;
    private List<TradeInfo> trades;

    public TradeAdapter(Context context, List<TradeInfo> trades) {
        this.context = context;
        this.trades = trades;
    }

    @Override
    public int getCount() {
        return trades == null ? 0 : trades.size();
    }

    @Override
    public Object getItem(int position) {
        return trades.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_trade, null);
            holder = new ViewHolder();
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TradeInfo trade = trades.get(position);
        holder.tv_time.setText(trade.getTime());
        holder.tv_content.setText(trade.getOperation());
        holder.tv_price.setText("¥ -" + trade.getPrice());

        return convertView;
    }

    static class ViewHolder {
        TextView tv_time;
        TextView tv_content;
        TextView tv_price;
    }
}
