package com.wteam.mixin.model.po;
// Generated 2016-4-6 23:48:57 by Hibernate Tools 4.3.1.Final


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;

import com.wteam.mixin.constant.UploadType;


/**
 * 上传文件<br>
 * 属性url 是相对于utils包的 {@link FileUtils}的rootURL的路径
 * @author benko
 * @version 2016年6月20日
 * @see UploadFilePo
 * @since
 */
@Entity
@Table(name = "t_uploadfile")
public class UploadFilePo implements java.io.Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** 上传ID */
    private Long uploadId;

    /** 服务器上的资源路径 */
    private String url;
    /** 客户端发送过来的文件名 */
    private String fileName;
    /** 上传文件类型 */
    private UploadType type;
    /** 客户端发送过来的MIME类型 */
    private String contentType;

    /** 是否删除 */
    private boolean isDelete;
    /** 创建时间 */
    private Date createTime;

    public UploadFilePo() {}

    @Id
    @GeneratedValue(strategy = IDENTITY)

    @Column(name = "uploadId", unique = true, nullable = false)
    public Long getUploadId() {
        return this.uploadId;
    }

    public void setUploadId(Long uploadId) {
        this.uploadId = uploadId;
    }

    @Column(name = "url")
    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Column(name = "fileName")
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    public UploadType getType() {
        return this.type;
    }

    public void setType(UploadType type) {
        this.type = type;
    }

    @Column(name = "contentType", nullable = false)
    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
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
