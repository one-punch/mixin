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
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.wteam.mixin.define.IPersistentObject;


/**
 * 流量套餐实体类
 * 
 * @author vee
 */
@Entity
@Table(name = "traffic_plan")
public class TrafficPlanPo implements java.io.Serializable, IPersistentObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 流量套餐Id */
    private Long id;

    /** 流量套餐所属分组 */
    private Long trafficGroupId;

    /** 套餐名字 */
    private String name;

    /** 套餐值：单位:MB */
    private String value;

    /** 套餐成本价 */
    private BigDecimal cost;

    /** 默认零售价 */
    private BigDecimal retailPrice;

    /** 是否上架 */
    private boolean display;

    /** 运营商 */
    private Integer provider;

    /** 产品编号 */
    private String pid;

    /** 接口提供商 */
    private String apiProvider;

    /** 充值奖励积分 */
    private Integer integral;

    /** 是否处于维护中 */
    private boolean isMaintain;

    /** 是否使用自动识别功能 */
    private boolean isAuto;

    // ----------------接口充值--------------------------//
    /** 是否是接口充值 */
    private boolean isApiRecharge;

    /** 产品编号 */
    private String productNum;

    /** 是否删除 */
    private boolean isDelete;

    /** 创建时间 */
    private Date createTime;

    public TrafficPlanPo() {}

    public TrafficPlanPo(String name, String value, BigDecimal cost, BigDecimal retailPrice,
                         boolean display, Integer provider, String apiProvider) {
        this.name = name;
        this.value = value;
        this.cost = cost;
        this.retailPrice = retailPrice;
        this.display = display;
        this.provider = provider;
        this.apiProvider = apiProvider;
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

    public Long getTrafficGroupId() {
        return trafficGroupId;
    }

    public void setTrafficGroupId(Long trafficGroupId) {
        this.trafficGroupId = trafficGroupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Column(precision = 23, scale = 6)
    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    @Column(name = "display")
    @Type(type = "boolean")
    public boolean getDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public Integer getProvider() {
        return provider;
    }

    public void setProvider(Integer provider) {
        this.provider = provider;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getApiProvider() {
        return apiProvider;
    }

    public void setApiProvider(String apiProvider) {
        this.apiProvider = apiProvider;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

    @Type(type = "boolean")
    public boolean getIsMaintain() {
        return isMaintain;
    }

    public void setIsMaintain(boolean isMaintain) {
        this.isMaintain = isMaintain;
    }

    @Type(type = "boolean")
    public boolean getIsAuto() {
        return isAuto;
    }

    public void setIsAuto(boolean isAuto) {
        this.isAuto = isAuto;
    }

    @Type(type = "boolean")
    public boolean getIsApiRecharge() {
        return isApiRecharge;
    }

    public void setIsApiRecharge(boolean isApiRecharge) {
        this.isApiRecharge = isApiRecharge;
    }

    public String getProductNum() {
        return productNum;
    }

    public void setProductNum(String productNum) {
        this.productNum = productNum;
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
        return "TrafficPlanPo [id=" + id + ", trafficGroupId=" + trafficGroupId + ", name=" + name
               + ", value=" + value + ", cost=" + cost + ", retailPrice=" + retailPrice
               + ", display=" + display + ", provider=" + provider + ", pid=" + pid
               + ", apiProvider=" + apiProvider + ", integral=" + integral + ", isMaintain="
               + isMaintain + ", isDelete=" + isDelete + ", createTime=" + createTime + "]";
    }
}
