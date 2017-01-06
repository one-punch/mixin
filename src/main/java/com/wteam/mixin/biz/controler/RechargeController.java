package com.wteam.mixin.biz.controler;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.service.IOrderService;
import com.wteam.mixin.biz.service.IRechargeHandleService;
import com.wteam.mixin.biz.service.IRechargeService;
import com.wteam.mixin.constant.PaymentMethod;
import com.wteam.mixin.constant.State;
import com.wteam.mixin.constant.ValidatePattern;
import com.wteam.mixin.define.Result;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.query.CustomerOrderQuery;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.model.vo.TrafficApiRechargeInfoVo;
import com.wteam.mixin.model.vo.TrafficApiRequestParams;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.recharge.JingYieRecharge;
import com.wteam.mixin.recharge.LiuLiangRecharge;
import com.wteam.mixin.recharge.JingYieRecharge.Json;
import com.wteam.mixin.recharge.KuaiChongRecharge;
import com.wteam.mixin.recharge.XiaoZhuRecharge.Response;
import com.wteam.mixin.task.OrderShouDanRechargeTask;
import com.wteam.mixin.utils.HttpRequest;
import com.wteam.mixin.utils.WeChatUtils;

/**
 * 流量充值
 *
 * @version 1.0
 * @author benko
 * @time 2016-5-13 19:49:04
 */
@Controller
@RequestMapping("/recharge")
public class RechargeController {

    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(RechargeController.class.getName());


    @Autowired
    OrderShouDanRechargeTask orderShouDanRechargeTask;

    @Autowired
    IOrderService orderService;
    @Autowired
    IRechargeService rechargeService;

    @Autowired
    IRechargeHandleService deLiHandleService;
    @Autowired
    IRechargeHandleService shangTongHandleService;
    @Autowired
    IRechargeHandleService kuaiChongHandleService;
    @Autowired
    IRechargeHandleService daHanHandleService;
    @Autowired
    IRechargeHandleService youXingHandleService;
    @Autowired
    IRechargeHandleService youXing2HandleService;
    @Autowired
    IRechargeHandleService shanWangHandleService;
    @Autowired
    IRechargeHandleService yikuaiHandleService;
    @Autowired
    IRechargeHandleService dazhongRechargerService;
    // 2016-09-20 08-54-53
    public static final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
    /**
     * 进业
     * @return
     */
    private void yingyie_callback(HttpServletRequest request, HttpServletResponse response) {

        try {
            String params = HttpRequest.inToString(request.getInputStream());
            LOG.debug(params);
            Json json = JSON.parseObject(params, Json.class);

            CustomerOrderVo orderVo = null;
            // 更新订单信息
            String orderNum = json.getMsgbody().getContent().getExtorder();
            if ("00".equals(json.getMsgbody().getContent().getCode())) {
                orderVo = orderService.chageOrderState(orderNum, State.CustomerOrder.success);
            } else {
                orderService.chageOrderState(orderNum, State.CustomerOrder.failure);
                orderVo = orderService.chageOrderState(orderNum, State.CustomerOrder.refunded);
                orderVo.setFailedInfo(json.getMsgbody().getContent().getStatus());
            }
            orderVo.setIsCallback(true);
            orderService.update(orderVo, orderNum);
            //
            rechargeService.apiCallback(orderVo);

            json = JingYieRecharge.instance().callback(orderVo.getOutOrderId(), orderVo.getOrderNum());
            response.getWriter().write(JSON.toJSONString(json));
            response.getWriter().flush();
            response.getWriter().close();
        }
        catch (Exception e) {
            LOG.error("",e);
        }

    }
    /**
     * 小猪
     * @return
     */
    private void xiaozhu_callback(HttpServletRequest request, HttpServletResponse response) {

        try {
            String params = HttpRequest.inToString(request.getInputStream());
            LOG.debug(params);
            Response json = JSON.parseObject(params, Response.class);

            CustomerOrderVo orderVo = null;
            // 更新订单信息
            String orderNum = json.getCustomerOrderNum();
            if ("13".equals(json.getStatus())) {
                orderVo = orderService.chageOrderState(orderNum, State.CustomerOrder.success);
            } else {
                orderService.chageOrderState(orderNum, State.CustomerOrder.failure);
                orderVo = orderService.chageOrderState(orderNum, State.CustomerOrder.refunded);
                orderVo.setFailedInfo(json.getMessage());
            }
            orderVo.setIsCallback(true);
            orderService.update(orderVo, orderNum);
            //
            rechargeService.apiCallback(orderVo);

            response.getWriter().write("00");
            response.getWriter().flush();
            response.getWriter().close();
        }
        catch (Exception e) {
            LOG.error("",e);
        }

    }
    /**
     * 流量
     * @return
     */
    private void liuliang_callback(HttpServletRequest request, HttpServletResponse response) {

        try {
            String params = HttpRequest.inToString(request.getInputStream());
            LOG.debug(params);

            CustomerOrderVo orderVo = null;
            // 更新订单信息
            LiuLiangRecharge.Responseitems responseitems = JSON.parseObject(params, LiuLiangRecharge.Responseitems.class);

            if(!LiuLiangRecharge.check(responseitems.getAppId()))
                throw new RuntimeException("非法访问");

            String orderNum = responseitems.getClientorderno();
            if ("0000".equals(responseitems.getCode())) {
                orderVo = orderService.chageOrderState(orderNum, State.CustomerOrder.success);
            } else {
                orderService.chageOrderState(orderNum, State.CustomerOrder.failure);
                orderVo = orderService.chageOrderState(orderNum, State.CustomerOrder.refunded);
                orderVo.setFailedInfo(responseitems.getMessage());
            }
            orderVo.setIsCallback(true);
            orderService.update(orderVo, orderNum);
            //
            rechargeService.apiCallback(orderVo);

            response.getWriter().write("0000");
            response.getWriter().flush();
            response.getWriter().close();
        }
        catch (Exception e) {
            LOG.error("",e);
        }

    }

