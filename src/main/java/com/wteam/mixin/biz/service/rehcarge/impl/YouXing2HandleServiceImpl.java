package com.wteam.mixin.biz.service.rehcarge.impl;

import org.springframework.stereotype.Service;

import com.wteam.mixin.recharge.YouXingRecharge;

/**
 * 友信2流量接口充值
 * @author benko
 *
 */
@Service("youXing2HandleService")
public class YouXing2HandleServiceImpl extends YouXingHandleServiceImpl {

    @Override
    protected YouXingRecharge getRecharge() {
        return YouXingRecharge.instance(YouXingRecharge.youxing2);
    }
}
