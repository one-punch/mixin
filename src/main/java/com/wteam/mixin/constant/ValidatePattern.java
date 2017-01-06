package com.wteam.mixin.constant;

import java.util.regex.Pattern;

/**
 * 请求数据格式验证的正则表达式字符串
 * @version 1.0
 * @author benko
 * @time
 */
public class ValidatePattern {

	/**
	 * 手机号码正则表达式.
	 */
	public static final String TEL = "1+\\d{10}";
	/**
	 * 短信验证码正则表达式.
	 */
	public static final String SMS_CODE = "[\\w\\d]{6}";
	/**
	 * md5 32位正则表达式.
	 */
	public static final String MD532 = "[\\w\\d]{32}";
	/**
	 * 纯字母正则表达式.
	 */
	public static final String LETTER = "[A-Za-z]*";
	/**
	 * url正则表达式.
	 */
	public static final String URL = "(http|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)"
			+ "+([\\w\\u4e00-\\u9fa5\\-\\.,@!\\[\\]?^=%&amp;:/~\\+#]*"
			+ "[\\w\\u4e00-\\u9fa5\\-\\@!\\[\\]?^=%&amp;/~\\+#])?";
	/**
	 * JSON正则表达式.
	 */
	public static final String JSON = "\\{(\"(.*)\":(.*))?((,\"(.*)\":(.*))*)" + "\\}";
	/**
	 * 时间格式正则表达式.
	 */
	public static final String DATATIME= "[\\d]{4}-[\\d]{2}-[\\d]{2}\\s[\\d]{2}:[\\d]{2}:[\\d]{2}";
	/**
	 * 纯时间格式正则表达式.
	 */
	public static final String TIME = "[\\d]{2}:[\\d]{2}:[\\d]{2}";
	/**
	 * 纯中文正则表达式.
	 */
	public static final String CN = "[\\u4e00-\\u9fa5]*";

    /**
     * 验证格式是否正确
     *
     * @param regex
     *            所要验证的类型的正则表达式
     * @param data
     *            所要验证的数据
     * @return 验证结果
     */
    public static boolean valid(String regex, String data) {
        if (data == null) return false;
        return Pattern.compile(regex).matcher(data).matches();
    }
}
