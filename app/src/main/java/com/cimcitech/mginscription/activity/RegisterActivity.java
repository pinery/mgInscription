package com.cimcitech.mginscription.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.utils.ConfigUtil;
import com.cimcitech.mginscription.utils.ToastUtil;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.login_tv, R.id.back_tv, R.id.submit_bt})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.login_tv:
                finish();
                break;
            case R.id.back_tv:
                finish();
                break;
            case R.id.submit_bt:
                String username = userNameEt.getText().toString().trim();
                String password = ConfigUtil.GET_PASSWORD(userPasswordEt.getText().toString().trim());
                //1967eedebee24581af284705dc4538b3
                //1967eedebee24581af284705dc4538b3
                String time = ConfigUtil.GET_TIME();
                String data = new Gson().toJson(new Request(username, password, time));
                Map map = new HashMap();
                map.put("register_phone", username);
                map.put("register_password", password);
                map.put("time", time);
                String sign = ConfigUtil.GET_SIGN(map);
                this.register(data, sign);
                break;
        }
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
                            }

                            @Override
                            public void onResponse(String response, int id) {
                                Gson gson = new Gson();
                                ToastUtil.showToast(response);
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
}
