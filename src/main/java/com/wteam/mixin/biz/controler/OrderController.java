package com.wteam.mixin.biz.controler;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.wteam.mixin.biz.service.*;
import com.wteam.mixin.model.po.*;
import com.wteam.mixin.model.vo.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.constant.PaymentMethod;
import com.wteam.mixin.constant.ProductType;
import com.wteam.mixin.constant.State;
import com.wteam.mixin.constant.Wechat;
import com.wteam.mixin.define.Result;
import com.wteam.mixin.define.ResultInfo;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.query.CustomerOrderQuery;
import com.wteam.mixin.model.query.TrafficPlanQuery;
import com.wteam.mixin.pagination.Pagination;
import com.wteam.mixin.utils.ExcelExportDataFactory;
import com.wteam.mixin.utils.ExcelUtils;
import com.wteam.mixin.utils.SpringUtils;
import com.wteam.mixin.utils.WeChatUtils;
import com.wteam.mixin.utils.ExcelUtils.ExcelExportData;


/**
 * 订单模块控制器
 *
 * @version 1.0
 * @author benko
 */
@Controller
@RequestMapping("/order")
public class OrderController {

    /**
     * log4j实例对象.
     */
    @SuppressWarnings("unused")
    private static final Logger LOG = LogManager.getLogger(OrderController.class.getName());

    /** 订单列表 */
    public static final String ORDER_LIST = "orderList";

    /** 订单 */
    public static final String ORDER = "order";

    @Autowired
    IOrderService orderService;

    @Autowired
    IWechatService wechatService;

    @Autowired
    IRechargeService rechargeService;

    @Autowired
    ITrafficPlanActivitiesService trafficPlanActivitiesService;

    @Autowired
    IBargainirgService bargainirgService;

    @Autowired
    IBargainirgRecordService bargainirgRecordService;

    @Autowired
    ITrafficService trafficService;


    /**
     * 超级管理员按条件分页查询订单
     *
     * @param pageNo
     *            目的页
     * @param pageSize
     *            每页的容量
     * @param resultMessage
     *            返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/list/super")
    public Result listBySuper(@RequestParam(name = "query", required = false) CustomerOrderQuery query,
                              @RequestParam(name = "trafficplanQuery", required = false) TrafficPlanQuery trafficplanQuery,
                              @RequestParam("pageNo") Integer pageNo,
                              @RequestParam("pageSize") Integer pageSize,
                              ResultMessage resultMessage) {
        query = query != null ? query : new CustomerOrderQuery();
        trafficplanQuery = trafficplanQuery != null ? trafficplanQuery : new TrafficPlanQuery();

        Pagination pagination = orderService.list(query,trafficplanQuery, pageNo, pageSize);

        return resultMessage.setSuccessInfo("获取订单列表成功").putParam(ORDER_LIST, pagination);
    }
    /**
     * 超级管理员按查询下载订单
     *
     * @param pageNo
     *            目的页
     * @param pageSize
     *            每页的容量
     * @param resultMessage
     *            返回参数.
     * @return resultMessage
     */
    @RequestMapping(value = "/list/super/download")
    public void listBySuper(@RequestParam(name = "query", required = false) CustomerOrderQuery query,
                              @RequestParam(name = "trafficplanQuery", required = false) TrafficPlanQuery trafficplanQuery,
                              HttpServletResponse response,
                              ResultMessage resultMessage) {
        CustomerOrderQuery query1 = query != null ? query : new CustomerOrderQuery();
        TrafficPlanQuery trafficplanQuery1 = trafficplanQuery != null ? trafficplanQuery : new TrafficPlanQuery();

        SpringUtils.outputHttpHandle(resultMessage, response, () -> {
            // 获取列表
            List<CustomerOrderVo> list = orderService.list(query1,trafficplanQuery1);
            // 构建excel数据
            ExcelExportData data = ExcelExportDataFactory.customerOrder(list, query1.getBusinessId() , true);
            // 输出到http请求中
            ExcelUtils.exportToHttpResponse(response, "米信订单报表.xls", data);
        });
    }


