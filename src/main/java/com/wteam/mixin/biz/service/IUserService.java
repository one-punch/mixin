package com.wteam.mixin.biz.service;

import java.util.List;

import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.model.query.BusinessInfoQuery;
import com.wteam.mixin.model.query.IQuery;
import com.wteam.mixin.model.query.UserQuery;
import com.wteam.mixin.model.vo.BusinessInfoVo;
import com.wteam.mixin.model.vo.CustomerInfoVo;
import com.wteam.mixin.model.vo.UserInfoVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.pagination.Pagination;

/**
 * 用户模块模块业务接口
 * @version 1.0
 * @author benko
 * @time 2016-4-12 21:30:16
 */
public interface IUserService {

    /**
     * 根据条件分页获取商家列表
     * @return
     */
    Pagination businessListBySuper(String wechatOfficAccount, Long businessId, String tel, Integer pageNo, Integer pageSize);
    /**
     * 根据条件分页获取代理商家列表
     * @return
     */
    Pagination businessList(UserQuery userQuery, BusinessInfoQuery businessQuery, Integer pageNo, Integer pageSize);

    /**
     * 更新顾客信息
     * @param infoVo
     * @return
     */
    CustomerInfoVo updateInfo(CustomerInfoVo infoVo);

    /**
     * 更新商家信息
     * @param infoVo
     * @return
     */
    BusinessInfoVo updateInfo(BusinessInfoVo infoVo);

    /**
     * 获取商家信息
     * @param infoVo
     * @return
     */
    BusinessInfoVo businessInfo(Long businessId);
    /**
     * 获取顾客信息
     * @param infoVo
     * @return
     */
    CustomerInfoVo customerInfo(Long customerId);
    /**
     * 获取管理员信息
     * @param infoVo
     * @return
     */
    UserInfoVo adminInfo(Long customerId);


    /**
     * 修改密码
     * @param userVo
     * @return
     */
    UserVo changePassword(UserVo userVo);

    /**
     * 用户是否role角色
     * @param userId
     * @param role
     * @return
     */
    boolean isRole(Long userId, RoleType role);

}
