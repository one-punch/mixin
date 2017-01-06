package com.wteam.mixin.model.vo;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.wteam.mixin.define.IValueObject;

public class ThemeVo implements IValueObject{
	
	private Long id;
	@NotNull
	private String name;
	private String sign;
	@NotNull
	private Long imgId;
	@NotNull
	private Boolean display;
	private boolean defaulted;
	@NotNull
	private BigDecimal cost;
	@NotNull
	private Integer vaildity;
	private Date createTime;
	private boolean isDelete;
	
	public ThemeVo() {	}
	public ThemeVo(String name, String sign, Long imgId, boolean defaulted, BigDecimal cost, Integer themeVaildity) {
		this.name = name;
		this.sign = sign;
		this.imgId = imgId;
		this.defaulted = defaulted;
		this.cost = cost;
		this.vaildity = themeVaildity;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Boolean isDisplay() {
		return display;
	}
	public void setDisplay(Boolean display) {
		this.display = display;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public Long getImgId() {
		return imgId;
	}
	public void setImgId(Long imgId) {
		this.imgId = imgId;
	}
	public boolean isDefaulted() {
		return defaulted;
	}
	public void setDefaulted(boolean defaulted) {
		this.defaulted = defaulted;
	}
	public BigDecimal getCost() {
		return cost;
	}
	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}
	public Integer getVaildity() {
        return vaildity;
    }
    public void setVaildity(Integer vaildity) {
        this.vaildity = vaildity;
    }
    public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	@Override
	public String toString() {
		return "ThemeVo [id=" + id + ", name=" + name + ", sign=" + sign + ", imgId=" + imgId + ", display=" + display
				+ ", defaulted=" + defaulted + ", cost=" + cost + ", vaildity=" + vaildity + ", createTime="
				+ createTime + ", isDelete=" + isDelete + "]";
	}
}
