package com.wteam.mixin.model.vo;

import com.wteam.mixin.define.IValueObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zbin on 17/1/21.
 */
@Data
public class BargainirgPlanVo implements IValueObject {

    String name;

    String providerName;

    BigDecimal cost;

    String apiProvider;

    String province;

    Boolean isActive;

    Date startTime;

    Date endTime;

    BigDecimal lowPrice;

    Integer limitNumber;

    BigDecimal retailPrice;

    Long id;

    public BargainirgPlanVo(){

    }

}
