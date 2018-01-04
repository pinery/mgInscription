package com.cimcitech.mginscription;

import android.app.Application;

import com.cimcitech.mginscription.utils.ConfigUtil;
import com.cimcitech.mginscription.widget.ShapeLoadingDialog;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;

/**
 * Created by cimcitech on 2017/12/29.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ConfigUtil.CONTEXT = getApplicationContext();
        ZXingLibrary.initDisplayOpinion(this);
    }
}
