package com.wteam.mixin.model.vo;

import java.util.List;

import com.wteam.mixin.define.IValueObject;

public class GroupNPlanVo implements IValueObject{
	
//	 "name":"",
//     "province":"",
//     "provider":"",
//     "sort":"",
//     "trafficplanList":[],
    private Long id;
	private String name;
	private String province;
	private Integer provider;
    private String info;
	private Integer sort;
	private List<TrafficPlanVo> trafficplanList;
	public GroupNPlanVo() {	}
	public GroupNPlanVo(String name, String province, Integer provider, Integer sort,
			List<TrafficPlanVo> trafficplanList) {
		this.name = name;
		this.province = province;
		this.provider = provider;
		this.sort = sort;
		this.trafficplanList = trafficplanList;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	
	public Integer getProvider() {
		return provider;
	}
	public void setProvider(Integer provider) {
		this.provider = provider;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getInfo() {
        return info;
    }
    public void setInfo(String info) {
        this.info = info;
    }
    public List<TrafficPlanVo> getTrafficplanList() {
		return trafficplanList;
	}
	public void setTrafficplanList(List<TrafficPlanVo> trafficplanList) {
		this.trafficplanList = trafficplanList;
	}
	@Override
	public String toString() {
		return "GroupNPlanVo [name=" + name + ", province=" + province + ", provider=" + provider + ", sort=" + sort
				+ ", trafficplanList=" + trafficplanList + "]";
	}
}
