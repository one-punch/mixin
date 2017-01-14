package com.wteam.mixin.model.po;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "activities")
@MappedSuperclass
public class Activity extends BasePo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public Long id;
	
	public String name;
	
	public String code;
	
	@Column(name = "createdAt")
	public Date createdAt;

	@Column(name = "updatedAt")
	public Date updatedAt;
	

	@PrePersist
	void createdAt() {
	    this.createdAt = this.updatedAt = new Date();
	}
	
	  @PreUpdate
	void updatedAt() {
	    this.updatedAt = new Date();
	}

	  
}
