package com.wteam.mixin.model.po;

import java.util.Date;

import javax.persistence.*;

import lombok.Data;

import static javax.persistence.GenerationType.IDENTITY;

@Data
@Entity
@Table(name = "activities")
public class Activity extends BasePo {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = IDENTITY)
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
