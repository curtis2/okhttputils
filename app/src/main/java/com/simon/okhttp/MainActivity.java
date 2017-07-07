package com.simon.okhttp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.IOException;

import okhttp3.Request;

import static com.simon.okhttp.TestOkhttp.getUrl;
import static com.simon.okhttp.TestOkhttp.postJsonUrl;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
//            testOkHttpUtilGet();
            testpostJson();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    static void testpostJson() throws IOException {
        String json="{\"isOk\":false}";
        OkHttpUtils.getmInstance()._postAsynJson(postJsonUrl, new OkHttpUtils.ResultCall<String>(){
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                System.out.print(response);
            }
        },json);
    }

    static void testOkHttpUtilGet() throws IOException {
        OkHttpUtils.getmInstance()._getAsync(getUrl, new OkHttpUtils.ResultCall<String>(){
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                System.out.print(response);
            }
        });
    }

}
