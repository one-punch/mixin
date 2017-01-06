package com.wteam.mixin.utils;


import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;


/**
 * 常用的和无法分类的工具类
 *
 * @version 1.0
 * @author benko
 * @time
 */
public class Utils {

    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(Utils.class.getName());

    /**
     * 生成6位短信随机验证码
     *
     * @return 字符串
     */
    public static String noteCode() {
        return (int)((Math.random() * 9 + 1) * 100000) + "";
    }

    /**
     * 会员是否有效
     *
     * @param startAt
     *            开始时间
     * @param vaildity
     *            有效天数
     * @return
     */
    public static boolean isMemberVail(Date startAt, Long vaildity) {
        Instant instant = startAt.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDate end = LocalDateTime.ofInstant(instant, zone).plusDays(vaildity).toLocalDate();

        return LocalDate.now().compareTo(end) >= 0 ? false : true;
    }

    public static String[] getInfoByPhone(String phone){

        String[] result = {null,null,null};
        int sum = 2;

        for (int i = 0; i < sum && result[0] == null; i++ ) {
            result = getInfoByPhone4(phone);
        }
        for (int i = 0; i < sum && result[0] == null; i++ ) {
            result = getInfoByPhone3(phone);
        }

        return result;
    }

    /**
     * 根据手机生成号段
     * @param phone
     * @return
     */
    public static String getHaoDan(String phone) {
        String[] info = getInfoByPhone(phone);
        return String.format("%s-%s-%s", info);
    }
    /**
     * 根据手机获取省份和运营商
     * 不稳定：成功率80%，200次/s
     * @param phone
     * @return {省份，运营商}
     */
    public static String[] getInfoByPhone1(String phone) {
        String apikey = "de9d99bbc02cf73ffe9c4c0bec16d538",
            url = "http://apis.baidu.com/apistore/mobilephoneservice/mobilephone";

        String param = HttpRequest.params("tel", phone);
        JSONObject jsonObject = baiduPhone(param, apikey, url);
        String province = null, provider = null ;
        if ("0".equals(jsonObject.getString("errNum"))) {
            province = (String)JSONPath.eval(jsonObject, "$.retData.province");
            provider = (String)JSONPath.eval(jsonObject, "$.retData.carrier");
            provider = provider.substring(provider.length() - 2);
        }

        return new String[] { province, provider, null };
    }
    /**
     * 根据手机获取省份和运营商
     * 不稳定：成功率50%，200次/s
     * @param phone
     * @return {省份，城市，运营商}
     */
    public static String[] getInfoByPhone2(String phone) {
        String apikey = "de9d99bbc02cf73ffe9c4c0bec16d538",
            url = "http://apis.baidu.com/apistore/mobilenumber/mobilenumber";

        String param = HttpRequest.params("phone", phone);
        JSONObject jsonObject = baiduPhone(param, apikey, url);

        String province = null, provider = null, city = null ;
        if ("0".equals(jsonObject.getString("errNum"))) {
            province = (String)JSONPath.eval(jsonObject, "$.retData.province");
            city = (String)JSONPath.eval(jsonObject, "$.retData.city");
            provider = (String)JSONPath.eval(jsonObject, "$.retData.supplier");
        }
        return new String[] { province, provider, city };
    }
    /**
     * 根据手机获取省份和运营商
     * 10次/s
     * @param phone
     * @return {省份，城市，运营商}
     */
    public static String[] getInfoByPhone3(String phone) {
        String apikey = "de9d99bbc02cf73ffe9c4c0bec16d538",
            url = "http://apis.baidu.com/showapi_open_bus/mobile/find";

        String param = HttpRequest.params("num", phone);
        JSONObject jsonObject = baiduPhone(param, apikey, url);

        String province = null, provider = null, city = null ;
        System.out.println(jsonObject);
        if ("0".equals(jsonObject.getString("showapi_res_code"))) {
            province = (String)JSONPath.eval(jsonObject, "$.showapi_res_body.prov");
            city = (String)JSONPath.eval(jsonObject, "$.showapi_res_body.city");
            provider = (String)JSONPath.eval(jsonObject, "$.showapi_res_body.name");
        }

        return new String[] { province, provider, city};
    }

    /**
     * 根据手机获取省份和运营商 聚合数据
     *
     * @param phone
     * @return {省份，城市，运营商}
     */
    public static String[] getInfoByPhone4(String phone) {
        String apikey = "caf1166c34ef0206f87afd5f3a945307",
            url = "http://apis.juhe.cn/mobile/get";

        String param = HttpRequest.params("phone", phone, "key", apikey);
        String result = HttpRequest.sendGet(url, param);
        JSONObject jsonObject = new JSONObject();
        if (LOG.isDebugEnabled()) LOG.debug("result:{}", result);
        try {
            result = convert(result); // 将Unicode编码转换为汉字
            if(!"".equals(result)) jsonObject = JSON.parseObject(result);
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        String province = null, provider = null, city = null ;
        if ("200".equals(jsonObject.getString("resultcode"))) {
            province = (String)JSONPath.eval(jsonObject, "$.result.province");
            city = (String)JSONPath.eval(jsonObject, "$.result.city");
            provider = (String)JSONPath.eval(jsonObject, "$.result.company");
            provider = provider.substring(provider.length() - 2);
        }

        return new String[] { province, provider, city };
    }

    /**
     * 百度API 根据手机获取省份和运营商
     *
     * @param phone
     * @return {省份，城市，运营商}
     */
    public static JSONObject baiduPhone(String param, String apikey, String url) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("apikey", apikey);

        String result = HttpRequest.sendGet(url, param, headers);
        result = convert(result); // 将Unicode编码转换为汉字
        if (LOG.isDebugEnabled()) LOG.debug("result:{}", result);
        try {
            if(!"".equals(result))
                return JSON.parseObject(result);
        }
        catch (Exception e) {
            // TODO: handle exception
        }
        return new JSONObject();
    }

    /**
     * 将Unicode编码转换为汉字
     *
     * @param utfString
     * @return
     */
    public static String convert(String theString) {
        if (theString == null) {
            return null;
        }
        char aChar;

        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);

        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++ );

            if (aChar == '\\') {
                aChar = theString.charAt(x++ );
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++ ) {
                        aChar = theString.charAt(x++ );
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                    "Malformed   \\uxxxx   encoding.");
                        }

                    }
                    outBuffer.append((char)value);
                }
                else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            }
            else
                outBuffer.append(aChar);
        }

        return outBuffer.toString();
    }

}
