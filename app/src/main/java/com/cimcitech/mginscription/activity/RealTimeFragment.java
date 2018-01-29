package com.cimcitech.mginscription.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.adapter.PopDeviceAdapter;
import com.cimcitech.mginscription.model.DeviceInfoVo;
import com.cimcitech.mginscription.model.DeviceVo;
import com.cimcitech.mginscription.model.ResultVo;
import com.cimcitech.mginscription.utils.ConfigUtil;
import com.cimcitech.mginscription.utils.MyActivityManager;
import com.cimcitech.mginscription.utils.ToastUtil;
import com.cimcitech.mginscription.widget.CustomScrollView;
import com.cimcitech.mginscription.widget.ShapeLoadingDialog;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import okhttp3.Call;

/**
 * Created by dapineapple on 2017/12/27.
 */

public class RealTimeFragment extends Fragment {

    @BindView(R.id.device_num_tv)
    TextView deviceNumTv;
    @BindView(R.id.drop_down_iv)
    ImageView dropDownIv;
    @BindView(R.id.work_time_tv)
    TextView workTimeTv;
    @BindView(R.id.startTime_time_tv)
    TextView startTimeTimeTv;
    @BindView(R.id.startTime_date_tv)
    TextView startTimeDateTv;
    @BindView(R.id.makenum_tv)
    TextView makenumTv;
    @BindView(R.id.ambienttemperature_tv)
    TextView ambienttemperatureTv;
    @BindView(R.id.ambienthumidity_tv)
    TextView ambienthumidityTv;
    @BindView(R.id.sumTime_layout)
    RelativeLayout sumTimeLayout;
    @BindView(R.id.scrollView)
    CustomScrollView scrollView;
    @BindView(R.id.sumTime_tv)
    TextView sumTimeTv;
    @BindView(R.id.countmakenum_tv)
    TextView countmakenumTv;
    @BindView(R.id.maintenance_time_tv)
    TextView maintenanceTimeTv;
    @BindView(R.id.maintenance_bt)
    Button maintenanceBt;

