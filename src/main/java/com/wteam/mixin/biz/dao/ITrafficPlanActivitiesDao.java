package com.wteam.mixin.biz.dao;

import com.wteam.mixin.model.vo.ActivityPlanVo;

import java.util.List;

/**
 * Created by zbin on 17/1/20.
 */
public interface ITrafficPlanActivitiesDao {

    List<ActivityPlanVo> preSelectPlan(String sql, Long businessId);
}
