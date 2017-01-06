package com.wteam.mixin.model.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.wteam.mixin.define.IValueObject;

public class BusinessThemeVo implements IValueObject{
	
	private Long id;
	private Long themeId;
	private Long businessId;
	private String name;
	private Long imgId;
	private BigDecimal cost;
	private boolean actived;
	private Date startAt;
	private Integer vaildity;
	public BusinessThemeVo() {	}
	
	public BusinessThemeVo(Long id, Long themeId, String name, Long imgId,
	                       BigDecimal cost, boolean actived, Date startAt,
			Integer vaildity) {
		this.id = id;
		this.themeId = themeId;
		this.name = name;
		this.imgId = imgId;
		this.cost = cost;
		this.actived = actived;
		this.startAt = startAt;
		this.vaildity = vaildity;
	}
	
	public BusinessThemeVo(Long themeId, String name, Long imgId, BigDecimal cost) {
		this.themeId = themeId;
		this.name = name;
		this.imgId = imgId;
		this.cost = cost;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getThemeId() {
		return themeId;
	}
	public void setThemeId(Long themeId) {
		this.themeId = themeId;
	}
	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Long getImgId() {
		return imgId;
	}
	public void setImgId(Long imgId) {
		this.imgId = imgId;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	public boolean isActived() {
		return actived;
	}
	public void setActived(boolean actived) {
		this.actived = actived;
	}
	public Date getStartAt() {
		return startAt;
	}
	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}
	public Integer getVaildity() {
		return vaildity;
	}
	public void setVaildity(Integer vaildity) {
		this.vaildity = vaildity;
	}
	@Override
	public String toString() {
		return "BusinessThemeVo [id=" + id + ", themeId=" + themeId + ", name=" + name + ", imgId=" + imgId + ", cost="
				+ cost + ", actived=" + actived + ", startAt=" + startAt + ", vaildity=" + vaildity + "]";
	}
}
