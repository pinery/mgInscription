package com.cimcitech.mginscription.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cimcitech.mginscription.R;

/**
 * Created by dapineapple on 2017/12/27.
 */

public class UserFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_layout, container, false);
        return view;
    }
}
