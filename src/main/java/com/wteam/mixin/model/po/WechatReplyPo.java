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
 * 微信自动回复实体类
 * @author vee
 */
@Entity
@Table(name = "wechat_reply")
public class WechatReplyPo implements java.io.Serializable, IPersistentObject {
    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /**ID*/
    private Long id;
    
    /**所属商家ID*/
    private Long businessId;
    
    /**关键词- -*/
    private String keyWord;
    
    /**内容*/
    private String content;
    
    /**是否使用*/
    private boolean actived;

    /** 是否删除 */
    private boolean isDelete;
    
    /** 创建时间 */
    private Date createTime;

    public WechatReplyPo() {	}
    
	public WechatReplyPo(Long businessId, String keyWord, String content, boolean actived) {
		this.businessId = businessId;
		this.keyWord = keyWord;
		this.content = content;
		this.actived = actived;
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
    @Column(nullable=false)
    public Long getBusinessId() {
		return businessId;
	}

	public void setBusinessId(Long businessId) {
		this.businessId = businessId;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String key) {
		this.keyWord = key;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	@Column(name="actived")
	@Type(type="boolean")
	public boolean getActived() {
		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
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

	@Override
	public String toString() {
		return "WechatReplyPo [id=" + id + ", businessId=" + businessId + ", keyWord=" + keyWord + ", content="
				+ content + ", actived=" + actived + ", isDelete=" + isDelete + ", createTime=" + createTime + "]";
	}
}
