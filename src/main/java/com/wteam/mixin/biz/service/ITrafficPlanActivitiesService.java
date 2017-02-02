package com.wteam.mixin.biz.service;

import com.wteam.mixin.model.po.TrafficPlanActivity;
import com.wteam.mixin.model.vo.*;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

/**
 * Created by zbin on 17/1/20.
 */
public interface ITrafficPlanActivitiesService {

    List<ActivityPlanVo> preSelectPlan(Long businessId);

    TrafficPlanActivity create(TrafficPlanActivityVo trafficPlanActivityVo, UserVo user);

    List<BargainirgPlanVo> getList(Long userId, Integer pageNo, Integer pageSize);

    TrafficPlanActivity findByUser(Long userId, Long id);

    void update(TrafficPlanActivity trafficPlanActivity);

    boolean allowBargainirg(CustomerOrderVo order);

    TrafficPlanActivityVo getAvailable(CustomerOrderVo order);

    BargainirgPlanVo get(Long activityPlanId);
}
