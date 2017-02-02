package com.wteam.mixin.biz.service.impl;

import static com.wteam.mixin.constant.State.CustomerOrder.failure;
import static com.wteam.mixin.constant.State.CustomerOrder.names;
import static com.wteam.mixin.constant.State.CustomerOrder.payFail;
import static com.wteam.mixin.constant.State.CustomerOrder.paySuccess;
import static com.wteam.mixin.constant.State.CustomerOrder.rechargeFail;
import static com.wteam.mixin.constant.State.CustomerOrder.rechargeSubmit;
import static com.wteam.mixin.constant.State.CustomerOrder.refunded;
import static com.wteam.mixin.constant.State.CustomerOrder.success;
import static com.wteam.mixin.constant.State.CustomerOrder.waitPay;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.IOrderDao;
import com.wteam.mixin.biz.dao.ISystemDao;
import com.wteam.mixin.biz.dao.ITrafficPlanDao;
import com.wteam.mixin.biz.dao.IUserDao;
import com.wteam.mixin.biz.service.IFinanceService;
import com.wteam.mixin.biz.service.IOrderService;
import com.wteam.mixin.biz.service.IWechatService;
import com.wteam.mixin.constant.Configs;
import com.wteam.mixin.constant.DConfig;
import com.wteam.mixin.constant.PaymentMethod;
import com.wteam.mixin.constant.ProductType;
import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.constant.State;
import com.wteam.mixin.constant.Wechat;
import com.wteam.mixin.define.ResultInfo;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessBalanceRecordPo;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.CustomerInfoPo;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.OrderSettlementPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.query.CustomerOrderQuery;
import com.wteam.mixin.model.query.TrafficPlanQuery;
import com.wteam.mixin.model.vo.BusinessBalanceRecordVo;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.model.vo.OrderSettlementVo;
import com.wteam.mixin.pagination.Pagination;
import com.wteam.mixin.task.OrderSettlementTask;
import com.wteam.mixin.utils.IdWorker;
import com.wteam.mixin.utils.LambdaUtils;
import com.wteam.mixin.utils.Utils;

/**
 * 订单模块业务实现类
 * @version 1.0
 * @author benko
 *
 */
@Service("orderService")
public class OrderServiceImpl implements IOrderService{

    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(OrderServiceImpl.class.getName());

    @Autowired
    DozerBeanMapper mapper;

    @Autowired
    IBaseDao baseDao;
    @Autowired
    IOrderDao orderDao;
    @Autowired
    ISystemDao systemDao;
    @Autowired
    ITrafficPlanDao trafficPlanDao;
    @Autowired
    IUserDao userDao;

    @Autowired
    OrderSettlementTask orderSettlementTask;

    @Autowired
    IdWorker orderIdWorker;

    @Autowired
    IWechatService wechatService;

    @Autowired
    IFinanceService financeService;

    /**套餐转换处理*/
//    private Function<Object, CustomerOrderVo> _orderToVo = LambdaUtils.mapTo(mapper, CustomerOrderVo.class);
    /**设置顾客信息*/
    Function<CustomerOrderVo, CustomerOrderVo> setCustomerInfo = order -> {
        UserPo userPo = baseDao.find(UserPo.class, order.getCustomerId());

        List<RoleType> roles = userPo.getRoles().stream()
            .map(rolePo -> rolePo.getRole())
            .collect(Collectors.toList());

        if (roles.contains(RoleType.customer)) {
            CustomerInfoPo infoPo = baseDao.findUniqueByProperty("customerId", order.getCustomerId(), CustomerInfoPo.class);
            if(infoPo == null) {
                if(LOG.isErrorEnabled()) LOG.error("订单 {} 找不到用户 {} 的信息", order.getId(), order.getCustomerId());
                return order;
            }
            order.setWechatHead(infoPo.getHeadimgurl());
            order.setWechatName(infoPo.getNickname());
        } else if (roles.contains(RoleType.bussiness)) {
            order.setAccount(userPo.getAccount());
        }
        return order;
    };
    /**设置流量产品信息*/
    Function<CustomerOrderVo, CustomerOrderVo> setProductInfo = order -> {
        if (order.getProductType().equals(ProductType.Traffic)) {
            TrafficPlanPo planPo = baseDao.find(TrafficPlanPo.class, order.getProductId());
            order.setProductName(planPo.getName());
            order.setTrafficplanValue(planPo.getValue());
            //
            if (order.getRealProductId() != null  && !order.getProductId().equals(order.getRealProductId())) {
                planPo = baseDao.find(TrafficPlanPo.class, order.getRealProductId());
            }
            order.setRealProductName(planPo.getName());
        }
        else if (order.getProductType().equals(ProductType.BusinessBalance)) {
            order.setProductName("商家余额充值");
        }
        return order;
    };


