package com.wteam.mixin.biz.dao.impl;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.ITrafficPlanActivitiesDao;
import com.wteam.mixin.constant.Provider;
import com.wteam.mixin.model.po.TrafficPlanActivity;
import com.wteam.mixin.model.vo.ActivityPlanVo;
import com.wteam.mixin.model.vo.BargainirgPlanVo;
import com.wteam.mixin.model.vo.TrafficPlanActivityVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by zbin on 17/1/20.
 */
@SuppressWarnings("unchecked")
@Repository("trafficPlanActivitiesDao")
public class TrafficPlanActivitiesDaoImpl implements ITrafficPlanActivitiesDao {

    @Autowired
    IBaseDao baseDao;

    public List<ActivityPlanVo> preSelectPlan(String sql, Long businessId){
        return baseDao.find(sql, new Object[]{businessId, businessId})
                .parallelStream().map(u -> {
                    ActivityPlanVo aPlanVo = new ActivityPlanVo();
                    Object[] o = (Object[]) u;
                    aPlanVo.setId((Long) o[0]);
                    aPlanVo.setName(Optional.ofNullable(o[1]).orElse("").toString() + Optional.ofNullable(o[2]).orElse("").toString());
                    return aPlanVo;
                }).collect(Collectors.toList());
    }

    public List<BargainirgPlanVo> getList(String sql, Object[] params, Integer pageNo, Integer pageSize){
        return baseDao.find(sql, params, pageNo, pageSize).parallelStream()
                .map(p -> generator((Object[]) p)).collect(Collectors.toList());
    }

    private BargainirgPlanVo generator(Object[] o){
        BargainirgPlanVo bargainirgPlanVo = new BargainirgPlanVo();
        bargainirgPlanVo.setName(Optional.ofNullable(o[0]).orElse("").toString());
        bargainirgPlanVo.setCost((BigDecimal)Optional.ofNullable(o[1]).orElse(BigDecimal.ZERO));
        bargainirgPlanVo.setProviderName(Provider.get((Integer)o[2]).name);
        bargainirgPlanVo.setApiProvider(Optional.ofNullable(o[3]).orElse("").toString());
        bargainirgPlanVo.setProvince(Optional.ofNullable(o[4]).orElse("").toString());
        bargainirgPlanVo.setIsActive((Boolean) Optional.ofNullable(o[5]).orElse(Boolean.FALSE));
        bargainirgPlanVo.setStartTime((Date)Optional.ofNullable(o[6]).orElse(new Date()));
        bargainirgPlanVo.setEndTime((Date)Optional.ofNullable(o[7]).orElse(new Date()));
        bargainirgPlanVo.setLowPrice((BigDecimal)Optional.ofNullable(o[8]).orElse(BigDecimal.ZERO));
        bargainirgPlanVo.setLimitNumber((Integer) Optional.ofNullable(o[9]).orElse(0));
        bargainirgPlanVo.setRetailPrice((BigDecimal) Optional.ofNullable(o[10]).orElse(BigDecimal.ZERO));
        bargainirgPlanVo.setId((Long)o[11]);
        return bargainirgPlanVo;
    }

    @Override
    public TrafficPlanActivity findByUser(Long userId, Long id) {
        return baseDao.get("FROM TrafficPlanActivity as ta WHERE ta.userId = ? AND ta.id = ?", new Object[]{userId, id});
    }

    @Override
    public TrafficPlanActivityVo getAvailable(String sql, Object[] objects) {
//        plan_activity.isActive, plan_activity.lowPrice, plan_activity.trafficplanId, "
//                +"plan_activity.limitNumber, plan_activity.startTime, plan_activity.endTime, plan_activity.userId
        Object[] o = (Object[]) baseDao.get(sql, objects);
        TrafficPlanActivityVo trafficPlanActivityVo = new TrafficPlanActivityVo();
        trafficPlanActivityVo.setIsActive((Boolean) Optional.ofNullable(o[0]).orElse(Boolean.TRUE));
        trafficPlanActivityVo.setLowPrice((BigDecimal) Optional.ofNullable(o[1]).orElse(BigDecimal.ZERO));
        trafficPlanActivityVo.setTrafficPlanId((Long) Optional.of(o[2]).get());
        trafficPlanActivityVo.setLimitNumber((Integer) Optional.ofNullable(o[3]).orElse(0));
        trafficPlanActivityVo.setStartTime((Date) Optional.ofNullable(o[4]).orElse(new Date()));
        trafficPlanActivityVo.setEndTime((Date) Optional.ofNullable(o[5]).orElse(new Date()));
        trafficPlanActivityVo.setUserId((Long) Optional.of(o[6]).get());
        trafficPlanActivityVo.setId((Long) Optional.of(o[7]).get());
        return  trafficPlanActivityVo;
    }

    @Override
    public BargainirgPlanVo get(String sql, Object[] objects) {
        return baseDao.find(sql, objects).parallelStream().map(p -> generator((Object[]) p)).collect(Collectors.toList()).get(0);
    }
}
