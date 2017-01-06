package com.wteam.mixin.recharge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流量充值的接口
 * @author benko
 *
 */
public class RechargeProvider {

    /** 无接口，手动充值 */
    public static final  String NULL = "手动";
    /** 新号吧*/
    public static final  String XinHaoBa = "新号吧";
    /** 易 赛*/
    public static final  String YiSai = "易赛";
    /** 进业*/
    public static final  String JingYie = "进业";
    /** 小猪*/
    public static final  String XiaoZhu = "小猪";
    /** 流量*/
    public static final  String LiuLiang = "流量";
    /** 流量*/
    public static final  String KuaiChong = "快充";
    /** 得力*/
    public static final  String DeLi = "得力";
    /** 尚通*/
    public static final  String ShangTong = "尚通";
    /** 大汉*/
    public static final  String DaHan = "大汉";
    /** 友信*/
    public static final  String YouXing = "友信";
    /** 友信2*/
    public static final  String YouXing2 = "友信2";
    /** 三网*/
    public static final  String ShanWang = "三网";
    /** 宜快*/
    public static final  String YiKuai = "宜快";
    
    public static final  String DaZhong = "大众";
    /** all*/
    public static final  List<String> ALL ;

    public static final Map<String, String> MAP ;

    static {
        MAP = new HashMap<>();
        MAP.put(NULL, "SD");
        MAP.put(XinHaoBa, "XHB");
        MAP.put(YiSai, "YS");
        MAP.put(JingYie, "JY");
        MAP.put(XiaoZhu, "XZ");
        MAP.put(LiuLiang, "LL");
        MAP.put(KuaiChong, "KC");
        MAP.put(DeLi, "DL");
        MAP.put(ShangTong, "ST");
        MAP.put(DaHan, "DH");
        MAP.put(YouXing, "YX");
        MAP.put(YouXing2, "YX2");
        MAP.put(ShanWang, "SW");
        MAP.put(YiKuai, "YK");
        MAP.put(DaZhong, "OMS");
        ALL = new ArrayList<>(MAP.keySet());
    }

}
