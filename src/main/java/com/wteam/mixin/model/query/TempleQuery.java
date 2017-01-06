package com.wteam.mixin.model.query;

import com.wteam.mixin.define.IValueObject;

public class TempleQuery extends AbstractQuery implements IValueObject {

    /** 是否删除 */
    private Boolean isDelete;
    

    @Override
    public String hqlQuery(String as) {
        String name = as != null && !"".equals(as) ? as+"." : "";
        String query = "", relation = "";

        if (isDelete != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sisDelete=%b ", relation, name,isDelete);
        }
        
        return query;
    }

    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
