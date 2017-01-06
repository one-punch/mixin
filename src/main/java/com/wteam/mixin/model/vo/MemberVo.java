package com.wteam.mixin.model.vo;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.wteam.mixin.define.IValueObject;

public class MemberVo implements IValueObject{
	
	private Long id;
	@NotNull
	private String name;
	
	private String sort;
	@NotNull
	private String info;
	
	private List<MemberVaildityVo> vailditys; 

	public MemberVo() {	}

	public MemberVo(String name, String sort, String info) {
		this.name = name;
		this.sort = sort;
		this.info = info;
	}

	public Long getId() {
		return id;
	}

	@Override
	public String toString() {
		return "MemberVo [id=" + id + ", name=" + name + ", sort=" + sort + ", info=" + info + ", vaildityLst="
				+ vailditys + "]";
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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public List<MemberVaildityVo> getVailditys() {
		return vailditys;
	}

	public void setVailditys(List<MemberVaildityVo> vaildityLst) {
		this.vailditys = vaildityLst;
	}
}
