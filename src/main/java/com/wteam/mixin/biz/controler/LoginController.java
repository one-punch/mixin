
package com.wteam.mixin.biz.controler;


import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.google.code.kaptcha.Producer;
import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.service.ILoginService;
import com.wteam.mixin.constant.SMSTpls;
import com.wteam.mixin.constant.ValidatePattern;
import com.wteam.mixin.define.Result;
import com.wteam.mixin.define.ResultInfo;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.vo.TelRegister2Vo;
import com.wteam.mixin.model.vo.TelRegisterVo;
import com.wteam.mixin.model.vo.UserInfoVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.shiro.CustomUsernamePasswordToken;
import com.wteam.mixin.utils.SMSUtils;
import com.wteam.mixin.utils.SpringUtils;
import com.wteam.mixin.utils.Utils;


/**
 * 登录模块控制器
 *
 * @version 1.0
 * @author benko
 */
@Controller
public class LoginController {

    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(LoginController.class.getName());

    /** 登录业务对象 */
    @Autowired
    private ILoginService loginService;

    /** kaptcha 图片验证码生成器 */
    @Autowired
    private Producer producer;

    /** 对象转换器 */
    @Autowired
    private DozerBeanMapper mapper;

    /** 用户 */
    public static final String USER = "user";
    /** 短信验证码 */
    public static final String NOTECODE = "notecode";
    /** 短信验证码 */
    public static final String KAPTCHA = "kaptcha";


    /**
     * 用户登录，手机或账号登录
     * @param user 登录信息
     * @param result1 数据验证结果
     * @param resultMessage 返回参数
     * @return Result
     * @see
     */
    @ResponseBody
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Result login(@ModelAttribute("user") @Valid UserVo user, BindingResult result1,
                        ResultMessage resultMessage) {

        SpringUtils.validate(result1);
        Subject subject = SecurityUtils.getSubject();

        try {
            if (subject.isAuthenticated() || subject.isRemembered()) {
                subject.logout();
            }

            CustomUsernamePasswordToken token = new CustomUsernamePasswordToken(
                user.getPrincipal(),
                user.getCredential(),
                CustomUsernamePasswordToken.LOGIN_WITN_USERINFO);
            subject.login(token);
        }
        catch (UnknownAccountException e) {
            return resultMessage.setServiceFailureInfo("用户名错误或不存在!");
        }
        catch (IncorrectCredentialsException e) {
            return resultMessage.setServiceFailureInfo("密码错误!");
        }
        catch (ExcessiveAttemptsException e) {
            return resultMessage.setServiceFailureInfo("密码重复输入错误超过5次，30分钟后再登录!");
        }
        catch (AuthenticationException e) {
            LOG.error("",e);
            return resultMessage.setServiceFailureInfo("认证失败!");
        }
        // 如果登录成功, 从subject中找到用户信息
        UserInfoVo userInfo = null;
		Set<?> set = subject.getPrincipals().asSet();
        for (Object principal : set) {
            if (principal instanceof UserVo) {
                userInfo = mapper.map(principal, UserInfoVo.class);
                userInfo.setRoleNames(new ArrayList<>(loginService.findRolesByUsername(user.getPrincipal())));
            }
        }
        return resultMessage.setSuccessInfo("登录成功").putParam(USER,userInfo);
    }