    @Override
    public CustomerOrderVo find(String orderNum) {
        CustomerOrderPo po = baseDao.findUniqueByProperty("orderNum", orderNum, CustomerOrderPo.class);
        if (po == null) {
            new ServiceException("找不到订单 " + orderNum);
        }
        CustomerOrderVo orderVo = mapper.map(po, CustomerOrderVo.class);
        setProductInfo.apply(orderVo);
        return orderVo;
    }

    @Override
    public CustomerOrderVo find(Long orderId) {
        CustomerOrderPo po = baseDao.findUniqueByProperty("id", orderId, CustomerOrderPo.class);
        if (po == null) {
            new ServiceException("找不到订单 " + orderId);
        }
        CustomerOrderVo orderVo = mapper.map(po, CustomerOrderVo.class);
        setProductInfo.apply(orderVo);
        return orderVo;
    }


    @Override
    public CustomerOrderVo find(CustomerOrderQuery query) {
        List<CustomerOrderPo> list = orderDao.listBySuper(query);
        CustomerOrderPo order = list.isEmpty() ? null : list.get(0);
        return order == null ? null : mapper.map(order, CustomerOrderVo.class);
    }

    @Override
    public CustomerOrderVo update(CustomerOrderVo orderVo,String orderNum) {
        CustomerOrderPo po = baseDao.findUniqueByProperty("orderNum", orderNum, CustomerOrderPo.class);
        if (po == null) {
            new ServiceException("找不到订单 " + orderNum);
        }
        mapper.map(orderVo, po);
        baseDao.update(po);

        return mapper.map(po, CustomerOrderVo.class);
    }

    @Override
    public CustomerOrderVo submitOrder(CustomerOrderVo orderVo) {

        if (!ProductType.ALL.contains(orderVo.getProductType())) {
            throw new ServiceException("非法产品类型: " + orderVo.getProductType());
        }

        CustomerOrderPo po = mapper.map(orderVo, CustomerOrderPo.class);
        po.setOrderNum(orderIdWorker.nextId() + "");

        if (orderVo.getProductType().equals(ProductType.Traffic)) { // 流量
            submitTrafficOrder(po, orderVo);
        }
        else if (orderVo.getProductType().equals(ProductType.BusinessBalance)) {// 商家余额充值
            LOG.debug("state 1 : {}", po.getState());
            po = submitBusinessBalance(po, orderVo);
            LOG.debug("state: {}", po.getState());
        }

        baseDao.saveOrUpdate(po);
        return mapper.map(po, CustomerOrderVo.class);
    }

    private void submitTrafficOrder(CustomerOrderPo po, CustomerOrderVo orderVo) {

        BigDecimal cost, rate, retailPrice;
        BusinessInfoPo infoPo = baseDao.findUniqueByProperty("businessId", orderVo.getBusinessId(), BusinessInfoPo.class);
        if (infoPo == null)
            throw new ServiceException("找不到商家 ");
        TrafficPlanPo trafficPlanPo = baseDao.findUniqueByProperty("id", orderVo.getProductId(), TrafficPlanPo.class);
        if (trafficPlanPo == null)
            throw new ServiceException("找不到产品 " + orderVo.getProductId());
        if (trafficPlanPo.getIsMaintain()) {
            throw new ServiceException("产品正在维护中，暂时不能充值");
        }
        // 设置号段
        po.setHaoduan(Utils.getHaoDan(po.getPhone()));

        // 获取系统的费率
        rate = new BigDecimal(systemDao.dconfig(DConfig.CustomerOrderRate));
        // 如果商家是会员，获取对应会员套餐的成本
        cost = trafficPlanDao.cost(orderVo.getBusinessId(), orderVo.getProductId());
        // 根据商家ID获取商家套餐的零售价，并保存到订单中
        retailPrice = trafficPlanDao.retailPrice(orderVo.getBusinessId(), orderVo.getProductId());

        if (infoPo.getBalance().subtract(retailPrice).signum() < 0) {
            if (orderVo.getBusinessId().equals(orderVo.getCustomerId())) {
                throw new ServiceException("余额不足");
            }
            else {
                throw new ServiceException("商家没有流量了");
            }
        }
        if (orderVo.getBusinessId().equals(orderVo.getCustomerId())) {
            po.setCost(cost);
            po.setFactorage(cost.multiply(rate));
            po.setRetailPrice(cost.add(po.getFactorage()));
        }
        else {
            po.setCost(cost);
            po.setRetailPrice(retailPrice);
            po.setFactorage(retailPrice.multiply(rate));
            po.setRealIncome(po.getRetailPrice().subtract(po.getFactorage()));
            po.setProfits(po.getRealIncome().subtract(po.getCost()));
        }
        // 设置状态
        po.setState(State.CustomerOrder.waitPay);
    }

