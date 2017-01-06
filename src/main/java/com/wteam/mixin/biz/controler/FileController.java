package com.wteam.mixin.biz.controler;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.wteam.mixin.biz.service.IUploadService;
import com.wteam.mixin.constant.UploadType;
import com.wteam.mixin.define.Result;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.vo.UploadFileVo;
import com.wteam.mixin.utils.FileUtils;


/**
 * 文件管理控制器
 *
 * @version 1.0
 * @author benko
 * @time 2016-5-13 19:49:04
 */
@Controller
@RequestMapping("/file")
public class FileController {

    @Autowired
    private IUploadService uploadService;

    /**
     * 上传单个文件，
     *
     * Description: <br>
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param file 上传文件
     * @param type 上传文件类型，默认为OTHERS
     * @param resultMessage 返回对象
     * @return Result
     * @throws IOException
     * @see
     */
    @ResponseBody
    @RequestMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file,
                         @RequestParam(value = "type", required = false) String type,
                         ResultMessage resultMessage)
        throws IOException {

        if (type == null || "".equals(type)) {
            type = UploadType.OTHERS.name();
        }

        UploadType uploadType = UploadType.checkType(type);
        if (uploadType == null || !uploadType.check(file.getContentType())) {
            return resultMessage.setServiceFailureInfo("文件类型与要求的不相符");
        }
        UploadFileVo uploadVo = new UploadFileVo();
        uploadVo.setContentType(file.getContentType());
        uploadVo.setType(uploadType.name());
        uploadVo.setUrl(uploadType.getRelativeURL(file.getOriginalFilename()));
        uploadVo.setFileName(file.getOriginalFilename());

        uploadVo = uploadService.save(uploadVo);
        // 复制文件到服务器上
        FileUtils.create(file.getInputStream(), FileUtils.getResourcePath(uploadVo.getUrl()));

        return resultMessage.setSuccessInfo("上传成功").putParam("file", uploadVo);
    }

    /**
     * 下载文件
     *
     * @param fid
     * @return
     * @throws IOException
     */
    @RequestMapping("/download")
    public void download(@RequestParam("fid") Long fid, HttpServletResponse response)
        throws IOException {

        UploadFileVo fileVo = uploadService.get(fid);

        File file = FileUtils.getResourceFile(fileVo.getUrl());

        if (file == null) {
            throw new ServiceException("文件丢失!");
        }

        response.reset();// 不加这一句的话会出现下载错误
        response.setHeader("Content-disposition",
            "attachment; filename=" + URLEncoder.encode(fileVo.getFileName(), "utf-8"));// 设定输出文件头
        response.setContentType(fileVo.getContentType());// 定义输出类型

        FileUtils.inToout(new FileInputStream(file), response.getOutputStream());

    }

    /**
     * 查看文件
     *
     * @param fid
     * @return
     * @throws IOException
     */
    @RequestMapping("/see")
    public void see(@RequestParam("fid") Long fid, HttpServletResponse response)
        throws IOException {

        UploadFileVo fileVo = uploadService.get(fid);

        File file = FileUtils.getResourceFile(fileVo.getUrl());

        if (file == null) {
            throw new ServiceException("文件丢失!");
        }

        response.reset();// 不加这一句的话会出现下载错误
        response.setHeader("Content-disposition",
            "inline; filename=" + URLEncoder.encode(fileVo.getFileName(), "utf-8"));// 设定输出文件头
        response.setContentType(fileVo.getContentType());// 定义输出类型

        FileUtils.inToout(new FileInputStream(file), response.getOutputStream());
    }

    /**
     * 下载文件
     *
     * @param fid
     * @return
     * @throws IOException
     */
    @RequestMapping("/downloadBigFile")
    public void download(HttpServletResponse response)
        throws IOException {

        File file = new File("E:\\PPDownload\\视频\\华丽上班族HD国语中字.mp4");// 1.3G

        response.reset();// 不加这一句的话会出现下载错误
        response.setHeader("Content-disposition",
            "attachment; filename=" + URLEncoder.encode("华丽上班族HD国语中字.mp4", "utf-8"));// 设定输出文件头
        response.setContentType("video/mpeg4");// 定义输出类型

        FileUtils.inToout(new FileInputStream(file), response.getOutputStream());

    }

}
