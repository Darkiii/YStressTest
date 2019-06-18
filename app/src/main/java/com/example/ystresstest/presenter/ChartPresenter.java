package com.example.ystresstest.presenter;

import android.content.Context;

import com.example.ystresstest.contact.BasePresenter;
import com.example.ystresstest.contact.ChartView;

public class ChartPresenter implements BasePresenter {

    private Context mContext;
    private ChartView chartView;

    public ChartPresenter(Context context, ChartView view) {
        this.mContext = context;
        this.chartView = view;
    }

    @Override
    public void start() {

    }
}
