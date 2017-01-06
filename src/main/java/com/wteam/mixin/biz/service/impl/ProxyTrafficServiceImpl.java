package com.wteam.mixin.biz.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.ITrafficGroupDao;
import com.wteam.mixin.biz.dao.ITrafficPlanDao;
import com.wteam.mixin.biz.dao.IUserDao;
import com.wteam.mixin.biz.service.IProxyTrafficService;
import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.BusinessTrafficPlanPo;
import com.wteam.mixin.model.po.ProxyBusinessTrafficPlanPo;
import com.wteam.mixin.model.po.TrafficGroupPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.query.ProxyBusinessTrafficQuery;
import com.wteam.mixin.model.query.TrafficGroupQuery;
import com.wteam.mixin.model.query.TrafficPlanQuery;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.model.vo.GroupNPlanVo;
import com.wteam.mixin.model.vo.ProxyBusinessTrafficPlanVo;
import com.wteam.mixin.model.vo.TrafficPlanVo;
import com.wteam.mixin.pagination.Pagination;
import com.wteam.mixin.utils.LambdaUtils;

/**
 * 代理商家流量模块
 * @author benko
 *
 */
@Service("proxyTrafficService")
public class ProxyTrafficServiceImpl implements IProxyTrafficService {
    private static Logger LOG = LogManager.getLogger(ProxyTrafficServiceImpl.class.getName());
    @Autowired
    IBaseDao baseDao;
    @Autowired
    IUserDao userDao;
    @Autowired
    ITrafficPlanDao trafficPlanDao;
    @Autowired
    ITrafficGroupDao trafficGroupDao;
    @Autowired
    private DozerBeanMapper mapper;

//    /**套餐转换处理*/
//    private Function<Object, TrafficPlanVo> _planToVo = LambdaUtils.mapTo(mapper, TrafficPlanVo.class);

    /**套餐设置分组信息处理*/
    private Function<TrafficPlanVo, TrafficPlanVo> _planGroupInfo = vo -> {
        if (vo.getTrafficgroupId() != null) {
            TrafficGroupPo groupPo = baseDao.findUniqueByProperty("id", vo.getTrafficgroupId(),
                TrafficGroupPo.class);
            if (groupPo != null) {
                vo.setProvince(groupPo.getProvince());
                vo.setGroupName(groupPo.getName());
            }
        }
        return vo;
    };

