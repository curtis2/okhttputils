package com.simon.okhttp.request;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class DeleteRequest extends OkHttpRequest<DeleteRequest>{

    public DeleteRequest(String url) {
        super(url);
    }

    @Override
    protected RequestBody builderRequestBody() {
        return null;
    }

    @Override
    protected Request builderRequest(RequestBody wrapedRequestBody) {
        return builder.delete().url(url).tag(tag).build();
    }
}
