package com.wteam.mixin.recharge;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.constant.Configs;
import com.wteam.mixin.utils.Utils;

/**
 * 尚通
 * @author benko
 *
 */
public class ShangTongRecharge {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(ShangTongRecharge.class.getName());
    // 初始化
    static { init(); }

    private static String account = "KMXHJJYU";
    private static String apikey = "fg1fkymatqKyjXG6WGkuTihD4SYqfenP";
    private static String callbackURL = "http://mixinwang.com/recharge/shangtong/callback";

    private static void init() {
        Properties properties = Configs.prop;
        if (properties != null) {
            account = properties.getProperty("shangtong.account");
            apikey = properties.getProperty("shangtong.apikey");
            //callbackURL = properties.getProperty("shangtong.callbackURL");
        }
        LOG.debug("properties: {},account:{},password:{}",properties,account);
    }

    private ShangTongRecharge() {}

    public static ShangTongRecharge instance() {
        return new ShangTongRecharge();
    }

    /**
     *  下单请求
     *
     * @param orderNo 下游企业提供唯一订单号
     * @param phone
     * @param size 流量包大小（传产品编码具体见文档下面）
     * @param range 使用范围。0 :全国; 1 :省内;
     * @return
     */
    public Response recharge(String orderNo,String phone,String size, String range) {
        Map<String, String> map = new HashMap<>();

        map.put("action", "Charge");
        map.put("phone", phone);
        map.put("size", size);
        map.put("range", range);
        map.put("orderNo", orderNo);
        map.put("timeStamp", timeStamp());
        map.put("account", account);
        String sign = HttpRequest.md5(apikey + HttpRequest.sortParams(map, Arrays.asList("orderNo")) + apikey);
        map.put("sign", sign);

        return send("http://api.liuliang.net.cn/Submit.php", map);
    }
    /**
     *  订单查询
     * @param orderID 订购请求产生的唯一orderID（备注：该参数可以是平台返回给你的orderID，也可以是你在下单时候提交的下游企业自己订单号orderNo）
     * @return
     */
    public Response getOrder(String orderID) {
        Map<String, String> map = new HashMap<>();

        map.put("action", "Query");
        map.put("orderID", orderID);
        map.put("timeStamp", timeStamp());
        map.put("account", account);
        String sign = HttpRequest.md5(apikey + HttpRequest.sortParams(map, null) + apikey);
        map.put("sign", sign);

        return send("http://api.liuliang.net.cn/Query.php", map);
    }
    /**
     * 发送请求
     * @param url
     * @param json
     * @return
     */
    private Response send(String url , Map<String, String> map) {
        // 加密
        String params = HttpRequest.sortParams(map, null);
        if(LOG.isDebugEnabled()) LOG.debug(params);

        String result = HttpRequest.sendPost(url, params);
        if(LOG.isDebugEnabled()) LOG.debug(Utils.convert(result));
        try {
            return result == null ? null : JSON.parseObject(Utils.convert(result), Response.class);
        }
        catch (Exception e) {
            LOG.error("",e);
        }
        return null;
    }

    private String timeStamp() {
        return System.currentTimeMillis() / 1000 + "";
    }

    public static class Response {

        private String respCode;
        private String respMsg;
        private String orderID;

        // 订单查询
        private String phoneNo;
        private String orderno_ID;

        public String getRespCode() {
            return respCode;
        }
        public void setRespCode(String respCode) {
            this.respCode = respCode;
        }
        public String getRespMsg() {
            return respMsg;
        }
        public void setRespMsg(String respMsg) {
            this.respMsg = respMsg;
        }
        public String getOrderID() {
            return orderID;
        }
        public void setOrderID(String orderID) {
            this.orderID = orderID;
        }
        public String getPhoneNo() {
            return phoneNo;
        }
        public void setPhoneNo(String phoneNo) {
            this.phoneNo = phoneNo;
        }
        public String getOrderno_ID() {
            return orderno_ID;
        }
        public void setOrderno_ID(String orderno_ID) {
            this.orderno_ID = orderno_ID;
        }

    }
}
