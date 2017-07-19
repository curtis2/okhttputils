package com.simon.okhttp.request;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public class PutRequest extends OkHttpRequest<PutRequest> {

    public static final MediaType MEDIA_TYPE_STREAM = MediaType.parse("application/octet-stream");
    private File file;
    protected MediaType mediaType;      //MIME类型

    public PutRequest(String url) {
        super(url);
    }

    /** 注意使用该方法上传文件会清空实体中其他所有的参数，头信息不清除 */
    public PutRequest postFile(File file) {
        this.file = file;
        this.mediaType = MEDIA_TYPE_STREAM;
        return this;
    }

    @Override
    protected RequestBody builderRequestBody() {
        if (file != null && mediaType != null) return RequestBody.create(mediaType, file);         //put上传单个文件
        return null;
    }

    @Override
    protected Request builderRequest(RequestBody wrapedRequestBody) {
        return builder.put(wrapedRequestBody).url(url).tag(tag).build();
    }
}
