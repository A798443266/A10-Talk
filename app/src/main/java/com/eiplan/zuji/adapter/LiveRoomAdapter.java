package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.LiveRoom;

import java.util.List;


public class LiveRoomAdapter extends BaseAdapter {

    private List<LiveRoom> rooms;
    private Context context;

    public LiveRoomAdapter(List<LiveRoom> rooms, Context context) {
        this.rooms = rooms;
        this.context = context;
    }


    @Override
    public int getCount() {
        return rooms == null ? 0 : rooms.size();
    }

    @Override
    public Object getItem(int position) {
        return rooms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = View.inflate(context,R.layout.live_room,null);
            holder = new ViewHolder();
            holder.tv_username = convertView.findViewById(R.id.tv_username);
            holder.tv_room_name = convertView.findViewById(R.id.tv_room_name);
            holder.tv_people = convertView.findViewById(R.id.tv_people);
            holder.iv_cover = convertView.findViewById(R.id.iv_cover);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        LiveRoom room = rooms.get(position);
        holder.tv_username.setText(room.getName());
        holder.tv_room_name.setText(room.getTitle());
        holder.tv_people.setText(room.getSee() + "");
        Glide.with(context).load(room.getCover()).error(R.drawable.p).into(holder.iv_cover);
        return convertView;
    }

    static class ViewHolder {
        TextView tv_room_name;
        TextView tv_username;
        TextView tv_people;
        ImageView iv_cover;
    }
}
