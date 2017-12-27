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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.back_tv})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.back_tv:
                finish();
                break;
        }
    }
}
