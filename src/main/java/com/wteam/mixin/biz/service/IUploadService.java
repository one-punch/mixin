package com.wteam.mixin.biz.service;


import com.wteam.mixin.model.vo.UploadFileVo;


/**
 * 文件上传,查看 ,下载管理业务
 *
 * @version 1.0
 * @author benko
 */
public interface IUploadService {

    /**
     * 保存文件信息
     *
     * @param uploadVo 文件对象
     * @return UploadFileVo
     */
    UploadFileVo save(UploadFileVo uploadVo);

    /**
     * 获取文件信息
     *
     * @param fid 文件ID
     * @return UploadFileVo
     */
    UploadFileVo get(Long fid);

}
