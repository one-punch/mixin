package com.wteam.mixin.biz.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficPlanPo;

public interface IRechargeHandleService {
    
    /**
     * 充值处理
     * @param orderPo 
     * @param trafficPlanPo
     */
    void recharge(CustomerOrderPo orderPo , TrafficPlanPo trafficPlanPo );
    

    /**
     * 向商家发回调
     * @param orderPo 
     * @param trafficPlanPo
     */
    void reCallback(CustomerOrderPo orderPo , TrafficPlanPo trafficPlanPo );
    

    /**
     * 处理接口商的回调
     * 
     * @param request
     * @param response
     */
    void callback(HttpServletRequest request, HttpServletResponse response);
}
