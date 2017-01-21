package com.wteam.mixin.biz.controler;

import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.service.ITrafficPlanActivitiesService;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.po.TrafficPlanActivity;
import com.wteam.mixin.model.vo.ActivityPlanVo;
import com.wteam.mixin.model.vo.TrafficPlanActivityVo;
import com.wteam.mixin.model.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.Date;
import java.util.List;
import java.util.Optional;

import static javax.persistence.GenerationType.IDENTITY;

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

    @RequestMapping(value="/add", method={RequestMethod.POST})
    public ResultMessage addProduct(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                                    @ModelAttribute("trafficPlan_activity") TrafficPlanActivityVo trafficPlanActivityVo,
                                    ResultMessage resultMessage){
        TrafficPlanActivity trafficPlanActivity = trafficPlanActivitiesService.create(trafficPlanActivityVo, user);
        boolean success = false;
        if(Optional.of(trafficPlanActivity.getId()).isPresent()){
            success = true;
        }
        return resultMessage.setSuccessInfo("成功").putParam("code", success ? 0 : 1)
                .putParam("msg", success ? "新建成功" : "新建失败,请检查必填参数");
    }



}
