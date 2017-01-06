/*
 * 文件名：IPermissionService.java
 * 版权：Copyright by wteam团队
 * 描述：
 * 修改人：benko
 * 修改时间：2016年6月12日 下午11:47:39
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.wteam.mixin.biz.service;

import java.util.Set;

/**
 *
 * @author benko
 * @version 2016年6月12日
 * @see IPermissionService
 * @since
 */
public interface IPermissionService {

    /**
     * 查找所有的没有被标志为删除的权限
     *
     * @return Set
     * @see
     */
    Set<String> findAll();
}
