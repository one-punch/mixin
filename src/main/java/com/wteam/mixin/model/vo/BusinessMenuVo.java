package com.wteam.mixin.model.vo;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.wteam.mixin.define.IValueObject;

/**
 * 商家菜单栏 标志实体类
 * <p>Title:</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月13日
 */
public class BusinessMenuVo implements IValueObject{
	
	/**主键ID*/
	private Long id;
	/**键（key）*/
	@NotNull
	private String menuKey;
	/**名（name）*/
	private String name;
	/**醒目标志（tip）*/
	@NotNull
	private String tip;
	/**是否已删除*/
	private boolean isDelete;
	/**创建时间*/
	private Date createTime;

	public BusinessMenuVo() {	}

	public BusinessMenuVo(String key, String name, String tip) {
		this.menuKey = key;
		this.name = name;
		this.tip = tip;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMenuKey() {
		return menuKey;
	}

	public void setMenuKey(String key) {
		this.menuKey = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getTip() {
		return tip;
	}

	public void setTip(String tip) {
		this.tip = tip;
	}

	public boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String toString() {
		return "BusinessMenuPo [id=" + id + ", key=" + menuKey + ", name=" + name + ", tip=" + tip + ", isDelete="
				+ isDelete + ", createTime=" + createTime + "]";
	}
	
}