    /**
     * 返回true 订单已经存在，返回false，订单不存在
     * @param po
     * @param orderVo
     * @return
     */
    private CustomerOrderPo submitBusinessBalance(CustomerOrderPo po, CustomerOrderVo orderVo) {

        if (!orderVo.getBusinessId().equals(Configs.businessId_null)) {
            BusinessInfoPo infoPo = baseDao.findUniqueByProperty("businessId", orderVo.getBusinessId(), BusinessInfoPo.class);
            if (infoPo == null)
                throw new ServiceException("找不到商家 ");
        }
        CustomerOrderQuery query = new CustomerOrderQuery();
        query.setAlipayOrderId(orderVo.getAlipayOrderId());
        query.setPaymentMethod(PaymentMethod.EasyPay);
        List<CustomerOrderPo> list = orderDao.listBySuper(query);
        CustomerOrderPo orderPo = list.isEmpty() ? null : list.get(0);

        // 支付通 如果订单已经存在且已经支付
        if (orderPo != null) {

            orderPo.setBusinessId(orderVo.getBusinessId());
            orderPo.setCustomerId(orderVo.getCustomerId());

            if (orderPo.getState().equals(State.CustomerOrder.paySuccess)) {

                BusinessBalanceRecordPo recordVo = new BusinessBalanceRecordPo();
                recordVo.setBusinessId(orderPo.getBusinessId());
                recordVo.setMoney(orderPo.getRetailPrice());
                recordVo.setSource(State.BBRecordSource.balanceRechange);
                recordVo.setSourceId(Long.valueOf(orderPo.getOrderNum()));
                financeService.saveBusinessBalanceRecord(recordVo);

                orderPo.setState(State.CustomerOrder.success);
            }

            return orderPo;
        }

        if (po.getPaymentMethod().equals(PaymentMethod.EasyPay) && po.getState() != null ) {// 在alipaycontroller已经设置过状态
            return po;
        }
        else {
            // 设置状态
            po.setState(State.CustomerOrder.waitPay);
            return po;
        }
    }


    @Override
    public CustomerOrderVo pay(CustomerOrderVo orderVo) {
        CustomerOrderPo po = baseDao.findUniqueByProperty("id", orderVo.getId(), CustomerOrderPo.class);
        if (po == null) {
            new ServiceException("找不到订单 " + orderVo.getId());
        }
        if (po.getPaymentMethod() == null) {
            po.setPaymentMethod(orderVo.getPaymentMethod());
        }

        if (po.getProductType().equals(ProductType.Traffic)) { // 流量
            if (po.getPaymentMethod().equals(PaymentMethod.Wechat)) {

            } else
            if (po.getPaymentMethod().equals(PaymentMethod.Balance)) {
                // 记录到recordPo中
                payOutcomeRecord(po, State.BBRecordSource.productRechange);
                chageOrderState(po, State.CustomerOrder.paySuccess);
            } else
            if (po.getPaymentMethod().equals(PaymentMethod.ApiBalance)) {
                // 记录到recordPo中
                payOutcomeRecord(po, State.BBRecordSource.productApiRecharge);
                chageOrderState(po, State.CustomerOrder.paySuccess);
            }else if(po.getPaymentMethod().equals(PaymentMethod.Cut)){
            }
        }
        baseDao.update(po);

        return mapper.map(po, CustomerOrderVo.class);
    }



