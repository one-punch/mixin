package com.wteam.mixin.model.vo;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.wteam.mixin.define.IValueObject;
/**
 * 代理商家流量套餐
 * @author benko
 *
 */
public class ProxyBusinessTrafficPlanVo implements IValueObject {

    /**ID*/
    private Long id;

    /**商家ID*/
    @NotNull
    private Long businessId;

    /**代理父商家ID*/
    private Long parentId;

    /**相应流量ID*/
    @NotNull
    private Long trafficplanId;

    /**会员特享成本价*/
    @NotNull
    private BigDecimal cost;

    /** 是否删除 */
    private boolean isDelete;

    /** 创建时间 */
    private Date createTime;

    public ProxyBusinessTrafficPlanVo() {
    }

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
    public boolean getIsDelete() {
        return this.isDelete;
    }
    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }
    public Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
