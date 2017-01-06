package com.wteam.mixin.model.vo;

/**
 * <p>Title:流量分组查询条件 实体对象</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月11日
 */
public class TrafficGroupQueryVo {
	
	/*
	 * *int  provider
		*str  province
		*boolean hasInfo
	 */
	private Integer provider;
	private String province;
	public TrafficGroupQueryVo() {	}
	public TrafficGroupQueryVo(Integer provider, String province) {
		this.provider = provider;
		this.province = province;
	}
	public Integer getProvider() {
		return provider;
	}
	public void setProvider(Integer provider) {
		this.provider = provider;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	@Override
	public String toString() {
		return "TrafficGroupQueryVo [provider=" + provider + ", province=" + province + "]";
	}
}
