package com.wteam.mixin.shiro;


import static com.wteam.mixin.shiro.CustomUsernamePasswordToken.*;

import java.util.Set;

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
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

import com.wteam.mixin.biz.service.ILoginService;
import com.wteam.mixin.model.vo.UserVo;



/**
 * <p>Title:CustomRealm </p> <p>Description:自定义shiro数据源 </p> <p>Company:Wteam </p>
 * 
 * @author Wteam 李焕滨 86571705@qq.com
 */
public class CustomRealm extends AuthorizingRealm {

    /**
     * 
     */
    private static Logger LOG = LogManager.getLogger(CustomPermissionCheckFilter.class);

    @Autowired
    ILoginService loginService;

    // 设置realm的名称
    @Override
    public void setName(String name) {
        super.setName("customRealm");
    }

    // realm的认证方法，从数据库查询用户信息
    @SuppressWarnings("unchecked")
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token)
        throws AuthenticationException {

        LOG.debug("进入doGetAuthenticationInfo登陆认证方法！");
        CustomUsernamePasswordToken customUsernamePasswordToken = null;
        if (token instanceof CustomUsernamePasswordToken) {
            customUsernamePasswordToken = (CustomUsernamePasswordToken)token;
        } 
        else {
            return null;
        }

        String loginType = customUsernamePasswordToken.getLoginType();
        SimpleAuthenticationInfo authenticationInfo = null;
        UserVo userVo = null;

        LOG.debug(loginType);
        switch (loginType) {
            case LOGIN_WITN_USERINFO : {
                try {
                    userVo = loginService.findByPrincipal((String)token.getPrincipal());
                } 
                catch (Exception e) {
                    LOG.error("",e);
                    throw new AuthenticationException("用户查询失败");
                }
            }break;
            
            case LOGIN_WECHAT_AUTO : {
                try {
                    userVo = loginService.findCustomerByOpenId((String)token.getPrincipal());
                } 
                catch (Exception e) {
                    LOG.error("",e);
                    throw new AuthenticationException("用户查询失败");
                }
            }break;

            default:
                break;
        }

        if (userVo != null) {
            LOG.debug("发现用户：{}",userVo.getAccount());
            // 给token重新设置身份
            customUsernamePasswordToken.setUsername(userVo.getPrincipal());
            authenticationInfo = new SimpleAuthenticationInfo(
                token, 
                userVo.getPassword(),
                ByteSource.Util.bytes(userVo.createCredentialsSalt()),
                this.getName());
            //authenticationInfo.getPrincipals().fromRealm(getName()).add(userVo);
            ((SimplePrincipalCollection)authenticationInfo.getPrincipals()).add(userVo, getName());
        } 
        else 
            throw new UnknownAccountException("找不到用户");
        
        LOG.debug("退出doGetAuthenticationInfo登陆认证方法！token.principal:{}",token.getPrincipal());
        return authenticationInfo;
    }

    // 用于授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        LOG.debug("进入doGetAuthorizationInfo授权方法！");
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        Set<String> permissions = null;

        CustomUsernamePasswordToken token = null;
        if (principals.getPrimaryPrincipal() instanceof CustomUsernamePasswordToken) {
            token = (CustomUsernamePasswordToken)principals.getPrimaryPrincipal();
        }
        else {
            return authorizationInfo;
        }

        // 判断是不是用户信息登陆
        String loginType = token.getLoginType();
        
        switch (loginType) {
            case LOGIN_WECHAT_AUTO :
            case LOGIN_WITN_USERINFO : {
                permissions = loginService.findPermsByUserPrincipal(token.getUsername());
            } break;

            default:
                break;
        }

        authorizationInfo.setStringPermissions(permissions);

        return authorizationInfo;
    }
}
