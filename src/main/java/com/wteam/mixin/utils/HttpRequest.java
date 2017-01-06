package com.wteam.mixin.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class HttpRequest {
    
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(HttpRequest.class.getName());

    
    /**
     * params = new String[]{"1","2","3","4"}
     * 生成： 1=2&3=4
     * @param params 请求键值对
     * @return
     */
    public static String params(String... params) {
        if (params.length%2 != 0) {
            throw new RuntimeException("键值对不是偶数");
        }
        String param = params[0];
        for (int i = 1; i < params.length; i++ ) {
            if (i%2 != 0) { // 奇数
                param += "=" + params[i];
            }
            else {
                param += "&" + params[i];
            }
        }
        return param;
    }
    
    public static String sendGet(String url, String param){
        return sendGet(url, param,null);
    }
    
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param, Map<String, Object> headers) {

        final Map<String, Object> map = new HashMap<>();
        Stream.of(param.split("&"))
            .map(nameValue -> nameValue.split("="))
            .forEach(arr -> map.put(arr[0], arr[1]));
        
        try {
            headers = headers != null ? headers : Collections.emptyMap();
            return HttpClient.get(url, headers, map);
        }
        catch (URISyntaxException e) {
            LOG.error("{}格式错误", url);
        }
        return HttpClient.EMPTY_STR;
    }

    
    public static String sendPost(String url, String param) {
        try {
            final Map<String, Object> map = new HashMap<>();
            Stream.of(param.split("&"))
                .map(nameValue -> nameValue.split("="))
                .forEach(arr -> map.put(arr[0], arr[1]));
            
            return HttpClient.post(url, map);
        }
        catch (Exception e) {
        }
        return null;
    }
    
    public static String sendPostJSON(String url, String param) {
        return HttpClient.postJson(url, param);
    }
    
    public static String inToString(InputStream inputStream) {
        String result = "";
        BufferedReader in = null;
        try {
            in = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        }
        catch (Exception e) {
            LOG.error("发送GET请求出现异常！", e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                LOG.error("发送GET请求出现异常！", e2);
            }
        }
        return result;
    }
    /**
     * md5加密32位
     * @param strSrc 加密原串
     * @return 加密后的值
     * @throws Exception
     */
    public static String md5(String strSrc) {
        return MD5Utils.md5(strSrc);
    }
    
    public static void main(String[] args) {
        System.out.println(params(new String[]{"1","2","3","4"}));
        System.out.println(md5("asdf") + "->" + md5("asdf").length());
    }
}
