package com.wteam.mixin.biz.dao;

import com.wteam.mixin.model.po.Bargainirg;
import com.wteam.mixin.model.po.BargainirgRecord;
import com.wteam.mixin.model.po.TrafficPlanActivity;
import com.wteam.mixin.model.vo.UserVo;

/**
 * Created by zbin on 17/1/30.
 */
public interface IBargainirgRecordDao {
    BargainirgRecord create(Bargainirg bargainirg, TrafficPlanActivity trafficPlanActivity, UserVo user, float discount);
}
