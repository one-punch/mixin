package com.wteam.mixin.model.vo;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.wteam.mixin.constant.State;
import com.wteam.mixin.define.IValueObject;

public class BusinessBalanceRecordVo implements IValueObject{

	/**ID*/
    private Long id;
    
    /**所属商家ID*/
    @NotNull
    private Long businessId;
    
    /**增减金额,正为进,负为出*/
    @NotNull
    private BigDecimal money;
    
    /**商家之前的余额*/
    private BigDecimal businessBeforeMoney;
    
    /**该项记录的来源:
		0：账户充值
		1：已结算转入
		2：订单成本转出
		3：提现
		4：增值业务
     */
    private Integer source;
    
    private String sourceName;
    
    /**来源ID*/
    private Long sourceId;

    /**备注*/
    private String info;

    /**电话*/
    private String tel;

    /** 是否删除 */
    private Boolean isDelete;
    
    /** 创建时间 */
    private Date createTime;

	public BusinessBalanceRecordVo() {	}

	public BusinessBalanceRecordVo(Long id, Long businessId, BigDecimal money, Integer source, Long sourceId,
			boolean isDelete, Date createTime) {
		this.id = id;
		this.businessId = businessId;
		this.money = money;
        setSource(source);
		this.sourceId = sourceId;
	}
	
	

	public BusinessBalanceRecordVo(Long businessId, BigDecimal money,
                                   BigDecimal businessBeforeMoney, Integer source, Long sourceId,
                                   String tel, Date createTime) {
        this.businessId = businessId;
        this.money = money;
        this.businessBeforeMoney = businessBeforeMoney;
        setSource(source);
        this.sourceId = sourceId;
        this.tel = tel;
        this.createTime = createTime;
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

	public BigDecimal getMoney() {
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

	public BigDecimal getBusinessBeforeMoney() {
        return businessBeforeMoney;
    }

    public void setBusinessBeforeMoney(BigDecimal businessBeforeMoney) {
        this.businessBeforeMoney = businessBeforeMoney;
    }

    public Integer getSource() {
		return source;
	}

	public void setSource(Integer source) {
		this.source = source;
		this.sourceName = State.BBRecordSource.names[source];
	}
	
	public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public Long getSourceId() {
		return sourceId;
	}

	public void setSourceId(Long sourceId) {
		this.sourceId = sourceId;
	}

	public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
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

    @Override
    public String toString() {
        return "BusinessBalanceRecordVo [id=" + id + ", businessId=" + businessId + ", money="
               + money + ", businessBeforeMoney=" + businessBeforeMoney + ", source=" + source
               + ", sourceName=" + sourceName + ", sourceId=" + sourceId + ", info=" + info
               + ", tel=" + tel + ", isDelete=" + isDelete + ", createTime=" + createTime + "]";
    }


}
