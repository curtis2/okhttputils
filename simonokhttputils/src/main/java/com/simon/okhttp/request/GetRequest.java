package com.simon.okhttp.request;

import android.net.Uri;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class GetRequest extends OkHttpRequest<GetRequest> {

    public GetRequest(String url) {
        super(url);
    }

    @Override
    protected RequestBody builderRequestBody() {
        return null;
    }

    @Override
    protected Request builderRequest(RequestBody requestBody) {
        if (params != null)
        {
            url = appendParams(url, params);
        }
        return builder.url(url).tag(tag).get().build();
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
