package com.wteam.mixin.model.vo;

import com.wteam.mixin.define.IValueObject;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by zbin on 17/1/30.
 */
@Data
public class CustomerRecordVo implements IValueObject {

    String nickname;

    String headimgurl;

    BigDecimal discount;

    Date createdAt;
}
