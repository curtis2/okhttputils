package com.simon.okhttp.builder;

import android.net.Uri;

import com.simon.okhttp.request.GetRequest;
import com.simon.okhttp.request.RequestCall;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */
public class GetBulider extends OkHttpRequestBuilder{
    @Override
    public RequestCall build() {
        //如果get请求有参数
        if(params!=null){
            url=appendParams(url,params);
        }
        return new GetRequest(id,tag,url,headers,params).build();
    }

    protected String appendParams(String url, Map<String, String> params)
    {
        if (url == null || params == null || params.isEmpty())
        {
            return url;
        }
        Uri.Builder builder = Uri.parse(url).buildUpon();
        Set<String> keys = params.keySet();
        Iterator<String> iterator = keys.iterator();
        while (iterator.hasNext())
        {
            String key = iterator.next();
            builder.appendQueryParameter(key, params.get(key));
        }
        return builder.build().toString();
    }

}
