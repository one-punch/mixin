package com.wteam.mixin.model.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.wteam.mixin.define.IPersistentObject;

/**
 * 商家菜单栏 标志实体类
 * <p>Title:</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月13日
 */
@Entity
@Table(name="business_menu")
public class BusinessMenuPo implements java.io.Serializable,IPersistentObject{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/**主键ID*/
	private Long id;
	/**键（key）*/
	private String menuKey;
	/**名（name）*/
	private String name;
	/**醒目标志（tip）*/
	private String tip;
	/**是否已删除*/
	private boolean isDelete;
	/**创建时间*/
	private Date createTime;

	public BusinessMenuPo() {	}

	public BusinessMenuPo(String key, String name, String tip) {
		this.menuKey = key;
		this.name = name;
		this.tip = tip;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id" , nullable=false, unique=true)
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

	@Type(type="boolean")
	public boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@CreationTimestamp
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public String toString() {
		return "BusinessMenuPo [id=" + id + ", key=" + menuKey + ", name=" + name + ", tip=" + tip + ", isDelete="
				+ isDelete + ", createTime=" + createTime + "]";
	}
	
}
