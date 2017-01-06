package com.wteam.mixin.recharge;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.constant.Configs;

/**
 * 得力 流量充值
 * @version 1.0
 * @author benko
 * @time 2016-08-12
 */
public class DeLiRecharge {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(DeLiRecharge.class.getName());
    // 初始化
    static { init(); }

    private static String account = "mengtao";
    private static String password = "123456";
    private static String key = "mt168";
    private static String callbackURL = "http://mixinwang.com/recharge/deli/callback";

    private static void init() {
        Properties properties = Configs.prop;
        if (properties != null) {
            account = properties.getProperty("deli.account");
            password = properties.getProperty("deli.password");
            key = properties.getProperty("deli.key");
            callbackURL = properties.getProperty("deli.callbackURL");
        }
        LOG.debug("properties: {},account:{},password:{}",properties,account,password);
    }

    private DeLiRecharge() {}

    public static DeLiRecharge instance() {
        return new DeLiRecharge();
    }

    /**
     *  下单请求
     *
     * @param mobile 所充流量的手机号码
     * @param flowNum 所充流量的产品代码
     * @param channelMarker 参数：dellidc 提供;电信:JTDTY,联通:DLTY,移动:DLMTY
     * @param range 0 全国流量 1省内流量，不带改参数时默认为1
     * @return
     */
    public Response recharge(String mobile,String flowNum, String channelMarker, String range) {
        Map<String, String> map = new HashMap<>();

        map.put("mobile", mobile);
        map.put("flowNum", flowNum);
        map.put("channelMarker", channelMarker);
        map.put("range", range);
        map.put("callbackURL", callbackURL);

        return send("http://sdk.dellidc.com/flow.json", map);
    }
    /**
     *  订单查询
     * @param ORDERNUM 外部订单号
     * @return
     */
    public Response getOrder(String sessionId) {
        Map<String, String> map = new HashMap<>();

        map.put("sessionId", sessionId);

        return send("http://sdk.dellidc.com/queryOrder.json", map);
    }
    /**
     * 发送请求
     * @param url
     * @param json
     * @return
     */
    private Response send(String url , Map<String, String> map) {
        // 加密
        String sign = md5(account + md5(password) + key);
        map.put("account", account);
        map.put("password", password);
        map.put("sign", sign);
        String params = HttpRequest.sortParams(map, null);
        if(LOG.isDebugEnabled()) LOG.debug(params);

        String result = HttpRequest.sendGet(url, params);

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

    public static class Response {

        private String code;
        private String msg;
        private String sessionId;
        private boolean state;

        // 订单回调信息
        private String userPhone;
        private String oderStat;
        private String orderDate;
        private String resultDesc;

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public String getMsg() {
            return msg;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public void setState(boolean state) {
            this.state = state;
        }

        public boolean getState() {
            return state;
        }

        public String getUserPhone() {
            return userPhone;
        }

        public void setUserPhone(String userPhone) {
            this.userPhone = userPhone;
        }

        public String getOderStat() {
            return oderStat;
        }

        public void setOderStat(String oderStat) {
            this.oderStat = oderStat;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }

        public String getResultDesc() {
            return resultDesc;
        }

        public void setResultDesc(String resultDesc) {
            this.resultDesc = resultDesc;
        }


    }

    public static void main(String[] args) {
        System.out.println(HttpRequest.md5(password));
        System.out.println(account + "+" + HttpRequest.md5(password) + "+" + key);
        String sign = HttpRequest.md5(account + HttpRequest.md5(password) + key);
    }
}
