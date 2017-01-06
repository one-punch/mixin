package com.wteam.mixin.recharge;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;

import com.wteam.mixin.utils.FileUtils;

/**
 * 易赛 流量充值
 * @version 1.0
 * @author benko
 * @time 2016-08-12
 */
public class YiShaRecharge {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(YiShaRecharge.class.getName());

    private static String UserNumber = "8788119";
    private static String UserSystemKey = "e14737138567d385e5bde7ef25275f8d";
    private static String UserCustomerKey = "";

    private static final SimpleDateFormat FORMAT = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");

    // 初始化
    static { init(); }

    private static void init() {
        Properties properties = FileUtils.getConfigProperties("./user-config/config.properties");
        if (properties != null) {
            UserNumber = properties.getProperty("yisai.UserNumber");
            UserSystemKey = properties.getProperty("yisai.UserSystemKey");
            UserCustomerKey = properties.getProperty("yisai.UserCustomerKey");
        }
        LOG.debug("properties: {},UserNumber: {},UserSystemKey: {},UserCustomerKey:{}",properties,UserNumber,UserSystemKey,UserCustomerKey);
    }

    private YiShaRecharge() {}


    public static YiShaRecharge instance() {
        return new YiShaRecharge();
    }

    /**
     * 充值提现成功：
     * <pre> <Esaipay>
     *     <InOrderNumber>IF8788119201608131721257626919</InOrderNumber>
     *     <OutOrderNumber>1471080062445</OutOrderNumber>
     *     <Result>success</Result>
     *     <Remark>300</Remark>
     *     <RecordKey>8498901111B1DA8C</RecordKey>
     *   </Esaipay>
     * </pre>
     * @param orderId 订单号
     * @param phone 充值的手机
     * @param ProId 流量的pid
     */
    public Document recharge(String orderId,String phone,String ProId) {
        String url = "http://llbchongzhi.esaipai.com/IRecharge_Flow";
        String PayAmount = "1",StartTime = createTime(),TimeOut = "300",Remark = "300";

        String RecordKey = md5(UserNumber+ orderId+ ProId+ phone+ PayAmount+
            StartTime+ TimeOut+ Remark+ UserSystemKey+ UserCustomerKey);

        String param = HttpRequest.params(
            "UserNumber", UserNumber,
            "OutOrderNumber", orderId,
            "ProId", ProId,
            "PhoneNumber", phone,
            "PayAmount", PayAmount,
            "StartTime", StartTime,
            "TimeOut", TimeOut,
            "Remark", Remark,
            "RecordKey", RecordKey
        );

        String result = HttpRequest.sendPost(url, param);

        if (LOG.isDebugEnabled()) LOG.debug("流量响应->{}",result);

        try {
            return result != null ? DocumentHelper.parseText(result) : null;
        }
        catch (DocumentException e) {
            LOG.error("XML格式解析错误",e);
        }
        return null;
    }


    public String createTime() {
        return FORMAT.format(new Date());
    }

    public static String md5(String src) {
        return HttpRequest.md5(src).toUpperCase().substring(0, 16);
    }

    public static void main(String[] args) {

        SimpleDateFormat FORMAT = new SimpleDateFormat("MMddHHmmYYYY.ss");

        System.out.println(FORMAT.format(new Date()));

    }


}
