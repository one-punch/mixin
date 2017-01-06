/*
 * 文件名：Permission.java
 * 版权：Copyright by wteam团队
 * 描述：
 * 修改人：benko
 * 修改时间：2016年6月12日 下午11:14:43
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.wteam.mixin.model.po;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.wteam.mixin.define.IPersistentObject;

/**
 *
 *
 * @author benko
 * @version 2016年6月12日
 * @see PermissionPo
 * @since
 */
@Entity
@Table(name = "t_permission")
public class PermissionPo implements IPersistentObject, Serializable {
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
    /** 关联的用户 */
    private Set<RolePo> roles = new HashSet<RolePo>(0);


    public PermissionPo() { }


    public PermissionPo(String resourcesUrl) {
        this.resourcesUrl = resourcesUrl;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "permissionId", unique = true, nullable = false)
    public Long getPermissionId() {
        return permissionId;
    }
    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }
    @Column(name = "name")
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    @Column(name = "description")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    @Column(name = "resourcesUrl", unique = true, nullable = false)
    public String getResourcesUrl() {
        return resourcesUrl;
    }
    public void setResourcesUrl(String resourcesUrl) {
        this.resourcesUrl = resourcesUrl;
    }
    @Column(name = "isDelete")
    @Type(type = "boolean")
    public boolean getIsDelete() {
        return this.isDelete;
    }
    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "createTime", length = 19)
    @CreationTimestamp
    public Date getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /* ************************************************************************************
     *
     *                                 下面是外键关联
     *
     * ************************************************************************************ */

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_role_t_permission", joinColumns = {
        @JoinColumn(name = "t_permissionId", nullable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "t_roleId", nullable = false, updatable = false)})
    public Set<RolePo> getRoles() {
        return roles;
    }
    public void setRoles(Set<RolePo> roles) {
        this.roles = roles;
    }

}
