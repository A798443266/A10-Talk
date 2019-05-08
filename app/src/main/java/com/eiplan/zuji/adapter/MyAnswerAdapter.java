package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.MyAnswerProblem;

import java.util.List;

/**
 * 我回答的问题适配器
 */
public class MyAnswerAdapter extends BaseAdapter {

    private List<MyAnswerProblem> datas;
    private Context context;

    public MyAnswerAdapter(List<MyAnswerProblem> datas, Context context) {
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
            convertView = View.inflate(context, R.layout.item_my_question, null);
            holder = new ViewHolder();
            holder.tv_jifen = convertView.findViewById(R.id.tv_jifen);
            holder.tv_answer = convertView.findViewById(R.id.tv_answer);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_time = convertView.findViewById(R.id.tv_time);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyAnswerProblem myAnswerProblem = datas.get(position);

        holder.tv_name.setText(myAnswerProblem.getProblemName());
        holder.tv_answer.setText("我的回答：" + myAnswerProblem.getProblemCommentInfo().getContent());
        holder.tv_time.setText(myAnswerProblem.getProblemCommentInfo().getTime());
        holder.tv_jifen.setText("+" + myAnswerProblem.getProblemPoint());
        return convertView;
    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_time;
        TextView tv_answer;
        TextView tv_jifen;
    }
}
