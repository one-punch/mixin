package com.wteam.mixin.recharge;

import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class HttpRequest extends com.wteam.mixin.utils.HttpRequest {
    
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(HttpRequest.class.getName());

    /**
     * 将参数排序并拼接成请求参数
     * @param map
     * @param excludeKeys
     * @return
     */
    public static String sortParams(Map<String, String> map, List<String> excludeKeys) {
        String[] param = {""} ;
        String result;
        map.keySet().stream().sorted().forEach(key -> {
            if(excludeKeys == null || !excludeKeys.contains(key) )
                param[0] += "&" + key + "=" + (map.get(key) != null ? map.get(key) : "");
        });
        result = param[0].replaceFirst("&", "");
        if(LOG.isDebugEnabled()) LOG.debug(result);
        return result;
    }
    
    public static void main(String[] args) {
        System.out.println(params(new String[]{"1","2","3","4"}));
        System.out.println(md5("asdf") + "->" + md5("asdf").length());
    }
}
