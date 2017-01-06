package com.wteam.mixin.model.vo;

import com.wteam.mixin.define.IValueObject;

public class RoleVo implements IValueObject {
	
	private Long roleId;
	private String role;
	
	
	public Long getRoleId() {
		return roleId;
	}
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
	
}
