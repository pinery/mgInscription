package com.cimcitech.mginscription.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.model.StatisticsByDayVo;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by cimcitech on 2017/8/1.
 */

public class PopupWindowAdapter extends BaseAdapter {

    private List<StatisticsByDayVo.DataBean.InfoBean> data;

    private LayoutInflater inflater;

    public PopupWindowAdapter(Context context, List<StatisticsByDayVo.DataBean.InfoBean> infoBeans) {
        inflater = LayoutInflater.from(context);
        this.data = infoBeans;
    }

    public List<StatisticsByDayVo.DataBean.InfoBean> getAll() {
        return data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        StatisticsByDayVo.DataBean.InfoBean infoBean = data.get(i);
        ViewHolder viewHolder = null;
        if (viewHolder == null) {
            view = inflater.inflate(R.layout.popup_window_item_view, null);
            viewHolder = new ViewHolder(view);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        viewHolder.textView.setText(infoBean.getDev_num());
        return view;
    }

    static class ViewHolder {
        @BindView(R.id.textView)
        TextView textView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
