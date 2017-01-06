package com.wteam.mixin.model.vo;

import java.math.BigDecimal;

import com.wteam.mixin.define.IValueObject;

public class MTrafficPlanVo implements IValueObject{
	
	private Long id;
	private BigDecimal cost;
	
	public MTrafficPlanVo() {	}

	public MTrafficPlanVo(Long id, BigDecimal cost) {
		this.id = id;
		this.cost = cost;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	@Override
	public String toString() {
		return "MTrafficPlanVo [id=" + id + ", cost=" + cost + "]";
	}
}