    private Unbinder unbinder;
    private PopupWindow pop;//pop
    private PopDeviceAdapter popAdapter;//pop
    private ListView popListContent;//pop
    private TextView popDeviceNum;//pop
    private String searchKey = "";//pop搜索设备的关键字
    private boolean isSearch = false; // pop是否在搜索设备
    private ShapeLoadingDialog dialog;
    private DeviceVo deviceVo;
    private DeviceVo.DataBean.InfoBean info; //默认显示该设备的数据
    private Handler uiHandler = null;
    private final int REQUEST_RESULT = 1000;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.real_time_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        initHandler();
        initView();
        return view;
    }

    private void initView() {
        MyActivityManager manager = MyActivityManager.getInstance();
        manager.pushOneActivity(getActivity());
        showCancelPopWin(getActivity());
        //获取设备信息
        dialog = new ShapeLoadingDialog(getActivity());
        dialog.setLoadingText("正在加载数据...");
        scrollView.setOnRefreshListener(new setPullRefreshListener());
    }

    public void initHandler() {
        uiHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case REQUEST_RESULT:// 显示加载中....
                        getUserDeviceInfoData();
                        break;
                }
                return false;
            }
        });
    }

    private class MyTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(1000);
                uiHandler.sendEmptyMessage(REQUEST_RESULT);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            scrollView.onRefreshComplete();
        }
    }

    class setPullRefreshListener implements CustomScrollView.OnRefreshListener {

        @Override
        public void onRefresh() {
            new MyTask().execute();//下拉刷新
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (ConfigUtil.loginInfo != null)
            getUserDeviceInfoData();
    }

    private void getUserDeviceInfoData() {
        dialog.show();
        String data = new Gson().toJson(new GetUserDeviceInfo("20", "1",
                ConfigUtil.GET_TIME(), searchKey, "", ""));
        Map map = new HashMap();
        map.put("num", "20");
        map.put("start", "1");
        map.put("time", ConfigUtil.GET_TIME());
        map.put("device_num", searchKey);
        map.put("startTime", "");
        map.put("endTime", "");
        String sign = ConfigUtil.GET_SIGN(map);
        GetUserDeviceInfo(data, sign);
    }

    private void getDeviceInfoData() {
        String data = new Gson().toJson(new GetUserDeviceInfo(ConfigUtil.GET_TIME(), info.getDevice_num()));
        Map map = new HashMap();
        map.put("device_num", info.getDevice_num());
        map.put("time", ConfigUtil.GET_TIME());
        String sign = ConfigUtil.GET_SIGN(map);
        GetDeviceInfo(data, sign);
    }

    @OnClick({R.id.drop_down_iv, R.id.close_sumTime_view, R.id.maintenance_bt})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.drop_down_iv:
                pop.showAtLocation(view, Gravity.CENTER, 0, 0);
                break;
            case R.id.close_sumTime_view:
                sumTimeLayout.setVisibility(View.INVISIBLE);
                break;
            case R.id.maintenance_bt:
                new AlertDialog.Builder(getActivity())
                        .setTitle("提示")
                        .setMessage("确认完成维保？")
                        .setCancelable(false)
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                sumTimeLayout.setVisibility(View.INVISIBLE);
                            }
                        })
                        .setNegativeButton("取消", null).create().show();

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
        popListContent = view.findViewById(R.id.listContent);
        View rewardView = view.findViewById(R.id.pop_reward_view);
        ImageView drop_down_iv = view.findViewById(R.id.drop_down_iv);
        popDeviceNum = view.findViewById(R.id.device_num_tv);
        final EditText searchEt = view.findViewById(R.id.search_et);
        if (info != null)
            popDeviceNum.setText(info.getDevice_num());
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
        rewardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
        //键盘搜索事件
        searchEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//* 执行操作
                    searchKey = searchEt.getText().toString().trim();
                    isSearch = true;
                    getUserDeviceInfoData();
                    return true;
                }
                return false;
            }
        });
        popListContent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                dialog.show();
                DeviceVo.DataBean.InfoBean infoVo = popAdapter.getAll().get(position);
                info = infoVo;
                popDeviceNum.setText(info.getDevice_num());
                deviceNumTv.setText(info.getDevice_num());
                getDeviceInfoData();
                pop.dismiss();
            }
        });
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
                                            deviceVo = gson.fromJson(response, DeviceVo.class);
                                            if (deviceVo.getData() != null && deviceVo.getData().getInfo() != null)
                                                if (deviceVo.getData().getInfo().size() > 0) {
                                                    info = deviceVo.getData().getInfo().get(0);
                                                    deviceNumTv.setText(info.getDevice_num());
                                                    ConfigUtil.deviceNum = info.getDevice_num();
                                                    ConfigUtil.infoBeans = deviceVo.getData().getInfo();
                                                    //pop
                                                    popDeviceNum.setText(info.getDevice_num());
                                                    popAdapter = new PopDeviceAdapter(getActivity(), deviceVo.getData().getInfo());
                                                    popListContent.setAdapter(popAdapter);
                                                    //详情
                                                    dialog.show();
                                                    getDeviceInfoData();
                                                } else if (isSearch) {//搜索无设备的话提示Toast
                                                    ToastUtil.showToast("搜索无该设备");
                                                    isSearch = false;
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

    //用户获取设备信息
    private void GetDeviceInfo(String data, String sign) {
        OkHttpUtils
                .post()
                .url(ConfigUtil.IP)
                .addParams("service", "DeviceInfo.GetDeviceInfo")
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
                                            DeviceInfoVo deviceInfoVo = gson.fromJson(response, DeviceInfoVo.class);
                                            if (deviceInfoVo.getData() != null && deviceInfoVo.getData().getInfo() != null)
                                                if (deviceInfoVo.getData().getInfo().size() > 0) {
                                                    DeviceInfoVo.DataBean.InfoBean info = deviceInfoVo.getData().getInfo().get(0);
                                                    setXmlViewData(info);
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

        public GetUserDeviceInfo(String time, String device_num) {
            this.time = time;
            this.device_num = device_num;
        }
    }

    //设置页面数据
    public void setXmlViewData(DeviceInfoVo.DataBean.InfoBean info) {
        if (info != null) {
            workTimeTv.setText(info.getWork_time() + "h");
            if (info.getStartTime() != null) {
                String[] time = info.getStartTime().split(" ");
                if (time.length > 0 && time.length == 2) {
                    startTimeTimeTv.setText(time[1]);
                    startTimeDateTv.setText(time[0]);
                }
            }
            countmakenumTv.setText(info.getCountMakeNum() + "");
            ambienttemperatureTv.setText(info.getAmbienttemperature() != null ? info.getAmbienttemperature() : "-");
            ambienthumidityTv.setText(info.getAmbienthumidity() != null ? info.getAmbienthumidity() + "%" : "-");
            if (info.getSumTime() > 0) {
                sumTimeLayout.setVisibility(View.VISIBLE);
                maintenanceTimeTv.setText("距离下次维保还有" + (5000 - info.getSumTime()) + "小时");
            } else
                sumTimeLayout.setVisibility(View.INVISIBLE);
            sumTimeTv.setText(info.getSumTime() + "");
            makenumTv.setText(info.getCountmakenum() != null ? info.getCountmakenum() : "-");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
