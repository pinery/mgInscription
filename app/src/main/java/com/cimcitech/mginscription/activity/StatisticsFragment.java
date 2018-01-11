package com.cimcitech.mginscription.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.adapter.PopupWindowAdapter;
import com.cimcitech.mginscription.model.DeviceDSumInfoVo;
import com.cimcitech.mginscription.model.DeviceVo;
import com.cimcitech.mginscription.model.ResultVo;
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
import butterknife.Unbinder;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;
import okhttp3.Call;

/**
 * 统计
 * Created by dapineapple on 2017/12/27.
 */

public class StatisticsFragment extends Fragment {

    @BindView(R.id.chart)
    ColumnChartView chart;
    @BindView(R.id.device_num_tv)
    TextView deviceNumTv;
    @BindView(R.id.sumTime_tv)
    TextView sumTimeTv;
    @BindView(R.id.countMakeNum_tv)
    TextView countMakeNumTv;

    private Unbinder unbinder;
    private ColumnChartData columnData;
    private ShapeLoadingDialog dialog;
    private PopupWindow pop;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        dialog = new ShapeLoadingDialog(getActivity());
        dialog.setLoadingText("正在加载...");
        initView();
        return view;
    }

    public void initView() {
        if (ConfigUtil.deviceNum != null) {
            deviceNumTv.setText(ConfigUtil.deviceNum);
            getDeviceDSumInfoData(ConfigUtil.deviceNum);
        }
        if (ConfigUtil.infoBeans != null && ConfigUtil.infoBeans.size() > 0) {
            generateColumnData(ConfigUtil.infoBeans);
            List<String> strings = new ArrayList<>();
            for (int i = 0; i < ConfigUtil.infoBeans.size(); i++)
                strings.add(ConfigUtil.infoBeans.get(i).getDevice_num());
            showContactUsPopWin(getActivity(), strings);
        }
        deviceNumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });
    }

    public void getDeviceDSumInfoData(String deviceNum) {
        dialog.show();
        String data = new Gson().toJson(new GetDeviceDSumInfo(ConfigUtil.deviceNum, ConfigUtil.GET_TIME()));
        Map map = new HashMap();
        map.put("time", ConfigUtil.GET_TIME());
        map.put("device_num", deviceNum);
        String sign = ConfigUtil.GET_SIGN(map);
        GetDeviceDSumInfo(data, sign);
    }

    private void GetDeviceDSumInfo(String data, String sign) {
        OkHttpUtils
                .post()
                .url(ConfigUtil.IP)
                .addParams("service", "DeviceInfo.GetDeviceDSumInfo")
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
                                    DeviceDSumInfoVo dSumInfoVo = gson.fromJson(response, DeviceDSumInfoVo.class);
                                    if (dSumInfoVo != null && dSumInfoVo.getData() != null && dSumInfoVo.getData().getInfo() != null) {
                                        sumTimeTv.setText(dSumInfoVo.getData().getInfo().getSumTime() + "");
                                        countMakeNumTv.setText(dSumInfoVo.getData().getInfo().getCountMakeNum() != null ?
                                                dSumInfoVo.getData().getInfo().getCountMakeNum().toString() : "-");
                                    }
                                } else if (resultVo.getData().getCode() == 2) {//登录超时
                                    ToastUtil.showToast("登录超时，请重新登录");
                                    ConfigUtil.isLogin = false;
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                } else
                                    ToastUtil.showToast(resultVo.getData().getReturnmsg());
                            }
                        }
                );
    }

    class GetDeviceDSumInfo {
        String device_num;
        String time;

        public GetDeviceDSumInfo(String device_num, String time) {
            this.device_num = device_num;
            this.time = time;
        }
    }

    private void generateColumnData(List<DeviceVo.DataBean.InfoBean> infoBeans) {
        int numSubcolumns = 2;
        int numColumns = infoBeans.size();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                if (j == 0)
                    values.add(new SubcolumnValue((float) 50, Color.parseColor("#83bde5")));
                else
                    values.add(new SubcolumnValue((float) 500, Color.parseColor("#afe19c")));
            }
            axisValues.add(new AxisValue(i).setLabel(infoBeans.get(i).getDevice_num()));
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }
        columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        //设置轴标签的最大字符数，最小值0，最大值32。
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));
        chart.setColumnChartData(columnData);
        chart.setValueSelectionEnabled(true);
        chart.setZoomType(ZoomType.HORIZONTAL);
    }

    public void showContactUsPopWin(Context context, final List<String> list) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.dialog_client_view, null);
        view.getBackground().setAlpha(100);
        // 创建PopupWindow对象
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);
        View pop_reward_view = view.findViewById(R.id.pop_reward_view);
        final PopupWindowAdapter adapter = new PopupWindowAdapter(context, list);
        ListView listView = view.findViewById(R.id.listContent);
        listView.setAdapter(adapter);
        // 需要设置一下此参数，点击外边可消失
        pop.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        pop.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        pop.setFocusable(true);
        pop_reward_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                deviceNumTv.setText(adapter.getAll().get(i));
                getDeviceDSumInfoData(adapter.getAll().get(i));
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
