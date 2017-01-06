package com.wteam.mixin.biz.dao;

import java.math.BigDecimal;

/**
 * 对应 PlatformInfoPo 和 DConfigPO
 * @author benko
 *
 */
public interface ISystemDao {

    String platfromInfo(String name);

    String platfromInfo(String name, String value);

    String dconfig(String name);

    String dconfig(String name, String value);

}
