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
 * 订单结算实体类
 * @author vee
 */
@Entity
@Table(name = "order_settlement")
public class OrderSettlementPo implements java.io.Serializable, IPersistentObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**ID*/
    private Long id;

    /**被结算订单ID*/
    private String orderNum;
    
    /**所属商家ID*/
    private Long businessId;
    
    /**订单实际收入*/
    private BigDecimal realIncome;
    
    /**结算状态
     * 	0：未结算
		1：已结算
		2：转入余额
     **/
    private Integer state;
    
    /** 是否删除 */
    private boolean isDelete;
    
    /** 创建时间 */
    private Date createTime;


    public OrderSettlementPo() { }
    
    public OrderSettlementPo(String orderNum, Long businessId, BigDecimal realIncome, Integer state) {
        this.orderNum = orderNum;
        this.businessId = businessId;
        this.realIncome = realIncome;
        this.state = state;
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
    public String getOrderNum() {
		return orderNum;
	}

	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	@Column(nullable = false)
	public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	@Column(precision = 23, scale = 6)  
	public BigDecimal getRealIncome() {
		return realIncome;
	}

	public void setRealIncome(BigDecimal realIncome) {
		this.realIncome = realIncome;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (OrderSettlementPo.class.isAssignableFrom(obj.getClass())) return false;
        OrderSettlementPo other = (OrderSettlementPo)obj;
        if (id == null) {
            if (other.id != null) return false;
        }
        else if (!id.equals(other.id)) return false;
        return true;
    }

    @Override
    public String toString() {
        return "OrderSettlementPo [id=" + id + ", orderNum=" + orderNum + ", businessId="
               + businessId + ", realIncome=" + realIncome + ", state=" + state + ", isDelete="
               + isDelete + ", createTime=" + createTime + "]";
    }
    
    
}
