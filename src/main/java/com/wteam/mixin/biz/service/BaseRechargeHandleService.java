package com.wteam.mixin.biz.service;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.constant.Provinces;
import com.wteam.mixin.constant.State;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficGroupPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.query.CustomerOrderQuery;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.recharge.JingYieRecharge;
import com.wteam.mixin.recharge.XinHaoBaRecharge;
import com.wteam.mixin.recharge.JingYieRecharge.Json;
import com.wteam.mixin.recharge.JingYieRecharge.Order;
import com.wteam.mixin.utils.HttpRequest;

/**
 * 基本流量充值接口处理
 * @author benko
 *
 */
public abstract class BaseRechargeHandleService implements IRechargeHandleService{

    @Autowired
    protected IOrderService orderService;

    @Autowired
    protected IRechargeService rechargeService;

    @Autowired
    protected IBaseDao baseDao;

    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(BaseRechargeHandleService.class.getName());

    /**
     * 如果订单处于收单状态，订单成功修改状态
     */
    public static final Consumer<CustomerOrderPo> SHOU_DAN_CHAGE = order -> {
        if (order.getShouDanState() != null && order.getShouDanState().equals(State.OrderShouDan.start)) {
            order.setShouDanState(State.OrderShouDan.success);
        }
    };


    @Override
    public void recharge(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo) {

        String code = "Code", msg = "Message";
        // 获取充值结果
        Result result = new Result();
        doRecharge(orderPo, trafficPlanPo, result);

        if (result.isServiceSuccess()) {// 成功
            orderPo.setState(State.CustomerOrder.rechargeSubmit);
            orderPo.setOutOrderId(result.getOutOrderId());
            SHOU_DAN_CHAGE.accept(orderPo);
        } else {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            orderPo.setFailedInfo(result.getMsg());
        }
    }

    @Override
    public void reCallback(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo) {

        CustomerOrderVo orderVo = null;

        // 获取订单信息
        Result result = new Result();
        try {
            doReCallback(orderPo, trafficPlanPo, result);

            if (result.isSuccuss) {// 成功
                if (!result.isProcessing()) {
                    if (!result.isServiceSuccess) {
                        orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.failure);
                        orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.refunded);

                        orderVo.setFailedInfo(result.getMsg());
                    }else {
                        orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.success);
                    }
                }

            } else {
                orderPo.setFailedInfo(result.getMsg());
            }
        }
        catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public void callback(HttpServletRequest request, HttpServletResponse response) {
        String params = null;
        Result result = new Result();
        try {
            // 流
            params = HttpRequest.inToString(request.getInputStream());
            // 参数键值对
            if ("".equals(params)) {
                JSONObject jsonObject = new JSONObject();
                request.getParameterMap().forEach(
                    (key,values) -> jsonObject.put(key, values.length > 1 ? values : values[0]));
                params = jsonObject.toJSONString();
            }
        }
        catch (Exception e) {
            LOG.error("", e);
        }
        LOG.debug(params);
        if (params == null) return;
        doCallback(params, result);

        CustomerOrderVo orderVo = null;
        // 更新订单信息
        String orderNum = result.getOrderNum();
        if (orderNum != null) {
            if (result.isServiceSuccess) {
                orderVo = orderService.chageOrderState(orderNum, State.CustomerOrder.success);
            }
            else {
                orderService.chageOrderState(orderNum, State.CustomerOrder.failure);
                orderVo = orderService.chageOrderState(orderNum, State.CustomerOrder.refunded);
                orderVo.setFailedInfo(result.getMsg());
            }
            orderVo.setIsCallback(true);
            orderService.update(orderVo, orderNum);
            //
            rechargeService.apiCallback(orderVo);
        }

        try {
            response.getWriter().write(result.getCallbackMsg());
            response.getWriter().flush();
            response.getWriter().close();
        }
        catch (Exception e) {
            LOG.error("", e);
        }
    }

    protected CustomerOrderVo getOrder(String outOrderId) {
        CustomerOrderQuery query = new CustomerOrderQuery();
        query.setOutOrderId(outOrderId);
        List<CustomerOrderVo> list = orderService.listByQuery(query);
        return list.isEmpty() ? null : list.get(0);
    }

    /**
     * 流量是否是全国流量
     * @param trafficPlanPo
     * @return
     */
    protected boolean isTrafficQuanGuo(TrafficPlanPo trafficPlanPo) {
        TrafficGroupPo group = baseDao.getOnly("from TrafficGroupPo where id=?", Arrays.asList(trafficPlanPo.getTrafficGroupId()));
        return group == null ? false : Provinces.QG.equals(group.getProvince());
    }

    /**
     * 处理充值
     * @param result
     */
    protected abstract void doRecharge(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo, Result result);
    /**
     * 处理向商家回调
     * @param result
     */
    protected abstract void doReCallback(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo, Result result);
    /**
     * 处理回调
     * @param result
     */
    protected abstract void doCallback(String callback,Result result);

    /**
     * 处理结果
     * @author benko
     */
    public static class Result {

        /**是否是否成功*/
        private boolean isSuccuss;
        /**是否业务成功*/
        private boolean isServiceSuccess;
        /**是否处理中*/
        private boolean processing;

        /**业务信息*/
        private String msg;
        /**回调信息*/
        private String callbackMsg;
        /**订单号*/
        private String orderNum;
        /**外部订单号*/
        private String outOrderId;

        public boolean isSuccuss() {
            return isSuccuss;
        }

        public void setSuccuss(boolean isSuccuss) {
            this.isSuccuss = isSuccuss;
        }

        public boolean isServiceSuccess() {
            return isServiceSuccess;
        }

        public void setServiceSuccess(boolean isServiceSuccess) {
            this.isServiceSuccess = isServiceSuccess;
        }
        public boolean isProcessing() {
            return processing;
        }
        public void setProcessing(boolean processing) {
            this.processing = processing;
        }
        public String getMsg() {
            return msg;
        }
        public void setMsg(String msg) {
            this.msg = msg;
        }
        public String getCallbackMsg() {
            return callbackMsg;
        }

        public void setCallbackMsg(String callbackMsg) {
            this.callbackMsg = callbackMsg;
        }

        public String getOrderNum() {
            return orderNum;
        }

        public void setOrderNum(String orderNum) {
            this.orderNum = orderNum;
        }

        public String getOutOrderId() {
            return outOrderId;
        }

        public void setOutOrderId(String outOrderId) {
            this.outOrderId = outOrderId;
        }

    }

}
