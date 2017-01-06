package com.wteam.mixin.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 系统文章
 * @author benko
 *
 */
public class Documents {

    /** 教程指导 */
    public static final  String Tutorial = "Tutorial";

    /** 商家注册服务协议 */
    public static final  String BusinessAgreement = "BusinessAgreement";

    /** 商家绑定公众号 */
    public static final  String AddOfficialAccount = "AddOfficialAccount";

    /** 商家余额充值 */
    public static final  String BusinessBalance = "BusinessBalance";

    public static final List<String> ALL = Arrays.asList(Tutorial, BusinessAgreement, AddOfficialAccount, BusinessBalance);
}
