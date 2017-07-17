package com.simon.okhttp.request;

import com.simon.okhttp.callback.Callback;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */
public abstract class OkHttpRequest <T extends OkHttpRequest>{

    protected int id;
    protected String tag;
    protected String url;
    protected Map<String,String> headers;
    protected Map<String,String> params;
    Request.Builder builder=new Request.Builder();

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

    public T params(Map<String, String> headers) {
        this.params = headers;
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

    public RequestCall build(){
        return new RequestCall(this);
    }

}
