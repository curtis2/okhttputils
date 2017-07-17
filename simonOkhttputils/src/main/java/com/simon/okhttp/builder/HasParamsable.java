package com.simon.okhttp.builder;

import java.util.Map;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public interface HasParamsable {
   OkHttpRequestBuilder params(Map<String,String> params);
   OkHttpRequestBuilder addParams(String key,String value);
}
