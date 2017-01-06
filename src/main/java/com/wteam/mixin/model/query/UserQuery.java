package com.wteam.mixin.model.query;

import com.wteam.mixin.define.IValueObject;

public class UserQuery extends AbstractQuery implements IValueObject {

    

    /** 账号 */
    private String account;
    /** 电话 */
    private String tel;
    /** 邮箱 */
    private String email;
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
        if (account != null&& !"".equals(account)) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %saccount='%s' ", relation, name, account);
        }
        if (tel != null&& !"".equals(tel)) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %stel='%s' ", relation, name, tel);
        }
        if (email != null&& !"".equals(email)) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %semail='%s' ", relation, name, email);
        }
        
        return query;
    }

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getTel() {
        return tel;
    }
    public void setTel(String tel) {
        this.tel = tel;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public Boolean getIsDelete() {
        return isDelete;
    }
    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }
}
