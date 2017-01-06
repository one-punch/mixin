package com.wteam.mixin.converter;


import org.springframework.core.convert.converter.Converter;
import org.springframework.core.convert.converter.ConverterFactory;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.define.IValueObject;


/**
 * 将 json 格式的字符串转换成实现了的 IValueObject 接口的 javaBean
 *
 * @author benko
 */
public class JsonToVoConverterFactory implements ConverterFactory<String, IValueObject> {

    @Override
    public <T extends IValueObject> Converter<String, T> getConverter(Class<T> targetType) {
        return new JsonToVo<>(targetType);
    }

    private class JsonToVo<T extends IValueObject> implements Converter<String, T> {

        private final Class<T> voClass;

        public JsonToVo(Class<T> voClass) {
            this.voClass = voClass;
        }

        @Override
        public T convert(String source) {
            if (source != null) {
                return JSON.parseObject(source, voClass);
            }
            return null;
        }
    }

}
