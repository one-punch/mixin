package com.wteam.mixin.biz.service.impl;

import com.wteam.mixin.biz.dao.IBargainirgRecordDao;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.service.IBargainirgRecordService;
import com.wteam.mixin.biz.service.ITrafficPlanActivitiesService;
import com.wteam.mixin.model.po.Bargainirg;
import com.wteam.mixin.model.po.BargainirgRecord;
import com.wteam.mixin.model.po.CustomerInfoPo;
import com.wteam.mixin.model.po.TrafficPlanActivity;
import com.wteam.mixin.model.vo.BargainirgPlanVo;
import com.wteam.mixin.model.vo.CustomerRecordVo;
import com.wteam.mixin.model.vo.UserVo;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

/**
 * Created by zbin on 17/1/29.
 */
@Service("bargainirgRecordService")
public class BargainirgRecordServiceImpl implements IBargainirgRecordService {

    @Autowired
    IBaseDao baseDao;

    @Autowired
    IBargainirgRecordDao bargainirgRecordDao;

    @Autowired
    ITrafficPlanActivitiesService trafficPlanActivitiesService;

    @Override
    public BargainirgRecord queryByCustomer(Long bargainirgId, Long userId) {
        String sql = "FROM BargainirgRecord AS record WHERE record.bargainirgId = ? AND record.customerId = ? ";
        return baseDao.get(sql, new Object[]{bargainirgId, userId});
    }

    @Override
    public BargainirgRecord doCut(Bargainirg bargainirg, TrafficPlanActivity trafficPlanActivity, UserVo user) {
        BargainirgPlanVo bargainirgPlanVo = trafficPlanActivitiesService.get(trafficPlanActivity.getId());
        float discount = ThreadLocalRandom.current().nextFloat() * bargainirgPlanVo.getRetailPrice().subtract(trafficPlanActivity.getLowPrice()).floatValue();
        return bargainirgRecordDao.create(bargainirg, trafficPlanActivity, user, discount);
    }

    @Override
    public List<CustomerRecordVo> getList(Long bargainirgId) {
        String sql = "SELECT customer.nickname, customer.headimgurl, record.discount, record.createdAt, customer.customerId " +
                "FROM CustomerInfoPo AS customer, BargainirgRecord AS record " +
                "WHERE record.bargainirgId = ? AND customer.customerId = record.customerId " +
                "AND customer.isDelete = 0 ORDER BY record.createdAt DESC ";
        return baseDao.find(sql, new Object[]{bargainirgId})
                .parallelStream().map(p -> {
            CustomerRecordVo customerRecordVo = new CustomerRecordVo();
            Object[] o = (Object[]) p;
            customerRecordVo.setNickname(Optional.ofNullable(o[0]).orElse("").toString());
            customerRecordVo.setHeadimgurl(Optional.ofNullable(o[1]).orElse("").toString());
            customerRecordVo.setDiscount((BigDecimal) Optional.ofNullable(o[2]).orElse(BigDecimal.ZERO));
            customerRecordVo.setCreatedAt((Date) Optional.ofNullable(o[3]).orElse(new Date()));
            customerRecordVo.setId((Long) Optional.of(o[4]).get());
            return customerRecordVo;
        }).collect(Collectors.toList());
    }

}
