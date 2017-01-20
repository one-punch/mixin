package com.wteam.mixin.biz.controler;

import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.service.ITrafficPlanActivitiesService;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.vo.ActivityPlanVo;
import com.wteam.mixin.model.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by zbin on 17/1/20.
 */
@RestController
@RequestMapping("/traffic_plan_activities")
public class BusinesssActivityController {

    @Autowired
    ITrafficPlanActivitiesService trafficPlanActivitiesService;

    @RequestMapping(value="/index", method={RequestMethod.GET})
    public ResultMessage index(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                               @RequestParam("pageNo") Integer pageNo,
                               @RequestParam("pageSize") Integer pageSize,
                                       ResultMessage resultMessage) {
        return resultMessage.setSuccessInfo("成功");
    }

    @RequestMapping(value="/preselectids", method={RequestMethod.GET})
    public ResultMessage preSelectPlan(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                                       ResultMessage resultMessage){
        List<ActivityPlanVo> activityPlanVoList =  trafficPlanActivitiesService.preSelectPlan(user.getUserId());
        return resultMessage.setSuccessInfo("成功").putParam("preIds", activityPlanVoList);
    }
}
