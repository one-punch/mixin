package com.wteam.mixin.model.query;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.validation.constraints.Pattern;

import com.wteam.mixin.constant.ValidatePattern;
import com.wteam.mixin.define.IValueObject;

public class CustomerOrderQuery extends AbstractQuery implements IValueObject {

    /**订单号*/
    private String orderNum;
    
    /**接口充值外部订单ID*/
    private String businessOutOrderNum;

    /**外部订单ID*/
    private String outOrderId;

    /**支付订单ID*/
    private String alipayOrderId;
    
    /**顾客手机号码*/
    @Pattern(regexp = ValidatePattern.TEL)
    private String phone;

    /**顾客Id*/
    private Long customerId;

    /**商家Id*/
    private Long businessId;
    
    /**订单状态*/
    private Integer state;

    /**收单状态*/
    private Integer shouDanState;
    
    /**支付方式*/
    private Integer paymentMethod;
    
    /**产品类*/
    private String productType;
    
    /** 开始时间 */
    private Date startAt;

    /** 结束时间 */
    private Date endAt;

    /**是否给商家的回调*/
    private Boolean isBusinessCallback;
    
    /** 是否删除 */
    private Boolean isDelete;

    public static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String hqlQuery(String as) {
        String name = as != null && !"".equals(as) ? as+"." : "";
        String query = "", relation = "";

        if (orderNum != null && !"".equals(orderNum)) {
            query += String.format(" %sorderNum='%s' ", name,orderNum);
        }
        if (outOrderId != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %soutOrderId='%s' ", relation, name, outOrderId);
        }
        if (businessOutOrderNum != null && !"".equals(businessOutOrderNum)) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sbusinessOutOrderNum='%s' ", relation, name,businessOutOrderNum);
        }
        if (alipayOrderId != null && !"".equals(alipayOrderId)) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %salipayOrderId='%s' ", relation, name, alipayOrderId);
        }
        if (phone != null && !"".equals(phone)) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sphone='%s' ", relation, name,phone);
        }
        if (customerId != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %scustomerId=%d ", relation, name,customerId);
        }
        if (businessId != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sbusinessId=%d ", relation, name,businessId);
        }
        if (state != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sstate=%d ", relation, name,state);
        }
        if (shouDanState != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sshouDanState=%d ", relation, name,shouDanState);
        }
        if (paymentMethod != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %spaymentMethod=%d ", relation, name,paymentMethod);
        }
        if (productType != null && !"".equals(productType)) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sproductType='%s' ", relation, name,productType);
        }
        if (startAt != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %screateTime>='%s' ", relation, name, FORMAT.format(startAt));
        }
        if (endAt != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %screateTime<='%s' ", relation, name, FORMAT.format(endAt));
        }
        if (isBusinessCallback != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sisBusinessCallback=%b ", relation, name,isBusinessCallback);
        }
        if (isDelete != null) {
            relation = "".equals(query) ? "" : "and";
            query += String.format("%s %sisDelete=%b ", relation, name,isDelete);
        }
        
        return query;
    }
    

    @Override
    public String hqlWhereQuery(String as) {
        String query = hqlQuery(as);
        return "".equals(query) ? query : " where " + query;
    }
    
    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getBusinessOutOrderNum() {
        return businessOutOrderNum;
    }

    public void setBusinessOutOrderNum(String businessOutOrderNum) {
        this.businessOutOrderNum = businessOutOrderNum;
    }

    public String getOutOrderId() {
        return outOrderId;
    }

    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }

    public String getAlipayOrderId() {
        return alipayOrderId;
    }

    public void setAlipayOrderId(String alipayOrderId) {
        this.alipayOrderId = alipayOrderId;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getShouDanState() {
        return shouDanState;
    }

    public void setShouDanState(Integer shouDanState) {
        this.shouDanState = shouDanState;
    }

    public Integer getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(Integer paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Date getStartAt() {
        return startAt;
    }

    public void setStartAt(Date startAt) {
        this.startAt = startAt;
    }

    public Date getEndAt() {
        return endAt;
    }

    public void setEndAt(Date endAt) {
        this.endAt = endAt;
    }

    public Boolean getIsBusinessCallback() {
        return isBusinessCallback;
    }
    public void setIsBusinessCallback(Boolean isBusinessCallback) {
        this.isBusinessCallback = isBusinessCallback;
    }
    
    public Boolean getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
        this.isDelete = isDelete;
    }

    
}
