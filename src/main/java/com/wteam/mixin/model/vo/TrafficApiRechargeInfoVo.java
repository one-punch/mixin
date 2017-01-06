package com.wteam.mixin.model.vo;

import org.hibernate.validator.constraints.URL;

import com.wteam.mixin.define.IValueObject;

public class TrafficApiRechargeInfoVo implements IValueObject{
    
    /**账号ID*/
    private Long businessId;
    
    /** 是否授权*/
    private boolean isApiRechargeAuthorized;

    /** appid*/
    private String apiRechargeAppId;
    
    /** key*/
    private String apiRechargeKey;

    /** IP白名单 以;号隔开*/
    private String apiRechargeIp;

    /** 回调*/
    @URL
    private String apiRechargeCallbackUrl;

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public boolean getIsApiRechargeAuthorized() {
        return isApiRechargeAuthorized;
    }

    public void setIsApiRechargeAuthorized(boolean isApiRechargeAuthorized) {
        this.isApiRechargeAuthorized = isApiRechargeAuthorized;
    }

    public String getApiRechargeAppId() {
        return apiRechargeAppId;
    }

    public void setApiRechargeAppId(String apiRechargeAppId) {
        this.apiRechargeAppId = apiRechargeAppId;
    }

    public String getApiRechargeKey() {
        return apiRechargeKey;
    }

    public void setApiRechargeKey(String apiRechargeKey) {
        this.apiRechargeKey = apiRechargeKey;
    }

    public String getApiRechargeIp() {
        return apiRechargeIp;
    }

    public void setApiRechargeIp(String apiRechargeIp) {
        this.apiRechargeIp = apiRechargeIp;
    }

    public String getApiRechargeCallbackUrl() {
        return apiRechargeCallbackUrl;
    }

    public void setApiRechargeCallbackUrl(String apiRechargeCallbackUrl) {
        this.apiRechargeCallbackUrl = apiRechargeCallbackUrl;
    }
    
    
}
