package com.example.ystresstest.units;

import android.content.Context;
import android.util.Log;

import com.example.ystresstest.utils.SuCommand;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Vector;

public class TemperatureUnit implements Runnable {

    private static final String TAG = "TemperatureUnit";

    private static final String A33H3_PATH = "/sys/class/thermal/thermal_zone0/temp";
    private static final String RK3288_PATH = "sys/devices/ff280000.tsadc/temp1_input";

    private ArrayList<Integer> tempData = new ArrayList<>();
    private int maxTemperature = 0;
    private int averageTemp = 0;
    private Context mContext;
    SharedPreferenceUnit spUnit;

    private int temperature;
    private boolean flag = false;
    private TemperatureCallback tCallback;

    private static TemperatureUnit instance = null;

    private TemperatureUnit(Context context) {
        mContext = context;
        spUnit = new SharedPreferenceUnit(mContext);
        new Thread(this).start();
    }

    public static synchronized TemperatureUnit getInstance(Context context) {
        if (instance == null) {
            instance = new TemperatureUnit(context);
        }
        return instance;
    }

    public void setTemperatureCallback(TemperatureCallback callback) {
        this.tCallback = callback;
    }

    public int getTemperature() {
        return temperature;
    }

    public void clearData() {
        clearTempData();
    }

    public void releaseInstance() {
        flag = true;
        instance = null;
        clearTempData();
    }

    @Override
    public void run() {
        FileOutputStream outputStream;
        OutputStreamWriter writer = null;

        String logPath = "/sdcard/stress.log";
        File stressLog = new File(logPath);
        try {
            outputStream = new FileOutputStream(stressLog);
            writer = new OutputStreamWriter(outputStream);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        while (!flag) {
            File tFile = null;

            if (new File(RK3288_PATH).exists()) {
                tFile = new File(RK3288_PATH);
            } else if (new File(A33H3_PATH).exists()) {
                tFile = new File(A33H3_PATH);
            }
            try {
                FileInputStream inputStream = new FileInputStream(tFile);
                InputStreamReader reader = new InputStreamReader(inputStream);
                temperature = reader.read();
//                Log.d(TAG, "temperature: " + temperature);
                reader.close();
                inputStream.close();
            } catch (FileNotFoundException e) {
                Log.e(TAG, "Can't get temperature.");
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            //温度日志需要重新完善
            try {
                writer.write("temperature is " + temperature + "\r\n");
                writer.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

            tCallback.temperatureChanged(temperature);
            tempData.add(temperature);
            if (temperature > maxTemperature) {
                maxTemperature = temperature;
                spUnit.setMaxTemperature(getMaxTemperature());
            }

            spUnit.setAverTemperature(getAverageTemp());

//            Log.e(TAG, "average: " + getAverageTemp() + " max: " + maxTemperature);

            try {
                Thread.sleep(10 * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public interface TemperatureCallback {
        public void temperatureChanged(int temp);
    }

    public void clearTempData() {
        maxTemperature = 0;
        tempData.clear();
    }

    public ArrayList<Integer> getTempData() {
        return tempData;
    }

    public String getMaxTemperature() {
        String max = "" + maxTemperature;
        if (max.length() == 1) {
            max = "00" + max;
        } else if (max.length() == 2) {
            max = "0" + max;
        }
        return max;
    }

    public String getAverageTemp() {
        String averString = "0";
        double average = 0.0;
        double size = (double) tempData.size();
        if (size == 0.0) {
            return "0";
        }
        List<Integer> temp = tempData;
        for (int t : temp) {
            average += t / size;
        }
        averString = "" + (int) average;
        if (averString.length() == 1) {
            averString = "00" + averString;
        } else if (averString.length() == 2) {
            averString = "0" + averString;
        }
        return averString;
    }

}
