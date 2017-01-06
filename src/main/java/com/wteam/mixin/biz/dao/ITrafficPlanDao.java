package com.wteam.mixin.biz.dao;

import java.math.BigDecimal;
import java.util.List;

import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.query.IQuery;
import com.wteam.mixin.pagination.Pagination;

/**
 * 流量套餐DAO
 * @author benko
 *
 */
public interface ITrafficPlanDao {


    /**
     * 根据套餐和分组查询条件查找套餐
     * @param planQuery
     * @param groupQuery
     * @return
     */
    List<TrafficPlanPo> find(IQuery planQuery, IQuery groupQuery);

    /**
     * 根据套餐和分组查询条件查找套餐
     * @param planQuery
     * @param groupQuery
     * @return
     */
    Pagination find(IQuery planQuery, IQuery groupQuery,Integer currentPage, Integer pageSize);


    /**
     * 根据套餐、分组查询条件查找代理套餐
     * @param planQuery
     * @param groupQuery
     * @return
     */
    Pagination findProxy(IQuery planQuery, IQuery groupQuery, IQuery proxyQuery, Integer currentPage, Integer pageSize);


    /**
     * 根据套餐、分组查询条件查找代理套餐
     * @param planQuery
     * @param groupQuery
     * @return
     */
    List<TrafficPlanPo> findProxy(IQuery planQuery, IQuery groupQuery, IQuery proxyQuery);

    /**
     * 获取商家某个流量的成本价
     * @param businessId
     * @param trafficplanId
     * @return
     */
    BigDecimal cost(Long businessId, Long trafficplanId);

    /**
     * 获取商家某个流量的零售价
     * @param businessId
     * @param trafficplanId
     * @return
     */
    BigDecimal retailPrice(Long businessId, Long trafficplanId);

}
