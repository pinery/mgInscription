package com.cimcitech.mginscription.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.adapter.UserDeviceAdapter;
import com.cimcitech.mginscription.model.DeviceVo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dapineapple on 2017/12/27.
 */

public class UserFragment extends Fragment {

    @BindView(R.id.user_photo_iv)
    ImageView userPhotoIv;
    @BindView(R.id.center_line)
    ImageView centerLine;
    @BindView(R.id.setting_layout)
    RelativeLayout settingLayout;
    @BindView(R.id.listContent)
    ListView listContent;

    private Unbinder unbinder;
    private UserDeviceAdapter adapter;
    private List<DeviceVo> deviceVos = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        for (int i = 0; i < 6; i++) {
            DeviceVo deviceVo = new DeviceVo();
            deviceVo.setName("XQ20171227-00" + i);
            deviceVos.add(deviceVo);
        }
        adapter = new UserDeviceAdapter(getActivity(), deviceVos);
        listContent.setAdapter(adapter);
    }

    @OnClick({R.id.setting_layout, R.id.device_register_view})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.setting_layout:
                startActivity(new Intent(getActivity(), SettingActivity.class));
                break;
            case R.id.device_register_view:
                startActivity(new Intent(getActivity(), AddDeviceActivity.class));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
