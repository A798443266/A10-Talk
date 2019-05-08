package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.StudyInfo;
import com.eiplan.zuji.bean.VideoInfo;
import com.wx.goodview.GoodView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 课程界面适配器
 */
public class StudyAdapter extends BaseAdapter {
    private Context context;
    private List<StudyInfo> studys;

    public StudyAdapter(Context context, List<StudyInfo> studys) {
        this.context = context;
        this.studys = studys;
    }

    @Override
    public int getCount() {
        return studys == null ? 0 : studys.size();
    }

    @Override
    public Object getItem(int position) {
        return studys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_study, null);
            holder = new ViewHolder();
            holder.tv_play = convertView.findViewById(R.id.tv_play);
            holder.tv_title = convertView.findViewById(R.id.tv_title);
            holder.tv_price = convertView.findViewById(R.id.tv_price);
            holder.iv_cover = convertView.findViewById(R.id.iv_cover);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StudyInfo study = studys.get(position);
        holder.tv_play.setText(study.getPlays());
        holder.tv_title.setText(study.getTitle());
        holder.tv_price.setText(study.getPrice() + "");
        Glide.with(context).load(study.getCover()).error(R.drawable.p).into(holder.iv_cover);

        return convertView;
    }

    static class ViewHolder {
        TextView tv_play;
        TextView tv_title;
        TextView tv_price;
        ImageView iv_cover;
    }
}
