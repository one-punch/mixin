package com.wteam.mixin.model.vo;

import com.alibaba.fastjson.annotation.JSONField;
import com.wteam.mixin.define.IValueObject;

/**
 * <p>Title:动态配置vo</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月15日
 */
public class DConfigVo implements IValueObject{
	
    @JSONField(name = "CustomerOrderRate")
	private String CustomerOrderRate;

    @JSONField(name = "WithdrawMinPrice")
	private String WithdrawMinPrice;

    @JSONField(name = "SettlemetMinPrice")
    private String SettlemetMinPrice;

	public DConfigVo() {	}

	public DConfigVo(String customerOrderRate, String withdrawMinPrice) {
		CustomerOrderRate = customerOrderRate;
		WithdrawMinPrice = withdrawMinPrice;
	}

	public DConfigVo(String customerOrderRate, String withdrawMinPrice, String settlemetMinPrice) {
        CustomerOrderRate = customerOrderRate;
        WithdrawMinPrice = withdrawMinPrice;
        SettlemetMinPrice = settlemetMinPrice;
    }

    public String getCustomerOrderRate() {
		return CustomerOrderRate;
	}

	public void setCustomerOrderRate(String customerOrderRate) {
		CustomerOrderRate = customerOrderRate;
	}

	public String getWithdrawMinPrice() {
		return WithdrawMinPrice;
	}

	public void setWithdrawMinPrice(String withdrawMinPrice) {
		WithdrawMinPrice = withdrawMinPrice;
	}

	public String getSettlemetMinPrice() {
        return SettlemetMinPrice;
    }

    public void setSettlemetMinPrice(String settlemetMinPrice) {
        SettlemetMinPrice = settlemetMinPrice;
    }

    @Override
	public String toString() {
		return "DConfigVo [CustomerOrderRate=" + CustomerOrderRate + ", WithdrawMinPrice=" + WithdrawMinPrice + "]";
	}
}
