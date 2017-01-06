/*
 * 文件名：PermissionVo.java
 * 版权：Copyright by wteam团队
 * 描述：
 * 修改人：benko
 * 修改时间：2016年6月12日 下午11:56:37
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.wteam.mixin.model.vo;


import java.io.Serializable;
import java.util.Date;

import com.wteam.mixin.define.IValueObject;


/**
 * 权限
 *
 * @author benko
 * @version 2016年6月12日
 * @see PermissionVo
 * @since
 */
public class PermissionVo implements IValueObject, Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** 权限ID */
    private Long permissionId;

    /** 权限名 */
    private String name;
    /** 描述 */
    private String description;
    /** 权限 */
    private String resourcesUrl;

    /** 是否删除 */
    private boolean isDelete;
    /** 创建时间 */
    private Date createTime;

    public PermissionVo() {}

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getResourcesUrl() {
        return resourcesUrl;
    }

    public void setResourcesUrl(String resourcesUrl) {
        this.resourcesUrl = resourcesUrl;
    }
}
