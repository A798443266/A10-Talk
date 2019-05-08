package com.eiplan.zuji.menu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.eiplan.zuji.R;
import com.eiplan.zuji.utils.UIUtils;

import java.util.List;

/**
 * 二级分类（即右侧菜单）的adapter
 */
public class SecondAdapter extends BaseAdapter {
    private Context context;
    private List<SecondItem> list;

    public SecondAdapter(Context context, List<SecondItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.right_listview_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.name.setText(list.get(position).getName());
        //选中和没选中时，设置不同的颜色
        if (position == selectedPosition) {
            convertView.setBackgroundResource(R.color.color_white);
            holder.name.setTextColor(UIUtils.getColor(R.color.system_blue));
        } else {
            convertView.setBackgroundResource(R.color.select_bg3);
            holder.name.setTextColor(UIUtils.getColor(R.color.color_system_gray));
        }

        return convertView;
    }

    private int selectedPosition = -1;

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    private class ViewHolder {
        TextView name;
    }
}
