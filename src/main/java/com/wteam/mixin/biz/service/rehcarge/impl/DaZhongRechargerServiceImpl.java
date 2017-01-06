/*
 * 文件名：DaZhongRechargerServiceImpl.java
 * 版权：Copyright by wteam团队
 * 描述：
 * 修改人：benko
 * 修改时间：Jan 6, 2017 4:14:29 PM
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.wteam.mixin.biz.service.rehcarge.impl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.service.BaseRechargeHandleService;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.recharge.DaZhongRecharger;

@Service("dazhongRechargerService")
public class DaZhongRechargerServiceImpl extends BaseRechargeHandleService{

	@Override
	protected void doRecharge(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo, Result result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doReCallback(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo, Result result) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void doCallback(String callback, Result result) {
		DaZhongRecharger.Response response = JSON.parseObject(callback, DaZhongRecharger.Response.class);
		response.getShippingStatus();
		
		result.setServiceSuccess(response.getShippingStatus().equals("4"));
		result.setOrderNum(response.getOrderNumber());
		result.setMsg(response.getShippingStatusMessage());
		result.setCallbackMsg("success");
	}

	protected DaZhongRecharger getRecharger(){
		return DaZhongRecharger.instance();
	}

}
