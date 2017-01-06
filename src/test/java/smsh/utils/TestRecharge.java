package smsh.utils;

import java.util.Date;
import java.util.function.Function;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.recharge.DaHanRecharge;
import com.wteam.mixin.recharge.DeLiRecharge;
import com.wteam.mixin.recharge.JingYieRecharge;
import com.wteam.mixin.recharge.KuaiChongRecharge;
import com.wteam.mixin.recharge.LiuLiangRecharge;
import com.wteam.mixin.recharge.ShanWangRecharge;
import com.wteam.mixin.recharge.ShangTongRecharge;
import com.wteam.mixin.recharge.XiaoZhuRecharge;
import com.wteam.mixin.recharge.XinHaoBaRecharge;
import com.wteam.mixin.recharge.YiKuaiRecharge;
import com.wteam.mixin.recharge.YiShaRecharge;
import com.wteam.mixin.recharge.YouXingRecharge;
import com.wteam.mixin.recharge.DaHanRecharge.Response;
import com.wteam.mixin.utils.HttpRequest;
import com.wteam.mixin.utils.MD5Utils;

/**
 * 测试流量充值
 * @version 1.0
 * @author benko
 * @time 2016-7-27 09:19:03
 */
public class TestRecharge {

    @Test
    public void testXinHaoba() {

        XinHaoBaRecharge xinHaoBa = XinHaoBaRecharge.instance();

        XinHaoBaRecharge.Response json = xinHaoBa.recharge(new Date().getTime()+"", "18320376671", "10022", "10");
        System.out.println(json);
    }



    @Test
    public void testYiSha() throws DocumentException {
        /*
         *
<Esaipay><InOrderNumber>IF8788119201608131721257626919</InOrderNumber><OutOrderNumber>1471080062445</OutOrderNumber><Result>success</Result><Remark>300</Remark><RecordKey>8498901111B1DA8C</RecordKey></Esaipay>
<Esaipay><InOrderNumber></InOrderNumber><OutOrderNumber>1471080261678</OutOrderNumber><Result>attrerr</Result><Remark>字段 PhoneNumber 无效。</Remark><RecordKey>F6029D20024ABDC9</RecordKey></Esaipay>
         */
        YiShaRecharge yisha = YiShaRecharge.instance();
        //Document xml = yisha.recharge(new Date().getTime()+"", "1832037671", "100027");
//        System.out.println(yisha.createTime());

        Document xml = DocumentHelper.parseText("<Esaipay><InOrderNumber>IF8788119201608131721257626919</InOrderNumber><OutOrderNumber>1471080062445</OutOrderNumber><Result>success</Result><Remark>300</Remark><RecordKey>8498901111B1DA8C</RecordKey></Esaipay>");
        String result = xml.getRootElement().elementText("Result");
        System.out.println(result);
    }

    @Test
    public void testJingYie() {
        JingYieRecharge jingyie = JingYieRecharge.instance();

        jingyie.recharge(new Date().getTime()+"", "18320376671", "YD10");
    }
    @Test
    public void testXiaoZhu() {
        XiaoZhuRecharge jingyie = XiaoZhuRecharge.instance();

        jingyie.recharge(new Date().getTime()+"", "18320376671", "RH_156_100M");
    }
    @Test
    public void testLiuLiang() {
        LiuLiangRecharge jingyie = LiuLiangRecharge.instance();

        jingyie.recharge(new Date().getTime()+"", "18320376671", "10");
    }
    @Test
    public void testKuaiChong() {
        KuaiChongRecharge jingyie = KuaiChongRecharge.instance();

        jingyie.recharge("18320376671", "GD1008607200");
    }
    @Test
    public void testDeLi() {
        DeLiRecharge jingyie = DeLiRecharge.instance();

        jingyie.recharge("18320376671", "DLM10", "DLMTY", "0");
    }
    @Test
    public void testShangTong() {
        ShangTongRecharge.Response response = ShangTongRecharge.instance()
            .recharge(new Date().getTime()+"", "18320376671", "10", "0");
    }
    @Test
    public void testDaHan() {
        DaHanRecharge.instance()
            .recharge(new Date().getTime()+"", "18320376671", "10");
    }
    @Test
    public void testYouXing() {
        YouXingRecharge.instance()
            .recharge( "18320376671", "18", new Date().getTime()+"");
    }
    @Test
    public void testShanWang() {
        ShanWangRecharge.instance()
            .recharge(new Date().getTime()+"", "18320376671", "1579");
    }
    @Test
    public void testYouXing2() {
        YouXingRecharge.instance(YouXingRecharge.youxing2)
            .recharge( "18320376671", "18", new Date().getTime()+"");
    }
    @Test
    public void testYiKuai() {
        YiKuaiRecharge.instance()
        .recharge( "18320376671", "10", new Date().getTime()+"", "0");
    }
    @Test
    public void testPost() {
        ResultMessage result = new ResultMessage();
        result.setServiceFailureInfo("充值失败");
        result.putParam("orderNum", "779324072516386816");
        result.putParam("outTradeNum", "2016092322183249540331");
        for (int i = 0; i < 5; i++ ) {
            String resultstr = HttpRequest.sendPostJSON("http://www.llkc8.com/flow_mgt/notify/miXinCallBack.do", JSON.toJSONString(result));
            System.out.println( resultstr);
        }
    }

