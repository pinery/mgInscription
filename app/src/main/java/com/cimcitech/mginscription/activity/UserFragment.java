package com.cimcitech.mginscription.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.adapter.UserDeviceAdapter;
import com.cimcitech.mginscription.model.DeviceVo;
import com.cimcitech.mginscription.model.ResultVo;
import com.cimcitech.mginscription.model.UserInfoVo;
import com.cimcitech.mginscription.utils.ConfigUtil;
import com.cimcitech.mginscription.utils.ToastUtil;
import com.cimcitech.mginscription.widget.ShapeLoadingDialog;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

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
    @BindView(R.id.username_tv)
    TextView usernameTv;
    @BindView(R.id.device_number_tv)
    TextView deviceNumberTv;
    @BindView(R.id.device_register_view)
    FrameLayout deviceRegisterView;

    private Unbinder unbinder;
    private UserDeviceAdapter adapter;
    private UserInfoVo userInfoVo;
    private ShapeLoadingDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getUserInfoData();
    }

    private void initView() {
        dialog = new ShapeLoadingDialog(getActivity());
        dialog.setLoadingText("正在加载数据...");
    }

    public void getUserInfoData() {
        if (ConfigUtil.isLogin) {
            //获取用户详细信息
            dialog.show();
            String data = new Gson().toJson(new Userinfo(ConfigUtil.GET_TIME()));
            Map map = new HashMap();
            map.put("time", ConfigUtil.GET_TIME());
            String sign = ConfigUtil.GET_SIGN(map);
            getUserinfo(data, sign);
            //获取用户设备信息
            if (ConfigUtil.infoBeans != null && ConfigUtil.infoBeans.size() > 0) {
                adapter = new UserDeviceAdapter(getActivity(), ConfigUtil.infoBeans);
                listContent.setAdapter(adapter);
            } else {
                listContent.setVisibility(View.GONE);
            }
        }
    }

    @OnClick({R.id.setting_layout, R.id.device_register_view})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.setting_layout:
                Intent intent = new Intent(getActivity(), SettingActivity.class);
                intent.putExtra("userInfoVo", userInfoVo);
                startActivity(intent);
                break;
            case R.id.device_register_view:
                startActivity(new Intent(getActivity(), AddDeviceActivity.class));
                break;
        }
    }

    //获取用户详细信息
    private void getUserinfo(String data, String sign) {
        OkHttpUtils
                .post()
                .url(ConfigUtil.IP)
                .addParams("service", "Users.GetUsersInfo")
                .addParams("data", data)
                .addParams("user_id", ConfigUtil.loginInfo.getRegister_id())
                .addParams("token", ConfigUtil.loginInfo.getToken())
                .addParams("sign", sign)
                .build()
                .execute(
                        new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtil.showNetError();
                                dialog.dismiss();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Gson gson = new Gson();
                                ResultVo resultVo = gson.fromJson(response, ResultVo.class);
                                dialog.dismiss();
                                if (resultVo != null && resultVo.getRet() == 200)
                                    if (resultVo.getData() != null)
                                        if (resultVo.getData().getCode() == 1) {//正常返回
                                            userInfoVo = gson.fromJson(response, UserInfoVo.class);
                                            usernameTv.setText(userInfoVo.getData().getInfo().getRegister_name() != null ?
                                                    userInfoVo.getData().getInfo().getRegister_name() :
                                                    userInfoVo.getData().getInfo().getRegister_phone());
                                            deviceNumberTv.setText(userInfoVo.getData().getInfo().getRegister_device_number() != null ?
                                                    "拥有设备 " + userInfoVo.getData().getInfo().getRegister_device_number() : "拥有设备 0");
                                        } else if (resultVo.getData().getCode() == 2) {//登录超时
                                            ToastUtil.showToast("登录超时，请重新登录");
                                            ConfigUtil.isLogin = false;
                                            ConfigUtil.isOutLogin = true;
                                            startActivity(new Intent(getActivity(), LoginActivity.class));
                                        } else
                                            ToastUtil.showToast(resultVo.getData().getReturnmsg());
                            }
                        }
                );
    }

    class Userinfo {
        String time;

        public Userinfo(String time) {
            this.time = time;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
