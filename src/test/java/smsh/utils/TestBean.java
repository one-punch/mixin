package smsh.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;

import com.wteam.mixin.constant.Provinces;
import com.wteam.mixin.constant.State;
import com.wteam.mixin.model.po.CustomerInfoPo;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.shiro.PasswordHelper;
import com.wteam.mixin.utils.MD5Utils;
import com.wteam.mixin.utils.WeChatUtils;

import test.hibernate.generate.MockGenerate;

public class TestBean {
    
    @Test
    public void test() {
        
        
/*        Arrays.asList(CustomerOrderPo.class.getDeclaredFields())
        .forEach(field -> {
            System.out.println(String.format("projections.add(Property.forName(\"o.%s\").as(\"%s\"));", field.getName(), field.getName()));
        });*/
/*        Arrays.asList(State.CustomerOrder.class.getDeclaredFields())
        .forEach(field -> {
            System.out.print(field.getName() + ",");
        });
        */
        UserVo userVo = new UserVo("super", MD5Utils.md5("123456"), "18320376673", "932141029@qq.com");
        encryptUser(userVo);
        System.out.println(userVo);
/*
        Arrays.asList(Provinces.class.getDeclaredFields())
        .forEach(field -> {
            try {
                System.out.print(String.format("{'name':'%s'},", field.get(Provinces.class)));
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });*/
        
        Arrays.asList(Provinces.class.getDeclaredFields())
        .forEach(field -> {
            try {
                System.out.print(String.format("{'name':'%s'},", field.get(Provinces.class)));
            }
            catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        
/*        
        DecimalFormat format = new DecimalFormat("0.00");
        String total_fee = format.format(new BigDecimal("30.1"));
        System.out.println(total_fee);*/
    }
    
    
    @Test
    public void name() {
        String noncestr = WeChatUtils.generateRamdomChar();
        String timestamp = String.valueOf((int)(System.currentTimeMillis() / 1000));
        Map<String, String> params = new HashMap<>();
        params.put("noncestr", "XQYIfMBWRMBBIPGMDCVObSVNJbfQabea");
        params.put("timestamp", "1473917855");
        params.put("jsapi_ticket", "gdzxN2g622vVfv3lEXnBsvj1Dy3o94Snn7fTlyk_wtsi8qMeXZ8iE0d_6Tje_zQJDMXUjETxOMRQh8149QqYJw");
        params.put("url", "http://mixinwang.com/wechat/theme/?businessId=1&menu=recharge");
//      签名
        String ticketSign = WeChatUtils.generateTicketSign(params);
        System.out.println(ticketSign);
    }
    
    private void encryptUser(UserVo userVo) {
        PasswordHelper helper = new PasswordHelper();
        String uuid;
        uuid = UUID.randomUUID().toString().replaceAll("-", "");
        userVo.setPrincipal(uuid);
        helper.encryptPassword(userVo);
    }

}
