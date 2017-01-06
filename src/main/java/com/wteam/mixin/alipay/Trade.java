package com.wteam.mixin.alipay;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;

/**
 * 登录模块控制器
 *
 * @version 1.0
 * @author benko
 */
public class Trade {

    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(Trade.class.getName());

    public static final String URL = "https://openapi.alipay.com/gateway.do";

    /**
     * 支付宝支付订单的查询
     * 订单支付时传入的商户订单号,和支付宝交易号不能同时为空。 trade_no,out_trade_no如果同时存在优先取trade_no
     * @param out_trade_no 订单支付时传入的商户订单号
     * @param trade_no 支付宝交易号
     * @return AlipayTradeQueryResponse
     */
    public AlipayTradeQueryResponse query(String out_trade_no, String trade_no) {
        if(LOG.isDebugEnabled()) LOG.debug("out_trade_no:{}, trade_no:{}",out_trade_no, trade_no);

        AlipayClient client = new DefaultAlipayClient(URL, Configs.appId, Configs.privateKeyPkcs8, Configs.format, Configs.charset, Configs.alipayPulicKey);
        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        JSONObject json = new JSONObject();
        json.put("out_trade_no", out_trade_no);
        json.put("trade_no", trade_no);
        request.setBizContent(json.toJSONString());

        try {
            AlipayTradeQueryResponse response = client.execute(request);
            if(LOG.isDebugEnabled()) LOG.debug(response.getBody());
            return response;
        }
        catch (AlipayApiException e) {
            LOG.error("",e);
        }
        return null;
    }

}
