package com.wteam.mixin.biz.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.IUserDao;
import com.wteam.mixin.biz.service.IUserService;
import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.constant.State;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.CustomerInfoPo;
import com.wteam.mixin.model.po.MemberPo;
import com.wteam.mixin.model.po.MemberTrafficPlanPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.query.BusinessInfoQuery;
import com.wteam.mixin.model.query.IQuery;
import com.wteam.mixin.model.query.UserQuery;
import com.wteam.mixin.model.vo.BusinessInfoVo;
import com.wteam.mixin.model.vo.CustomerInfoVo;
import com.wteam.mixin.model.vo.UserInfoVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.pagination.Pagination;
import com.wteam.mixin.shiro.PasswordHelper;
import com.wteam.mixin.utils.Utils;

/**
 * 用户模块业务实现类
 *
 * @version 1.0
 * @author benko
 */
@Service("userService")
public class UserServiceImpl implements IUserService{

    /** 基本dao. */
    @Autowired
    private IBaseDao baseDao;
    @Autowired
    private IUserDao userDao;
    /** 对象转换器. */
    @Autowired
    private DozerBeanMapper mapper;

    /**商家设置会员*/
    private Function<BusinessInfoVo, BusinessInfoVo> _memberName = info -> {
        if (info.getMemberId() != null && Utils.isMemberVail(info.getMemberStartAt(), (long)info.getMemberVailidity())) {
            String name = baseDao.get("select name from MemberPo where id=? ", new Object[]{info.getMemberId()});
            info.setMemberName(name);
        }
        return info;
    } ;

    /**如果是代理商家获取父商家信息*/
    private Function<BusinessInfoVo, BusinessInfoVo> _proxyParent = info -> {
        if (info.getProxyParentId() != null ) {
            UserPo parent = baseDao.get("from UserPo where userId=?", new Object[]{info.getProxyParentId()});
            info.setProxyParentAccount(parent.getAccount());
            info.setProxyParentTel(parent.getTel());
            info.setProxyParentEmail(parent.getEmail());
        }
        return info;
    } ;

    /**如果是代理商家,计算他给父商家的收入*/
    private Function<BusinessInfoVo, BusinessInfoVo> _proxyParentIncome = info -> {
        if (info.getProxyParentId() != null ) {
            BigDecimal income = baseDao.get("select sum(money) from BusinessBalanceRecordPo "
                + " where  businessId=? and source=? and sourceId=?",
                new Object[]{info.getProxyParentId(), State.BBRecordSource.proxyIncome, info.getBusinessId()});
            info.setProxyParentIncome(income != null ? income : BigDecimal.ZERO);
        }
        return info;
    } ;

    /**商家信息默认处理块*/
    private Function<List<?>, List<?>> _businessDefaultBock = pos -> {
        return pos.stream().map(po -> (BusinessInfoVo)po).map(_memberName).map(_proxyParentIncome).collect(Collectors.toList());
    };

    @Override
    public CustomerInfoVo updateInfo(CustomerInfoVo infoVo) {
        CustomerInfoPo infoPo = baseDao.get("from CustomerInfoPo where customerId=?", new Object[]{infoVo.getCustomerId()});
        if (infoPo == null)
            throw new ServiceException("找不到顾客信息 " + infoVo.getCustomerId());
        mapper.map(infoVo, infoPo);
        baseDao.update(infoPo);

        return mapper.map(infoPo, CustomerInfoVo.class);
    }

    @Override
    public BusinessInfoVo updateInfo(BusinessInfoVo infoVo) {
        BusinessInfoPo infoPo = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{infoVo.getBusinessId()});
        if (infoPo == null)
            throw new ServiceException("找不到商家信息 " + infoVo.getBusinessId());
        mapper.map(infoVo, infoPo);
        baseDao.update(infoPo);

        return mapper.map(infoPo, BusinessInfoVo.class);
    }

    @Override
    public UserVo changePassword(UserVo userVo) {
        UserPo userPo = baseDao.get("from UserPo where userId=?", new Object[]{userVo.getUserId()});
        if (userPo == null)
            throw new ServiceException("找不到用户 " + userVo.getUserId());
        UserVo userVo2 = mapper.map(userPo, UserVo.class);
        userVo2.setPassword(userVo.getPassword());
        new PasswordHelper().encryptPassword(userVo2);
        mapper.map(userVo2, userPo);
        baseDao.update(userPo);
        return userVo2;
    }

    @Override
    public UserInfoVo adminInfo(Long userId) {
        UserPo userPo = baseDao.get("from UserPo where userId=?", new Object[]{userId});
        if (userPo == null)
            throw new ServiceException("找不到用户 " + userId);
        UserInfoVo userInfo = mapper.map(userPo, UserInfoVo.class);
        return userInfo;
    }

    @Override
    public BusinessInfoVo businessInfo(Long businessId) {
        BusinessInfoPo infoPo = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{businessId});
        if (infoPo == null)
            throw new ServiceException("找不到商家信息 " + businessId);
        BusinessInfoVo infoVo = mapper.map(infoPo, BusinessInfoVo.class);
        // 用户信息
        UserPo userPo = baseDao.get("from UserPo where userId=?", new Object[]{businessId});
        mapper.map(userPo, infoVo);
        // 会员信息,父代理信息
        infoVo = _memberName.andThen(_proxyParent).apply(infoVo);
        return infoVo;
    }

    @Override
    public CustomerInfoVo customerInfo(Long customerId) {
        CustomerInfoPo infoPo = baseDao.get("from CustomerInfoPo where customerId=?", new Object[]{customerId});
        if (infoPo == null)
            throw new ServiceException("找不到顾客信息 " + customerId);
        return mapper.map(infoPo, CustomerInfoVo.class);
    }

    @Override
    public Pagination businessListBySuper(String wechatOfficAccount, Long businessId,
                                                    String tel, Integer pageNo, Integer pageSize) {

        UserQuery userQuery = new UserQuery();
        BusinessInfoQuery businessInfoQuery = new BusinessInfoQuery();
        userQuery.setAccount(tel);
        businessInfoQuery.setWechatOfficAccount(wechatOfficAccount);
        businessInfoQuery.setBusinessId(businessId);
        businessInfoQuery.setIsDelete(false);

        Pagination pagination = userDao.findBusiness(userQuery, businessInfoQuery, pageNo, pageSize);

        return pagination.handle(_businessDefaultBock);
    }

    @Override
    public Pagination businessList(UserQuery userQuery, BusinessInfoQuery businessQuery, Integer pageNo,
                                        Integer pageSize) {
        userQuery.setIsDelete(false);
        businessQuery.setIsDelete(false);

        Pagination pagination = userDao.findBusiness(userQuery, businessQuery, pageNo, pageSize);
        return pagination.handle(_businessDefaultBock);
    }

    @Override
    public boolean isRole(Long userId, RoleType role) {
        return userDao.isRole(userId, role);
    }


}
