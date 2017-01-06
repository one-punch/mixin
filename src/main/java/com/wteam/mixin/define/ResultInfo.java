package com.wteam.mixin.define;


import java.util.HashMap;
import java.util.Map;


/**
 * 基本的返回状态信息
 *
 * @version 1.0
 * @author benko
 * @time
 */
public class ResultInfo {

    /** 默认成功 */
    public static final int SUCCESS = -1;

    /** 默认“系统错误” */
    public static final int SYSTEM_FAIL = 0;

    /** 默认“业务失败” */
    public static final int SERVICE_FAIL = 1;

    /** 默认“验证失败” */
    public static final int VALIDATE_FAIL = 2;

    /** 默认“请求参数不全” */
    public static final int MISSING_REQUEST_PARAM_FAIL = 3;

    /** 未认证  */
    public static final int UNAUTHENTICATED_FAIL = 4;

    /** 认证通过无访问权限  */
    public static final int UNAUTHORIZED_FAIL = 5;

    /** 订单状态修改失败  */
    public static final int ORDER_CHANGE_FAIL = 6;

    /** 商家没有余额  */
    public static final int BUSINESS_NO_BALANCE = 7;

    /** 验证信息 */
    public static final String VALIDATE = "validate";

    /** 返回提示信息 */
    private static Map<Integer, String> INFO = new HashMap<Integer, String>() {
        private static final long serialVersionUID = 1L;
        { // 业务成功
            put(SUCCESS, "\u4E1A\u52A1\u6210\u529F");
            // 系统错误
            put(SYSTEM_FAIL, "\u7CFB\u7EDF\u9519\u8BEF");
            // 业务错误
            put(SERVICE_FAIL, "\u4E1A\u52A1\u9519\u8BEF");
            // 请求数据验证失败
            put(VALIDATE_FAIL, "\u8BF7\u6C42\u6570\u636E\u9A8C\u8BC1\u5931\u8D25");
            // 请求参数不全
            put(MISSING_REQUEST_PARAM_FAIL, "\u8BF7\u6C42\u53C2\u6570\u4E0D\u5168");
            // 未认证
            put(UNAUTHENTICATED_FAIL, "用户没有登录，不能访问此路径");
            // 认证通过无访问权限
            put(UNAUTHORIZED_FAIL, "用户没有权限访问此路径");
            // 订单状态修改失败
            put(ORDER_CHANGE_FAIL, "订单状态修改失败");
            // 商家没有余额
            put(BUSINESS_NO_BALANCE, "商家没有余额 ");
        }
    };

    /**
     * Description: 根据返回码，获取返回提示信息<br>
     *
     * @param key 返回码
     * @return 返回提示信息
     * @see
     */
    public static String getInfo(int key) {
        return ResultInfo.INFO.get(key);
    }
}
