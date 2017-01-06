package com.wteam.mixin.model.po;
// Generated 2016-4-6 23:48:57 by Hibernate Tools 4.3.1.Final


import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.wteam.mixin.define.IPersistentObject;


/**
 * 用户表
 * @author benko
 * @version 2016年6月12日
 * @see UserPo
 * @since
 */
@Entity
@Table(name = "t_user", uniqueConstraints = {
    @UniqueConstraint(columnNames = "account"),
    @UniqueConstraint(columnNames = "email"),
    @UniqueConstraint(columnNames = "tel")})
@SequenceGenerator(name="user_gen",initialValue=10000, allocationSize = 1)
public class UserPo implements java.io.Serializable, IPersistentObject {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** 用户ID */
    private Long userId;

    /** 账号 */
    private String account;

    /** 登录账号,唯一标志 */
    private String principal;
    /** 密码 */
    private String password;
    /** 密码的盐 */
    private String passSalt;
    /** 客户端token标识 */
    private String refreshtoken;
    /** token的盐 */
    private String tokenSalt;
    /** token的创建时间 */
    private String tokenCreateTime;
    /** token的有效期，按毫秒 */
    private int tokenValidity;
    /**  */
    private String tel;

    /** 邮箱 */
    private String email;

    /** 是否删除 */
    private boolean isDelete;
    /** 创建时间 */
    private Date createTime;

    /** 用户信息关联 */
    private UserInfoPo userInfo;

    /** 关联的角色集合 */
    private Set<RolePo> roles = new HashSet<RolePo>(0);

    public UserPo() {}

    public UserPo(String account) {
        this.account = account;
    }

    public UserPo(String tel, String account, String principal, String password, String passSalt,
                  String email) {
        this.tel = tel;
        this.account = account;
        this.principal = principal;
        this.password = password;
        this.passSalt = passSalt;
        this.email = email;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY, generator="user_gen")
    @Column(name = "userId", unique = true, nullable = false)
    public Long getUserId() {
        return this.userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Column(name = "tel", unique = true)
    public String getTel() {
        return this.tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    @Column(name = "account", unique = true, nullable = false)
    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    @Column(name = "principal", unique = true, nullable = false)
    public String getPrincipal() {
        return principal;
    }
    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    @Column(name = "password")
    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "passSalt")
    public String getPassSalt() {
        return this.passSalt;
    }

    public void setPassSalt(String passSalt) {
        this.passSalt = passSalt;
    }

    @Column(name = "email", unique = true)
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column(name = "refreshtoken")
    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    @Column(name = "tokenSalt")
    public String getTokenSalt() {
        return tokenSalt;
    }

    public void setTokenSalt(String tokenSalt) {
        this.tokenSalt = tokenSalt;
    }

    @Column(name = "tokenCreateTime")
    public String getTokenCreateTime() {
        return tokenCreateTime;
    }

    public void setTokenCreateTime(String tokenCreateTime) {
        this.tokenCreateTime = tokenCreateTime;
    }

    @Column(name = "tokenValidity")
    public int getTokenValidity() {
        return tokenValidity;
    }

    public void setTokenValidity(int tokenValidity) {
        this.tokenValidity = tokenValidity;
    }

    @Column(name = "isDelete")
    @Type(type = "boolean")
    public boolean getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createTime", length = 19)
    @CreationTimestamp
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /* ************************************************************************************
     *
     *                                 下面是外键关联
     *
     * ************************************************************************************ */


    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_user_t_role", joinColumns = {
        @JoinColumn(name = "t_userId", nullable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "t_roleId", nullable = false, updatable = false)})
    public Set<RolePo> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<RolePo> roles) {
        this.roles = roles;
    }

    @OneToOne(mappedBy="user")
    public UserInfoPo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoPo userInfo) {
        this.userInfo = userInfo;
    }

}
