package com.simon.okhttp.request;

import com.simon.okhttp.OkHttpUtils;
import com.simon.okhttp.callback.Callback;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class PostRequest extends OkHttpRequest<PostRequest>{
    public static final MediaType MEDIA_TYPE_PLAIN = MediaType.parse("text/plain;charset=utf-8");
    public static final MediaType MEDIA_TYPE_JSON = MediaType.parse("application/json;charset=utf-8");
    public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");
    protected MediaType mediaType;      //上传的MIME类型
    private String string;            //上传的文本内容
    private String json;              //上传的Json
    private byte[] bs;                //上传的字节数据
    private File file;                //上传单个文件
    private List<FileInput> files;

    public PostRequest(String url) {
        super(url);
    }

    /** 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除 */
    public PostRequest postString(String string) {
        this.string = string;
        this.mediaType = MEDIA_TYPE_PLAIN;
        return this;
    }

    /** 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除 */
    public PostRequest postJson(String json) {
        this.json = json;
        this.mediaType = MEDIA_TYPE_JSON;
        return this;
    }

    /** 注意使用该方法上传字符串会清空实体中其他所有的参数，头信息不清除 */
    public PostRequest postBytes(byte[] bs) {
        this.bs = bs;
        this.mediaType = MEDIA_TYPE_STREAM;
        return this;
    }

    /** 注意使用该方法上传文件会清空实体中其他所有的参数，头信息不清除 */
    public PostRequest postFile(File file) {
        this.file = file;
        this.mediaType = MEDIA_TYPE_STREAM;
        return this;
    }

    public PostRequest addFile(String name, String filename, File file) {
        files.add(new FileInput(name, filename, file));
        return this;
    }

    @Override
    protected RequestBody wrapedRequestBody(RequestBody requestBody, final Callback callback) {
        if (callback == null) return requestBody;
        CountingRequestBody  countingRequestBody = new CountingRequestBody(requestBody, new CountingRequestBody.Listener()
        {
            @Override
            public void onRequestProgress(final long bytesWritten, final long contentLength)
            {
                OkHttpUtils.getmInstance().getDelivery().execute(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        callback.upProgress(bytesWritten * 1.0f / contentLength,contentLength,id);
                    }
                });
            }
        });
        return countingRequestBody;
    }

    @Override
    protected Request builderRequest(RequestBody wrapedRequestBody) {
        return builder.post(wrapedRequestBody).url(url).tag(tag).build();
    }

    @Override
    protected RequestBody builderRequestBody() {
        if (string != null && mediaType != null) return RequestBody.create(mediaType, string); //post上传字符串数据
        if (json != null && mediaType != null) return RequestBody.create(mediaType, json);     //post上传json数据
        if (bs != null && mediaType != null) return RequestBody.create(mediaType, bs);         //post上传字节数组
        if (file != null && mediaType != null) return RequestBody.create(mediaType, file);         //post上传单个文件
        if (files == null || files.isEmpty())
        {   //post上传表单
            FormBody.Builder builder = new FormBody.Builder();
            addFromParams(builder);
            FormBody formBody = builder.build();
            return formBody;
        } else
        {
            //post上传多个文件
            MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            addMultipartParams(builder);
            for (int i = 0; i < files.size(); i++)
            {
               FileInput fileInput = files.get(i);
                RequestBody fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileInput.filename)), fileInput.file);
                builder.addFormDataPart(fileInput.key, fileInput.filename, fileBody);
            }
            return builder.build();
        }
    }

    private void addFromParams(FormBody.Builder builder)
    {
        if (params != null)
        {
            for (String key : params.keySet())
            {
                builder.add(key, params.get(key));
            }
        }
    }

    private void addMultipartParams(MultipartBody.Builder builder)
    {
        if (params != null && !params.isEmpty())
        {
            for (String key : params.keySet())
            {
                builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + key + "\""),
                        RequestBody.create(null, params.get(key)));
            }
        }
    }

    private String guessMimeType(String path)
    {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = null;
        try
        {
            contentTypeFor = fileNameMap.getContentTypeFor(URLEncoder.encode(path, "UTF-8"));
        } catch (UnsupportedEncodingException e)
        {
            e.printStackTrace();
        }
        if (contentTypeFor == null)
        {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    public static class FileInput
    {
        public String key;
        public String filename;
        public File file;

        public FileInput(String name, String filename, File file)
        {
            this.key = name;
            this.filename = filename;
            this.file = file;
        }

        @Override
        public String toString()
        {
            return "FileInput{" +
                    "key='" + key + '\'' +
                    ", filename='" + filename + '\'' +
                    ", file=" + file +
                    '}';
        }
    }

}
