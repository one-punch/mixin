package com.wteam.mixin.biz.service.impl;

import java.util.function.UnaryOperator;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.thoughtworks.xstream.alias.ClassMapper.Null;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.service.IProxyBusinessService;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessInfoPo;
/**
 * 代理商家模块
 * @author benko
 *
 */
@Service("proxyBusinessService")
public class ProxyBusinessServiceImpl implements IProxyBusinessService {

    @Autowired
    IBaseDao baseDao;

    private static final BusinessInfoPo NONE = new BusinessInfoPo();

    UnaryOperator<BusinessInfoPo> getProxyParent = proxyBusiness -> {
        System.out.println(proxyBusiness);
        return proxyBusiness != null && proxyBusiness.getProxyParentId() != null ? baseDao.findUniqueByProperty("businessId", proxyBusiness.getProxyParentId(), BusinessInfoPo.class) : NONE;
    };

    @Override
    public boolean isProxyAncestry(Long proxyBusinessId, Long ancestryId) {
        BusinessInfoPo proxyBusinessPo = baseDao.findUniqueByProperty("businessId", proxyBusinessId, BusinessInfoPo.class);
        if (proxyBusinessPo == null) {
            throw new ServiceException("没有这个代理商家");
        }
        if (proxyBusinessId.equals(ancestryId)) {
            return false;
        }

        return Stream.iterate(proxyBusinessPo, getProxyParent )
             .filter(po -> po == NONE || po.getBusinessId().equals(ancestryId))
             .findFirst()
             .get() != NONE;
    }

    @Override
    public boolean changeAllowBalanceRecharge(Long proxyBusinessId, Long ancestryId) {
        if (!isProxyAncestry(proxyBusinessId, ancestryId))
            throw new ServiceException("此商家不是你的代理商家");

        BusinessInfoPo proxyBusinessPo = baseDao.findUniqueByProperty("businessId", proxyBusinessId, BusinessInfoPo.class);
        proxyBusinessPo.setIsAllowBalanceRecharge(!proxyBusinessPo.getIsAllowBalanceRecharge());
        baseDao.update(proxyBusinessPo);
        return true;
    }

}
