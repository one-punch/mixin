/*
 * 文件名：IPermissionDao.java
 * 版权：Copyright by wteam团队
 * 描述：
 * 修改人：benko
 * 修改时间：2016年6月13日 上午10:32:30
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.wteam.mixin.biz.dao;

import java.util.List;

import com.wteam.mixin.model.po.PermissionPo;

/**
 * 权限dao
 *
 * @author benko
 * @version 2016年6月13日
 * @see IPermissionDao
 * @since
 */
public interface IPermissionDao {

    /**
     * 根据用户的唯一标志查找权限
     * @param principal 用户的唯一标志
     * @return list
     * @see
     */
    List<PermissionPo> findByUserPrincipal(String principal);
}
