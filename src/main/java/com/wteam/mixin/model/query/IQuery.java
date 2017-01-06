package com.wteam.mixin.model.query;

import java.text.SimpleDateFormat;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 *
 * @author benko
 *
 */
public interface IQuery {

    static final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /** 加后缀 addSuffix.apply("source", ".suffix") -> "source.suffix"*/
    static final BiFunction<String, String, String> addSuffix = (source, suffix) -> "".equals(source) ? "" : source + suffix;

    /** 加前缀 addPrefix.apply("source", "prefix_") -> "prefix_source"*/
    static final BiFunction<String, String, String> addPrefix = (prefix, source) -> "".equals(source) ? "" : prefix + source;

    static final Function<String, Function<String, String>> prefix = prefix -> source -> "".equals(source) ? "" : prefix + source;

    /**
     * 生成hql查询语句
     * @param as po的别名
     * @return
     */
    String hqlQuery(String as);

    /**
     * 生成hql查询语句
     * @param as po的别名
     * @return
     */
    String hqlWhereQuery(String as);

    /**
     * 生成排序语句
     * @param as
     * @return
     */
    String hqlSorted(String as);
    /**
     * 设置排序字段，按插入的顺序排序
     * @param field
     * @return
     */
    IQuery putSortField(String field);

    /**
     * 设置排序字段，按插入的顺序排序
     * @param field
     * @param isAsc true asc, false desc
     * @return
     */
    IQuery putSortField(String field, boolean isAsc);


}
