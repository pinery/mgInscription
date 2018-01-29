package com.cimcitech.mginscription.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.model.ResultVo;
import com.cimcitech.mginscription.utils.ConfigUtil;
import com.cimcitech.mginscription.utils.MyActivityManager;
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

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.right_ib)
    ImageButton rightIb;
    @BindView(R.id.user_name_et)
    EditText userNameEt;
    @BindView(R.id.code_et)
    EditText codeEt;
    @BindView(R.id.code_bt)
    Button codeBt;
    @BindView(R.id.user_password_et)
    EditText userPasswordEt;
    @BindView(R.id.confirm_password_et)
    EditText confirmPasswordEt;
    @BindView(R.id.submit_bt)
    Button submitBt;
    @BindView(R.id.login_tv)
    TextView loginTv;
    @BindView(R.id.back_tv)
    TextView backTv;
    private SharedPreferences sp;
    private ShapeLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        MyActivityManager manager = MyActivityManager.getInstance();
        manager.pushOneActivity(this);
        sp = this.getSharedPreferences(ConfigUtil.KEY_LOGIN_AUTO, MODE_PRIVATE);
        dialog = new ShapeLoadingDialog(RegisterActivity.this);
        dialog.setLoadingText("正在加载...");
    }

    @OnClick({R.id.login_tv, R.id.back_tv, R.id.submit_bt, R.id.code_bt})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.login_tv:
                finish();
                break;
            case R.id.back_tv:
                finish();
                break;
            case R.id.submit_bt:
                if (!checkInput()) return;
                dialog.show();
                String username = userNameEt.getText().toString().trim();
                String password = ConfigUtil.GET_PASSWORD(userPasswordEt.getText().toString().trim());
                String time = ConfigUtil.GET_TIME();
                String code = codeEt.getText().toString().trim();
                String data = new Gson().toJson(new Request(username, password, time, code));
                Map map = new HashMap();
                map.put("register_phone", username);
                map.put("register_password", password);
                map.put("time", time);
                map.put("code", code);
                String sign = ConfigUtil.GET_SIGN(map);
                this.register(data, sign);
                break;
            case R.id.code_bt:
                getSendMsgData();
                break;
        }
    }

    public void getSendMsgData() {
        if (!userNameEt.getText().toString().trim().equals("")) {
            dialog.show();
            String username = userNameEt.getText().toString().trim();
            String time = ConfigUtil.GET_TIME();
            String data = new Gson().toJson(new SendMsg(username, time));
            Map map = new HashMap();
            map.put("phoneNumber", username);
            map.put("time", time);
            String sign = ConfigUtil.GET_SIGN(map);
            getSendMsg(data, sign);
        }
    }

    private boolean checkInput() {
        if (userNameEt.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入手机号码");
            return false;
        }
        if (userNameEt.getText().toString().trim().length() != 11) {
            ToastUtil.showToast("请输入正确的手机号码");
            return false;
        }
        if (codeEt.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入验证码");
            return false;
        }
        if (codeEt.getText().toString().trim().length() != 6) {
            ToastUtil.showToast("请输入正确的验证码");
            return false;
        }
        if (userPasswordEt.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入密码");
            return false;
        }
        if (userPasswordEt.getText().toString().trim().length() < 6) {
            ToastUtil.showToast("密码长度必须6位以上");
            return false;
        }
        if (!isContainAll(userPasswordEt.getText().toString().trim())) {
            ToastUtil.showToast("密码至少一个大写字母和小写字母、数字组成");
            return false;
        }
        if (confirmPasswordEt.getText().toString().trim().compareTo(userPasswordEt.getText().toString().trim()) != 0) {
            ToastUtil.showToast("两次密码输入不一致，请重新输入");
            return false;
        }
        return true;
    }

    /**
     * 规则3：必须同时包含大小写字母及数字
     * 是否包含
     *
     * @param str
     * @return
     */
    public static boolean isContainAll(String str) {
        boolean isDigit = false;//定义一个boolean值，用来表示是否包含数字
        boolean isLowerCase = false;//定义一个boolean值，用来表示是否包含字母
        boolean isUpperCase = false;
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {   //用char包装类中的判断数字的方法判断每一个字符
                isDigit = true;
            } else if (Character.isLowerCase(str.charAt(i))) {  //用char包装类中的判断字母的方法判断每一个字符
                isLowerCase = true;
            } else if (Character.isUpperCase(str.charAt(i))) {
                isUpperCase = true;
            }
        }
        String regex = "^[a-zA-Z0-9]+$";
        boolean isRight = isDigit && isLowerCase && isUpperCase && str.matches(regex);
        return isRight;
    }

    //注册
    private void register(String data, String sign) {
        OkHttpUtils
                .post()
                .url(ConfigUtil.IP)
                .addParams("service", "Users.Register")
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
                                            ToastUtil.showToast("注册成功");
                                            saveUserInfo();
                                            finish();
                                        } else
                                            ToastUtil.showToast(resultVo.getData().getReturnmsg());
                            }
                        }
                );
    }


    private void saveUserInfo() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", userNameEt.getText().toString().trim());
        editor.putString("password", userPasswordEt.getText().toString().trim());
        editor.commit();
    }

    class Request {
        String register_phone;
        String register_password;
        String time;
        String code;


        public Request(String register_phone, String register_password, String time, String code) {
            this.register_phone = register_phone;
            this.register_password = register_password;
            this.time = time;
            this.code = code;
        }
    }

    //获取验证码
    private void getSendMsg(String data, String sign) {
        OkHttpUtils
                .post()
                .url(ConfigUtil.IP)
                .addParams("service", "SendMsg.SendMsg")
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
                                            ToastUtil.showToast("验证码已发送");
                                        } else
                                            ToastUtil.showToast(resultVo.getData().getReturnmsg());
                            }
                        }
                );
    }

    class SendMsg {
        String phoneNumber;
        String time;

        public SendMsg(String phoneNumber, String time) {
            this.phoneNumber = phoneNumber;
            this.time = time;
        }
    }
}
