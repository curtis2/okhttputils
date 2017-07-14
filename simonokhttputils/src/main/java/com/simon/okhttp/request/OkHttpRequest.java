package com.simon.okhttp.request;

import com.simon.okhttp.Callback.Callback;
import com.simon.okhttp.utils.Exceptions;

import java.util.Map;

import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public abstract class OkHttpRequest{
    protected  String url;
    protected  Object tag;
    protected  Map<String,String> headers;
    protected  Map<String,String> params;
    protected  int id;
    protected  Request.Builder builder = new Request.Builder();

    public OkHttpRequest(String url, Object tag, Map<String, String> headers, Map<String, String> params, int id) {
        this.url = url;
        this.tag = tag;
        this.headers = headers;
        this.params = params;
        this.id = id;
        if(url==null){
           Exceptions.illegalArgument("url not be able null ");
        }
        initBuilder();
    }

    public Request generateRequest(Callback Callback){
        RequestBody requestBody=builderRequestBody();
        RequestBody wrappedRequestBody=wrapRequestBody(requestBody,Callback);
        Request request=builderRequest(wrappedRequestBody);
        return  request;
    }

    protected abstract Request builderRequest(RequestBody requestBody);

    protected  RequestBody wrapRequestBody(RequestBody requestBody,Callback Callback){
        return  requestBody;
    }

    protected abstract RequestBody builderRequestBody();

    public RequestCall bulid(){
        return  new RequestCall(this);
    }

    /**
     * 初始化builder的相关设置
     */
    protected void initBuilder(){
        builder.url(url).tag(tag);
        appendHeaders();
    }

    protected void appendHeaders(){
        Headers.Builder HeadersBuilder=new Headers.Builder();
        if(headers==null||headers.isEmpty())return;
        for (String key:headers.keySet()) {
            HeadersBuilder.add(key,headers.get(key));
        }
        builder.headers(HeadersBuilder.build());
    }

    public int getId() {
        return id;
    }


}
