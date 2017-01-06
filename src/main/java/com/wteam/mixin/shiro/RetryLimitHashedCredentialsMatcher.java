package com.wteam.mixin.shiro;


import java.util.concurrent.atomic.AtomicInteger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;


/**
 * 功能: 实现登陆密码验证服务<br>
 * 相关配置: spring-shiro-web.xml
 * @version 1.0
 * @author benko
 * @time 2016-1-8 15:31:10
 */
public class RetryLimitHashedCredentialsMatcher extends HashedCredentialsMatcher{
	/**
	 * log4j实例对象.
	 */
	private static Logger logger = LogManager.getLogger(RetryLimitHashedCredentialsMatcher.class
			.getName());

    private Cache<String, AtomicInteger> passwordRetryCache;

    public RetryLimitHashedCredentialsMatcher(CacheManager cacheManager) {
        passwordRetryCache = cacheManager.getCache("passwordRetryCache");
    }

    @Override
    public boolean doCredentialsMatch(AuthenticationToken token, AuthenticationInfo info) {
        logger.debug("进入 doCredentialsMatch()");

    	String username = (String)token.getPrincipal();
        //retry count + 1
        AtomicInteger retryCount = passwordRetryCache.get(username);
        if(retryCount == null) {
            retryCount = new AtomicInteger(0);
            passwordRetryCache.put(username, retryCount);
        }
        // 重复登录超过5次
        if(retryCount.incrementAndGet() > 5) {
            //if retry count > 5 throw
            throw new ExcessiveAttemptsException();
        }
        // 密码是否匹配
        boolean matches = super.doCredentialsMatch(token, info);
        if(matches) {
            //clear retry count
            passwordRetryCache.remove(username);
        }

        logger.debug("退出 doCredentialsMatch()");
        return matches;
    }
}
