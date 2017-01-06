package com.wteam.mixin.biz.service.rehcarge.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.thoughtworks.xstream.alias.ClassMapper.Null;
import com.wteam.mixin.biz.service.BaseRechargeHandleService;
import com.wteam.mixin.constant.Provider;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.query.CustomerOrderQuery;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.recharge.DeLiRecharge;

/**
 * 得力流量接口充值处理
 * @author benko
 *
 */
@Service("deLiHandleService")
public class DeLiHandleServiceImpl extends BaseRechargeHandleService{

    private final static Map<Integer, String> channel_marker_map = new HashMap<>();
    static {
        channel_marker_map.put(Provider.DianXin.id, "JTDTY");
        channel_marker_map.put(Provider.LingTong.id, "DLTY");
        channel_marker_map.put(Provider.YiDong.id, "DLMTY");
    }

    @Override
    public void doRecharge(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo, Result result) {

        DeLiRecharge.Response response = DeLiRecharge.instance().recharge(orderPo.getPhone(),
            trafficPlanPo.getPid(), channel_marker_map.get(trafficPlanPo.getProvider()),
            super.isTrafficQuanGuo(trafficPlanPo) ? "0" : "1");

        result.setServiceSuccess("2000".equals(response.getCode())); // 必填
        result.setOutOrderId(response.getSessionId()); // 必填
        result.setMsg(response.getMsg());
    }

    @Override
    public void doReCallback(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo, Result result) {
        DeLiRecharge.Response response = DeLiRecharge.instance().getOrder(orderPo.getOutOrderId());
        if (response == null) {
            result.setSuccuss(false);
            return;
        }
        result.setSuccuss(response.getState()); // 必填
        if (response.getState()) {
            DeLiRecharge.Response order = JSON.parseObject(response.getMsg(), DeLiRecharge.Response.class);

            result.setServiceSuccess("0".equals(order.getOderStat())); // 必填
            result.setMsg(order.getResultDesc());
        }
        else {
            result.setMsg(response.getMsg());
        }
    }

    @Override
    public void doCallback(String callback, Result result) {
        DeLiRecharge.Response response = JSON.parseObject(callback, DeLiRecharge.Response.class);
        // 获取订单号
        CustomerOrderVo orderVo = super.getOrder(response.getSessionId());
        String orderNum = orderVo == null ? null : orderVo.getOrderNum();

        result.setServiceSuccess("0".equals(response.getOderStat())); // 必填
        result.setMsg(response.getResultDesc());
        result.setOrderNum(orderNum); // 必填
        result.setCallbackMsg("success");

    }

}
