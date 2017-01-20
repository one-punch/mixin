package com.wteam.mixin.biz.service;

import com.wteam.mixin.model.vo.ActivityPlanVo;

import java.util.List;

/**
 * Created by zbin on 17/1/20.
 */
public interface ITrafficPlanActivitiesService {

    List<ActivityPlanVo> preSelectPlan(Long businessId);
}
