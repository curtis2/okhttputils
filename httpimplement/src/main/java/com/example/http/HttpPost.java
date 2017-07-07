package com.example.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.example.http.HttpServer.HTTP_PORT;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 *
 * http请求，客户端发送类
 */
public class HttpPost {

     public String url;

     private Map<String,String> mParamsMap=new HashMap<>();

     Socket mSocket;

     private HttpPost(String url){
         this.url=url;
     }

     public static void main(String[] args){
         HttpPost httpPost=new HttpPost("172.16.1.148");
         httpPost.addParams("username", "mr.simple");
         httpPost.addParams("pwd", "my_pwd123");
         httpPost.execute();
     }

     public void addParams(String key,String value){
         mParamsMap.put(key,value);
     }

     public void execute(){
        try {
            mSocket=new Socket(url,HTTP_PORT);
            PrintStream outputStream = new PrintStream(mSocket.getOutputStream());
            BufferedReader inputStream = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
            final String boundary="my_boundary_123";
            //写入header
            writeHeader(boundary,outputStream);
            //写入参数
            writeParams(boundary,outputStream);

            waitResponse(inputStream);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            IoUtils.closeSocket(mSocket);
        }
     }

    /**
     * 写入请求头
     * @param boundary
     * @param outputStream
     */
    private void writeHeader(String boundary, PrintStream outputStream) {
        outputStream.println("POST /api/login/  HTTP/1.1");
        outputStream.println("content-length:123");
        outputStream.println("Host:" + this.url + ":" + HttpServer.HTTP_PORT);

        //请求的Content-Type不同，请求参数的格式也不相同
        //如果是multipart/form-data，就需要boundary分割符
        //如果是application/json; charset=utf-8 ,只需要上传对应的json文件即可
        outputStream.println("Content-Type: multipart/form-data; boundary=" + boundary);
        outputStream.println("User-Agent:android");
        //写一个空行
        outputStream.println();
    }

    private void writeParams(String boundary, PrintStream outputStream) {
        Iterator<String> paramsKeySet = mParamsMap.keySet().iterator();
        while (paramsKeySet.hasNext()) {
            String parseName = paramsKeySet.next();
            outputStream.println("--"+boundary);
            outputStream.println("Content-Disposition: form-data; name=" + parseName);
            outputStream.println();
            outputStream.println(mParamsMap.get(parseName));
        }
        outputStream.println("--"+boundary+"--");
    }

    private void waitResponse(BufferedReader inputStream) throws IOException {
        System.out.println("请求结果: ");
        String responseLine = inputStream.readLine();
        while (responseLine == null || !responseLine.contains("HTTP")) {
            responseLine = inputStream.readLine();
        }

        while ((responseLine = inputStream.readLine()) != null) {
            System.out.println(responseLine);
        }
    }


}
