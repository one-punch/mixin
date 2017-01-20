package com.wteam.mixin.biz.service.impl;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.ITrafficPlanActivitiesDao;
import com.wteam.mixin.biz.dao.impl.TrafficPlanActivitiesDaoImpl;
import com.wteam.mixin.biz.service.ITrafficPlanActivitiesService;
import com.wteam.mixin.model.vo.ActivityPlanVo;
import com.wteam.mixin.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by zbin on 17/1/20.
 */
@Service("trafficPlanActivitiesService")
public class TrafficPlanActivitiesServiceImpl implements ITrafficPlanActivitiesService {

    @Autowired
    IBaseDao baseDao;

    @Autowired
    ITrafficPlanActivitiesDao trafficPlanActivitiesDao;


    public Pagination getPage(Object[] params, Integer pageNo, Integer pageSize){
        return null;
    }

    public List<ActivityPlanVo> preSelectPlan(Long businessId){
        String sql = "SELECT plan.id, plan.apiProvider, plan.name FROM TrafficPlanPo as plan, "
                +"BusinessTrafficPlanPo as businessPlan "
                +"WHERE businessPlan.businessId = ? AND businessPlan.trafficplanId = plan.id AND "
                +"businessPlan.isDelete = 0 AND plan.id NOT IN "
                +"( SELECT activityPlan.trafficPalnId FROM TrafficPlanActivity as activityPlan WHERE "
                +" activityPlan.userId = ? )";
        String orderBy = "";
        return trafficPlanActivitiesDao.preSelectPlan(sql, businessId);
    }

}
