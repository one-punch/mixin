package com.wteam.mixin.biz.controler;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.service.ILoginService;
import com.wteam.mixin.biz.service.IPermissionService;
import com.wteam.mixin.biz.service.IUploadService;
import com.wteam.mixin.biz.service.IUserService;
import com.wteam.mixin.biz.service.IWechatService;
import com.wteam.mixin.define.Result;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.exception.ValidateException;
import com.wteam.mixin.model.po.UploadFilePo;
import com.wteam.mixin.model.vo.BusinessInfoVo;
import com.wteam.mixin.model.vo.UploadFileVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.model.wexin.XPayConfig;
import com.wteam.mixin.utils.FileUtils;
import com.wteam.mixin.utils.SpringUtils;


/**
 * 测试控制器
 *
 * @version 1.0
 * @author benko
 * @time 2016-5-13 19:49:04
 */
@Controller
@RequestMapping("/test")
public class TestController {

    /**
     * log4j实例对象.
     */
    private static Logger logger = LogManager.getLogger(TestController.class.getName());

    @ModelAttribute
    public void defaultValidate(@RequestParam(value = "user2", required = false) UserVo user2,
                                Map<String, Object> map) {
        logger.debug(user2);
        if (user2 == null) {
            map.put("user2", null);
        }
    }

    /** 用户 */
    public static final String USER = "user";

    @Autowired
    IPermissionService permissionService;

    @Autowired
    ILoginService loginService;

    @Autowired
    IUserService userService;
    @Autowired
    IUploadService uploadService;
    @Autowired
    IWechatService wechatService;
    @Autowired
    DozerBeanMapper mapper;

    /**
     * json
     *
     * @param user1
     *            请求参数
     * @param resultMessage
     *            返回参数
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/jsonToObject", method = RequestMethod.POST)
    public Result testJsonToObject(@RequestParam("user") UserVo user1,
                                   ResultMessage resultMessage) {
        logger.debug(user1);

        return resultMessage.setSuccessInfo("成功").putParam(USER, user1);
    }

    @ResponseBody
    @RequestMapping("/jsonToObjectList/{id}")
    public Result testJsonToObjectList(@RequestParam("user") List<UserVo> user1,
    									@RequestParam("id") Integer id,
                                       ResultMessage resultMessage) {
        logger.debug(user1);

        return resultMessage.setSuccessInfo("成功").putParam(USER, user1);
    }

    /**
     * 异常测试
     *
     * @param exception
     *            请求参数
     * @param resultMessage
     *            返回参数
     * @return Result
     * @throws Exception 异常
     */
    @ResponseBody
    @RequestMapping("/exception")
    public Result testException(Integer exception, ResultMessage resultMessage)
        throws Exception {

        switch (exception) {
            case 0:
                throw new Exception();
            case 1:
                throw new ServiceException("业务失败!!");
            case 2:
                throw new ValidateException(JSON.parseObject("{'username':'用户名不能为空'}"));
            default:
                break;
        }

        return resultMessage.setSuccessInfo("成功");
    }

    /**
     * 校验
     *
     * @param user1
     *            请求参数
     * @param resultMessage
     *            返回参数
     * @return
     */
    @ResponseBody
    @RequestMapping("/validate")
    public Result testValidate(@ModelAttribute("user") @Valid UserVo user, BindingResult result1,
                               @ModelAttribute("user2") @Valid UserVo user2, BindingResult result2,
                               ResultMessage resultMessage) {
        SpringUtils.validate(result1, result2);
        logger.debug(user);
        logger.debug(user2);
        return resultMessage.setSuccessInfo("成功").putParam(USER, user);
    }

    /**
     * 用户权限
     *
     * @param resultMessage 返回参数
     * @return Result
     */
    @ResponseBody
    @RequestMapping(value = "/user")
    public Result user(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                       ResultMessage resultMessage) {
        XPayConfig payConfig = wechatService.getPayConfig(user.getUserId(), true);
        logger.debug(payConfig);
        return resultMessage.setSuccessInfo("成功").putParam(SystemModelHandler.CURRENT_USER, user);
    }

    /**
     * 超级管理员权限
     *
     * @param resultMessage 返回参数
     * @return Result
     */
    @ResponseBody
    @RequestMapping(value = "/supermanager")
    public Result supermanager(ResultMessage resultMessage) {
        return resultMessage.setSuccessInfo("成功");
    }

    /**
     * 公用权限
     *
     * @param resultMessage 返回参数
     * @return Result
     */
    @ResponseBody
    @RequestMapping(value = "/all")
    public Result all(ResultMessage resultMessage) {
        return resultMessage.setSuccessInfo("成功");
    }


    /**
     * 压力测试-读数据库
     *
     * @param resultMessage 返回参数
     * @return Result
     */
    @ResponseBody
    @RequestMapping(value = "/test")
    public Result test(ResultMessage resultMessage) {
        resultMessage.putParam("user_permissions", loginService.findPermsByUserPrincipal("6df4600a44dd4650bf87ad7b142b758b"));
        resultMessage.putParam("user_roles", loginService.findPermsByUserPrincipal("6df4600a44dd4650bf87ad7b142b758b"));
        return resultMessage.setSuccessInfo("成功").putParam("permissions", permissionService.findAll());
    }

    @ResponseBody
    @RequestMapping("/testREST/{id}")
    public Result testREST(@PathVariable int id, ResultMessage resultMessage){
    	return resultMessage.setSuccessInfo("successed").putParam("info", "in a REST way "+id);
    }
}










