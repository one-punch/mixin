package com.wteam.mixin.model.vo;

import com.wteam.mixin.define.IValueObject;

/**
 * <p>Title:流量查询值对象</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年7月29日
 */
public class TrafficPlanQueryVo implements IValueObject{
	
	private Integer provider;
	
	private String province;
	
	private Long trafficgroupId;
	
	private boolean isApiRecharge = false;

	public TrafficPlanQueryVo() {	}

	public TrafficPlanQueryVo(Integer provider, String province, Long trafficgroupId) {
		this.provider = provider;
		this.province = province;
		this.trafficgroupId = trafficgroupId;
	}
	

	public TrafficPlanQueryVo(Integer provider, String province, Long trafficgroupId,
                              boolean isApiRecharge) {
        this.provider = provider;
        this.province = province;
        this.trafficgroupId = trafficgroupId;
        this.isApiRecharge = isApiRecharge;
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

	public Long getTrafficgroupId() {
		return trafficgroupId;
	}

	public void setTrafficgroupId(Long trafficplanId) {
		this.trafficgroupId = trafficplanId;
	}

	public boolean getIsApiRecharge() {
        return isApiRecharge;
    }

    public void setIsApiRecharge(boolean isApiRecharge) {
        this.isApiRecharge = isApiRecharge;
    }

    @Override
	public String toString() {
		return "TrafficPlanQueryVo [provider=" + provider + ", province=" + province + ", trafficplanId="
				+ trafficgroupId + "]";
	}
}
