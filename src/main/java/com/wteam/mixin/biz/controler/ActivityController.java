package com.wteam.mixin.biz.controler;

import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.service.IBargainirgService;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.ActivityBusiness;
import com.wteam.mixin.model.vo.BargainirgUserVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by zbin on 17/1/17.
 */
@RestController
@RequestMapping("/business")
public class ActivityController {

    @Autowired
    private IBargainirgService bargainirgService;

    @RequestMapping(value="/preselection", method={RequestMethod.GET})
    public ResultMessage preSelecttion(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                                   ResultMessage resultMessage){
        if(user == null){
            throw new ServiceException("请先登录");
        }
        List<UserVo> preselectionUsers = bargainirgService.activityBusinessIds(user.getUserId());
        return resultMessage.setSuccessInfo("成功").putParam("preselectionIds", preselectionUsers);
    }

    @RequestMapping(value="/bargainirg/create", method={RequestMethod.POST})
    public ResultMessage createBusinessInBargainirg(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                                                    @RequestParam("userId") Long userId,
                                   ResultMessage resultMessage){
        ActivityBusiness activityBusiness = bargainirgService.create(userId);
        return resultMessage.setSuccessInfo("成功").putParam("resultCode", activityBusiness.getId()).putParam("msg", "创建成功");
    }

    @RequestMapping(value="/bargainirg/index", method={RequestMethod.GET})
    public ResultMessage list(@RequestParam("pageNo") Integer pageNo,
                              @RequestParam("pageSize") Integer pageSize,
                                ResultMessage resultMessage){
        Pagination page = bargainirgService.getActivityBusinessByPage(new Object[]{}, pageNo, pageSize);
        return resultMessage.setSuccessInfo("成功").putParam("page", page);
    }


    @RequestMapping(value="/bargainirg/edit", method={RequestMethod.POST})
    public ResultMessage edit(@ModelAttribute("activity_business") BargainirgUserVo bargainirgUserVo,
                              ResultMessage resultMessage) {

        ActivityBusiness baseActivityBusiness = bargainirgService.findActivityBusinessById(bargainirgUserVo.getId());
        if (baseActivityBusiness == null) {
            throw new ServiceException("该条件不存在");
        }
        baseActivityBusiness.setActive(bargainirgUserVo.getIsActive());
        baseActivityBusiness.setUpdatedAt(new Date());
        baseActivityBusiness = bargainirgService.update(baseActivityBusiness);
        bargainirgUserVo.setIsActive(baseActivityBusiness.isActive());
        return resultMessage.setSuccessInfo("成功").putParam("activity_business", bargainirgUserVo);
    }

}
