package smsh.utils;


import org.junit.Test;

import com.alipay.api.request.AlipayUserTradeSearchRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.wteam.mixin.alipay.Trade;

public class TestAlipay {
    @Test
    public void test() {
        
        Trade trade = new Trade();
        
        AlipayTradeQueryResponse response; 
        response = trade.query("T200P2251221201221831", "2016082521001001950283657520");
        response.getCode().equals("10000");
        
        
/*        trade.query(null, "20160824661359099304746071743688");
        trade.query("20160824200040011100950025360143", null);
        trade.query(null, "20160824200040011100950025360143");*/
        
        
    }
}
