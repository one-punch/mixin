package com.wteam.mixin.biz.dao;

import com.wteam.mixin.model.vo.BargainirgUserVo;
import com.wteam.mixin.model.vo.UserVo;

import java.util.List;

/**
 * Created by cps on 17-1-20.
 */
public interface IBargainirgDao {

    List<UserVo> activityBusinessIds(String sql, Object[] params);

    ActivityBusiness created(Long userId);

    List<BargainirgUserVo> getActivityBusinessList(String sql, Object[] params, Integer pageNo, Integer pageSize);

}
