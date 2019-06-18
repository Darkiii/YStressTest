package com.example.ystresstest.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.example.ystresstest.MainActivity;
import com.example.ystresstest.StressApplication;
import com.example.ystresstest.activities.ChartActivity;
import com.example.ystresstest.contact.BasePresenter;
import com.example.ystresstest.contact.MainView;
import com.example.ystresstest.dialog.StressTimeDialog;
import com.example.ystresstest.units.MemoryUnit;
import com.example.ystresstest.units.SharedPreferenceUnit;
import com.example.ystresstest.units.TemperatureUnit;
import com.example.ystresstest.utils.SuCommand;

import java.util.Timer;
import java.util.TimerTask;

public class MainPresenter implements BasePresenter {

    private static final String TAG = "MainPresenter";
    private static final String CPU_TEMP = "CPU温度：";

    private static final int STEP_GPU = 0;
    private static final int STEP_DISPLAY = 1;
    private static final int STEP_CPU_RAM = 2;
    private static final int STEP_OK = 5;

    private boolean threadCreated = false;

    private int stressTimeIndex = 0;
    private final int[] stressTimes = {2, 8, 12, -1};
    private final String[] items = {"2小时", "8小时", "12小时", "永久"};

    private int percentage = 0;
    private static final int PHASE1 = 0;
    private static final int PHASE2 = 33;
    private static final int PHASE3 = 66;
    private static final int PHASE4 = 100;

    private boolean loopTest = false;

    private static final String GPU_TESTING = "正在老化GPU";
    private static final String CPU_TESTING = "正在老化CPU和RAM";
    private static final String MOVIE_TESTING = "正在老化屏幕";
    private static final String OK_TESTING = "老化测试完成";

    private MainView mainView;
    private Context mContext;
    private Handler mHandler;
    private TemperatureUnit temperature;

    private Thread cpuThread;

    private Timer timer = null;
    class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            switch (percentage) {

                case PHASE1:
                    mainView.launchFragment(StressApplication.ITEMS.get(StressApplication.Category.GENERAL)[STEP_GPU]);
                    mainView.showStepInfo(GPU_TESTING);
                    Log.e(TAG, "phase1 = " + percentage);
                    break;
                case PHASE2:
//                    Intent intent = new Intent(mContext, ChartActivity.class);
//                    mContext.startActivity(intent);
                    mainView.launchFragment(StressApplication.ITEMS.get(StressApplication.Category.GENERAL)[STEP_DISPLAY]);
                    mainView.showStepInfo(MOVIE_TESTING);
                    Log.e(TAG, "phase2 = " + percentage);
                    break;
                case PHASE3:
                    cpuThread = new CpuMemThread();
                    cpuThread.start();

                    mainView.showStepInfo(CPU_TESTING);
                    Log.e(TAG, "phase3 = " + percentage);
                    break;
                case PHASE4:
                    cpuThread.interrupt();
                    mainView.showStepInfo(OK_TESTING);
                    new SuCommand().execRootCmdSilent("busybox killall memtester");
                    Log.e(TAG, "phase4 = " + percentage);
                    if (!loopTest) {
                        Intent intent = new Intent(mContext, ChartActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        mContext.startActivity(intent);
                    }
                    break;
            }

            if (percentage <= 100) {
                mainView.refreshProgress(percentage);
                percentage++;
            }
            if (loopTest && percentage > 100) {
                percentage = 0;
            }
        }
    };

    public MainPresenter(Context context, MainView mainView) {
        mContext = context;
        this.mainView = mainView;
    }

    public void setStressTime(int time) {
        Log.d(TAG, "setStressTime");
    }

    class CpuMemThread extends Thread {
        @Override
        public void run() {
            if (!interrupted()) {
                new MemoryUnit(200, 10).startTest();
            }
        }
    }

    @Override
    public void start() {
        startPhaseTest(0);

        temperature = TemperatureUnit.getInstance(mContext);
        temperature.setTemperatureCallback(new TemperatureUnit.TemperatureCallback() {
            @Override
            public void temperatureChanged(int temp) {
                mainView.showTemperatureInfo(CPU_TEMP + temp);
            }
        });
    }

    private void startPhaseTest(int timeIndex) {
        int timeOfPercent = stressTimes[timeIndex] * 36;
        String length = "" + stressTimes[timeIndex];
        if (length.length() == 1) {
            length = "0" + length;
        }
        new SharedPreferenceUnit(mContext).setTestLength(length);
        timer = new Timer();
        timer.schedule(new MyTimerTask(), 200, timeOfPercent * 1000);
    }

    private void resetTest(int timeIndex) {
        timer.cancel();
        percentage = 0;
        temperature.clearTempData();
        mainView.removeCurrentFragment();

        if (stressTimes[timeIndex] < 0) {
            loopTest = true;
            timeIndex = 0;
        } else {
            loopTest = false;
        }
        startPhaseTest(timeIndex);
    }

    public void showSingleChoiceDialog() {

        StressTimeDialog.Builder dialog = new StressTimeDialog.Builder(mContext);
        dialog.setTitle("请选择一个老化时间");
        dialog.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                Log.d(TAG, "which: " + which);
                stressTimeIndex = which;
            }
        });

        dialog.setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        resetTest(stressTimeIndex);
                    }
                });
        dialog.show();
    }

}
