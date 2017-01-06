package com.wteam.mixin.biz.dao.impl;

import static com.wteam.mixin.model.query.IQuery.addPrefix;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wteam.mixin.biz.dao.ITrafficGroupDao;
import com.wteam.mixin.model.po.TrafficGroupPo;
import com.wteam.mixin.model.query.AbstractQuery;
import com.wteam.mixin.model.query.IQuery;

@Repository("trafficGroupDao")
public class TrafficGroupDaoImpl implements ITrafficGroupDao{
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(TrafficPlanDaoImpl.class.getName());

    /**
     * 使用@Autowired注解将sessionFactory注入到OrderDaoImpl中
     */
    @Autowired
    private SessionFactory sessionFactory;

    @Override
    @SuppressWarnings("unchecked")
    public List<TrafficGroupPo> findList(IQuery groupQuery) {
        String 
            group_query = groupQuery.hqlQuery("groupPo"),
            group_sort = groupQuery.hqlSorted("groupPo");
        
        String 
            from = " from TrafficGroupPo groupPo ",
            query = addPrefix.apply(" where ", group_query),
            sorted = AbstractQuery.hqlSort( group_sort);;
        List<TrafficGroupPo> list = sessionFactory.getCurrentSession().createQuery(
                "select groupPo " + from + query + sorted)
                .list();
        return list;
    }

}
