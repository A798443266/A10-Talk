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

import de.hdodenhof.circleimageview.CircleImageView;

public class VedioAdapter extends BaseAdapter {
    private Context context;
    private List<VideoInfo> videos;
    private GoodView mGoodView;

    public VedioAdapter(Context context, List<VideoInfo> videos) {
        this.context = context;
        this.videos = videos;
        mGoodView = new GoodView(context);
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
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_vedio, null);
            holder = new ViewHolder();
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.iv_zan = convertView.findViewById(R.id.iv_zan);
            holder.tv_zan = convertView.findViewById(R.id.tv_zan);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_desc = convertView.findViewById(R.id.tv_desc);
            holder.tv_look = convertView.findViewById(R.id.tv_look);
            holder.tv_pinglun = convertView.findViewById(R.id.tv_pinglun);
            holder.cv_user_pic = convertView.findViewById(R.id.cv_user_pic);
            holder.iv_cover = convertView.findViewById(R.id.iv_cover);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        VideoInfo video = videos.get(position);
        holder.tv_name.setText(video.getUploader());
        holder.tv_zan.setText(video.getGood() + "");
        holder.tv_look.setText(video.getLook() + "");
        holder.tv_pinglun.setText(video.getComment() + "");
        holder.tv_time.setText(video.getTime());
        holder.tv_desc.setText(video.getTitle());

        Glide.with(context).load(video.getCover()).error(R.drawable.vedio_bg).into(holder.iv_cover);
        Glide.with(context).load(video.getPic()).error(R.drawable.logo).into(holder.cv_user_pic);

        holder.iv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.iv_zan.setImageResource(R.drawable.icon_zan1);
                mGoodView.setImage(context.getResources().getDrawable(R.drawable.icon_zan1));
                mGoodView.show(holder.iv_zan);
                holder.tv_zan.setText((Integer.parseInt(holder.tv_zan.getText().toString()) + 1) + "");
            }
        });
        return convertView;
    }

    static class ViewHolder {
        TextView tv_name;
        ImageView iv_zan;
        TextView tv_zan;
        TextView tv_time;
        TextView tv_desc;
        TextView tv_look;
        TextView tv_pinglun;
        ImageView iv_cover;
        CircleImageView cv_user_pic;
    }
}
