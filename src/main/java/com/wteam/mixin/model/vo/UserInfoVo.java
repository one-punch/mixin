package com.wteam.mixin.model.vo;


import java.util.Date;
import java.util.List;


/**
 * 用户信息vo
 *
 * @version 1.0
 * @author benko
 * @time
 */
public class UserInfoVo {

    private Long userId;

    private String tel;

    private boolean isDelete;

    private boolean isInitPassword;

    private String account;

    private String email;

    private Date createTime;

    private List<String> roleNames;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public boolean isInitPassword() {
        return isInitPassword;
    }

    public void setInitPassword(boolean isInitPassword) {
        this.isInitPassword = isInitPassword;
    }

    public boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }


    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public List<String> getRoleNames() {
        return roleNames;
    }

    public void setRoleNames(List<String> roleNames) {
        this.roleNames = roleNames;
    }

}
