package com.wteam.mixin.biz.dao.impl;

import com.wteam.mixin.biz.dao.IBargainirgRecordDao;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.model.po.Bargainirg;
import com.wteam.mixin.model.po.BargainirgRecord;
import com.wteam.mixin.model.po.TrafficPlanActivity;
import com.wteam.mixin.model.vo.UserVo;
import org.hibernate.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 * Created by zbin on 17/1/30.
 */
@Repository("bargainirgRecordDao")
public class BargainirgRecordDaoImpl implements IBargainirgRecordDao {

    @Autowired
    IBaseDao baseDao;

    @Override
    public BargainirgRecord create(Bargainirg bargainirg, TrafficPlanActivity trafficPlanActivity, UserVo user, float discount) {
        Query query = baseDao.getSession().createSQLQuery("INSERT INTO bargainirg_record (createdAt, updatedAt, bargainirg_id, customer_id, discount) " +
                "SELECT * FROM (SELECT NOW() AS createdAt, NOW() AS updatedAt, :bargainirg_id AS bargainirg_id, :customer_id AS customer_id, :discount AS discount ) AS tmp " +
                "WHERE NOT EXISTS (" +
                "    SELECT b1.customer_id FROM bargainirg_record AS b1 WHERE b1.bargainirg_id = :check_bargainirg_id AND b1.customer_id = :check_customer_id " +
                ")");
        query.setParameter("bargainirg_id", bargainirg.getId());
        query.setParameter("customer_id", user.getUserId());
        query.setParameter("discount", discount);
        query.setParameter("check_bargainirg_id", bargainirg.getId());
        query.setParameter("check_customer_id", user.getUserId());
        query.executeUpdate();
        return baseDao.get("FROM BargainirgRecord WHERE bargainirg_id = ? AND customer_id = ? ", new Object[]{bargainirg.getId(), user.getUserId()});
    }
}
