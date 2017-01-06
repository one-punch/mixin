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
 * 商家主题实体类
 * @author vee
 */
/**
 * @author vee
 *
 */
@Entity
@Table(name = "business_theme")
public class BusinessThemePo implements java.io.Serializable, IPersistentObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**ID*/
    private Long id;

    /**对应主题ID*/
    private Long themeId;
    
    /**所属商家ID*/
    private Long businessId;
    
    /**购买时间*/
    private Date startAt;
    
    /**是否正在使用中*/
    private boolean actived;
    
    /**主题有效期*/
    private Integer vaildity;
    
    /** 是否删除 */
    private boolean isDelete;
    
    /** 创建时间 */
    private Date createTime;
    
    public BusinessThemePo() {	}

	public BusinessThemePo(Long businessId, Date startAt, boolean actived, int vaildity) {
		this.businessId = businessId;
		this.startAt = startAt;
		this.actived = actived;
		this.vaildity = vaildity;
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
    public Long getThemeId() {
		return themeId;
	}

	public void setThemeId(Long themeId) {
		this.themeId = themeId;
	}

	@Column(nullable = false)
	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	
	@Temporal(TemporalType.TIMESTAMP)
	public Date getStartAt() {
		return startAt;
	}

	public void setStartAt(Date startAt) {
		this.startAt = startAt;
	}

	@Column(name = "actived")
	@Type(type="boolean")
	public boolean getActived() {
		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
	}

	public Integer getVaildity() {
		return vaildity;
	}

	public void setVaildity(Integer validity) {
		this.vaildity = validity;
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
		return "BusinessThemePo [id=" + id + ", themeId=" + themeId + ", businessId=" + businessId + ", startAt="
				+ startAt + ", actived=" + actived + ", vaildity=" + vaildity + ", isDelete=" + isDelete
				+ ", createTime=" + createTime + "]";
	}
}
