package com.wteam.mixin.constant;

/**
 * 流量的运营商
 * @author benko
 *
 */
public enum Provider {

    YiDong("中国移动", "YD"),LingTong("中国联通", "LT"),DianXin("中国电信", "DX");

    /** 名字*/
    public final String name;
    /** 拼音缩写*/
    public final String acronym;
    /** ID*/
    public final int id;

    private Provider(String name, String acronym) {
        this.name = name;
        this.acronym = acronym;
        this.id = this.ordinal();
    }

    public static Provider get(String name) {

        for (int i = 0; i < values().length; i++ ) {
            if (values()[i].name.equals(name)) {
                return values()[i];
            }
        }
        return null;
    }

    public static Provider get(int id) {
        return values()[id];
    }

}
