/*
 * 鏂囦欢鍚嶏細TestJSON.java
 * 鐗堟潈锛欳opyright by wteam鍥㈤槦
 * 鎻忚堪锛�
 * 淇敼浜猴細鍞愪寒
 * 淇敼鏃堕棿锛�2016骞�6鏈�3鏃� 涓嬪崍6:41:59
 * 璺熻釜鍗曞彿锛�
 * 淇敼鍗曞彿锛�
 * 淇敼鍐呭锛�
 */

package smsh.utils;

import java.math.BigDecimal;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.wexin.WcPayUniformOrderRespBody;

/**
 * @author Administrator
 * @version 2016骞�6鏈�3鏃�
 * @see TestJSON
 * @since
 */

public class TestJSON {

    @Test
    public void json() {
//        String json = "{\"resultcode\":\"200\",\"reason\":\"Successed!\",\"result\":{\"data\":[{\"id\":\"61181\",\"name\":\"妗ヨタ鍔犳补绔橽",\"area\":\"524100\",\"areaname\":\"骞夸笢鐪� 婀涙睙甯� 楹荤珷鍖篭",\"address\":\"骞夸笢鐪佹箾姹熷競楹荤珷鍖烘箹鍏夐晣373鐪侀亾鏃у幙澶ч槦鐣滅墽鍦鸿矾杈筡",\"brandname\":\"涓嶈\",\"type\":\"鍏朵粬\",\"discount\":\"闈炴墦鎶樺姞娌圭珯\",\"exhaust\":\"鍥解參\",\"position\":\"110.2710996228,21.1237703546\",\"lon\":\"110.27754723779\",\"lat\":\"21.130066277\",\"price\":{\"E90\":\"5.55\",\"E93\":\"5.98\",\"E97\":\"6.48\",\"E0\":\"5.56\"},\"gastprice\":{\"92#\":\"5.58\",\"0#杞︽煷\":\"5.15\"},\"fwlsmc\":\"\",\"distance\":4402},{\"id\":\"31654\",\"name\":\"涓煶娌规柊鏄岃揪鍔犳补绔橽",\"area\":\"524100\",\"areaname\":\"骞夸笢鐪� 婀涙睙甯� 楹荤珷鍖篭",\"address\":\"骞夸笢鐪佹箾姹熷競楹荤珷鍖烘箹鍏夐晣373鐪侀亾瑗垮箔鏉戣矾娈�,璺寳\",\"brandname\":\"涓煶娌筡",\"type\":\"鍏朵粬\",\"discount\":\"鎵撴姌鍔犳补绔橽",\"exhaust\":\"鍥解參\",\"position\":\"110.2867096367,21.1156877533\",\"lon\":\"110.299756\",\"lat\":\"21.12838\",\"price\":{\"E90\":\"5.55\",\"E93\":\"5.98\",\"E97\":\"6.48\",\"E0\":\"5.56\"},\"gastprice\":{\"92#\":\"5.58\",\"0#杞︽煷\":\"5.15\"},\"fwlsmc\":\"閾惰仈鍗�,淇＄敤鍗℃敮浠�,鍔犳补鍗�,渚垮埄搴梊",\"distance\":3386},{\"id\":\"61180\",\"name\":\"涓滃崕鍔犳补绔橽",\"area\":\"524100\",\"areaname\":\"骞夸笢鐪� 婀涙睙甯� 楹荤珷鍖篭",\"address\":\"骞夸笢鐪佹箾姹熷競楹荤珷鍖烘箹鍏夐晣373鐪侀亾瑗垮箔鏉戣矾鍙�,閭斂鍌ㄨ搫閾惰鏃佽竟\",\"brandname\":\"涓嶈\",\"type\":\"鍏朵粬\",\"discount\":\"闈炴墦鎶樺姞娌圭珯\",\"exhaust\":\"鍥解參\",\"position\":\"110.2947838577,21.1222314332\",\"lon\":\"110.30124097924\",\"lat\":\"21.128469040321\",\"price\":{\"E90\":\"5.55\",\"E93\":\"5.98\",\"E97\":\"6.48\",\"E0\":\"5.56\"},\"gastprice\":{\"92#\":\"5.58\",\"0#杞︽煷\":\"5.15\"},\"fwlsmc\":\"\",\"distance\":3342},{\"id\":\"61176\",\"name\":\"鎭掓腐鍔犳补绔橽",\"area\":\"524100\",\"areaname\":\"骞夸笢鐪� 婀涙睙甯� 楹荤珷鍖篭",\"address\":\"骞夸笢鐪佹箾姹熷競楹荤珷鍖烘箹鍏夐晣293鐪侀亾涓滅敯鏉戝崄瀛楄矾鍙ｅ\",\"brandname\":\"涓嶈\",\"type\":\"鍏朵粬\",\"discount\":\"闈炴墦鎶樺姞娌圭珯\",\"exhaust\":\"鍥解參\",\"position\":\"110.3149169692,21.1338935013\",\"lon\":\"110.32144620482\",\"lat\":\"21.13981123759\",\"price\":{\"E90\":\"5.55\",\"E93\":\"5.98\",\"E97\":\"6.48\",\"E0\":\"5.56\"},\"gastprice\":{\"92#\":\"5.58\",\"0#杞︽煷\":\"5.15\"},\"fwlsmc\":\"\",\"distance\":2469},{\"id\":\"31653\",\"name\":\"涓煶娌规箹鍏夊姞娌圭珯\",\"area\":\"524100\",\"areaname\":\"骞夸笢鐪� 婀涙睙甯� 楹荤珷鍖篭",\"address\":\"骞夸笢鐪佹箾姹熷競楹荤珷鍖烘箹鍏夐晣373鐪侀亾娴峰熬鏉戣矾娈�,璺崡\",\"brandname\":\"涓煶娌筡",\"type\":\"鍏朵粬\",\"discount\":\"鎵撴姌鍔犳补绔橽",\"exhaust\":\"鍥解參\",\"position\":\"110.3112624069,21.1194350363\",\"lon\":\"110.324916\",\"lat\":\"21.131317\",\"price\":{\"E90\":\"5.55\",\"E93\":\"5.98\",\"E97\":\"6.48\",\"E0\":\"5.56\"},\"gastprice\":{\"92#\":\"5.58\",\"0#杞︽煷\":\"5.15\"},\"fwlsmc\":\"閾惰仈鍗�,淇＄敤鍗℃敮浠�,鍔犳补鍗�,渚垮埄搴�,鍙戝崱鍏呭�肩綉鐐�,閾惰仈鍗″厖鍊�,鍔犳补鍗″厖鍊间笟鍔",\"distance\":3458},{\"id\":\"61178\",\"name\":\"楣挎簮鍔犳补绔橽",\"area\":\"524100\",\"areaname\":\"骞夸笢鐪� 婀涙睙甯� 楹荤珷鍖篭",\"address\":\"骞夸笢鐪佹箾姹熷競楹荤珷鍖烘箹鍏夐晣373鐪侀亾楣挎笟鏉�,婀涙睙楣挎笟瀛︽牎瀵归潰\",\"brandname\":\"涓嶈\",\"type\":\"鍏朵粬\",\"discount\":\"闈炴墦鎶樺姞娌圭珯\",\"exhaust\":\"鍥解參\",\"position\":\"110.3471372898,21.1370225656\",\"lon\":\"110.35371131465\",\"lat\":\"21.142717731956\",\"price\":{\"E90\":\"5.55\",\"E93\":\"5.98\",\"E97\":\"6.48\",\"E0\":\"5.56\"},\"gastprice\":{\"92#\":\"5.58\",\"0#杞︽煷\":\"5.15\"},\"fwlsmc\":\"\",\"distance\":5070}],\"pageinfo\":{\"pnums\":20,\"current\":1,\"allpage\":1}},\"error_code\":0}";
//        
//        JSON.parseObject(json);
//        System.out.println(JSON.parseObject(json));

        JSONObject wechat_pay_params = null;                    
        WcPayUniformOrderRespBody wcPay = new WcPayUniformOrderRespBody(null,
            null, null, "asdfasdfas");
        // 参数签名
        wcPay.setPaySign("asdfasdf");
        wechat_pay_params = (JSONObject)JSON.toJSON(wcPay);
        System.out.println(wechat_pay_params.toJSONString());
    }
    
    @Test
    public void jsonBigDecimal() {
        String string = "话费套餐 广东移动话费50元（早上11点之前下单当天晚上到，11点之后隔天晚上到，周末下单周一到，停机不能充、)";
        System.out.println(string.length());
        CustomerOrderPo po = new CustomerOrderPo();
        po.setCost(new BigDecimal("0.12"));
        String json = JSON.toJSONString(po);
        
        System.out.println(json);
        
        CustomerOrderPo po1 = JSON.parseObject(json, CustomerOrderPo.class);
        System.out.println(po1.getCost());
        
    }
    
    
}
