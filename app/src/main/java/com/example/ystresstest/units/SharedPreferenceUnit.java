package com.example.ystresstest.units;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPreferenceUnit {
    private Context mContext;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPreferenceUnit(Context context) {
        mContext = context;
        sharedPreferences = mContext.getSharedPreferences("stress_test", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }
    public String getUuid() {
        return sharedPreferences.getString("uuid", null);
    }
    public void setUuid(String uuid) {
        editor.putString("uuid", uuid);
        editor.commit();
    }

    public String getMaxTemperature() {
        return sharedPreferences.getString("max_temp", null);
    }

    public void setMaxTemperature(String maxTemperature) {
        editor.putString("max_temp", maxTemperature);
        editor.commit();
    }

    public String getAverTemperature() {
        return sharedPreferences.getString("aver_temp", null);
    }

    public void setAverTemperature(String averTemperature) {
        editor.putString("aver_temp", averTemperature);
        editor.commit();
    }

    public String getTestLength() {
        return sharedPreferences.getString("test_length", null);
    }

    public void setTestLength(String length) {
        editor.putString("test_length", length);
        editor.commit();
    }
}
