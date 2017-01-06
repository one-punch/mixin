package com.wteam.mixin.biz.service.rehcarge.impl;


import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.service.BaseRechargeHandleService;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.recharge.YouXingRecharge;

/**
 * 友信流量接口充值
 * @author benko
 *
 */
@Service("youXingHandleService")
public class YouXingHandleServiceImpl extends BaseRechargeHandleService {

    @Override
    protected void doRecharge(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo,
                              Result result) {
        YouXingRecharge.Response response = getRecharge()
            .recharge(orderPo.getPhone(), trafficPlanPo.getPid(), orderPo.getOrderNum());

        result.setServiceSuccess("0".equals(response.getError())); // 必填
        result.setOutOrderId(response.getP_order_sn()); // 必填
        result.setMsg(response.getMsg());
    }

    @Override
    protected void doReCallback(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo,
                                Result result) {
        YouXingRecharge.Response response = getRecharge().getOrder(orderPo.getOutOrderId());
        result.setSuccuss("0".contains(response.getError())); // 必填
        result.setServiceSuccess("3".equals(response.getOrder_status())); // 必填
        result.setProcessing("1,2".contains(response.getOrder_status())); // 必填
        result.setMsg(response.getInfo());
    }

    @Override
    protected void doCallback(String callback, Result result) {
        YouXingRecharge.Response response = JSON.parseObject(callback, YouXingRecharge.Response.class);

        String orderNum = response.getOrder_sn();

        result.setServiceSuccess("3".equals(response.getOrder_status())); // 必填
        result.setOrderNum(orderNum); // 必填
        result.setMsg(response.getInfo());
        result.setCallbackMsg("success");
    }

    protected YouXingRecharge getRecharge() {
        return YouXingRecharge.instance();
    }

}
