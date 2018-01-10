package com.cimcitech.mginscription.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.model.ResultVo;
import com.cimcitech.mginscription.utils.ConfigUtil;
import com.cimcitech.mginscription.utils.MD5Util;
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

public class UpdatePasswordActivity extends AppCompatActivity {

    @BindView(R.id.back_ib)
    ImageButton backIb;
    @BindView(R.id.new_pwd_et)
    EditText newPwdEt;
    @BindView(R.id.confirm_pwd_et)
    EditText confirmPwdEt;
    @BindView(R.id.submit_bt)
    Button submitBt;
    @BindView(R.id.old_pwd_et)
    EditText oldPwdEt;
    private SharedPreferences sp;
    private ShapeLoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);
        sp = this.getSharedPreferences(ConfigUtil.KEY_LOGIN_AUTO, MODE_PRIVATE);
        dialog = new ShapeLoadingDialog(UpdatePasswordActivity.this);
        dialog.setLoadingText("正在加载...");
    }

    @OnClick({R.id.submit_bt, R.id.back_ib})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.submit_bt:
                if (!checkInput()) return;
                dialog.show();
                String oldPwdStr = oldPwdEt.getText().toString().trim();
                String newPwdStr = newPwdEt.getText().toString().trim();
                String oldPwd = ConfigUtil.GET_PASSWORD(oldPwdStr);
                String newPwd = ConfigUtil.GET_PASSWORD(newPwdStr);
                String time = ConfigUtil.GET_TIME();
                String flag = MD5Util.md5(MD5Util.md5(ConfigUtil.GET_TIME()) + MD5Util.md5(ConfigUtil.MD5_CODE));
                String data = new Gson().toJson(new EditUserPwd(oldPwd, newPwd, flag, time));
                Map map = new HashMap();
                map.put("oldPwd", oldPwd);
                map.put("newPwd", newPwd);
                map.put("flag", flag);
                map.put("time", time);
                String sign = ConfigUtil.GET_SIGN(map);
                getEditUserPwd(data, sign);
                break;
            case R.id.back_ib:
                finish();
                break;
        }
    }

    public boolean checkInput() {
        if (oldPwdEt.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入原始密码");
            return false;
        }
        if (newPwdEt.getText().toString().trim().equals("")) {
            ToastUtil.showToast("请输入新密码");
            return false;
        }
        if (newPwdEt.getText().toString().trim().length() < 6) {
            ToastUtil.showToast("密码长度必须6位以上");
            return false;
        }
        if (!isContainAll(newPwdEt.getText().toString().trim())) {
            ToastUtil.showToast("密码至少一个大写字母和小写字母、数字组成");
            return false;
        }
        if (confirmPwdEt.getText().toString().trim().compareTo(newPwdEt.getText().toString().trim()) != 0) {
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
    private void getEditUserPwd(String data, String sign) {
        OkHttpUtils
                .post()
                .url(ConfigUtil.IP)
                .addParams("service", "Users.EditUserPwd")
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
                                            ToastUtil.showToast("修改成功，下次请用新密码登录");
                                            saveUserInfo();
                                            finish();
                                        } else if (resultVo.getData().getCode() == 2) {//登录超时
                                            ToastUtil.showToast("登录超时，请重新登录");
                                            ConfigUtil.isLogin = false;
                                            startActivity(new Intent(UpdatePasswordActivity.this, LoginActivity.class));
                                        } else
                                            ToastUtil.showToast(resultVo.getData().getReturnmsg());
                            }
                        }
                );
    }

    private void saveUserInfo() {
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("username", ConfigUtil.userName);
        editor.putString("password", newPwdEt.getText().toString().trim());
        editor.commit();
    }

    class EditUserPwd {
        String oldPwd;
        String newPwd;
        String flag;
        String time;

        public EditUserPwd(String oldPwd, String newPwd, String flag, String time) {
            this.oldPwd = oldPwd;
            this.newPwd = newPwd;
            this.flag = flag;
            this.time = time;
        }
    }
}
