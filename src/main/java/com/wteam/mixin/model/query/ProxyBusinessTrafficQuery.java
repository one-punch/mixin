package com.wteam.mixin.model.query;

public class ProxyBusinessTrafficQuery extends AbstractQuery {

    /**商家ID*/
    private Long businessId;
    /**代理父商家ID*/
    private Long parentId;
    
    public Long getBusinessId() {
        return businessId;
    }
    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    
    @Override
    public String hqlQuery(String as) {
        String name = as != null && !"".equals(as) ? as+"." : "";
        String query = "", relation = "";
        
        if (parentId != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sparentId=%d ", relation, name,parentId);
        }
        if (businessId != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sbusinessId=%d ", relation, name,businessId);
        }
        return query;
    }

}
