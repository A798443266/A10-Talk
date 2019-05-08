package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.MyCustomer;
import com.eiplan.zuji.bean.UserInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


//我的客户的适配器
public class MeCustomerAdapter2 extends BaseAdapter {

    private List<MyCustomer> customers;
    private Context context;

    public MeCustomerAdapter2(Context context, List<MyCustomer> customers) {
        this.context = context;
        this.customers = customers;
    }

    @Override
    public int getCount() {
        return customers == null ? 0 : customers.size();
    }

    @Override
    public Object getItem(int position) {
        return customers.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_me_customer2, null);
            holder.cl_user_pic = convertView.findViewById(R.id.cl_user_pic);
            holder.tv_username = convertView.findViewById(R.id.tv_username);
            holder.tv_dingdan = convertView.findViewById(R.id.tv_dingdan);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyCustomer customer = customers.get(position);
        holder.tv_username.setText(customer.getName());
        Glide.with(context).load(customer.getPic()).error(R.drawable.p).into(holder.cl_user_pic);

        holder.tv_dingdan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGreeClickListener.itemGreeClick(v, position);
            }
        });
        return convertView;
    }

    class ViewHolder {
        CircleImageView cl_user_pic;
        TextView tv_username;
        TextView tv_dingdan;
        TextView tv_time;
    }

    private OnGreeClickListener onGreeClickListener;


    //实现接口可以点击item内部的控件
    public interface OnGreeClickListener {
        void itemGreeClick(View view, int position);
    }

    public void setOnGreeClickListener(OnGreeClickListener onGreeClickListener) {
        this.onGreeClickListener = onGreeClickListener;
    }

}
