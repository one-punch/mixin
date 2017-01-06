package com.wteam.mixin.biz.service.impl;


import org.dozer.DozerBeanMapper;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.service.IUploadService;
import com.wteam.mixin.model.po.UploadFilePo;
import com.wteam.mixin.model.vo.UploadFileVo;


/**
 * @version 1.0
 * @author benko
 */
@Service("uploadService")
public class UploadServiceImpl implements IUploadService {

    /** 基本dao. */
    @Autowired
    private IBaseDao baseDao;
    /** 对象转换器. */
    @Autowired
    private DozerBeanMapper mapper;

    @Override
    public UploadFileVo save(UploadFileVo uploadVo) {

        UploadFilePo uploadfilePo = mapper.map(uploadVo, UploadFilePo.class);
        baseDao.save(uploadfilePo);

        return mapper.map(uploadfilePo, UploadFileVo.class);
    }

    @Override
    public UploadFileVo get(Long fid) {
        UploadFilePo uploadfilePo = baseDao.findUniqueByProperty("uploadId", fid,
            UploadFilePo.class);
        if (uploadfilePo == null) throw new ServiceException("找不到此文件");
        return mapper.map(uploadfilePo, UploadFileVo.class);
    }

}
