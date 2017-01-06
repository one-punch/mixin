package com.wteam.mixin.model.query;

import com.wteam.mixin.define.IValueObject;

public class BusinessInfoQuery extends AbstractQuery implements IValueObject {

    /**账号ID*/
    private Long businessId;
    /**代理父商家ID*/
    private Long proxyParentId;
    /** 公众号帐号*/
    private String wechatOfficAccount;
    /** 是否删除 */
    private Boolean isDelete;
    

    @Override
    public String hqlQuery(String as) {
        String name = as != null && !"".equals(as) ? as+"." : "";
        String query = "", relation = "";

        if (businessId != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sbusinessId=%d ", relation, name,businessId);
        }
        if (proxyParentId != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sproxyParentId=%d ", relation, name,proxyParentId);
        }        
        if (wechatOfficAccount != null && !"".equals(wechatOfficAccount)) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %swechatOfficAccount='%s' ", relation, name, wechatOfficAccount);
        }
        if (isDelete != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sisDelete=%b ", relation, name,isDelete);
        }
        
        return query;
    }


    public Long getBusinessId() {
        return businessId;
    }
    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
    public Long getProxyParentId() {
        return proxyParentId;
    }
    public void setProxyParentId(Long proxyParentId) {
        this.proxyParentId = proxyParentId;
    }
    public String getWechatOfficAccount() {
        return wechatOfficAccount;
    }
    public void setWechatOfficAccount(String wechatOfficAccount) {
        this.wechatOfficAccount = wechatOfficAccount;
    }
    public Boolean getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
