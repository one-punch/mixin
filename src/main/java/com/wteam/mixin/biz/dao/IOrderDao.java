package com.wteam.mixin.biz.dao;

import java.util.List;
import java.util.Map;

import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.OrderSettlementPo;
import com.wteam.mixin.model.query.CustomerOrderQuery;
import com.wteam.mixin.model.query.IQuery;
import com.wteam.mixin.pagination.Pagination;

/**
 * 订单dao模块
 * @author benko
 * @time 2016-08-14
 * @see IUserDao
 * @since
 */
public interface IOrderDao {
    /**
     * 按条件分页查询订单
     * 注意：四个查询条件其中有一项没有数据或者都没有数据就把所有的都查询过来
     * @param orderPo 查询使用的po
     * @param pageSize 每页的容量
     * @return
     */
    Pagination listBySuper(CustomerOrderQuery query, Integer pageNo, Integer pageSize);

    /**
     * 按条件分页查询订单
     * 注意：四个查询条件其中有一项没有数据或者都没有数据就把所有的都查询过来
     * @param orderPo 查询使用的po
     * @param pageSize 每页的容量
     * @return
     */
    Pagination list(CustomerOrderQuery query,IQuery trafficplanQuery, Integer pageNo, Integer pageSize);

    /**
     * 按条件查询订单
     * 注意：四个查询条件其中有一项没有数据或者都没有数据就把所有的都查询过来
     * @param query 查询使用的po
     * @return
     */
    List<CustomerOrderPo> list(CustomerOrderQuery query, IQuery trafficplanQuery);
    /**
     * 按条件查询订单
     * 注意：四个查询条件其中有一项没有数据或者都没有数据就把所有的都查询过来
     * @param query 查询使用的po
     * @return
     */
    List<CustomerOrderPo> listBySuper(CustomerOrderQuery query);

    /**
     * 按条件查询订单记录
     * @param queryPo
     * @return
     */
    List<OrderSettlementPo> findOrderSettlementList(OrderSettlementPo queryPo);

    /**
     * 统计信息
     * totalcost : 订单成本汇总
     * @param query 查询条件
     * @return
     */
    Map<String, Object> statistics(CustomerOrderQuery query);
}
