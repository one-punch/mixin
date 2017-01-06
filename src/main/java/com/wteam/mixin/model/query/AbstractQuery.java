package com.wteam.mixin.model.query;

import static com.wteam.mixin.model.query.IQuery.addPrefix;
import static com.wteam.mixin.model.query.IQuery.prefix;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * 抽象查询语句生成类
 * @author benko
 *
 */
public abstract class AbstractQuery implements IQuery {

    /**排序字段集合 Map<字段, 排序方式>*/
    protected Map<String, String> sortFieldMap = new  LinkedHashMap<>();

    /**正序，排序*/
    private final static String ASC = "asc", DESC = "desc";


    @Override
    public String hqlWhereQuery(String as) {
        String query = hqlQuery(as);
        return "".equals(query) ? query : " where " + query;
    }

    @Override
    public String hqlSorted(String as) {
        String name = as != null && !"".equals(as) ? as+"." : "";
        StringBuilder sorted = new StringBuilder();

        sortFieldMap.forEach((field, sort) -> sorted.append(String.format(" %s%s %s,", name, field, sort)));
        // 去掉最后一个“,”
        if (sorted.length() > 0) {
            sorted.replace(sorted.length()-1, sorted.length(), " ");
        }

        return sorted.toString();
    }

    /**
     * 将 hqlSorted()方法生成的排序语句拼接起来；
     * @param sorts
     * @return
     */
    public static String hqlSort(String... sorts) {
        if (sorts.length == 0) return "";

        Predicate<String> bank = sort -> !"".equals(sort);
        String fisrt = Stream.of(sorts).filter(bank).findFirst().orElse("");
        if ("".equals(fisrt)) return "";

        String next = Stream.of(sorts).filter(bank).skip(1).map(prefix.apply(", ")).reduce((a,b) -> a + b).orElse("");

        return addPrefix.apply(" order by ", fisrt + next) + " ";
    }

    @Override
    public IQuery putSortField(String field) {
        if (field != null) {
            sortFieldMap.put(field, ASC);
        }
        return this;
    }

    @Override
    public IQuery putSortField(String field, boolean isAsc) {
        if (field != null) {
            sortFieldMap.put(field, isAsc ? ASC : DESC);
        }
        return this;
    }


    public static void main(String[] args) {

        StringBuilder sorted = new StringBuilder();

        System.out.println("".equals(sorted.toString()));

        System.out.println(hqlSort(""));
        System.out.println(hqlSort("p.as"));
        System.out.println(hqlSort("p.as", "g.a"));
        System.out.println(hqlSort("", "g.a"));
        System.out.println(hqlSort("p.as", ""));
        System.out.println(hqlSort("", ""));
    }

}
