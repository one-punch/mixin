package com.wteam.mixin.model.po;


import java.math.BigDecimal;
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
 * 主题实体类
 * @author vee
 */
@Entity
@Table(name = "theme")
public class ThemePo implements java.io.Serializable, IPersistentObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**ID*/
    private Long id;
    
    /**主题名称*/
    private String name;
    
    /**标志*/
    private String sign;
    
    /**图片ID*/
    private Long imgId;
    
    /**是否默认*/
    private boolean defaulted;
    
    private boolean display;
    
    /**价格*/
    private BigDecimal cost;
    
    /**主题有效期(单位：天)*/
    private Integer vaildity;

    /** 是否删除 */
    private boolean isDelete;
    
    /** 创建时间 */
    private Date createTime;

    public ThemePo() {	}
    
	public ThemePo(String name, String sign, Long imgId, boolean defaulted, BigDecimal cost, Integer themeVaildity, boolean isDelete, boolean display) {
		this.name = name;
		this.sign = sign;
		this.imgId = imgId;
		this.defaulted = defaulted;
		this.cost = cost;
		this.vaildity = themeVaildity;
		this.isDelete = isDelete;
		this.display = display;
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

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public Long getImgId() {
		return imgId;
	}

	public void setImgId(Long imgId) {
		this.imgId = imgId;
	}

	@Column(name = "defaulted")
	@Type(type="boolean")
	public boolean getDefaulted() {
		return defaulted;
	}

	public void setDefaulted(boolean defaulted) {
		this.defaulted = defaulted;
	}
	
	@Type(type="boolean")
	public boolean getDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}
	@Column(precision=23, scale=6)
	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public Integer getVaildity() {
		return vaildity;
	}

	public void setVaildity(Integer themeVaildity) {
		this.vaildity = themeVaildity;
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

	@Override
	public String toString() {
		return "ThemePo [id=" + id + ", name=" + name + ", sign=" + sign + ", imgId=" + imgId + ", defaulted="
				+ defaulted + ", cost=" + cost + ", themeVaildity=" + vaildity + ", isDelete=" + isDelete
				+ ", createTime=" + createTime + "]";
	}
}
