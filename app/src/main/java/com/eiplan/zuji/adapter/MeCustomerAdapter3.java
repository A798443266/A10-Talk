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
import com.eiplan.zuji.utils.UIUtils;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


//我的客户的适配器
public class MeCustomerAdapter3 extends BaseAdapter {

    private List<MyCustomer> customers;
    private Context context;

    public MeCustomerAdapter3(Context context, List<MyCustomer> customers) {
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_me_customer3, null);
            holder.cl_user_pic = convertView.findViewById(R.id.cl_user_pic);
            holder.tv_username = convertView.findViewById(R.id.tv_username);
            holder.tv_zhifu = convertView.findViewById(R.id.tv_zhifu);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MyCustomer customer = customers.get(position);
        holder.tv_username.setText(customer.getName());
        holder.tv_time.setText(customer.getTime());

        Glide.with(context).load(customer.getPic()).error(R.drawable.p).into(holder.cl_user_pic);
        if (customer.getState().equals("支付")) {
            holder.tv_zhifu.setText("客户尚未支付");
            holder.tv_zhifu.setTextColor(UIUtils.getColor(android.R.color.holo_red_light));
        } else {
            holder.tv_zhifu.setText("客户已支付");
        }
        return convertView;
    }

    class ViewHolder {
        CircleImageView cl_user_pic;
        TextView tv_username;
        TextView tv_zhifu;
        TextView tv_time;
    }

}
