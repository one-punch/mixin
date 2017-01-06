package com.wteam.mixin.model.vo;

import javax.validation.constraints.NotNull;

import com.wteam.mixin.define.IValueObject;

/**
 * 
 * <p>Title:流量分组值对象</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年7月29日
 */
public class TrafficGroupVo implements IValueObject{

//	分组ID（id）
//	运营商（provider）
//	省（province）
//	显示（display）
//	详情（info）
//	排序（sort）

	private Long id;
	private String name;
	private Integer provider;
	private String province;
	private Boolean display;
	private String info;
	private Integer sort;
	public TrafficGroupVo() {	}
	public TrafficGroupVo(Long id,String name, int provider, String province, boolean display, int sort) {
		this.id = id;
        this.name = name;
		this.provider = provider;
		this.province = province;
		this.display = display;
		this.sort = sort;
	}
	public TrafficGroupVo(Long id,String name, int provider, String province, boolean display, String info, int sort) {
		this.id = id;
        this.name = name;
		this.provider = provider;
		this.province = province;
		this.display = display;
		this.info = info;
		this.sort = sort;
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
	public Boolean getDisplay() {
		return display;
	}
	public void setDisplay(Boolean display) {
		this.display = display;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Integer getSort() {
		return sort;
	}
	public void setSort(Integer sort) {
		this.sort = sort;
	}
	@Override
	public String toString() {
		return "TrafficGroupVo [id=" + id + ", name=" + name + ", provider=" + provider + ", province=" + province
				+ ", display=" + display + ", info=" + info + ", sort=" + sort + "]";
	}
}
