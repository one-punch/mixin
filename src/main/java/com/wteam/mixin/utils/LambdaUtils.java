package com.wteam.mixin.utils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;

public class LambdaUtils {

    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(LambdaUtils.class.getName());
    /**
     * 
     * @param toT
     * @param functions
     * @return
     */
    public static <T,R> Function<List<?>, List<?>> bockMap(Function<T,R> toR, Function<R, R>... functions) {
        return pos -> {
            return pos.stream().map(po -> toR.apply((T)po)).map(LambdaUtils.setMaps(functions)).collect(Collectors.toList());
        };
    }
    
    /**
     * 
     * @param toT
     * @param functions
     * @return
     */
    public static <T,R> Function<List<?>, List<?>> bockFilter(Function<T,R> toR, Predicate<R>... filters) {
        return pos -> {
            return pos.stream().map(po -> toR.apply((T)po)).filter(LambdaUtils.setFilters(filters)).collect(Collectors.toList());
        };
    }
    /**
     * 设置多个转换器
     * @param filters
     * @return
     */
    public static <T> Function<T, T> setMaps(final Function<T, T>... filters) {
        return Arrays.asList(filters).stream()
            .reduce((current, next) -> current.andThen(next))
            .orElseGet(Function::identity);
    }
    
    /**
     * 设置多个过滤器
     * @param filters
     * @return
     */
    public static <T> Predicate<T> setFilters(final Predicate<T>... filters) {
        return Arrays.asList(filters).stream()
            .reduce((current, next) -> current.and(next))
            .orElse(vo -> true);
    }
    
    /**
     * 设置对象转换器
     * @param mapper
     * @param class1
     * @return
     */
    public static <T,R> Function<T, R> mapTo(DozerBeanMapper mapper, Class<R> class1) {
        return po -> {
//            LOG.debug("class1:{},po：{}, mapper:{}", class1,po, mapper);
            return class1.isAssignableFrom(po.getClass()) ? (R)po : mapper.map(po, class1);
        };
    }
}
