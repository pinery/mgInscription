package com.cimcitech.mginscription.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.cimcitech.mginscription.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cimcitech on 2018/1/11.
 */

public class DataAxisYAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<List<String>> y;

    public DataAxisYAdapter(Context context, List<List<String>> y) {
        inflater = LayoutInflater.from(context);
        this.y = y;
    }

    @Override
    public int getCount() {
        return y.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        List<String> item = y.get(position);
        ViewHolder wrapper = null;
        if (wrapper == null) {
            convertView = inflater.inflate(R.layout.data_axis_item_view, null);
            wrapper = new ViewHolder(convertView);
            convertView.setTag(wrapper);
            convertView.setPadding(5, 10, 5, 10);
        } else {
            wrapper = (ViewHolder) convertView.getTag();
        }
        if (item.size() >= 2) {
            wrapper.itemNumTv.setText("" + (position + 1));
            wrapper.itemValueTv.setText(item.get(0));
            if (item.get(1).equals("1")) {
                wrapper.itemCb.setImageDrawable(ContextCompat.getDrawable(inflater.getContext(), R.mipmap.check_off_icon));
            } else if (item.get(1).equals("0"))
                wrapper.itemCb.setImageDrawable(ContextCompat.getDrawable(inflater.getContext(), R.mipmap.check_on_icon));
        }
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.item_num_tv)
        TextView itemNumTv;
        @BindView(R.id.item_value_tv)
        TextView itemValueTv;
        @BindView(R.id.item_cb)
        ImageView itemCb;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
