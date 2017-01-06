package com.wteam.mixin.model.vo;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.wteam.mixin.constant.State;
import com.wteam.mixin.constant.ValidatePattern;
import com.wteam.mixin.define.IValueObject;

/**
 * 顾客订单Vo
 * @version 1.0
 * @author benko
 * @time 2016-4-12 21:30:16
 */
public class CustomerOrderVo implements IValueObject {

    public static String SELECT_1(String as) {
        as = as == null ? "" : (as.equals("") ? as : as + ".");
        return "select new com.wteam.mixin.model.vo.CustomerOrderVo(this.id,this.orderNum,this.customerId,this.businessId,this.productId,this.realProductId,this.productType,this.phone,this.num,this.retailPrice,this.cost,this.factorage,this.realIncome,this.profits,this.state,this.info,this.paymentMethod,this.failedInfo,this.isDelete,this.createTime,this.isBusinessCallback)"
            .replaceAll("this.", as);
    }

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

    /**支付订单ID*/
    private String alipayOrderId;

    /**支付宝订单ID*/
    private String alipayInfo;

    /**顾客Id*/
    private Long customerId;

    /**商家Id*/
    @NotNull
    private Long businessId;

    /**产品Id*/
    @NotNull
    private Long productId;

    /**真实产品Id*/
    private Long realProductId;

    /**产品类*/
    @NotNull
    private String productType;

    /**顾客手机号码*/
    @NotNull
    @Pattern(regexp = ValidatePattern.TEL)
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
    /**订单状态名*/
    private String stateName;
    /**收单状态*/
    private Integer shouDanState;

    /**备注*/
    private String info;

    /**号段: 省-城市-运营商*/
    private String haoduan;

    /**支付方式*/
    private Integer paymentMethod;

    /**失败信息*/
    private String failedInfo;

    /**是否收到接口方的回调*/
    private Boolean isCallback;

    /**是否给商家的回调*/
    private Boolean isBusinessCallback;

    /**是否被删除*/
    private Boolean isDelete;

    /**创建时间*/
    private Date createTime;

    //----------------------

    /**顾客微信头像链接*/
    private String wechatHead;

    /**顾客微信名*/
    private String wechatName;
    /**顾客账号*/
    private String account;

    //------------------------
    /**产品名*/
    private String productName;
    /**真实产品名*/
    private String realProductName;

    private String trafficplanValue;




    public CustomerOrderVo() {
    }





    /** SELECT_1*/
    public CustomerOrderVo(Long id, String orderNum, Long customerId, Long businessId,
                           Long productId, Long realProductId,String productType, String phone, Integer num,
                           BigDecimal retailPrice, BigDecimal cost, BigDecimal factorage,
                           BigDecimal realIncome, BigDecimal profits, Integer state, String info,
                           Integer paymentMethod, String failedInfo, Boolean isDelete,
                           Date createTime, Boolean isBusinessCallback) {
        this.id = id;
        this.orderNum = orderNum;
        this.customerId = customerId;
        this.businessId = businessId;
        this.productId = productId;
        this.realProductId = realProductId;
        this.productType = productType;
        this.phone = phone;
        this.num = num;
        this.retailPrice = retailPrice;
        this.cost = cost;
        this.factorage = factorage;
        this.realIncome = realIncome;
        this.profits = profits;
        this.state = state;
        this.info = info;
        this.paymentMethod = paymentMethod;
        this.failedInfo = failedInfo;
        this.isDelete = isDelete;
        this.createTime = createTime;
        this.isBusinessCallback = isBusinessCallback;
    }





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

    public String getAlipayInfo() {
        return alipayInfo;
    }

    public void setAlipayInfo(String alipayInfo) {
        this.alipayInfo = alipayInfo;
    }

    public Integer getNum() {
        return num;
    }

    public void setNum(Integer num) {
        this.num = num;
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

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getRealProductId() {
        return realProductId;
    }
    public void setRealProductId(Long realProductId) {
        this.realProductId = realProductId;
    }

    public String getProductType() {
        return productType;
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

    public BigDecimal getRetailPrice() {
        return retailPrice;
    }

    public void setRetailPrice(BigDecimal retailPrice) {
        this.retailPrice = retailPrice;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public BigDecimal getFactorage() {
        return factorage;
    }

    public void setFactorage(BigDecimal factorage) {
        this.factorage = factorage;
    }

    public BigDecimal getRealIncome() {
        return realIncome;
    }

    public void setRealIncome(BigDecimal realIncome) {
        this.realIncome = realIncome;
    }

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
        this.stateName = State.CustomerOrder.names[state];
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public Integer getShouDanState() {
        return shouDanState;
    }

    public void setShouDanState(Integer shouDanState) {
        this.shouDanState = shouDanState;
    }

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

    public String getFailedInfo() {
        return failedInfo;
    }

    public void setFailedInfo(String failedInfo) {
        this.failedInfo = failedInfo;
    }

    public Boolean getIsCallback() {
        return isCallback;
    }
    public void setIsCallback(Boolean isCallback) {
        this.isCallback = isCallback;
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

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getWechatHead() {
        return wechatHead;
    }

    public void setWechatHead(String wechatHead) {
        this.wechatHead = wechatHead;
    }

    public String getWechatName() {
        return wechatName;
    }

    public void setWechatName(String wechatName) {
        this.wechatName = wechatName;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getRealProductName() {
        return realProductName;
    }

    public void setRealProductName(String realProductName) {
        this.realProductName = realProductName;
    }

    public String getTrafficplanValue() {
        return trafficplanValue;
    }

    public void setTrafficplanValue(String trafficplanValue) {
        this.trafficplanValue = trafficplanValue;
    }



    @Override
    public String toString() {
        return "CustomerOrderVo [id=" + id + ", orderNum=" + orderNum + ", outOrderId="
               + outOrderId + ", businessOutOrderNum=" + businessOutOrderNum + ", wechatOrderId="
               + wechatOrderId + ", alipayOrderId=" + alipayOrderId + ", alipayInfo=" + alipayInfo
               + ", customerId=" + customerId + ", businessId=" + businessId + ", productId="
               + productId + ", realProductId=" + realProductId + ", productType=" + productType
               + ", phone=" + phone + ", num=" + num + ", retailPrice=" + retailPrice + ", cost="
               + cost + ", factorage=" + factorage + ", realIncome=" + realIncome + ", profits="
               + profits + ", state=" + state + ", shouDanState=" + shouDanState + ", info=" + info
               + ", haoduan=" + haoduan + ", paymentMethod=" + paymentMethod + ", failedInfo="
               + failedInfo + ", isCallback=" + isCallback + ", isBusinessCallback="
               + isBusinessCallback + ", isDelete=" + isDelete + ", createTime=" + createTime
               + ", wechatHead=" + wechatHead + ", wechatName=" + wechatName + ", account="
               + account + ", productName=" + productName + ", realProductName=" + realProductName
               + ", trafficplanValue=" + trafficplanValue + "]";
    }





    public static void main(String[] args) {


        System.out.println(SELECT_1("a"));
    }
}
