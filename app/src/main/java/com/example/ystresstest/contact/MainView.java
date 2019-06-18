package com.example.ystresstest.contact;

import android.content.Context;
import android.support.v4.app.Fragment;

import com.example.ystresstest.StressApplication;
import com.example.ystresstest.presenter.MainPresenter;

public interface MainView extends BaseView<MainPresenter> {
    void refreshProgress(int progress);
    void showStepInfo(String info);
    void showTemperatureInfo(String info);
    void launchFragment(StressApplication.ExampleItem exampleItem);
    void removeCurrentFragment();
}
