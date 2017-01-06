package com.wteam.mixin.biz.dao.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Property;
import org.hibernate.transform.Transformers;

import static com.wteam.mixin.model.query.IQuery.addPrefix;
import static org.hibernate.criterion.Restrictions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.thoughtworks.xstream.alias.ClassMapper.Null;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.IOrderDao;
import com.wteam.mixin.constant.ProductType;
import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.model.po.CustomerInfoPo;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.OrderSettlementPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.query.AbstractQuery;
import com.wteam.mixin.model.query.CustomerOrderQuery;
import com.wteam.mixin.model.query.IQuery;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.pagination.Pagination;

@Repository("orderDao")
public class OrderDaoImpl implements IOrderDao{
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(OrderDaoImpl.class.getName());

    /**
     * 使用@Autowired注解将sessionFactory注入到OrderDaoImpl中
     */
    @Autowired
    private SessionFactory sessionFactory;
    
    @Autowired
    IBaseDao baseDao;
    
    /** 生成查询条件 query = toQuery.apply(plan_query, plan_query),*/
    private final BiFunction< String,String,String> toPlanOrderQuery = 
        (order_query,plan_query) -> "".equals(plan_query) ? order_query : " orderPo.productId=plan.id "+  addPrefix.apply(" and", order_query) + addPrefix.apply(" and", plan_query);
    
    @SuppressWarnings("unchecked")
    @Override
    public Pagination listBySuper(CustomerOrderQuery query,
                                  Integer pageNo, Integer pageSize) {

        String where_query = query.hqlWhereQuery("");
        
        Long total = baseDao.getOnly("select count(*) from CustomerOrderPo " + where_query);
        Pagination pagination = new Pagination(pageNo, pageSize, total.intValue());
        
        List<CustomerOrderVo> list = sessionFactory.getCurrentSession().createQuery(
            CustomerOrderVo.SELECT_1("")
                + " from CustomerOrderPo"
                + where_query
                + " order by createTime desc ")
            .setFirstResult(pagination.getFirstResult())
            .setMaxResults(pagination.getPageSize())
            .list();
        LOG.debug("list.size -> {}",list.size());
        
        pagination.setList(list);
        return pagination;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CustomerOrderPo> listBySuper(CustomerOrderQuery query) {
        String where_query = query.hqlWhereQuery("");
        List<CustomerOrderPo> list = sessionFactory.getCurrentSession().createQuery(
                " from CustomerOrderPo" + where_query + " order by createTime desc ")
            .list();
        
        return list;
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public List<OrderSettlementPo> findOrderSettlementList(OrderSettlementPo queryPo) {
        
        return sessionFactory.getCurrentSession().createCriteria(OrderSettlementPo.class)
            .add(Example.create(queryPo))
            .list();
    }

    @Override
    public Map<String, Object> statistics(CustomerOrderQuery query) {
        Map<String, Object> msg = new HashMap<>();
        String where_query = query.hqlWhereQuery("");
        Object totalcost = baseDao.getOnly("select sum(cost) from CustomerOrderPo " + where_query);
        
        msg.put("totalcost", totalcost);
        return msg;
    }

    @Override
    public Pagination list(CustomerOrderQuery orderQuery, IQuery trafficplanQuery,
                                  Integer pageNo, Integer pageSize) {
        // orderPo.productId=plan.id
        String 
            order_query = orderQuery.hqlQuery("orderPo"),
            order_sort = orderQuery.hqlSorted("orderPo"),
            plan_query = trafficplanQuery.hqlQuery("plan"),
            plan_sort = trafficplanQuery.hqlSorted("plan");
        String 
            from = "".equals(plan_query) ? " from CustomerOrderPo orderPo " : " from TrafficPlanPo plan, CustomerOrderPo orderPo ",
            query = toPlanOrderQuery.apply(order_query, plan_query),
            sorted = AbstractQuery.hqlSort(order_sort, plan_sort );
        query = addPrefix.apply(" where ", query);
        Long count = (Long)sessionFactory.getCurrentSession().createQuery("select count(*) " + from + query).uniqueResult();
        Pagination pagination = new Pagination(pageNo, pageSize, count.intValue());
        
        List<?> list = sessionFactory.getCurrentSession().createQuery("select orderPo " + from + query + sorted)
            .setFirstResult(pagination.getFirstResult())
            .setMaxResults(pagination.getPageSize())
            .list();
        pagination.setList(list);
        
        return pagination;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CustomerOrderPo> list(CustomerOrderQuery orderQuery, IQuery trafficplanQuery) {
        // orderPo.productId=plan.id
        String 
            order_query = orderQuery.hqlQuery("orderPo"),
            order_sort = orderQuery.hqlSorted("orderPo"),
            plan_query = trafficplanQuery.hqlQuery("plan"),
            plan_sort = "".equals(plan_query) ? "" : trafficplanQuery.hqlSorted("plan");
        String 
            from = "".equals(plan_query) ? " from CustomerOrderPo orderPo " : " from TrafficPlanPo plan, CustomerOrderPo orderPo ",
            query = toPlanOrderQuery.apply(order_query, plan_query),
            sorted = AbstractQuery.hqlSort(order_sort, plan_sort );
        query = addPrefix.apply(" where ", query);
        List<CustomerOrderPo> list = sessionFactory.getCurrentSession().createQuery("select orderPo " + from + query + sorted)
            .list();
        
        return list;
    }


}
