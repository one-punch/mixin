package com.wteam.mixin.model.vo;

import java.util.Arrays;
import java.util.Date;

import com.wteam.mixin.define.IValueObject;

public class CustomerInfoVo implements IValueObject{
    
    /**顾客id*/
    private Long id;
    /**顾客openId*/
    private String openid;
    /**顾客昵称*/
    private String nickname;
    /**顾客性别*/
    private Integer sex;
    /**顾客省份*/
    private String province;
    /**顾客城市*/
    private String city;
    /**顾客国家*/
    private String country;
    /**顾客语言*/
    private String language;
    /**顾客头像url*/
    private String headimgurl;
    /**顾客授权*/
    private String[] privilige;
    /**顾客  系统Id*/
    /**账号ID*/
    private Long customerId;
    
    /**顾客积分*/
    private Integer integral;
    
    /**是否被删除*/
    private boolean isDelete;
    
    /**创建时间*/
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String[] getPrivilige() {
        return privilige;
    }

    public void setPrivilige(String[] privilige) {
        this.privilige = privilige;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "CustomerInfoVo [id=" + id + ", openid=" + openid + ", nickname=" + nickname
               + ", sex=" + sex + ", province=" + province + ", city=" + city + ", country="
               + country + ", language=" + language + ", headimgurl=" + headimgurl + ", privilige="
               + Arrays.toString(privilige) + ", customerId=" + customerId + ", integral="
               + integral + ", isDelete=" + isDelete + ", createTime=" + createTime + "]";
    }
    
    
}
