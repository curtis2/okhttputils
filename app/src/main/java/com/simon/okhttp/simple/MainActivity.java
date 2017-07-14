package com.simon.okhttp.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.simon.okhttp.Callback.StringCallback;
import com.simon.okhttp.OkHttpUtils;
import com.simon.okhttp.simple.utils.CrashHandleUtil;

import okhttp3.Call;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CrashHandleUtil.getmInstance().init(this,"sample");

        OkHttpUtils.get().url("https://www.baidu.com/").build().execute(new StringCallback() {
            @Override
            public void onResponse(String response, int id) {

            }

            @Override
            public void onError(Call call, Exception e, int id) {

            }
        });
    }


}
