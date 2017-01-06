package com.wteam.mixin.biz.dao.impl;


import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.wteam.mixin.model.query.IQuery.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.ITrafficPlanDao;
import com.wteam.mixin.biz.dao.IUserDao;
import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.BusinessTrafficPlanPo;
import com.wteam.mixin.model.po.MemberTrafficPlanPo;
import com.wteam.mixin.model.po.ProxyBusinessTrafficPlanPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.query.AbstractQuery;
import com.wteam.mixin.model.query.IQuery;
import com.wteam.mixin.pagination.Pagination;
import com.wteam.mixin.utils.Utils;

/**
 * @author benko
 *
 */
@Repository("trafficPlanDao")
@SuppressWarnings("unchecked")
public class TrafficPlanDaoImpl implements ITrafficPlanDao {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(TrafficPlanDaoImpl.class.getName());

    /**
     * 使用@Autowired注解将sessionFactory注入到OrderDaoImpl中
     */
    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    IBaseDao baseDao;
    @Autowired
    IUserDao userDao;


    /** 生成查询条件 query = toQuery.apply(plan_query, group_query),*/
    private final BiFunction< String,String,String> toPlanGroupQuery =
        (plan_query,group_query) -> "".equals(group_query) ? plan_query : " groupPo.id=plan.trafficGroupId "+  addPrefix.apply(" and", plan_query) + addPrefix.apply(" and", group_query);

    @Override
    public List<TrafficPlanPo> find(IQuery planQuery, IQuery groupQuery) {

        String
            plan_query = planQuery.hqlQuery("plan"),
            group_query = groupQuery.hqlQuery("groupPo"),
            plan_sort = planQuery.hqlSorted("plan"),
            group_sort = groupQuery.hqlSorted("groupPo");

        String
            from = "".equals(group_query) ? " from TrafficPlanPo plan " : " from TrafficPlanPo plan, TrafficGroupPo groupPo ",
            query = toPlanGroupQuery.apply(plan_query, group_query),
            sorted = AbstractQuery.hqlSort(plan_sort, group_sort);

        query = addPrefix.apply(" where ", query);

        List<TrafficPlanPo> list = sessionFactory.getCurrentSession().createQuery(
            "select plan " + from + query + sorted)
            .list();

        return list;
    }

    @Override
    public Pagination find(IQuery planQuery, IQuery groupQuery, Integer currentPage,
                           Integer pageSize) {
        String
            plan_query = planQuery.hqlQuery("plan"),
            group_query = groupQuery.hqlQuery("groupPo"),
            plan_sort = planQuery.hqlSorted("plan"),
            group_sort = groupQuery.hqlSorted("groupPo");

        String
            from = "".equals(group_query) ? " from TrafficPlanPo plan " : " from TrafficPlanPo plan, TrafficGroupPo groupPo ",
            query = toPlanGroupQuery.apply(plan_query, group_query),
            sorted = AbstractQuery.hqlSort(plan_sort, group_sort) ;
        query = addPrefix.apply(" where ", query);

        Long count = (Long)sessionFactory.getCurrentSession().createQuery("select count(*) " + from + query).uniqueResult();
        Pagination pagination = new Pagination(currentPage, pageSize, count.intValue());

        List<TrafficPlanPo> list = sessionFactory.getCurrentSession().createQuery("select plan " + from + query + sorted)
            .setFirstResult(pagination.getFirstResult())
            .setMaxResults(pagination.getPageSize())
            .list();

        pagination.setList(list);
        return pagination;
    }

