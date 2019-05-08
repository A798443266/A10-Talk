package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.VideoInfo;
import com.wx.goodview.GoodView;

import java.util.List;


public class UpdateVideoAdapter extends BaseAdapter {
    private Context context;
    private List<VideoInfo> videos;

    public UpdateVideoAdapter(Context context, List<VideoInfo> videos) {
        this.context = context;
        this.videos = videos;
    }

    @Override
    public int getCount() {
        return videos == null ? 0 : videos.size();
    }

    @Override
    public Object getItem(int position) {
        return videos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_update_vedio, null);
            holder = new ViewHolder();
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_major = convertView.findViewById(R.id.tv_major);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.iv_cover = convertView.findViewById(R.id.iv_cover);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VideoInfo video = videos.get(position);
        holder.tv_time.setText(video.getTime());
        holder.tv_major.setText(video.getMajor());
        holder.tv_title.setText(video.getTitle());

        Glide.with(context).load(video.getCover()).error(R.drawable.vedio_bg).into(holder.iv_cover);

        return convertView;
    }

    class ViewHolder {
        TextView tv_time;
        TextView tv_title;
        TextView tv_major;
        ImageView iv_cover;
    }
}
