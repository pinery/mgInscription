package com.cimcitech.mginscription.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cimcitech.mginscription.R;
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

    private Unbinder unbinder;
    private ColumnChartData columnData;
    private ShapeLoadingDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        dialog = new ShapeLoadingDialog(getActivity());
        dialog.setLoadingText("正在加载...");
        if (ConfigUtil.deviceNum != null)
            getDeviceDSumInfoData();
        if (ConfigUtil.infoBeans != null && ConfigUtil.infoBeans.size() > 0)
            generateColumnData(ConfigUtil.infoBeans);
        return view;
    }

    public void getDeviceDSumInfoData() {
        dialog.show();
        String data = new Gson().toJson(new GetDeviceDSumInfo(ConfigUtil.deviceNum, ConfigUtil.GET_TIME()));
        Map map = new HashMap();
        map.put("time", ConfigUtil.GET_TIME());
        map.put("device_num", ConfigUtil.deviceNum);
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
