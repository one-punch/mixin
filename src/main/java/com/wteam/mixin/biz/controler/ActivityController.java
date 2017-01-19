package com.wteam.mixin.biz.controler;

import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.define.IValueObject;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.ActivityBusiness;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.BargainirgUserVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.pagination.Pagination;
import lombok.Data;
import org.dozer.DozerBeanMapper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
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

        String sql = "SELECT user.userId, user.tel FROM UserPo as user, BusinessInfoPo as business WHERE "
                    +"user.userId = business.businessId AND business.isDelete=0 AND user.userId != ? AND "
                    +"user.userId NOT IN (SELECT ab.userId FROM ActivityBusiness as ab )";
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
        return resultMessage.setSuccessInfo("成功").putParam("resultCode", activityBusiness.getId()).putParam("msg", "创建成功");
    }

    @RequestMapping(value="/bargainirg/index", method={RequestMethod.GET})
    public ResultMessage list(@RequestParam("pageNo") Integer pageNo,
                              @RequestParam("pageSize") Integer pageSize,
                                ResultMessage resultMessage){

        String columns = "user.userId, user.tel, user.account, business.wechatOfficAccount, activityBusiness.isActive, activityBusiness.id ";
        String condition = "FROM UserPo as user, BusinessInfoPo as business, ActivityBusiness as activityBusiness "
                    +"WHERE user.userId = activityBusiness.userId AND business.businessId=user.userId "
                    +"AND business.isDelete=0";
        String orderBy = " ORDER BY activityBusiness.createdAt DESC";
        Long count = baseDao.getOnly("SELECT COUNT(*) " + condition);
        Pagination page = new Pagination(pageNo, pageSize, count.intValue());
        List<BargainirgUserVo> bargainirgUserVoList =  baseDao.find("SELECT " + columns + condition + orderBy, new Object[]{}, pageNo, pageSize)
                            .parallelStream()
                            .map(p -> {
                                Object[] result = (Object[]) p;
                                BargainirgUserVo userVo = new BargainirgUserVo();
                                userVo.setUserId((Long)result[0]);
                                userVo.setTel(Optional.ofNullable(result[1]).orElse("").toString());
                                userVo.setAccount(Optional.ofNullable(result[2]).orElse("").toString());
                                userVo.setWechatOfficAccount(Optional.ofNullable(result[3]).orElse("").toString());
                                userVo.setIsActive((Boolean) result[4]);
                                userVo.setId((Long) result[5]);
                                return userVo;
                            }).collect(Collectors.toList());
        page.setList(bargainirgUserVoList);
        return resultMessage.setSuccessInfo("成功").putParam("page", page);
    }


    @RequestMapping(value="/bargainirg/edit", method={RequestMethod.POST})
    public ResultMessage edit(@ModelAttribute("activity_business") BargainirgUserVo bargainirgUserVo,
                              ResultMessage resultMessage) {

        ActivityBusiness baseActivityBusiness = baseDao.find(ActivityBusiness.class, bargainirgUserVo.getId());
        if (baseActivityBusiness == null) {
            throw new ServiceException("该条件不存在");
        }
        baseActivityBusiness.setActive(bargainirgUserVo.getIsActive());
        baseActivityBusiness.setUpdatedAt(new Date());
        Session session = baseDao.getSession();
        session.beginTransaction();
        session.update(baseActivityBusiness);
        session.flush();
        session.getTransaction().commit();
        session.close();
        bargainirgUserVo.setIsActive(baseActivityBusiness.isActive());
        return resultMessage.setSuccessInfo("成功").putParam("activity_business", bargainirgUserVo);
    }


    @Data
    static class BargainirgUser extends UserPo implements IValueObject {

        Long userId;
    }


}
