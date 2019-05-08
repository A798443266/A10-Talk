package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eiplan.zuji.R;

import java.util.List;

public class HomeProblemAdapter extends BaseAdapter {

    private List<String[]> datas;
    private Context context;

    public HomeProblemAdapter(List<String[]> datas, Context context) {
        this.datas = datas;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
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
            convertView = View.inflate(context, R.layout.home_problem, null);
            holder = new ViewHolder();
            holder.tv_question = convertView.findViewById(R.id.tv_question);
            holder.tv_answer = convertView.findViewById(R.id.tv_answer);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.tv_question.setText(datas.get(position)[0]);
        holder.tv_answer.setText("热门答案：" + datas.get(position)[1]);
        return convertView;
    }

    static class ViewHolder {
        TextView tv_question;
        TextView tv_answer;
    }
}
