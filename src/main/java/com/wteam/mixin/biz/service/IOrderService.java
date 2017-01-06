package com.wteam.mixin.biz.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.wteam.mixin.model.query.CustomerOrderQuery;
import com.wteam.mixin.model.query.IQuery;
import com.wteam.mixin.model.query.TrafficPlanQuery;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.model.vo.OrderSettlementVo;
import com.wteam.mixin.pagination.Pagination;

/**
 * 订单业务层接口
 * @version 1.0
 * @author benko
 * @time 2016-08-12
 */
public interface IOrderService {


    /**
     * 查找订单
     * @param orderVo
     * @return
     */
    CustomerOrderVo find(Long orderId);
    /**
     * 查询订单
     * @param orderVo
     * @return
     */
    CustomerOrderVo find(String orderNum);
    /**
     * 查询订单
     * @param orderVo
     * @return
     */
    CustomerOrderVo find(CustomerOrderQuery query);
    /**
     * 更新订单
     * @param orderVo
     * @return
     */
    CustomerOrderVo update(CustomerOrderVo orderVo, String orderNum);

    /**
     * 按条件分页查询订单
     * 注意：四个查询条件其中有一项没有数据或者都没有数据就把所有的都查询过来
     * @param query 查询条件
     * @param pageNo 目的页
     * @param pageSize 每页的容量
     * @return
     */
    Pagination listBySuper(CustomerOrderQuery query, Integer pageNo, Integer pageSize);

    /**
     * 按条件分页查询订单
     * 注意：四个查询条件其中有一项没有数据或者都没有数据就把所有的都查询过来
     * @param query 查询条件
     * @param pageNo 目的页
     * @param pageSize 每页的容量
     * @return
     */
    Pagination list(CustomerOrderQuery query, TrafficPlanQuery trafficPlanQuery, Integer pageNo, Integer pageSize);

    /**
     * 按条件分页查询订单
     * 注意：四个查询条件其中有一项没有数据或者都没有数据就把所有的都查询过来
     * @param query 查询条件
     * @param pageNo 目的页
     * @param pageSize 每页的容量
     * @return
     */
    List<CustomerOrderVo> listByQuery(CustomerOrderQuery query);
    /**
     * 按条件分页查询订单
     * 注意：四个查询条件其中有一项没有数据或者都没有数据就把所有的都查询过来
     * @param query 查询条件
     * @param pageNo 目的页
     * @param pageSize 每页的容量
     * @return
     */
    List<CustomerOrderVo> list(CustomerOrderQuery query, TrafficPlanQuery trafficPlanQuery);


    /**
     * 提交订单
     * @param orderVo
     * @return
     */
    CustomerOrderVo submitOrder(CustomerOrderVo orderVo);

    /**
     * 支付订单
     * @param orderVo
     * @return
     */
    CustomerOrderVo pay(CustomerOrderVo orderVo);



    /**
     * 微信支付回调此参数
     * @param map
     * @return
     */
    CustomerOrderVo wechatPayCallback(Map<String, String> map);


    /**
     * 修改订单的状态
     * @param orderId 订单ID
     * @param state 状态
     * @return
     */
    CustomerOrderVo chageOrderState(Long orderId, Integer state);

    /**
     * 修改订单的状态
     * @param orderNum 订单号
     * @param state 状态
     * @return
     */
    CustomerOrderVo chageOrderState(String orderNum, Integer state);

    /**
     * 保存一个订单结算记录
     * @param orderNum
     * @param businessId
     * @param realIncome
     * @return
     */
    OrderSettlementVo saveOrderSettlement(String orderNum, Long businessId, BigDecimal realIncome);

    /**
     * 根据条件获取订单结算列表
     * @param businessId 商家ID
     * @param state 订单状态
     * @return
     */
    List<OrderSettlementVo> findOrderSettlementList(Long businessId,Integer state);

    /**
     * 修改订单结算的状态
     * @param orderSettlementId
     * @return
     */
    OrderSettlementVo changeOrderSettlementState(Long orderSettlementId,Integer state);

    /**
     * 计算商家状态为已结算的订单结算
     * @param businessId 商家ID
     */
    void computeOrderSettlements(Long businessId);


}
