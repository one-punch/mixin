package com.wteam.mixin.biz.service.rehcarge.impl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.service.BaseRechargeHandleService;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.recharge.ShangTongRecharge;

/**
 * 尚通流量接口充值
 * @author benko
 *
 */
@Service("shangTongHandleService")
public class ShangTongHandleServiceImpl extends BaseRechargeHandleService {

    @Override
    protected void doRecharge(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo,
                              Result result) {
        ShangTongRecharge.Response response = ShangTongRecharge.instance().recharge(orderPo.getOrderNum(),
            orderPo.getPhone(), trafficPlanPo.getValue(), super.isTrafficQuanGuo(trafficPlanPo) ? "0" : "1");

        result.setServiceSuccess("0000".equals(response.getRespCode())); // 必填
        result.setOutOrderId(response.getOrderID()); // 必填
        result.setMsg(response.getRespMsg());
    }

    @Override
    protected void doReCallback(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo,
                                Result result) {
        ShangTongRecharge.Response response = ShangTongRecharge.instance().getOrder(orderPo.getOutOrderId());
        result.setSuccuss("0002,0001".contains(response.getRespCode())); // 必填
        result.setServiceSuccess("0002".equals(response.getRespCode())); // 必填
        result.setProcessing("0001".equals(response.getRespCode())); // 必填
        result.setMsg(response.getRespMsg());
    }

    @Override
    protected void doCallback(String callback, Result result) {
        ShangTongRecharge.Response response = JSON.parseObject(callback, ShangTongRecharge.Response.class);

        String orderNum = response.getOrderno_ID();

        result.setServiceSuccess("0002".equals(response.getRespCode())); // 必填
        result.setOrderNum(orderNum); // 必填
        result.setMsg(response.getRespMsg());
        result.setCallbackMsg("Success");
    }

}
