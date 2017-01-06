package com.wteam.mixin.biz.service.rehcarge.impl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.service.BaseRechargeHandleService;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.recharge.YiKuaiRecharge;

@Service("yikuaiHandleService")
public class YiKuaiHandleServiceImpl extends BaseRechargeHandleService {

    @Override
    protected void doRecharge(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo,
                              Result result) {
        YiKuaiRecharge.Response response = getRecharge().recharge(orderPo.getPhone(), trafficPlanPo.getPid(), 
            orderPo.getOrderNum(), super.isTrafficQuanGuo(trafficPlanPo) ? "0" : "1");

        result.setServiceSuccess("0000".contains(response.getCode())); // 必填
        result.setOutOrderId(response.getTaskid()); // 必填
        result.setMsg(response.getMessage());
    }

    @Override
    protected void doReCallback(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo,
                                Result result) {
        YiKuaiRecharge.Response response = getRecharge().getOrder(orderPo.getOutOrderId());
        
        result.setSuccuss("0000".contains(response.getCode())); // 必填
        YiKuaiRecharge.Response item = response.getReports().get(0);
        result.setServiceSuccess("4".equals(item.getStatus())); // 必填
        result.setProcessing("2".equals(item.getStatus())); // 必填
        result.setMsg(response.getReportcode());
    }

    @Override
    protected void doCallback(String callback, Result result) {
        YiKuaiRecharge.Response response = JSON.parseObject(callback, YiKuaiRecharge.Response.class);

        String orderNum = response.getOutTradeNo();

        result.setServiceSuccess("4".equals(response.getStatus())); // 必填
        result.setOrderNum(orderNum); // 必填
        result.setMsg(response.getReportcode());
        result.setCallbackMsg("ok");
    }
    
    protected YiKuaiRecharge getRecharge() {
        return YiKuaiRecharge.instance();
    }

}