    /**
     * 回调
     * @return
     */
    @RequestMapping("/{apiprovider}/callback")
    public void callback(@PathVariable("apiprovider") String apiprovider,HttpServletRequest request, HttpServletResponse response) {

        switch (apiprovider) {
            case "jingyie": // 进业
                yingyie_callback(request, response);break;
            case "xiaozhu": // 小猪
                xiaozhu_callback(request, response);break;
            case "liuliang": // 流量
                liuliang_callback(request, response);break;
            case "kuaichong": // 快充
                kuaiChongHandleService.callback(request, response);break;
            case "deli": // 得力
                deLiHandleService.callback(request, response);break;
            case "shangtong": // 尚得
                shangTongHandleService.callback(request, response);break;
            case "dahan": // 大汉
                daHanHandleService.callback(request, response);break;
            case "youxing": // 友信
                youXingHandleService.callback(request, response);break;
            case "youxing2": // 友信
                youXing2HandleService.callback(request, response);break;
            case "shanwang": // 友信
                shanWangHandleService.callback(request, response);break;
            case "yikuai": // 宜快
                yikuaiHandleService.callback(request, response);break;
            case "dazhong": // 大众
            	dazhongRechargerService.callback(request, response);break;
        }

    }


    @RequestMapping(value = "/api", method = RequestMethod.POST)
    @ResponseBody
    public Result rechargeApi(
                            @RequestParam("tel")String tel,
                            @RequestParam("appid")String appid,
                            @RequestParam("type")String type,
                            @RequestParam("outTradeNum")String outTradeNum,
                            @RequestParam("productNum")String productNum,
                            @RequestParam("timestamp")String timestamp,
                            @RequestParam("sign")String sign,
                            HttpServletRequest request,
                            ResultMessage resultMessage) {
        // 验证手机
        if (!ValidatePattern.valid(ValidatePattern.TEL, tel)) {
            return resultMessage.setServiceFailureInfo("电话格式不对");
        }
        // 验证时间戳
        try {
            format.parse(timestamp);
        }
        catch (Exception e) {
            return resultMessage.setServiceFailureInfo("时间戳格式不对");
        }

        String ip = WeChatUtils.getRemoteAddr(request);
        // 产品 类型
        switch (type) {
            case "1":{
                CustomerOrderVo orderVo = rechargeService.apiRecharge(new TrafficApiRequestParams(tel, appid, type,outTradeNum, productNum, timestamp, sign, ip));
                if (orderVo.getState().equals(State.CustomerOrder.rechargeSubmit)) {
                    resultMessage.setSuccessInfo("充值提交成功！");
                } else {
                    resultMessage.setServiceFailureInfo("充值提交失败！");
                }
                resultMessage.putParam("orderNum", orderVo.getOrderNum());
                resultMessage.putParam("outTradeNum", orderVo.getBusinessOutOrderNum());
            }break;

            default:
               return resultMessage.setServiceFailureInfo("没有这种类型");
        }

        return resultMessage;
    }

