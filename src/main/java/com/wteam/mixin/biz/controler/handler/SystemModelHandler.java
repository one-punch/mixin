/*
 * 文件名：SystemModelHandler.java
 * 版权：Copyright by wteam团队
 * 描述：
 * 修改人：benko
 * 修改时间：2016年6月2日 下午3:10:55
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */

package com.wteam.mixin.biz.controler.handler;

import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.wteam.mixin.model.vo.UserVo;

/**
 *
 * <p>将一些运行时要使用的信息填充进model中
 * <p>登录用户的信息</p>
 * 〈功能详细描述〉
 * @author benko
 * @version 2016年6月2日
 * @see SystemModelHandler
 * @since
 */
@ControllerAdvice
public class SystemModelHandler {
    /**
     * log4j实例对象.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(SystemModelHandler.class.getName());
    /**
     *
     */
    public static final String CURRENT_USER = "currentUser";

    /**
     *
     * Description: <br>
     * 1、…<br>
     * 2、…<br>
     * Implement: <br>
     * 1、…<br>
     * 2、…<br>
     *
     * @param map
     * @see
     */
    @ModelAttribute
    public void systemInfo(Map<String, Object> map) {
        map.put(CURRENT_USER, getCurrentUser());
    }

    public static UserVo getCurrentUser() {
        UserVo userVo = null;
        // 登录用户的信息
        Subject subject = SecurityUtils.getSubject();
        // 如果用户已经登录
        if (subject.isAuthenticated() || subject.isRemembered()) {

            Set<?> set = subject.getPrincipals().asSet();
            for (Object principal : set) {
                if (principal instanceof UserVo) {
                    userVo = (UserVo)principal;
                    break;
                }
            }
            LOG.debug("当前用户：{} {}",userVo.getAccount(), userVo.getUserId());
        }
        return userVo;
    }
}
