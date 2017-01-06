package com.wteam.mixin.biz.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.service.ITemporaryService;
import com.wteam.mixin.constant.ProductType;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.utils.Utils;



/**
 * 订单模块业务实现类
 * @version 1.0
 * @author benko
 * @time
 *
 */
@Service("temporaryService")
public class TemporaryServiceImpl implements ITemporaryService {

    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(TemporaryServiceImpl.class.getName());

    @Autowired
    IBaseDao baseDao;

    @Autowired
    DozerBeanMapper mapper;

    @Override
    public void addHaoduanToOrders() {
        List<CustomerOrderPo> list = baseDao.find("from CustomerOrderPo where haoduan is null and phone is not null and productType=?",new Object[]{ProductType.Traffic});
        list.forEach(po -> {
            String[] haoduan = Utils.getInfoByPhone(po.getPhone());
            po.setHaoduan(String.format("%s-%s-%s", haoduan[0],haoduan[2],haoduan[1]));
            baseDao.update(po);
        });

    }

}
