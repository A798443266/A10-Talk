package com.eiplan.zuji.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.eiplan.zuji.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布问题显示图片的适配器
 */

public class ReleasePhotoAdapter extends BaseAdapter {

    private LayoutInflater listContainer;
    private int selectedPosition = -1;
    private Context context;
    private List<Bitmap> bmps = new ArrayList();

    public ReleasePhotoAdapter(Context context, List<Bitmap> bmp) {
        listContainer = LayoutInflater.from(context);
        bmps = bmp;
        this.context = context;
    }

    public int getCount() {
        return bmps == null ? 0 : bmps.size() + 1;
    }

    public Object getItem(int arg0) {
        return null;
    }

    public long getItemId(int arg0) {
        return 0;
    }

    public void setSelectedPosition(int position) {
        selectedPosition = position;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        final int sign = position;
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = listContainer.inflate(R.layout.item_release_photo, null);
            holder.image = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (position == bmps.size()) {
            holder.image.setImageBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_addpic_unfocused));
            if (position == 3) {
                holder.image.setVisibility(View.GONE);
            }
        } else {
            holder.image.setImageBitmap(bmps.get(position));
        }
        return convertView;
    }

    static class ViewHolder{
        public ImageView image;
    }
}
