package com.wteam.mixin.biz.service;

import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.model.vo.TrafficApiRechargeInfoVo;
import com.wteam.mixin.model.vo.TrafficApiRequestParams;

/**
 * 流量充值模块业务接口
 * @version 1.0
 * @author benko
 * @time 2016-08-12
 */
public interface IRechargeService {

    /**
     * 流量充值
     * @param orderNum 订单号
     * @return 修改后的订单
     */
    CustomerOrderVo recharge(String orderNum);

    /**
     * 修改授权
     * @param businessId
     */
    TrafficApiRechargeInfoVo changeApiRechargeAuthoried(Long businessId);

    /**
     * 查询商家的配置信息
     * @param orderNum 订单号
     * @return 修改后的订单
     */
    TrafficApiRechargeInfoVo apiInfo(Long businessId);

    /**
     * 修改商家的配置信息
     * @param orderNum 订单号
     * @return 修改后的订单
     */
    TrafficApiRechargeInfoVo updateApiInfo(TrafficApiRechargeInfoVo infoVo);

    /**
     * 接口充值
     */
    CustomerOrderVo apiRecharge(TrafficApiRequestParams params);

    /**
     * 订单查询
     * @param params
     * @return
     */
    CustomerOrderVo orderQuery(TrafficApiRequestParams params);

    /**
     * 回调订单
     */
    void apiCallback(CustomerOrderVo orderVo);

    /**
     * 重新回调订单
     */
    void reCallback(String orderNum);
    /**
     * 重新回调所有的订单
     */
    void reAllCallback();
    /**
     *
     */
    void getAllOutOrderId();

    //测试使用
    //TrafficPlanPo getTrafficPlanPo(CustomerOrderPo orderPo) ;
}
