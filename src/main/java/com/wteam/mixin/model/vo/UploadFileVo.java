package com.wteam.mixin.model.vo;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.wteam.mixin.define.IValueObject;


/**
 * 上传Vo
 * @version 1.0
 * @author benko
 * @time 2016-4-10 17:59:12
 */
public class UploadFileVo implements IValueObject {

	private Long uploadId;
	@JSONField(name = "isDelete")
	private boolean isDelete;
	private Date createTime;
	private String url;
	private String type;
	private String fileName;
	private String contentType;

	public String createFileName() {
		String[] array = url.split("/");
		return array[array.length -1];
	}

	public Long getUploadId() {
		return uploadId;
	}
	public void setUploadId(Long uploadId) {
		this.uploadId = uploadId;
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
	public String getContentType() {
		return contentType;
	}
	public void setContentType(String contentType) {
		this.contentType = contentType;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
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
}
