package com.wteam.mixin.recharge;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wteam.mixin.constant.Configs;
import com.wteam.mixin.recharge.DaHanRecharge.Response;

/**
 * 大汉
 * @author benko
 *
 */
public class DaHanRecharge {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(DaHanRecharge.class.getName());
    // 初始化
    static { init(); }

    private static String account = "Admintaomeng";
    private static String pwd = "test1234";
    private static String callbackURL = "http://mixinwang.com/recharge/dahan/callback";

    private static void init() {
        Properties properties = Configs.prop;
        if (properties != null) {
            account = properties.getProperty("dahan.account");
            pwd = properties.getProperty("dahan.pwd");
            //callbackURL = properties.getProperty("dahan.callbackURL");
        }
        LOG.debug("properties: {},account:{},password:{}",properties,account, pwd);
    }

    private DaHanRecharge() {}

    public static DaHanRecharge instance() {
        return new DaHanRecharge();
    }
    /**
     *  下单请求
     * @param clientOrderId
     * @param mobiles
     * @param packageSize
     * @return
     */
    public Optional<Response> recharge(String clientOrderId,String mobiles,String packageSize) {
        Request request = new Request(account);
        request.setTimestamp(timeStamp());
        request.setMobiles(mobiles);
        request.setClientOrderId(clientOrderId);
        request.setPackageSize(packageSize);
        // MD5(account+MD5(pwd)+timestamp+mobiles) 小写
        request.setSign(HttpRequest.md5(request.getAccount() + HttpRequest.md5(pwd) + request.getTimestamp() + request.getMobiles()));

        Object response = send("http://if.dahanbank.cn/FCOrderServlet", request);
        return Optional.ofNullable((Response)response);
    }

    /**
     * 订单查询
     * @param clientOrderId
     * @return
     */
    public Optional<List<Response>> getOrder(String clientOrderId) {
        Request request = new Request(account);
        request.setClientOrderId(clientOrderId);
        // MD5(account+MD5(pwd)) 小写
        request.setSign(HttpRequest.md5(request.getAccount() + HttpRequest.md5(pwd)) );

        Object response = send("http://if.dahanbank.cn/FCOrderServlet", request);
        Object _return = null;
        if (response != null && response instanceof Response) {
            _return = Arrays.asList(response);
        } else {
            _return = response;
        }
        return Optional.ofNullable((List<Response>)_return);
    }

    /**
     * 发送请求
     * @param url
     * @param json
     * @return
     */
    private Object send(String url , Request request) {
        String params = JSON.toJSONString(request);
        if(LOG.isDebugEnabled()) LOG.debug("params:{}",params);

        String result = HttpRequest.sendPostJSON(url, params);
        if(LOG.isDebugEnabled()) LOG.debug("result:{}",result);
        try {

            if (result == null || "".equals(result)) {
                return null;
            }
            else {
                Object object = JSON.parse(result);
                if (object instanceof JSONObject) {
                    return JSONObject.toJavaObject((JSON)object, Response.class);
                } else if (object instanceof JSONArray) {
                    return JSON.parseArray(result, Response.class);
                }
            }
        }
        catch (Exception e) {
            LOG.error("",e);
        }
        return null;
    }



    private String timeStamp() {
        return System.currentTimeMillis() + "";
    }
    public static class Request {

        private String timestamp;
        private String sign;
        private String mobiles;
        private String account;
        private String packageSize;
        private String clientOrderId;

        public Request(){}
        public Request(String account) {
            this.account = account;
        }
        public String getTimestamp() {
            return timestamp;
        }
        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }
        public String getSign() {
            return sign;
        }
        public void setSign(String sign) {
            this.sign = sign;
        }
        public String getMobiles() {
            return mobiles;
        }
        public void setMobiles(String mobiles) {
            this.mobiles = mobiles;
        }
        public String getAccount() {
            return account;
        }
        public void setAccount(String account) {
            this.account = account;
        }
        public String getPackageSize() {
            return packageSize;
        }
        public void setPackageSize(String packageSize) {
            this.packageSize = packageSize;
        }
        public String getClientOrderId() {
            return clientOrderId;
        }
        public void setClientOrderId(String clientOrderId) {
            this.clientOrderId = clientOrderId;
        }

    }

    public static class Response {

        private String resultCode;
        private String resultMsg;
        private String failPhones;
        private String clientOrderId;

        // 回调
        private String mobile;
        private String status;
        private String errorCode;
        private String errorDesc;
        private String discount;
        private String costMoney;
        private String sign;

        public String getResultCode() {
            return resultCode;
        }
        public void setResultCode(String resultCode) {
            this.resultCode = resultCode;
        }
        public String getResultMsg() {
            return resultMsg;
        }
        public void setResultMsg(String resultMsg) {
            this.resultMsg = resultMsg;
        }
        public String getFailPhones() {
            return failPhones;
        }
        public void setFailPhones(String failPhones) {
            this.failPhones = failPhones;
        }
        public String getClientOrderId() {
            return clientOrderId;
        }
        public void setClientOrderId(String clientOrderId) {
            this.clientOrderId = clientOrderId;
        }
        public String getMobile() {
            return mobile;
        }
        public void setMobile(String mobile) {
            this.mobile = mobile;
        }
        public String getStatus() {
            return status;
        }
        public void setStatus(String status) {
            this.status = status;
        }
        public String getErrorCode() {
            return errorCode;
        }
        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }
        public String getErrorDesc() {
            return errorDesc;
        }
        public void setErrorDesc(String errorDesc) {
            this.errorDesc = errorDesc;
        }
        public String getDiscount() {
            return discount;
        }
        public void setDiscount(String discount) {
            this.discount = discount;
        }
        public String getCostMoney() {
            return costMoney;
        }
        public void setCostMoney(String costMoney) {
            this.costMoney = costMoney;
        }
        public String getSign() {
            return sign;
        }
        public void setSign(String sign) {
            this.sign = sign;
        }
    }


}
