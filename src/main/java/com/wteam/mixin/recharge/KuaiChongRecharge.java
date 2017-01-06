package com.wteam.mixin.recharge;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.wteam.mixin.constant.Configs;

/**
 * 快充 流量充值
 * @version 1.0
 * @author benko
 * @time 2016-08-12
 */
public class KuaiChongRecharge {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(KuaiChongRecharge.class.getName());
    // 初始化
    static { init(); }

    private static String account = "mixin";
    private static String apikey = "6a25ceee76549578908662795e43b55e";
    private static String notifyurl = "http://mixinwang.com/recharge/kuaichong/callback";
    private static final String API_URL = "http://www.llkc8.com/api/flowService.do";
    private static void init() {
        Properties properties = Configs.prop;
        if (properties != null) {
            account = properties.getProperty("kuaichong.account");
            apikey = properties.getProperty("kuaichong.apikey");
            notifyurl = properties.getProperty("kuaichong.notifyurl");
        }
        LOG.debug("properties: {},account:{},apikey:{}",properties,account,apikey);
    }

    private KuaiChongRecharge() {}


    public static KuaiChongRecharge instance() {
        return new KuaiChongRecharge();
    }
    /**
     *  下单请求
     *
     * @param mobile 号码 (签名)
     * @param id
     * @return
     */
    public Response recharge(String mobile,String id) {

        Map<String, String> map = new HashMap<>();

        map.put("action", "createOrder.do");
        map.put("account", account);
        map.put("mobile", mobile);
        map.put("id", id);
        map.put("notifyurl", notifyurl);

        return post(API_URL, map);
    }
    /**
     *  订单查询
     * @param ORDERNUM 外部订单号
     * @return
     */
    public Response getOrder(String taskid) {

        Map<String, String> map = new HashMap<>();

        map.put("action", "queryOrder.do");
        map.put("account", account);
        map.put("taskid", taskid);

        return post(API_URL, map);
    }
    /**
     * 发送请求
     * @param url
     * @param json
     * @return
     */
    private Response post(String url , Map<String, String> map) {
        // 加密
        List<String> excludeKeys = Arrays.asList("action", "v");
        String sign = HttpRequest.md5(sortParams(map, excludeKeys)  + "&key=" + apikey);
        map.put("v", "1.0");
        map.put("sign", sign.toUpperCase());
        String params = sortParams(map, null);
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

    private String sortParams(Map<String, String> map, List<String> excludeKeys) {
        String[] param = {""} ;
        String result;
        map.keySet().stream().sorted().forEach(key -> {
            if(excludeKeys == null || !excludeKeys.contains(key) )
                param[0] += "&" + key + "=" + map.get(key);
        });
        result = param[0].replaceFirst("&", "");
        if(LOG.isDebugEnabled()) LOG.debug(result);
        return result;
    }

    public static class Response {
        private String message;

        private String status;

        @JSONField(name = "taskID")
        private String taskID;
        @JSONField(name = "taskid")
        private String taskid;


        private String code;

        // 回调参数
        private String id; // 流量包ID(签名)
        private String oper_time; //交易完成时间: (签名)
        private String result; // 结果编码： 0：充值成功 1：充值失败 (签名)
        private String resultdesc; //结果描述(签名)
        private String sign; //见 注1

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }

        public String getTaskid() {
            return taskid;
        }

        public void setTaskid(String taskid) {
            this.taskid = taskid;
        }

        public String getTaskID() {
            return taskID;
        }

        public void setTaskID(String taskID) {
            this.taskID = taskID;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOper_time() {
            return oper_time;
        }

        public void setOper_time(String oper_time) {
            this.oper_time = oper_time;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getResultdesc() {
            return resultdesc;
        }

        public void setResultdesc(String resultdesc) {
            this.resultdesc = resultdesc;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }
        /**
         * 获取taskid不为空
         * @return
         */
        public String getRealTaskId() {
            return taskid != null ? taskid : taskID;
        }

    }
}
