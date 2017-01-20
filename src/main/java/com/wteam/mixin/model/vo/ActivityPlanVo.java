package com.wteam.mixin.model.vo;

import com.wteam.mixin.define.IValueObject;
import lombok.Data;

/**
 * Created by zbin on 17/1/20.
 */
@Data
public class ActivityPlanVo implements IValueObject {

    private Long id;

    private String name;
}
