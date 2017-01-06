package com.wteam.mixin.biz.controler.handler;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.wteam.mixin.define.Result;
import com.wteam.mixin.define.ResultInfo;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.exception.ValidateException;


/**
 * 控制层异常处理类
 *
 * @version 1.0
 * @author benko
 * @time
 */
@ControllerAdvice
public class WteamExceptionHandler {

    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(WteamExceptionHandler.class.getName());

    /**
     * 系统错误异常
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler({Exception.class, RuntimeException.class})
    @ResponseBody
    public Result hanldeException(Exception e) {
        LOG.error("", e);
        ResultMessage resultMessage = new ResultMessage();
        return resultMessage.setInfo(ResultInfo.SYSTEM_FAIL);
    }

    /**
     * 业务失败异常
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler({ServiceException.class})
    @ResponseBody
    public Result hanldeServiceException(ServiceException e) {
        LOG.error("", e);
        ResultMessage resultMessage = new ResultMessage();
        return resultMessage.setInfo(e.getCode(), e.getInfo());
    }

    /**
     * 验证失败异常
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler({ValidateException.class})
    @ResponseBody
    public Result handleValidateException(ValidateException e) {
        LOG.error("", e);
        ResultMessage resultMessage = new ResultMessage();
        return resultMessage.setInfo(ResultInfo.VALIDATE_FAIL).putParam(ResultInfo.VALIDATE,
            e.getValidate());
    }

    /**
     * 请求参数不全异常
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler({MissingServletRequestParameterException.class})
    @ResponseBody
    public Result handleMissingException(MissingServletRequestParameterException e) {
        LOG.error("", e);
        ResultMessage resultMessage = new ResultMessage();
        return resultMessage.setInfo(ResultInfo.MISSING_REQUEST_PARAM_FAIL,
            "没有请求参数 " + e.getParameterName());
    }


    /**
     * 请求方式不支持
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    @ResponseBody
    public Result handleException(HttpRequestMethodNotSupportedException e) {
        LOG.error(e);
        ResultMessage resultMessage = new ResultMessage();
        return resultMessage.setInfo(ResultInfo.SYSTEM_FAIL, "不支持' " + e.getMethod() + "'请求");
    }


    /**
     * 请求方式不支持
     *
     * @param e 异常
     * @return Result
     */
    @ExceptionHandler({HttpMediaTypeNotAcceptableException.class})
    public String handleException(HttpMediaTypeNotAcceptableException e) {
        LOG.error(e);
        return "";
    }

}
