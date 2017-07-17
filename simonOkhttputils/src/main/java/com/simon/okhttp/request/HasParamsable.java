package com.simon.okhttp.request;

import java.util.Map;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public interface HasParamsable {
   OkHttpRequest params(Map<String,String> params);
   OkHttpRequest addParams(String key,String value);
}
