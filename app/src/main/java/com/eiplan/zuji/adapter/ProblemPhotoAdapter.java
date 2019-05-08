package com.eiplan.zuji.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;

import java.util.List;

//显示问题图片的适配器
class ProblemPhotoAdapter extends BaseAdapter {

    private List<String> pics;
    private Context context;

    public ProblemPhotoAdapter(List<String> pics, Context context) {
        this.pics = pics;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pics == null ? 0 : pics.size();
    }

    @Override
    public Object getItem(int position) {
        return pics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder1 holder1;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_problem_gv, null);
            holder1 = new ViewHolder1();
            holder1.iv = convertView.findViewById(R.id.iv);
            convertView.setTag(holder1);
        } else {
            holder1 = (ViewHolder1) convertView.getTag();
        }
//        Glide.with(context).load(pics.get(position)).into(holder1.iv);

        return convertView;
    }

    static class ViewHolder1 {
        ImageView iv;
    }
}


