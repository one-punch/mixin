package com.wteam.mixin.biz.service.impl;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.ITrafficPlanActivitiesDao;
import com.wteam.mixin.biz.dao.impl.TrafficPlanActivitiesDaoImpl;
import com.wteam.mixin.biz.service.ITrafficPlanActivitiesService;
import com.wteam.mixin.model.po.TrafficPlanActivity;
import com.wteam.mixin.model.vo.ActivityPlanVo;
import com.wteam.mixin.model.vo.BargainirgPlanVo;
import com.wteam.mixin.model.vo.TrafficPlanActivityVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.pagination.Pagination;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
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
                +"( SELECT activityPlan.trafficplanId FROM TrafficPlanActivity as activityPlan WHERE "
                +" activityPlan.userId = ? )";
        String orderBy = "";
        return trafficPlanActivitiesDao.preSelectPlan(sql, businessId);
    }


    public TrafficPlanActivity create(TrafficPlanActivityVo trafficPlanActivityVo, UserVo user){
        TrafficPlanActivity trafficPlanActivity = new TrafficPlanActivity();
        trafficPlanActivity.setUpdatedAt(new Date());
        trafficPlanActivity.setTrafficplanId(trafficPlanActivityVo.getTrafficPlanId());
        trafficPlanActivity.setCreatedAt(new Date());
        trafficPlanActivity.setActive(Optional.ofNullable(trafficPlanActivityVo.getIsActive()).orElse(false));
        trafficPlanActivity.setUserId(user.getUserId());
        trafficPlanActivity.setActiveId(1L);
        trafficPlanActivity.setLowPrice(trafficPlanActivityVo.getLowPrice());
        trafficPlanActivity.setLimitNumber(trafficPlanActivityVo.getLimitNumber());
        trafficPlanActivity.setStartTime(trafficPlanActivityVo.getStartTime());
        trafficPlanActivity.setEndTime(trafficPlanActivityVo.getEndTime());
        baseDao.save(trafficPlanActivity);
        return trafficPlanActivity;
    }


    public List<BargainirgPlanVo> getList(Long userId, Integer pageNo, Integer pageSize){
        String sql = "SELECT plan.name, plan.cost, plan.provider, plan.apiProvider, planGroup.province, activity_plan.isActive, "
                +"activity_plan.startTime, activity_plan.endTime, activity_plan.lowPrice, activity_plan.limitNumber, "
                +"business_plan.retailPrice FROM TrafficPlanPo AS plan, TrafficPlanActivity AS activity_plan, "
                +"BusinessTrafficPlanPo AS business_plan, TrafficGroupPo AS planGroup "
                +"WHERE activity_plan.trafficplanId = plan.id AND plan.trafficGroupId = planGroup.id AND "
                +"business_plan.trafficplanId = plan.id AND plan.isDelete = 0"
                +"AND business_plan.businessId = ? ";
        String orderBy = "ORDER BY activity_plan.createdAt DESC";
        return trafficPlanActivitiesDao.getList(sql + orderBy, new Object[]{userId}, pageNo, pageSize);
    }
}
