package com.cimcitech.mginscription.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cimcitech.mginscription.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dapineapple on 2017/12/27.
 */

public class StatisticsFragment extends Fragment {

    @BindView(R.id.axis_tv)
    TextView axisTv;
    @BindView(R.id.axis_view)
    RelativeLayout axisView;
    @BindView(R.id.register_tv)
    TextView registerTv;
    @BindView(R.id.register_view)
    RelativeLayout registerView;
    @BindView(R.id.axis_line_iv)
    ImageView axisLineIv;
    @BindView(R.id.register_line_iv)
    ImageView registerLineIv;

    private Unbinder unbinder;
    private View statisticsLeftAxisView, statisticsRightRegisterView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        statisticsLeftAxisView = view.findViewById(R.id.statistics_left_axis_view);
        statisticsRightRegisterView = view.findViewById(R.id.statistics_right_register_view);
        return view;
    }

    @OnClick({R.id.axis_view, R.id.register_view})
    public void onclick(View view) {
        switch (view.getId()) {
            case R.id.axis_view:
                axisLineIv.setVisibility(View.VISIBLE);
                registerLineIv.setVisibility(View.INVISIBLE);
                statisticsLeftAxisView.setVisibility(View.VISIBLE);
                statisticsRightRegisterView.setVisibility(View.GONE);
                break;
            case R.id.register_view:
                axisLineIv.setVisibility(View.INVISIBLE);
                registerLineIv.setVisibility(View.VISIBLE);
                statisticsLeftAxisView.setVisibility(View.GONE);
                statisticsRightRegisterView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
