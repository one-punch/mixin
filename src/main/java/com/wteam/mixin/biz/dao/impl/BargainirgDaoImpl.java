package com.wteam.mixin.biz.dao.impl;

import com.wteam.mixin.biz.dao.IBargainirgDao;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.model.po.ActivityBusiness;
import com.wteam.mixin.model.vo.BargainirgUserVo;
import com.wteam.mixin.model.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by cps on 17-1-20.
 */
@SuppressWarnings("unchecked")
@Repository("bargainirgDao")
public class BargainirgDaoImpl implements IBargainirgDao {

    @Autowired
    private IBaseDao baseDao;


    public List<UserVo> activityBusinessIds(String sql, Object[] params){
        return baseDao.find(sql, params)
                .parallelStream().map(u -> {
                    UserVo userVo = new UserVo();
                    Object[] o = (Object[]) u;
                    userVo.setUserId((Long) o[0]);
                    userVo.setTel(o[1].toString());
                    return userVo;
                }).collect(Collectors.toList());
    }

    public ActivityBusiness created(Long userId){
        ActivityBusiness activityBusiness = new ActivityBusiness();
        activityBusiness.setActiceId(1L);
        activityBusiness.setUserId(userId);
        activityBusiness.setCreatedAt(new Date());
        activityBusiness.setUpdatedAt(new Date());
        activityBusiness.setActive(true);
        baseDao.save(activityBusiness);
        baseDao.flush();
        return activityBusiness;
    }

    public List<BargainirgUserVo> getActivityBusinessList(String sql, Object[] params, Integer pageNo, Integer pageSize){
        return baseDao.find(sql, params, pageNo, pageSize)
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
    }


}
