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
 * 订单实体类
 * @author vee
 *
 */
@Entity
@Table(name="customer_order")
public class CustomerOrderPo implements Serializable, IPersistentObject {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**订单id*/
	private Long id;
	
	/**订单号*/
	private String orderNum;
	
    /**外部订单ID*/
    private String outOrderId;

    /**接口充值外部订单ID*/
    private String businessOutOrderNum;
    
    /**微信订单ID*/
    private String wechatOrderId;

    /**支付宝订单ID*/
    private String alipayOrderId;
    /**支付宝订单ID*/
    private String alipayInfo;
	
	/**顾客Id*/
	private Long customerId;
	
	/**商家Id*/
	private Long businessId;
	
	/**产品Id*/
	private Long productId;
	
    /**真实产品Id*/
    private Long realProductId;
	
	/**产品类型*/
	private String productType;
	
	/**顾客手机号码*/
	private String phone;
	
    /**数量*/
    private Integer num;
	
	/**零售价*/
	private BigDecimal retailPrice;
	
	/**成本价*/
	private BigDecimal cost;
	
	/**手续费*/
	private BigDecimal factorage;
	
	/**订单实际收入：零售价-手续费*/
	private BigDecimal realIncome;
	
	/**净利润：零售价-成本价-手续费*/
	private BigDecimal profits;
	
	/**订单状态*/
	private Integer state;
	
    /**收单状态*/
    private Integer shouDanState;
	
	/**备注:如果支付方式为微信支付，保存微信订单相关的信息*/
	private String info;
	
    /**号段: 省-城市-运营商*/
    private String haoduan;

	/**支付方式*/
	private Integer paymentMethod;
	
	/**失败信息*/
	private String failedInfo;

    /**是否收到接口方的回调*/
    private boolean isCallback;
    
    /**是否给商家的回调*/
    private boolean isBusinessCallback;
    
	/**是否被删除*/
	private boolean isDelete;
	
	/**创建时间*/
	private Date createTime;
	
	
	public CustomerOrderPo(String orderNum, String productType, String phone, BigDecimal cost,
                           Integer state, boolean isDelete) {
        this.orderNum = orderNum;
        this.productType = productType;
        this.phone = phone;
        this.cost = cost;
        this.state = state;
        this.isDelete = isDelete;
    }
    public CustomerOrderPo(String orderNum, Long customerId, Long businessId, Long productId, String phone,
			BigDecimal retailPrice, BigDecimal cost, BigDecimal factorage, BigDecimal realIncome, BigDecimal profits, Integer state) {
		super();
		this.orderNum = orderNum;
		this.customerId = customerId;
		this.businessId = businessId;
		this.productId = productId;
		this.phone = phone;
		this.retailPrice = retailPrice;
		this.cost = cost;
		this.factorage = factorage;
		this.realIncome = realIncome;
		this.profits = profits;
		this.state = state;
	}
	public CustomerOrderPo() {	}
	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	@Column(nullable = false)
	public String getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}
	@Column(nullable = false)
	public Long getCustomerId() {
		return customerId;
	}
	public String getOutOrderId() {
        return outOrderId;
    }
    public void setOutOrderId(String outOrderId) {
        this.outOrderId = outOrderId;
    }
    public String getBusinessOutOrderNum() {
        return businessOutOrderNum;
    }
    public void setBusinessOutOrderNum(String businessOutOrderNum) {
        this.businessOutOrderNum = businessOutOrderNum;
    }
    public String getWechatOrderId() {
        return wechatOrderId;
    }
    public void setWechatOrderId(String wechatOrderId) {
        this.wechatOrderId = wechatOrderId;
    }
    public String getAlipayOrderId() {
        return alipayOrderId;
    }
    public void setAlipayOrderId(String alipayOrderId) {
        this.alipayOrderId = alipayOrderId;
    }
    @Type(type = "text")
    public String getAlipayInfo() {
        return alipayInfo;
    }
    public void setAlipayInfo(String alipayInfo) {
        this.alipayInfo = alipayInfo;
    }
    public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}
	@Column(nullable = false)
	public Long getBusinessId() {
		return businessId;
	}
	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	@Column(nullable = false)
	public Long getProductId() {
		return productId;
	}
	public void setProductId(Long productId) {
		this.productId = productId;
	}
	public String getProductType() {
		return productType;
	}
	public Long getRealProductId() {
        return realProductId;
    }
    public void setRealProductId(Long realProductId) {
        this.realProductId = realProductId;
    }
    public void setProductType(String productType) {
		this.productType = productType;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
    public Integer getNum() {
        return num;
    }
    public void setNum(Integer num) {
        this.num = num;
    }
    @Column(precision = 23, scale = 6)
	public BigDecimal getRetailPrice() {
        return retailPrice;
    }
    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }
    @Column(precision = 23, scale = 6)
    public BigDecimal getCost() {
        return cost;
    }
    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }
    @Column(precision = 23, scale = 6)
    public BigDecimal getFactorage() {
        return factorage;
    }
    public void setFactorage(BigDecimal factorage) {
        this.factorage = factorage;
    }
    @Column(precision = 23, scale = 6)
    public BigDecimal getRealIncome() {
        return realIncome;
    }
    public void setRealIncome(BigDecimal realIncome) {
        this.realIncome = realIncome;
    }
    @Column(precision = 23, scale = 6)
    public BigDecimal getProfits() {
        return profits;
    }
    public void setProfits(BigDecimal profits) {
        this.profits = profits;
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
    @Type(type = "text")
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getHaoduan() {
        return haoduan;
    }
    public void setHaoduan(String haoduan) {
        this.haoduan = haoduan;
    }
    public Integer getPaymentMethod() {
		return paymentMethod;
	}
	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}
    @Type(type = "text")
	public String getFailedInfo() {
		return failedInfo;
	}
	public void setFailedInfo(String failedInfo) {
		this.failedInfo = failedInfo;
	}
    @Type(type = "boolean")
	public boolean getIsCallback() {
        return isCallback;
    }
    public void setIsCallback(boolean isCallback) {
        this.isCallback = isCallback;
    }
    @Type(type = "boolean")
    public boolean getIsBusinessCallback() {
        return isBusinessCallback;
    }
    public void setIsBusinessCallback(boolean isBusinessCallback) {
        this.isBusinessCallback = isBusinessCallback;
    }
    @Column(name = "isDelete")
    @Type(type = "boolean")
	public boolean getIsDelete() {
		return isDelete;
	}
	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	@Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createTime", length = 19)
    @CreationTimestamp
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
    @Override
    public String toString() {
        return "CustomerOrderPo [id=" + id + ", orderNum=" + orderNum + ", outOrderId="
               + outOrderId + ", businessOutOrderNum=" + businessOutOrderNum + ", wechatOrderId="
               + wechatOrderId + ", alipayOrderId=" + alipayOrderId + ", alipayInfo=" + alipayInfo
               + ", customerId=" + customerId + ", businessId=" + businessId + ", productId="
               + productId + ", realProductId=" + realProductId + ", productType=" + productType
               + ", phone=" + phone + ", num=" + num + ", retailPrice=" + retailPrice + ", cost="
               + cost + ", factorage=" + factorage + ", realIncome=" + realIncome + ", profits="
               + profits + ", state=" + state + ", shouDanState=" + shouDanState + ", info=" + info
               + ", paymentMethod=" + paymentMethod + ", failedInfo=" + failedInfo
               + ", isCallback=" + isCallback + ", isBusinessCallback=" + isBusinessCallback
               + ", isDelete=" + isDelete + ", createTime=" + createTime + "]";
    }


}