    @Override
    public Pagination findProxy(IQuery planQuery, IQuery groupQuery, IQuery proxyQuery, Integer currentPage,
                                Integer pageSize) {
        String
            plan_query = planQuery.hqlQuery("plan"),
            group_query = groupQuery.hqlQuery("groupPo"),
            proxy_query = proxyQuery.hqlQuery("proxyPlan"),
            plan_sort = planQuery.hqlSorted("plan"),
            group_sort = groupQuery.hqlSorted("groupPo");

        String
            from = "".equals(group_query) ? " from TrafficPlanPo plan, ProxyBusinessTrafficPlanPo proxyPlan " : " from TrafficPlanPo plan, ProxyBusinessTrafficPlanPo proxyPlan , TrafficGroupPo groupPo ",
            query = toPlanGroupQuery.apply(plan_query, group_query),
            sorted = AbstractQuery.hqlSort(plan_sort, group_sort) ;
        query = addPrefix.apply(" where ", query + "and plan.id=proxyPlan.trafficplanId" + addPrefix.apply(" and ", proxy_query));

        Long count = (Long)sessionFactory.getCurrentSession().createQuery("select count(*) " + from + query).uniqueResult();
        Pagination pagination = new Pagination(currentPage, pageSize, count.intValue());

        List<TrafficPlanPo> list = sessionFactory.getCurrentSession().createQuery("select plan " + from + query + sorted)
            .setFirstResult(pagination.getFirstResult())
            .setMaxResults(pagination.getPageSize())
            .list();

        pagination.setList(list);
        return pagination;
    }

    @Override
    public List<TrafficPlanPo> findProxy(IQuery planQuery, IQuery groupQuery, IQuery proxyQuery) {
        String
            plan_query = planQuery.hqlQuery("plan"),
            group_query = groupQuery.hqlQuery("groupPo"),
            proxy_query = proxyQuery.hqlQuery("proxyPlan"),
            plan_sort = planQuery.hqlSorted("plan"),
            group_sort = groupQuery.hqlSorted("groupPo");

        String
            from = "".equals(group_query) ? " from TrafficPlanPo plan, ProxyBusinessTrafficPlanPo proxyPlan " : " from TrafficPlanPo plan, ProxyBusinessTrafficPlanPo proxyPlan , TrafficGroupPo groupPo ",
            query = toPlanGroupQuery.apply(plan_query, group_query),
            sorted = AbstractQuery.hqlSort(plan_sort, group_sort) ;
        query = addPrefix.apply(" where ", query + "and plan.id=proxyPlan.trafficplanId" + addPrefix.apply(" and ", proxy_query));

        List<TrafficPlanPo> list = sessionFactory.getCurrentSession()
            .createQuery("select plan " + from + query + sorted)
            .list();
        return list;
    }

    @Override
    public BigDecimal cost(Long businessId, Long trafficplanId) {
        BigDecimal cost = null;

        TrafficPlanPo planPo = baseDao.findUniqueByProperty("id", trafficplanId,TrafficPlanPo.class);
        cost = planPo.getCost();
        BusinessInfoPo _business = userDao.businessInfo(businessId);
        if (userDao.isRole(businessId, RoleType.bussiness)) {
            if (_business.getMemberId() != null && Utils.isMemberVail(_business.getMemberStartAt(), (long)_business.getMemberVailidity())) {
                MemberTrafficPlanPo memberPlanPo = baseDao.get("from MemberTrafficPlanPo where memberId=? and trafficplanId=? ",
                    Arrays.asList(_business.getMemberId(), trafficplanId));
                if (memberPlanPo != null) cost = memberPlanPo.getCost();
            }
        } else {
            ProxyBusinessTrafficPlanPo before = baseDao.get("from ProxyBusinessTrafficPlanPo where  businessId=? and trafficplanId=? ",
                Arrays.asList(businessId, trafficplanId));
            cost = before.getCost();
        }
        return cost;
    }

    @Override
    public BigDecimal retailPrice(Long businessId, Long trafficplanId) {
        TrafficPlanPo trafficPlanPo = baseDao.findUniqueByProperty("id", trafficplanId, TrafficPlanPo.class);
        // 根据商家ID获取商家套餐的零售价，并保存到订单中
        BusinessTrafficPlanPo businessTrafficPlanPo = baseDao.get("from BusinessTrafficPlanPo where businessId=? and trafficplanId=?",
            new Object[]{businessId, trafficplanId});
        return businessTrafficPlanPo != null ? businessTrafficPlanPo.getRetailPrice() : trafficPlanPo.getRetailPrice();
    }

}
