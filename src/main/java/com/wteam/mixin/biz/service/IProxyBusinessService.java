package com.wteam.mixin.biz.service;


/**
 * 代理商家模块业务接口
 * @version 1.0
 * @author benko
 * @time 2016-11-04 17:06:07
 */
public interface IProxyBusinessService {

    /**
     * 是代理的祖先吗
     * @param proxyBusinessId
     * @param ancestryId
     * @return
     */
    boolean isProxyAncestry(Long proxyBusinessId, Long ancestryId);


    /**
     * 改变是否允许代理商家在平台充值
     * @param proxyBusinessId
     * @param ancestryId
     * @return
     */
    boolean changeAllowBalanceRecharge(Long proxyBusinessId, Long ancestryId);





}
