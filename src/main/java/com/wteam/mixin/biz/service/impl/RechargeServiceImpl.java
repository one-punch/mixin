package com.wteam.mixin.biz.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dom4j.Element;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.IOrderDao;
import com.wteam.mixin.biz.dao.ISystemDao;
import com.wteam.mixin.biz.dao.ITrafficPlanDao;
import com.wteam.mixin.biz.service.BaseRechargeHandleService;
import com.wteam.mixin.biz.service.IOrderService;
import com.wteam.mixin.biz.service.IRechargeHandleService;
import com.wteam.mixin.biz.service.IRechargeService;
import com.wteam.mixin.constant.DConfig;
import com.wteam.mixin.constant.PaymentMethod;
import com.wteam.mixin.constant.ProductType;
import com.wteam.mixin.constant.Provider;
import com.wteam.mixin.constant.Provinces;
import com.wteam.mixin.constant.State;
import com.wteam.mixin.define.ResultInfo;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.TrafficGroupPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.query.CustomerOrderQuery;
import com.wteam.mixin.model.query.TrafficGroupQuery;
import com.wteam.mixin.model.query.TrafficPlanQuery;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.model.vo.TrafficApiRechargeInfoVo;
import com.wteam.mixin.model.vo.TrafficApiRequestParams;
import com.wteam.mixin.recharge.JingYieRecharge;
import com.wteam.mixin.recharge.LiuLiangRecharge;
import com.wteam.mixin.recharge.JingYieRecharge.Json;
import com.wteam.mixin.recharge.JingYieRecharge.Order;
import com.wteam.mixin.recharge.KuaiChongRecharge;
import com.wteam.mixin.recharge.RechargeProvider;
import com.wteam.mixin.recharge.XiaoZhuRecharge;
import com.wteam.mixin.recharge.XiaoZhuRecharge.Response;
import com.wteam.mixin.utils.HttpRequest;
import com.wteam.mixin.utils.MD5Utils;
import com.wteam.mixin.utils.Utils;
import com.wteam.mixin.recharge.XinHaoBaRecharge;
import com.wteam.mixin.recharge.YiShaRecharge;

import static com.wteam.mixin.recharge.RechargeProvider.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;


/**
 * 流量充值模块业务实现类
 * @version 1.0
 * @author benko
 * @time 2016-08-12
 */
@Service("rechargeService")
public class RechargeServiceImpl implements IRechargeService {


    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(RechargeServiceImpl.class.getName());
    /** 基本dao. */
    @Autowired
    private IBaseDao baseDao;

    @Autowired
    ISystemDao systemDao;

    @Autowired
    IOrderDao orderDao;

    @Autowired
    ITrafficPlanDao trafficPlanDao;

    /** 对象转换器. */
    @Autowired
    private DozerBeanMapper mapper;

    @Autowired
    IOrderService orderService;

    @Autowired
    ThreadPoolTaskExecutor taskExector;

    @Autowired
    IRechargeHandleService deLiHandleService;
    @Autowired
    IRechargeHandleService shangTongHandleService;
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



