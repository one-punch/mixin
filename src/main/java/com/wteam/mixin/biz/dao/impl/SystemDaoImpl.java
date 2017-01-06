package com.wteam.mixin.biz.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.ISystemDao;
import com.wteam.mixin.model.po.DConfigPo;
import com.wteam.mixin.model.po.PlatformInfoPo;

/**
 * 对应 PlatformInfoPo 和 DConfigPO
 * @author benko
 *
 */
@Repository("systemDao")
public class SystemDaoImpl implements ISystemDao{

    @Autowired
    IBaseDao baseDao;

    @Override
    public String platfromInfo(String name) {
        PlatformInfoPo infoPo = baseDao.findUniqueByProperty("name", name, PlatformInfoPo.class);
        return infoPo != null? infoPo.getValue() : null;
    }

    @Override
    public String dconfig(String name) {
        DConfigPo configPo = baseDao.findUniqueByProperty("name", name, DConfigPo.class);
        return configPo != null? configPo.getValue() : null;
    }

    @Override
    public String platfromInfo(String name, String value) {
        PlatformInfoPo infoPo = baseDao.findUniqueByProperty("name", name, PlatformInfoPo.class);
        if (infoPo == null) {
            infoPo = new PlatformInfoPo(name, value);
        }
        infoPo.setValue(value);
        baseDao.saveOrUpdate(infoPo);
        return value;
    }

    @Override
    public String dconfig(String name, String value) {
        DConfigPo configPo = baseDao.findUniqueByProperty("name", name, DConfigPo.class);
        if (configPo == null) {
            configPo = new DConfigPo(name, value);
        }
        configPo.setValue(value);
        baseDao.saveOrUpdate(configPo);
        return value;
    }

}
