package com.wteam.mixin.task;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.wteam.mixin.biz.service.IOrderService;
import com.wteam.mixin.biz.service.IRechargeService;
import com.wteam.mixin.constant.ProductType;
import com.wteam.mixin.constant.State;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.query.CustomerOrderQuery;
import com.wteam.mixin.model.vo.CustomerOrderVo;

/**
 * 将收单的订单全部充值
 * @author benko
 *
 */
public class OrderShouDanRechargeTask implements Runnable {


    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(OrderSettlementTask.class.getName());

    @Autowired
    IOrderService orderService;

    @Autowired
    IRechargeService rechargeService;

    /** 线程调度器 */
    @Autowired
    ThreadPoolTaskExecutor taskExector;
    /**
     * 是否开始
     */
    private AtomicBoolean isStart = new AtomicBoolean(false);

    /**
     * 还有下一次
     */
    private AtomicBoolean hasNext = new AtomicBoolean(false);

    /**
     * 处理记录 初始值：{0-成功数,0-失败数}
     */
    private int[] sums;

    /**
     * 处理列表
     */
    private List<CustomerOrderVo> list;

    @Override
    public void run() {
        rawRun();
        if (hasNext.get()) {
            rawRun();
        }
    }

    public boolean execute() {
        if (isStart.get()) {
            hasNext.set(true);
            return false;
        }

        taskExector.execute(this);
        return true;
    }

    private void rawRun() {
        //
        if (isStart.get()) {
            return;
        }
        isStart.set(true);

        handle();

        isStart.set(false);
    }

    public String message() {
        if (list !=null && sums != null && sums.length == 2) {
            return String.format("列表数:%d, 收单总数：%d, 成功数：%d, 失败数:%d " , list.size(), sums[0] + sums[1], sums[0], sums[1]);
        }
        return "现在没有处理收单";
    }

    /**
     * 正式处理
     */
    private void handle() {
        CustomerOrderQuery query = new CustomerOrderQuery();
        query.setState(State.CustomerOrder.rechargeSubmit);
        query.setShouDanState(State.OrderShouDan.start);
        query.setIsDelete(false);
        query.setProductType(ProductType.Traffic);

        list = orderService.listByQuery(query);
        sums = new int[] {0/*成功数*/,0/*失败数*/};
        list.forEach(order -> {
            try {
                rechargeService.recharge(order.getOrderNum());
                LOG.debug("收单成功： orderNum: " + order.getOrderNum());
                sums[0] ++;
            }
            catch (ServiceException e) {
                sums[1] ++;
                LOG.error("收单失败： orderNum: " + order.getOrderNum() + " msg:" + e.getMessage());
            }
            catch (Exception e) {
                sums[1] ++;
                LOG.error("收单失败： orderNum: " + order.getOrderNum(), e);
            }
        });
        LOG.debug("列表数:{}, 收单总数：{}, 成功数：{}, 失败数:{} " , list.size(), sums[0] + sums[1], sums[0], sums[1]);
    }



    private void test() {
        LOG.debug("收单开始： ");

        try {
            TimeUnit.MINUTES.sleep(1);
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}
