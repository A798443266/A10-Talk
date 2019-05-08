package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.RecordInfo;

import java.util.List;

/**
 * 搜索记录适配器
 */
public class SearchRecordAdapter extends BaseAdapter implements View.OnClickListener {

    private Context context;
    private List<RecordInfo> records;

    public SearchRecordAdapter(Context context, List<RecordInfo> records) {
        this.context = context;
        this.records = records;
    }

    @Override
    public int getCount() {
        return records == null ? 0 : records.size();
    }

    @Override
    public Object getItem(int position) {
        return records.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context, R.layout.item_record,null);
            holder = new ViewHolder();
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            holder.iv_delete = convertView.findViewById(R.id.iv_delete);

            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        RecordInfo record = records.get(position);
        holder.tv_content.setText(record.getContent());
        holder.iv_delete.setTag(position);
        holder.iv_delete.setOnClickListener(this);
        return convertView;
    }

    @Override
    public void onClick(View v) {
        itemDeleteClickListener.itemDeleteClick(v);
    }

    static class ViewHolder {
        TextView tv_content;
        ImageView iv_delete;
    }

    private ItemDeleteClickListener itemDeleteClickListener;


    //实现接口可以点击item内部的控件
    public interface ItemDeleteClickListener{
        void itemDeleteClick(View view);
    }

    public void setOnItemDeleteClickListener(ItemDeleteClickListener itemDeleteClickListener){
        this.itemDeleteClickListener = itemDeleteClickListener;
    }
}
