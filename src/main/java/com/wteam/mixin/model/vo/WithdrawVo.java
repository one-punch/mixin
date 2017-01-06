package com.wteam.mixin.model.vo;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.wteam.mixin.define.IValueObject;

/**
 * <p>Title:提现申请实体</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月13日
 */
public class WithdrawVo implements IValueObject{

	/**主键ID*/
	private Long id;
	/**商家ID*/
	private Long businessId;
	/**支付宝账号（alipayAccount）*/
	@NotNull
	private String alipayAccount;
	/**金额（realIncome）*/
	@NotNull
	private BigDecimal realIncome;
	/**备注（info）*/
	private String info;
	/**提现状态（state）*/
	private Integer state;
	/**失败信息*/
	private String failInfo;
	/**是否已被删除*/
	private boolean isDelete;
	/**创建时间*/
	private Date createTime;
	public WithdrawVo() {	}
	public WithdrawVo(Long businessId, String alipayAccount, BigDecimal realIncome, String info) {
		super();
		this.businessId = businessId;
		this.alipayAccount = alipayAccount;
		this.realIncome = realIncome;
		this.info = info;
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
	public String getAlipayAccount() {
		return alipayAccount;
	}
	public void setAlipayAccount(String alipayAccount) {
		this.alipayAccount = alipayAccount;
	}
	public BigDecimal getRealIncome() {
		return realIncome;
	}
	public void setRealIncome(BigDecimal realIncome) {
		this.realIncome = realIncome;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getFailInfo() {
		return failInfo;
	}
	public void setFailInfo(String failInfo) {
		this.failInfo = failInfo;
	}
	public boolean getIsDelete() {
		return isDelete;
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
	public String toString() {
		return "WithdrawVo [id=" + id + ", businessId=" + businessId + ", alipayAccount=" + alipayAccount
				+ ", realIncome=" + realIncome + ", info=" + info + ", state=" + state + ", failInfo=" + failInfo
				+ ", isDelete=" + isDelete + ", createTime=" + createTime + "]";
	}
}
