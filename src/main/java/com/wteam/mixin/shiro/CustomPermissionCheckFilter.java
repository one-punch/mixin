package com.wteam.mixin.shiro;


import java.util.Set;
import java.util.function.Function;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.wteam.mixin.biz.service.ILoginService;


/**
 * <p>Title:CustomPermissionCheckFilter </p> <p>Description:自定义权限过滤器 </p> <p>Company:Wteam </p>
 * 
 * @author 李焕滨
 */
public class CustomPermissionCheckFilter extends AccessControlFilter {
    
    /**
     * 
     */
    private static Logger LOG = LogManager.getLogger(CustomPermissionCheckFilter.class);

    /**
     * 认证不通过跳转的url
     */
    private String unauthenticatedUrl;

    /**
     * 认证通过无访问权限跳转的url
     */
    private String unauthorizedUrl;

    /**
     * 无须权限即可访问的地址
     */
    private String[] urlWithoutPermission;

    /**
     * 登录认证业务对象
     */
    @Autowired
    private ILoginService loginService;

    /**
     * 系统权限缓存 为非法路径过滤设置的缓存 修改权限信息时手动调用updateUrlAllCache（）刷新此缓存 默认服务器运行后第一次使用此参数时查询一次数据库，之后靠手动刷新此缓存
     */
    private Set<String> urlAllCache;

    /**
     * 刷新数据库缓存的方法
     */
    public void updateUrlAllCache() {
        urlAllCache = loginService.findAllPermission();
    }

    public String getUnauthenticatedUrl() {
        return unauthenticatedUrl;
    }

    public void setUnauthenticatedUrl(String unauthenticatedUrl) {
        this.unauthenticatedUrl = unauthenticatedUrl;
    }

    public String getUnauthorizedUrl() {
        return unauthorizedUrl;
    }

    public void setUnauthorizedUrl(String unauthorizedUrl) {
        this.unauthorizedUrl = unauthorizedUrl;
    }

    public String[] getUrlWithoutPermission() {
        return urlWithoutPermission;
    }

    public void setUrlWithoutPermission(String[] urlWithoutPermission) {
        this.urlWithoutPermission = urlWithoutPermission;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response,
                                      Object mappedValue)
        throws Exception {

        Subject subject = getSubject(request, response);
        String url = getPathWithinApplication(request);
        HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        
        
        LOG.debug("requestURI:{}, url:{}",httpServletRequest.getRequestURI(),url);
        
        
        if(urlWithoutPermission != null){
            for (String string : urlWithoutPermission) {
                if (url.equals(string)) {
                    LOG.error("退出CustomPermissionCheckFilter方法-------无需权限");
                    return true;
                }
            }
        }
        
        if (url.equals("/" + unauthenticatedUrl) || url.equals("/" + unauthorizedUrl)) {
            LOG.error("退出CustomPermissionCheckFilter方法-------授权认证处理链接");
            return true;
        }

        /**
         * 判断是不是非法路径
         */
        if (null == urlAllCache) {
            updateUrlAllCache();
        }
        boolean isIlleageUrl = true;

        if (urlAllCache.contains(url)) {
            isIlleageUrl = false;
        }
        
        if (isIlleageUrl) {
            //LOG.error("退出CustomPermissionCheckFilter方法-------404");
            return true;
        }

        if (subject.isAuthenticated()) {
            // 认证通过
            if (subject.isPermitted(url)) {
                LOG.error("退出CustomPermissionCheckFilter方法-------有权限");

                // 拥有权限
                return true;
            }
            else {
                // 无权限
                httpServletResponse.sendRedirect(
                    httpServletRequest.getContextPath() + "/" + unauthorizedUrl);
                LOG.error("退出CustomPermissionCheckFilter方法-------无权限");
                return false;
            }
        }
        else {
            // 认证不通过
            httpServletResponse.sendRedirect(
                httpServletRequest.getContextPath() + "/" + unauthenticatedUrl);
            LOG.error("退出CustomPermissionCheckFilter方法-------未认证");
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
        throws Exception {
        return false;
    }

}
