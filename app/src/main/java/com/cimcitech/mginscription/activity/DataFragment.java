package com.cimcitech.mginscription.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.adapter.DataAxisXAdapter;
import com.cimcitech.mginscription.adapter.DataAxisYAdapter;
import com.cimcitech.mginscription.adapter.DataRegisterCAdapter;
import com.cimcitech.mginscription.adapter.DataRegisterDAdapter;
import com.cimcitech.mginscription.model.DeviceRegisterInfo1Vo;
import com.cimcitech.mginscription.model.DeviceRegisterInfo2Vo;
import com.cimcitech.mginscription.model.ResultVo;
import com.cimcitech.mginscription.utils.ConfigUtil;
import com.cimcitech.mginscription.utils.ToastUtil;
import com.cimcitech.mginscription.widget.CustomScrollView;
import com.cimcitech.mginscription.widget.MyGridView;
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

public class DataFragment extends Fragment {

    @BindView(R.id.axis_tv)
    TextView axisTv;
    @BindView(R.id.axis_view)
    RelativeLayout axisView;
    @BindView(R.id.register_tv)
    TextView registerTv;
    @BindView(R.id.register_view)
    RelativeLayout registerView;
    @BindView(R.id.axis_line_iv)
    ImageView axisLineIv;
    @BindView(R.id.register_line_iv)
    ImageView registerLineIv;
    @BindView(R.id.scrollView)
    CustomScrollView scrollView;
    @BindView(R.id.axis_x_grid)
    MyGridView axisXGrid;
    @BindView(R.id.axis_y_grid)
    MyGridView axisYGrid;
    @BindView(R.id.register_c_grid)
    MyGridView registerCGrid;
    @BindView(R.id.register_d_grid)
    MyGridView registerDGrid;

