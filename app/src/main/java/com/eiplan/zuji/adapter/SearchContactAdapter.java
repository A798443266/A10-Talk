package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.SearchContactInfo;
import com.eiplan.zuji.bean.SearchGroupInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchContactAdapter extends BaseAdapter {

    private List<SearchContactInfo> contacts;
    private Context context;

    public SearchContactAdapter(List<SearchContactInfo> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    @Override
    public int getCount() {
        return contacts == null ? 0 : contacts.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_search_contact, null);
            holder = new ViewHolder();
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.cl_user_pic = convertView.findViewById(R.id.cl_user_pic);
            holder.btn_add = convertView.findViewById(R.id.btn_add);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        SearchContactInfo contact = contacts.get(position);

        holder.tv_name.setText(contact.getName());
        Glide.with(context).load(contact.getPic()).error(R.drawable.logo).into(holder.cl_user_pic);
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
