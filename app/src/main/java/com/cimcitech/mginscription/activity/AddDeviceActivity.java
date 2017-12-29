package com.cimcitech.mginscription.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.cimcitech.mginscription.MainActivity;
import com.cimcitech.mginscription.R;
import com.uuzuche.lib_zxing.activity.CaptureActivity;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddDeviceActivity extends AppCompatActivity {

    @BindView(R.id.back_ib)
    ImageButton backIb;
    @BindView(R.id.right_ib)
    ImageButton rightIb;
    @BindView(R.id.qr_code_view)
    RelativeLayout qrCodeView;

    public static final int REQUEST_CODE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_device);
        ButterKnife.bind(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * 处理二维码扫描结果
         */
        if (requestCode == REQUEST_CODE) {
            //处理扫描结果（在界面上显示）
            if (null != data) {
                Bundle bundle = data.getExtras();
                if (bundle == null) return;
                if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_SUCCESS) {
                    String result = bundle.getString(CodeUtils.RESULT_STRING);
                    Toast.makeText(this, "解析结果:" + result, Toast.LENGTH_LONG).show();
                } else if (bundle.getInt(CodeUtils.RESULT_TYPE) == CodeUtils.RESULT_FAILED) {
                    Toast.makeText(AddDeviceActivity.this, "解析二维码失败", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @OnClick({R.id.qr_code_view, R.id.back_ib})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.qr_code_view:
                Intent intent = new Intent(AddDeviceActivity.this, SecondActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
                break;
            case R.id.back_ib:
                finish();
                break;
        }
    }
}
