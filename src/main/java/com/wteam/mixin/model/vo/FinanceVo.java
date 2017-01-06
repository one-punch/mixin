package com.wteam.mixin.model.vo;

import java.math.BigDecimal;

/**
 * <p>Title:财务数据实体</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月13日
 */
public class FinanceVo {
	
//	 "balance":"",
//     "unSettlement":"",
//     "settlement":"",
//     "orderIncome":"",
//     "orderCost":"",
//     "profits":"",
	
	private BigDecimal balance;
	
	private BigDecimal unSettlement;
	
	private BigDecimal settlement;
	
	private BigDecimal orderIncome;
	
	private BigDecimal orderCost;
	
	private BigDecimal profits;

	public FinanceVo() {	}

	public FinanceVo(BigDecimal balance, BigDecimal unSettlement, BigDecimal settlement, BigDecimal orderIncome, BigDecimal orderCost,
			BigDecimal profits) {
		this.balance = balance;
		this.unSettlement = unSettlement;
		this.settlement = settlement;
		this.orderIncome = orderIncome;
		this.orderCost = orderCost;
		this.profits = profits;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getUnSettlement() {
		return unSettlement;
	}

	public void setUnSettlement(BigDecimal unSettlement) {
		this.unSettlement = unSettlement;
	}

	public BigDecimal getSettlement() {
		return settlement;
	}

	public void setSettlement(BigDecimal settlement) {
		this.settlement = settlement;
	}

	public BigDecimal getOrderIncome() {
		return orderIncome;
	}

	public void setOrderIncome(BigDecimal orderIncome) {
		this.orderIncome = orderIncome;
	}

	public BigDecimal getOrderCost() {
		return orderCost;
	}

	public void setOrderCost(BigDecimal orderCost) {
		this.orderCost = orderCost;
	}

	public BigDecimal getProfits() {
		return profits;
	}

	public void setProfits(BigDecimal profits) {
		this.profits = profits;
	}

	@Override
	public String toString() {
		return "PlatformFinanceVo [balance=" + balance + ", unSettlement=" + unSettlement + ", settlement=" + settlement
				+ ", orderIncome=" + orderIncome + ", orderCost=" + orderCost + ", profits=" + profits + "]";
	}
	
}
