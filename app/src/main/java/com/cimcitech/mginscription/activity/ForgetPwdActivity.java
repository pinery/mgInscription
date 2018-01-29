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

public class ForgetPwdActivity extends AppCompatActivity {

    @BindView(R.id.back_tv)
    TextView backTv;
    @BindView(R.id.right_ib)
    ImageButton rightIb;
    @BindView(R.id.user_name_et)
    EditText userNameEt;
    @BindView(R.id.code_bt)
    Button codeBt;
    @BindView(R.id.new_pwd_et)
    EditText newPwdEt;
    @BindView(R.id.confirm_pwd_et)
    EditText confirmPwdEt;
    @BindView(R.id.submit_bt)
    Button submitBt;
    @BindView(R.id.code_et)
    EditText codeEt;
    private ShapeLoadingDialog dialog;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        MyActivityManager manager = MyActivityManager.getInstance();
        manager.pushOneActivity(this);
        sp = this.getSharedPreferences(ConfigUtil.KEY_LOGIN_AUTO, MODE_PRIVATE);
        dialog = new ShapeLoadingDialog(ForgetPwdActivity.this);
        dialog.setLoadingText("正在加载...");
    }

    @OnClick({R.id.back_tv, R.id.submit_bt, R.id.code_bt})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back_tv:
                finish();
                break;
            case R.id.code_bt:
                getSendMsgData();
                break;
            case R.id.submit_bt:
                if (!checkInput()) return;
                dialog.show();
                String register_password = ConfigUtil.GET_PASSWORD(newPwdEt.getText().toString().trim());
                String register_password_two = ConfigUtil.GET_PASSWORD(confirmPwdEt.getText().toString().trim());
                String data = new Gson().toJson(new GetBackRegister(userNameEt.getText().toString().trim(),
                        codeEt.getText().toString().trim(), register_password, register_password_two, ConfigUtil.GET_TIME()));
                Map map = new HashMap();
                map.put("register_phone", userNameEt.getText().toString().trim());
                map.put("code", codeEt.getText().toString().trim());
                map.put("register_password", register_password);
                map.put("register_password_two", register_password_two);
                map.put("time", ConfigUtil.GET_TIME());
                String sign = ConfigUtil.GET_SIGN(map);
                getBackRegister(data, sign);
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

    public boolean checkInput() {
        String username = userNameEt.getText().toString().trim();
        String password = newPwdEt.getText().toString().trim();
        String code = codeEt.getText().toString().trim();
        String confirmPwd = confirmPwdEt.getText().toString().trim();
        if (username.equals("")) {
            ToastUtil.showToast("请输入手机号码");
            return false;
        }
        if (username.length() != 11) {
            ToastUtil.showToast("请输入正确的手机号码");
            return false;
        }
        if (code.equals("")) {
            ToastUtil.showToast("请输入验证码");
            return false;
        }
        if (code.length() != 6) {
            ToastUtil.showToast("请输入正确的验证码");
            return false;
        }
        if (password.equals("")) {
            ToastUtil.showToast("请输入新密码");
            return false;
        }
        if (password.length() < 6) {
            ToastUtil.showToast("密码长度必须6位以上");
            return false;
        }
        if (!isContainAll(password)) {
            ToastUtil.showToast("密码至少一个大写字母和小写字母、数字组成");
            return false;
        }
        if (confirmPwd.compareTo(password) != 0) {
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

    //找回密码
    private void getBackRegister(String data, String sign) {
        OkHttpUtils
                .post()
                .url(ConfigUtil.IP)
                .addParams("service", "GetBackRegister.DoGetBack")
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
                                            ToastUtil.showToast("重置成功");
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
        editor.putString("password", newPwdEt.getText().toString().trim());
        editor.commit();
    }

    class GetBackRegister {
        String register_phone;
        String code;
        String register_password;
        String register_password_two;
        String time;

        public GetBackRegister(String register_phone, String code, String register_password, String register_password_two, String time) {
            this.register_phone = register_phone;
            this.code = code;
            this.register_password = register_password;
            this.register_password_two = register_password_two;
            this.time = time;
        }
    }
}