    @Test
    public void test() {
        // 准备参数
        String appid = "f9631c8bab624bd9f7a9e0ffe0520a56";
        String key = "f3187169fd6c85ef705d410603eba532";
        String tel = "13809714559";
        String type = "1";
        String outTradeNum = "" + new Date().getTime();
        String productNum = "SD_QG_200";
        String timestamp = "2016-09-22 19-03-35";


//        String url = "http://120.24.248.88:8091/recharge/api";
        String url = "http://mixinwang.com/recharge/api";
        // 生成签名
        String sign = MD5Utils.md5(appid + key + tel + type + outTradeNum + productNum + timestamp);
        // 组织参数
        String param = "tel="+ tel +
            "&appid="+ appid +
            "&outTradeNum="+ outTradeNum  +
            "&type="+ type +
            "&productNum="+ productNum +
            "&timestamp="+ timestamp +
            "&sign="+ sign;
        // 发送请求，并接收响应
        String result = HttpRequest.sendPost(url, param);
        System.out.println(result);
    }

    @Test
    public void testDeli_callback() {
        String url = "http://mixinwang.com" + "/recharge/kuaichong/callback";
        KuaiChongRecharge.Response response = new KuaiChongRecharge.Response();
/*        response.setTaskid("1234");
        response.setResult("0");// 成功
//        response.setResult("1");// 失败
        response.setResultdesc("asdfasdfasdf");
        System.out.println(JSON.toJSONString(response));*/
        String result = HttpRequest.sendPostJSON(url, JSON.toJSONString(response));
        System.out.println(result);
    }

    @Test
    public void testKuaiChong_callback() {
        String url = "http://localhost:8080" + "/recharge/kuaichong/callback";
        KuaiChongRecharge.Response response = new KuaiChongRecharge.Response();
        response.setTaskid("1234");
        response.setResult("0");// 成功
//        response.setResult("1");// 失败
        response.setResultdesc("asdfasdfasdf");
        System.out.println(JSON.toJSONString(response));
        String result = HttpRequest.sendPostJSON(url, JSON.toJSONString(response));
        System.out.println(result);
    }

    @Test
    public void testShangTong_callback() {
        String url = "http://localhost:8080" + "/recharge/shangtong/callback";
        ShangTongRecharge.Response response = new ShangTongRecharge.Response();
        response.setOrderno_ID("772138013705633792");
        response.setRespCode("0002");;// 成功
//        response.setOderStat("1");// 失败
        response.setRespMsg("asdfasdfasdf");
        System.out.println(JSON.toJSONString(response));
        String result = HttpRequest.sendPostJSON(url, JSON.toJSONString(response));
        System.out.println(result);
    }


    @Test
    public void testDaHan_send() {
        Function<String, Object> send = result -> {
            try {

                if (result == null || "".equals(result)) {
                    return null;
                }
                else {
                    Object object = JSON.parse(result);
                    System.out.println(object);
                    System.out.println(object.getClass());
                    if (object instanceof JSONObject) {
                        return JSONObject.toJavaObject((JSON)object, Response.class);
                    } else if (object instanceof JSONArray) {
                        return JSON.parseArray(result, Response.class);
                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        };
        String result = "[{\"resultCode\":\"21\",\"resultMsg\":\"余额不足\"}]";
        System.out.println(send.apply(result));
    }

    public static void main(String[] args) {
        System.out.println(new Date(Long.valueOf(1477904572L)*1000));
    }
}
