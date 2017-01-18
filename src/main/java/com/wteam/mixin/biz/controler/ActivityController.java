package com.wteam.mixin.biz.controler;

import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.define.IValueObject;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.po.ActivityBusiness;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.UserVo;
import lombok.Data;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * Created by zbin on 17/1/17.
 */
@RestController
@RequestMapping("/business")
public class ActivityController {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private IBaseDao baseDao;

    @RequestMapping(value="/preselection", method={RequestMethod.GET})
    public ResultMessage preSelecttion(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                                   ResultMessage resultMessage){

        String sql = "SELECT user.userId, user.tel FROM UserPo as user, BusinessInfoPo as business WHERE user.userId = business.businessId AND user.userId != ? AND user.userId NOT IN (SELECT ab.userId FROM ActivityBusiness as ab )";
        List<UserVo> preselectionUsers = baseDao.find(sql, new Object[]{user.getUserId()})
                .parallelStream().map(u -> {
                    UserVo userVo = new UserVo();
                    Object[] o = (Object[]) u;
                    userVo.setUserId((Long) o[0]);
                    userVo.setTel(o[1].toString());
                    return userVo;
                }).collect(Collectors.toList());
        return resultMessage.setSuccessInfo("").putParam("preselectionIds", preselectionUsers);
    }

    @RequestMapping(value="/bargainirg/create", method={RequestMethod.POST})
    public ResultMessage createBusinessInBargainirg(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                                                    @RequestParam("userId") Long userId,
                                   ResultMessage resultMessage){
        ActivityBusiness activityBusiness = new ActivityBusiness();
        activityBusiness.setActiceId(1L);
        activityBusiness.setUserId(userId);
        activityBusiness.setCreatedAt(new Date());
        activityBusiness.setUpdatedAt(new Date());
        activityBusiness.setActive(true);
        baseDao.save(activityBusiness);
        baseDao.flush();
        return resultMessage.setSuccessInfo("创建成功");
    }

    @RequestMapping(value="/bargainirg/business/index", method={RequestMethod.POST})
    public ResultMessage list(@RequestParam("pageNo") Integer pageNo,
                              @RequestParam("pageSize") Integer pageSize,
                                ResultMessage resultMessage){

        String sql = "SELECT user.userId, user.account, user.createTime, user.email, user.isDelete, "
                    +"user.passSalt, user.password, user.principal, user.refreshtoken, user.tel, "
                    +"user.tokenCreateTime, user.tokenSalt, user.tokenValidity, ab.is_active FROM UserPo as user INNER JOIN "
                    +"ActivityBusiness as ab ON user.userId = ab.user_id";
        List<BargainirgUser> bargainirgUserList =  baseDao.get(sql, new Object[]{});
        return resultMessage.setSuccessInfo("").putParam("bargainirgUserList", bargainirgUserList);
    }

    @Data
    static class BargainirgUser extends UserPo implements IValueObject {

        Long userId;
    }


}
