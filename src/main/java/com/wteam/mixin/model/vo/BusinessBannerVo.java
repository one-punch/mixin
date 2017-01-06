package com.wteam.mixin.model.vo;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.URL;

import com.wteam.mixin.define.IValueObject;

public class BusinessBannerVo implements IValueObject{
	
//	广告名（name）
//	图片ID（imgId）
//	是否显示（active）
//	跳转链接（url）

	private Long id;
	@NotNull
	private String name;
	@NotNull
	private Long imgId;
	private boolean actived;
	@URL
	private String url;
	
	
	
	public BusinessBannerVo() {
	}

	public BusinessBannerVo(String name, Long imgId, boolean actived, String url) {
		this.name = name;
		this.imgId = imgId;
		this.actived = actived;
		this.url = url;
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
	public Long getImgId() {
		return imgId;
	}
	public void setImgId(Long imgId) {
		this.imgId = imgId;
	}
	public boolean isActived() {
		return actived;
	}
	public void setActived(boolean actived) {
		this.actived = actived;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	@Override
	public String toString() {
		return "businessBannerVO [id=" + id + ", name=" + name + ", imgId=" + imgId + ", actived=" + actived + ", url="
				+ url + "]";
	}
}
