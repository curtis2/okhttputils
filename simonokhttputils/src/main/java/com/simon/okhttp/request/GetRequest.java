package com.simon.okhttp.request;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class GetRequest extends OkHttpRequest {

    public GetRequest(String url, Object tag, Map<String, String> headers, Map<String, String> params, int id) {
        super(url, tag, headers, params, id);
    }

    @Override
    protected Request builderRequest(RequestBody requestBody) {
        return builder.get().build();
    }

    @Override
    protected RequestBody builderRequestBody() {
        return null;
    }
}
