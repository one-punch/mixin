package com.wteam.mixin.model.vo;

import com.wteam.mixin.define.IValueObject;
import com.wteam.mixin.utils.MD5Utils;

public class TrafficApiRequestParams implements IValueObject {
    
    String tel;
    String appid;
    String key;
    String type;
    String outTradeNum;
    String productNum;
    String timestamp;
    String sign;
    String ip;
    
    
    public TrafficApiRequestParams() {
    }
    public TrafficApiRequestParams(String tel, String appid, String type, String productNum,
                                   String timestamp, String sign) {
        this.tel = tel;
        this.appid = appid;
        this.type = type;
        this.productNum = productNum;
        this.timestamp = timestamp;
        this.sign = sign;
    }
    
    public TrafficApiRequestParams(String tel, String appid, String type, String outTradeNum,
                                   String productNum, String timestamp, String sign, String ip) {
        this.tel = tel;
        this.appid = appid;
        this.type = type;
        this.outTradeNum = outTradeNum;
        this.productNum = productNum;
        this.timestamp = timestamp;
        this.sign = sign;
        this.ip = ip;
    }
    
    public TrafficApiRequestParams(String appid, String outTradeNum, String timestamp,
                                   String sign) {
        this.appid = appid;
        this.outTradeNum = outTradeNum;
        this.timestamp = timestamp;
        this.sign = sign;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getAppid() {
        return appid;
    }
    public void setAppid(String appid) {
        this.appid = appid;
    }
    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getProductNum() {
        return productNum;
    }
    public String getOutTradeNum() {
        return outTradeNum;
    }
    public void setOutTradeNum(String outTradeNum) {
        this.outTradeNum = outTradeNum;
    }
    public void setProductNum(String productNum) {
        this.productNum = productNum;
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
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String createSign() {
        return MD5Utils.md5(appid + key + tel + type + outTradeNum + productNum + timestamp);
    }
    @Override
    public String toString() {
        return "TrafficApiRequestParams [tel=" + tel + ", appid=" + appid + ", key=" + key
               + ", type=" + type + ", outTradeNum=" + outTradeNum + ", productNum=" + productNum
               + ", timestamp=" + timestamp + ", sign=" + sign + ", ip=" + ip + "]";
    }
    
    
}
