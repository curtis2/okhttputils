package com.simon.okhttp.simple;

import android.os.Handler;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 *
 * 初步封装okHttp的使用
 * 单元测试
 */
public class OkHttpManager {

    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
    //单例模式
    private static OkHttpManager mInstance;
    private OkHttpClient mOkHttpClient;
    private Handler mDelivery;
    private Gson mGson;

    private OkHttpManager(){
        mOkHttpClient=new OkHttpClient();
//        mDelivery=new Handler(Looper.getMainLooper());
        mGson=new Gson();
    }

    public static OkHttpManager getmInstance(){
         if(mInstance==null){
             synchronized (OkHttpManager.class){
                 if(mInstance==null){
                     mInstance=new OkHttpManager();
                 }
             }
         }
         return mInstance;
    }

    /**
     * 同步的get请求
     */
    public Response _getAsync(String url) throws IOException {
        Request request=new Request.Builder().url(url).build();
        Response response=mOkHttpClient.newCall(request).execute();
        return  response;
    }

    /**
     * 异步的get请求
     */
    public void  _getAsync(String url,ResultCall resultCall) throws IOException {
        Request request=new Request.Builder()
                .url(url)
                .build();
        deliveryResult(resultCall,request);
    }


    /**
     * 同步的post请求
     */
    public Response _postJson(String url,String json) throws IOException {
        Request request=buildPostJsonRequest(url,json);
        Response response=mOkHttpClient.newCall(request).execute();
        return  response;
    }

    /**
     * 异步的post json请求
     */
    public void _postAsynJson(String url,ResultCall resultCall,String json) throws IOException {
        Request request=buildPostJsonRequest(url,json);
        deliveryResult(resultCall, request);
    }

    /**
     * 异步的post form请求
     */
    public void _postAsynForm(String url,ResultCall resultCall,Param[] params) throws IOException {
        Request request=buildPostFormRequest(url,params);
        deliveryResult(resultCall, request);
    }

    private Request buildPostJsonRequest(String url,String json) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        return request;
    }

    private Request buildPostFormRequest(String url,Param[] params) {
        FormBody.Builder builder = new FormBody.Builder();
        for (Param param : params) {
            builder.add(param.key,param.value);
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        return request;
    }

    private void deliveryResult(final ResultCall resultCall, Request request) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedCallback(resultCall,call.request(),e);
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
               try {
                   final String string=response.body().string();
                  if(resultCall.mType==String.class){
                      sendSuccessCallback(resultCall,string);
                  }else {
                      Object o = mGson.fromJson(string, resultCall.mType);
                      sendSuccessCallback(resultCall, o);
                  }
               }catch (IOException e){
                   sendFailedCallback(resultCall,call.request(),e);
               }
            }
        });
    }

    private  void sendFailedCallback(final ResultCall resultCall, final Request request,final Exception e){
/*        mDelivery.post(new Runnable() {
            @Override
            public void run() {*/
                resultCall.onError(request,e);
       /*     }
        });*/
    }

    private void sendSuccessCallback(final ResultCall resultCall,final Object o){
      /*  mDelivery.post(new Runnable() {
            @Override
            public void run() {*/
                resultCall.onResponse(o);
      /*      }
        });*/
    }

    /**
     * 泛型，封装的请求回调
     * @param <T>
     */
   public static abstract  class  ResultCall<T>{
        Type mType;
        public ResultCall()
        {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass)
        {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class)
            {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

       public abstract void onError(Request request,Exception e);
       public abstract void onResponse(T response);
   }

   public static class Param{
       String key;
       String value;
       public Param(String key, String value) {
           this.key = key;
           this.value = value;
       }
   }

}