    @SuppressWarnings("unchecked")
    @Override
    public Pagination listTrafficPlan(TrafficPlanQuery planQuery, TrafficGroupQuery groupQuery,
                                      final Long businessId, Integer currentPage, Integer pageSize) {
        ProxyBusinessTrafficQuery proxyQuery = searchBefore(planQuery, groupQuery, businessId);
        Pagination pagination = trafficPlanDao.findProxy(planQuery, groupQuery, proxyQuery, currentPage, pageSize);
        return pagination.handle(bockMap(_planGroupInfo, func_proxyBusinessPlan(businessId), func_proxyPlanCost(businessId)));
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<GroupNPlanVo> listGroupNPlan(TrafficPlanQuery planQuery,
                                                       TrafficGroupQuery groupQuery,
                                                       Long businessId) {
        ProxyBusinessTrafficQuery proxyQuery = searchBefore(planQuery, groupQuery, businessId);

        List<TrafficGroupPo> groupPos = trafficGroupDao.findList(groupQuery);

        List<GroupNPlanVo> list = groupPos.stream()
        .map(groupPo -> {
            GroupNPlanVo gnp = mapper.map(groupPo, GroupNPlanVo.class);
            planQuery.setTrafficGroupId(groupPo.getId());
            List<TrafficPlanPo> poLst = trafficPlanDao.findProxy(planQuery, groupQuery, proxyQuery);
            List<TrafficPlanVo> planList = (List<TrafficPlanVo>)bockMap(_planGroupInfo, func_proxyBusinessPlan(businessId), func_proxyPlanCost(businessId)).apply(poLst);
            gnp.setTrafficplanList(planList);
            return gnp;
        })
        .collect(Collectors.toList());

        return list;
    }



    @SuppressWarnings("unchecked")
    @Override
    public List<GroupNPlanVo> listGroupNPlanByCustomer(TrafficPlanQuery planQuery,
                                                       TrafficGroupQuery groupQuery,
                                                       Long businessId) {
        ProxyBusinessTrafficQuery proxyQuery = searchBefore(planQuery, groupQuery, businessId);

        List<TrafficGroupPo> groupPos = trafficGroupDao.findList(groupQuery);

        List<GroupNPlanVo> list = groupPos.stream()
        .map(groupPo -> {
            GroupNPlanVo gnp = mapper.map(groupPo, GroupNPlanVo.class);
            planQuery.setTrafficGroupId(groupPo.getId());
            List<TrafficPlanPo> poLst = trafficPlanDao.findProxy(planQuery, groupQuery, proxyQuery);
            List<TrafficPlanVo> planList = (List<TrafficPlanVo>)bockMap(_planGroupInfo, func_proxyBusinessPlan(businessId), func_proxyPlanCost(businessId))
                                            .andThen(bockFilter(TrafficPlanVo::getDisplay)) // 只给顾客看可显示的
                                            .apply(poLst);
            gnp.setTrafficplanList(planList);
            return gnp;
        })
        .collect(Collectors.toList());

        return list;
    }


    @Override
    public ProxyBusinessTrafficPlanVo add(ProxyBusinessTrafficPlanVo proxyPlanVo) {

        if (!userDao.isRole(proxyPlanVo.getBusinessId(), RoleType.proxy_business))
            throw new ServiceException("不是代理商家");
        BusinessInfoPo _business = userDao.businessInfo(proxyPlanVo.getBusinessId());

        ProxyBusinessTrafficPlanPo before = baseDao.get("from ProxyBusinessTrafficPlanPo where businessId=? and parentId=? and trafficplanId=? ",
            Arrays.asList(proxyPlanVo.getBusinessId(), _business.getProxyParentId(), proxyPlanVo.getTrafficplanId()));
        if (before != null) {
            throw new ServiceException("已经添加了这个套餐");
        }
        proxyPlanVo.setParentId(_business.getProxyParentId());
        ProxyBusinessTrafficPlanPo po = mapper.map(proxyPlanVo, ProxyBusinessTrafficPlanPo.class);
        baseDao.save(po);

        return  mapper.map(po, ProxyBusinessTrafficPlanVo.class);
    }


    @Override
    public ProxyBusinessTrafficPlanVo edit(ProxyBusinessTrafficPlanVo proxyPlanVo) {
        ProxyBusinessTrafficPlanPo before = baseDao.get("from ProxyBusinessTrafficPlanPo where businessId=? and trafficplanId=? ",
            Arrays.asList(proxyPlanVo.getBusinessId(), proxyPlanVo.getTrafficplanId()));
        if (before == null)
            throw new ServiceException("套餐不存在");
        BigDecimal minCost = trafficPlanDao.cost(before.getParentId(), before.getTrafficplanId());
        LOG.debug("minCost:{}, proxyPlanVo.cost:{}, compareTo:{}", minCost, proxyPlanVo.getCost(), minCost.compareTo(proxyPlanVo.getCost()));
        if (minCost.compareTo(proxyPlanVo.getCost()) >= 0) {
            throw new ServiceException("设置的成本低于你的成本价");
        }
        before.setCost(proxyPlanVo.getCost());
        baseDao.update(before);
        return mapper.map(before, ProxyBusinessTrafficPlanVo.class);
    }


    @Override
    public void delete(ProxyBusinessTrafficPlanVo proxyPlanVo) {
        ProxyBusinessTrafficPlanPo before = baseDao.get("from ProxyBusinessTrafficPlanPo where businessId=? and trafficplanId=? ",
            Arrays.asList(proxyPlanVo.getBusinessId(), proxyPlanVo.getTrafficplanId()));
        if (before == null)
            throw new ServiceException("套餐不存在");
        baseDao.delete(before);
    }

    /**
     * 获取代理商家套餐设置成本方法
     * @param businessId
     * @return
     */
    private Function<TrafficPlanVo, TrafficPlanVo> func_proxyPlanCost(final Long businessId) {
        return vo -> {
            BigDecimal cost = baseDao.get("select cost from ProxyBusinessTrafficPlanPo where businessId=? and trafficplanId=? ", Arrays.asList(businessId, vo.getId()));
            vo.setCost(cost);
            return vo;
        };
    }


    /**
     * 获取代理商家套餐设置零售价、tip、显示方法
     * @param businessId
     * @return
     */
    private Function<TrafficPlanVo, TrafficPlanVo> func_proxyBusinessPlan(final Long businessId) {
        return vo -> {
            BusinessTrafficPlanPo _busiPlan = baseDao.get("from BusinessTrafficPlanPo where trafficplanId=? and businessId=?", new Object[]{vo.getId(), businessId});
            if(_busiPlan != null){
                vo.setRetailPrice(_busiPlan.getRetailPrice());
                vo.setDisplay(_busiPlan.getDisplay());
                vo.setTip(_busiPlan.getTip());
            }
            return vo;
        };
    }

    /**
     * 查询之前设置通用查询条件
     * @param planQuery
     * @param groupQuery
     * @param businessId
     * @return
     */
    private ProxyBusinessTrafficQuery searchBefore(TrafficPlanQuery planQuery,TrafficGroupQuery groupQuery,Long businessId) {
        if (!userDao.isRole(businessId, RoleType.proxy_business))
            throw new ServiceException("不是代理商家");

        BusinessInfoPo _business = userDao.businessInfo(businessId);
        ProxyBusinessTrafficQuery proxyQuery = new ProxyBusinessTrafficQuery();
        proxyQuery.setBusinessId(businessId);
        proxyQuery.setParentId(_business.getProxyParentId());

        planQuery.setIsDelete(false);
        planQuery.setDisplay(true);
        planQuery.setIsMaintain(false);
        groupQuery.setIsDelete(false);
        groupQuery.setDisplay(true);

        groupQuery.putSortField("sort", false);

        return proxyQuery;
    }


    private Function<List<?>, List<?>> bockMap(Function<TrafficPlanVo, TrafficPlanVo>... functions) {
        return LambdaUtils.bockMap(LambdaUtils.mapTo(mapper, TrafficPlanVo.class), functions);
    }
    private Function<List<?>, List<?>> bockFilter(Predicate<TrafficPlanVo>... filters) {
        return LambdaUtils.bockFilter(LambdaUtils.mapTo(mapper, TrafficPlanVo.class), filters);
    }

}
