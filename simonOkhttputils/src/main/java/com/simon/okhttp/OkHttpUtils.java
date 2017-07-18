package com.simon.okhttp;

import com.simon.okhttp.callback.Callback;
import com.simon.okhttp.request.DeleteRequest;
import com.simon.okhttp.request.GetRequest;
import com.simon.okhttp.request.OkHttpRequest;
import com.simon.okhttp.request.PostRequest;
import com.simon.okhttp.request.PutRequest;
import com.simon.okhttp.utils.Platform;

import java.io.IOException;
import java.util.concurrent.Executor;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */
public class OkHttpUtils {
    private OkHttpClient okHttpClient;
    private static OkHttpUtils mInstance;
    Platform platform;

    private OkHttpUtils(OkHttpClient okhttpClient) {
        if(okhttpClient==null){
            this.okHttpClient =new OkHttpClient();
        }else{
            this.okHttpClient =okhttpClient;
        }
        platform=Platform.get();
    }

    public static OkHttpUtils getmInstance(){
      if (mInstance==null){
          synchronized (OkHttpUtils.class){
              if(mInstance==null){
                  return new OkHttpUtils(null);
              }
          }
      }
      return mInstance;
    }


    public void execute(final OkHttpRequest okHttpRequest, Callback callback) {
        if(callback==null) callback=Callback.CALLBACK_DEFAULT;
        final Callback  finalCallback=callback;
        final int id = okHttpRequest.getId();
        okHttpRequest.getCall().enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
               sendFaildResultCallback(call, e,finalCallback,id);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(call.isCanceled()){
                    sendFaildResultCallback(call,new IOException("canceled!"),finalCallback,id);
                }

                if(!response.isSuccessful()){
                    sendFaildResultCallback(call,new IOException("request failed!"),finalCallback,id);
                }

                try {
                    Object o = finalCallback.parseResponse(response);
                    sendSuccessResultCallback(o,finalCallback,id);
                }catch (Exception e){
                    e.printStackTrace();
                }finally {
                    if (response.body() != null)
                        response.body().close();
                }
            }
        });
    }

    private void sendSuccessResultCallback(final Object o,final Callback finalCallback,final int id) {
        platform.execute(new Runnable() {
            @Override
            public void run() {
                finalCallback.onResponse(o,id);
                finalCallback.onAfter(id);
            }
        });
    }

    private void sendFaildResultCallback(final Call call,final IOException e, final Callback finalCallback, final int id) {
        platform.execute(new Runnable() {
            @Override
            public void run() {
                finalCallback.onError(call,e,id);
                finalCallback.onAfter(id);
            }
        });
    }

    /**
     * 对外暴露的方法
     * @return
     */
    public OkHttpClient getOkHttpClient() {
        return okHttpClient;
    }
    public static GetRequest get(String url){
        return new GetRequest(url);
    }
    public static PostRequest post(String url){
        return new PostRequest(url);
    }

    public static PutRequest put(String url){
        return new PutRequest(url);
    }
    public static DeleteRequest delete(String url){
        return new DeleteRequest(url);
    }

    public Executor getDelivery() {
        return platform.defaultCallbackExecutor();
    }
}
