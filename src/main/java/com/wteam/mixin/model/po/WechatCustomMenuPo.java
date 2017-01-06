package com.wteam.mixin.model.po;


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.wteam.mixin.define.IPersistentObject;


/**
 * 微信自定义菜单实体类
 * @author vee
 */
@Entity
@Table(name = "wechat_custom_menu")
public class WechatCustomMenuPo implements java.io.Serializable, IPersistentObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**ID*/
    private Long id;

    /**所属商家ID*/
    private Long businessId;
    
    /**菜单名称*/
    private String name;
    
    /**菜单key值*/
    private String menuKey;
    
    /**链接地址*/
    private String url;
    
    /**菜单类型:click?view?...type的值必须小写
     * 此外，菜单的内容格式也是固定的，如click类型，需要添加key字段，不需要url字段，
     * 而view类型则相反
     * */
    private String type;
    
    /**级别：一级？二级*/
    private Integer level;
    
    /**父菜单ID：默认0*/
    private Long parentId;
    
    /** 是否删除 */
    private boolean isDelete;
    
    /** 创建时间 */
    private Date createTime;

    public WechatCustomMenuPo() {	}
    
	public WechatCustomMenuPo(Long businessId, String name, String key, String url, String type, Integer levle,
			Long parentId) {
		this.businessId = businessId;
		this.name = name;
		this.menuKey = key;
		this.url = url;
		this.type = type;
		this.level = levle;
		this.parentId = parentId;
	}


	@Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Long getId() {
        return this.id;
    }

    public void setId(Long userId) {
        this.id = userId;
    }
    @Column(nullable = false)
    public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getMenuKey() {
		return menuKey;
	}

	public void setMenuKey(String key) {
		this.menuKey = key;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer levle) {
		this.level = levle;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
}
