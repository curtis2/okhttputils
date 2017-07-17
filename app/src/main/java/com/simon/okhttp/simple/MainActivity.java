package com.simon.okhttp.simple;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.simon.okhttp.OkHttpUtils;
import com.simon.okhttp.callback.StringCallback;
import com.simon.okhttp.simple.utils.CrashHandleUtil;

import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private TextView contentTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentTv= (TextView) findViewById(R.id.content);
        CrashHandleUtil.getmInstance().init(this,"sample");
    }

    /**
     * http get 请求
     * @param v
     */
    public void getStringRequest(View v){
        String url="http://www.391k.com/api/xapi.ashx/info.json?key=bd_hyrzjjfb4modhj&size=10&page=1";
        OkHttpUtils.get(url).build().execute(new StringCallback() {
            @Override
            public String onResponse(String response, int id) {
                contentTv.setText(response);
                return null;
            }
            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                contentTv.setText(e.toString());
            }
        });
    }

    /**
     * http get 请求
     * 并携带参数
     * @param v
     */
    public void getStringWithParamsRequest(View v){
        String url="https://www.baidu.com/";
        Map<String,String> params=new HashMap<>();
        params.put("p","aa");
        params.put("k","bb");
        OkHttpUtils.get(url).params(params).build().execute(new StringCallback() {
            @Override
            public String onResponse(String response, int id) {
                contentTv.setText(response);
                return null;
            }

            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                contentTv.setText(e.toString());
            }
        });
    }

    /**
     * post json 请求
     * 并携带参数
     * @param v
     */
    public void postJson(View v){
        String url="https://www.baidu.com/";
        OkHttpUtils.get(url).build().execute(new StringCallback() {
            @Override
            public String onResponse(String response, int id) {
                contentTv.setText(response);
                return null;
            }

            @Override
            public void onError(okhttp3.Call call, Exception e, int id) {
                contentTv.setText(e.toString());
            }
        });
    }




}
