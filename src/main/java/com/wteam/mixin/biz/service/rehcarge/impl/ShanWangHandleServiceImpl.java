package com.wteam.mixin.biz.service.rehcarge.impl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.service.BaseRechargeHandleService;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.recharge.ShanWangRecharge;

/**
 * 三网
 */
@Service("shanWangHandleService")
public class ShanWangHandleServiceImpl extends BaseRechargeHandleService {

    @Override
    protected void doRecharge(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo,
                              Result result) {
        // TODO Auto-generated method stub
        ShanWangRecharge.Response  response = getRecharge().recharge(orderPo.getOrderNum(), orderPo.getPhone(), trafficPlanPo.getPid());
        
        result.setServiceSuccess("1000".equals(response.getCode())); // 必填
        result.setOutOrderId(response.getData().getOrderid()); // 必填
        result.setMsg(response.getMsg());
    }

    @Override
    protected void doReCallback(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo,
                                Result result) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void doCallback(String callback, Result result) {
        ShanWangRecharge.Data response = JSON.parseObject(callback, ShanWangRecharge.Data.class);
        
        result.setServiceSuccess("3001".equals(response.getStatus())); // 必填
        result.setMsg(response.getReason());
        result.setOrderNum(response.getOutid()); // 必填
        result.setCallbackMsg("");
    }
    
    protected ShanWangRecharge getRecharge() {
        return ShanWangRecharge.instance();
    }

}
