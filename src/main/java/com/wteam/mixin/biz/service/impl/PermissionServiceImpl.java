/*
 * 文件名：PermissionServiceImpl.java
 * 版权：Copyright by wteam团队
 * 描述：
 * 修改人：benko
 * 修改时间：2016年6月12日 下午11:51:50
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.wteam.mixin.biz.service.impl;

import java.util.Set;
import java.util.stream.Collectors;

import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.service.IPermissionService;
import com.wteam.mixin.model.po.PermissionPo;

/**
 *
 *
 * @author benko
 * @version 2016年6月12日
 * @see PermissionServiceImpl
 * @since
 */
@Service("permissionService")
public class PermissionServiceImpl implements IPermissionService {


    /** 基本dao. */
    @Autowired
    private IBaseDao baseDao;
    /** 对象转换器. */
    @Autowired
    private DozerBeanMapper mapper;

    /* (non-Javadoc)
     * @see com.wteam.smsh.biz.service.IPermissionService#findAll()
     */
    @Override
    public Set<String> findAll() {
        Set<String> permissions = baseDao.findByProperty("isDelete", false, PermissionPo.class).stream()
            .map(po -> po.getResourcesUrl())
            .collect(Collectors.toSet());

        return permissions;
    }

}
