package com.wteam.mixin.biz.service;

import com.wteam.mixin.model.po.Bargainirg;
import com.wteam.mixin.model.po.BargainirgRecord;
import com.wteam.mixin.model.po.TrafficPlanActivity;
import com.wteam.mixin.model.vo.CustomerRecordVo;
import com.wteam.mixin.model.vo.UserVo;

import java.util.List;

/**
 * Created by zbin on 17/1/29.
 */
public interface IBargainirgRecordService {
    BargainirgRecord queryByCustomer(Long bargainirgId, Long userId);

    BargainirgRecord doCut(Bargainirg bargainirg, TrafficPlanActivity trafficPlanActivity, UserVo user);

    List<CustomerRecordVo> getList(Long bargainirgId);
}
