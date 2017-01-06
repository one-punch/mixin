package com.wteam.mixin.model.vo;

import java.util.Date;
import java.util.List;

import com.wteam.mixin.define.IValueObject;

public class CustomMenuVo implements IValueObject{
	
	 /**ID*/
    private Long id;

    /**所属商家ID*/
    private Long businessId;
    
    /**该菜单名称*/
    private String name;
    
    /**该菜单key值*/
    private String key;
    
    /**链接地址*/
    private String url;
    
    /**用于标记该菜单所跳转的页面，该参数值必须包含于user_config中的属性文件*/
    private String menuKey;
    
    /**菜单类型:CLICK?VIEW?...*/
    private String type;
    
    /**级别：一级？二级*/
    private Integer level;
    
    /**父菜单ID：默认0*/
    private Long parentId = 0L;
    
    /** 是否删除 */
    private boolean isDelete;
    
    /** 创建时间 */
    private Date createTime;

	public CustomMenuVo() {	}
	public CustomMenuVo(Long businessId, String name, String key, String url, String type, Integer level, Long parentId) {
		this.businessId = businessId;
		this.name = name;
		this.key = key;
		this.url = url;
		this.type = type;
		this.level = level;
		this.parentId = parentId;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	
	public String getMenuKey() {
		return menuKey;
	}
	public void setMenuKey(String menu) {
		this.menuKey = menu;
	}
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer levle) {
		this.level = levle;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public boolean isDelete() {
		return isDelete;
	}

	public void setDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	@Override
	public String toString() {
		return "CustomMenuVo [id=" + id + ", businessId=" + businessId + ", name=" + name + ", key=" + key + ", url="
				+ url + ", menu=" + menuKey + ", type=" + type + ", level=" + level + ", parentId=" + parentId
				+ ", isDelete=" + isDelete + ", createTime=" + createTime + "]";
	}
}
