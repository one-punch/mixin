package com.wteam.mixin.model.vo;

import java.math.BigDecimal;
import java.util.Date;

import com.wteam.mixin.define.IValueObject;

/**
 * 订单结算实体类
 * @author benko
 */
public class OrderSettlementVo implements IValueObject {
    /**ID*/
    private Long id;

    /**被结算订单ID*/
    private String orderNum;

    /**所属商家ID*/
    private Long businessId;

    /**订单实际收入*/
    private BigDecimal realIncome;

    /**结算状态
     *  0：未结算
        1：已结算
        2：转入余额
     **/
    private Integer state;

    /** 是否删除 */
    private boolean isDelete;

    /** 创建时间 */
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

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

    public boolean getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
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
        if (getClass() != obj.getClass()) return false;
        OrderSettlementVo other = (OrderSettlementVo)obj;
        if (id == null) {
            if (other.id != null) return false;
        }
        else if (!id.equals(other.id)) return false;
        return true;
    }

}
