package com.example.ystresstest;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ystresstest.units.SharedPreferenceUnit;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.support.v4.content.PermissionChecker.PERMISSION_GRANTED;

public class UuidActivity extends AppCompatActivity {
    private static final String TAG = "UuidActivity";

    private EditText uuidEditText;
    private TextView uuidTextView;
    private Button uuidBtn;
    private String uuid;
    private SharedPreferenceUnit sharedPreferenceUnit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_uuid);

        uuidEditText = findViewById(R.id.uuid_edit);
        uuidTextView = findViewById(R.id.uuid_text);
        uuidBtn = findViewById(R.id.uuid_btn);

        sharedPreferenceUnit = new SharedPreferenceUnit(this);

        requestAllPower();

        uuidBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uuidT = uuidEditText.getText().toString();
                uuidT = replaceBlank(uuidT); //去除制表符
                if(isUuidVaild(uuidT)){
//                    saveDataUtils.updateUUID(string);
                    sharedPreferenceUnit.setUuid(uuidT);
                    startMainActivity();
                }else{
                    Toast.makeText(UuidActivity.this,"设备码格式不正确，请重新输入!!!",Toast.LENGTH_LONG).show();
                    uuidEditText.setText("");
                }
            }
        });
    }

    private boolean isUuidVaild(String uuid){
        if(uuid == null){return false;}
        String format = "^[A-Z0-9]{15}$";
        Pattern pattern = Pattern.compile(format);
        Matcher matcher = pattern.matcher(uuid);
        if(matcher.find()){
            return true;
        }else{
            return false;
        }
    }


    private void startMainActivity(){
        Intent intent = new Intent(this,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        this.finish();
    }


    /*
    去掉换行和制表符号
    */
    private String replaceBlank(String str) {
        String dest = "";
        if (str!=null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            dest = m.replaceAll("");
        }
        return dest;
    }

    public void requestAllPower() {
        //使用兼容库就无需判断系统版本
        int hasWriteStoragePermission = ContextCompat.checkSelfPermission(getApplication(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (hasWriteStoragePermission == PackageManager.PERMISSION_GRANTED) {
            //拥有权限，执行操作
            readUuidFile();
        }else{
            //没有权限，向用户请求权限
            ActivityCompat.requestPermissions(UuidActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PERMISSION_GRANTED) {
//                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请成功", Toast.LENGTH_SHORT).show();
                    readUuidFile();
                } else {
                    Toast.makeText(this, "" + "权限" + permissions[i] + "申请失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void readUuidFile() {
        File uuidFile = new File("/sdcard/uuid.txt");
        if (uuidFile.exists()) {
            try {
                FileInputStream inputStream = new FileInputStream(uuidFile);
                InputStreamReader reader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(reader);
                uuid = bufferedReader.readLine();
                uuid = replaceBlank(uuid);
                if (isUuidVaild(uuid)) {
                    Log.d(TAG, "" + uuid);
                    uuidEditText.setText(uuid);
                    uuidTextView.setText("已找到序列号文件，点击“确定”继续");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
