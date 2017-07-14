package com.simon.okhttp.Callback;

import okhttp3.Call;
import okhttp3.Request;
import okhttp3.Response;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public abstract class Callback<T> {

    /**
     * @param request
     * @param id
     */
    public void onBefore(Request request,int id){
    }

    public void onAfter(int id){
    }

    public void inProgress(float progress,long total,int id){

    }

    public boolean validateResponse(Response response,int id){
        return  response.isSuccessful();
    }

    public abstract  T parseNetworkResponse(Response response, int id )throws Exception;
    public abstract  void onResponse(T response, int id );
    public abstract  void onError(Call call, Exception e, int id);

    /**
     * 默认的Callback
     */
    public static Callback CALLBACK_DEFAULT=new Callback() {
        @Override
        public Object parseNetworkResponse(Response response, int id) throws Exception {
            return null;
        }

        @Override
        public void onResponse(Object response, int id) {

        }

        @Override
        public void onError(Call call, Exception e, int id){

        }
    };


}
