package com.simon.okhttp.request;

import com.simon.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.simon.okhttp.OkHttpUtils.getmInstance;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */
public abstract class OkHttpRequest<T extends OkHttpRequest>{
    private long readTimeOut;
    private long whiteTimeOut;
    private long connTimeOut;
    public static final long DEFAULT_MILLISECONDS = 10_000L;

    protected int id;
    protected String tag;
    protected String url;
    protected Map<String,String> headers;
    protected Map<String,String> params;

    Request.Builder builder=new Request.Builder();
    private Request request;
    private Call call;

    public OkHttpRequest(String url) {
        this.url=url;
    }

    public T headers(Map<String, String> headers) {
        this.headers = headers;
        return (T)this;
    }

    public T addHeaders(String key,String value) {
        if(headers==null){
            headers=new HashMap<>();
        }
        headers.put(key,value);
        return (T)this;
    }

    public T params(Map<String, String> params) {
        this.params = params;
        return (T)this;
    }

    public T addParams(String key, String value) {
        if(params==null){
            params=new HashMap<>();
        }
        params.put(key,value);
        return (T)this;
}


    public T id(int id) {
        this.id = id;
        return (T)this;
    }

    public T tag(String tag) {
        this.tag = tag;
        return (T)this;
    }

    public T readTimeOut(long readTimeOut) {
        this.readTimeOut = readTimeOut;
        return (T)this;
    }

    public T whiteTimeOut(long whiteTimeOut) {
        this.whiteTimeOut = whiteTimeOut;
        return (T)this;
    }

    public T connTimeOut(long connTimeOut) {
        this.connTimeOut = connTimeOut;
        return (T)this;
    }

    public int getId() {
        return id;
    }

    protected void appendHeaders(){
        Headers.Builder HeadersBuilder=new Headers.Builder();
        if(headers==null||headers.isEmpty())return;

        for(String key:headers.keySet()){
            HeadersBuilder.add(key,headers.get(key));
        }
        builder.headers(HeadersBuilder.build());
    }


    protected abstract RequestBody builderRequestBody();

    protected  RequestBody wrapedRequestBody(RequestBody requestBody,Callback callback){
        return requestBody;
    }

    protected abstract Request builderRequest(RequestBody wrapedRequestBody);

    protected Request generateRequest(Callback callback){
        appendHeaders();
        RequestBody requestBody=builderRequestBody();
        RequestBody wrappedRequestBody=wrapedRequestBody(requestBody,callback);
        Request request=builderRequest(wrappedRequestBody);
        return request;
    }

    public void execute(Callback callback){
        buildCall(callback);
        if(callback!=null){
            callback.onBefore(request,getId());
        }
        getmInstance().execute(this,callback);
    }

    private Call buildCall(Callback callback) {
        request=generateRequest(callback);
        if(readTimeOut>0||whiteTimeOut>0||connTimeOut>0){
            call= getmInstance().getOkhttpClient().newBuilder().
                    readTimeout(readTimeOut>0?readTimeOut:DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
                    .writeTimeout(whiteTimeOut>0?whiteTimeOut:DEFAULT_MILLISECONDS, TimeUnit.SECONDS)
                    .connectTimeout(connTimeOut>0?connTimeOut:DEFAULT_MILLISECONDS, TimeUnit.SECONDS).build().newCall(request);
        }else{
            call= getmInstance().getOkhttpClient().newCall(request);
        }
        return call;
    }

    public Call getCall() {
        return call;
    }

}