    @Override
    public CustomerOrderVo wechatPayCallback(Map<String, String> map) {
        String return_code = map.get(Wechat.XmlTags.return_code);
        CustomerOrderVo orderVo = null;
        if ("SUCCESS".equalsIgnoreCase(return_code)) {
            String orderNum = map.get(Wechat.XmlTags.out_trade_no);
            // 修改订单状态为支付成功
            orderVo = chageOrderState(orderNum, State.CustomerOrder.paySuccess);
            CustomerOrderPo orderPo = baseDao.findUniqueByProperty("orderNum", orderNum, CustomerOrderPo.class);
            // transaction_id
            orderPo.setWechatOrderId(map.get(Wechat.XmlTags.transaction_id));
        }
        return orderVo;
    }

    @Override
    public CustomerOrderVo chageOrderState(Long orderId, Integer state) {
        if (state == null || !State.CustomerOrder.ALL.contains(state)) {
            throw new ServiceException(state == null ? "没有参数state" : "非法state: " + state);
        }
        CustomerOrderPo orderPo = baseDao.findUniqueByProperty("id", orderId, CustomerOrderPo.class);
        if (orderPo == null)
            throw new ServiceException("找不到订单 " + orderId);

        return chageOrderState(orderPo, state);
    }


    @Override
    public CustomerOrderVo chageOrderState(String orderNum, Integer state) {
        if (state == null || !State.CustomerOrder.ALL.contains(state)) {
            throw new ServiceException(state == null ? "没有参数state" : "非法state: " + state);
        }
        CustomerOrderPo orderPo = baseDao.findUniqueByProperty("orderNum", orderNum, CustomerOrderPo.class);
        if (orderPo == null)
            throw new ServiceException("找不到订单 " + orderNum);

        return chageOrderState(orderPo, state);
    }

    private CustomerOrderVo chageOrderState(CustomerOrderPo orderPo, Integer state) {
        int from = orderPo.getState(), to = state;

        BusinessInfoPo infoPo = userDao.businessInfo(orderPo.getBusinessId());

        if(from == waitPay && to == paySuccess){

        }else
        if (from == waitPay && to == payFail) {

        }else
        if(from == paySuccess && to == rechargeFail){

        }else
        if(from == paySuccess && to == rechargeSubmit){

        }else
        if(from == rechargeSubmit && to == failure){

        }else
        // 进入订单结算
        if (from == rechargeSubmit && to == success) {
            boolean isTraffic = orderPo.getProductType().equals(ProductType.Traffic);
            boolean isWachat = orderPo.getPaymentMethod().equals(PaymentMethod.Wechat);
            boolean useBusinessPay = infoPo.getUseBusinessPay();
            if (isTraffic && ( !isWachat || (isWachat && !useBusinessPay) )) {
                proxyParentIncome(orderPo);
                orderSettlement(orderPo);
            }
        }else
        // 充值提交失败，微信自动退款
        if(from == rechargeFail && to == refunded){
            payRefund(orderPo);
        }else
        // 充值失败，微信自动退款
        if(from == failure && to == refunded) {
            payRefund(orderPo);
        } else {
           String msg = String.format("订单'%s': 状态'%s' 不可变为 '%s'", orderPo.getOrderNum(), names[from] , names[to]);
            throw new ServiceException(ResultInfo.ORDER_CHANGE_FAIL,"订单 " + orderPo.getOrderNum() + " 状态  " + names[from] + " 不可变为 " + names[to]);
        }
        orderPo.setState(to);
        baseDao.update(orderPo);

        return mapper.map(orderPo, CustomerOrderVo.class);
    }

    private void orderSettlement(CustomerOrderPo orderPo) {
        if (orderPo.getBusinessId().equals(orderPo.getCustomerId())) {
            return;
        }
        // 保存订单结算
        OrderSettlementPo settlementPo = new OrderSettlementPo(
            orderPo.getOrderNum(),
            orderPo.getBusinessId(),
            orderPo.getRealIncome(),
            State.OrderSettlement.unSettlement);
        baseDao.save(settlementPo);
        // 将订单结算加入到结算任务中
        orderSettlementTask.add(mapper.map(settlementPo, OrderSettlementVo.class));
        // 记录到recordPo中 订单成本转出
        payOutcomeRecord(orderPo, State.BBRecordSource.orderCost);
    }

