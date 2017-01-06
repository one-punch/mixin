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
 * <p>Title:文档持久化对象</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月15日
 */
@Entity
@Table(name="m_document")
public class DocumentPo implements java.io.Serializable, IPersistentObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Long id;
	
	private String name;
	
	private String description;
	
	private String content;
	
	private boolean isDelete;
	
	private Date createTime;

	public DocumentPo() {	}

	public DocumentPo(String name, String description, String content) {
		this.name = name;
		this.description = description;
		this.content = content;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false, unique=true)
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Type(type="text")
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Type(type="boolean")
	public boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete){
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
		return "DocumentPo [id=" + id + ", name=" + name + ", description=" + description + ", content=" + content
				+ ", isDelete=" + isDelete + ", createTime=" + createTime + "]";
	}
	
}










