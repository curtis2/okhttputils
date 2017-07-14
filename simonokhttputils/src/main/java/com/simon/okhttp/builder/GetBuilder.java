package com.simon.okhttp.builder;

import android.net.Uri;

import com.simon.okhttp.request.RequestCall;
import com.simon.okhttp.request.GetRequest;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class GetBuilder extends OkHttpRequestBuilder<GetBuilder> implements HasParamsable{

    @Override
    public RequestCall build() {
        //如果get请求有params参数，则拼接这个参数
        if(params!=null){
            url=appendParams(url,params);
        }
        return new GetRequest(url,tag,headers,params,id).bulid();
    }

    private String appendParams(String url, Map<String, String> params) {
       if(url==null||params==null||params.isEmpty()){
           return  url;
       }
        Uri.Builder builder=new Uri.Builder();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            builder.appendQueryParameter(key,params.get(key));
        }
      return  builder.build().toString();
    }

    @Override
    public OkHttpRequestBuilder params(Map<String, String> params) {
        this.params=params;
        return this;
    }

    @Override
    public OkHttpRequestBuilder addParams(String key, String value) {
        if(params==null){
            params=new LinkedHashMap<>();
        }
        params.put(key,value);
        return this;
    }


}
