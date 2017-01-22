package com.wteam.mixin.biz.dao;

import com.wteam.mixin.model.po.TrafficPlanActivity;
import com.wteam.mixin.model.vo.ActivityPlanVo;
import com.wteam.mixin.model.vo.BargainirgPlanVo;

import java.util.List;
import java.util.Map;

/**
 * Created by zbin on 17/1/20.
 */
public interface ITrafficPlanActivitiesDao {

    List<ActivityPlanVo> preSelectPlan(String sql, Long businessId);

    List<BargainirgPlanVo> getList(String sql, Object[] params, Integer pageNo, Integer pageSize);

    TrafficPlanActivity findByUser(Long userId, Long id);
}
