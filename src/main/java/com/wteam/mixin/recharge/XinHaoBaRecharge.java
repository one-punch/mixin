package com.wteam.mixin.recharge;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.wteam.mixin.utils.FileUtils;

/**
 * 新号吧 流量充值
 * @version 1.0
 * @author benko
 * @time 2016-08-12
 */
public class XinHaoBaRecharge {
   /**
    * log4j实例对象.
    */
   private static Logger LOG = LogManager.getLogger(FileUtils.class.getName());


    private static String apiKey = "a3iMBuJi0RcYrgmjqVyUp12c4D1OnN7j";
    private static String loginname = "mixinwang";
    private static String url = "http://api.xinhaoba.com/commoninterface.do";


    // 初始化
    static { init(); }

    private static void init() {
        Properties properties = FileUtils.getConfigProperties("./user-config/config.properties");
        if (properties != null) {
            apiKey = properties.getProperty("xinhaoba.apikey");
            loginname = properties.getProperty("xinhaoba.loginname");
        }
        LOG.debug("properties: {},apiKey: {},loginname: {}",properties,apiKey,loginname);
    }

    private XinHaoBaRecharge() {}


    public static XinHaoBaRecharge instance() {
        return new XinHaoBaRecharge();
    }

    /**
     *
     * 充值成功：{"Message":"","Code":"1"}<br>
     * 充值失败：{"Message":"版本错误","Code":"010"}<br>
     * @param orderId 订单号
     * @param phone 充值的手机
     * @param prodid 流量的pid
     * @param num 流量值
     * @return 返回json对象
     */
    public Response recharge(String orderId, String phone,String prodid,String num) {

        String checkParams = String.format("api_key=%s&prodid=%s&submitorderid=%s&phone=%s&num=%s",
             apiKey, prodid, orderId, phone, num);

        String param = HttpRequest.params(
            "cmd", "recharge",
            "loginname", loginname,
            "prodid", prodid,
            "submitorderid", orderId,
            "phone", phone,
            "num", num,
            "custname", phone + "_" + orderId,
            "check", HttpRequest.md5(checkParams)
        );
        return send(url, param);
    }

    /**
     *
     * @param orderId
     * @param phone
     * @param prodid
     * @param sysorderid
     * @return
     */
    public Response getOrder(String orderId, String phone,String prodid,String sysorderid){

       String checkParams = String.format("api_key=%s&prodid=%s&submitorderid=%s&sysorderid=%s&phone=%s",
            apiKey, prodid, orderId,sysorderid, phone);

       String param = HttpRequest.params(
           "cmd", "query",
           "loginname", loginname,
           "prodid", prodid,
           "submitorderid", orderId,
           "sysorderid", sysorderid,
           "phone", phone,
           "check", HttpRequest.md5(checkParams)
       );

        return send(url, param);
    }

    /**
     * 发送请求
     * @param url
     * @param json
     * @return
     */
    private Response send(String url , String params) {

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


    public static class Response {
        // 通用返回信息
        @JSONField(name = "Code")
        private String code;
        @JSONField(name = "Message")
        private String message;
        @JSONField(name = "sysorderid")
        private String sysorderid;
        public String getCode() {
            return code;
        }
        public void setCode(String code) {
            this.code = code;
        }
        public String getMessage() {
            return message;
        }
        public void setMessage(String message) {
            this.message = message;
        }
        public String getSysorderid() {
            return sysorderid;
        }
        public void setSysorderid(String sysorderid) {
            this.sysorderid = sysorderid;
        }
    }
}