    @RequestMapping(value = "/order_query", method = RequestMethod.POST)
    @ResponseBody
    public Result order_query(
                            @RequestParam("appid")String appid,
                            @RequestParam("outTradeNum")String outTradeNum,
                            @RequestParam("timestamp")String timestamp,
                            @RequestParam("sign")String sign,
                            HttpServletRequest request,
                            ResultMessage resultMessage) {

        // 验证时间戳
        try {
            format.parse(timestamp);
        }
        catch (Exception e) {
            return resultMessage.setServiceFailureInfo("时间戳格式不对");
        }
        String ip = WeChatUtils.getRemoteAddr(request);

        CustomerOrderVo orderVo = rechargeService.orderQuery(new TrafficApiRequestParams(appid, outTradeNum, timestamp, sign));

        Map<String, Object> map = new HashMap<>();
        map.put("orderNum", orderVo.getOrderNum());
        map.put("outOrderNum", orderVo.getBusinessOutOrderNum());
        map.put("state", orderVo.getState());
        map.put("createTime", format.format(orderVo.getCreateTime()));
        map.put("isCallback", orderVo.getIsBusinessCallback());
        map.put("price", orderVo.getRetailPrice());
        map.put("productName", orderVo.getProductName());
        map.put("value", orderVo.getTrafficplanValue());

        return resultMessage.setSuccessInfo("查询订单成功").putParam("order", map);
    }


    @RequestMapping(value = "/recallback/all")
    @ResponseBody
    public Result reAllCallback(ResultMessage resultMessage) {
        rechargeService.reAllCallback();
        return resultMessage.setSuccessInfo("重新回调成功");
    }

    @RequestMapping(value = "/recallback")
    @ResponseBody
    public Result reCallback(
           @RequestParam("orderNum")String orderNum,
           ResultMessage resultMessage) {
        rechargeService.reCallback(orderNum);
        return resultMessage.setSuccessInfo("重新回调成功");
    }

    /**
     * 商家修改商家的配置信息
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/api/config/change/business")
    public Result rechargeApiConfigChangeBySuper(
                 @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                 @RequestParam("config")TrafficApiRechargeInfoVo infoVo,
                 ResultMessage resultMessage) {
        infoVo.setBusinessId(user.getUserId());
        infoVo = rechargeService.updateApiInfo(infoVo);

        return resultMessage.setSuccessInfo("修改成功").putParam("config", infoVo);
    }

    /**
     * 管理员修改商家的配置信息
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/api/config/change/super")
    public Result rechargeApiConfigChangeBySuper(
                 @RequestParam("config")TrafficApiRechargeInfoVo infoVo,
                 ResultMessage resultMessage) {
        infoVo = rechargeService.updateApiInfo(infoVo);

        return resultMessage.setSuccessInfo("修改成功").putParam("config", infoVo);
    }

    /**
     * 管理员修改商家的配置信息
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/api/config/super")
    public Result rechargeApiConfigBySuper(
                 @RequestParam("businessId")Long businessId,
                 ResultMessage resultMessage) {

        TrafficApiRechargeInfoVo infoVo = rechargeService.apiInfo(businessId);
        return resultMessage.setSuccessInfo("获取成功").putParam("config", infoVo);
    }

    /**
     * 获取商家的配置信息
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/api/config/business")
    public Result rechargeApiConfigByBusiness(
                 @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                 ResultMessage resultMessage) {

        TrafficApiRechargeInfoVo infoVo = rechargeService.apiInfo(user.getUserId());
        return resultMessage.setSuccessInfo("获取成功").putParam("config", infoVo);
    }

    /**
     * 修改授权
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/api/authoried/change")
    public Result changeApiRechargeAuthoried(
                 @RequestParam("businessId")Long businessId,
                 ResultMessage resultMessage) {

        TrafficApiRechargeInfoVo infoVo = rechargeService.changeApiRechargeAuthoried(businessId);
        return resultMessage.setSuccessInfo("获取成功").putParam("config", infoVo);
    }

    /**
     * 测试
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/test/{command}")
    public Result test_notify(@PathVariable("command") String command, ResultMessage resultMessage) {

        switch (command) {
            case "notity":{
                CustomerOrderVo orderVo = new CustomerOrderVo();
                orderVo.setBusinessId(1L);
                orderVo.setPaymentMethod(PaymentMethod.ApiBalance);
                orderVo.setState(1);

                rechargeService.apiCallback(orderVo);
            }break;
            default:
                break;
        }

        return resultMessage.setSuccessInfo("成功");
    }
    /**
     * 修改授权
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @RequestMapping(value = "/callabck")
    public void test_callback(HttpServletRequest request, HttpServletResponse response, ResultMessage resultMessage) {
        try {
            String params = HttpRequest.inToString(request.getInputStream());
            LOG.debug(params);
            response.getWriter().write("success");
        }
        catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /**
     * 给所有收单
     * @param user
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @ResponseBody
    @RequestMapping(value = "/shoudan")
    public Result shoudan( ResultMessage resultMessage) {

        if (!orderShouDanRechargeTask.execute()) {
            return resultMessage.setServiceFailureInfo(orderShouDanRechargeTask.message());
        }

        return resultMessage.setSuccessInfo(orderShouDanRechargeTask.message());
    }

    public static void main(String[] args) {
        System.out.println(format.format(new Date()));
    }



}
