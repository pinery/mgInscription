package com.cimcitech.mginscription.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.model.DeviceVo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cimcitech on 2017/12/28.
 */

public class UserDeviceAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<DeviceVo> data;

    public List<DeviceVo> getAll() {
        return data;
    }

    public UserDeviceAdapter(Context context, List<DeviceVo> data) {
        inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        DeviceVo deviceVo = data.get(position);
        PopDeviceAdapter.ViewHolder viewHolder = null;
        if (viewHolder == null) {
            view = inflater.inflate(R.layout.user_device_list_item, null);
            viewHolder = new PopDeviceAdapter.ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (PopDeviceAdapter.ViewHolder) view.getTag();
        }
        //viewHolder.contentTv.setText(deviceVo.getName());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.content_tv)
        TextView contentTv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}