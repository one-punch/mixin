package com.wteam.mixin.biz.controler;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.service.IProxyBusinessService;
import com.wteam.mixin.biz.service.IUserService;
import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.constant.ValidatePattern;
import com.wteam.mixin.define.Result;
import com.wteam.mixin.define.ResultInfo;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.query.BusinessInfoQuery;
import com.wteam.mixin.model.query.UserQuery;
import com.wteam.mixin.model.vo.BusinessInfoVo;
import com.wteam.mixin.model.vo.UserInfoVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.model.wexin.XPayConfig;
import com.wteam.mixin.pagination.Pagination;
import com.wteam.mixin.utils.SpringUtils;

/**
 * 用户模块控制器
 *
 * @version 1.0
 * @author benko
 */
@Controller
public class UserController {
    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(UserController.class.getName());

    /** 登录业务对象 */
    @Autowired
    IUserService userService;

    @Autowired
    IProxyBusinessService proxyBusinessService;

    @Autowired
    DozerBeanMapper mapper;

    /** 用户 */
    public static final String USER = "user";
    /** 商家信息 */
    public static final String BUSINESS = "business";
    /** 管理员信息 */
    public static final String ADMIN = "admin";
    /** 支付配置 */
    public static final String PAY_CONFIG = "payconfig";

    /**
     * 管理员信息
     *
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/user/admin")
    public Result admin(
                 @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                 ResultMessage resultMessage) {
        UserInfoVo infoVo = userService.adminInfo(user.getUserId());
        return resultMessage.setSuccessInfo("获取管理员信息成功").putParam(ADMIN, infoVo);
    }


    /**
     * 商家用户信息
     *
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/user/business")
    public Result business(
                 @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                 @RequestParam(name="businessId", required=false)Long businessId,
                 ResultMessage resultMessage) {
        BusinessInfoVo infoVo = null;

        if (userService.isRole(user.getUserId(), RoleType.bussiness)
            || userService.isRole(user.getUserId(), RoleType.proxy_business)) {
            infoVo = userService.businessInfo(user.getUserId());
        }
        if (userService.isRole(user.getUserId(), RoleType.supermanager)) {
            if (businessId == null)
                return resultMessage.setInfo(ResultInfo.MISSING_REQUEST_PARAM_FAIL, "请求缺少参数 businessId");
            // 管理员获取某个商家或代理商家的信息
            infoVo = userService.businessInfo(businessId);
        }

        return resultMessage.setSuccessInfo("获取商家用户信息成功").putParam(BUSINESS, infoVo);
    }


    /**
     * 根据条件分页获取商家列表
     *
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/business/list/super")
    public Result listBySuper(@RequestParam(name="wechatOfficAccount", required=false)String wechatOfficAccount,
                           @RequestParam(name="businessId", required=false)Long businessId,
                           @RequestParam(name="tel", required=false)String tel,
                           @RequestParam("pageNo")Integer pageNo,
                           @RequestParam("pageSize")Integer pageSize,
                           ResultMessage resultMessage) {

        // 验证格式
        if(tel != null){
            if(!ValidatePattern.valid(ValidatePattern.TEL, tel))
                return resultMessage.setInfo(ResultInfo.VALIDATE_FAIL, "手机号格式错误");
        }

        Pagination pagination = userService.businessListBySuper(wechatOfficAccount, businessId, tel, pageNo, pageSize);
        return resultMessage.setSuccessInfo("获取商家用户信息成功").putParam("page", pagination);
    }

    /**
     * 获取支付方式
     * @param user
     * @param resultMessage
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/business/payconfig")
    public Result payconfig(
                 @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                 ResultMessage resultMessage) {
        BusinessInfoVo infoVo = userService.businessInfo(user.getUserId());
        return resultMessage.setSuccessInfo("获取管理员信息成功").putParam(PAY_CONFIG, mapper.map(infoVo, XPayConfig.class));
    }
    /**
     * 修改支付方式
     * @param user
     * @param payConfig
     * @param result
     * @param resultMessage
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/business/payconfig/edit")
    public Result editPayconfig(
                 @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                 @ModelAttribute("payconfig")@Valid XPayConfig payConfig, BindingResult result,
                 ResultMessage resultMessage) {
        SpringUtils.validate(result);
        LOG.debug(JSON.toJSONString(payConfig));
        BusinessInfoVo infoVo = mapper.map(payConfig, BusinessInfoVo.class);
        infoVo.setBusinessId(user.getUserId());
        LOG.debug(JSON.toJSONString(infoVo));
        infoVo = userService.updateInfo(infoVo);
        return resultMessage.setSuccessInfo("获取管理员信息成功").putParam(PAY_CONFIG, infoVo);
    }

    /**
     * 根据条件分页获取代理商家列表
     *
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/business/proxy/list")
    public Result proxyBusinessList(
                           @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                           @RequestParam(name="wechatOfficAccount", required=false)String wechatOfficAccount,
                           @RequestParam(name="businessId", required=false)Long businessId,
                           @RequestParam(name="tel", required=false)String tel,
                           @RequestParam(name="proxyParentId", required=false)Long proxyParentId,
                           @RequestParam("pageNo")Integer pageNo,
                           @RequestParam("pageSize")Integer pageSize,
                           UserQuery userQuery , BusinessInfoQuery businessQuery,
                           ResultMessage resultMessage) {

        // 验证格式
        if(tel != null){
            if(!ValidatePattern.valid(ValidatePattern.TEL, tel))
                return resultMessage.setInfo(ResultInfo.VALIDATE_FAIL, "手机号格式错误");
        }
        // 设置代理父商家
        if (userService.isRole(user.getUserId(), RoleType.supermanager)) {
            if (proxyParentId == null) {
                return resultMessage.setInfo(ResultInfo.MISSING_REQUEST_PARAM_FAIL, "请求缺少参数 proxyParentId");
            }
            businessQuery.setProxyParentId(proxyParentId);
        } else {
            businessQuery.setProxyParentId(user.getUserId());
        }

        userQuery.setTel(tel);
        businessQuery.setBusinessId(businessId);
        businessQuery.setWechatOfficAccount(wechatOfficAccount);

        Pagination pagination = userService.businessList(userQuery, businessQuery, pageNo, pageSize);
        return resultMessage.setSuccessInfo("获取商家用户信息成功").putParam("page", pagination);
    }

    /**
     * 商家用户信息
     *
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/user/changePassword")
    public Result changePassword(
                 @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                 @RequestParam("password")String password,
                 ResultMessage resultMessage) {
        user.setPassword(password);
        userService.changePassword(user);
        return resultMessage.setSuccessInfo("修改密码成功");
    }


    /**
     * 改变是否允许代理商家在平台充值
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/proxyBusiness/changeAllowBalanceRecharge")
    public Result changeAllowBalanceRecharge(
                 @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                 @RequestParam("proxyBusinessId")Long proxyBusinessId,
                 ResultMessage resultMessage) {
        proxyBusinessService.changeAllowBalanceRecharge(proxyBusinessId, user.getUserId());
        return resultMessage.setSuccessInfo("修改成功");
    }

}
