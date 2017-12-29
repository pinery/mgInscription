package com.cimcitech.mginscription.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.utils.ConfigUtil;
import com.cimcitech.mginscription.utils.MD5Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.submit_bt, R.id.register_tv, R.id.update_password_tv})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.submit_bt:
                String string = MD5Util.md5(userPasswordEt.getText().toString().trim());
                Log.e("md5--->", MD5Util.md5(string + ConfigUtil.MD));
                Log.e("time--->", getTime());
                break;
            case R.id.register_tv:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.update_password_tv:
                startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
                break;
        }
    }

    //获取系统时间的10位的时间戳
    public String getTime() {
        long time = System.currentTimeMillis() / 1000;
        String str = String.valueOf(time);
        return str;
    }
}