    /**
     * 商家手机号-短信验证码注册
     *
     * @param business 商家注册信息
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/register/business", method = RequestMethod.POST)
    public Result registerBusiness(
            @ModelAttribute("business") @Valid TelRegisterVo business, BindingResult result1,
    		ResultMessage resultMessage) {
        SpringUtils.validate(result1);
        // 验证短信验证码是否正确
        String notecode = (String)SecurityUtils.getSubject().getSession().getAttribute(NOTECODE);
        if(notecode == null)
            return resultMessage.setServiceFailureInfo("没有请求短信验证码");
        if(!notecode.equals(business.getNotecode()))
            return resultMessage.setServiceFailureInfo("验证码输入错误");

        // 注册用户
        loginService.registerBusiness(mapper.map(business, UserVo.class));

        return resultMessage.setSuccessInfo("注册成功");
    }

    /**
     * 商家创建代理商家
     *
     * @param business 商家注册信息
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/create/proxy/business", method = RequestMethod.POST)
    public Result createProxyBusiness(
            @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
            @ModelAttribute("business") @Valid TelRegister2Vo business, BindingResult result1,
            ResultMessage resultMessage) {
        SpringUtils.validate(result1);

        // 注册用户
        loginService.registerProxyBusiness(mapper.map(business, UserVo.class),user.getUserId());

        return resultMessage.setSuccessInfo("注册成功");
    }


    /**
     * 创建商家
     *
     * @param business 商家注册信息
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/create/business", method = RequestMethod.POST)
    public Result createBusiness(
            @ModelAttribute("business") @Valid TelRegister2Vo business, BindingResult result1,
            ResultMessage resultMessage) {
        SpringUtils.validate(result1);

        // 注册用户
        loginService.registerBusiness(mapper.map(business, UserVo.class));

        return resultMessage.setSuccessInfo("注册成功");
    }

    /**
     * 获取注册短信验证码
     *
     * @param tel 手机号
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/register/notecode")
    public Result registerAuthcode(
            @RequestParam("tel") String tel,
            @RequestParam("imageCode") String imageCode,
            ResultMessage resultMessage) {
        Session session = SecurityUtils.getSubject().getSession(true);
        // 验证格式
        if(!ValidatePattern.valid(ValidatePattern.TEL, tel))
            return resultMessage.setInfo(ResultInfo.VALIDATE_FAIL, "手机号格式错误");
        // 验证图片验证码
        String code = (String) session.getAttribute(KAPTCHA);
        if(code == null || !code.equalsIgnoreCase(imageCode))
            return resultMessage.setInfo(ResultInfo.VALIDATE_FAIL, "验证码错误");
        // 生成六位随机数
        String notecode = Utils.noteCode();
        String result = SMSUtils.sendSms(SMSTpls.NoteCode.param(notecode), tel);
        if ("error".equals(result)) {
            return resultMessage.setServiceFailureInfo("发送验证码失败");
        }
        // 缓存到session中
        session.setAttribute(NOTECODE, notecode);

        return resultMessage.setSuccessInfo("获取注册短信验证码成功");
    }

    /**
     * 生成图片验证码
     * @param rsp
     * @throws Exception
     */
    @RequestMapping(value = "/imageCode", method = RequestMethod.GET)
    public void kaptcha(HttpServletResponse rsp) throws Exception {
        Session session = SecurityUtils.getSubject().getSession(true);
//        String code = (String) session.getAttribute(KAPTCHA);
//        System.out.println("验证码: " + code);

        rsp.setDateHeader("Expires", 0);
        rsp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        rsp.addHeader("Cache-Control", "post-check=0, pre-check=0");
        rsp.setHeader("Pragma", "no-cache");
        rsp.setContentType("image/jpeg");

        String capText = producer.createText();
        session.setAttribute(KAPTCHA, capText);
        if(LOG.isDebugEnabled()) LOG.debug("imageCode->{}",capText);

        BufferedImage image = producer.createImage(capText);
        ServletOutputStream out = rsp.getOutputStream();
        ImageIO.write(image, "jpg", out);
        try {
            out.flush();
        } finally {
            out.close();
        }
    }
    /**
     * 用户登出
     *
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/loginout")
    public Result loginout(ResultMessage resultMessage) {

        SecurityUtils.getSubject().logout();

        return resultMessage.setSuccessInfo("注销成功");
    }

    /**
     * 未认证或session超时
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/sessionTimeOutResponse")
    public Result sessionTimeOutResponse(ResultMessage resultMessage) {
        return resultMessage.setInfo(ResultInfo.UNAUTHENTICATED_FAIL);
    }

    /**
     * 认证通过无访问权限
     *
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/unauthorized")
    public Result unauthorized(ResultMessage resultMessage) {
        return resultMessage.setInfo(ResultInfo.UNAUTHORIZED_FAIL);
    }
}
