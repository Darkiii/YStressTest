package com.example.ystresstest.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.example.ystresstest.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

public class ChartFragment extends Fragment {

    private RelativeLayout mLayout;
    private LineChart lineChart;
    private DataObj[] objs = {new DataObj(2, 3), new DataObj(3, 5), new DataObj(4, 7), new DataObj(5, 6)};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstaceState) {
        super.onCreateView(inflater, container, savedInstaceState);

        mLayout = (RelativeLayout) inflater.inflate(R.layout.chart_fragment, container, false);
        lineChart = mLayout.findViewById(R.id.chart);

        List<Entry> entries = new ArrayList<>();
        for (DataObj data : objs) {
            entries.add(new Entry(data.getValueX(), data.getValueY()));
        }

        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.BLUE);
        dataSet.setColor(Color.BLUE);
        dataSet.setValueTextColor(Color.RED);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();

        return mLayout;
    }

    private void initChart() {
        lineChart.setDrawBorders(true);

    }

    class DataObj {
        int valueX, valueY;

        public DataObj(int x, int y) {
            valueX = x;
            valueY = y;
        }

        public int getValueX() {
            return valueX;
        }

        public int getValueY() {
            return valueY;
        }
    }

}
