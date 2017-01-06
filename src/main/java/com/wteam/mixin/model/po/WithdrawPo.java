package com.wteam.mixin.model.po;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.wteam.mixin.define.IPersistentObject;

/**
 * 商家提现申请表实体
 * <p>Title:</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月13日
 */
@Entity
@Table(name = "m_withdraw")
public class WithdrawPo implements java.io.Serializable,IPersistentObject{
	
	private static final long serialVersionUID = 1L;
	
	/**主键ID*/
	private Long id;
	/**商家ID*/
	private Long businessId;
	/**支付宝账号（alipayAccount）*/
	private String alipayAccount;
	/**金额（realIncome）*/
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

	public WithdrawPo() {	}

	public WithdrawPo(Long businessId, String alipayAccount, BigDecimal realIncome, String info, Integer state) {
		this.businessId = businessId;
		this.alipayAccount = alipayAccount;
		this.realIncome = realIncome;
		this.info = info;
		this.state = state;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false, unique=true)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	@Column(nullable=false)
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

	@Type(type="boolean")
	public boolean getIsDelete() {
		return isDelete;
	}

	public void setIsDelete(boolean isDelete) {
		this.isDelete = isDelete;
	}
	@Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date startTime) {
		this.createTime = startTime;
	}

	@Override
	public String toString() {
		return "WithdrawPo [id=" + id + ", businessId=" + businessId + ", alipayAccount=" + alipayAccount
				+ ", realIncome=" + realIncome + ", info=" + info + ", state=" + state + ", failInfo=" + failInfo
				+ ", isDelete=" + isDelete + ", createTime=" + createTime + "]";
	}
}









