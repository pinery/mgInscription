package com.cimcitech.mginscription.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cimcitech.mginscription.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.view.ColumnChartView;


/**
 * Created by dapineapple on 2017/12/27.
 */

public class DataFragment extends Fragment {

    @BindView(R.id.chart)
    ColumnChartView chart;

    private Unbinder unbinder;
    private ColumnChartData columnData;
    private List<String> data = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.data_fragment_layout, container, false);
        unbinder = ButterKnife.bind(this, view);
        generateColumnData();
        return view;
    }

    private void generateColumnData() {
        for (int i = 0; i < 6; i++)
            data.add("00" + (i + 1));
        int numSubcolumns = 2;
        int numColumns = data.size();
        List<AxisValue> axisValues = new ArrayList<AxisValue>();
        List<Column> columns = new ArrayList<Column>();
        List<SubcolumnValue> values;
        for (int i = 0; i < numColumns; ++i) {
            values = new ArrayList<>();
            for (int j = 0; j < numSubcolumns; ++j) {
                if (j == 0)
                    values.add(new SubcolumnValue((float) 50, Color.parseColor("#83bde5")));
                else
                    values.add(new SubcolumnValue((float) 500, Color.parseColor("#afe19c")));
            }
            axisValues.add(new AxisValue(i).setLabel(data.get(i)));
            columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
        }
        columnData = new ColumnChartData(columns);
        columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
        //设置轴标签的最大字符数，最小值0，最大值32。
        columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));
        chart.setColumnChartData(columnData);
        chart.setValueSelectionEnabled(true);
        chart.setZoomType(ZoomType.HORIZONTAL);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
