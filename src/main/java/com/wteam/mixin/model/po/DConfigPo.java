package com.wteam.mixin.model.po;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.wteam.mixin.define.IPersistentObject;


/**
 * 动态配置实体类
 * @author vee
 */
@Entity
@Table(name = "d_config")
public class DConfigPo implements java.io.Serializable, IPersistentObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**ID*/
    private Long id;
    
    /**属性名*/
    private String name;
    
    /**属性名*/
    private String value;

    /** 是否删除 */
    private boolean isDelete;
    
    /** 创建时间 */
    private Date createTime;

    public DConfigPo() {	}

	public DConfigPo(String name, String value) {
		super();
		this.name = name;
		this.value = value;
	}

	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    public void setId(Long userId) {
        this.id = userId;
    }
    
    public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Column(name = "isDelete")
    @Type(type = "boolean")
    public boolean getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createTime", length = 19)
    @CreationTimestamp
    public Date getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
