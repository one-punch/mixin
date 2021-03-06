package com.wteam.mixin.biz.service.impl;

import com.wteam.mixin.biz.dao.IBargainirgDao;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.service.IBargainirgService;
import com.wteam.mixin.model.po.ActivityBusiness;
import com.wteam.mixin.model.po.Bargainirg;
import com.wteam.mixin.model.po.TrafficPlanActivity;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.model.vo.TrafficPlanActivityVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.pagination.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by cps on 17-1-20.
 */
@Service("bargainirgService")
public class BargainirgServiceImpl implements IBargainirgService {
    @Autowired
    private IBaseDao baseDao;

    @Autowired
    private IBargainirgDao bargainirgDao;


    public List<UserVo> activityBusinessIds(Long userId){
        String sql = "SELECT user.userId, user.tel FROM UserPo as user, BusinessInfoPo as business WHERE "
                    +"user.userId = business.businessId AND business.isDelete=0 AND user.userId != ? AND "
                    +"user.userId NOT IN (SELECT ab.userId FROM ActivityBusiness as ab )";
        return bargainirgDao.activityBusinessIds(sql, new Object[]{userId});
    }

    public ActivityBusiness create(Long userId){
        return bargainirgDao.created(userId);
    }

    public Pagination getActivityBusinessByPage(Object[] params, Integer pageNo, Integer pageSize){
        String columns = "user.userId, user.tel, user.account, business.wechatOfficAccount, activityBusiness.isActive, activityBusiness.id ";
        String condition = "FROM UserPo as user, BusinessInfoPo as business, ActivityBusiness as activityBusiness "
                    +"WHERE user.userId = activityBusiness.userId AND business.businessId=user.userId "
                    +"AND business.isDelete=0";
        String orderBy = " ORDER BY activityBusiness.createdAt DESC";
        Long count = baseDao.getOnly("SELECT COUNT(*) " + condition);
        Pagination page = new Pagination(pageNo, pageSize, count.intValue());
        page.setList(bargainirgDao.getActivityBusinessList("SELECT " + columns + condition + orderBy, params, pageNo, pageSize));
        return page;
    }

    public ActivityBusiness findActivityBusinessById(Long id){
        return baseDao.find(ActivityBusiness.class, id);
    }

    public Bargainirg findById(Long id){
        return baseDao.find(Bargainirg.class, id);
    }

    public ActivityBusiness update(ActivityBusiness activityBusiness){
        baseDao.update(activityBusiness);
        return activityBusiness;
    }

    @Override
    public Bargainirg createByOrder(CustomerOrderVo order, TrafficPlanActivityVo trafficPlanActivity) {
        Bargainirg bargainirg = new Bargainirg();
        bargainirg.setUpdatedAt(new Date());
        bargainirg.setCreatedAt(new Date());
        bargainirg.setCustomerId(order.getCustomerId());
        bargainirg.setCustomerOrderId(order.getId());
        bargainirg.setState(Bargainirg.State.INIT);
        bargainirg.setTrafficPlanActivityId(trafficPlanActivity.getId());
        baseDao.save(bargainirg);
        return bargainirg;
    }


}
