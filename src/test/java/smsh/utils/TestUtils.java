package smsh.utils;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.JSONPath;
import com.wteam.mixin.utils.Utils;

public class TestUtils {

    @Test
    public void getInfoByPhone() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1; i++ ) {
            String[] arr =  Utils.getInfoByPhone("13750371474");
            System.out.println(JSON.toJSONString(arr));
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start )/1000);
    }
    @Test
    public void getInfoByPhone1() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++ ) {
            String[] arr =  Utils.getInfoByPhone1("18320376671");
            System.out.println(JSON.toJSONString(arr));
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start )/1000);
    }
    @Test
    public void getInfoByPhone2() {
        for (int i = 0; i < 10; i++ ) {
            String[] arr =  Utils.getInfoByPhone2("18320376671");
            System.out.println(JSON.toJSONString(arr));
        }
    }
    @Test
    public void getInfoByPhone3() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++ ) {
            String[] arr =  Utils.getInfoByPhone3("18320376671");
            System.out.println(JSON.toJSONString(arr));
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start )/1000);
    }
    @Test
    public void getInfoByPhone4() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10; i++ ) {
            String[] arr =  Utils.getInfoByPhone4("18320376671");
            System.out.println(JSON.toJSONString(arr));
        }
        long end = System.currentTimeMillis();
        System.out.println((end - start )/1000);
    }
    

    @Test
    public void test() {
        String json = "{\"showapi_res_error\":\"\",\"showapi_res_body\":{\"provCode\":\"440000\",\"city\":\"湛江\",\"num\":1832037,\"name\":\"移动\",\"type\":1,\"ret_code\":0,\"prov\":\"广东\"},\"showapi_res_code\":0}";
        JSONObject jsonObject = JSONObject.parseObject(json);
        System.out.println(jsonObject.get("showapi_res_code").getClass());
        System.out.println(JSONPath.eval(jsonObject, "$.showapi_res_code").getClass());
        
        jsonObject = new JSONObject();
        System.out.println(jsonObject.get("showapi_res_code"));
        System.out.println(jsonObject.getString("showapi_res_code"));
        
        System.out.println("0".equals(null));

        jsonObject = JSON.parseObject("");
        System.out.println(jsonObject.get("showapi_res_code"));
        System.out.println(jsonObject.getString("showapi_res_code"));
        
    }
    
    
}
