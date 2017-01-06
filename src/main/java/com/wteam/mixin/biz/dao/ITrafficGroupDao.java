package com.wteam.mixin.biz.dao;

import java.util.List;

import com.wteam.mixin.model.po.TrafficGroupPo;
import com.wteam.mixin.model.query.IQuery;

/**
 * 流量分组模块
 * @author benko
 */
public interface ITrafficGroupDao {

    List<TrafficGroupPo> findList(IQuery groupQuery);
}
