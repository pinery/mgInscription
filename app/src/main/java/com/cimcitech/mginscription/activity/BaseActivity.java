package com.cimcitech.mginscription.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.cimcitech.mginscription.R;
import com.cimcitech.mginscription.utils.MyActivityManager;

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        MyActivityManager manager = MyActivityManager.getInstance();
        manager.pushOneActivity(this);
    }
}
