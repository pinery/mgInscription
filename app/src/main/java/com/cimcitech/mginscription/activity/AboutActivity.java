package com.cimcitech.mginscription.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.utils.MyActivityManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends AppCompatActivity {

    @BindView(R.id.back_ib)
    ImageButton backIb;
    @BindView(R.id.right_ib)
    ImageButton rightIb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        ButterKnife.bind(this);
        MyActivityManager manager = MyActivityManager.getInstance();
        manager.pushOneActivity(this);
    }

    @OnClick(R.id.back_ib)
    public void onclick() {
        finish();
    }
}
