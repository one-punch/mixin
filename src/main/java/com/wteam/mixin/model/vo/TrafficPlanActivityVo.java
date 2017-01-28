package com.wteam.mixin.model.vo;

import com.wteam.mixin.define.IValueObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zbin on 17/1/21.
 */
@Data
public class TrafficPlanActivityVo implements IValueObject {


    public Long trafficPlanId;

    public Long userId;

    public Long bargainirgId;

    public Boolean isActive;

    public BigDecimal lowPrice;

    public Integer limitNumber;

    public Date startTime;

    public Date endTime;

    public TrafficPlanActivityVo(){

    }

    public TrafficPlanActivityVo(Long trafficPlanId, Integer limitNumber, BigDecimal lowPrice, boolean isActive, Date startTime, Date endTime){
        this.trafficPlanId = trafficPlanId;
        this.limitNumber = limitNumber;
        this.lowPrice = lowPrice;
        this.isActive = isActive;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}
