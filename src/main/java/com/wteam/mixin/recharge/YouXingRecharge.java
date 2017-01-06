package com.wteam.mixin.recharge;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.constant.Configs;
import com.wteam.mixin.recharge.DeLiRecharge.Response;
/**
 * 友信 流量充值
 * @version 1.0
 * @author benko
 * @time 2016-08-12
 */
public class YouXingRecharge {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(YouXingRecharge.class.getName());

    private static final Map<String, Map<String, String>> CONFIGS = new HashMap<>();
    public static final String youxing = "youxing" ;
    public static final String youxing2 = "youxing2";

//    private String appid = "5f7ffd01bd2e3f987a1e554f6aa543f1";
//    private String key = "5abf5606a27d766e1cc16d681db778a5";
    private String appid = "596ddb6155fba2a4782bac2c4ba98c1a";
    private String key = "ff4e9069c291babd2df755c8bfa9e209";
    private String host = "http://mp.imakebe.com";
    private static String callbackURL = "http://mixinwang.com/recharge/youxing/callback";

    // 初始化
    static { init(); }
    private static void init() {
        Stream.of(youxing,youxing2)
            .forEach(YouXingRecharge::config);
    }

    public static void config(String name) {
        CONFIGS.put(name, new HashMap<>());
        final Properties properties = Configs.prop;
        String appid = null,key = null, host = null;
        if (properties != null) {
            appid = properties.getProperty(name + ".appid");
            key = properties.getProperty(name + ".key");
            host = properties.getProperty(name + ".host");
            CONFIGS.get(name).put("appid", appid);
            CONFIGS.get(name).put("key", key);
            CONFIGS.get(name).put("host", host);
        }
        LOG.debug("name: {},appid:{},password:{}",name,appid,key);
    }

    private YouXingRecharge(String name) {
        if (Configs.prop != null) {
            appid = CONFIGS.get(name).get("appid");
            key = CONFIGS.get(name).get("key");
            host = CONFIGS.get(name).get("host");
        }
    }

    public static YouXingRecharge instance() {
        return new YouXingRecharge(youxing);
    }

    public static YouXingRecharge instance(String name) {
        return new YouXingRecharge(name);
    }


    /**
     * 下单请求
     * @param mobile 充值手机号码
     * @param code 充值产品id，登陆平台在“商品编码”页面获取，充值号码省份、运营商必须与产品对应
     * @param order_sn 请求方的业务订单号码
     * @return
     */
    public Response recharge(String mobile,String code, String order_sn){
        Map<String, String> map = new HashMap<>();

        map.put("mobile", mobile);
        map.put("code", code);
        map.put("order_sn", order_sn);
        map.put("timestamp", timestamp());

        return send("/api/flow/recharge", map);
    }
    /**
     *  订单查询
     * @param p_order_sn 外部订单号
     * @return
     */
    public Response getOrder(String p_order_sn){
        Map<String, String> map = new HashMap<>();

        map.put("p_order_sn", p_order_sn);
        map.put("timestamp", timestamp());

        return send("/api/flow/result", map);
    }
    /**
     * 发送请求
     * @param url
     * @param json
     * @return
     */
    private Response send(String url , Map<String, String> map) {
        // 加密
        map.put("appid", appid);
        String sign = md5(HttpRequest.sortParams(map, null) + "&key=" + key);
        map.put("sign", sign);
        String params = HttpRequest.sortParams(map, null);
        if(LOG.isDebugEnabled()) LOG.debug(params);

        String result = HttpRequest.sendPost(host + url, params);

        if(LOG.isDebugEnabled()) LOG.debug(result);
        try {
            return result == null ? null : JSON.parseObject(result, Response.class);
        }
        catch (Exception e) {
            LOG.error("",e);
        }
        return null;
    }

    private String md5(String source) {
        return HttpRequest.md5(source).toUpperCase();
    }

    private String timestamp() {
        return "" + System.currentTimeMillis()/1000;
    }

    public static class Response {
        private String error;
        private String msg;
        private String p_order_sn;

        // 订单回调信息
        private String order_sn;
        private String order_status;
        private String fail_msg;
        private String sign;

        public String getInfo() {
            return "4".equals(order_status) ? fail_msg : msg;
        }

        public String getError() {
            return error;
        }
        public void setError(String error) {
            this.error = error;
        }
        public String getMsg() {
            return msg;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }
        public String getP_order_sn() {
            return p_order_sn;
        }
        public void setP_order_sn(String p_order_sn) {
            this.p_order_sn = p_order_sn;
        }
        public String getOrder_sn() {
            return order_sn;
        }
        public void setOrder_sn(String order_sn) {
            this.order_sn = order_sn;
        }
        public String getOrder_status() {
            return order_status;
        }
        public void setOrder_status(String order_status) {
            this.order_status = order_status;
        }
        public String getFail_msg() {
            return fail_msg;
        }
        public void setFail_msg(String fail_msg) {
            this.fail_msg = fail_msg;
        }
        public String getSign() {
            return sign;
        }
        public void setSign(String sign) {
            this.sign = sign;
        }

    }

    public static void main(String[] args) {

    }
}
