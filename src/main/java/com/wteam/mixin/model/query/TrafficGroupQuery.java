package com.wteam.mixin.model.query;

import com.wteam.mixin.define.IValueObject;

public class TrafficGroupQuery extends AbstractQuery implements IValueObject {

    /**运营商*/
    private Integer provider;
    /**省份:全国+34省*/
    private String province;
    /** 是否显示*/
    private Boolean display;
    /** 是否删除 */
    private Boolean isDelete;
    

    @Override
    public String hqlQuery(String as) {
        String name = as != null && !"".equals(as) ? as+"." : "";
        String query = "", relation = "";

        if (provider != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sprovider=%d ", relation, name,provider);
        }
        if (province != null && !"".equals(province)) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sprovince='%s' ", relation, name, province);
        }
        if (isDelete != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sisDelete=%b ", relation, name,isDelete);
        }
        if (display != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sdisplay=%b ", relation, name,display);
        }
        
        return query;
    }

    public Integer getProvider() {
        return provider;
    }

    public void setProvider(Integer provider) {
        this.provider = provider;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public Boolean getDisplay() {
        return display;
    }

    public void setDisplay(Boolean display) {
        this.display = display;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
