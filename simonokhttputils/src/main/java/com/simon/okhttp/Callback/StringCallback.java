package com.simon.okhttp.Callback;

import okhttp3.Response;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public abstract  class StringCallback extends Callback<String> {
    @Override
    public String parseNetworkResponse(Response response, int id) throws Exception {
        return response.body().string();
    }

}
