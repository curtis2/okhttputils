package com.simon.okhttp.simple;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.simon.okhttp.OkHttpUtils;
import com.simon.okhttp.callback.BitmapCallback;
import com.simon.okhttp.callback.FileCallback;
import com.simon.okhttp.callback.StringCallback;
import com.simon.okhttp.simple.utils.CrashHandleUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    private TextView contentTv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        contentTv= (TextView) findViewById(R.id.content);
        CrashHandleUtil.getmInstance().init(this,"sample");

    }

    /**
     * http get 请求
     * @param v
     */
    public void getStringRequest(View v){
        String url="http://www.391k.com/api/xapi.ashx/info.json?key=bd_hyrzjjfb4modhj&size=10&page=1";
        OkHttpUtils
             .get(url)
             .execute(new StringCallback() {
                @Override
                public String onResponse(String response, int id) {
                    contentTv.setText(response);
                    return null;
                }
                @Override
                public void onError(okhttp3.Call call, Exception e, int id) {
                    contentTv.setText(e.toString());
                }
        });
    }

    /**
     * 携带参数的http get 请求
     * @param v
     */
    public void getStringWithParamsRequest(View v){
        String url="https://www.baidu.com/";
        Map<String,String> params=new HashMap<>();
        params.put("p","aa");
        params.put("k","bb");
        OkHttpUtils
                .get(url)
                .params(params)
                .execute(new StringCallback() {
                   @Override
                   public String onResponse(String response, int id) {
                       contentTv.setText(response);
                       return null;
                   }

                   @Override
                   public void onError(okhttp3.Call call, Exception e, int id) {
                       contentTv.setText(e.toString());
                   }
        });
    }

    /**
     * post Json 请求
     * @param v
     */
    public void postJson(View v){
        String url="https://www.baidu.com/";
        JSONObject object=new JSONObject();
        try {
            object.put("key",11);
            object.put("value",22);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkHttpUtils
                .post(url)
                .postJson(object.toString())
                .execute(new StringCallback() {
                     @Override
                     public String onResponse(String response, int id) {
                         contentTv.setText(response);
                         return null;
                     }
                     @Override
                     public void onError(okhttp3.Call call, Exception e, int id) {
                         contentTv.setText(e.toString());
                     }
        });
    }
    /**
     * post String 请求
     * @param v
     */
    public void postString(View v){
        String url="https://www.baidu.com/";
        String content="this is test content";
        OkHttpUtils
                .post(url)
                .postString(content)
                .execute(new StringCallback() {
                     @Override
                      public String onResponse(String response, int id) {
                          contentTv.setText(response);
                          return null;
                      }
                      @Override
                      public void onError(okhttp3.Call call, Exception e, int id) {
                          contentTv.setText(e.toString());
                      }
                });
    }

    /**
     * post Bytes 请求
     * @param v
     */
    public void postBytes(View v){
        String url="https://www.baidu.com/";
        String content="this is test content";
        OkHttpUtils
                .post(url)
                .postBytes(content.getBytes())
                .execute(new StringCallback() {
                    @Override
                    public String onResponse(String response, int id) {
                        contentTv.setText(response);
                        return null;
                    }
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        contentTv.setText(e.toString());
                    }
                });
     }

    /**
     * post single file
     * @param v
     */
    public void postFile(View v){
        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
        if (!file.exists())
        {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        String url="https://www.baidu.com/";
        OkHttpUtils
                .post(url)
                .postFile(file)
                .execute(new StringCallback() {
                    @Override
                    public String onResponse(String response, int id) {
                        contentTv.setText(response);
                        return null;
                    }
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        contentTv.setText(e.toString());
                    }
                });
    }

    /**
     * post 表单
     * @param v
     */
    public void postForm(View v){
        String url="https://www.baidu.com/";
        OkHttpUtils
                .post(url)
                .addParams("paramskey1","paramsvalue1")
                .addParams("paramskey2","paramsvalue2")
                .execute(new StringCallback() {
                    @Override
                    public String onResponse(String response, int id) {
                        contentTv.setText(response);
                        return null;
                    }
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        contentTv.setText(e.toString());
                    }
                });
    }

    /**
     * post 多文件上传 ，并回调进度
     *  upProgress 方法为上传进度回调
     * @param v
     */
    public void postMultiFileUpload(View v){
        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
        File file2 = new File(Environment.getExternalStorageDirectory(), "test1#.txt");
        if (!file.exists())
        {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, String> params = new HashMap<>();
        params.put("username", "simone");
        params.put("password", "123");
        String url="https://www.baidu.com/";
            OkHttpUtils
                .post(url)
                .addParams("paramskey1", "paramsvalue1")
                .addParams("paramskey2", "paramsvalue2")
                .addFile("mFile", "messenger_01.png", file)//
                .addFile("mFile", "test1.txt", file2)
                .execute(new StringCallback() {
                    @Override
                    public String onResponse(String response, int id) {
                        contentTv.setText(response);
                        return null;
                    }
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {
                        contentTv.setText(e.toString());
                    }

                    @Override
                    public void upProgress(float progress, long total, int id) {
                        super.upProgress(progress, total, id);
                    }
                });
      }

    /**
     * 下载文件，并有进度
     * downloadProgress为下载进度回调
     * @param v
     */
    public void downFileWithProgress(View v){
        String url="https://www.baidu.com/";
        OkHttpUtils
                .get(url)
                .execute(new FileCallback("文件夹","名称") {
                    @Override
                    public File onResponse(File response, int id) {
                        return null;
                    }
                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }

                    @Override
                    public void downloadProgress(long currentSize, long totalSize, float progress, long networkSpeed) {
                        super.downloadProgress(currentSize, totalSize, progress, networkSpeed);
                    }
                });
    }

    /**
     * 下载图片
     * @param v
     */
    public void getImage(View v){
        String url="https://www.baidu.com/";
        OkHttpUtils
                .get(url)
                .execute(new BitmapCallback() {
                    @Override
                    public Bitmap onResponse(Bitmap response, int id) {
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                });
    }

    /**
     * put请求
     * @param v
     */
    public void putFile(View v){
        String url="https://www.baidu.com/";
        File file = new File(Environment.getExternalStorageDirectory(), "messenger_01.png");
        if (!file.exists())
        {
            Toast.makeText(MainActivity.this, "文件不存在，请修改文件路径", Toast.LENGTH_SHORT).show();
            return;
        }
        OkHttpUtils
                .put(url)
                .postFile(file)
                .execute(new StringCallback() {
                    @Override
                    public String onResponse(String response, int id) {
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                });
    }


    /**
     * delete请求
     * @param v
     */
    public void deleteFile(View v){
        String url="https://www.baidu.com/";
        OkHttpUtils
                .delete(url)
                .execute(new StringCallback() {
                    @Override
                    public String onResponse(String response, int id) {
                        return null;
                    }

                    @Override
                    public void onError(Call call, Exception e, int id) {

                    }
                });
    }

    public static final MediaType STRING_TYPE=  MediaType.parse("text/plain;charset=utf-8");
    /**
     * 自定义请求
     * @param v
     */
    public void customRequest(View v){
        String url="https://www.baidu.com/";
        //自己构建RequestBody
        RequestBody body = RequestBody.create(STRING_TYPE, "this is test content");

        //自己构建requst
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        OkHttpUtils.getmInstance().getOkHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });
    }


}
