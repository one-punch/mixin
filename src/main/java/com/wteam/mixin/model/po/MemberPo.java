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
 * 会员实体类
 * @author vee
 */
@Entity
@Table(name = "member")
public class MemberPo implements java.io.Serializable, IPersistentObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**会员ID*/
    private Long id;
    
    /**会员名称*/
    private String name;
    
    /**排序*/
    private String sort;
    
    /**详情*/
    private String info;

    /** 是否删除 */
    private boolean isDelete;
    
    /** 创建时间 */
    private Date createTime;

    public MemberPo() {	}
    
	public MemberPo(String name, String sort, String info) {
		this.name = name;
		this.sort = sort;
		this.info = info;
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

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	@Type(type="text") 
	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
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
		return "MemberPo [id=" + id + ", name=" + name + ", sort=" + sort + ", info=" + info + ", isDelete=" + isDelete
				+ ", createTime=" + createTime + "]";
	}
}
