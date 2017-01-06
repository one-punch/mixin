package com.wteam.mixin.model.vo;


import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.wteam.mixin.constant.ValidatePattern;
import com.wteam.mixin.define.IValueObject;


/**
 * 商家手机短信注册使用
 *
 * @author benko
 */
public class TelRegisterVo implements IValueObject {

    /** 账号 */
    @NotNull
    private String account;

    /** 凭证，即只有主体知道的安全值，如密码/数字证书等 */
    @NotNull
    private String credential;

    /** 短信验证码 */
    @NotNull
    private String notecode;

    /** 手机号 */
    @NotNull
    @Pattern(regexp = ValidatePattern.TEL)
    private String tel;

    public TelRegisterVo() {}

    public TelRegisterVo(String account, String credential, String notecode, String tel) {
        this.account = account;
        this.credential = credential;
        this.notecode = notecode;
        this.tel = tel;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getNotecode() {
        return notecode;
    }

    public void setNotecode(String notecode) {
        this.notecode = notecode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

}
