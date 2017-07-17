package com.simon.okhttp.request;

import com.simon.okhttp.OkHttpUtils;
import com.simon.okhttp.callback.Callback;

import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Request;

import static com.simon.okhttp.OkHttpUtils.getmInstance;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */
public class RequestCall {
    public static final long DEFAULT_MILLISECONDS = 10_000L;
    private  OkHttpRequest okHttpRequest;
    private Request request;
    private Call call;
    private long readTimeOut;
    private long whiteTimeOut;
    private long connTimeOut;

    public RequestCall readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return this;
    }

    public RequestCall whiteTimeOut(long whiteTimeOut) {
        this.whiteTimeOut = whiteTimeOut;
        return this;
    }

    public RequestCall connTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return this;
    }

    public RequestCall(OkHttpRequest okHttpRequest) {
       this.okHttpRequest=okHttpRequest;
    }

    public void execute(Callback callback){
        buildCall(callback);
        if(callback!=null){
            callback.onBefore(request,okHttpRequest.getId());
        }
        OkHttpUtils.getmInstance().execute(this,callback);
    }

    private Call buildCall(Callback callback) {
        request=generateRequest(callback);
        if(readTimeOut>0||whiteTimeOut>0||connTimeOut>0){
           call= OkHttpUtils.getmInstance().getOkhttpClient().newBuilder().
                    readTimeout(readTimeOut>0?readTimeOut:DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
                    .writeTimeout(whiteTimeOut>0?whiteTimeOut:DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
                    .connectTimeout(connTimeOut>0?connTimeOut:DEFAULT_MILLISECONDS, TimeUnit.SECONDS).build().newCall(request);
        }else{
            call= getmInstance().getOkhttpClient().newCall(request);
        }
        return call;
    }

    private Request generateRequest(Callback callback) {
        return  okHttpRequest.generateRequest(callback);
    }

    public OkHttpRequest getOkHttpRequest() {
        return okHttpRequest;
    }

    public Request getRequest() {
        return request;
    }

    public Call getCall() {
        return call;
    }
}
