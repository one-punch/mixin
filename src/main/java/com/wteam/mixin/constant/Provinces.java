package com.wteam.mixin.constant;

import java.util.Arrays;
import java.util.List;

/**
 *
 * @author benko
 * @version 1.0
 * @time 2016-08-01 19:23:44
 */
public class Provinces {
    /** 北京 */
    public static final  String BJ = "北京";
    /** 天津 */
    public static final  String TJ = "天津";
    /** 河北 */
    public static final  String HE = "河北";
    /** 山西 */
    public static final  String SX = "山西";
    /** 内蒙古 */
    public static final  String LMG = "内蒙古";
    /** 辽宁 */
    public static final  String LL = "辽宁";
    /** 吉林 */
    public static final  String JL = "吉林";
    /** 黑龙江 */
    public static final  String HLJ = "黑龙江";
    /** 上海 */
    public static final  String SH = "上海";
    /** 江苏 */
    public static final  String JS = "江苏";
    /** 浙江 */
    public static final  String ZJ = "浙江";
    /** 安徽 */
    public static final  String AF = "安徽";
    /** 福建 */
    public static final  String FJ = "福建";
    /** 江西 */
    public static final  String JX = "江西";
    /** 山东 */
    public static final  String SD = "山东";
    /** 河南 */
    public static final  String HA = "河南";
    /** 湖北 */
    public static final  String HB = "湖北";
    /** 湖南 */
    public static final  String HN = "湖南";
    /** 广东 */
    public static final  String GD = "广东";
    /** 广西 */
    public static final  String GX = "广西";
    /** 海南 */
    public static final  String HI = "海南";
    /** 重庆 */
    public static final  String CQ = "重庆";
    /** 四川 */
    public static final  String SC = "四川";
    /** 贵州 */
    public static final  String GZ = "贵州";
    /** 云南 */
    public static final  String YN = "云南";
    /** 西藏 */
    public static final  String XZ = "西藏";
    /** 陕西 */
    public static final  String SN = "陕西";
    /** 甘肃 */
    public static final  String GS = "甘肃";
    /** 青海 */
    public static final  String QH = "青海";
    /** 宁夏 */
    public static final  String NX = "宁夏";
    /** 新疆 */
    public static final  String XJ = "新疆";
    /** 全国 */
    public static final  String QG = "全国";

    /** 省份列表 */
    public static final List<String> all = Arrays.asList(
        new String[] {BJ, TJ, HE, SX, LMG, LL, JL, HLJ, SH, JS, ZJ, AF, FJ, JX, SD, HA, HB, HN, GD,
            GX, HI, CQ, SC, GZ, YN, XZ, SN, GS, QH, NX, XJ, QG});


    public static String getFieldName(String fieldValue) {
        if (fieldValue == null) {
            return null;
        }
        String[] name = {null};
        Arrays.asList(Provinces.class.getDeclaredFields())
        .forEach(field -> {
            try {
                if (fieldValue.equals(field.get(Provinces.class))) {
                    name[0] = field.getName();
                }
            }
            catch (Exception e) {
            }
        });

        return name[0];
    }
}
