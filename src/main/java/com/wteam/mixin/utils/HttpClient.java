package com.wteam.mixin.utils;

import java.util.Map;
import java.io.IOException;  
import java.io.UnsupportedEncodingException;  
import java.net.URISyntaxException;  
import java.util.ArrayList;  
  
import org.apache.http.HttpEntity;  
import org.apache.http.NameValuePair;  
import org.apache.http.client.ClientProtocolException;  
import org.apache.http.client.entity.UrlEncodedFormEntity;  
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;
import org.apache.http.client.methods.HttpGet;  
import org.apache.http.client.methods.HttpPost;  
import org.apache.http.client.methods.HttpRequestBase;  
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;  
import org.apache.http.impl.client.HttpClients;  
  
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;  
import org.apache.http.message.BasicNameValuePair;  
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;  


public class HttpClient {

    /**
     * log4j实例对象.
     */
    private final static Logger LOG = LogManager.getLogger(HttpRequest.class.getName());
    
    private static PoolingHttpClientConnectionManager cm;  
    public static final String EMPTY_STR = "";  
    private static final String UTF_8 = "UTF-8";  
  
    /** 
     *  
     * @param url 
     * @return 
     */  
    public static String get(String url) {  
        if(LOG.isDebugEnabled()) LOG.debug("url:{}", url);
        HttpGet httpGet = new HttpGet(url);  
        return getResult(httpGet);  
    }  
  
    /**
     * 
     * @param url
     * @param params
     * @return
     * @throws URISyntaxException
     */
    public static String get(String url, Map<String, Object> params) throws URISyntaxException {  
        if(LOG.isDebugEnabled()) LOG.debug("url:{}, params:{}", url, params);
        URIBuilder ub = new URIBuilder();  
        ub.setPath(url);  
  
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);  
        ub.setParameters(pairs);  
  
        HttpGet httpGet = new HttpGet(ub.build());  
        return getResult(httpGet);  
    }  
  
    /**
     * 
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws URISyntaxException
     */
    public static String get(String url, Map<String, Object> headers, Map<String, Object> params)  
            throws URISyntaxException {  
        if(LOG.isDebugEnabled()) LOG.debug("url:{}, headers:{}, params:{}", url, headers,params);
        URIBuilder ub = new URIBuilder();  
        ub.setPath(url);  
  
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);  
        ub.setParameters(pairs);  
  
        HttpGet httpGet = new HttpGet(ub.build());  
        for (Map.Entry<String, Object> param : headers.entrySet()) {  
            httpGet.addHeader(param.getKey(), String.valueOf(param.getValue()));  
        }  
        return getResult(httpGet);  
    }  

    /**
     * 
     * @param url
     * @param json
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String postJson(String url, String json) {  
        if(LOG.isDebugEnabled()) LOG.debug("url:{}, json:{}", url, json);
        HttpPost httpPost = new HttpPost(url);  
        StringEntity entity = new StringEntity(json, UTF_8);
        entity.setContentEncoding("UTF-8");    
        entity.setContentType("application/json");  
        
        httpPost.setEntity(entity);  
        return getResult(httpPost);  
    }
    
    /**
     * 
     * @param url
     * @param xml
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String postXml(String url, String xml) {  
        if(LOG.isDebugEnabled()) LOG.debug("url:{}, json:{}", url, xml);
        HttpPost httpPost = new HttpPost(url);  
        StringEntity entity = new StringEntity(xml, UTF_8);
        entity.setContentEncoding("UTF-8");    
        entity.setContentType("text/xml");  
        
        httpPost.setEntity(entity);  
        return getResult(httpPost);  
    }
    
    public static String post(String url) {  
        HttpPost httpPost = new HttpPost(url);  
        return getResult(httpPost);  
    }  
    /**
     * 
     * @param url
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String post(String url, Map<String, Object> params) throws UnsupportedEncodingException {  
        if(LOG.isDebugEnabled()) LOG.debug("url:{}, params:{}", url, params);
        HttpPost httpPost = new HttpPost(url);  
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);  
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));  
        return getResult(httpPost);  
    }  
  
    /**
     * 
     * @param url
     * @param headers
     * @param params
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String post(String url, Map<String, Object> headers, Map<String, Object> params)  
            throws UnsupportedEncodingException {  
        if(LOG.isDebugEnabled()) LOG.debug("url:{}, headers:{}, params:{}", url, headers,params);
        HttpPost httpPost = new HttpPost(url);  
  
        for (Map.Entry<String, Object> param : headers.entrySet()) {  
            httpPost.addHeader(param.getKey(), String.valueOf(param.getValue()));  
        }  
  
        ArrayList<NameValuePair> pairs = covertParams2NVPS(params);  
        httpPost.setEntity(new UrlEncodedFormEntity(pairs, UTF_8));  
  
        return getResult(httpPost);  
    }  
    
    private static void init() {  
        synchronized(HttpClient.class){  
            if (cm == null) {  
                cm = new PoolingHttpClientConnectionManager();  
                cm.setMaxTotal(50);// 整个连接池最大连接数  
                cm.setDefaultMaxPerRoute(5);// 每路由最大连接数，默认值是2  
            }  
        } 
    }  
  
    /** 
     * 通过连接池获取HttpClient 
     *  
     * @return 
     */  
    private static CloseableHttpClient getHttpClient() {  
        init();  
        return HttpClients.custom().setConnectionManager(cm).build();  
    }  
    
    private static ArrayList<NameValuePair> covertParams2NVPS(Map<String, Object> params) { 
        ArrayList<NameValuePair> pairs = new ArrayList<NameValuePair>();  
        for (Map.Entry<String, Object> param : params.entrySet()) {  
            pairs.add(new BasicNameValuePair(param.getKey(), String.valueOf(param.getValue())));  
        }  
  
        return pairs;  
    }  
  
    /** 
     * 处理Http请求 
     *  
     * @param request 
     * @return 
     */  
    private static String getResult(HttpRequestBase request) {  
        // CloseableHttpClient httpClient = HttpClients.createDefault();  
        CloseableHttpResponse response = null;
        try {  
            CloseableHttpClient httpClient = getHttpClient(); 
            LOG.debug("uri: "+ request.getURI());
            if (request instanceof HttpEntityEnclosingRequestBase) {
                LOG.debug("params: "+ ((HttpEntityEnclosingRequestBase)request).getEntity());
            }
            
            response = httpClient.execute(request);  
            // response.getStatusLine().getStatusCode();  
            HttpEntity entity = response.getEntity();  
            if (entity != null) {  
                // long len = entity.getContentLength();// -1 表示长度未知  
                String result = EntityUtils.toString(entity);  
                response.close();  
                // httpClient.close();  
                return result;  
            }  
        } catch (ClientProtocolException e) {  
            LOG.error(request.getURI(),e); 
        } catch (IOException e) { 
            LOG.error(request.getURI(),e);   
        } catch (Exception e) { 
            LOG.error(request.getURI(),e);   
        } finally {  
        
        }
  
        return EMPTY_STR;  
    }  
}