    /**
     * 订单退款
     * @param orderPo
     */
    public void payRefund(CustomerOrderPo orderPo) {
        if (orderPo.getPaymentMethod().equals(PaymentMethod.Wechat)) {
            wechatService.payRefund(mapper.map(orderPo, CustomerOrderVo.class));
        }

        List<Integer> allowMethods = Arrays.asList(PaymentMethod.Balance, PaymentMethod.ApiBalance);
        List<RoleType> allowRoles = Arrays.asList(RoleType.bussiness, RoleType.proxy_business);

        if (allowMethods.contains(orderPo.getPaymentMethod())) {
            // 获取用户的
            UserPo userPo = baseDao.find(UserPo.class, orderPo.getCustomerId());
            Boolean isAllow = userPo.getRoles().stream()
                .map(rolePo -> rolePo.getRole())
                .filter(role -> allowRoles.contains(role))
                .findFirst().isPresent();

            if (isAllow) {
                // 记录到recordPo中
                BusinessBalanceRecordPo recordPo = new BusinessBalanceRecordPo();
                recordPo.setBusinessId(orderPo.getBusinessId());
                recordPo.setMoney(orderPo.getRetailPrice());// +
                recordPo.setSource(State.BBRecordSource.payRefund);
                recordPo.setSourceId(Long.valueOf(orderPo.getOrderNum()));
                financeService.saveBusinessBalanceRecord(recordPo);
            }

        }
    }

    @Override
    public OrderSettlementVo saveOrderSettlement(String orderNum, Long businessId, BigDecimal realIncome) {
        if (orderNum == null || businessId == null || realIncome == null) {
            throw new ServiceException(String.format("信息不全：orderNum=%s, businessId=%d, realIncome=%d", orderNum,businessId,realIncome));
        }
        CustomerOrderPo orderPo = baseDao.findUniqueByProperty("orderNum", orderNum, CustomerOrderPo.class);
        if (orderPo == null)
            throw new ServiceException("找不到订单 " + orderNum);
        BusinessInfoPo infoPo = baseDao.findUniqueByProperty("businessId", businessId, BusinessInfoPo.class);
        if (infoPo == null)
            throw new ServiceException("找不到商家" + businessId);

        OrderSettlementPo po = new OrderSettlementPo(orderNum, businessId, realIncome, State.OrderSettlement.unSettlement);
        baseDao.save(po);

        return mapper.map(po, OrderSettlementVo.class);
    }


    @Override
    public List<OrderSettlementVo> findOrderSettlementList(Long businessId, Integer state) {

        List<OrderSettlementPo> list = orderDao.findOrderSettlementList(new OrderSettlementPo(null, businessId, null, state));

        return list.stream().map(po -> mapper.map(po, OrderSettlementVo.class)).collect(Collectors.toList());
    }


    @Override
    public OrderSettlementVo changeOrderSettlementState(Long orderSettlementId,Integer state) {
        OrderSettlementPo po = baseDao.findUniqueByProperty("id", orderSettlementId, OrderSettlementPo.class);
        return changeOrderSettlementState(po, state);
    }

    /**
     * 修改订单结算的状态，并修改对应的商家账务信息和平台账务融信息
     * @param po
     * @param state
     * @return
     */
    private OrderSettlementVo changeOrderSettlementState(OrderSettlementPo po,Integer state) {
        int from = po.getState(), to = state;
        String[] names = State.OrderSettlement.names;
        // 修改
        if(from == State.OrderSettlement.unSettlement && to == State.OrderSettlement.settlement){

        }else
        if (from == State.OrderSettlement.settlement && to == State.OrderSettlement.toBalance) {

            // 记录到 Record中
            BusinessBalanceRecordPo recordPo = new BusinessBalanceRecordPo();
            recordPo.setBusinessId(po.getBusinessId());
            recordPo.setMoney(po.getRealIncome()); // +
            recordPo.setSource(State.BBRecordSource.settlement);
            recordPo.setSourceId(po.getId());
            financeService.saveBusinessBalanceRecord(recordPo);

        } else {
            throw new ServiceException(ResultInfo.ORDER_CHANGE_FAIL,"订单结算  " + po.getId() + "状态 " + names[from] + "不可变为 " + names[to]);
        }
        po.setState(to);
        baseDao.update(po);

        return mapper.map(po, OrderSettlementVo.class);
    }