    @Override
    public CustomerOrderVo recharge(String orderNum) {
        // 1.根据订单号获取订单PO
        CustomerOrderPo orderPo = baseDao.findUniqueByProperty("orderNum", orderNum, CustomerOrderPo.class);
        if (orderPo == null) {
            throw new ServiceException("订单不存在");
        }
        // 2.根据不同的接口提供商调用不同的充值接口，并接收返回的状态
        if (!orderPo.getProductType().equals(ProductType.Traffic))  {
            throw new ServiceException("不是流量订单，不能进行流量充值");
        }

        if (orderPo.getState() != State.CustomerOrder.paySuccess) {
            if (orderPo.getState() == State.CustomerOrder.rechargeSubmit
                && orderPo.getShouDanState() == State.OrderShouDan.start) {

            }else if (orderPo.getState() == State.CustomerOrder.rechargeSubmit
                && orderPo.getShouDanState() != State.OrderShouDan.start) {
                throw new ServiceException("订单已经充值不能再充值了");
            }
            else {
                throw new ServiceException("订单没有支付成功不能充值");
            }
        }

        // 获取套餐
        List<TrafficPlanPo> list = getTrafficPlanPo(orderPo);
        if (list.isEmpty()) {
            throw new ServiceException("没有找到对应的套餐");
        }

        // 3.如果成功，将状态改为“充值提交”，否则改为“充值提交失败”
        String isShouDan = systemDao.dconfig(DConfig.ShouDanSwitch);

        if (isShouDan != null && "true".equals(isShouDan)) {
            orderPo.setState(State.CustomerOrder.rechargeSubmit);
            orderPo.setShouDanState(State.OrderShouDan.start);
        }
        else {
            TrafficPlanPo trafficPlanPo = null;
            if(LOG.isDebugEnabled())LOG.debug("trafficPlanPo : {}",list);

            Predicate<CustomerOrderPo> check = order -> !order.getState().equals(State.CustomerOrder.rechargeSubmit)
                || ( order.getShouDanState()!= null && order.getShouDanState().equals(State.OrderShouDan.start));

            // 给手机充流量，直到充值提交成功
            for (int i = 0; i < list.size() && check.test(orderPo); i++ ) {
                trafficPlanPo = list.get(i);
                switch (trafficPlanPo.getApiProvider()) {
                    case XinHaoBa:
                        xinHaoBa(orderPo, trafficPlanPo);
                        break;
                    case YiSai:
                        yiSai(orderPo, trafficPlanPo);
                        break;
                    case JingYie:
                        jingYie(orderPo, trafficPlanPo);
                        break;
                    case XiaoZhu:
                        xiaoZhu(orderPo, trafficPlanPo);
                        break;
                    case LiuLiang:
                        liuLiang(orderPo, trafficPlanPo);
                        break;
                    case KuaiChong:
                        kuaiChong(orderPo, trafficPlanPo);
                        break;
                    case DeLi:
                        deLiHandleService.recharge(orderPo, trafficPlanPo);
                        break;
                    case ShangTong:
                        shangTongHandleService.recharge(orderPo, trafficPlanPo);
                        break;
                    case DaHan:
                        daHanHandleService.recharge(orderPo, trafficPlanPo);
                        break;
                    case YouXing:
                        youXingHandleService.recharge(orderPo, trafficPlanPo);
                        break;
                    case YouXing2:
                        youXing2HandleService.recharge(orderPo, trafficPlanPo);
                        break;
                    case ShanWang:
                        shanWangHandleService.recharge(orderPo, trafficPlanPo);
                        break;
                    case YiKuai:
                        yikuaiHandleService.recharge(orderPo, trafficPlanPo);
                        break;
                    case DaZhong:
                    	dazhongRechargerService.reCallback(orderPo, trafficPlanPo);
                    	break;
                    case NULL:
                        orderPo.setState(State.CustomerOrder.rechargeSubmit);
                        break;

                    default:
                        throw new ServiceException("没有此接口商");
                }
                orderPo.setRealProductId(trafficPlanPo.getId());
            }
            String label = !orderPo.getState().equals(State.CustomerOrder.rechargeSubmit) ? "最后一个失败接口" : "成功提交接口";
            if(LOG.isDebugEnabled() )LOG.debug("{}:{}", label, trafficPlanPo);

            if (orderPo.getShouDanState() != null) orderPo.setShouDanState(State.OrderShouDan.end);
        }
        baseDao.update(orderPo);

        return mapper.map(orderPo, CustomerOrderVo.class);
    }

    @Override
    public void reCallback(String orderNum) {
        // TODO Auto-generated method stub
        // 1.根据订单号获取订单PO
        CustomerOrderPo orderPo = baseDao.findUniqueByProperty("orderNum", orderNum, CustomerOrderPo.class);
        if (orderPo == null) {
            throw new ServiceException("订单不存在");
        }
        // 2.根据不同的接口提供商调用不同的充值接口，并接收返回的状态
        if (!orderPo.getProductType().equals(ProductType.Traffic))  {
            throw new ServiceException("不是流量订单，不能进行回调");
        }
        reCallback(orderPo);
    }

    @Override
    public void reAllCallback() {
        // TODO Auto-generated method stub
        List<CustomerOrderPo> orderPos = baseDao.find("from CustomerOrderPo "
            + " where outOrderId is not null and state=? and productType=?",
            Arrays.asList(State.CustomerOrder.rechargeSubmit,ProductType.Traffic));
        orderPos.forEach(orderPo -> reCallback(orderPo));
    }

