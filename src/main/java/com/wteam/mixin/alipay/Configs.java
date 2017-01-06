package com.wteam.mixin.alipay;

import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wteam.mixin.utils.FileUtils;

public class Configs {
    private static Logger LOG = LogManager.getLogger(Configs.class.getName());


    public static String appId = "2016073001685719";
    /** 合作伙伴身份（PID） */
    public static String pid = "2016073001685719";
    public static String privateKeyPkcs8 = "MIICeAIBADANBgkqhkiG9w0BAQEFAASCAmIwggJeAgEAAoGBANgH3gCgNzAJ13qZY7WtsPbJ513X10616D7yQfOHLT4Oa72rtUk2ZLaq+t2yTfH//2ISTJYVaqFuMHyPJkth9RL4rvwfftwTl2qr9qb00sg2E0ScRsM9/XmWBO6+kA1PVqi45MQU3SNdgzUoLJECzWfofJvYRzc1h3+SRX5aAmHfAgMBAAECgYEAl6WmJMBTHUi7V1eX0/ASmpsO7CRZAurVFmLPAxmwG7DZ1vJTNwqU1lnd8oR5DNkMuBZqZqywBpQoOzftxNsFwjlAYunvvszbM/HAIJU5Plw10zm21JaRwkJbewVAzdM9OgOsb4i1USdwxkfTyoQpYtmsKlQneDz7lS3M2mtajkECQQD/XI2/x150VW/saSxO8+MfKmLxb+/Q6EF70rVhZm+FV7YoLicIazSG+BFG9JGgXgDWtSK4ZoIWakNZWz6bxR1hAkEA2JIjtMSodfDCb0QaLYgFwFFWhto9qdi148aV7sdAEdFsCSgoghTwG68pVGlQmaz1UqgsFKeyJFciVtSI30KHPwJBALIDXTVR0ECcxpcxghfP4IZ7T9orRCyHnA6rhpHNjPSRfoRoGAHAai+kgrbKCzKWFVOXlgqiOxFRJn4GI5EHSkECQQCVSWfDnCxj4GqnOFKzQCh2wZrbqmUHR5NaR/HifwbzQWEvjrcdtEEvVDcxMMeWp1sUd0irlE2AL3BEWo1fGQURAkAJxC51MiE6S/0H8rs9mH7fW5uZOVT+CU8Vx1I/1091AFi5EF4rwgiLMZsIRqZS66s9ahMZcCAdgCAhGhD19G9Q";
    public static final String format = "JSON";
    public static final String charset = "utf-8";
    public static String alipayPulicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

    /** 合作伙伴 md5key*/
    public static String md5key = "m1pzj00mua97alxhneakqrubr0zrpak9";
    /** 主网址 */
    public static String host = "2016073001685719";
    /** 即时付钱 同步通知页面路径） */
    public static String return_url = "2016073001685719";
    /** 即时付钱 异步通知页面路径 */
    public static String notify_url = "2016073001685719";
    // 初始化
    static { init(); }
    
    public static void init() {
        Properties properties = FileUtils.getConfigProperties("./alipay/config.properties");
        if (properties != null) {
            appId = properties.getProperty("appId");
            privateKeyPkcs8 = properties.getProperty("private_key_pkcs8");
            alipayPulicKey = properties.getProperty("alipay_pulic_key");
            pid = properties.getProperty("pid");
            return_url = properties.getProperty("return_url");
            notify_url = properties.getProperty("notify_url");
            host = properties.getProperty("host");
            md5key = properties.getProperty("md5key");
        }
        LOG.debug("properties:{}",properties);
    }
}