    @Override
    public void computeOrderSettlements(Long businessId) {
        List<OrderSettlementPo> list = orderDao.findOrderSettlementList(new OrderSettlementPo(null, businessId, null, State.OrderSettlement.settlement));
        // 计算 此商家的已结算的总金额 单位：分
        BigDecimal count = list.stream().map(po -> po.getRealIncome()).reduce(BigDecimal.ZERO, (p1,p2) -> p1.add(p2));
        BigDecimal minPrice = new BigDecimal(systemDao.dconfig(DConfig.SettlemetMinPrice));
        if (count.compareTo(minPrice) == -1) {
            return;
        }
        // 获取时间最早的一项记录
        OrderSettlementPo min = list.stream().min((p1, p2) -> p1.getCreateTime().compareTo(p2.getCreateTime())).get();
        // 修改订单结算的状态
        changeOrderSettlementState(min, State.OrderSettlement.toBalance);
    }

    @SuppressWarnings("unchecked")
    @Override
    public Pagination listBySuper(CustomerOrderQuery query, Integer pageNo, Integer pageSize) {

        query.setProductType(ProductType.Traffic);
        query.setIsDelete(false);

        Pagination pagination = orderDao.listBySuper(query, pageNo, pageSize);
        pagination.setMsg(orderDao.statistics(query));

        return pagination.handle(bockMap(setCustomerInfo, setProductInfo));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CustomerOrderVo> listByQuery(CustomerOrderQuery query) {
        query.setProductType(ProductType.Traffic);
        query.setIsDelete(false);
        List<CustomerOrderPo> list = orderDao.listBySuper(query);
        return (List<CustomerOrderVo>)bockMap(setCustomerInfo, setProductInfo).apply(list);
    }

    @Override
    public Pagination list(CustomerOrderQuery query, TrafficPlanQuery trafficPlanQuery,
                           Integer pageNo, Integer pageSize) {
        query.setProductType(ProductType.Traffic);
        query.setIsDelete(false);
        trafficPlanQuery.setIsDelete(false);

        query.putSortField("createTime", false);
        trafficPlanQuery.putSortField("apiProvider");
        trafficPlanQuery.putSortField("createTime", false);

        Pagination pagination = orderDao.list(query, trafficPlanQuery, pageNo, pageSize);
        pagination.setMsg(orderDao.statistics(query));

        return pagination.handle(bockMap(setCustomerInfo, setProductInfo));
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CustomerOrderVo> list(CustomerOrderQuery query,
                                      TrafficPlanQuery trafficPlanQuery) {
        query.setProductType(ProductType.Traffic);
        query.setIsDelete(false);
        trafficPlanQuery.setIsDelete(false);

        query.putSortField("createTime", false);
        trafficPlanQuery.putSortField("apiProvider");
        trafficPlanQuery.putSortField("createTime", false);
        List<CustomerOrderPo> list = orderDao.list(query, trafficPlanQuery);
        return (List<CustomerOrderVo>)bockMap(setCustomerInfo, setProductInfo).apply(list);
    }

    /**
     * 商家支付支出，记录财务
     * @param po
     * @param recordSource
     */
    private void payOutcomeRecord(CustomerOrderPo po , int recordSource) {

        // 记录到recordPo中
        BusinessBalanceRecordPo recordPo = new BusinessBalanceRecordPo();
        recordPo.setBusinessId(po.getBusinessId());
        recordPo.setMoney(po.getRetailPrice().negate()); // -
        recordPo.setSource(recordSource);
        recordPo.setSourceId(Long.valueOf(po.getOrderNum()));
        financeService.saveBusinessBalanceRecord(recordPo);
    }
    /**
     * 如果是代理商家要给父商家代理佣金
     * @param po
     */
    private void proxyParentIncome(CustomerOrderPo po ) {
        // 如果是代理商家要给父商家代理佣金
        if (userDao.isRole(po.getBusinessId(), RoleType.proxy_business)) {
            BusinessInfoPo proxyParent = userDao.proxyParentBy(po.getBusinessId());
            BigDecimal parentCost = trafficPlanDao.cost(proxyParent.getBusinessId(), po.getProductId());

            BusinessBalanceRecordPo recordPo = new BusinessBalanceRecordPo();
            recordPo.setBusinessId(proxyParent.getBusinessId());
            recordPo.setMoney(po.getCost().subtract(parentCost)); // -
            recordPo.setSource(State.BBRecordSource.proxyIncome);
            recordPo.setSourceId(Long.valueOf(po.getOrderNum()));
            financeService.saveBusinessBalanceRecord(recordPo);
        }
    }

    private Function<List<?>, List<?>> bockMap(Function<CustomerOrderVo, CustomerOrderVo>... functions) {
        return LambdaUtils.bockMap(LambdaUtils.mapTo(mapper, CustomerOrderVo.class), functions);
    }

}