    /**
     * 商家按条件分页查询自己的订单
     *
     * @param tel
     *            手机号
     * @param state
     *            状态
     * @param pageNo
     *            目的页
     * @param pageSize
     *            每页的容量
     * @param user
     *            商家
     * @param resultMessage
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/list/business")
    public Result listByBusiness(@RequestParam(name = "query", required = false) CustomerOrderQuery query,
                                 @RequestParam("pageNo") Integer pageNo,
                                 @RequestParam("pageSize") Integer pageSize,
                                 @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                                 ResultMessage resultMessage) {
        query = query != null ? query : new CustomerOrderQuery();
        query.setBusinessId(user.getUserId());
        Pagination pagination = orderService.listBySuper(query, pageNo, pageSize);

        return resultMessage.setSuccessInfo("获取订单列表成功").putParam(ORDER_LIST, pagination);
    }

    /**
     * 查看自己的订单
     *
     * @param tel
     *            手机号
     * @param state
     *            状态
     * @param pageNo
     *            目的页
     * @param pageSize
     *            每页的容量
     * @param user
     *            商家
     * @param resultMessage
     * @return
     */
    @SuppressWarnings("unchecked")
    @ResponseBody
    @RequestMapping(value = "/list/my")
    public Result listByMy(@RequestParam(name = "tel", required = false) String tel,
                           @RequestParam(name = "state", required = false) Integer state,
                           @RequestParam("pageNo") Integer pageNo,
                           @RequestParam("pageSize") Integer pageSize,
                           @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                           ResultMessage resultMessage) {
        CustomerOrderQuery query = new CustomerOrderQuery();
        query.setCustomerId(user.getUserId());
        query.setPhone(tel);
        query.setState(state);

        Pagination pagination = orderService.listBySuper(query, pageNo, pageSize);
        List<CustomerOrderVo> list = (List<CustomerOrderVo>)pagination.getList();
        list.forEach(order -> {
            order.setCost(null);
            order.setFactorage(null);
            order.setRealIncome(null);
            order.setProfits(null);
        });

        return resultMessage.setSuccessInfo("获取订单列表成功").putParam(ORDER_LIST, pagination);
    }

