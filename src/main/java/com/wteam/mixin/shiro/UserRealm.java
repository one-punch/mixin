package com.wteam.mixin.shiro;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.wteam.mixin.biz.service.ILoginService;
import com.wteam.mixin.model.vo.UserVo;


/**
 * 功能: 用户身份验证 相关配置: spring-shiro-web.xml
 *
 * @version 1.0
 * @author benko
 */
public class UserRealm extends AuthorizingRealm {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(UserRealm.class.getName());

    /** 登录业务对象*/
//    @Autowired
//    private ILoginService loginService;

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LOG.debug("进入doGetAuthorizationInfo (principals)");
        String username = (String)principals.getPrimaryPrincipal();

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        //authorizationInfo.setRoles(loginService.findRolesByUsername(username));
        // authorizationInfo.setStringPermissions(findPermissons(username));

        LOG.debug("进入doGetAuthorizationInfo (principals)");
        return authorizationInfo;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
        throws AuthenticationException {
        LOG.debug("进入 doGetAuthenticationInfo(token)");
        String username = (String)token.getPrincipal();
//        UserVo user = loginService.findByPrincipal(username);


        UserVo user = new UserVo("principal", "credential");

        if (user == null) {
            throw new UnknownAccountException();// 没找到帐号
        }

        // if(Boolean.TRUE.equals(user.getLocked())) {
        // throw new LockedAccountException(); //帐号锁定
        // }

        // 交给AuthenticatingRealm使用CredentialsMatcher进行密码匹配，如果觉得人家的不好可以自定义实现
        SimpleAuthenticationInfo authenticationInfo = new SimpleAuthenticationInfo(
            user.getPrincipal(), // 用户名
            user.getPassword(), // 密码
            ByteSource.Util.bytes(user.createCredentialsSalt()), // salt=principal+salt
            getName() // realm name
        );
        // 将用户信息添加到身份中
        authenticationInfo.getPrincipals().fromRealm(getName()).add(user);

        LOG.debug("退出 doGetAuthenticationInfo(token)");
        return authenticationInfo;
    }

}
