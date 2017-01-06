package com.wteam.mixin.model.query;

import com.wteam.mixin.define.IValueObject;

public class TrafficPlanQuery extends AbstractQuery implements IValueObject {

    /**套餐值：单位:MB*/
    private String value;
    /**流量套餐所属分组*/
    private Long trafficGroupId;
    /**是否上架*/
    private Boolean display;
    /**运营商*/
    private Integer provider;
    /**接口提供商*/
    private String apiProvider;
    /**不是接口提供商*/
    private String notApiProvider;
    /**是否是接口充值*/
    private Boolean isApiRecharge;
    /**是否处于维护中*/
    private Boolean isMaintain;
    /** 是否删除 */
    private Boolean isDelete;
    

    @Override
    public String hqlQuery(String as) {
        String name = as != null && !"".equals(as) ? as+"." : "";
        String query = "", relation = "";

        if (value != null && !"".equals(value)) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %svalue='%s' ", relation, name, value);
        }
        if (trafficGroupId != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %strafficGroupId=%d ", relation, name,trafficGroupId);
        }
        if (display != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sdisplay=%b ", relation, name,display);
        }
        if (provider != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sprovider=%d ", relation, name,provider);
        }
        if (apiProvider != null && !"".equals(apiProvider)) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sapiProvider='%s' ", relation, name, apiProvider);
        }
        if (notApiProvider != null && !"".equals(notApiProvider)) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sapiProvider!='%s' ", relation, name, notApiProvider);
        }
        if (isApiRecharge != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sisApiRecharge=%b ", relation, name,isApiRecharge);
        }
        if (isMaintain != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sisMaintain=%b ", relation, name,isMaintain);
        }
        if (isDelete != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sisDelete=%b ", relation, name,isDelete);
        }
        
        return query;
    }

    

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getTrafficGroupId() {
        return trafficGroupId;
    }

    public void setTrafficGroupId(Long trafficGroupId) {
        this.trafficGroupId = trafficGroupId;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Integer getProvider() {
        return provider;
    }


    public void setProvider(Integer provider) {
        this.provider = provider;
    }


    public String getApiProvider() {
        return apiProvider;
    }


    public void setApiProvider(String apiProvider) {
        this.apiProvider = apiProvider;
    }

    public String getNotApiProvider() {
        return notApiProvider;
    }

    public void setNotApiProvider(String notApiProvider) {
        this.notApiProvider = notApiProvider;
    }

    public Boolean getIsMaintain() {
        return isMaintain;
    }

    public void setIsMaintain(Boolean isMaintain) {
        this.isMaintain = isMaintain;
    }
    public Boolean getIsApiRecharge() {
        return isApiRecharge;
    }

    public void setIsApiRecharge(Boolean isApiRecharge) {
        this.isApiRecharge = isApiRecharge;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
