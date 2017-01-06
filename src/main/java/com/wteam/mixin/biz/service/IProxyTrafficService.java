package com.wteam.mixin.biz.service;

import java.util.List;

import com.wteam.mixin.model.query.TrafficGroupQuery;
import com.wteam.mixin.model.query.TrafficPlanQuery;
import com.wteam.mixin.model.vo.GroupNPlanVo;
import com.wteam.mixin.model.vo.ProxyBusinessTrafficPlanVo;
import com.wteam.mixin.pagination.Pagination;

/**
 * 代理流量模块业务接口
 * @version 1.0
 * @author benko
 * @time 2016-11-04 17:06:07
 */
public interface IProxyTrafficService {

    /**
     * 获取流量套餐
     * @param planQuery
     * @param groupQuery
     * @param businessId
     * @param currentPage
     * @param pageSize
     * @return
     */
    Pagination listTrafficPlan(TrafficPlanQuery planQuery, TrafficGroupQuery groupQuery, Long businessId, Integer currentPage, Integer pageSize);

    /**
     * 代理 商家查询分组和相应的流量套餐情况
     * @param planQuery
     * @param groupQuery
     * @param businessId
     * @return
     */
    List<GroupNPlanVo> listGroupNPlan(TrafficPlanQuery planQuery, TrafficGroupQuery groupQuery, Long businessId);
    /**
     * 查询给顾客看代理 商家查询分组和相应的流量套餐情况
     * @param planQuery
     * @param groupQuery
     * @param businessId
     * @return
     */
    List<GroupNPlanVo> listGroupNPlanByCustomer(TrafficPlanQuery planQuery, TrafficGroupQuery groupQuery, Long businessId);

    ProxyBusinessTrafficPlanVo add(ProxyBusinessTrafficPlanVo proxyPlanVo);
    /**
     * 必须要有businessId,trafficplanId
     * @param proxyPlanVo
     * @return
     */
    ProxyBusinessTrafficPlanVo edit(ProxyBusinessTrafficPlanVo proxyPlanVo);
    /**
     * 必须要有businessId,trafficplanId
     * @param proxyPlanVo
     */
    void delete(ProxyBusinessTrafficPlanVo proxyPlanVo);
}
