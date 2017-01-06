package com.wteam.mixin.constant;

/**
 * 短信模板枚举
 *
 * @version 1.0
 * @author benko
 * @time 2016-1-12 17:04:37
 */
public enum SMSTpls {

    /**
     * 测试
     * 参数：用户的手机号 充值的手机号
     */
    Test("【广州孚技网络科技】尊敬的%s用户，您好，您通过[孚云]微信公众号充值的%s已经提交，将在两个小时内到账。如未成功到账，请关注[孚云]公众号查询流量充值进度。"),
    /**
     * 验证码
     * 参数：6位验证码
     */
    NoteCode("【米信网】您的验证码是%s");

    /** 短信内容模板 */
    public final String paramTemplate;

    /**
     * @param paramTemplate 短信内容模板
     */
    private SMSTpls(String paramTemplate) {
        this.paramTemplate = paramTemplate;
    }

    /**
     * String.format根据短信内容模板生成发送信息使用的参数字符串
     *
     * @param params 参数
     * @return 发送文本
     */
    public String param(Object... params) {
        if (params == null) {
            return paramTemplate;
        }
        else {
            return String.format(paramTemplate, params);
        }
    }

    /**
     * String.format根据短信内容模板生成发送信息使用的参数字符串
     * @return 发送文本
     */
    public String param() {
        return param(null);
    }
}
