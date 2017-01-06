package com.wteam.mixin.model.wexin;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * <p>Title: 微信统一下单 完成后返回给前台的支付参数信息</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月11日
 */
public class WcPayUniformOrderRespBody {
	
	/*
	 * prepay_id 通过微信支付统一下单接口拿到，paySign 采用统一的微信支付 Sign 签名生成方法，
	 * 注意这里 appId 也要参与签名，appId 与 config 中传入的 appId 一致，即最后参与签名的参数有appId, 
	 * timeStamp, nonceStr, package, signType。
	 */
	private String appId;
	private Long timeStamp;
	private String nonceStr;
	@JSONField(name = "package")
	private String packagee;
	private String signType = "MD5";
	private String paySign;
	public WcPayUniformOrderRespBody() {
	}
	public WcPayUniformOrderRespBody(String appid, Long timeStamp, String nonceStr, String packagee) {
		this.appId = appid;
		this.timeStamp = timeStamp;
		this.nonceStr = nonceStr;
		this.packagee = packagee;
	}
	public String getAppId() {
		return appId;
	}
	public void setAppId(String appid) {
		this.appId = appid;
	}
	public Long getTimeStamp() {
		return timeStamp;
	}
	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}
	public String getNonceStr() {
		return nonceStr;
	}
	public void setNonceStr(String nonceStr) {
		this.nonceStr = nonceStr;
	}
	public String getPackagee() {
		return packagee;
	}
	public void setPackagee(String packagee) {
		this.packagee = packagee;
	}
	public String getSignType() {
		return signType;
	}
	public void setSignType(String signType) {
		this.signType = signType;
	}
	public String getPaySign() {
		return paySign;
	}
	public void setPaySign(String paySign) {
		this.paySign = paySign;
	}
	@Override
	public String toString() {
		return "WcPayUniformOrderRespBody [appid=" + appId + ", timeStamp=" + timeStamp + ", nonceStr=" + nonceStr
				+ ", packagee=" + packagee + ", signType=" + signType + ", paySign=" + paySign + "]";
	}
}
