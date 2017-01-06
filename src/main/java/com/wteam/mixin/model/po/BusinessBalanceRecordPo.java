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

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.wteam.mixin.define.IPersistentObject;


/**
 * 商家账户余额记录
 * @author vee
 */
@Entity
@Table(name = "business_balance_record")
public class BusinessBalanceRecordPo implements java.io.Serializable, IPersistentObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**ID*/
    private Long id;
    
    /**所属商家ID*/
    private Long businessId;
    
    /**增减金额,正为进,负为出*/
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
    
    /**来源ID*/
    private Long sourceId;

    /**备注*/
    private String info;
    
    /**订单电话*/
    private String tel;

    /** 是否删除 */
    private Boolean isDelete = false;
    
    /** 创建时间 */
    private Date createTime;

    public BusinessBalanceRecordPo() {	}
    
	public BusinessBalanceRecordPo(Long businessId, BigDecimal money, Integer source) {
		this.businessId = businessId;
		this.money = money;
		this.source = source;
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
    @Column(nullable = false)
    public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	@Column(precision=23, scale=6)
	public BigDecimal getMoney(){
		return money;
	}

	public void setMoney(BigDecimal money) {
		this.money = money;
	}

    @Column(precision=23, scale=6)
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

    @Column(name = "isDelete")
    @Type(type = "boolean")
    public Boolean getIsDelete() {
        return this.isDelete;
    }

    public void setIsDelete(Boolean isDelete) {
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
        return "BusinessBalanceRecordPo [id=" + id + ", businessId=" + businessId + ", money="
               + money + ", businessBeforeMoney=" + businessBeforeMoney + ", source=" + source
               + ", sourceId=" + sourceId + ", info=" + info + ", tel=" + tel + ", isDelete="
               + isDelete + ", createTime=" + createTime + "]";
    }
	
	
}
