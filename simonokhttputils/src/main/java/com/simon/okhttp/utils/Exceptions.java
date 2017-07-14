package com.simon.okhttp.utils;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class Exceptions {

    public static void illegalArgument(String msg,Object ...params){
        throw  new IllegalArgumentException(String .format(msg,params));
    }

}
