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
 * 会员有效期实体类
 * @author vee
 */
@Entity
@Table(name = "member_vaildity")
public class MemberVaildityPo implements java.io.Serializable, IPersistentObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**ID*/
    private Long id;
    
    /**会员ID*/
    private Long memberId;
    
    /**价格*/
    private BigDecimal cost;
    
    /**会员有效期（天）*/
    private Integer memberVaildity;

    /** 是否删除 */
    private boolean isDelete;
    
    /** 创建时间 */
    private Date createTime;

    public MemberVaildityPo() {	}
    
	public MemberVaildityPo(Long memberId, BigDecimal cost, Integer memberVaildity) {
		this.memberId = memberId;
		this.cost = cost;
		this.memberVaildity = memberVaildity;
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
    
    @Column(nullable = false)
    public Long getMemberId() {
		return memberId;
	}

	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	@Column(precision=23, scale=6)
	public BigDecimal getCost() {
		return cost;
	}

	public void setCost(BigDecimal cost) {
		this.cost = cost;
	}

	public Integer getMemberVaildity() {
		return memberVaildity;
	}

	public void setMemberVaildity(Integer memberVaildity) {
		this.memberVaildity = memberVaildity;
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
		return "MemberVaildityPo [id=" + id + ", memberId=" + memberId + ", cost=" + cost + ", memberVaildity="
				+ memberVaildity + ", isDelete=" + isDelete + ", createTime=" + createTime + "]";
	}
}
