package com.wteam.mixin.biz.dao;

import com.wteam.mixin.model.po.TrafficPlanActivity;
import com.wteam.mixin.model.vo.ActivityPlanVo;
import com.wteam.mixin.model.vo.BargainirgPlanVo;
import com.wteam.mixin.model.vo.TrafficPlanActivityVo;

import java.util.List;

/**
 * Created by zbin on 17/1/20.
 */
public interface ITrafficPlanActivitiesDao {

    List<ActivityPlanVo> preSelectPlan(String sql, Long businessId);

    List<BargainirgPlanVo> getList(String sql, Object[] params, Integer pageNo, Integer pageSize);

    TrafficPlanActivity findByUser(Long userId, Long id);

    TrafficPlanActivityVo getAvailable(String sql, Object[] objects);

    BargainirgPlanVo get(String s, Object[] objects);

    Long recordCount(Long id);
}
