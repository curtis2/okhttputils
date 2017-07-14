package com.simon.okhttp.simple;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class TestOkhttp {
    static String getUrl = "http://rapapi.org/mockjs/21497/query";
    static String postJsonUrl = "http://rapapi.org/mockjs/21900/PostJson";
    public static void main(String[] args) {
        try {
//          testHttpGet();
//          testHttpPost();
//            testOkHttpUtilGet();
            testpostJson();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    static void testOkHttpUtilGet() throws IOException {
        OkHttpManager.getmInstance()._getAsync(getUrl, new OkHttpManager.ResultCall<String>(){
            @Override
            public void onError(Request request, Exception e) {

            }
            @Override
            public void onResponse(String response) {
                System.out.print(response);
            }
        });
    }

    static void testpostJson() throws IOException {
        String json="{\"isOk\":false}";
        OkHttpManager.getmInstance()._postAsynJson(postJsonUrl, new OkHttpManager.ResultCall<String>(){
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                System.out.print(response);
            }
        },json);
    }

    static void testHttpGet() throws IOException {
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().url(getUrl).build();
        Response response=okHttpClient.newCall(request).execute();
        if(response.isSuccessful()){
            String content=response.body().string();
            System.out.print(content);
        }
    }


    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    static String postUrl = "http://rapapi.org/mockjs/21497/postquery";

    static void testHttpPost() throws IOException {
         String json="{\n" +
                 "    \"isOk\": false\n" +
                 "}";
        RequestBody body=RequestBody.create(JSON,json);
        OkHttpClient okHttpClient=new OkHttpClient();
        Request request=new Request.Builder().
                url(postUrl).
                post(body).
                build();

      Response response=okHttpClient.newCall(request).execute();
        if(response.isSuccessful()){
            String content=response.body().string();
            System.out.print(content);
        }
    }
}
