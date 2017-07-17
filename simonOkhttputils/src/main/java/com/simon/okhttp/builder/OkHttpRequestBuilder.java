package com.simon.okhttp.builder;

import com.simon.okhttp.request.RequestCall;

import java.util.HashMap;
import java.util.Map;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder> {
    protected int id;
    protected String tag;
    protected String url;
    protected Map<String,String> params;
    protected Map<String,String> headers;

    public T headers(Map<String, String> headers) {
        this.headers = headers;
        return (T)this;
    }

    public T params(Map<String, String> headers) {
        this.params = headers;
        return (T)this;
    }

    public T addHeaders(String key,String value) {
        if(headers==null){
            headers=new HashMap<>();
        }
        headers.put(key,value);
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

    public T url(String url) {
        this.url = url;
        return (T)this;
    }

    public abstract RequestCall build();

}