    @Override
    public void getAllOutOrderId() {
        List<CustomerOrderPo> orderPos = baseDao.find("from CustomerOrderPo "
            + " where outOrderId is null and state=? and productType=?",
            Arrays.asList(State.CustomerOrder.rechargeSubmit,ProductType.Traffic));

    }

    private void reCallback(CustomerOrderPo orderPo) {
        // 获取套餐
        Long trafficplanId = orderPo.getRealProductId() != null ? orderPo.getRealProductId() : orderPo.getProductId();
        TrafficPlanPo trafficPlanPo = baseDao.findUniqueByProperty("id", trafficplanId, TrafficPlanPo.class);
        // 3.如果成功，将状态改为“充值提交”，否则改为“充值提交失败”
        switch (trafficPlanPo.getApiProvider()) {
            case NULL:
                apiCallback(mapper.map(orderPo, CustomerOrderVo.class));
                break;
            case XinHaoBa:
                break;
            case YiSai:
                break;
            case JingYie:{
                CustomerOrderVo orderVo = null;
                try {

                    Json json = JingYieRecharge.instance().getOrder(orderPo.getOutOrderId());
                    if ("00".equals(json.getMsgbody().getResp().getRcode())) {// 成功
                        Integer state = Integer.valueOf(json.getMsgbody().getContent().getStatus());
                        if (state == Order.Status.failure.id
                            || state == Order.Status.success.id) {
                            if (state == Order.Status.failure.id) {
                                orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.failure);
                                orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.refunded);

                                orderVo.setFailedInfo(json.getMsgbody().getContent().getStatus());
                            }
                            if (state == Order.Status.success.id) {
                                orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.success);
                            }

                        }

                    } else {
                        orderPo.setFailedInfo(json.getMsgbody().getResp().getRmsg());
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                }

           }break;
            case XiaoZhu:{
                CustomerOrderVo orderVo = null;
                try {
                    Response response = XiaoZhuRecharge.instance().getOrder(orderPo.getOutOrderId());
                    if (!"-1".equals(response.getStatus())) {
                        Integer state = Integer.valueOf(response.getStatus());
                        if (state == 12
                            || state == 13) {
                            if (state == 12) {
                                orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.failure);
                                orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.refunded);
                                orderVo.setFailedInfo(response.getMessage());
                            }
                            if (state == 13) {
                                orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.success);
                            }
                        }
                    } else {
                        orderPo.setFailedInfo(response.getMessage());
                    }

                }
                catch (Exception e) {
                    // TODO: handle exception
                }

            }break;

            case LiuLiang:{
                CustomerOrderVo orderVo = null;
                try {

                    LiuLiangRecharge.Response response = LiuLiangRecharge.instance().getOrder(orderPo.getOrderNum());
                    LiuLiangRecharge.Responseitems responseitems = response.getData().getResponseitems().get(0);
                    if (!response.getIssuccess()) {

                        if ("0000".equals(responseitems.getCode())
                            || "1111".equals(responseitems.getCode())) {
                            if ("1111".equals(responseitems.getCode())) {
                                orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.failure);
                                orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.refunded);
                                orderVo.setFailedInfo(responseitems.getMessage());
                            }
                            if ("0000".equals(responseitems.getCode())) {
                                orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.success);
                            }
                        }
                    } else {
                        orderPo.setFailedInfo(response.getMessage());
                    }
                }
                catch (Exception e) {
                    // TODO: handle exception
                }

            }break;

            case KuaiChong:{
                CustomerOrderVo orderVo = null;
                try {
                    KuaiChongRecharge.Response response = KuaiChongRecharge.instance().getOrder(orderPo.getOutOrderId());
                    if (!"000".equals(response.getCode())) {
                        if ("2".equals(response.getStatus())
                            || "3".equals(response.getStatus())) {
                            if ("3".equals(response.getStatus())) {
                                orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.failure);
                                orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.refunded);
                                orderVo.setFailedInfo(response.getMessage());
                            }
                            if ("2".equals(response.getStatus())) {
                                orderVo = orderService.chageOrderState(orderPo.getId(), State.CustomerOrder.success);
                            }
                        }

                    } else {
                        orderPo.setFailedInfo(response.getMessage());
                    }

                }
                catch (Exception e) {
                    // TODO: handle exception
                }
            }break;
            case DeLi: deLiHandleService.reCallback(orderPo, trafficPlanPo); break;
            case ShangTong: shangTongHandleService.reCallback(orderPo, trafficPlanPo); break;
            case DaHan: daHanHandleService.reCallback(orderPo, trafficPlanPo); break;
            case YouXing: youXingHandleService.reCallback(orderPo, trafficPlanPo); break;
            case YouXing2: youXing2HandleService.reCallback(orderPo, trafficPlanPo); break;
            case ShanWang: shanWangHandleService.reCallback(orderPo, trafficPlanPo); break;
            case YiKuai: yikuaiHandleService.reCallback(orderPo, trafficPlanPo); break;
            default:
        }
        orderPo.setIsCallback(true);
        baseDao.update(orderPo);
        LOG.debug(orderPo);
        LOG.debug(mapper.map(orderPo, CustomerOrderVo.class));
        apiCallback(mapper.map(orderPo, CustomerOrderVo.class));
    }

    private List<TrafficPlanPo> getTrafficPlanPo(CustomerOrderPo orderPo) {
        List<TrafficPlanPo> trafficPlanPos = new ArrayList<>();
        TrafficPlanPo trafficPlanPo = baseDao.findUniqueByProperty("id", orderPo.getProductId(), TrafficPlanPo.class);
        String _province = null; // 流量分组的省份 要为全国
        if (trafficPlanPo == null ) return trafficPlanPos;

        if ( trafficPlanPo.getTrafficGroupId() != null) {
            TrafficGroupPo groupPo = baseDao.find(TrafficGroupPo.class, trafficPlanPo.getTrafficGroupId());
            _province = groupPo.getProvince();
        }

        if(LOG.isDebugEnabled())LOG.debug("province:{}, IsAuto:{}",_province, trafficPlanPo.getIsAuto());

        if (Provinces.QG.equals(_province) && trafficPlanPo.getIsAuto()) {
            final Function<TrafficPlanPo, BigDecimal> byCost = trafficPlan -> trafficPlan.getCost();
            // 根据手机获取移动商和省份
            String[] info = Utils.getInfoByPhone(orderPo.getPhone());
            String province = info[0];
            Integer provider = Provider.get("中国" + info[1]).id;
            // 查询对应省份和值的套餐的
            TrafficGroupQuery groupQuery = new TrafficGroupQuery();
            TrafficPlanQuery planQuery = new TrafficPlanQuery();
            groupQuery.setIsDelete(false);
            groupQuery.setDisplay(true);
            groupQuery.setProvider(provider);

            planQuery.setIsDelete(false);
            planQuery.setDisplay(true);
            planQuery.setIsMaintain(false);
            planQuery.setProvider(provider);
            planQuery.setNotApiProvider(RechargeProvider.NULL);
            planQuery.setValue(trafficPlanPo.getValue());
            // 查询具体省份
            groupQuery.setProvince(province);
            List<TrafficPlanPo> list = trafficPlanDao.find(planQuery, groupQuery);
            List<TrafficPlanPo> shenList = list.stream().sorted(Comparator.comparing(byCost)).collect(Collectors.toList());
            // 查询全国
            groupQuery.setProvince(Provinces.QG);
            list = trafficPlanDao.find(planQuery, groupQuery);
            List<TrafficPlanPo> quanList = list.stream().sorted(Comparator.comparing(byCost)).collect(Collectors.toList());
            trafficPlanPos.addAll(shenList);
            trafficPlanPos.addAll(quanList);
        } else {
            trafficPlanPos.add(trafficPlanPo);
        }

        return trafficPlanPos;
    }

    private void xinHaoBa(CustomerOrderPo orderPo , TrafficPlanPo trafficPlanPo ) {
        String code = "Code", msg = "Message";

        XinHaoBaRecharge.Response json = XinHaoBaRecharge.instance().recharge(orderPo.getOrderNum(), orderPo.getPhone(),
            trafficPlanPo.getPid(), trafficPlanPo.getValue());

        if (json == null) {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            return;
        }
        if ("1".equals(json.getCode())) {// 成功
            orderPo.setState(State.CustomerOrder.rechargeSubmit);
            BaseRechargeHandleService.SHOU_DAN_CHAGE.accept(orderPo);
        } else {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            orderPo.setFailedInfo(json.getMessage());
        }

    }


    private void yiSai(CustomerOrderPo orderPo , TrafficPlanPo trafficPlanPo ) {

        Element xml = YiShaRecharge.instance()
            .recharge(orderPo.getOrderNum(), orderPo.getPhone(), trafficPlanPo.getPid())
            .getRootElement();

        if (xml == null) {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            return;
        }
        if ("success".equals(xml.elementText("Result"))) {// 成功
            orderPo.setState(State.CustomerOrder.rechargeSubmit);
            BaseRechargeHandleService.SHOU_DAN_CHAGE.accept(orderPo);
        } else {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            orderPo.setFailedInfo(xml.elementText("Remark"));
        }

    }

    private void xiaoZhu(CustomerOrderPo orderPo , TrafficPlanPo trafficPlanPo ) {
        Response response = XiaoZhuRecharge.instance().recharge(orderPo.getOrderNum(), orderPo.getPhone(), trafficPlanPo.getPid());

        if (response == null) {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            return;
        }
        if ("13".equals(response.getStatus())) {// 成功
            orderPo.setState(State.CustomerOrder.rechargeSubmit);
            orderPo.setOutOrderId(response.getOrderNum());
            BaseRechargeHandleService.SHOU_DAN_CHAGE.accept(orderPo);
        } else {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            orderPo.setFailedInfo(response.getMessage());
        }

    }


    private void jingYie(CustomerOrderPo orderPo , TrafficPlanPo trafficPlanPo ) {

        Json json = JingYieRecharge.instance()
            .recharge(orderPo.getOrderNum(), orderPo.getPhone(), trafficPlanPo.getPid());

        if (json == null) {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            return;
        }
        if ("00".equals(json.getMsgbody().getResp().getRcode())) {// 成功
            orderPo.setState(State.CustomerOrder.rechargeSubmit);
            orderPo.setOutOrderId(json.getMsgbody().getContent().getOrderId()); // 记录订单号
            BaseRechargeHandleService.SHOU_DAN_CHAGE.accept(orderPo);
        } else {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            orderPo.setFailedInfo(json.getMsgbody().getResp().getRmsg());
        }

    }

    private void liuLiang(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo) {
        LiuLiangRecharge.Response response = LiuLiangRecharge.instance()
            .recharge(orderPo.getOrderNum(), orderPo.getPhone(), trafficPlanPo.getValue());

        if (response == null) {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            return;
        }
        if (!response.getIssuccess()) {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            orderPo.setFailedInfo(response.getMessage());
            return;
        }
        LiuLiangRecharge.Responseitems responseitems = response.getData().getResponseitems().get(0);
        if ("0000".equals(responseitems.getCode())) {// 成功
            orderPo.setState(State.CustomerOrder.rechargeSubmit);
            orderPo.setOutOrderId(responseitems.getOrderno());
            BaseRechargeHandleService.SHOU_DAN_CHAGE.accept(orderPo);
        } else {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            orderPo.setFailedInfo(responseitems.getMessage());
        }

    }

    private void kuaiChong(CustomerOrderPo orderPo, TrafficPlanPo trafficPlanPo) {
        KuaiChongRecharge.Response response = KuaiChongRecharge.instance()
            .recharge( orderPo.getPhone(), trafficPlanPo.getPid());

        if (response == null) {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            return;
        }
        if ("000".equals(response.getCode())) {// 成功
            orderPo.setState(State.CustomerOrder.rechargeSubmit);
            orderPo.setOutOrderId(response.getRealTaskId());
            BaseRechargeHandleService.SHOU_DAN_CHAGE.accept(orderPo);
        } else {
            orderPo.setState(State.CustomerOrder.rechargeFail);
            orderPo.setFailedInfo(response.getMessage());
        }

    }

    @Override
    public TrafficApiRechargeInfoVo apiInfo(Long businessId) {
        BusinessInfoPo infoPo = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{businessId});
        if (infoPo == null)
            throw new ServiceException("找不到商家信息 " + businessId);
        TrafficApiRechargeInfoVo infoVo = mapper.map(infoPo, TrafficApiRechargeInfoVo.class);

        return infoVo;
    }


    @Override
    public TrafficApiRechargeInfoVo updateApiInfo(TrafficApiRechargeInfoVo infoVo) {
        BusinessInfoPo infoPo = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{infoVo.getBusinessId()});
        if (infoPo == null)
            throw new ServiceException("找不到商家信息 " + infoVo.getBusinessId());
        if (infoVo.getApiRechargeCallbackUrl() != null)
            infoPo.setApiRechargeCallbackUrl(infoVo.getApiRechargeCallbackUrl());
        if (infoVo.getApiRechargeIp() != null)
            infoPo.setApiRechargeIp(infoVo.getApiRechargeIp());

        baseDao.update(infoPo);

        return mapper.map(infoPo, TrafficApiRechargeInfoVo.class);
    }


    @Override
    public TrafficApiRechargeInfoVo changeApiRechargeAuthoried(Long businessId) {
        BusinessInfoPo infoPo = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{businessId});
        if (infoPo == null)
            throw new ServiceException("找不到商家信息 " + businessId);

        if (infoPo.getIsApiRechargeAuthorized() == false) {
            if (infoPo.getApiRechargeAppId() == null
                || infoPo.getApiRechargeKey() == null) {
                infoPo.setApiRechargeAppId(MD5Utils.md5(infoPo.getBusinessId().toString() + System.currentTimeMillis() + "") );
                infoPo.setApiRechargeKey(MD5Utils.md5(infoPo.getApiRechargeAppId()));
            }

            infoPo.setIsApiRechargeAuthorized(true);
        } else {
            infoPo.setIsApiRechargeAuthorized(false);
        }
        baseDao.update(infoPo);

        return mapper.map(infoPo, TrafficApiRechargeInfoVo.class);
    }


    @Override
    public CustomerOrderVo apiRecharge(TrafficApiRequestParams params) {
        LOG.debug("params->{}", params);
        // 查询商家信息
        BusinessInfoPo infoPo = baseDao.get("from BusinessInfoPo where apiRechargeAppId=?", new Object[]{params.getAppid()});
        // 检查参数合法性
        check(infoPo, params);
        // 验证签名
        params.setKey(infoPo.getApiRechargeKey());
        if (!params.createSign().equals(params.getSign())) {
            throw new ServiceException("签名错误");
        }
        // 查询订单有没有冲突
        CustomerOrderPo orderPo = baseDao.get("from CustomerOrderPo where businessId=? and businessOutOrderNum=?",
            new Object[]{infoPo.getBusinessId(), params.getOutTradeNum()});
        if (orderPo != null)
            throw new ServiceException("订单已存在！ ");
        // 查询套餐
        TrafficPlanPo planPo = baseDao.get("from TrafficPlanPo where productNum=?", new Object[]{params.getProductNum()});

        if (planPo == null)
            throw new ServiceException("非法产品编号");
        if (!planPo.getIsApiRecharge())
            throw new ServiceException("此产品没有开通");

        // 提交订单
        CustomerOrderVo orderVo = new CustomerOrderVo();
        orderVo.setBusinessId(infoPo.getBusinessId());
        orderVo.setCustomerId(infoPo.getBusinessId());
        orderVo.setProductType(ProductType.Traffic);
        orderVo.setPhone(params.getTel());
        orderVo.setProductId(planPo.getId());
        orderVo.setPaymentMethod(PaymentMethod.ApiBalance);
        orderVo.setBusinessOutOrderNum(params.getOutTradeNum());
        orderVo = orderService.submitOrder(orderVo);

        // 支付订单
        orderVo = orderService.pay(orderVo);

        // 充流量
        orderVo = recharge(orderVo.getOrderNum());

        // 充值失败退款
        if (orderVo.getState().equals(State.CustomerOrder.rechargeFail)) {
            orderVo = orderService.chageOrderState(orderVo.getOrderNum(), State.CustomerOrder.refunded);
        }

        return orderVo;
    }


    @Override
    public CustomerOrderVo orderQuery(TrafficApiRequestParams params) {
        // 查询商家信息
        BusinessInfoPo infoPo = baseDao.get("from BusinessInfoPo where apiRechargeAppId=?", new Object[]{params.getAppid()});
        // 检查参数合法性
        check(infoPo, params);
        // 验证签名
        params.setKey(infoPo.getApiRechargeKey());


        if (!params.createSign().equals(params.getSign())) {
            throw new ServiceException("签名错误");
        }

        // 查询订单有没有冲突
        CustomerOrderPo orderPo = baseDao.get("from CustomerOrderPo where businessId=? and businessOutOrderNum=?",
            new Object[]{infoPo.getBusinessId(), params.getOutTradeNum()});
        if (orderPo == null)
            throw new ServiceException("订单不存在！ ");
        CustomerOrderVo order = mapper.map(orderPo, CustomerOrderVo.class);

        if (order.getProductType().equals(ProductType.Traffic)) {
            TrafficPlanPo planPo = baseDao.find(TrafficPlanPo.class, order.getProductId());
            order.setProductName(planPo.getName());
            order.setTrafficplanValue(planPo.getValue());
        }

        return order;
    }


    @Override
    public void apiCallback(CustomerOrderVo orderVo) {
        // TODO
        BusinessInfoPo infoPo = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{orderVo.getBusinessId()});
        String callbackUrl = infoPo.getApiRechargeCallbackUrl();
        if (!infoPo.getIsApiRechargeAuthorized()) {
            if(LOG.isInfoEnabled()) LOG.info("没有授权");
            return;
        }
        if (infoPo.getApiRechargeCallbackUrl() == null) {
            if(LOG.isInfoEnabled()) LOG.info("没有设置回调路径");
            return;
        }
        if (!orderVo.getPaymentMethod().equals(PaymentMethod.ApiBalance)) {
            if(LOG.isInfoEnabled()) LOG.info("不是接口充值订单");
            return;
        }

        // 1.根据订单号获取订单PO
        CustomerOrderPo orderPo = baseDao.findUniqueByProperty("orderNum", orderVo.getOrderNum(), CustomerOrderPo.class);
        orderPo.setIsBusinessCallback(true);
        baseDao.update(orderPo);

        ResultMessage resultMessage = new ResultMessage();
        resultMessage.putParam("orderNum", orderVo.getOrderNum());
        resultMessage.putParam("outTradeNum", orderVo.getBusinessOutOrderNum());
        switch (orderVo.getState()) {
            case State.CustomerOrder.success:
                resultMessage.setSuccessInfo("充值成功！");break;
            case State.CustomerOrder.rechargeFail:
            case State.CustomerOrder.failure:
            case State.CustomerOrder.refunded:
                resultMessage.setServiceFailureInfo("充值失败");break;
            default:
                if(LOG.isInfoEnabled()) LOG.info("其它状态： {}", State.CustomerOrder.names[orderVo.getState()]);
                return;
        }

        taskExector.execute(() -> {

            String response = JSON.toJSONString(resultMessage);
            if(LOG.isInfoEnabled()) LOG.info("callback {}",response);
            for (int i = 0; i < 5; i++ ) { // 通知5次
                String result = HttpRequest.sendPostJSON(callbackUrl, response);
                if ("success".equals(result)) {
                    if(LOG.isDebugEnabled()) LOG.debug("callback success");
                    return;
                }
            }

            if(LOG.isDebugEnabled()) LOG.debug("callback end");
        });

    }

    /**
     * 检查参数合法性
     * @param infoPo
     * @param params
     */
    private void check(BusinessInfoPo infoPo, TrafficApiRequestParams params) {
        if (infoPo == null)
            throw new ServiceException("无效appid: " + params.getAppid());
        // 是否授权
        if (!infoPo.getIsApiRechargeAuthorized())
            throw new ServiceException("appid未授权，请管理员授权");
        // 验证ip
        if (infoPo.getApiRechargeIp() == null)
            throw new ServiceException("没有设置IP白名单");
        else if (!infoPo.getApiRechargeIp().contains(params.getIp())) {
            throw new ServiceException("IP " + params.getIp() + " 不在白名单");
        }
    }

    public static void main(String[] args) {
        Predicate<CustomerOrderPo> check = order -> !order.getState().equals(State.CustomerOrder.rechargeSubmit)
            || ( order.getShouDanState()!= null && !order.getShouDanState().equals(State.OrderShouDan.start));
        CustomerOrderPo orderPo = new CustomerOrderPo();
        orderPo.setState(State.CustomerOrder.rechargeFail);
        System.out.println(check.test(orderPo));
        System.out.println(orderPo.getShouDanState()==State.OrderShouDan.start);
        orderPo.setState(State.CustomerOrder.rechargeSubmit);
        orderPo.setShouDanState(State.OrderShouDan.start);
        System.out.println(check.test(orderPo));
        orderPo.setState(State.CustomerOrder.rechargeFail);
        orderPo.setShouDanState(State.OrderShouDan.start);
        System.out.println(check.test(orderPo));
    }


}
