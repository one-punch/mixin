package com.wteam.mixin.model.vo;

import java.util.Date;

import javax.validation.constraints.NotNull;

import com.wteam.mixin.define.IValueObject;

public class AutoReplyVo implements IValueObject{
	
	/**ID*/
    private Long id;
    
    /**所属商家ID*/
    private Long businessId;
    
    /**关键词- -*/
    @NotNull
    private String keyWord;
    
    /**内容*/
    @NotNull
    private String content;
    
    /**是否使用*/
    @NotNull
    private boolean actived;

    /** 是否删除 */
    private boolean isDelete;
    
    /** 创建时间 */
    private Date createTime;

    public AutoReplyVo() {	}

	public AutoReplyVo(Long id, String key, String content, boolean actived) {
		this.id = id;
		this.keyWord = key;
		this.content = content;
		this.actived = actived;
	}
	
	public AutoReplyVo(String key, String content, boolean actived) {
		this.keyWord = key;
		this.content = content;
		this.actived = actived;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
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

	public boolean getActived() {
		return actived;
	}

	public void setActived(boolean actived) {
		this.actived = actived;
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

	@Override
	public String toString() {
		return "AutoReplyVo [id=" + id + ", businessId=" + businessId + ", key=" + keyWord + ", content=" + content
				+ ", actived=" + actived + ", isDelete=" + isDelete + ", createTime=" + createTime + "]";
	}
}
