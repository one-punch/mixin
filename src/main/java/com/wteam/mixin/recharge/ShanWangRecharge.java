package com.wteam.mixin.recharge;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.constant.Configs;

public class ShanWangRecharge {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(ShanWangRecharge.class.getName());
    
    private static final Map<String, Map<String, String>> CONFIGS = new HashMap<>(); 
    public static final String shanwang = "shanwang" ;
    
    private String uid = "129";
    private String key = "4f53cec206a645e2b801dff91e2da8c4";
    private static String callbackURL = "http://mixinwang.com/recharge/shanwang/callback";
    // 初始化
    static { init(); }
    
    public static void init() {
        Stream.of(shanwang)
            .forEach(ShanWangRecharge::config);
    }
    
    public static void config(String name) {
        Utils.config(CONFIGS, name, "uid", "key");
    }
    
    private ShanWangRecharge(String name) {
        if (Configs.prop != null) {
            uid = CONFIGS.get(name).get("uid");
            key = CONFIGS.get(name).get("key");
        }
    }
    
    public static ShanWangRecharge instance() {
        return new ShanWangRecharge(shanwang);
    }
    public static ShanWangRecharge instance(String name) {
        return new ShanWangRecharge(name);
    }
    public Response recharge(String outid,String mobile,String pid) {
        Request request = new Request();
        String 
            timestamp = timeStamp(),signature = sha1(timestamp),
            url = String.format("http://api.mytzb.com/api/SendPackage.aspx?uid=%s&ts=%s", uid, timestamp);
        request.setMobile(mobile);
        request.setOutid(outid);
        request.setPid(pid);
        request.setSignature(signature);
        
        return send(url, request);
    }
    
    /**
     * 发送请求
     * @param url
     * @param json
     * @return
     */
    private Response send(String url , Request request) {
        String params = JSON.toJSONString(request);
        if(LOG.isDebugEnabled()) LOG.debug("url:{}, params:{}",url, params);

        String result = HttpRequest.sendPostJSON(url, params);
        if(LOG.isDebugEnabled()) LOG.debug("result:{}",result);
        try {
            
            if (result == null || "".equals(result)) {
                return null;
            }
            else {
                return JSON.parseObject(result, Response.class);
            }
        }
        catch (Exception e) {
            LOG.error("",e);
        }
        return null;
    }
    
    private String sha1(String timestamp) {
        return DigestUtils.sha1Hex(timestamp + key).toLowerCase();
    }


    private String timeStamp() {
        return System.currentTimeMillis()/1000 + "";
    }

    public static class Request {
        private String signature;
        private String outid;
        private String mobile;
        private String pid;
        
        public String getSignature() {
            return signature;
        }
        public void setSignature(String signature) {
            this.signature = signature;
        }
        public String getOutid() {
            return outid;
        }
        public void setOutid(String outid) {
            this.outid = outid;
        }
        public String getMobile() {
            return mobile;
        }
        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
        public String getPid() {
            return pid;
        }
        public void setPid(String pid) {
            this.pid = pid;
        }
    }
    public static class Response {
        private String code;
        private String msg;
        private Data data;
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getMsg() {
            return msg;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }
        public Data getData() {
            return data;
        }
        public void setData(Data data) {
            this.data = data;
        }
        
    }
    
    public static class Data {

        private String orderid;
        private String outid;
        private String status;
        private String reason;
        public String getOrderid() {
            return orderid;
        }
        public void setOrderid(String orderid) {
            this.orderid = orderid;
        }
        public String getOutid() {
            return outid;
        }
        public void setOutid(String outid) {
            this.outid = outid;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public String getReason() {
            return reason;
        }
        public void setReason(String reason) {
            this.reason = reason;
        }
    }
}
