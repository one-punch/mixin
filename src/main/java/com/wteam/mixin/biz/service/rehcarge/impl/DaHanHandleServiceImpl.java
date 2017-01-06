package com.wteam.mixin.biz.service.rehcarge.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.service.BaseRechargeHandleService;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.recharge.DaHanRecharge;
import com.wteam.mixin.recharge.DaHanRecharge.Response;

/**
 * 大汉
 * @author benko
 *
 */
@Service("daHanHandleService")
public class DaHanHandleServiceImpl extends BaseRechargeHandleService {

    @Override
    protected void doRecharge(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo,
                              Result result) {
        Integer value = Integer.valueOf(trafficPlanPo.getValue());
        if (value > 1000)  value = value /1024 * 1000;
        //
        Optional<DaHanRecharge.Response> optional = DaHanRecharge.instance().recharge(orderPo.getOrderNum(), orderPo.getPhone(), value.toString());
        optional.ifPresent(response -> {
            result.setServiceSuccess("00".equals(response.getResultCode())); // 必填
            result.setMsg(response.getResultMsg());
        });
    }

    @Override
    protected void doReCallback(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo,
                                Result result) {
        // TODO Auto-generated method stub
        Optional<List<Response>> optional = DaHanRecharge.instance().getOrder(orderPo.getOrderNum());

        optional.ifPresent(list -> {
            result.setSuccuss(true);
            if (!list.isEmpty()) {
                Response response = list.get(0);
                if (response.getResultCode() == null) {
                    result.setMsg(response.getErrorDesc() );
                    result.setServiceSuccess("0".equals(response.getStatus()));
                }
                else {
                    result.setMsg(response.getResultMsg());
                }
            } else {
                result.setProcessing(true);
            }
        });
    }

    @Override
    protected void doCallback(String callback, Result result) {
        DaHanRecharge.Response response = JSON.parseObject(callback, DaHanRecharge.Response.class);

        String orderNum = response.getClientOrderId();

        result.setServiceSuccess("0".equals(response.getStatus())); // 必填
        result.setOrderNum(orderNum); // 必填
        result.setMsg(response.getErrorDesc());
        result.setCallbackMsg("{\"resultCode\":\"0000\",\"resultMsg\":\"处理成功！ \"}");
    }

}
