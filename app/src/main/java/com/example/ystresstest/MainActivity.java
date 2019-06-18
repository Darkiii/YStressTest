package com.example.ystresstest;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.EventLog;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.dinuscxj.progressbar.CircleProgressBar;
import com.example.ystresstest.contact.MainView;
import com.example.ystresstest.dialog.StressTimeDialog;
import com.example.ystresstest.presenter.MainPresenter;
import com.example.ystresstest.units.SharedPreferenceUnit;
import com.example.ystresstest.utils.SuCommand;
import com.github.clans.fab.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements MainView, View.OnClickListener {

    private static final String TAG = "MainActivity";

    private static final String FRAGMENT_TAG = "rajawali";

    private SharedPreferenceUnit sharedPreferenceUnit;

    private TextView tvTesting, tvCpu;
    private CircleProgressBar mCustomProgressBar5;
    private FloatingActionButton btnSetTime;
    private int percentage = 0;

    private Thread cpuThread;

    private Handler mHandler = new Handler();

    private MainPresenter mainPresenter;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Fragment mCurrentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //关闭显示Action Bar
        ActionBar actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.hide();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferenceUnit = new SharedPreferenceUnit(this);
        Log.d(TAG, "sharedPreferenceUnit： uuid " + sharedPreferenceUnit.getUuid());

        full_screen();

        //!!!!动态权限申请待完成!!!! 日志部分待完成
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

//        CircleProgressBar
        mCustomProgressBar5 = findViewById(R.id.custom_progress);

        tvTesting = findViewById(R.id.testing);

        tvCpu = findViewById(R.id.cpu_temp);

        btnSetTime = findViewById(R.id.menu_item);
        btnSetTime.setOnClickListener(this);

        mCustomProgressBar5.setProgressFormatter(new CircleProgressBar.ProgressFormatter() {
            @Override
            public CharSequence format(int progress, int max) {
                return progress + "%";
            }
        });

        fragmentManager = getSupportFragmentManager();


        mainPresenter = new MainPresenter(this, this);
        mainPresenter.start();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.menu_item:
//                mainPresenter.setStressTime();
                mainPresenter.showSingleChoiceDialog();
                Log.d(TAG, "MENU_ITEM clicked.");
                break;
        }
    }

    @Override
    public void refreshProgress(final int progress) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                mCustomProgressBar5.setProgress(progress);
            }
        });
    }

    @Override
    public void showStepInfo(final String info) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                tvTesting.setText(info);
            }
        });
    }

    @Override
    public void showTemperatureInfo(final String info) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                tvCpu.setText(info);
            }
        });
    }

    @Override
    public void setPresenter(MainPresenter presenter) {
    }

    @Override
    public void launchFragment(final StressApplication.ExampleItem exampleItem) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                transaction = fragmentManager.beginTransaction();
                try {
                    mCurrentFragment = (Fragment) exampleItem.actionClass.getConstructors()[0].newInstance();
                    transaction.replace(R.id.content_frame, mCurrentFragment, FRAGMENT_TAG);
                    transaction.commit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void removeCurrentFragment() {
        transaction = fragmentManager.beginTransaction();
        transaction.remove(mCurrentFragment);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        new SuCommand().execRootCmdSilent("busybox killall memtester");
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
