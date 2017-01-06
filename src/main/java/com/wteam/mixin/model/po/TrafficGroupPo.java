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
 * 流量分组实体类
 * @author vee
 */
@Entity
@Table(name = "traffic_group")
public class TrafficGroupPo implements java.io.Serializable, IPersistentObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**流量分组Id*/
    private Long id;
    
    /**流量分组名称*/
    private String name;
    
    /**运营商*/
    private int provider;
    
    /**省份:全国+34省*/
    private String province;
    
    /**详情信息*/
    private String info;
    
    /** 是否显示*/
    private boolean display;
    
    /**排序*/
    private int sort;

    /** 是否删除 */
    private boolean isDelete;
    
    /** 创建时间 */
    private Date createTime;

    public TrafficGroupPo() {	}
    
    
	public TrafficGroupPo(String name, int provider, String province, String info, boolean display, int sort,
			boolean isDelete) {
		this.name = name;
		this.provider = provider;
		this.province = province;
		this.info = info;
		this.display = display;
		this.sort = sort;
		this.isDelete = isDelete;
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

	public int getProvider() {
		return provider;
	}

	public void setProvider(int provider) {
		this.provider = provider;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
	@Type(type = "boolean")
	public boolean getDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
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
		return "TrafficGroupPo [id=" + id + ", name=" + name + ", provider=" + provider + ", province=" + province
				+ ", info=" + info + ", display=" + display + ", sort=" + sort + ", isDelete=" + isDelete
				+ ", createTime=" + createTime + "]";
	}
}
