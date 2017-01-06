package com.wteam.mixin.biz.service.rehcarge.impl;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.service.BaseRechargeHandleService;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.recharge.KuaiChongRecharge;

/**
 * 快充
 * @author benko
 *
 */
@Service("kuaiChongHandleService")
public class KuaiChongHandleServiceImpl extends BaseRechargeHandleService {

    @Override
    protected void doRecharge(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo,
                              Result result) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void doReCallback(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo,
                                Result result) {
        // TODO Auto-generated method stub

    }

    @Override
    protected void doCallback(String callback, Result result) {
        KuaiChongRecharge.Response json = JSON.parseObject(callback, KuaiChongRecharge.Response.class);
        CustomerOrderVo orderVo = super.getOrder(json.getTaskid());
        String orderNum = orderVo == null ? null : orderVo.getOrderNum();

        result.setServiceSuccess("0".equals(json.getResult())); // 必填
        result.setMsg(json.getResultdesc());
        result.setOrderNum(orderNum); // 必填
        result.setCallbackMsg("{\"message\":\"ok\",\"status\":\"ok\",\"code\":\"0\"}");
    }

}
