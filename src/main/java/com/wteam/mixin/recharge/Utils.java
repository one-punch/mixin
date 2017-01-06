package com.wteam.mixin.recharge;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Stream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.wteam.mixin.constant.Configs;

public class Utils {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(Utils.class.getName());
    
    /**
     * 
     * @param CONFIGS
     * @param name
     * @param propertieArray
     */
    public static void config(Map<String, Map<String, String>> CONFIGS, String name, String... propertieArray) {
        CONFIGS.put(name, new HashMap<>());
        final Properties properties = Configs.prop;
        LOG.debug("properties: {},propertieArray: {}",properties, propertieArray);
        if (properties != null) {
            Stream.of(propertieArray).forEach(propertie -> 
            CONFIGS.get(name).put(propertie, properties.getProperty(name + "." + propertie)));
        }
        LOG.debug("name: {},config:{}",name,CONFIGS.get(name));
    }
    
}
