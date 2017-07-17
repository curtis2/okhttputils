package com.simon.okhttp.request;

import com.simon.okhttp.callback.Callback;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */
public abstract class OkHttpRequest{
    protected int id;
    protected String tag;
    protected String url;
    protected Map<String,String> headers;
    protected Map<String,String> params;
    Request.Builder builder=new Request.Builder();

    public OkHttpRequest(int id, String tag, String url, Map<String, String> headers, Map<String, String> params) {
        this.id = id;
        this.tag = tag;
        this.url = url;
        this.headers = headers;
        this.params = params;
        initBuilder();
    }

    protected  void initBuilder(){
        builder.url(url).tag(tag);
        appendHeaders();
    }

    protected void appendHeaders(){
        Headers.Builder HeadersBuilder=new Headers.Builder();
        if(headers==null||headers.isEmpty())return;

         for(String key:headers.keySet()){
           HeadersBuilder.add(key,headers.get(key));
         }
         builder .headers(HeadersBuilder.build());
    }


    protected abstract RequestBody builderRequestBody();

    protected  RequestBody wrapedRequestBody(RequestBody requestBody,Callback callback){
        return requestBody;
    }

    protected abstract Request builderRequest();

    protected Request generateRequest(Callback callback){
        RequestBody requestBody=builderRequestBody();
        RequestBody wrappedRequestBody=wrapedRequestBody(requestBody,callback);
        Request request=builderRequest();
        return request;
    }

    public int getId() {
        return id;
    }

    public RequestCall build(){
        return new RequestCall(this);
    }

}
