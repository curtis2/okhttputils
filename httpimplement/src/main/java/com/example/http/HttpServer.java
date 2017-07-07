package com.example.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 * auther: elliott zhang
 * Emaill:18292967668@163.com
 */
public class HttpServer extends Thread{
    public HttpServer() {
        try {
            socket=new ServerSocket(HTTP_PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        new HttpServer().start();
    }

    ServerSocket socket;
    public static final int HTTP_PORT=9999;

    @Override
    public void run() {
        super.run();
        try {
            while (true){
                new DeliverThread(socket.accept()).start();
            }
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class DeliverThread extends Thread{
        Socket clientSocket;
        BufferedReader mInputStream;
        PrintStream  mPrintStream;
        /**
         *请求方法
         */
        String httpMethod;
        /**
         *子路径
         */
        String subPath;

        /**
         *分割符（请求参数分割符）
         */
        String boundary;

        /**
         *请求参数
         */
        Map<String,String> mParams=new HashMap<>();
        /**
         * 请求方法
         */
        Map<String,String> mHeaders=new HashMap<>();

        /**
         * 请求头是否解析完成
         */
        private boolean isParseHeader;

        public DeliverThread(Socket accept) {
            this.clientSocket=accept;
        }

        @Override
        public void run() {
            super.run();
            try {
                mInputStream=new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                mPrintStream=new PrintStream(clientSocket.getOutputStream());
                parseRequest();
                handlerResponse();
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                IoUtils.closeQuitly(mInputStream);
                IoUtils.closeQuitly(mPrintStream);
                IoUtils.closeSocket(clientSocket);
            }
        }

        /**
         * 解析请求
         */
        private void parseRequest() {
            String line;
            int lineNumber=0;
            try {
                while((line=mInputStream.readLine())!=null){
                    if(lineNumber==0){
                        parseRequestLine(line);
                    }
                    if(isEnd(line)){
                       break;
                    }
                    if(lineNumber!=0&&!isParseHeader){
                       parseHeader(line);
                    }
                    if(isParseHeader){
                         parseRequestParams(line);
                    }
                    lineNumber++;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         *解析起始行
         * @param line
         */
        private void parseRequestLine(String line) {
            //请求行的格式   GET  /?P=909   HTTP/1.1
            String[] tempStrs = line.split(" ");
             httpMethod=tempStrs[0];
             subPath=tempStrs[1];
            System.out.println("请求行，请求方式："+tempStrs[0]+", 子路径 ："+tempStrs[1]+",HTTP版本："+tempStrs[2]);
        }

        /**
         * 解析请求头
         * @param headerline
         */
        private void parseHeader(String headerline) {
            if(headerline.equals("")){
                isParseHeader=true;
                System.out.println(" - - - - - - - - - - ->"+"header 解析完成\n");
            }else if(headerline.contains("boundary")){
                boundary= parseSecondField(headerline);
            }else{
                parseHeaderParams(headerline);
            }
        }

        /**
         * 解析请求头参数
         * @param headerline
         */
        private void parseHeaderParams(String headerline) {
            String[] keyValues = headerline.split(":");
            mHeaders.put(keyValues[0].trim(),keyValues[1].trim());
            System.out.println("header参数名 ："+keyValues[0].trim()+"header参数值 ："+keyValues[1].trim());

        }

        /**
         * 解析请求头中的boundary参数
         * @param headerline
         * @return
         */
        private String parseSecondField(String headerline) {
            String[] headerArray = headerline.split(";");
            parseHeaderParams(headerArray[0]);
            if(headerArray.length>1){
                return  headerArray[1].split("=")[1];
            }
            return "";
        }

        /**
         * 解析请求参数
         * 请求头的格式
         *  --boundary
         *  请求参数
         *  请求参数
         *  请求参数
         *  ...
         *  空行
         *  -
         *  例：
         *   --OCqxMF6-JxtxoMDHMoG5W5eY9MgRsTBP
         *   Content-Dispositon:form-data;name="username"
         *   Content-Type:test/plain;charset=UTF-8;
         *   Content-Transfer-Encoding:8bit
         *
         *   Mr.Simple
         *
         * @param paramsLine
         */
        private void parseRequestParams(String paramsLine) throws IOException {
            if(paramsLine.equals("--"+boundary)){
                String contentDispostion=mInputStream.readLine();
                String paramName=parseSecondField(contentDispostion);
                mInputStream.readLine();
                String paramValue = mInputStream.readLine();
                mParams.put(paramName, paramValue);
                System.out.println("参数名 : " + paramName + ", 参数值 : " + paramValue);
            }
        }

        /**
         * 是否为请求结束
         * @param line
         * @return
         */
        private boolean isEnd(String line) {
           return line.equals("--"+boundary+"--");
        }

        /**
         * http响应头的格式
         *  响应行
         *  header区域
         *  空行
         *  响应数据
         */
        private void handlerResponse() {
            sleep();
            mPrintStream.println("HTTP/1.1 200 OK");
            mPrintStream.println("Content-Type: application/json");
            mPrintStream.println();
            mPrintStream.println("{\"stCode\":\"success\"}");
        }

        private void sleep() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }




}
