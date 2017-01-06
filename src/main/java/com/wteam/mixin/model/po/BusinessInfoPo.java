package com.wteam.mixin.model.po;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.wteam.mixin.define.IPersistentObject;

/**
 * 商家实体类
 * @author vee
 *
 */
@Entity
@Table(name="business_info")
public class BusinessInfoPo implements Serializable, IPersistentObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
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
	
	/**会员时间*/
	private Date memberStartAt;
	
	/**会员有效期*/
	private int memberVailidity;

    /*---------------------微信--------------------*/
	
	/**	公众号id*/
	private String authorizerAppid;
	
	/**	公众号调用凭据*/
	private String authorizerAccessToken;
	
	/**	凭据有效期 单位 ms*/
	private Long expiresIn;
	
	/**	凭据产生时间*/
	private Date tokenBeginTime;
	
	/**	刷新凭据*/
	private String refreshToken;
	
	/**	授权接口*/
	private String funcInfo;
	
	/**	是否取消授权*/
	private boolean isAuthorized;
	
	/**	授权时间*/
	private Date authorizedTime;
	
	/**jsapi调用凭据*/
	private String jsapiTicket;
	
	/**jsapi调用凭据  授权时间*/
	private Date ticketStartTime;
	
	/**jsapi调用凭据  有效期*/
	private Long ticketExpired;

    /** 是否使用商家支付*/
    private boolean useBusinessPay;
    
    /**mch_id 平台微信支付id*/
    private String mch_id;

    /**支付key*/
    private String payKey;

    /**支付证书*/
    private Long apiclient_cert_id;
    
    /*---------------------代理商家信息--------------------*/
    /**代理父商家ID*/
    private Long proxyParentId;
    
    /**是否允许代理商家在平台充值余额*/
    private boolean isAllowBalanceRecharge;
	
    /*---------------------微信公众号信息--------------------*/
    /** 公众号帐号*/
    private String wechatOfficAccount;
    
    /** 公众号帐号基本信息*/
    private String wechatInfo;
    
    /*---------------------接口充值--------------------*/
    /** 是否授权*/
    private boolean isApiRechargeAuthorized;

    /** appid*/
    private String apiRechargeAppId;
    
    /** key*/
    private String apiRechargeKey;

    /** IP白名单 以;号隔开*/
    private String apiRechargeIp;

    /** 回调*/
    private String apiRechargeCallbackUrl;
    
    
    /**是否被删除*/
	private boolean isDelete;
	
	/**创建时间*/
	private Date createTime;
	
	public BusinessInfoPo() {}
	public BusinessInfoPo(Long businessId, BigDecimal balance, Date memberStartAt, int memberVailidity) {
		super();
		this.businessId = businessId;
		this.balance = balance;
		this.memberStartAt = memberStartAt;
		this.memberVailidity = memberVailidity;
	}
	
	public BusinessInfoPo(Long businessId, String authorizerAppid, String authorizerAccessToken, Long expiresIn, Date tokenBeginTime,
			String refreshToken) {
		this.businessId = businessId;
		this.authorizerAppid = authorizerAppid;
		this.authorizerAccessToken = authorizerAccessToken;
		this.expiresIn = expiresIn;
		this.tokenBeginTime = tokenBeginTime;
		this.refreshToken = refreshToken;
	}
	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(nullable = false)
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	@Column(precision=23, scale=6)
	public BigDecimal getBalance() {
		return balance;
	}
	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}
	@Column(precision=23, scale=6)
	public BigDecimal getUnSettlement() {
		return unSettlement;
	}
	public void setUnSettlement(BigDecimal unSettlement) {
		this.unSettlement = unSettlement;
	}
	@Column(precision=23, scale=6)
	public BigDecimal getSettlement() {
		return settlement;
	}
	public void setSettlement(BigDecimal settlement) {
		this.settlement = settlement;
	}
	@Column(precision=23, scale=6)
	public BigDecimal getOrderIncome() {
		return orderIncome;
	}
	public void setOrderIncome(BigDecimal orderIncome) {
		this.orderIncome = orderIncome;
	}
	@Column(precision=23, scale=6)
	public BigDecimal getOrderCost() {
		return orderCost;
	}
	public void setOrderCost(BigDecimal orderCost) {
		this.orderCost = orderCost;
	}
	@Column(precision=23, scale=6)
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
	@Temporal(TemporalType.TIMESTAMP)
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
	@Temporal(TemporalType.TIMESTAMP)
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
    @Type(type="text")
	public String getFuncInfo() {
		return funcInfo;
	}
	public void setFuncInfo(String funcInfo) {
		this.funcInfo = funcInfo;
	}
	@Type(type = "boolean")
	public boolean getIsAuthorized() {
		return isAuthorized;
	}
	public void setIsAuthorized(boolean isAuthorized) {
		this.isAuthorized = isAuthorized;
	}
	@Temporal(TemporalType.TIMESTAMP)
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
	public void setTicketStartTime(Date ticketTime) {
		this.ticketStartTime = ticketTime;
	}
	public Long getTicketExpired() {
		return ticketExpired;
	}
	public void setTicketExpired(Long ticketExpired) {
		this.ticketExpired = ticketExpired;
	}
    @Type(type = "boolean")
    public boolean getUseBusinessPay() {
        return useBusinessPay && isAuthorized;
    }
    public void setUseBusinessPay(boolean useBusinessPay) {
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
    public Long getProxyParentId() {
        return proxyParentId;
    }
    public void setProxyParentId(Long proxyParentId) {
        this.proxyParentId = proxyParentId;
    }
    @Type(type = "boolean")
    public boolean getIsAllowBalanceRecharge() {
        return isAllowBalanceRecharge;
    }
    public void setIsAllowBalanceRecharge(boolean isAllowBalanceRecharge) {
        this.isAllowBalanceRecharge = isAllowBalanceRecharge;
    }
    public String getWechatOfficAccount() {
        return wechatOfficAccount;
    }
    public void setWechatOfficAccount(String wechatOfficAccount) {
        this.wechatOfficAccount = wechatOfficAccount;
    }
    @Type(type="text") 
    public String getWechatInfo() {
        return wechatInfo;
    }
    public void setWechatInfo(String wechatInfo) {
        this.wechatInfo = wechatInfo;
    }
    @Type(type = "boolean")
    public boolean getIsApiRechargeAuthorized() {
        return isApiRechargeAuthorized;
    }
    public void setIsApiRechargeAuthorized(boolean isApiRechargeAuthorized) {
        this.isApiRechargeAuthorized = isApiRechargeAuthorized;
    }
    
	public String getApiRechargeAppId() {
        return apiRechargeAppId;
    }
    public void setApiRechargeAppId(String apiRechargeAppId) {
        this.apiRechargeAppId = apiRechargeAppId;
    }
    public String getApiRechargeKey() {
        return apiRechargeKey;
    }
    public void setApiRechargeKey(String apiRechargeKey) {
        this.apiRechargeKey = apiRechargeKey;
    }
    public String getApiRechargeIp() {
        return apiRechargeIp;
    }
    public void setApiRechargeIp(String apiRechargeIp) {
        this.apiRechargeIp = apiRechargeIp;
    }
    public String getApiRechargeCallbackUrl() {
        return apiRechargeCallbackUrl;
    }
    public void setApiRechargeCallbackUrl(String apiRechargeCallbackUrl) {
        this.apiRechargeCallbackUrl = apiRechargeCallbackUrl;
    }
    @Column(name = "isDelete")
    @Type(type = "boolean")
	public boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createTime", length = 19)
    @CreationTimestamp
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    @Override
    public String toString() {
        return "BusinessInfoPo [id=" + id + ", businessId=" + businessId + ", balance=" + balance
               + ", unSettlement=" + unSettlement + ", settlement=" + settlement + ", orderIncome="
               + orderIncome + ", orderCost=" + orderCost + ", profits=" + profits + ", memberId="
               + memberId + ", memberStartAt=" + memberStartAt + ", memberVailidity="
               + memberVailidity + ", authorizerAppid=" + authorizerAppid
               + ", authorizerAccessToken=" + authorizerAccessToken + ", expiresIn=" + expiresIn
               + ", tokenBeginTime=" + tokenBeginTime + ", refreshToken=" + refreshToken
               + ", funcInfo=" + funcInfo + ", isAuthorized=" + isAuthorized + ", authorizedTime="
               + authorizedTime + ", jsapiTicket=" + jsapiTicket + ", ticketStartTime="
               + ticketStartTime + ", ticketExpired=" + ticketExpired + ", proxyParentId="
               + proxyParentId + ", wechatOfficAccount=" + wechatOfficAccount + ", wechatInfo="
               + wechatInfo + ", isApiRechargeAuthorized=" + isApiRechargeAuthorized
               + ", apiRechargeAppId=" + apiRechargeAppId + ", apiRechargeKey=" + apiRechargeKey
               + ", apiRechargeIp=" + apiRechargeIp + ", apiRechargeCallbackUrl="
               + apiRechargeCallbackUrl + ", isDelete=" + isDelete + ", createTime=" + createTime
               + "]";
    }

}
