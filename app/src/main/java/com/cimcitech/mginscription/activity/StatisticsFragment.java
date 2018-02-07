package com.cimcitech.mginscription.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.adapter.PopupWindowAdapter;
import com.cimcitech.mginscription.model.DeviceDSumInfoVo;
import com.cimcitech.mginscription.model.ResultVo;
import com.cimcitech.mginscription.model.StatisticsByDayVo;
import com.cimcitech.mginscription.utils.ConfigUtil;
import com.cimcitech.mginscription.utils.MyActivityManager;
import com.cimcitech.mginscription.utils.ToastUtil;
import com.cimcitech.mginscription.widget.CustomScrollView;
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
import lecho.lib.hellocharts.model.Viewport;
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
    @BindView(R.id.sumMakeNum_tv)
    TextView sumMakeNumTv;
    @BindView(R.id.productivity_tv)
    TextView productivityTv;
    @BindView(R.id.scrollView)
    CustomScrollView scrollView;

    private Unbinder unbinder;
    private ColumnChartData columnData;
    private ShapeLoadingDialog dialog;
    private PopupWindow pop;
    private Handler uiHandler = null;
    private final int REQUEST_RESULT = 1000;
    private StatisticsByDayVo.DataBean.InfoBean infoBean;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        dialog = new ShapeLoadingDialog(getActivity());
        dialog.setLoadingText("正在加载...");
        initHandler();
        initView();
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("StatisticsFragment", "onStart......");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("StatisticsFragment", "onResume......");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.e("StatisticsFragment", "onPause......");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.e("StatisticsFragment", "onStop......");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("StatisticsFragment", "onDestroy......");
    }

    public void initView() {
        MyActivityManager manager = MyActivityManager.getInstance();
        manager.pushOneActivity(getActivity());
        getStatisticsByDayData();//获取柱状图的数据
        scrollView.setOnRefreshListener(new setPullRefreshListener());
        deviceNumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!deviceNumTv.getText().toString().trim().equals("")) //判断一下防止奔溃
                    pop.showAtLocation(view, Gravity.CENTER, 0, 0);
            }
        });
    }

    public void initHandler() {
        uiHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case REQUEST_RESULT:// 显示加载中....
                        getStatisticsByDayData();
                        break;
                }
                return false;
            }
        });
    }

    class setPullRefreshListener implements CustomScrollView.OnRefreshListener {

        @Override
        public void onRefresh() {
            new MyTask().execute();//下拉刷新
        }
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

    private void getDeviceDSumInfoData(String deviceNum) {
        dialog.show();
        String data = new Gson().toJson(new GetDeviceDSumInfo(ConfigUtil.deviceNum, ConfigUtil.GET_TIME()));
        Map map = new HashMap();
        map.put("time", ConfigUtil.GET_TIME());
        map.put("device_num", deviceNum);
        String sign = ConfigUtil.GET_SIGN(map);
        GetDeviceDSumInfo(data, sign);
    }

    private void getStatisticsByDayData() {
        dialog.show();
        String data = new Gson().toJson(new StatisticsByDay("20", "1", ConfigUtil.GET_TIME()));
        Map map = new HashMap();
        map.put("time", ConfigUtil.GET_TIME());
        map.put("num", "20");
        map.put("start", "1");
        String sign = ConfigUtil.GET_SIGN(map);
        GetStatisticsByDay(data, sign);
    }

    //用户获取单个设备的总的生产数量和总的运行时间信息
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

    class GetDeviceDSumInfo {
        String device_num;
        String time;

        public GetDeviceDSumInfo(String device_num, String time) {
            this.device_num = device_num;
            this.time = time;
        }
    }

    private void generateColumnData(List<StatisticsByDayVo.DataBean.InfoBean> infoBeans) {
        int numSubcolumns = 2;
        int numColumns = infoBeans.size();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                if (j == 0)
                    values.add(new SubcolumnValue(infoBeans.get(i).getCountmakenum() != null ?
                            Float.parseFloat(infoBeans.get(i).getCountmakenum()) : 0,
                            Color.parseColor("#83bde5")));
                else
                    values.add(new SubcolumnValue((float) infoBeans.get(i).getRun_time(),
                            Color.parseColor("#afe19c")));
            }
            axisValues.add(new AxisValue(i).setLabel(infoBeans.get(i).getDev_num()));
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }
        columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        //设置轴标签的最大字符数，最小值0，最大值32。
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(5));//0~10000
        chart.setColumnChartData(columnData);
        chart.setValueSelectionEnabled(true);
        chart.setZoomType(ZoomType.HORIZONTAL);
        //设置他的缩放级别
        Viewport tempViewport = new Viewport(0, chart.getMaximumViewport().height(), 5, 0);
        chart.setOnTouchListener(touchListener);
        chart.setCurrentViewport(tempViewport);
    }

    View.OnTouchListener touchListener = new View.OnTouchListener() {
        float ratio = 1.8f; //水平和竖直方向滑动的灵敏度,偏大是水平方向灵敏
        float x0 = 0f;
        float y0 = 0f;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x0 = event.getX();
                    y0 = event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:
                    float dx = Math.abs(event.getX() - x0);
                    float dy = Math.abs(event.getY() - y0);
                    x0 = event.getX();
                    y0 = event.getY();
                    scrollView.requestDisallowInterceptTouchEvent(dx * ratio > dy);
                    break;
            }
            return false;
        }
    };

    @SuppressLint("SetTextI18n")
    private void showContactUsPopWin(Context context, final List<StatisticsByDayVo.DataBean.InfoBean> infoBeans) {
        LayoutInflater inflater = LayoutInflater.from(context);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.dialog_client_view, null);
        view.getBackground().setAlpha(100);
        // 创建PopupWindow对象
        pop = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT, false);
        View pop_reward_view = view.findViewById(R.id.pop_reward_view);
        final PopupWindowAdapter adapter = new PopupWindowAdapter(context, infoBeans);
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
                StatisticsByDayVo.DataBean.InfoBean infoBean = adapter.getAll().get(i);
                deviceNumTv.setText(infoBean.getDev_num());
                sumTimeTv.setText(infoBean.getSumTime() + "");
                sumMakeNumTv.setText(infoBean.getCountMakeNum() + "");
                productivityTv.setText(infoBean.getProductivity() + "");
                //ConfigUtil.deviceNum = infoBean.getDev_num();
                pop.dismiss();
            }
        });
    }

    //获取设备的日排名（产量），用于柱形图显示
    private void GetStatisticsByDay(String data, String sign) {
        OkHttpUtils
                .post()
                .url(ConfigUtil.IP)
                .addParams("service", "DeviceInfo.StatisticsByDay")
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

                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onResponse(String response, int id) {
                                Gson gson = new Gson();
                                ResultVo resultVo = gson.fromJson(response, ResultVo.class);
                                dialog.dismiss();
                                if (resultVo.getData().getCode() == 1) {//正常返回
                                    StatisticsByDayVo dayVo = gson.fromJson(response, StatisticsByDayVo.class);
                                    if (dayVo.getData() != null && dayVo.getData().getInfo().size() > 0) {
                                        generateColumnData(dayVo.getData().getInfo());
                                        showContactUsPopWin(getActivity(), dayVo.getData().getInfo());
                                        if (!ConfigUtil.deviceNum.equals("")) {
                                            for (int i = 0; i < dayVo.getData().getInfo().size(); i++)
                                                if (dayVo.getData().getInfo().get(i).getDev_num().equals(ConfigUtil.deviceNum))
                                                    infoBean = dayVo.getData().getInfo().get(i);
                                            if (infoBean == null) //如果equals没有还是给默认第一个，以免报错
                                                infoBean = dayVo.getData().getInfo().get(0);//默认的统计
                                        } else
                                            infoBean = dayVo.getData().getInfo().get(0);//默认的统计
                                        deviceNumTv.setText(infoBean.getDev_num());
                                        sumTimeTv.setText(infoBean.getSumTime() + "");
                                        sumMakeNumTv.setText(infoBean.getCountMakeNum() + "");
                                        productivityTv.setText(infoBean.getProductivity() + "");

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

    class StatisticsByDay {
        String num;
        String start;
        String time;

        public StatisticsByDay(String num, String start, String time) {
            this.num = num;
            this.start = start;
            this.time = time;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
