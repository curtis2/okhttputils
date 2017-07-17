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
     *下载进度回调
     */
    public  void inProcess(int process,int id){

    }

    /**
     *请求执行之后回调
     */
    public  void onAfter(int id){

    }

    public abstract T parseResponse(Response response) throws Exception;
    public abstract T onResponse(T response,int id);
    public abstract void onError(Call call,Exception e,int id);

   public static Callback CALLBACK_DEFAULT=new Callback() {
       @Override
       public Object parseResponse(Response response) throws Exception {
           return null;
       }

       @Override
       public Object onResponse(Object response, int id) {
           return null;
       }

       @Override
       public void onError(Call call, Exception e, int id) {

       }
     };


}
