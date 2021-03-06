package com.wteam.mixin.biz.service;

import com.wteam.mixin.model.po.ActivityBusiness;
import com.wteam.mixin.model.po.Bargainirg;
import com.wteam.mixin.model.po.TrafficPlanActivity;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.model.vo.TrafficPlanActivityVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.pagination.Pagination;

import java.util.List;

/**
 * Created by cps on 17-1-20.
 */
public interface IBargainirgService {

    List<UserVo> activityBusinessIds(Long userId);

    ActivityBusiness create(Long userId);

    Pagination getActivityBusinessByPage(Object[] params, Integer pageNo, Integer pageSize);

    ActivityBusiness findActivityBusinessById(Long id);

    Bargainirg findById(Long id);

    ActivityBusiness update(ActivityBusiness activityBusiness);

    Bargainirg createByOrder(CustomerOrderVo order, TrafficPlanActivityVo trafficPlanActivity);
}
