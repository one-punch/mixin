package com.wteam.mixin.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 动态配置常量
 * @author benko
 *
 */
public class DConfig {

    /** 顾客订单费率（CustomerOrderRate）：百分比（0-1）*/
    public static final  String CustomerOrderRate = "CustomerOrderRate";

    /** 提现门槛（WithdrawMinPrice）：元  */
    public static final  String WithdrawMinPrice = "WithdrawMinPrice";

    /** 已结算到余额的最小金额（SettlemetMinPrice）：元  */
    public static final  String SettlemetMinPrice = "SettlemetMinPrice";

    /** 收单开关  true or false*/
    public static final  String ShouDanSwitch = "ShouDanSwitch";

    public static final List<String> ALL = Arrays.asList(CustomerOrderRate, WithdrawMinPrice, SettlemetMinPrice, ShouDanSwitch);
}
