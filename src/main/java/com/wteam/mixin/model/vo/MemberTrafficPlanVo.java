package com.wteam.mixin.model.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.wteam.mixin.define.IValueObject;

public class MemberTrafficPlanVo implements IValueObject{
	private Long id;
    private String memberName;
	@NotNull
	private Long memberId;
	@NotNull
	private Long trafficPlanId;
	@NotNull
	private BigDecimal cost;

    private BigDecimal memberCost;
	private String name;
	private String value;
	private BigDecimal retailPrice;
	private boolean display;
	private Integer provider;
	private String apiProvider;
	
	public MemberTrafficPlanVo() {	}
//	  + "memPlan.id,m.name, m.id, plan.id, "
//    + "plan.cost, memPlan.cost, plan.name, plan.value, plan.retailPrice, "
//    + "plan.display, plan.provider, plan.apiProvider
	public MemberTrafficPlanVo(Long id, String memberName, Long memberId, Long trafficPlanId, 
	                           BigDecimal cost, BigDecimal memberCost, String name, String value, BigDecimal retailPrice, 
	                           boolean display, Integer provider, String apiProvider) {
		this.id = id;
        this.memberName = memberName;
		this.memberId = memberId;
		this.trafficPlanId = trafficPlanId;
        this.cost = cost;
		this.memberCost = memberCost;
		this.name = name;
		this.value = value;
		this.retailPrice = retailPrice;
		this.display = display;
		this.provider = provider;
		this.apiProvider = apiProvider;
	}
	public MemberTrafficPlanVo(Long memberId, String memberName, Long trafficPlanId, BigDecimal memberCost) {
		this.memberId = memberId;
		this.trafficPlanId = trafficPlanId;
		this.cost = memberCost;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getMemberName() {
        return memberName;
    }
    public void setMemberName(String memberName) {
        this.memberName = memberName;
    }
    public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Long getTrafficPlanId() {
		return trafficPlanId;
	}
	public void setTrafficPlanId(Long trafficPlanId) {
		this.trafficPlanId = trafficPlanId;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal memberCost) {
		this.cost = memberCost;
	}
	public BigDecimal getMemberCost() {
        return memberCost;
    }
    public void setMemberCost(BigDecimal memberCost) {
        this.memberCost = memberCost;
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
	public BigDecimal getRetailPrice() {
		return retailPrice;
	}
	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}
	public boolean isDisplay() {
		return display;
	}
	public void setDisplay(boolean display) {
		this.display = display;
	}
	public Integer getProvider() {
		return provider;
	}
	public void setProvider(Integer provider) {
		this.provider = provider;
	}
	public String getApiProvider() {
		return apiProvider;
	}
	public void setApiProvider(String apiProvider) {
		this.apiProvider = apiProvider;
	}
	@Override
	public String toString() {
		return "MemberTrafficPlanVo [id=" + id + ", memberId=" + memberId + ", trafficPlanId=" + trafficPlanId
				+ ", memberCost=" + cost + ", name=" + name + ", value=" + value + ", retailPrice=" + retailPrice
				+ ", display=" + display + ", provider=" + provider + ", apiProvider=" + apiProvider
				+ "]";
	}
}
