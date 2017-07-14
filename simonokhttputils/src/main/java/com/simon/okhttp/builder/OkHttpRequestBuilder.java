package com.simon.okhttp.builder;

import com.simon.okhttp.request.RequestCall;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 *
 */
public abstract class OkHttpRequestBuilder<T extends OkHttpRequestBuilder> {

    protected  String url;
    protected  Object tag;
    protected Map<String,String> headers;
    protected Map<String,String> params;
    protected  int id;

    public T url(String url) {
        this.url = url;
        return (T)this;
    }


    public T tag(Object obj) {
        this.tag = obj;
        return (T)this;
    }


    public T headers(Map<String, String> headers) {
        this.headers = headers;
        return (T)this;
    }

    public  T addHeader(String key,String value){
        if(headers==null){
            headers=new LinkedHashMap<>();
        }
        headers.put(key,value);
        return (T)this;
    }


    public T id(int id) {
        this.id = id;
        return (T)this;
    }

    public  abstract RequestCall build();
}
