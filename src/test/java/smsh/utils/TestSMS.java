package smsh.utils;


import static org.junit.Assert.*;

import org.junit.Test;

import com.wteam.mixin.constant.SMSTpls;
import com.wteam.mixin.utils.SMSUtils;

/**
 * 短信发送测试
 * @version 1.0
 * @author benko
 * @time 2016-1-12 17:04:37
 */
public class TestSMS {

    @Test
    public void sendTest() {
        String result = SMSUtils.sendSms(SMSTpls.Test.param("1832376671","1832376671"), "18320376671");
        assertEquals("success", result);
    }

}