    /**
     * 批量改变订单状态
     *
     * @param state
     *            状态
     * @param orderIds
     *            订单ID列表
     * @param resultMessage
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/state/change")
    public Result changeState(@RequestParam("state") Integer state,
                           @RequestParam("orderIds") Long[] orderIds,
                           ResultMessage resultMessage) {
        // 保存处理成功的订单列表
        List<CustomerOrderVo> list = new ArrayList<>();
        resultMessage.setSuccessInfo("").putParam(ORDER_LIST, list);

        for (int i = 0; i < orderIds.length; i++ ) {
            try {
                list.add(orderService.chageOrderState(orderIds[i], state));
            }
            catch (ServiceException e) {
                LOG.error(e.getMessage());
                // 记录失败信息
                resultMessage.setInfo(e.getCode(),
                    resultMessage.getResultInfo() + "\n" + e.getInfo());
            }
        }

        return resultMessage.getResultCode() != ResultInfo.SUCCESS ? resultMessage : resultMessage.setSuccessInfo(
            "修改订单状态成功").putParam(ORDER_LIST, null);
    }
    /**
     * 批量改变订单状态
     *
     * @param state
     *            状态
     * @param orderIds
     *            订单ID列表
     * @param resultMessage
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/apiCallback")
    public Result apiCallback(
                           @RequestParam("orderIds") Long[] orderIds,
                           ResultMessage resultMessage) {
        // 保存处理成功的订单列表
        List<CustomerOrderVo> list = new ArrayList<>();
        resultMessage.setSuccessInfo("").putParam(ORDER_LIST, list);
        CustomerOrderVo orderVo = null;
        for (int i = 0; i < orderIds.length; i++ ) {
            try {
                orderVo = orderService.find(orderIds[i]);
                rechargeService.apiCallback(orderVo);
            }
            catch (ServiceException e) {
                LOG.error(e.getMessage());
                String msg = String.format("订单 '%s' 回调失败", orderVo.getOrderNum());
                // 记录失败信息
                resultMessage.setInfo(e.getCode(),
                    resultMessage.getResultInfo() + "\n" + msg );
            }
        }

        return resultMessage.getResultCode() != ResultInfo.SUCCESS ? resultMessage : resultMessage.setSuccessInfo(
            "修改订单状态成功").putParam(ORDER_LIST, null);
    }




    /**
     * 提交订单
     *
     * @param order
     * @param result1
     * @param resultMessage
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/submit")
    public Result submit(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                         @ModelAttribute("order") @Valid CustomerOrderVo order,
                         BindingResult result1, ResultMessage resultMessage) {
        SpringUtils.validate(result1);
        order.setCustomerId(user.getUserId());
        order = orderService.submitOrder(order);

        return resultMessage.setSuccessInfo("提交订单成功").putParam(ORDER, order);
    }

    /**
     * 提交商家充值订单
     *
     * @param order
     * @param result1
     * @param resultMessage
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/submit/business/balance")
    public Result submitBusinessBalance(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                                        @RequestParam("retailPrice") BigDecimal retailPrice,
                                        @RequestParam(name = "alipayOrderId", required=false)String alipayOrderId,
                                        @RequestParam(name = "paymentMethod", required=false)Integer paymentMethod,
                                        ResultMessage resultMessage) {
        CustomerOrderVo order = new CustomerOrderVo();

        order.setBusinessId(user.getUserId());
        order.setCustomerId(user.getUserId());
        order.setNum(1);
        order.setRetailPrice(retailPrice);
        order.setProductId(-1L);
        order.setProductType(ProductType.BusinessBalance);
        order.setAlipayOrderId(alipayOrderId);
        order.setPaymentMethod(paymentMethod);
        order = orderService.submitOrder(order);

        return resultMessage.setSuccessInfo("提交订单成功").putParam(ORDER, order);
    }

    /**
     * 提交订单
     *
     * @param req
     * @param orderId
     * @param money
     * @param paymentMethod
     * @param resultMessage
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/pay")
    public Result pay(HttpServletRequest req, @RequestParam("orderId") Long orderId,
                      @RequestParam(value = "money", required = false) BigDecimal money,
                      @RequestParam("paymentMethod") Integer paymentMethod,
                      ResultMessage resultMessage) {
        CustomerOrderVo order = new CustomerOrderVo();
        order.setId(orderId);
        order.setPaymentMethod(paymentMethod);

        order = orderService.pay(order);
        Object wechat_pay_params = null;
        if (order.getPaymentMethod().equals(PaymentMethod.Wechat)) {
            wechat_pay_params = wechatService.pay(order, req);
        }
        if (order.getProductType().equals(ProductType.Traffic)
            && order.getState().equals(State.CustomerOrder.paySuccess)) {
            CustomerOrderVo orderVo = rechargeService.recharge(order.getOrderNum());
            // 充值失败退款
            if (orderVo.getState().equals(State.CustomerOrder.rechargeFail)) {
                orderService.chageOrderState(orderId, State.CustomerOrder.refunded);
            }
        }

        return resultMessage.setSuccessInfo("提交订单成功").putParam(ORDER, order).putParam(
            "wechat_pay_params", wechat_pay_params);
    }

    @ResponseBody
    @RequestMapping(value = "/bargainirg")
    public Result bargainirg(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                             HttpServletRequest req, @RequestParam("orderId") Long orderId,
                             @RequestParam("paymentMethod") Integer paymentMethod,
                             ResultMessage resultMessage) {
        CustomerOrderVo order = new CustomerOrderVo();
        order.setId(orderId);
        order.setPaymentMethod(paymentMethod);

        order = orderService.pay(order);
        TrafficPlanActivityVo trafficPlanActivity = trafficPlanActivitiesService.getAvailable(order);
        if(Optional.ofNullable(trafficPlanActivity).isPresent()){
            TrafficPlanActivityVo trafficPlanActivityVo = new TrafficPlanActivityVo(trafficPlanActivity.getTrafficPlanId(), trafficPlanActivity.getLimitNumber(),
                    trafficPlanActivity.getLowPrice(), trafficPlanActivity.getIsActive(), trafficPlanActivity.getStartTime(), trafficPlanActivity.getEndTime());
            Bargainirg bargainirg = bargainirgService.createByOrder(order, trafficPlanActivity);
            return resultMessage.setSuccessInfo("提交订单成功").putParam(ORDER, order)
                    .putParam("code", 0)
                    .putParam("plan", trafficPlanActivityVo)
                    .putParam("bargainirg", bargainirg.getId());
        }else{
            return resultMessage.setSuccessInfo("砍价活动已失效").putParam(ORDER, order)
                    .putParam("code", 1)
                    .putParam("msg", "砍价活动已失效");
        }
    }

    @ResponseBody
    @RequestMapping(value = "/bargainirg/info",  method = RequestMethod.POST)
    public Result bargainirgInfo(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                      HttpServletRequest req, @RequestParam("id") Long bargainirgId,
                      ResultMessage resultMessage) {
        Bargainirg bargainirg = bargainirgService.findById(bargainirgId);
        if(!Optional.ofNullable(bargainirg).isPresent() || bargainirg.getState(Bargainirg.State.CLOSE).equals(Bargainirg.State.CLOSE)){
            return resultMessage.setSuccessInfo("参加失败").putParam("code", 1)
                    .putParam("msg", "此活动不可用");
        }

        TrafficPlanActivity trafficPlanActivity = trafficPlanActivitiesService.findByUser(bargainirg.getCustomerId(), bargainirg.getTrafficPlanActivityId());
        if(!trafficPlanActivity.isAvailable()) {
            return resultMessage.setSuccessInfo("参加失败").putParam("code", 1)
                    .putParam("msg", "此活动不可用");
        }
        BargainirgPlanVo bargainirgPlanVo = trafficPlanActivitiesService.get(trafficPlanActivity.getId());

        List<CustomerRecordVo> customerRecordVoList = bargainirgRecordService.getList(bargainirgId);

        return resultMessage.setSuccessInfo("砍价活动已失效").putParam("records", customerRecordVoList)
                .putParam("activity", trafficPlanActivity)
                .putParam("businessPlan", bargainirgPlanVo);

    }


    @ResponseBody
    @RequestMapping(value = "/cut", method = RequestMethod.POST)
    public Result cut(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                             HttpServletRequest req, @RequestParam("bargainirg_id") Long bargainirgId,
                             ResultMessage resultMessage) {
        Bargainirg bargainirg = bargainirgService.findById(bargainirgId);
        if(!Optional.of(bargainirg).isPresent() || bargainirg.getState(Bargainirg.State.CLOSE).equals(Bargainirg.State.CLOSE)){
            return resultMessage.setSuccessInfo("参加失败").putParam("code", 1)
                        .putParam("msg", "此活动不可用");
        }

        TrafficPlanActivity trafficPlanActivity = trafficPlanActivitiesService.findByUser(bargainirg.getCustomerId(), bargainirg.getTrafficPlanActivityId());
        if(!trafficPlanActivity.isAvailable()) {
            return resultMessage.setSuccessInfo("参加失败").putParam("code", 1)
                    .putParam("msg", "此活动不可用");
        }

        BargainirgRecord record = bargainirgRecordService.queryByCustomer(bargainirgId, user.getUserId());
        if(Optional.of(record).isPresent()){
            return resultMessage.setSuccessInfo("参加失败").putParam("code", 1)
                    .putParam("msg", "你已经参加过此次活动");
        }else{
            record = bargainirgRecordService.doCut(bargainirg, trafficPlanActivity, user);
            if(Optional.of(record.getId()).isPresent()) {
                return resultMessage.setSuccessInfo("参与成功").putParam("code", 0)
                        .putParam("msg", "成功参与");
            }else{
                return resultMessage.setSuccessInfo("参加失败").putParam("code", 1)
                        .putParam("msg", "你已经参加过此次活动");
            }
        }
    }

    @ResponseBody
    @RequestMapping(value = "/pay/wechat")
    public String wechatPayCallback(HttpServletRequest req) {
        String returnFormat = "<xml><return_code><![CDATA[{code}]]></return_code><return_msg><![CDATA[{msg}]]></return_msg></xml>",
            code = "{code}", msg = "{msg}";

        try {
            req.setCharacterEncoding("UTF-8");
            // 1.将微信服务器的推送流解析成Map型数据结构
            Map<String, String> xmlMap = WeChatUtils.readXmlMapFromStream(req.getInputStream());
            if (LOG.isDebugEnabled()) LOG.debug("xmlMap->{}", xmlMap);
            String return_code = xmlMap.get(Wechat.XmlTags.return_code);

            if ("SUCCESS".equalsIgnoreCase(return_code)) {
                String orderNum = xmlMap.get(Wechat.XmlTags.out_trade_no);
                // 修改订单状态为支付成功
                orderService.wechatPayCallback(xmlMap);
                // 充值
                CustomerOrderVo orderVo = rechargeService.recharge(orderNum);
                // 充值失败退款
                if (orderVo.getState().equals(State.CustomerOrder.rechargeFail)) {
                    orderService.chageOrderState(orderNum, State.CustomerOrder.refunded);
                }
            }
            else {

            }
        }
        catch (ServiceException e) {
            LOG.error(e);
            return returnFormat.replace(code, "SUCCESS").replace(msg, "ok");
        }
        catch (Exception e) {
            LOG.error("", e);
            return returnFormat.replace(code, "FAIL").replace(msg, "系统错误");
        }
        return returnFormat.replace(code, "SUCCESS").replace(msg, "ok");
    }

}
