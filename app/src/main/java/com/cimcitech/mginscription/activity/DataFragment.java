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
import android.widget.ListView;
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
import com.cimcitech.mginscription.widget.ShapeLoadingDialog;
import com.cimcitech.mginscription.widget.Utility;
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

    @BindView(R.id.axis_x_tv)
    TextView axisXTv;
    @BindView(R.id.axis_x_line_iv)
    ImageView axisXLineIv;
    @BindView(R.id.axis_x_view)
    RelativeLayout axisXView;
    @BindView(R.id.axis_y_tv)
    TextView axisYTv;
    @BindView(R.id.axis_y_line_iv)
    ImageView axisYLineIv;
    @BindView(R.id.axis_y_view)
    RelativeLayout axisYView;
    @BindView(R.id.register_c_tv)
    TextView registerCTv;
    @BindView(R.id.register_c_line_iv)
    ImageView registerCLineIv;
    @BindView(R.id.register_c_view)
    RelativeLayout registerCView;
    @BindView(R.id.register_d_tv)
    TextView registerDTv;
    @BindView(R.id.register_d_line_iv)
    ImageView registerDLineIv;
    @BindView(R.id.register_d_view)
    RelativeLayout registerDView;
    @BindView(R.id.scrollView)
    CustomScrollView scrollView;
    @BindView(R.id.listContent_x)
    ListView listContentX;
    @BindView(R.id.listContent_y)
    ListView listContentY;
    @BindView(R.id.listContent_c)
    ListView listContentC;
    @BindView(R.id.listContent_d)
    ListView listContentD;

    private Handler uiHandler = null;
    private final int REQUEST_RESULT = 1000;
    private Unbinder unbinder;
    private View axisXLayout, axisYLayout, registerCLayout, registerDLayout;
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
        axisXLayout = view.findViewById(R.id.statistics_axis_x_view);
        axisYLayout = view.findViewById(R.id.statistics_axis_y_view);
        registerCLayout = view.findViewById(R.id.statistics_register_c_view);
        registerDLayout = view.findViewById(R.id.statistics_register_d_view);
        initHandler();
        initView();
        return view;
    }

    public void initHandler() {
        uiHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case REQUEST_RESULT:// 显示加载中....
                        getDeviceRegisterInfoData();
                        break;
                }
                return false;
            }
        });
    }

    public void initView() {
        scrollView.setOnRefreshListener(new setPullRefreshListener());
        dialog = new ShapeLoadingDialog(getActivity());
        dialog.setLoadingText("正在加载...");
        getDeviceRegisterInfoData();
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

    @OnClick({R.id.axis_x_view, R.id.axis_y_view, R.id.register_c_view, R.id.register_d_view})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.axis_x_view:
                showLineView(axisXLineIv, axisXLayout, 2);
                break;
            case R.id.axis_y_view:
                showLineView(axisYLineIv, axisYLayout, 2);
                break;
            case R.id.register_c_view:
                showLineView(registerCLineIv, registerCLayout, 1);
                break;
            case R.id.register_d_view:
                showLineView(registerDLineIv, registerDLayout, 1);
                break;
        }
    }

    public void showLineView(ImageView imageView, View view, int num) {
        axisXLineIv.setVisibility(View.INVISIBLE);
        axisYLineIv.setVisibility(View.INVISIBLE);
        registerCLineIv.setVisibility(View.INVISIBLE);
        registerDLineIv.setVisibility(View.INVISIBLE);
        imageView.setVisibility(View.VISIBLE);
        axisXLayout.setVisibility(View.GONE);
        axisYLayout.setVisibility(View.GONE);
        registerCLayout.setVisibility(View.GONE);
        registerDLayout.setVisibility(View.GONE);
        view.setVisibility(View.VISIBLE);
        if (type != num) {
            type = num;
            getDeviceRegisterInfoData();
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
        listContentX.setAdapter(xAdapter);
        new Utility().setListViewHeightBasedOnChildren(listContentX);
    }

    public void initAxisYData(List<List<String>> y) {
        yAdapter = new DataAxisYAdapter(getActivity(), y);
        listContentY.setAdapter(yAdapter);
        new Utility().setListViewHeightBasedOnChildren(listContentY);
    }

    public void initRegisterCData(List<List<String>> c) {
        cAdapter = new DataRegisterCAdapter(getActivity(), c);
        listContentC.setAdapter(cAdapter);
        new Utility().setListViewHeightBasedOnChildren(listContentC);
    }

    public void initRegisterDData(List<List<String>> d) {
        dAdapter = new DataRegisterDAdapter(getActivity(), d);
        listContentD.setAdapter(dAdapter);
        new Utility().setListViewHeightBasedOnChildren(listContentD);
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
