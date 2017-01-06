package com.wteam.mixin.constant;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Properties;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wteam.mixin.utils.FileUtils;

/**
 * 平台的配置信息
 * @author benko
 *
 */
public class Configs {

    /** 从未结算到已结算的间隔 */
    public static final String settlemet_interval = "settlemet.interval";

    /** 任务运行的间隔 */
    public static final String task_interval = "task.interval";


    /** 商家id为空 */
    public static final Long businessId_null = -1L;


    /**配置文件*/
    public static Properties prop;

    // 初始化
    static { init(); }

    public static void init() {
        SerializeConfig.getGlobalInstance().put(Long.class, longSerializer);
        prop = FileUtils.getConfigProperties("./user-config/config.properties");
    }


    static ObjectSerializer longSerializer = new ObjectSerializer() {

        @Override
        public void write(JSONSerializer serializer, Object object, Object fieldName,
                          Type fieldType, int features)
            throws IOException {
            SerializeWriter out = serializer.getWriter();
            if ( object == null ) {
                if ( out.isEnabled(SerializerFeature.WriteNullNumberAsZero) ) {
                    out.write("0");
                } else {
                    out.writeNull();
                }
                return;
            }
            out.writeString(object.toString());
        }

    };

}
