package com.simon.okhttp.callback;

import okhttp3.Response;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public abstract class StringCallback extends Callback<String>{

    @Override
    public String parseResponse(Response response) throws Exception {
        return response.body().string();
    }
}
