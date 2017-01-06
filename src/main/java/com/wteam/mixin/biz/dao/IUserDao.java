package com.wteam.mixin.biz.dao;

import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.query.IQuery;
import com.wteam.mixin.pagination.Pagination;

/**
 *
 * @author benko
 * @version 2016年6月2日
 * @see IUserDao
 * @since
 */
public interface IUserDao {

    /**
     * Description:  根据身份(可以是用户名,电话,邮箱)查找用户,<br>
     *
     * @param principal 唯一索引
     * @param isDelete 是否被删除
     * @return UserPo
     * @author benko
     * @see
     */
    UserPo findByPrincipal(String principal, boolean isDelete);

    /**
     * 查找商家列表
     * @param userQuery
     * @param businessQuery
     * @param pageNo
     * @param pageSize
     * @return
     */
    Pagination findBusiness(IQuery userQuery, IQuery businessQuery, Integer pageNo, Integer pageSize);
    /**
     *
     * @param businessId
     * @return
     */
    BusinessInfoPo businessInfo(Long businessId);

    /**
     * 用户是这个角色吗
     * @param userId
     * @param role
     * @return
     */
    boolean isRole(Long userId, RoleType role);

    /**
     * 获取代理商家的父商家
     * @param businessId
     * @return
     */
    public BusinessInfoPo proxyParentBy(Long businessId);
}
