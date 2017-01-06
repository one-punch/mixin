/*
 * 文件名：PermissionDaoImpl.java
 * 版权：Copyright by wteam团队
 * 描述：
 * 修改人：benko
 * 修改时间：2016年6月13日 上午10:36:32
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.wteam.mixin.biz.dao.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wteam.mixin.biz.dao.IPermissionDao;
import com.wteam.mixin.model.po.PermissionPo;

/**
 * 权限DAO实现类
 *
 * @author benko
 * @version 2016年6月13日
 * @see PermissionDaoImpl
 * @since
 */
@Repository("permissionDao")
public class PermissionDaoImpl implements IPermissionDao {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(PermissionDaoImpl.class.getName());

    /**
     * 使用@Autowired注解将sessionFactory注入到UserDaoImpl中
     */
    @Autowired
    private SessionFactory sessionFactory;

    @SuppressWarnings("unchecked")
    @Override
    public List<PermissionPo> findByUserPrincipal(String principal) {
        LOG.debug(principal);
        List<?> list = sessionFactory.getCurrentSession().createQuery(
            "select distinct p from PermissionPo as p "
             + "    join p.roles as r "
             + "    join r.users as user "
             + "where p.isDelete=false "
             + "    and user.principal=:principal")
            .setString("principal", principal)
            .list();

        return (List<PermissionPo>)list;
    }

}
