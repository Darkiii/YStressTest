package com.example.ystresstest.activities;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ystresstest.R;
import com.example.ystresstest.contact.ChartView;
import com.example.ystresstest.presenter.ChartPresenter;
import com.example.ystresstest.units.SharedPreferenceUnit;
import com.example.ystresstest.units.TemperatureUnit;
import com.example.ystresstest.utils.ZxingUtil;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity implements ChartView {
    private static final String TAG = "ChartActivity";
    private LineChart lineChart;
    private ArrayList<Integer> tempData;
    private ImageView zxingView;
    private Bitmap zxingBitmap;
    private SharedPreferenceUnit spUnit = null;
    private Button btnLeave;
    private TextView tvMaxTemp, tvAvgTemp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        spUnit = new SharedPreferenceUnit(this);

        full_screen();

        lineChart = findViewById(R.id.chart);
        zxingView = findViewById(R.id.zxing);
        btnLeave = findViewById(R.id.leave);
        tvMaxTemp = findViewById(R.id.maxT);
        tvAvgTemp = findViewById(R.id.avgT);

        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAffinity();
            }
        });

        TemperatureUnit temperature = TemperatureUnit.getInstance(this);
        tempData = temperature.getTempData();

        lineChart.setDrawBorders(true);
        lineChart.getAxisRight().setEnabled(false);
        lineChart.setDrawBorders(false);

        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < tempData.size(); i++) {
            entries.add(new Entry(i * 10, tempData.get(i)));
        }

        LineDataSet dataSet = new LineDataSet(entries, "CPU温度");
        dataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        dataSet.setDrawFilled(true);
        dataSet.setFillColor(Color.BLUE);
        dataSet.setColor(Color.BLUE);
        dataSet.setDrawValues(false);
        dataSet.setDrawCircles(false);

        XAxis xAxis = lineChart.getXAxis();
        YAxis yAxis = lineChart.getAxisLeft();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setAxisMaximum(400);
        xAxis.setGranularity(1);
//        yAxis.setAxisMaximum(400);
        yAxis.setAxisMaximum(100);
        yAxis.setAxisMinimum(20);
        yAxis.setGranularity(1);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);
        lineChart.invalidate();

        String zxing = generateZxing();
        Log.d(TAG, "zxing=" + zxing);
        showZxing(zxing);

        tvMaxTemp.append(" " + Integer.parseInt(spUnit.getMaxTemperature()));
        tvAvgTemp.append(" " + Integer.parseInt(spUnit.getAverTemperature()));

        File file = new File("/sdcard/uuid.txt");
        if (file.exists()) {
            file.delete();
        }
    }

    private String generateZxing() {
        String uuid = spUnit.getUuid();
        String maxT = spUnit.getMaxTemperature();
        String averT = spUnit.getAverTemperature();
        String length = spUnit.getTestLength();
        return uuid + averT + maxT + length + "0000000000";
    }

    private void showZxing(String information) {
        zxingBitmap = ZxingUtil.createQRCodeBitmap(information, 240, 240);
        zxingView.setImageBitmap(zxingBitmap);
    }
    @Override
    public void setPresenter(ChartPresenter presenter) {

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                full_screen();
                break;
        }
        return super.onTouchEvent(event);
    }

    private long mPressedTime = 0;

    @Override
    public void onBackPressed() {
        long mNowTime = System.currentTimeMillis();//获取第一次按键时间
            if((mNowTime - mPressedTime) > 2000){//比较两次按键时间差
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mPressedTime = mNowTime;
        }
        else{//退出程序
            finishAffinity();
        }
    }

    public void full_screen(){
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE
        );
    }
}
