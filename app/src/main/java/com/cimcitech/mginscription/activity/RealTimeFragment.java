package com.cimcitech.mginscription.activity;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.adapter.PopDeviceAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dapineapple on 2017/12/27.
 */

public class RealTimeFragment extends Fragment {

    @BindView(R.id.drop_down_iv)
    ImageView dropDownIv;

    private Unbinder unbinder;
    private PopupWindow pop;
    private PopDeviceAdapter adapter;
    private List<String> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.real_time_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        for (int i = 0; i < 6; i++)
            data.add("EQ20170635-00" + i);
        adapter = new PopDeviceAdapter(getActivity(), data);
    }

    @OnClick({R.id.drop_down_iv})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.drop_down_iv:
                showCancelPopWin(getActivity());
                pop.showAtLocation(view, Gravity.CENTER, 0, 0);
                break;
        }
    }

    public void showCancelPopWin(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.dialog_device_list_view, null);
        view.getBackground().setAlpha(100);
        // 创建PopupWindow对象
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        ListView listContent = view.findViewById(R.id.listContent);
        ImageView drop_down_iv = view.findViewById(R.id.drop_down_iv);
        listContent.setAdapter(adapter);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        drop_down_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
