package com.wteam.mixin.biz.dao.impl;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.ITrafficPlanActivitiesDao;
import com.wteam.mixin.model.vo.ActivityPlanVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

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
}
