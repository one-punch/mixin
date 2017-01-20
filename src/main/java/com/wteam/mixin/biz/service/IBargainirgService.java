package com.wteam.mixin.biz.service;

import com.wteam.mixin.model.po.ActivityBusiness;
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

    ActivityBusiness findById(Long id);

    ActivityBusiness update(ActivityBusiness activityBusiness);
}
