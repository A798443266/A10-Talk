package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.DocInfo;
import com.eiplan.zuji.bean.SearchGroupInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchGroupAdapter extends BaseAdapter {

    private List<SearchGroupInfo> groups;
    private Context context;

    public SearchGroupAdapter(List<SearchGroupInfo> groups, Context context) {
        this.groups = groups;
        this.context = context;
    }

    @Override
    public int getCount() {
        return groups == null ? 0 : groups.size();
    }

    @Override
    public Object getItem(int position) {
        return groups.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_search_group, null);
            holder = new ViewHolder();
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_desc = convertView.findViewById(R.id.tv_desc);
            holder.cl_user_pic = convertView.findViewById(R.id.cl_user_pic);
            holder.btn_add = convertView.findViewById(R.id.btn_add);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SearchGroupInfo group = groups.get(position);

        holder.tv_name.setText(group.getName());
        holder.tv_desc.setText(group.getDescribe());
        holder.btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAcceptClickListener != null) {
                    onAcceptClickListener.acceptClick(v, position);
                }
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tv_name;
        TextView tv_desc;
        CircleImageView cl_user_pic;
        Button btn_add;
    }

    public interface OnAcceptClickListener {
        void acceptClick(View view, int position);
    }

    private OnAcceptClickListener onAcceptClickListener;

    public void setOnAcceptClickListener(OnAcceptClickListener onAcceptClickListener) {
        this.onAcceptClickListener = onAcceptClickListener;
    }
}
