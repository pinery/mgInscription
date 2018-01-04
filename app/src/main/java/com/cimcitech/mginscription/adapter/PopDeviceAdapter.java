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

public class PopDeviceAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<DeviceVo.DataBean.InfoBean> data;

    public List<DeviceVo.DataBean.InfoBean> getAll() {
        return data;
    }

    public PopDeviceAdapter(Context context, List<DeviceVo.DataBean.InfoBean> data) {
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
        DeviceVo.DataBean.InfoBean info = data.get(position);
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            view = inflater.inflate(R.layout.pop_device_list_item, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.contentTv.setText(info.getDevice_num());
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
