package com.wteam.mixin.model.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.wteam.mixin.define.IValueObject;

public class BusinessInfoVo implements IValueObject{
    
    /**
     * BusinessInfoPo as  business
     * UserPo as user
     */
    public static final String SELECT_1 = "select new com.wteam.mixin.model.vo.BusinessInfoVo( "
                                        + " business.businessId,business.memberId,business.memberStartAt,business.memberVailidity, "
                                        + " user.account, user.tel,business.wechatOfficAccount,business.proxyParentId, "
                                        + " business.isAllowBalanceRecharge)";
    
    /**商家id*/
    private Long id;
    
    /**账号ID*/
    private Long businessId;
    
    /**账户余额,单位(分)*/
    private BigDecimal balance;
    
    /**未结算资金*/
    private BigDecimal unSettlement;
    
    /**已结算资金*/
    private BigDecimal settlement;
    
    /**订单总收入*/
    private BigDecimal orderIncome;
    
    /**订单总收入*/
    private BigDecimal orderCost;
    
    /**总净利润*/
    private BigDecimal profits;
    
    /**会员Id*/
    private Long memberId;
    
    /**会员Id*/
    private String memberName;
    
    /**会员时间*/
    private Date memberStartAt;
    
    /**会员有效期*/
    private int memberVailidity;
    /*---------------------代理商家信息--------------------*/
    /**代理父商家ID*/
    private Long proxyParentId;
    /**是否允许代理商家在平台充值余额*/
    private Boolean isAllowBalanceRecharge;

    /**代理父商家账号*/
    private String proxyParentAccount;
    /**代理父商家电话*/
    private String proxyParentTel;
    /**代理父商家邮箱*/
    private String proxyParentEmail;
    /**代理父商家收入*/
    private BigDecimal proxyParentIncome;
    /*---------------------UserPo--------------------*/

    /** 账号 */
    private String account;
    /** 手机号 */
    private String tel;
    /** 邮箱 */
    private String email;
    
    /*---------------------微信--------------------*/
    
    /** 公众号id*/
    private String authorizerAppid;
    
    /** 公众号调用凭据*/
    private String authorizerAccessToken;
    
    /** 凭据有效期 单位 ms*/
    private Long expiresIn;
    
    /** 凭据产生时间*/
    private Date tokenBeginTime;
    
    /** 刷新凭据*/
    private String refreshToken;
    
    /** 授权接口*/
    private String funcInfo;
    
    /** 是否取消授权*/
    private Boolean isAuthorized;
    
    /** 授权时间*/
    private Date authorizedTime;
    
    /**jsapi调用凭据*/
    private String jsapiTicket;
    
    /**jsapi调用凭据  授权时间*/
    private Date ticketStartTime;
    
    /**jsapi调用凭据  有效期*/
    private Long ticketExpired;

    /** 是否使用商家支付*/
    private Boolean useBusinessPay;
    
    /**mch_id 平台微信支付id*/
    private String mch_id;

    /**支付key*/
    private String payKey;

    /**支付证书*/
    private Long apiclient_cert_id;
    

    
    /*---------------------微信公众号信息--------------------*/

    /** 公众号帐号*/
    private String wechatOfficAccount;
    
    /** 公众号帐号基本信息*/
    private String wechatInfo;
    
    /**是否被删除*/
    private boolean isDelete;
    
    /**创建时间*/
    private Date createTime;

    public BusinessInfoVo() {
    }
    
