package com.wteam.mixin.model.vo;

import com.wteam.mixin.define.IValueObject;
import lombok.Data;

/**
 * Created by zbin on 17/1/19.
 */
@Data
public class BargainirgUserVo implements IValueObject {

    Long userId;

    String tel;

    String account;

    String wechatOfficAccount;

    Boolean isActive;

    Long id;

    public BargainirgUserVo(){}

    public BargainirgUserVo(Long userId, String tel, String account, String wechatOfficAccount, boolean isActive){
        this.userId = userId;
        this.tel = tel;
        this.account = account;
        this.wechatOfficAccount = wechatOfficAccount;
        this.isActive = isActive;
    }

}
