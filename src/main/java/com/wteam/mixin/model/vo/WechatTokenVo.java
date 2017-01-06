package com.wteam.mixin.model.vo;
/**
 * 微信Token实体
 * <p>Title:</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月3日
 */
public class WechatTokenVo {
	
	private String name;
	
	private String value;
	
	private Long created;
	
	private Long expired;
	
	private String refreshToken;
	
	public WechatTokenVo() {	}

	public WechatTokenVo(String name, String value, Long created, Long expired) {
		this.name = name;
		this.created = created;
		this.expired = expired;
		this.value = value;
	}
	
	public WechatTokenVo(String name, String value, Long created, Long expired, String refreshToken) {
		this.name = name;
		this.created = created;
		this.expired = expired;
		this.value = value;
		this.refreshToken = refreshToken;
	}
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Long getExpired() {
		return expired;
	}

	public void setExpired(Long expired) {
		this.expired = expired;
	}
	
	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	@Override
	public String toString() {
		return "WechatToken [name=" + name + ", value=" + value + ", created=" + created + ", expired=" + expired
				+ ", refreshToken=" + refreshToken + "]";
	}
}
