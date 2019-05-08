package com.eiplan.zuji.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.CommentInfo;
import com.eiplan.zuji.bean.ProblemCommentInfo;
import com.eiplan.zuji.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

//问题评论的适配器
public class ProblemCommentAdapter extends BaseAdapter {

    private Context context;
    private List<ProblemCommentInfo> comments = new ArrayList<>();
    //    private int type;//用来判断采纳按钮是否可以点击，只有从我的问题进来的才可以点击
    private boolean hasAccepet;

    public ProblemCommentAdapter(Context context, List<ProblemCommentInfo> comments) {
        this.context = context;
        this.comments = comments;
    }

    public ProblemCommentAdapter(Context context) {
        this.context = context;
    }


    public void refresh(List<ProblemCommentInfo> comments) {
        if (comments != null && comments.size() > 0) {
            this.comments.clear();
            this.comments.addAll(comments);
            notifyDataSetChanged();
        }

    }


    @Override
    public int getCount() {
        return comments == null ? 0 : comments.size();
    }

    @Override
    public Object getItem(int position) {
        return comments.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_comment, null);
            holder = new ViewHolder();
            holder.tv_content = convertView.findViewById(R.id.tv_content);
            holder.tv_pinglun = convertView.findViewById(R.id.tv_pinglun);
            holder.tv_name = convertView.findViewById(R.id.tv_name);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.tv_zan = convertView.findViewById(R.id.tv_zan);
            holder.cv_user_pic = convertView.findViewById(R.id.cv_user_pic);
            holder.btn_caina = convertView.findViewById(R.id.btn_caina);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ProblemCommentInfo commnet = comments.get(position);
        holder.tv_content.setText(commnet.getContent());
        holder.tv_time.setText(commnet.getTime());
        holder.tv_name.setText(commnet.getName());
        holder.tv_pinglun.setText(commnet.getReply() + "");
        holder.tv_zan.setText(commnet.getGood() + "");

        Glide.with(context).load(commnet.getPic()).error(R.drawable.logo).into(holder.cv_user_pic);
        holder.btn_caina.setText(commnet.getState());

        if (commnet.getState().equals("采纳")) {
            holder.btn_caina.setBackgroundResource(R.drawable.release_study_commend_bg);
            holder.btn_caina.setText("已采纳");
        }


        holder.btn_caina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onAcceptClickListener != null)
                    onAcceptClickListener.OnAcceptClick(v, position);
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView tv_content;
        TextView tv_pinglun;
        TextView tv_name;
        TextView tv_time;
        TextView tv_zan;
        Button btn_caina;
        CircleImageView cv_user_pic;
    }

    private OnAcceptClickListener onAcceptClickListener;

    public interface OnAcceptClickListener {
        void OnAcceptClick(View v, int position);
    }

    public void setOnAcceptClickListener(OnAcceptClickListener onAcceptClickListener) {
        this.onAcceptClickListener = onAcceptClickListener;
    }
}
