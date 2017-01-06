package com.wteam.mixin.biz.service;

import java.util.Set;

import com.wteam.mixin.model.vo.UserVo;


/**
 * 登录模块业务接口
 * @version 1.0
 * @author benko
 * @time 2016-4-12 21:30:16
 */
public interface ILoginService {

    /**
     * 根据身份(用户名,电话,邮箱)查找用户,
     * @param principal
     * @return 失败返回null
     */
    UserVo findByPrincipal(String principal);

    /**
     * 根据用户名查找对应的角色
     * @param username 根据用户名
     * @return 失败返回null
     */
    Set<String> findRolesByUsername(String username);

    /**
     * 查找所有的没有被标志为删除的权限
     *
     * @return Set
     * @see
     */
    Set<String> findAllPermission();

    /**
     * 根据用户的唯一标志查找所有的没有被标志为删除的权限
     *
     * @param principal 用户的唯一标志
     * @return Set
     * @see
     */
    Set<String> findPermsByUserPrincipal(String principal);


    /**
     * 手机号和账号注册商家用户
     * @param userVo
     * @return 失败返回null
     */
    UserVo registerBusiness(UserVo userVo);

    /**
     * 注册手机号和账号代理商家用户
     * @param business
     * @param parentId 父商家ID
     * @return 失败返回null
     */
    UserVo registerProxyBusiness(UserVo business, Long parentId);

    /**
     * 使用openId注册顾客
     * @param userVo
     * @return
     */
    UserVo registerCustomer(String openId);

    /**
     * 根据openId查找顾客的account
     * @param openId
     * @return 失败返回null
     */
    UserVo findCustomerByOpenId(String openId);

}
