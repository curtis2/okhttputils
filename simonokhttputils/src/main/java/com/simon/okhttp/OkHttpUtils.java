package com.simon.okhttp;

import com.simon.okhttp.Callback.Callback;
import com.simon.okhttp.builder.GetBuilder;
import com.simon.okhttp.request.RequestCall;
import com.simon.okhttp.utils.Platform;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 * Okhttp封装类
 */
public class OkHttpUtils {

    public static final long DEFAULT_MILLISECONDS = 10_000L;
   public static volatile OkHttpUtils mInstance;

    private OkHttpClient mOkHttpClient;

    Platform platform;

    public OkHttpUtils(OkHttpClient okHttpClient) {
        if(okHttpClient==null){
            mOkHttpClient=new OkHttpClient();
        }else{
            mOkHttpClient=okHttpClient;
        }

        platform=Platform.get();
    }

    public static OkHttpUtils getInstance() {
        return initClient(null);
    }

    private static OkHttpUtils initClient(OkHttpClient OkHttpClient) {
        if(mInstance==null){
            synchronized (OkHttpUtils.class){
               if(mInstance==null){
                   mInstance=new OkHttpUtils(OkHttpClient);
               }
            }
        }
        return  mInstance;
    }

    public OkHttpClient getmOkHttpClient() {
        return mOkHttpClient;
    }

    public static GetBuilder get(){
        return new GetBuilder();
    }

    public void execute(RequestCall requestCall, Callback callback) {
        if(callback==null)callback=Callback.CALLBACK_DEFAULT;
        final Callback finalCallback=callback;
        final  int id=requestCall.getOkHttpRequest().getId();
        requestCall.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
              sendFailResultCallback(call,e,finalCallback,id);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
              try{
                if(call.isCanceled()){
                    sendFailResultCallback(call,new IOException("Canceled!"),finalCallback,id);
                    return;
                }
                if(!finalCallback.validateResponse(response,id)){
                    sendFailResultCallback(call,new IOException("request failed , reponse's code is : " + response.code()),finalCallback,id);
                }

                Object o=finalCallback.parseNetworkResponse(response,id);
                sendSuccessResultCallback(o,finalCallback,id);
              }catch (Exception e){
                  sendFailResultCallback(call,new IOException("request failed , reponse's code is : " + response.code()),finalCallback,id);
              }finally{
                  if(response.body()!=null)response.body().close();
              }
            }
        });
    }

    private void sendSuccessResultCallback(final Object Object, final Callback finalCallback,final int id) {
        if (finalCallback == null) return;
        platform.execute(new Runnable() {
            @Override
            public void run() {
                finalCallback.onResponse(Object,id);
                finalCallback.onAfter(id);
            }
        });
    }

    private void sendFailResultCallback(final Call call,final IOException e, final Callback finalCallback, final  int id) {
        if (finalCallback == null) return;
        platform.execute(new Runnable() {
            @Override
            public void run() {
                finalCallback.onError(call,e,id);
            }
        });
    }


}
