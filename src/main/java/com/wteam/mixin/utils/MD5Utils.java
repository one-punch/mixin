package com.wteam.mixin.utils;


import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5加密
 * 〈功能详细描述〉
 * @author Administrator
 * @version 2016年6月2日
 * @see MD5Utils
 * @since
 */
public class MD5Utils {

    /**
     * md5加密算法.
     * 
     * @param plainText
     *            加密原文
     * @return 加密后密文
     */
    public static String md5(final String plainText) {
        byte[] secretBytes = null;
        try {
            secretBytes = MessageDigest.getInstance("md5").digest(plainText.getBytes());
        }
        catch (final NoSuchAlgorithmException e) {
            throw new RuntimeException("登陆出现错误！");
        }
        return byteArrayToHexString(secretBytes);
    }

    /** 16进制字符 */
    private static final String[] HEX_DIGITS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
        "a", "b", "c", "d", "e", "f"};

    /**
     * 转换字节数组为16进制字串
     * 
     * @param b
     *            字节数组
     * @return 16进制字串
     */
    public static String byteArrayToHexString(byte[] b) {
        StringBuilder resultSb = new StringBuilder();
        for (byte aB : b) {
            resultSb.append(byteToHexString(aB));
        }
        return resultSb.toString();
    }

    /**
     * 转换byte到16进制
     * 
     * @param b
     *            要转换的byte
     * @return 16进制格式
     */
    private static String byteToHexString(byte b) {
        int n = b;
        if (n < 0) {
            n = 256 + n;
        }
        int d1 = n / 16;
        int d2 = n % 16;
        return HEX_DIGITS[d1] + HEX_DIGITS[d2];
    }

    /**
     * 微信支付所需的MD5加密
     * 
     * @param origin
     *            需要加密String
     * @param characterEncoding
     *            加密后编码
     * @return 加密数据
     */
    public static String md5Encode(String origin, String characterEncoding) {

        String resultString = null;
        try {
            resultString = new String(origin);
            MessageDigest md = MessageDigest.getInstance("MD5");
            if (characterEncoding == null || "".equals(characterEncoding))
                resultString = byteArrayToHexString(md.digest(resultString.getBytes()));
            else
                resultString = byteArrayToHexString(
                    md.digest(resultString.getBytes(characterEncoding)));
        }
        catch (Exception exception) {}
        return resultString;
    }
}
