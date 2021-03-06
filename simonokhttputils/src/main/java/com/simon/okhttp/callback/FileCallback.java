package com.simon.okhttp.callback;

import android.support.annotation.NonNull;

import com.simon.okhttp.OkHttpUtils;
import com.simon.okhttp.utils.L;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Response;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */

public abstract class FileCallback extends Callback<File>{
    /** 目标文件存储的文件夹路径 */
    private String destFileDir;
    /** 目标文件存储的文件名 */
    private String destFileName;

    /**
     * @param destFileDir  要保存的目标文件夹
     * @param destFileName 要保存的文件名
     */
    public FileCallback(@NonNull String destFileDir, @NonNull String destFileName) {
        this.destFileDir = destFileDir;
        this.destFileName = destFileName;
    }

    @Override
    public File parseResponse(Response response) throws Exception {
        return saveFile(response);
    }
    private File saveFile(Response response) throws IOException {
        File dir = new File(destFileDir);
        if (!dir.exists()) dir.mkdirs();
        File file = new File(dir, destFileName);
        if (file.exists()) file.delete();

        long lastRefreshUiTime = 0;  //最后一次刷新的时间
        long lastWriteBytes = 0;     //最后一次写入字节数据

        InputStream is = null;
        byte[] buf = new byte[2048];
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;
            int len;
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);
                final long finalSum = sum;

                long curTime = System.currentTimeMillis();
                //每200毫秒刷新一次数据
                if (curTime - lastRefreshUiTime >= 200 || finalSum == total) {
                    //计算下载速度
                    long diffTime = (curTime - lastRefreshUiTime) / 1000;
                    if (diffTime == 0) diffTime += 1;
                    long diffBytes = finalSum - lastWriteBytes;
                    final long networkSpeed = diffBytes / diffTime;
                    OkHttpUtils.getmInstance().getDelivery().execute(new Runnable() {
                        @Override
                        public void run() {
                            downloadProgress(finalSum, total, finalSum * 1.0f / total, networkSpeed);   //进度回调的方法
                        }
                    });

                    lastRefreshUiTime = System.currentTimeMillis();
                    lastWriteBytes = finalSum;
                }
            }
            fos.flush();
            return file;
        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
                L.e(e.toString());
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
                L.e(e.toString());
            }
        }
    }
}
