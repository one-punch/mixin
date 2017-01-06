package com.wteam.mixin.biz.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.IPermissionDao;
import com.wteam.mixin.biz.dao.IUserDao;
import com.wteam.mixin.biz.service.ILoginService;
import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.CustomerInfoPo;
import com.wteam.mixin.model.po.PermissionPo;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.CustomerInfoVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.shiro.PasswordHelper;


/**
 * 登录模块业务实现类
 *
 * @version 1.0
 * @author benko
 */
@Service("loginService")
public class LoginServiceImpl implements ILoginService {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(LoginServiceImpl.class.getName());
    /** 用户DAO. */
    @Autowired
    private IUserDao userDao;
    /** 权限DAO. */
    @Autowired
    private IPermissionDao permissionDao;
    /** 基本dao. */
    @Autowired
    private IBaseDao baseDao;
    /** 对象转换器. */
    @Autowired
    private DozerBeanMapper mapper;

    @Override
    public UserVo findByPrincipal(String principal) {
        try {
            if (principal != null && !"".equals(principal)) {
                UserPo userPo = userDao.findByPrincipal(principal, false);
                UserVo userVo = mapper.map(userPo, UserVo.class);
                return userVo;
            }
        }
        catch (Exception e) {
            LOG.debug("", e);
        }
        return null;
    }

    @Override
    public Set<String> findRolesByUsername(String username) {
        UserPo userPo = userDao.findByPrincipal(username, false);
        if (userPo != null)
            return userPo.getRoles().stream().map(role -> role.getRole().name()).collect(
                Collectors.toSet());
        return null;
    }

    @Override
    public Set<String> findAllPermission() {
        Set<String> permissions = baseDao.findByProperty("isDelete", false, PermissionPo.class).stream()
            .map(po -> po.getResourcesUrl())
            .collect(Collectors.toSet());

        return permissions;
    }

    @Override
    public Set<String> findPermsByUserPrincipal(String principal) {
        Set<String> permissions = permissionDao.findByUserPrincipal(principal).stream()
            .map(po -> po.getResourcesUrl())
            .collect(Collectors.toSet());
        return permissions;
    }

    @Override
    public UserVo registerBusiness(UserVo userVo) {
        UserPo userPo ;
        // 唯一性验证
        userPo = baseDao.findUniqueByProperty("account", userVo.getAccount(), UserPo.class);
        if(userPo != null)
            throw new ServiceException("用户号已经被注册");
        userPo = baseDao.findUniqueByProperty("tel", userVo.getTel(), UserPo.class);
        if(userPo != null)
            throw new ServiceException("手机号已经被注册");
        // 密码加密
        PasswordHelper helper = new PasswordHelper();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        userVo.setPrincipal(uuid);
        userVo.setPassword(userVo.getCredential());
        helper.encryptPassword(userVo);
        // 保存
        userPo = mapper.map(userVo, UserPo.class);
        baseDao.save(userPo);
        // 关联商家角色
        RolePo businessRole = baseDao.findUniqueByProperty("role", RoleType.bussiness, RolePo.class);
        userPo.getRoles().add(businessRole);
        // 保存商家信息
        BusinessInfoPo infoPo = new BusinessInfoPo();
        BigDecimal zero = new BigDecimal("0");
        infoPo.setBalance(zero);
        infoPo.setUnSettlement(zero);
        infoPo.setSettlement(zero);
        infoPo.setOrderIncome(zero);
        infoPo.setOrderCost(zero);
        infoPo.setProfits(zero);
        infoPo.setBusinessId(userPo.getUserId());
        baseDao.save(infoPo);

        return mapper.map(userPo, UserVo.class);
    }

    @Override
    public UserVo registerCustomer(String openId) {

        UserVo customer = new UserVo();
        customer.setAccount(openId);
        customer.setPassword(openId);
        // 密码加密
        encryptUser(customer);
        // 保存
        UserPo userPo = mapper.map(customer, UserPo.class);
        baseDao.save(userPo);
        // 关联顾客角色
        RolePo customerRole = baseDao.findUniqueByProperty("role", RoleType.customer, RolePo.class);
        userPo.getRoles().add(customerRole);
        // 保存顾客信息
        CustomerInfoPo infoPo = new CustomerInfoPo();
        infoPo.setCreateTime(new Date());
        infoPo.setIntegral(0);
        infoPo.setCustomerId(userPo.getUserId());
        baseDao.save(infoPo);
        baseDao.flush();
        return mapper.map(userPo, UserVo.class);
    }

    @Override
    public UserVo findCustomerByOpenId(String openId) {
        UserPo userPo = baseDao.findUniqueByProperty("account", openId, UserPo.class);
        return userPo == null ? null : mapper.map(userPo, UserVo.class);
    }

    /**
     * 加密用户密码
     * @param userVo
     */
    private void encryptUser(UserVo userVo) {
        PasswordHelper helper = new PasswordHelper();
        String uuid;
        uuid = UUID.randomUUID().toString().replaceAll("-", "");
        userVo.setPrincipal(uuid);
        helper.encryptPassword(userVo);
    }

    @Override
    public UserVo registerProxyBusiness(UserVo business, Long parentId) {
        UserPo userPo ;
        // 唯一性验证
        userPo = baseDao.findUniqueByProperty("account", business.getAccount(), UserPo.class);
        if(userPo != null)
            throw new ServiceException("用户号已经被注册");
        userPo = baseDao.findUniqueByProperty("tel", business.getTel(), UserPo.class);
        if(userPo != null)
            throw new ServiceException("手机号已经被注册");
        // 密码加密
        PasswordHelper helper = new PasswordHelper();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        business.setPrincipal(uuid);
        business.setPassword(business.getCredential());
        helper.encryptPassword(business);
        // 保存
        userPo = mapper.map(business, UserPo.class);
        baseDao.save(userPo);
        // 关联代理商家角色
        RolePo businessRole = baseDao.findUniqueByProperty("role", RoleType.proxy_business, RolePo.class);
        userPo.getRoles().add(businessRole);
        // 保存商家信息
        BusinessInfoPo infoPo = new BusinessInfoPo();
        BigDecimal zero = BigDecimal.ZERO;
        infoPo.setProxyParentId(parentId); // 设置父商家
        infoPo.setBalance(zero);
        infoPo.setUnSettlement(zero);
        infoPo.setSettlement(zero);
        infoPo.setOrderIncome(zero);
        infoPo.setOrderCost(zero);
        infoPo.setProfits(zero);
        infoPo.setIsAllowBalanceRecharge(true);
        infoPo.setBusinessId(userPo.getUserId());
        baseDao.save(infoPo);

        return mapper.map(userPo, UserVo.class);
    }

}
