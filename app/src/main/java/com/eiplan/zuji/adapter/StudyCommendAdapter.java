package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.StudyCommendInfo;
import com.eiplan.zuji.bean.StudyInfo;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 课程评论的适配器
 */
public class StudyCommendAdapter extends BaseAdapter {
    private Context context;
    private List<StudyCommendInfo> studys;

    public StudyCommendAdapter(Context context, List<StudyCommendInfo> studys) {
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
            convertView = View.inflate(context, R.layout.item_study_commend, null);
            holder = new ViewHolder();
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_zan = convertView.findViewById(R.id.tv_zan);
            holder.iv_zan = convertView.findViewById(R.id.iv_zan);
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            holder.cv_user_pic = convertView.findViewById(R.id.cv_user_pic);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StudyCommendInfo study = studys.get(position);
        holder.tv_name.setText(study.getUserName());
        holder.tv_time.setText(study.getTime());
        holder.tv_zan.setText(study.getGood() + "");
        holder.tv_content.setText(study.getContent());
        Glide.with(context).load(study.getUserPic()).error(R.drawable.p).into(holder.cv_user_pic);

        return convertView;
    }

    static class ViewHolder {
        TextView tv_time;
        TextView tv_name;
        TextView tv_zan;
        TextView tv_content;
        ImageView iv_zan;
        CircleImageView cv_user_pic;
    }
}
