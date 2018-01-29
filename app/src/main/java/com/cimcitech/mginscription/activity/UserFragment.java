package com.cimcitech.mginscription.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.model.DeviceVo;
import com.cimcitech.mginscription.model.ResultVo;
import com.cimcitech.mginscription.model.UserInfoVo;
import com.cimcitech.mginscription.utils.ConfigUtil;
import com.cimcitech.mginscription.utils.MyActivityManager;
import com.cimcitech.mginscription.utils.ToastUtil;
import com.cimcitech.mginscription.widget.ShapeLoadingDialog;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

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
        if (ConfigUtil.isAddDevice) {
            ConfigUtil.isAddDevice = false;
            //刷新设备列表
            if (ConfigUtil.loginInfo != null)
                getUserDeviceInfoData();
        }
    }

    //刷新设备列表
    private void getUserDeviceInfoData() {
        String data = new Gson().toJson(new GetUserDeviceInfo("20", "1",
                ConfigUtil.GET_TIME(), "", "", ""));
        Map map = new HashMap();
        map.put("num", "20");
        map.put("start", "1");
        map.put("time", ConfigUtil.GET_TIME());
        map.put("device_num", "");
        map.put("startTime", "");
        map.put("endTime", "");
        String sign = ConfigUtil.GET_SIGN(map);
        GetUserDeviceInfo(data, sign);
    }

    class GetUserDeviceInfo {
        String num;
        String start;
        String time;
        String device_num;
        String startTime;
        String endTime;

        public GetUserDeviceInfo(String num, String start, String time, String device_num, String startTime, String endTime) {
            this.num = num;
            this.start = start;
            this.time = time;
            this.device_num = device_num;
            this.startTime = startTime;
            this.endTime = endTime;
        }
    }

    //用户获取设备信息(批量，带搜索)
    private void GetUserDeviceInfo(String data, String sign) {
        OkHttpUtils
                .post()
                .url(ConfigUtil.IP)
                .addParams("service", "UsersDevice.GetUserDeviceInfo")
                .addParams("userDeviceInfo", data)
                .addParams("user_id", ConfigUtil.loginInfo.getRegister_id())
                .addParams("token", ConfigUtil.loginInfo.getToken())
                .addParams("sign", sign)
                .build()
                .execute(
                        new StringCallback() {
                            @Override
                            public void onError(Call call, Exception e, int id) {
                                ToastUtil.showNetError();
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Gson gson = new Gson();
                                ResultVo resultVo = gson.fromJson(response, ResultVo.class);
                                dialog.dismiss();
                                if (resultVo != null && resultVo.getRet() == 200)
                                    if (resultVo.getData() != null)
                                        if (resultVo.getData().getCode() == 1) {//正常返回
                                            DeviceVo deviceVo = gson.fromJson(response, DeviceVo.class);
                                            if (deviceVo.getData() != null && deviceVo.getData().getInfo() != null)
                                                if (deviceVo.getData().getInfo().size() > 0) {
                                                    ConfigUtil.infoBeans = deviceVo.getData().getInfo();
                                                    adapter = new UserDeviceAdapter(getActivity(), ConfigUtil.infoBeans);
                                                    listContent.setAdapter(adapter);
                                                }
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

    private void initView() {
        MyActivityManager manager = MyActivityManager.getInstance();
        manager.pushOneActivity(getActivity());
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

    public class UserDeviceAdapter extends BaseAdapter {

        private LayoutInflater inflater;
        private List<DeviceVo.DataBean.InfoBean> data;

        public List<DeviceVo.DataBean.InfoBean> getAll() {
            return data;
        }

        public UserDeviceAdapter(Context context, List<DeviceVo.DataBean.InfoBean> data) {
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
            DeviceVo.DataBean.InfoBean item = data.get(position);
            ViewHolder viewHolder = null;
            if (viewHolder == null) {
                view = inflater.inflate(R.layout.user_device_list_item, null);
                viewHolder = new ViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
            viewHolder.deviceNumTv.setText(item.getDevice_num() != null ? item.getDevice_num() : "-");
            viewHolder.deviceRegTimeTv.setText(item.getDevice_reg_time() != null ? item.getDevice_reg_time() : "-");
            viewHolder.deleteDeviceBt.setOnClickListener(new deleteButtonClickListener(position));
            return view;
        }

        class deleteButtonClickListener implements View.OnClickListener {

            private int position;

            public deleteButtonClickListener(int position) {
                this.position = position;
            }

            @Override
            public void onClick(View view) {
                final DeviceVo.DataBean.InfoBean infoBean = data.get(position);
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("确认删除此设备？")
                        .setCancelable(false)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getDelUserDeviceInfoData(infoBean.getDevice_num());
                            }
                        })
                        .setNegativeButton("取消", null).create().show();
            }
        }

        class DelUserDeviceInfo {
            String device_num;
            String time;

            public DelUserDeviceInfo(String device_num, String time) {
                this.device_num = device_num;
                this.time = time;
            }
        }

        private void getDelUserDeviceInfoData(String deviceNum) {
            String data = new Gson().toJson(new DelUserDeviceInfo(deviceNum, ConfigUtil.GET_TIME()));
            Map map = new HashMap();
            map.put("device_num", deviceNum);
            map.put("time", ConfigUtil.GET_TIME());
            String sign = ConfigUtil.GET_SIGN(map);
            DelUserDeviceInfo(data, sign);
        }

        //删除设备
        private void DelUserDeviceInfo(String data, String sign) {
            OkHttpUtils
                    .post()
                    .url(ConfigUtil.IP)
                    .addParams("service", "UsersDevice.DelUserDeviceInfo")
                    .addParams("userDeviceInfo", data)
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
                                                ToastUtil.showToast("删除成功");
                                                getUserDeviceInfoData();
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

        class ViewHolder {
            @BindView(R.id.device_num_tv)
            TextView deviceNumTv;
            @BindView(R.id.device_reg_time_tv)
            TextView deviceRegTimeTv;
            @BindView(R.id.delete_device_bt)
            Button deleteDeviceBt;

            ViewHolder(View view) {
                ButterKnife.bind(this, view);
            }
        }
    }
}
