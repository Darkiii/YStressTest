package com.example.ystresstest;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import com.example.ystresstest.StressApplication.ExampleItem;
import com.example.ystresstest.StressApplication.Category;

import android.telephony.mbms.MbmsErrors;
import android.view.View;
import android.widget.Button;

import com.example.ystresstest.fragments.DisplayFragment;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String FRAGMENT_TAG = "test";

    private Button btnChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        btnChange = findViewById(R.id.bt_change);
        btnChange.setOnClickListener(this);

        full_screen();

        Fragment fragment = new DisplayFragment();
        launchFragment(Category.GENERAL, StressApplication.ITEMS.get(Category.GENERAL)[1]);
    }

//    private void lunchFragment(Fragment fragment) {
//        final FragmentManager fragmentManager = getSupportFragmentManager();
//        final FragmentTransaction transaction = fragmentManager.beginTransaction();
//        try {
////            final Fragment fragment = (Fragment) new MovieFragment();
//            transaction.replace(R.id.test_frame, fragment, FRAGMENT_TAG);
//            transaction.commit();
//        } catch (Exception e) {
//
//        }
//    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_change:
                launchFragment(Category.GENERAL, StressApplication.ITEMS.get(Category.GENERAL)[1]);
                break;
        }
    }

    private void launchFragment(Category category, ExampleItem exampleItem) {
        final FragmentManager fragmentManager = getSupportFragmentManager();
        final FragmentTransaction transaction = fragmentManager.beginTransaction();

        try {
            final Fragment fragment = (Fragment) exampleItem.actionClass.getConstructors()[0].newInstance();
            transaction.replace(R.id.content_frame, fragment, FRAGMENT_TAG);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
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