    /**
     * SELECT_1
     * @param businessId
     * @param account
     * @param tel
     * @param wechatInfo
     */
    public BusinessInfoVo(Long businessId,Long memberId, Date memberStartAt, int memberVailidity, String account,
                          String tel, String wechatOfficAccount, Long proxyParentId, Boolean isAllowBalanceRecharge) {
        this.businessId = businessId;
        this.memberId = memberId;
        this.memberStartAt = memberStartAt;
        this.memberVailidity = memberVailidity;
        this.account = account;
        this.tel = tel;
        this.wechatOfficAccount = wechatOfficAccount;
        this.proxyParentId = proxyParentId;
        this.isAllowBalanceRecharge = isAllowBalanceRecharge;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public BigDecimal getUnSettlement() {
        return unSettlement;
    }

    public void setUnSettlement(BigDecimal unSettlement) {
        this.unSettlement = unSettlement;
    }

    public BigDecimal getSettlement() {
        return settlement;
    }

    public void setSettlement(BigDecimal settlement) {
        this.settlement = settlement;
    }

    public BigDecimal getOrderIncome() {
        return orderIncome;
    }

    public void setOrderIncome(BigDecimal orderIncome) {
        this.orderIncome = orderIncome;
    }

    public BigDecimal getOrderCost() {
        return orderCost;
    }

    public void setOrderCost(BigDecimal orderCost) {
        this.orderCost = orderCost;
    }

    public BigDecimal getProfits() {
        return profits;
    }

    public void setProfits(BigDecimal profits) {
        this.profits = profits;
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberName() {
        return memberName;
    }

    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }

    public Date getMemberStartAt() {
        return memberStartAt;
    }

    public void setMemberStartAt(Date memberStartAt) {
        this.memberStartAt = memberStartAt;
    }

    public int getMemberVailidity() {
        return memberVailidity;
    }

    public void setMemberVailidity(int memberVailidity) {
        this.memberVailidity = memberVailidity;
    }

    public Long getProxyParentId() {
        return proxyParentId;
    }

    public void setProxyParentId(Long proxyParentId) {
        this.proxyParentId = proxyParentId;
    }

    public Boolean getIsAllowBalanceRecharge() {
        return isAllowBalanceRecharge;
    }
    public void setIsAllowBalanceRecharge(Boolean isAllowBalanceRecharge) {
        this.isAllowBalanceRecharge = isAllowBalanceRecharge;
    }
    
    public String getProxyParentAccount() {
        return proxyParentAccount;
    }

    public void setProxyParentAccount(String proxyParentAccount) {
        this.proxyParentAccount = proxyParentAccount;
    }

    public String getProxyParentTel() {
        return proxyParentTel;
    }

    public void setProxyParentTel(String proxyParentTel) {
        this.proxyParentTel = proxyParentTel;
    }

    public String getProxyParentEmail() {
        return proxyParentEmail;
    }

    public void setProxyParentEmail(String proxyParentEmail) {
        this.proxyParentEmail = proxyParentEmail;
    }

    public BigDecimal getProxyParentIncome() {
        return proxyParentIncome;
    }

    public void setProxyParentIncome(BigDecimal proxyParentIncome) {
        this.proxyParentIncome = proxyParentIncome;
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

    public String getAuthorizerAppid() {
        return authorizerAppid;
    }

    public void setAuthorizerAppid(String authorizerAppid) {
        this.authorizerAppid = authorizerAppid;
    }

    public String getAuthorizerAccessToken() {
        return authorizerAccessToken;
    }

    public void setAuthorizerAccessToken(String authorizerAccessToken) {
        this.authorizerAccessToken = authorizerAccessToken;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Date getTokenBeginTime() {
        return tokenBeginTime;
    }

    public void setTokenBeginTime(Date tokenBeginTime) {
        this.tokenBeginTime = tokenBeginTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getFuncInfo() {
        return funcInfo;
    }

    public void setFuncInfo(String funcInfo) {
        this.funcInfo = funcInfo;
    }

    public Boolean  getIsAuthorized() {
        return isAuthorized;
    }

    public void setIsAuthorized(Boolean isAuthorized) {
        this.isAuthorized = isAuthorized;
    }

    public Date getAuthorizedTime() {
        return authorizedTime;
    }

    public void setAuthorizedTime(Date authorizedTime) {
        this.authorizedTime = authorizedTime;
    }

    public String getJsapiTicket() {
        return jsapiTicket;
    }

    public void setJsapiTicket(String jsapiTicket) {
        this.jsapiTicket = jsapiTicket;
    }

    public Date getTicketStartTime() {
        return ticketStartTime;
    }

    public void setTicketStartTime(Date ticketStartTime) {
        this.ticketStartTime = ticketStartTime;
    }

    public Long getTicketExpired() {
        return ticketExpired;
    }

    public void setTicketExpired(Long ticketExpired) {
        this.ticketExpired = ticketExpired;
    }
    public Boolean getUseBusinessPay() {
        return useBusinessPay;
    }
    public void setUseBusinessPay(Boolean useBusinessPay) {
        this.useBusinessPay = useBusinessPay;
    }
    public String getMch_id() {
        return mch_id;
    }
    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }
    public String getPayKey() {
        return payKey;
    }
    public void setPayKey(String payKey) {
        this.payKey = payKey;
    }
    public Long getApiclient_cert_id() {
        return apiclient_cert_id;
    }
    public void setApiclient_cert_id(Long apiclient_cert_id) {
        this.apiclient_cert_id = apiclient_cert_id;
    }
    public String getWechatOfficAccount() {
        return wechatOfficAccount;
    }
    public void setWechatOfficAccount(String wechatOfficAccount) {
        this.wechatOfficAccount = wechatOfficAccount;
    }
    public String getWechatInfo() {
        return wechatInfo;
    }
    public void setWechatInfo(String wechatInfo) {
        this.wechatInfo = wechatInfo;
    }

    public boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
    
}
