package com.cimcitech.mginscription.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.model.ResultVo;
import com.cimcitech.mginscription.model.UserInfoVo;
import com.cimcitech.mginscription.utils.ConfigUtil;
import com.cimcitech.mginscription.utils.ToastUtil;
import com.cimcitech.mginscription.widget.ShapeLoadingDialog;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.back_ib)
    ImageButton backIb;
    @BindView(R.id.right_ib)
    ImageButton rightIb;
    @BindView(R.id.username_tv)
    TextView usernameTv;
    @BindView(R.id.nickname_tv)
    TextView nicknameTv;
    @BindView(R.id.user_password_tv)
    TextView userPasswordTv;
    @BindView(R.id.device_number_tv)
    TextView deviceNumberTv;
    @BindView(R.id.about_view)
    LinearLayout aboutView;
    @BindView(R.id.update_version_tv)
    TextView updateVersionTv;
    @BindView(R.id.out_login_bt)
    Button outLoginBt;
    private UserInfoVo userInfoVo;
    private PopupWindow popEditText;
    private ShapeLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        dialog = new ShapeLoadingDialog(SettingActivity.this);
        dialog.setLoadingText("正在加载数据...");
        showEditMobilePopWin();
        userInfoVo = (UserInfoVo) this.getIntent().getSerializableExtra("userInfoVo");
        usernameTv.setText(ConfigUtil.userName);
        updateVersionTv.setText("当前版本号：" + getAppVersionName(this));
        if (userInfoVo != null) {
            nicknameTv.setText(userInfoVo.getData().getInfo().getRegister_name() != null ?
                    userInfoVo.getData().getInfo().getRegister_name() : "未设置");
            deviceNumberTv.setText(userInfoVo.getData().getInfo().getRegister_device_number() != null ?
                    userInfoVo.getData().getInfo().getRegister_device_number() : "0");
        }
    }

    @OnClick({R.id.back_ib, R.id.out_login_bt, R.id.user_password_tv, R.id.nickname_tv})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back_ib:
                finish();
                break;
            case R.id.out_login_bt:
                startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                ConfigUtil.isLogin = false;
                finish();
                break;
            case R.id.user_password_tv:
                startActivity(new Intent(SettingActivity.this, UpdatePasswordActivity.class));
                break;
            case R.id.nickname_tv:
                popEditText.showAtLocation(view, Gravity.CENTER, 0, 0);
                break;
        }
    }

    /**
     * 返回当前程序版本名
     */
    public static String getAppVersionName(Context context) {
        String versionName = "";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0)
                return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }

    private void showEditMobilePopWin() {
        LayoutInflater inflater = LayoutInflater.from(this);
        // 引入窗口配置文件
        View view = inflater.inflate(R.layout.dialog_edit_text_view, null);
        view.getBackground().setAlpha(100);
        // 创建PopupWindow对象
        popEditText = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, false);
        View pop_reward_view = view.findViewById(R.id.pop_reward_view);
        final EditText mobile_et = view.findViewById(R.id.mobile_et);
        TextView submit_tv = view.findViewById(R.id.submit_tv);
        // 需要设置一下此参数，点击外边可消失
        popEditText.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        popEditText.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        popEditText.setFocusable(true);
        pop_reward_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popEditText.dismiss();
            }
        });
        submit_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile_et.getText().toString().trim().equals("")) {
                    ToastUtil.showToast("请输入昵称");
                } else {
                    dialog.show();
                    String data = new Gson().toJson(new EditUserInfo(mobile_et.getText().toString().trim(), ConfigUtil.GET_TIME()));
                    Map map = new HashMap();
                    map.put("register_name", mobile_et.getText().toString().trim());
                    map.put("time", ConfigUtil.GET_TIME());
                    String sign = ConfigUtil.GET_SIGN(map);
                    getEditUserInfo(data, sign);
                    nicknameTv.setText(mobile_et.getText().toString().trim());
                    popEditText.dismiss();
                }
            }
        });
    }

    //获取用户详细信息
    private void getEditUserInfo(String data, String sign) {
        OkHttpUtils
                .post()
                .url(ConfigUtil.IP)
                .addParams("service", "Users.EditUserInfo")
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

                                        } else if (resultVo.getData().getCode() == 2) {//登录超时
                                            ToastUtil.showToast("登录超时，请重新登录");
                                            ConfigUtil.isLogin = false;
                                            startActivity(new Intent(SettingActivity.this, LoginActivity.class));
                                        } else
                                            ToastUtil.showToast(resultVo.getData().getReturnmsg());
                            }
                        }
                );
    }

    //修改用户信息
    class EditUserInfo {
        String register_name;
        String time;


        public EditUserInfo(String register_name, String time) {
            this.register_name = register_name;
            this.time = time;
        }
    }
}
