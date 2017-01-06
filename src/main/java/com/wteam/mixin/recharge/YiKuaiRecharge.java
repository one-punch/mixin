package com.wteam.mixin.recharge;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.wteam.mixin.constant.Configs;
/**
 * 宜快流量充值
 * @author benko
 *
 */
public class YiKuaiRecharge {

    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(YiKuaiRecharge.class.getName());

    private static final Map<String, YiKuaiRecharge> CONFIGS = new HashMap<>();

    public static final String yikuai = "yikuai";

    private String account = "nadou";
    private String ApiKey = "1c6d74ca09b04da9bc17a5baa220754a";
    private String hostname = "http://120.76.188.201:8080/api.aspx";

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("YYYY-MM-dd");

    // 初始化
    static { init(); }

    // 初始化
    static { init(); }

    private static void init() {
        Stream.of(yikuai)
            .forEach(YiKuaiRecharge::config);
    }

    private static void config(String name) {
        final Properties properties = Configs.prop;
        if (properties != null) {
            String account = properties.getProperty(name + ".account");
            String ApiKey = properties.getProperty(name + ".ApiKey");
            String hostname = properties.getProperty(name + ".hostname");
            CONFIGS.put(name, new YiKuaiRecharge(account, ApiKey, hostname));
        }
        else {
            CONFIGS.put(name, new YiKuaiRecharge());
        }

        LOG.debug("properties: {},",properties);
    }

    private YiKuaiRecharge(String account, String apiKey, String hostname) {
        this.account = account;
        this.ApiKey = apiKey;
        this.hostname = hostname;
    }

    private YiKuaiRecharge() {
    }

    public static YiKuaiRecharge instance() {
        return CONFIGS.get(yikuai);
    }
    public static YiKuaiRecharge instance(String name) {
        return CONFIGS.get(name);
    }

    /**
     *
     * @param mobile 号码
     * @param _package 套餐 (签名)
     * @param outTradeNo 商户订单号
     * @param range 0 全国流量 1省内流量，不带改参数时默认为0
     * @return
     */
    public Response recharge(String mobile,String _package, String outTradeNo, String range){
        Map<String, String> basemap = new HashMap<>();
        basemap.put("action", "charge");
        basemap.put("v", "1.1");
        basemap.put("range", range);
        basemap.put("outTradeNo", outTradeNo);

        Map<String, String> signmap = new HashMap<>();
        signmap.put("mobile", mobile);
        signmap.put("package", _package);
        return send(basemap, signmap);
    }
    /**
     *
     * @param outTradeNo
     * @return
     */
    public Response getOrder(String outTradeNo){
        Map<String, String> basemap = new HashMap<>();
        basemap.put("action", "queryReport");
        basemap.put("v", "1.1");
        basemap.put("sendTime", FORMAT.format(new Date()));
        basemap.put("outTradeNo", outTradeNo);

        Map<String, String> signmap = new HashMap<>();
        return send(basemap, signmap);
    }

    /**
     * 发送请求
     * @param url
     * @param json
     * @return
     */
    private Response send(Map<String, String> basemap, Map<String, String> signmap) {
        // 加密
        signmap.put("account", account);
        String sign = HttpRequest.md5(HttpRequest.sortParams(signmap, null) + "&key=" + ApiKey);

        basemap.put("sign", sign);
        basemap.putAll(signmap);
        String params = HttpRequest.sortParams(basemap, null);
        if(LOG.isDebugEnabled()) LOG.debug(params);

        String result = HttpRequest.sendPost(hostname, params);

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
        @JSONField(name = "TaskID")
        private String taskid;
        private String Code;
        private String Message;

        // 订单信息
        @JSONField(name = "Mobile")
        private String mobile;
        @JSONField(name = "Status")
        private String status;
        @JSONField(name = "ReportTime")
        private String reporttime;
        @JSONField(name = "ReportCode")
        private String reportcode;
        @JSONField(name = "OutTradeNo")
        private String outTradeNo;

        // 主动查询
        @JSONField(name = "Reports")
        private List<Response> Reports;

        public String getCode() {
            return Code;
        }
        public void setCode(String code) {
            Code = code;
        }
        public String getMessage() {
            return Message;
        }
        public void setMessage(String message) {
            Message = message;
        }
        public String getTaskid() {
            return taskid;
        }
        public void setTaskid(String taskid) {
            this.taskid = taskid;
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
        public String getReporttime() {
            return reporttime;
        }
        public void setReporttime(String reporttime) {
            this.reporttime = reporttime;
        }
        public String getReportcode() {
            return reportcode;
        }
        public void setReportcode(String reportcode) {
            this.reportcode = reportcode;
        }
        public String getOutTradeNo() {
            return outTradeNo;
        }
        public void setOutTradeNo(String outTradeNo) {
            this.outTradeNo = outTradeNo;
        }
        public List<Response> getReports() {
            return Reports;
        }
        public void setReports(List<Response> reports) {
            Reports = reports;
        }

    }
}
