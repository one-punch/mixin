package com.wteam.mixin.model.po;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.wteam.mixin.define.IPersistentObject;

/**
 * 代理商家流量套餐
 * @author benko
 *
 */
@Entity
@Table(name = "proxy_business_traffic_plan")
public class ProxyBusinessTrafficPlanPo implements Serializable, IPersistentObject {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**ID*/
    private Long id;

    /**商家ID*/
    private Long businessId;

    /**代理父商家ID*/
    private Long parentId;

    /**相应流量ID*/
    private Long trafficplanId;

    /**父商家设置的成本价*/
    private BigDecimal cost;

    /** 是否删除 */
    private boolean isDelete;

    /** 创建时间 */
    private Date createTime;

    public ProxyBusinessTrafficPlanPo() {
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getBusinessId() {
        return businessId;
    }
    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }
    public Long getParentId() {
        return parentId;
    }
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }
    public Long getTrafficplanId() {
        return trafficplanId;
    }
    public void setTrafficplanId(Long trafficplanId) {
        this.trafficplanId = trafficplanId;
    }
    public BigDecimal getCost() {
        return cost;
    }
    public void setCost(BigDecimal cost) {
        this.cost = cost;
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
        return "ProxyBusinessTrafficPlanPo [id=" + id + ", businessId=" + businessId
               + ", parentId=" + parentId + ", trafficplanId=" + trafficplanId + ", cost=" + cost
               + ", isDelete=" + isDelete + ", createTime=" + createTime + "]";
    }

}
