package com.simon.okhttp.request;

import com.simon.okhttp.Callback.Callback;
import com.simon.okhttp.OkHttpUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 *
 *  request封装类
 *
 */

public class RequestCall {
    OkHttpClient OkHttpClient;
    OkHttpRequest OkHttpRequest;
    Request request;

    private long readTimeOut;
    private long writeTimeOut;
    private long connTimeOut;

   private OkHttpClient clone;
   private Call call;

    public RequestCall(OkHttpRequest okHttpRequest) {
        this.OkHttpRequest=okHttpRequest;
    }

    public void execute(Callback callback){
       buildCall(callback);
        if(callback!=null){
            callback.onBefore(request,getOkHttpRequest().getId());
        }

        OkHttpUtils.getInstance().execute(this,callback);
    }

    public com.simon.okhttp.request.OkHttpRequest getOkHttpRequest() {
        return OkHttpRequest;
    }

    private Call buildCall(Callback callback) {
       request=OkHttpRequest.generateRequest(callback);
        if(readTimeOut>0||writeTimeOut>0||connTimeOut>0){
          readTimeOut=readTimeOut>0?readTimeOut: OkHttpUtils.DEFAULT_MILLISECONDS;
            writeTimeOut=writeTimeOut>0?writeTimeOut: OkHttpUtils.DEFAULT_MILLISECONDS;
            connTimeOut=connTimeOut>0?connTimeOut: OkHttpUtils.DEFAULT_MILLISECONDS;
            clone=OkHttpUtils.getInstance().getmOkHttpClient().newBuilder().
                    readTimeout(readTimeOut, TimeUnit.MILLISECONDS).
                    writeTimeout(writeTimeOut,TimeUnit.MILLISECONDS).
                    connectTimeout(connTimeOut,TimeUnit.MILLISECONDS).build();
            call=clone.newCall(request);
        }else{
            call=OkHttpUtils.getInstance().getmOkHttpClient().newCall(request);
        }
        return call;
    }


    public Call getCall() {
        return call;
    }
}
