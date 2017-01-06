package com.wteam.mixin.constant;

import java.io.File;
import java.util.Properties;

import com.wteam.mixin.utils.FileUtils;

/**
 * 
 * @author 微信配置信息
 *
 */
public class WechatConfigs {
    
    /**配置文件*/
    public static Properties prop;
    
    public static Properties self_menu_prop;
    
    /**
     * 
     */
    public static File apiclient_cert;
    
    // 初始化
    static { init(); }
    
    public static void init() {
        prop = FileUtils.getConfigProperties("./wechat/config.properties");
        self_menu_prop = FileUtils.getConfigProperties("./wechat/self-menu.properties");
        apiclient_cert = FileUtils.getConfigFile("./wechat/apiclient_cert.p12");
    }
}
