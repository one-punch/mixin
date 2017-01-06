package com.wteam.mixin.recharge;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.wteam.mixin.constant.Configs;
import com.wteam.mixin.recharge.XiaoZhuRecharge.Response;


public class LiuLiangRecharge {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(LiuLiangRecharge.class.getName());

    private static String APIID = "8A8CA9787D754698B64AF38298C20BE7";
    private static String APISECRET = "AA3DD6AFF0634A48B1027AA3D2868ECB";
    private static String NotifyUrl = "http://mixinwang.com/recharge/liuliang/callback";
    
    // 初始化
    static {
        init();
    }

    private static void init() {
        Properties properties = Configs.prop;
        if (properties != null) {
            APIID = properties.getProperty("liuliang.APIID");
            APISECRET = properties.getProperty("liuliang.APISECRET");
            NotifyUrl = properties.getProperty("liuliang.NotifyUrl");
        }
        LOG.debug("properties: {},APISECRET:{},APIID:{}", properties, APISECRET, APIID);
    }

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    private LiuLiangRecharge() {}

    public static LiuLiangRecharge instance() {
        return new LiuLiangRecharge();
    }

    public static boolean check(String appid) {
        return APIID.equals(appid);
    }
    
    /**
     * 
     * @param ClientOrderNO
     * @param PhoneNumber
     * @param PackageCode
     * @return
     */
    public Response recharge(String ClientOrderNO,String PhoneNumber,String PackageCode) {
        Request request = new Request();
        
        request.setClientorderno(formatOrderNo(ClientOrderNO));
        request.setPhonenumber(PhoneNumber);
        request.setPackagecode(PackageCode);
        request.setNotifyUrl(NotifyUrl);
        
        return post("http://120.76.98.32:8000/Api/Api/SingleRecharge", request);
    }

    
    /**
     *  订单查询
     * @param ClientOrderNO 
     * @return
     */
    public Response getOrder(String ClientOrderNO) {
        Request request = new Request();
        List<Items> items = new ArrayList<>();
        Items item = new Items();
        item.setClientorderno(formatOrderNo(ClientOrderNO));
        
        items.add(item);
        request.setItems(items);

        return post("http://120.76.98.32:8000/Api/Api/GetStatus", request);
    }
    
    private String formatOrderNo(String orderNo) {
        int length = 32 - orderNo.length();
        
        String px = "";
        for (int i = 0; i < length; i++ ) {
            px += "a";
        }
        return px + orderNo;
    }
    /**
     * 发送请求
     * 
     * @param url
     * @param json
     * @return
     */
    private Response post(String url, Request request) {
        request.setApiid(APIID);
        request.setTimestamp(FORMAT.format(new Date()));
        request.setSign(request.createSign());
        // 加密
        if (LOG.isDebugEnabled()) LOG.debug(request);

        String result = com.wteam.mixin.utils.HttpRequest.sendPostJSON(url,
            JSON.toJSONString(request));

        if (LOG.isDebugEnabled()) LOG.debug(result);
        try {
            return result == null ? null : JSON.parseObject(result, Response.class);
        }
        catch (Exception e) {
            LOG.error("", e);
        }
        return null;
    }

    public static class Request {

        @JSONField(name = "APIID")
        private String apiid;

        @JSONField(name = "TimeStamp")
        private String timestamp;

        @JSONField(name = "Sign")
        private String sign;

        @JSONField(name = "PhoneNumber")
        private String phonenumber;

        @JSONField(name = "ClientOrderNO")
        private String clientorderno;

        @JSONField(name = "PackageCode")
        private String packagecode;
        
        @JSONField(name = "NotifyUrl")
        private String notifyUrl;
        
        @JSONField(name = "Items ")
        private List<Items > items ;

        public void setApiid(String apiid) {
            this.apiid = apiid;
        }

        public String getApiid() {
            return apiid;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        public String getSign() {
            return sign;
        }

        public void setPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setClientorderno(String clientorderno) {
            this.clientorderno = clientorderno;
        }

        public String getClientorderno() {
            return clientorderno;
        }

        public void setPackagecode(String packagecode) {
            this.packagecode = packagecode;
        }

        public String getPackagecode() {
            return packagecode;
        }

        public String getNotifyUrl() {
            return notifyUrl;
        }

        public void setNotifyUrl(String notifyUrl) {
            this.notifyUrl = notifyUrl;
        }

        public List<Items> getItems() {
            return items;
        }

        public void setItems(List<Items> items) {
            this.items = items;
        }

        public String createSign() {
            return HttpRequest.md5(
                String.format("APIID=%sAPISECRET=%sTIMESTAMP=%s", apiid, APISECRET, timestamp));
        }

        @Override
        public String toString() {
            return "Request [apiid=" + apiid + ", timestamp=" + timestamp + ", sign=" + sign
                   + ", phonenumber=" + phonenumber + ", clientorderno=" + clientorderno
                   + ", packagecode=" + packagecode + "]";
        }

    }
    
    public static class Items  {

        @JSONField(name = "ClientOrderNO")
        private String clientorderno;


         public void setClientorderno(String clientorderno) {
             this.clientorderno = clientorderno;
         }
         public String getClientorderno() {
             return clientorderno;
         }
         
     }

    public static class Response {
        @JSONField(name = "IsSuccess")
        private boolean issuccess;

        @JSONField(name = "Message")
        private String message;

        @JSONField(name = "Code")
        private String code;

        @JSONField(name = "ErrorMessageList")
        private List<String> errormessagelist;

        @JSONField(name = "Data")
        private Data data;

        public void setIssuccess(boolean issuccess) {
            this.issuccess = issuccess;
        }

        public boolean getIssuccess() {
            return issuccess;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setErrormessagelist(List<String> errormessagelist) {
            this.errormessagelist = errormessagelist;
        }

        public List<String> getErrormessagelist() {
            return errormessagelist;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public Data getData() {
            return data;
        }
    }

    public static class Area {

        @JSONField(name = "Name")
        private String name;

        @JSONField(name = "Value")
        private String value;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    public static class Operator {

        @JSONField(name = "Name")
        private String name;

        @JSONField(name = "Value")
        private String value;

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    public static class Data {

        @JSONField(name = "ResponseItems")
        private List<Responseitems> responseitems;

        public void setResponseitems(List<Responseitems> responseitems) {
            this.responseitems = responseitems;
        }

        public List<Responseitems> getResponseitems() {
            return responseitems;
        }

    }

    public static class Responseitems {

        @JSONField(name = "APIID")
        private String appId;
        
        @JSONField(name = "ClientOrderNO")
        private String clientorderno;

        @JSONField(name = "OrderNO")
        private String orderno;

        @JSONField(name = "Area")
        private Area area;

        @JSONField(name = "Operator")
        private Operator operator;

        @JSONField(name = "PhoneNumber")
        private String phonenumber;

        @JSONField(name = "Code")
        private String code;

        @JSONField(name = "Message ")
        private String message;

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public void setClientorderno(String clientorderno) {
            this.clientorderno = clientorderno;
        }

        public String getClientorderno() {
            return clientorderno;
        }

        public void setOrderno(String orderno) {
            this.orderno = orderno;
        }

        public String getOrderno() {
            return orderno;
        }

        public void setArea(Area area) {
            this.area = area;
        }

        public Area getArea() {
            return area;
        }

        public void setOperator(Operator operator) {
            this.operator = operator;
        }

        public Operator getOperator() {
            return operator;
        }

        public void setPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getCode() {
            return code;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

    }
}
