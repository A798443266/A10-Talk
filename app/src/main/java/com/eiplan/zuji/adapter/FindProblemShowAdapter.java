package com.eiplan.zuji.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.eiplan.zuji.R;
import com.eiplan.zuji.bean.ProblemInfo;
import com.eiplan.zuji.view.TextViewExpandableAnimation;
import com.wx.goodview.GoodView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 发现问题列表的显示的适配器
 */

public class FindProblemShowAdapter extends BaseAdapter implements View.OnClickListener {

    private List<ProblemInfo> datas;
    private Context context;

//    private GoodView mGoodView;

    public FindProblemShowAdapter(List<ProblemInfo> datas, Context context) {
        this.datas = datas;
        this.context = context;
//        mGoodView = new GoodView(context);
    }

    @Override
    public int getCount() {
        return datas == null ? 0 : datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_find_problem_show, null);
            holder = new ViewHolder();
            holder.tv_user_name = convertView.findViewById(R.id.tv_user_name);
            holder.tv_problem_desc = convertView.findViewById(R.id.tv_problem_desc);
            holder.iv_zan = convertView.findViewById(R.id.iv_zan);
            holder.tv_zan = convertView.findViewById(R.id.tv_zan);
            holder.tv_comment = convertView.findViewById(R.id.tv_comment);
            holder.tv_jifen = convertView.findViewById(R.id.tv_jifen);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
            holder.iv1 = convertView.findViewById(R.id.iv1);
            holder.iv2 = convertView.findViewById(R.id.iv2);
            holder.ll_photo = convertView.findViewById(R.id.ll_photo);
            holder.tv_time = convertView.findViewById(R.id.tv_time);
//            holder.gv = convertView.findViewById(R.id.gv);
            holder.cv_user_pic = convertView.findViewById(R.id.cv_user_pic);
            holder.cv_user_pic = convertView.findViewById(R.id.cv_user_pic);
            holder.tv_problem_name = convertView.findViewById(R.id.tv_problem_name);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ProblemInfo problem = datas.get(position);
        holder.tv_user_name.setText(problem.getPubisher());
        holder.tv_zan.setText(problem.getGood() + "");
        holder.tv_comment.setText(problem.getComment() + "");
        holder.tv_jifen.setText(problem.getPoint() + "");
        holder.tv_problem_desc.setText(problem.getContent());
        holder.tv_time.setText(problem.getTime());
        holder.tv_problem_name.setText(problem.getQuestion());
        Glide.with(context).load(problem.getToupic()).error(R.drawable.logo).into(holder.cv_user_pic);

        if (!problem.getPic().equals("0")) {//如果有图片
            holder.ll_photo.setVisibility(View.VISIBLE);
//            List<String> pics = new ArrayList<>();
//            pics.add(problem.getPic());
//            Log.e("TAG", "收到图片" + position + "  " + problem.getPic());
//            holder.gv.setAdapter(new ProblemPhotoAdapter(pics, context));
            Glide.with(context).load(problem.getPic()).error(R.drawable.ease_default_image).into(holder.iv1);
        } else {
            holder.ll_photo.setVisibility(View.GONE);
        }

        holder.iv_zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoodView mGoodView = new GoodView(context);
                holder.iv_zan.setImageResource(R.drawable.icon_zan1);
                mGoodView.setImage(context.getResources().getDrawable(R.drawable.icon_zan1));
                mGoodView.show(holder.iv_zan);
                holder.tv_zan.setText((Integer.parseInt(holder.tv_zan.getText().toString()) + 1) + "");
            }
        });

        //图片的点击事件
        holder.iv1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onPhotoClickListener != null) {
                    onPhotoClickListener.photoClick(problem.getPic());
                }
            }
        });
        return convertView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
//            case R.id.iv_zan:
//                itemZanClickListener.itemZanClick(v);
//                break;
        }
    }

    static class ViewHolder {
        TextView tv_user_name;
        TextViewExpandableAnimation tv_problem_desc;
        ImageView iv_zan;
        TextView tv_zan;
        TextView tv_problem_name;
        TextView tv_comment;
        TextView tv_jifen;
        TextView tv_time;
        //        GridView gv;
        ImageView iv1;
        ImageView iv2;
        LinearLayout ll_photo;
        CircleImageView cv_user_pic;
    }

    private ItemZanClickListener itemZanClickListener;
    private OnPhotoClickListener onPhotoClickListener;

    /**
     * 赞的点击监听接口
     */
    public interface ItemZanClickListener {
        void itemZanClick(View view);
    }

    /**
     * 图片点击的监听接口
     */
    public interface OnPhotoClickListener {
        void photoClick(String path);
    }

    public void setOnItemZanClickListener(ItemZanClickListener itemZanClickListener) {
        this.itemZanClickListener = itemZanClickListener;
    }

    public void setOnPhotoClickListener(OnPhotoClickListener onPhotoClickListener) {
        this.onPhotoClickListener = onPhotoClickListener;
    }


}
