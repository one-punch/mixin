package com.wteam.mixin.biz.controler;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.alibaba.fastjson.JSON;
import com.alipay.util.AlipayNotify;
import com.wteam.mixin.alipay.Configs;
import com.wteam.mixin.biz.service.IFinanceService;
import com.wteam.mixin.biz.service.IOrderService;
import com.wteam.mixin.constant.PaymentMethod;
import com.wteam.mixin.constant.ProductType;
import com.wteam.mixin.constant.State;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.function.IHandle;
import com.wteam.mixin.function.IHandle1;
import com.wteam.mixin.model.po.BusinessBalanceRecordPo;
import com.wteam.mixin.model.query.CustomerOrderQuery;
import com.wteam.mixin.model.vo.BusinessBalanceRecordVo;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.utils.IdWorker;
import com.wteam.mixin.utils.MD5Utils;


/**
 * 支付宝模块控制器
 *
 * @version 1.0
 * @author benko
 */
@Controller
@RequestMapping("/alipay")
public class AlipayController {
    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(AlipayController.class.getName());

    @Autowired
    IOrderService orderService;

    @Autowired
    IFinanceService financeService;

    @Autowired
    private IdWorker orderIdWorker;

    @RequestMapping(value = "/return")
    public void return_url(HttpServletRequest request , HttpServletResponse response) {
        Map<String,String> params = new HashMap<String,String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            params.put(name, valueStr);
        }
        //商户订单号
        String out_trade_no = request.getParameter("out_trade_no");
        //支付宝交易号
        String trade_no = request.getParameter("trade_no");
        //交易状态
        String trade_status = request.getParameter("trade_status");
        // 订单
        CustomerOrderVo orderVo = new CustomerOrderVo();
        orderVo.setAlipayInfo(JSON.toJSONString(params));
        orderVo.setAlipayOrderId(trade_no);
        LOG.debug(orderVo.getAlipayInfo());
        // token
        String token = "";
        try {
            if(AlipayNotify.verify(params)){//验证成功
                CustomerOrderVo oldOrder = orderService.find(out_trade_no);
                if(trade_status.equals("TRADE_FINISHED")){
                    //判断该笔订单是否在商户网站中已经做过处理
                        //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                        //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                        //如果有做过处理，不执行商户的业务程序

                    //注意：
                    //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                } else if (trade_status.equals("TRADE_SUCCESS")){
                    //判断该笔订单是否在商户网站中已经做过处理
                        //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                        //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                        //如果有做过处理，不执行商户的业务程序

                    //注意：
                    //付款完成后，支付宝系统发送该交易状态通知
                    token = "success";
                    orderVo.setState(State.CustomerOrder.paySuccess);
                    if (oldOrder.getProductType().equals(ProductType.BusinessBalance)
                        && oldOrder.getState().equals(State.CustomerOrder.waitPay)) {
                        BusinessBalanceRecordPo recordPo = new BusinessBalanceRecordPo();
                        recordPo.setBusinessId(oldOrder.getBusinessId());
                        recordPo.setMoney(oldOrder.getRetailPrice()); // +
                        recordPo.setSource(State.BBRecordSource.balanceRechange);
                        recordPo.setSourceId(Long.valueOf(oldOrder.getOrderNum()));
                        financeService.saveBusinessBalanceRecord(recordPo);
                    }

                } else {
                    token = "failure";
                }

                orderService.update(orderVo, out_trade_no);
            }else {
                token = "failure";
            }
        }
        catch (Exception e) {
            LOG.error("",e);
            token = "failure";
        }
        String url = String.format("%s%s?token=%s", Configs.host, "Info.html", token);
        try {
            response.sendRedirect(url);
        }
        catch (IOException e1) {
            throw new ServiceException("重定向失败：" + url);
        }
    }


    @RequestMapping(value = "/notify")
    public void notify_url(HttpServletRequest request , HttpServletResponse response) {
        Map<String,String> params = new HashMap<String,String>();
        Map<String, String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用。如果mysign和sign不相等也可以使用这段代码转化
            params.put(name, valueStr);
        }

        //商户订单号
        String out_trade_no = request.getParameter("out_trade_no");
        //支付宝交易号
        String trade_no = request.getParameter("trade_no");
        //交易状态
        String trade_status = request.getParameter("trade_status");
        CustomerOrderVo orderVo = new CustomerOrderVo();
        orderVo.setAlipayInfo(JSON.toJSONString(params));
        orderVo.setAlipayOrderId(trade_no);
        LOG.debug(orderVo.getAlipayInfo());
        try {
            if(AlipayNotify.verify(params)){//验证成功
                CustomerOrderVo oldOrder = orderService.find(out_trade_no);
                if(trade_status.equals("TRADE_FINISHED")){
                    //判断该笔订单是否在商户网站中已经做过处理
                        //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                        //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                        //如果有做过处理，不执行商户的业务程序

                    //注意：
                    //退款日期超过可退款期限后（如三个月可退款），支付宝系统发送该交易状态通知
                } else if (trade_status.equals("TRADE_SUCCESS")){
                    //判断该笔订单是否在商户网站中已经做过处理
                        //如果没有做过处理，根据订单号（out_trade_no）在商户网站的订单系统中查到该笔订单的详细，并执行商户的业务程序
                        //请务必判断请求时的total_fee、seller_id与通知时获取的total_fee、seller_id为一致的
                        //如果有做过处理，不执行商户的业务程序

                    //注意：
                    //付款完成后，支付宝系统发送该交易状态通知
                    orderVo.setState(State.CustomerOrder.paySuccess);

                    if (oldOrder.getProductType().equals(ProductType.BusinessBalance)
                        && oldOrder.getState().equals(State.CustomerOrder.waitPay)) {
                        BusinessBalanceRecordPo recordPo = new BusinessBalanceRecordPo();
                        recordPo.setBusinessId(oldOrder.getBusinessId());
                        recordPo.setMoney(oldOrder.getRetailPrice()); // +
                        recordPo.setSource(State.BBRecordSource.balanceRechange);
                        recordPo.setSourceId(Long.valueOf(oldOrder.getOrderNum()));
                        financeService.saveBusinessBalanceRecord(recordPo);
                    }
                }
                orderService.update(orderVo, out_trade_no);
            }else {

            }

        }
        catch (Exception e) {

        }
        try {
            response.getWriter().write("success");
            response.getWriter().flush();
        }
        catch (IOException e) {
        }
    }


    @RequestMapping(value = "/easypay/callback")
    public void easypayCallback(HttpServletRequest request , HttpServletResponse response) throws Exception {
        IHandle1<String> hander = msg -> {
            response.getWriter().write(msg);
            response.getWriter().flush();
        };

        try {
            LOG.debug(request.getParameterMap());

            String key = com.wteam.mixin.constant.Configs.prop.getProperty("easypay.key", null);
            Long businessId_null = com.wteam.mixin.constant.Configs.businessId_null;
            String tradeNo = request.getParameter("tradeNo");//交易号
            String desc = request.getParameter("desc");//支付说明
            String time = request.getParameter("time");//付款时间
            String username = request.getParameter("username");//对方用户名
            String userid = request.getParameter("userid");//对方用户ID
            String amount = request.getParameter("amount");//金额
            String status = request.getParameter("status");//交易状态
            String sig = request.getParameter("sig");//数字签名

            //
            String string = Stream.of( tradeNo, desc, time, username, userid, amount, status, key)
                .collect(Collectors.joining("|"));
            if (MD5Utils.md5(string).equalsIgnoreCase(sig)) {
                CustomerOrderQuery query = new CustomerOrderQuery();
                query.setAlipayOrderId(tradeNo);
                query.setPaymentMethod(PaymentMethod.EasyPay);
                // 找到这个订单，找不到就提交
                CustomerOrderVo orderVo = orderService.find(query);
                if (orderVo != null) {
                    if (!orderVo.getBusinessId().equals(businessId_null)
                        && State.CustomerOrder.waitPay == orderVo.getState()) {
                        orderVo.setRetailPrice(new BigDecimal(amount));

                        BusinessBalanceRecordPo recordVo = new BusinessBalanceRecordPo();
                        recordVo.setBusinessId(orderVo.getBusinessId());
                        recordVo.setMoney(orderVo.getRetailPrice());
                        recordVo.setSource(State.BBRecordSource.balanceRechange);
                        recordVo.setSourceId(Long.valueOf(orderVo.getOrderNum()));
                        financeService.saveBusinessBalanceRecord(recordVo);

                        orderVo.setState(State.CustomerOrder.success);
                        orderService.update(orderVo, orderVo.getOrderNum());
                    }
                }
                else {
                    CustomerOrderVo order = new CustomerOrderVo();
                    order.setOrderNum(orderIdWorker.nextId() + "");
                    order.setBusinessId(businessId_null);
                    order.setCustomerId(businessId_null);
                    order.setNum(1);
                    order.setRetailPrice(new BigDecimal(amount));
                    order.setProductId(-1L);
                    order.setProductType(ProductType.BusinessBalance);
                    order.setAlipayOrderId(tradeNo);
                    order.setPaymentMethod(PaymentMethod.EasyPay);
                    switch (status) {
                        case "TRADE_FINISHED":
                        case "TRADE_SUCCESS":
                        case "交易成功":
                            order.setState(State.CustomerOrder.paySuccess);
                            break;
                        default:
                            order.setState(State.CustomerOrder.waitPay);
                            break;
                    }

                    LOG.debug("status: {}, state: {}",status, order.getState());
                    order = orderService.submitOrder(order);
                }

                hander.handle("ok");
            }
            else {
                hander.handle("签名错误");
            }
        }
        catch (Exception e) {
            LOG.error("",e);
            hander.handle("系统错误:" + e.getMessage());
        }
    }


}
