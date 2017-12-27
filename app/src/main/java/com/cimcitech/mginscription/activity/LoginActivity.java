package com.cimcitech.mginscription.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cimcitech.mginscription.MainActivity;
import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.utils.StatusBarUtils;

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

    @OnClick({R.id.submit_bt, R.id.register_tv,R.id.update_password_tv})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.submit_bt:
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                break;
            case R.id.register_tv:
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                break;
            case R.id.update_password_tv:
                startActivity(new Intent(LoginActivity.this, ForgetPwdActivity.class));
                break;
        }
    }
}
