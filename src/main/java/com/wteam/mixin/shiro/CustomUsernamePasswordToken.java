package com.wteam.mixin.shiro;


import org.apache.shiro.authc.UsernamePasswordToken;


/**
 * 登录认证，需要确认登录使用的类型。
 *
 * @author benko
 * @version 2016年6月13日
 * @see CustomUsernamePasswordToken
 * @since
 */
public class CustomUsernamePasswordToken extends UsernamePasswordToken {

    /**
     * 电话号码
     */
    public static final String LOGIN_WITN_TELEPHONENUM = "TELEPHONE_NUM";
    /**
     * 用户名
     */
    public static final String LOGIN_WITN_USERINFO = "USER_INGO";
    /**
     * 微信自动登录
     */
    public static final String LOGIN_WECHAT_AUTO = "WECHAT_AUTO";
    /**
     * 自动登录
     */
    public static final String LOGIN_AUTO = "AUTO";

    /**
     * 序列化ID
     */
    private static final long serialVersionUID = 1L;

    /**
     * 客户端登陆类型
     */
    private String loginType;

    /**
     * @param principal  身份，从前台传来时，即主体的标识属性，可以是任何东西，如用户名、邮箱等，
     * @param credential 凭证，即只有主体知道的安全值，如密码/数字证书等
     * @param loginType 登陆类型
     */
    public CustomUsernamePasswordToken(String principal, String credential, String loginType) {
        super(principal, credential);
        this.loginType = loginType;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }
}
