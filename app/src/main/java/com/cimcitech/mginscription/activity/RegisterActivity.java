package com.cimcitech.mginscription.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.utils.StatusBarUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login_tv, R.id.back_tv})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.login_tv:
                finish();
                break;
            case R.id.back_tv:
                finish();
                break;
        }
    }


    //获取系统时间的10位的时间戳
    public String getTime() {
        long time = System.currentTimeMillis() / 1000;
        String str = String.valueOf(time);
        return str;
    }

    public ArrayList<KeyValuePair> getRequestData() {
        ArrayList<KeyValuePair> data = new ArrayList<>();
        data.add(new KeyValuePair("register_phone", "18927436372"));
        data.add(new KeyValuePair("register_password", "123456"));
        data.add(new KeyValuePair("time", getTime()));
        return data;
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

    public class KeyValuePair {
        String key;
        String value;

        public KeyValuePair(String k, String v) {
            key = k;
            value = v;
        }
    }
}
