package com.cimcitech.mginscription.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.model.ResultVo;
import com.cimcitech.mginscription.utils.ConfigUtil;
import com.cimcitech.mginscription.utils.MyActivityManager;
import com.cimcitech.mginscription.utils.ToastUtil;
import com.cimcitech.mginscription.widget.ShapeLoadingDialog;
import com.google.gson.Gson;
import com.uuzuche.lib_zxing.activity.CodeUtils;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class AddDeviceActivity extends AppCompatActivity {

    @BindView(R.id.back_ib)
    ImageButton backIb;
    @BindView(R.id.right_ib)
    ImageButton rightIb;
    @BindView(R.id.qr_code_view)
    RelativeLayout qrCodeView;
    @BindView(R.id.driver_number_et)
    EditText driverNumberEt;
    @BindView(R.id.close_iv)
    ImageView closeIv;
    @BindView(R.id.submit_bt)
    Button submitBt;
    @BindView(R.id.driver_IEMI_et)
    EditText driverIEMIEt;
    @BindView(R.id.close_IEMI_iv)
    ImageView closeIEMIIv;
    @BindView(R.id.not_device_view)
    LinearLayout notDeviceView;
    @BindView(R.id.device_num_tv)
    TextView deviceNumTv;
    @BindView(R.id.driver_IEMI_tv)
    TextView driverIEMITv;
    @BindView(R.id.have_device_view)
    RelativeLayout haveDeviceView;

    public static final int REQUEST_CODE = 1000;
    private ShapeLoadingDialog dialog;
    private String deviceNumber, deviceImei; //扫描获得的编号和IMEI号码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        MyActivityManager manager = MyActivityManager.getInstance();
        manager.pushOneActivity(this);
        dialog = new ShapeLoadingDialog(this);
        dialog.setLoadingText("正在加载数据...");
        driverNumberEt.addTextChangedListener(new editTextChangedListener());
        driverIEMIEt.addTextChangedListener(new editTextChangedListener());
    }

    class editTextChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (driverNumberEt.getText().toString().trim().equals(""))
                closeIv.setVisibility(View.INVISIBLE);
            else
                closeIv.setVisibility(View.VISIBLE);
            if (driverIEMIEt.getText().toString().trim().equals(""))
                closeIEMIIv.setVisibility(View.INVISIBLE);
            else
                closeIEMIIv.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) return;
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    if (!result.equals("")) {
                        String[] strings = result.split(" ");
                        if (!ConfigUtil.isEmpty(strings) && strings.length == 2) {
                            String[] str1 = strings[0].split(":");
                            String[] str2 = strings[1].split(":");
                            if (!ConfigUtil.isEmpty(str1) && str1.length == 2)
                                deviceNumber = str1[1];
                            if (!ConfigUtil.isEmpty(str2) && str2.length == 2)
                                deviceImei = str2[1];
                            if (!ConfigUtil.isEmpty(deviceNumber) && !ConfigUtil.isEmpty(deviceImei)) {
                                notDeviceView.setVisibility(View.INVISIBLE);
                                haveDeviceView.setVisibility(View.VISIBLE);
                                deviceNumTv.setText("终端编码：  " + deviceNumber);
                                driverIEMITv.setText("IMEI号码：  " + deviceImei);
                                getDeviceRegisterData();//注册设备了
                            } else {
                                notDeviceView.setVisibility(View.VISIBLE);
                                haveDeviceView.setVisibility(View.INVISIBLE);
                            }
                        } else
                            ToastUtil.showToast("无效二维码/条码，请重新扫描");
                    } else
                        ToastUtil.showToast("无效二维码/条码，请重新扫描");

                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(AddDeviceActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public boolean checkInput() {
        if (driverNumberEt.getText().toString().trim().equals("")) {
            ToastUtil.showToast("设备编号不能为空");
            return false;
        } else
            deviceNumber = driverNumberEt.getText().toString().trim();
        if (driverIEMIEt.getText().toString().trim().equals("")) {
            ToastUtil.showToast("IMEI号码不能为空");
            return false;
        } else
            deviceImei = driverIEMIEt.getText().toString().trim();
        return true;
    }

    public void getDeviceRegisterData() {
        dialog.show();
        String data = new Gson().toJson(new DeviceRegister(deviceNumber, deviceImei, ConfigUtil.GET_TIME()));
        Map map = new HashMap();
        map.put("device_num", deviceNumber);
        map.put("device_sn", deviceImei);
        map.put("time", ConfigUtil.GET_TIME());
        String sign = ConfigUtil.GET_SIGN(map);
        getDeviceRegister(data, sign);
    }

    //用户获取设备信息(批量，带搜索)
    private void getDeviceRegister(String data, String sign) {
        OkHttpUtils
                .post()
                .url(ConfigUtil.IP)
                .addParams("service", "UsersDevice.Register")
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
                                            ToastUtil.showToast("注册成功");
                                            ConfigUtil.isAddDevice = true;
                                            finish();
                                        } else if (resultVo.getData().getCode() == 2) {//登录超时
                                            ToastUtil.showToast("登录超时，请重新登录");
                                            ConfigUtil.isLogin = false;
                                            ConfigUtil.isOutLogin = true;
                                            startActivity(new Intent(AddDeviceActivity.this, LoginActivity.class));
                                        } else
                                            ToastUtil.showToast(resultVo.getData().getReturnmsg());


                            }
                        }
                );
    }

    class DeviceRegister {
        String device_num;
        String device_sn;
        String time;

        public DeviceRegister(String device_num, String device_sn, String time) {
            this.device_num = device_num;
            this.device_sn = device_sn;
            this.time = time;
        }
    }

    @OnClick({R.id.qr_code_view, R.id.back_ib, R.id.close_iv, R.id.close_IEMI_iv, R.id.submit_bt})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.qr_code_view:
                Intent intent = new Intent(AddDeviceActivity.this, SecondActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.back_ib:
                finish();
                break;
            case R.id.close_iv:
                driverNumberEt.setText("");
                break;
            case R.id.close_IEMI_iv:
                driverIEMIEt.setText("");
                break;
            case R.id.submit_bt:
                if (!checkInput()) return;
                getDeviceRegisterData();
                break;
        }
    }
}
