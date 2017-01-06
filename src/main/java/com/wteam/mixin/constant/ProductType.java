package com.wteam.mixin.constant;

import java.util.Arrays;
import java.util.List;

/**
 * 产品类型，订单中使用
 * @author benko
 *
 */
public class ProductType {

    /** 流量 */
    public static final  String Traffic = "Traffic";

    /** 余额充值 */
    public static final  String BusinessBalance = "BusinessBalance";

    /** 所有产品类型 */
    public static final  List<String> ALL = Arrays.asList(Traffic, BusinessBalance);
}