    private Handler uiHandler = null;
    private final int REQUEST_RESULT = 1000;
    private Unbinder unbinder;
    private View statisticsLeftAxisView, statisticsRightRegisterView;
    private ShapeLoadingDialog dialog;
    private int type = 2;//1表示寄存器，2表示轴状态
    private DataAxisXAdapter xAdapter;
    private DataAxisYAdapter yAdapter;
    private DataRegisterCAdapter cAdapter;
    private DataRegisterDAdapter dAdapter;
    private List<List<String>> x;
    private List<List<String>> y;
    private List<List<String>> c;
    private List<List<String>> d;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        statisticsLeftAxisView = view.findViewById(R.id.statistics_left_axis_view);
        statisticsRightRegisterView = view.findViewById(R.id.statistics_right_register_view);
        initHandler();
        initView();
        return view;
    }


    private void initHandler() {
        uiHandler = new Handler() {
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case REQUEST_RESULT:// 显示加载中....
                        getDeviceRegisterInfoData();
                        break;
                }
            }
        };
    }

    public void initView() {
        scrollView.setOnRefreshListener(new setPullRefreshListener());
        dialog = new ShapeLoadingDialog(getActivity());
        dialog.setLoadingText("正在加载...");
        getDeviceRegisterInfoData();
    }

    class setPullRefreshListener implements CustomScrollView.OnRefreshListener {

        @Override
        public void onRefresh() {
            new AsyncTask<Void, Void, Void>() {

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
            }.execute();
        }
    }

    @OnClick({R.id.axis_view, R.id.register_view})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.axis_view:
                type = 2;
                axisLineIv.setVisibility(View.VISIBLE);
                registerLineIv.setVisibility(View.INVISIBLE);
                statisticsLeftAxisView.setVisibility(View.VISIBLE);
                statisticsRightRegisterView.setVisibility(View.GONE);
                getDeviceRegisterInfoData();
                break;
            case R.id.register_view:
                type = 1;
                axisLineIv.setVisibility(View.INVISIBLE);
                registerLineIv.setVisibility(View.VISIBLE);
                statisticsLeftAxisView.setVisibility(View.GONE);
                statisticsRightRegisterView.setVisibility(View.VISIBLE);
                getDeviceRegisterInfoData();
                break;
        }
    }

    public void getDeviceRegisterInfoData() {
        dialog.show();
        String data = new Gson().toJson(new GetDeviceRegisterInfo("20", "1",
                ConfigUtil.GET_TIME(), ConfigUtil.deviceNum, type));
        Map map = new HashMap();
        map.put("num", "20");
        map.put("start", "1");
        map.put("device_num", ConfigUtil.deviceNum != null ? ConfigUtil.deviceNum : "");
        map.put("type", type);
        map.put("time", ConfigUtil.GET_TIME());
        String sign = ConfigUtil.GET_SIGN(map);
        getDeviceRegisterInfo(data, sign);
    }

    private void getDeviceRegisterInfo(String data, String sign) {
        OkHttpUtils
                .post()
                .url(ConfigUtil.IP)
                .addParams("service", "DeviceInfo.GetDeviceRegisterInfo")
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
                                if (resultVo.getData().getCode() == 1) {//正常返回
                                    if (type == 2) {
                                        DeviceRegisterInfo1Vo info1Vo = gson.fromJson(response, DeviceRegisterInfo1Vo.class);
                                        if (info1Vo != null && info1Vo.getData() != null && info1Vo.getData().getInfo() != null)
                                            if (info1Vo.getData().getInfo().size() > 0) {
                                                for (int i = 0; i < info1Vo.getData().getInfo().size(); i++) {
                                                    x = info1Vo.getData().getInfo().get(i).getX();
                                                    y = info1Vo.getData().getInfo().get(i).getY();
                                                }
                                                if (x != null && x.size() > 0)
                                                    initAxisXData(x);
                                                if (y != null && y.size() > 0)
                                                    initAxisYData(y);
                                            }
                                    } else if (type == 1) {
                                        DeviceRegisterInfo2Vo info2Vo = gson.fromJson(response, DeviceRegisterInfo2Vo.class);
                                        if (info2Vo.getData().getInfo().size() > 0) {
                                            for (int i = 0; i < info2Vo.getData().getInfo().size(); i++) {
                                                c = info2Vo.getData().getInfo().get(i).getC();
                                                d = info2Vo.getData().getInfo().get(i).getD();
                                            }
                                            if (c != null && c.size() > 0)
                                                initRegisterCData(c);
                                            if (d != null && d.size() > 0)
                                                initRegisterDData(d);
                                        }
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

    public void initAxisXData(List<List<String>> x) {
        xAdapter = new DataAxisXAdapter(getActivity(), x);
        axisXGrid.setAdapter(xAdapter);
        axisXGrid.setSelector(R.drawable.hide_listview_yellow_selector);
    }

    public void initAxisYData(List<List<String>> y) {
        yAdapter = new DataAxisYAdapter(getActivity(), y);
        axisYGrid.setAdapter(yAdapter);
        axisYGrid.setSelector(R.drawable.hide_listview_yellow_selector);
    }

    public void initRegisterCData(List<List<String>> c) {
        cAdapter = new DataRegisterCAdapter(getActivity(), c);
        registerCGrid.setAdapter(cAdapter);
        registerCGrid.setSelector(R.drawable.hide_listview_yellow_selector);
    }

    public void initRegisterDData(List<List<String>> d) {
        dAdapter = new DataRegisterDAdapter(getActivity(), d);
        registerDGrid.setAdapter(dAdapter);
        registerDGrid.setSelector(R.drawable.hide_listview_yellow_selector);
    }

    class GetDeviceRegisterInfo {
        String num;
        String start;
        String time;
        String device_num;
        int type;

        public GetDeviceRegisterInfo(String num, String start, String time, String device_num, int type) {
            this.num = num;
            this.start = start;
            this.time = time;
            this.device_num = device_num;
            this.type = type;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
