package com.wteam.mixin.biz.service.rehcarge.impl;

import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.service.BaseRechargeHandleService;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficPlanPo;

/**
 * 新号吧
 * @author benko
 *
 */
@Service("xinHaoBaHandleService")
public class XinHaoBaHandleServiceImpl extends BaseRechargeHandleService {

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
        // TODO Auto-generated method stub

    }

}
