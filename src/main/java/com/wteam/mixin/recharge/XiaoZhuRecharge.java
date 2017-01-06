package com.wteam.mixin.recharge;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.wteam.mixin.constant.Configs;

/**
 * 小猪 流量充值
 * @version 1.0
 * @author benko
 * @time 2016-08-12
 */
public class XiaoZhuRecharge {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(XiaoZhuRecharge.class.getName());

    private static String USERNAME = "97408847212114c9accaf5a944ee1afe";
    private static String PASSWORD = "91f1526e4cd7a3c2ea2fe09da8c6b791";
    private static String KEY = "9f4f48ce3991c5a614c842617f960a36";
    private static String CTMRETURL = "http://mixinwang.com/recharge/xiaozhu/callback";

    // 初始化
    static { init(); }

    private static void init() {
        Properties properties = Configs.prop;
        if (properties != null) {
            USERNAME = properties.getProperty("xiaozhu.USERNAME");
            PASSWORD = properties.getProperty("xiaozhu.PASSWORD");
            KEY = properties.getProperty("xiaozhu.KEY");
            CTMRETURL = properties.getProperty("xiaozhu.CTMRETURL");
        }
        LOG.debug("properties: {},PASSWORD:{},USERNAME:{},KEY:{}",properties,PASSWORD,USERNAME,KEY);
    }

    private XiaoZhuRecharge() {}


    public static XiaoZhuRecharge instance() {
        return new XiaoZhuRecharge();
    }

    /**
     *  下单请求
     * @param CUSTOMORDERNUM 客户订单号（确保订单唯一性）
     * @param MOBILE 充值的 手机号码
     * @param PRODUCTCODE 充值的业务代码
     * @return
     */
    public Response recharge(String CUSTOMORDERNUM,String MOBILE,String PRODUCTCODE) {

        Map<String, String> map = new HashMap<>();

        map.put("USERNAME", USERNAME);
        map.put("PASSWORD", PASSWORD);
        map.put("MOBILE", MOBILE);
        map.put("PRODUCTCODE", PRODUCTCODE);
        map.put("CUSTOMORDERNUM", CUSTOMORDERNUM);
        map.put("CTMRETURL", CTMRETURL);

        return post("http://www.xz520.cn/interface/submitOrder.json", map);
    }

    /**
     *  订单查询
     * @param ORDERNUM 外部订单号
     * @return
     */
    public Response getOrder(String ORDERNUM) {

        Map<String, String> map = new HashMap<>();

        map.put("USERNAME", USERNAME);
        map.put("PASSWORD", PASSWORD);
        map.put("ORDERNUM", ORDERNUM);

        return post("http://www.xz520.cn/interface/submitOrder.json", map);
    }



    /**
     * 发送请求
     * @param url
     * @param json
     * @return
     */
    private Response post(String url , Map<String, String> map) {
        // 加密
        String sign = HttpRequest.md5(sortParams(map) + KEY);
        map.put("SIGN", sign);
        String params = sortParams(map);
        if(LOG.isDebugEnabled()) LOG.debug(params);

        String result = HttpRequest.sendPost(url, params);

        if(LOG.isDebugEnabled()) LOG.debug(result);
        try {
            return result == null ? null : JSON.parseObject(result, Response.class);
        }
        catch (Exception e) {
            LOG.error("",e);
        }
        return null;
    }

    private String sortParams(Map<String, String> map) {
        String[] param = {""};
        map.keySet().stream().sorted().forEach(key -> {
            param[0] += "&" + key + "=" + map.get(key);
        });
        param[0] = param[0].replaceFirst("&", "");
        return param[0];
    }

    public static class Response {

        // 通用返回信息
        @JSONField(name = "STATUS")
        private String status;
        @JSONField(name = "MESSAGE")
        private String message;

        // 下单 \ 订单查询
        @JSONField(name = "ORDERNUM")
        private String orderNum;

        // 订单异步通知 和orderNum
        @JSONField(name = "CUSTOMORDERNUM")
        private String customerOrderNum;
        @JSONField(name = "PRICE")
        private String price;

        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        public String getOrderNum() {
            return orderNum;
        }
        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }
        public String getCustomerOrderNum() {
            return customerOrderNum;
        }
        public void setCustomerOrderNum(String customerOrderNum) {
            this.customerOrderNum = customerOrderNum;
        }
        public String getPrice() {
            return price;
        }
        public void setPrice(String price) {
            this.price = price;
        }

     }

}
