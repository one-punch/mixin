package com.wteam.mixin.model.vo;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;

import com.wteam.mixin.define.IValueObject;

public class TrafficPlanVo implements IValueObject{
    
    /**
     * TrafficgroupPo as groupPo
     * TrafficPlanPo as plan
     */
    public static final String SELECT_1 = "select new com.wteam.mixin.model.vo.TrafficPlanVo("
        + "plan.id,plan.name,plan.value,plan.cost,"
        + "plan.retailPrice,plan.display,plan.isMaintain,"
        + "plan.provider,plan.pid,plan.trafficGroupId,"
        + "groupPo.name,groupPo.province,plan.apiProvider,plan.integral) ";
    /**
     * TrafficgroupPo as groupPo
     * TrafficPlanPo as plan
     */
    public static final String SELECT_2 = "select new com.wteam.mixin.model.vo.TrafficPlanVo("
        + "plan.id,plan.name,plan.value,plan.cost,"
        + "plan.retailPrice,plan.display,plan.isMaintain,"
        + "plan.provider, plan.trafficGroupId,"
        + "groupPo.name,groupPo.province,plan.productNum) ";
    
    
	
//	ID（id）
//	名字（name）
//	值（value）
//	成本价（cost）
//	默认零售价（retailPrice）
//	是否上架（display）
//	维修中（isMaintain）
//	运营商（provider）
//	产品编号（pid）
//	流量分组（tranfficgroudId）
//	接口提供商（apiProvider）
//	奖励积分（integral）
	private Long id;
    @NotNull
	private String name;
    @NotNull
	private String value;
    @NotNull
	private BigDecimal cost;
    @NotNull
	private BigDecimal retailPrice;
    @NotNull
	private boolean display;
    @NotNull
	private boolean isMaintain;
    @NotNull
	private int provider;
    @NotNull
	private String pid;
    @NotNull
	private Long trafficgroupId;
    private String groupName;
    private String province;
    @NotNull
	private String apiProvider;
    @NotNull
	private Integer integral;
    /**是否使用自动识别功能*/
    private boolean isAuto;
	
	private String tip;

    //----------------接口充值--------------------------//
    /**是否是接口充值*/
    private boolean isApiRecharge;
    /**产品编号*/
    private String productNum;
	
	public TrafficPlanVo() {	}
	
	/**
	 * SELECT_1
	 */
	public TrafficPlanVo(Long id, String name, String value, BigDecimal cost, 
	        BigDecimal retailPrice, boolean display,
			boolean isMaintain, int provider, String pid, 
			Long trafficgroupId, String groupName, String province,
			String apiProvider, Integer integral) {
		this.id = id;
		this.name = name;
		this.value = value;
		this.cost = cost;
		this.retailPrice = retailPrice;
		this.display = display;
		this.isMaintain = isMaintain;
		this.provider = provider;
		this.pid = pid;
		this.trafficgroupId = trafficgroupId;
        this.groupName = groupName;
        this.province = province;
		this.apiProvider = apiProvider;
		this.integral = integral;
	}
    /**
     * SELECT_2
     */
    public TrafficPlanVo(Long id, String name, String value, BigDecimal cost, 
            BigDecimal retailPrice, boolean display,
            boolean isMaintain, int provider,
            Long trafficgroupId, String groupName, String province,
            String productNum) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.cost = cost;
        this.retailPrice = retailPrice;
        this.display = display;
        this.isMaintain = isMaintain;
        this.provider = provider;
        this.trafficgroupId = trafficgroupId;
        this.groupName = groupName;
        this.province = province;
        this.productNum = productNum;
    }
	
    public TrafficPlanVo(Long id, String name, String value, BigDecimal cost, BigDecimal retailPrice, boolean display,
                         boolean isMaintain, int provider, String pid, Long trafficgroupId,String apiProvider, Integer integral) {
                     this.id = id;
                     this.name = name;
                     this.value = value;
                     this.cost = cost;
                     this.retailPrice = retailPrice;
                     this.display = display;
                     this.isMaintain = isMaintain;
                     this.provider = provider;
                     this.pid = pid;
                     this.trafficgroupId = trafficgroupId;
                     this.apiProvider = apiProvider;
                     this.integral = integral;
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public BigDecimal getRetailPrice() {
		return retailPrice;
	}

	public void setRetailPrice(BigDecimal retailPrice) {
		this.retailPrice = retailPrice;
	}

	public boolean getDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public boolean getIsMaintain() {
		return isMaintain;
	}

	public void setIsMaintain(boolean isMaintain) {
		this.isMaintain = isMaintain;
	}

	public int getProvider() {
		return provider;
	}

	public void setProvider(int provider) {
		this.provider = provider;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	public Long getTrafficgroupId() {
		return trafficgroupId;
	}

	public void setTrafficgroupId(Long trafficgroupId) {
		this.trafficgroupId = trafficgroupId;
	}

	public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getApiProvider() {
		return apiProvider;
	}

	public void setApiProvider(String apiProvider) {
		this.apiProvider = apiProvider;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public boolean getIsApiRecharge() {
        return isApiRecharge;
    }

    public void setIsApiRecharge(boolean isApiRecharge) {
        this.isApiRecharge = isApiRecharge;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
    }
    
    public boolean getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }
    
    @Override
	public String toString() {
		return "TrafficPlanVo [id=" + id + ", name=" + name + ", value=" + value + ", cost=" + cost + ", retailPrice="
				+ retailPrice + ", display=" + display + ", isMaintain=" + isMaintain + ", provider=" + provider
				+ ", pid=" + pid + ", trafficgroupId=" + trafficgroupId + ", apiProvider=" + apiProvider + ", integral="
				+ integral + "]";
	}
}
