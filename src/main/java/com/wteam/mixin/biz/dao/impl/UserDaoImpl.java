package com.wteam.mixin.biz.dao.impl;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import static org.hibernate.criterion.Restrictions.*;
import static com.wteam.mixin.model.query.IQuery.*;

import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.IUserDao;
import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.query.AbstractQuery;
import com.wteam.mixin.model.query.IQuery;
import com.wteam.mixin.model.vo.BusinessInfoVo;
import com.wteam.mixin.pagination.Pagination;

/**
 *
 *
 *
 * @author Administrator
 * @version 2016年6月2日
 * @see UserDaoImpl
 * @since
 */
@Repository("userDao")
public class UserDaoImpl implements IUserDao {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(UserDaoImpl.class.getName());

    /**
     * 使用@Autowired注解将sessionFactory注入到UserDaoImpl中
     */
    @Autowired
    private SessionFactory sessionFactory;
    @Autowired
    IBaseDao baseDao;

    /** 生成查询条件 query = toQuery.apply(user_query, business_query),*/
    private final BiFunction< String,String,String> toBusinessQuery =
        (user_query,business_query) -> " user.userId=business.businessId "+  addPrefix.apply(" and", user_query) + addPrefix.apply(" and", business_query);

    /**
     * 根据账号或手机号获取用户
     * @author benko
     */
    @Override
    public UserPo findByPrincipal(String principal, boolean isDelete) {
        LOG.debug(principal);
        return (UserPo)sessionFactory.getCurrentSession().createCriteria(UserPo.class).add(
            and(
             or(eq("tel", principal),
                eq("email", principal),
                eq("principal", principal),
                eq("account", principal)),
             eq("isDelete", isDelete)))
            .uniqueResult();
    }

    @Override
    public Pagination findBusiness(IQuery userQuery, IQuery businessQuery, Integer pageNo,
                                   Integer pageSize) {

        String
            user_query = userQuery.hqlQuery("user"),
            business_query = businessQuery.hqlQuery("business"),
            user_sort = userQuery.hqlSorted("user"),
            business_sort = businessQuery.hqlSorted("business");

        String
            from = " from UserPo user, BusinessInfoPo business ",
            query = toBusinessQuery.apply(user_query, business_query),
            sorted = AbstractQuery.hqlSort(user_sort, business_sort);
        query = addPrefix.apply(" where ", query);

        Long count = (Long)sessionFactory.getCurrentSession().createQuery("select count(*) " + from + query).uniqueResult();
        Pagination pagination = new Pagination(pageNo, pageSize, count.intValue());

        List<BusinessInfoVo> list = sessionFactory.getCurrentSession().createQuery(BusinessInfoVo.SELECT_1 + from + query + sorted)
            .setFirstResult(pagination.getFirstResult())
            .setMaxResults(pagination.getPageSize())
            .list();

        pagination.setList(list);
        return pagination;
    }

    @Override
    public boolean isRole(Long userId, RoleType role) {
        UserPo userPo = baseDao.get("from UserPo where userId=?", Arrays.asList(userId));
        if (userPo == null) {
            throw new ServiceException("找不到这个用户：" + userId);
        }
        return userPo.getRoles().stream().reduce(false, (result,po) -> po.getRole().equals(role), (result, item) -> result || item);
    }

    @Override
    public BusinessInfoPo businessInfo(Long businessId) {
        return baseDao.get("from BusinessInfoPo where businessId=? and isDelete=false", Arrays.asList(businessId));
    }

    @Override
    public BusinessInfoPo proxyParentBy(Long businessId) {
        return baseDao.get("from BusinessInfoPo pb where pb.businessId=(select proxyParentId from  BusinessInfoPo where businessId=?)", Arrays.asList(businessId));
    }

}
