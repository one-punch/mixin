package com.wteam.mixin.model.vo;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.wteam.mixin.constant.ValidatePattern;
import com.wteam.mixin.define.IValueObject;


public class UserVo implements IValueObject {

    /**
     * 身份，从前台传来时，即主体的标识属性，可以是任何东西，如用户名、邮箱等， 保存用户是一个uuid
     */
    @NotNull
    private String principal;

    /** 凭证，即只有主体知道的安全值，如密码/数字证书等 */
    @NotNull
    private String credential;

    /** 用户ID */
    private Long userId;

    /** 账号 */
    private String account;

    /** 密码 */
    private String password;
    /** 密码的盐 */
    private String passSalt;
    /**  */
    private String refreshtoken;
    /**  */
    private String tokenSalt;
    /**  */
    private String tokenCreateTime;
    /**  */
    private int tokenValidity;
    /**  */
    @Pattern(regexp = ValidatePattern.TEL)
    private String tel;
    
    /** 邮箱 */
    private String email;

    /**
     * 创建用户的盐: 身份 + 随机数
     * 
     * @return String
     */
    public String createCredentialsSalt() {
        if (passSalt == null) throw new NullPointerException("passSalt 为空");
        if (principal == null) throw new NullPointerException("principal 为空");
        return principal + passSalt;
    }

    public UserVo() {}

    
    
    public UserVo(String principal, String credential) {
        this.principal = principal;
        this.credential = credential;
    }

    public UserVo( String account, String password, String tel, String email) {
        this.account = account;
        this.password = password;
        this.tel = tel;
        this.email = email;
    }

    public UserVo(String principal, String credential, String account,
                  String password, String passSalt, String refreshtoken, String tokenSalt,
                  String tokenCreateTime, int tokenValidity, String tel, String email) {
        this.principal = principal;
        this.credential = credential;
        this.account = account;
        this.password = password;
        this.passSalt = passSalt;
        this.refreshtoken = refreshtoken;
        this.tokenSalt = tokenSalt;
        this.tokenCreateTime = tokenCreateTime;
        this.tokenValidity = tokenValidity;
        this.tel = tel;
        this.email = email;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassSalt() {
        return passSalt;
    }

    public void setPassSalt(String passSalt) {
        this.passSalt = passSalt;
    }

    public String getRefreshtoken() {
        return refreshtoken;
    }

    public void setRefreshtoken(String refreshtoken) {
        this.refreshtoken = refreshtoken;
    }

    public String getTokenSalt() {
        return tokenSalt;
    }

    public void setTokenSalt(String tokenSalt) {
        this.tokenSalt = tokenSalt;
    }

    public String getTokenCreateTime() {
        return tokenCreateTime;
    }

    public void setTokenCreateTime(String tokenCreateTime) {
        this.tokenCreateTime = tokenCreateTime;
    }

    public int getTokenValidity() {
        return tokenValidity;
    }

    public void setTokenValidity(int tokenValidity) {
        this.tokenValidity = tokenValidity;
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

    @Override
    public String toString() {
        return "UserVo [principal=" + principal + ", credential=" + credential + ", userId="
               + userId + ", account=" + account + ", password=" + password + ", passSalt="
               + passSalt + ", refreshtoken=" + refreshtoken + ", tokenSalt=" + tokenSalt
               + ", tokenCreateTime=" + tokenCreateTime + ", tokenValidity=" + tokenValidity
               + ", tel=" + tel + ", email=" + email + "]";
    }

}
