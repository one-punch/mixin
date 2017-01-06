package com.wteam.mixin.model.vo;

import javax.validation.constraints.NotNull;

import com.wteam.mixin.define.IValueObject;

/**
 * <p>Title:文档vo</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月15日
 */
public class DocumentVo implements IValueObject{
	
	private Long id;
	
	@NotNull
	private String name;
	
	private String description;
	
	@NotNull
	private String content;

	public DocumentVo() {	}
	
	public DocumentVo(Long id, String name) {
		this.id = id;
		this.name = name;
	}

	public DocumentVo(String name, String description, String content) {
		this.name = name;
		this.description = description;
		this.content = content;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String toString() {
		return "DocumentVo [id=" + id + ", name=" + name + ", description=" + description + ", content=" + content
				+ "]";
	}

}
