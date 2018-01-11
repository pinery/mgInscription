package com.cimcitech.mginscription.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cimcitech.mginscription.MainActivity;
import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.model.LoginVo;
import com.cimcitech.mginscription.model.ResultVo;
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

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.user_name_et)
    EditText userNameEt;
    @BindView(R.id.user_password_et)
    EditText userPasswordEt;
    @BindView(R.id.submit_bt)
    Button submitBt;
    @BindView(R.id.register_tv)
    TextView registerTv;
    @BindView(R.id.update_password_tv)
    TextView updatePasswordTv;
    private SharedPreferences sp;
    private ShapeLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        sp = this.getSharedPreferences(ConfigUtil.KEY_LOGIN_AUTO, MODE_PRIVATE);
        dialog = new ShapeLoadingDialog(LoginActivity.this);
        dialog.setLoadingText("正在登陆...");
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
    }

    private boolean checkInput() {
        if (userNameEt.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入用户名");
            return false;
        }
        if (userPasswordEt.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入密码");
            return false;
        }
        return true;
    }

    @OnClick({R.id.submit_bt, R.id.register_tv, R.id.update_password_tv})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.submit_bt:
                if (!checkInput()) return;
                dialog.show();
                String username = userNameEt.getText().toString().trim();
                String password = ConfigUtil.GET_PASSWORD(userPasswordEt.getText().toString().trim());
                String time = ConfigUtil.GET_TIME();
                String data = new Gson().toJson(new Request(username, password, time));
                Map map = new HashMap();
                map.put("register_phone", username);
                map.put("register_password", password);
                map.put("time", time);
                String sign = ConfigUtil.GET_SIGN(map);
                login(data, sign);
                break;
            case R.id.register_tv:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.update_password_tv:
                startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
                break;
        }
    }

    //登录
    private void login(String data, String sign) {
        OkHttpUtils
                .post()
                .url(ConfigUtil.IP)
                .addParams("service", "Users.Login")
                .addParams("data", data)
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
                                            LoginVo loginVo = gson.fromJson(response, LoginVo.class);
                                            ToastUtil.showToast("登录成功");
                                            saveUserInfo();
                                            ConfigUtil.userName = userNameEt.getText().toString().trim();
                                            ConfigUtil.isLogin = true;
                                            ConfigUtil.loginInfo = loginVo.getData().getInfo();
                                            if (ConfigUtil.isOutLogin) {
                                                ConfigUtil.isOutLogin = false;
                                            } else
                                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                            finish();
                                        } else
                                            ToastUtil.showToast(resultVo.getData().getReturnmsg());

                            }
                        }
                );
    }

    class Request {
        String register_phone;
        String register_password;
        String time;

        public Request(String register_phone, String register_password, String time) {
            this.register_phone = register_phone;
            this.register_password = register_password;
            this.time = time;
        }
    }

    private void getUserInfo() {
        if (sp.getString("username", "") != "") {
            String phone = sp.getString("username", "");
            String pwd = sp.getString("password", "");
            userNameEt.setText(phone);
            userPasswordEt.setText(pwd);
        }
    }

    private void saveUserInfo() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", userNameEt.getText().toString().trim());
        editor.putString("password", userPasswordEt.getText().toString().trim());
        editor.commit();
    }

}
