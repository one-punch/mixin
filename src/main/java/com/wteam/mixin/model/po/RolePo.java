package com.wteam.mixin.model.po;
// Generated 2016-4-6 23:48:57 by Hibernate Tools 4.3.1.Final


import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.define.IPersistentObject;


/**
 * 角色
 * @author benko
 * @version 2016年6月12日
 * @see RolePo
 * @since
 */
@Entity
@Table(name = "t_role")
public class RolePo implements java.io.Serializable, IPersistentObject {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /** 角色ID */
    private Long roleId;
    /** 角色类型  */
    private RoleType role;
    /** 描述 */
    private String description;

    /** 关联的用户 */
    private Set<UserPo> users = new HashSet<UserPo>(0);
    /** 关联的用户 */
    private Set<PermissionPo> permissions = new HashSet<PermissionPo>(0);

    public RolePo() {}

    public RolePo(RoleType role) {
        this.role = role;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "roleId", unique = true, nullable = false)
    public Long getRoleId() {
        return this.roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    @Column(name = "role", unique = true, nullable = false)
    @Enumerated(EnumType.STRING)
    public RoleType getRole() {
        return this.role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    @Column(name = "description")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    /* ************************************************************************************
     *
     *                                 下面是外键关联
     *
     * ************************************************************************************ */

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_user_t_role", joinColumns = {
        @JoinColumn(name = "t_roleId", nullable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "t_userId", nullable = false, updatable = false)})
    public Set<UserPo> getUsers() {
        return users;
    }

    public void setUsers(Set<UserPo> users) {
        this.users = users;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "t_role_t_permission", joinColumns = {
        @JoinColumn(name = "t_roleId", nullable = false, updatable = false)}, inverseJoinColumns = {
            @JoinColumn(name = "t_permissionId", nullable = false, updatable = false)})
    public Set<PermissionPo> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<PermissionPo> permissions) {
        this.permissions = permissions;
    }



}
