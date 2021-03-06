package com.simon.okhttp.callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public abstract class Callback<T> {

    /**
     *请求执行之前回调
     */
    public  void onBefore(Request request, int id){
    }
    /**
     *上传进度回调
     */
    public  void upProgress(float progress,long total , int id){

    }

    /**
     *下载进度回调
     */
    public  void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed){
    }

    /**
     *请求执行之后回调
     */
    public  void onAfter(int id){

    }

    public abstract T parseResponse(Response response) throws Exception;
    public abstract void onResponse(T response,int id);
    public abstract void onError(Call call,Exception e,int id);

   public static Callback CALLBACK_DEFAULT=new Callback() {
       @Override
       public Object parseResponse(Response response) throws Exception {
           return null;
       }

       @Override
       public void onResponse(Object response, int id){
       }

       @Override
       public void onError(Call call, Exception e, int id) {

       }
     };


}
