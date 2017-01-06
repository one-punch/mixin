/*
 * 文件名：UserInfoPo.java
 * 版权：Copyright by wteam团队
 * 描述：
 * 修改人：benko
 * 修改时间：2016年6月20日 下午4:31:02
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.wteam.mixin.model.po;

import static javax.persistence.GenerationType.IDENTITY;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.wteam.mixin.define.IPersistentObject;

/**
 * 用户信息
 *
 * @author benko
 * @version 2016年6月20日
 * @see UserInfoPo
 * @since
 */
@Entity
@Table(name = "t_userinfo")
public class UserInfoPo implements IPersistentObject, Serializable {

    /** */
    private static final long serialVersionUID = 1L;


    /** 用户ID */
    private Long id;

    /** 头像 */
    private UploadFilePo headImg;
    /** 关联的用户 */
    private UserPo user;

    public UserInfoPo() { }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /* ************************************************************************************
     *
     *                                 下面是外键关联
     *
     * ************************************************************************************ */

    @OneToOne(fetch = FetchType.LAZY)
    public UploadFilePo getHeadImg() {
        return headImg;
    }
    public void setHeadImg(UploadFilePo headImg) {
        this.headImg = headImg;
    }

    @OneToOne(fetch = FetchType.LAZY)
    public UserPo getUser() {
        return user;
    }

    public void setUser(UserPo user) {
        this.user = user;
    }

}
