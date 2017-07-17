package com.simon.okhttp.request;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class GetRequest extends OkHttpRequest {

    public GetRequest(int id, String tag, String url, Map<String, String> headers, Map<String, String> params) {
        super(id, tag, url, headers, params);
    }

    @Override
    protected RequestBody builderRequestBody() {
        return null;
    }

    @Override
    protected Request builderRequest() {
        return builder.get().build();
    }

}
