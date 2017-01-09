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

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.service.BaseRechargeHandleService;
import com.wteam.mixin.biz.service.impl.RechargeServiceImpl;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.recharge.DaZhongRecharger;

@Service("dazhongRechargerService")
public class DaZhongRechargerServiceImpl extends BaseRechargeHandleService{
	private static Logger LOG = LogManager.getLogger(RechargeServiceImpl.class.getName());

	static final List<String> successList = Arrays.asList("1", "2", "3", "4", "6");
	private boolean isSuccess(DaZhongRecharger.Response response){
		return "success".equalsIgnoreCase(response.getAck()) && (successList.contains(response.getShippingStatus()));
	}

	private boolean isOrderSuccess(DaZhongRecharger.Response response){
		return "success".equalsIgnoreCase(response.getAck()) && response.getOrder()!=null &&
				(successList.contains(response.getOrder().getShippingStatus()));
	}

	private String getMessage(DaZhongRecharger.Response response){
		return (response.getShippingStatusMessage() == null || "".equals(response.getShippingStatusMessage().trim())) ? response.getMessage() : response.getShippingStatusMessage() ;
	}

	@Override
	protected void doRecharge(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo, Result result) {
		Optional<String> optional = getRecharger().recharge(orderPo.getPhone(), trafficPlanPo.getPid());
		optional.ifPresent(resStr -> {
			DaZhongRecharger.Response response = JSON.parseObject(resStr, DaZhongRecharger.Response.class);
			LOG.debug("{}:{}", resStr, isSuccess(response));
			result.setServiceSuccess(isSuccess(response));
			result.setOutOrderId(response.getOrderNumber());
			result.setMsg(response.getShippingStatusMessage());
		});
	}

	@Override
	protected void doReCallback(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo, Result result) {
		Optional<String> optional = getRecharger().instance().orderQuery(orderPo.getOrderNum());
		optional.ifPresent(resStr -> {
			DaZhongRecharger.Response response = JSON.parseObject(resStr, DaZhongRecharger.Response.class);
			LOG.debug("{}:orderid:{}, {}", resStr, orderPo.getId(), isOrderSuccess(response));
			if(response.getOrder()!=null){
				result.setServiceSuccess(isOrderSuccess(response));
				result.setMsg(getMessage(response));
			}else{
				result.setMsg(getMessage(response));
			}
		});
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
