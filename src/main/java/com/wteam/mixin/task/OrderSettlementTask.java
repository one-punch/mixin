package com.wteam.mixin.task;

import java.util.Date;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.spring.stat.annotation.Stat;
import com.wteam.mixin.biz.service.IOrderService;
import com.wteam.mixin.constant.Configs;
import com.wteam.mixin.constant.State;
import com.wteam.mixin.model.vo.OrderSettlementVo;

/**
 * 订单结算后台线程
 * @version 1.0
 * @author benko
 * @time 2016-08-12
 */
public class OrderSettlementTask {

    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(OrderSettlementTask.class.getName());

    private static final long day_2 = 172800000L;

//    private static long day_2 = Long.valueOf(Configs.prop.getProperty(Configs.settlemet_interval,"172800000"));

    private static final long munite_30 = 1800000L;
//    private static long munite_30 = Long.valueOf(Configs.prop.getProperty(Configs.task_interval,"1800000"));

    /** 线程调度器 */
    @Autowired
    ThreadPoolTaskExecutor taskExector;

    @Autowired
    IOrderService orderService;

    public OrderSettlementTask() {
        // TODO Auto-generated constructor stub
    }

    /** 线程安全的优先队列 ，根据时间排序*/
    private PriorityBlockingQueue<OrderSettlementVo> QUEUE =
        new PriorityBlockingQueue<>(11, (o1, o2) -> o1.getCreateTime().compareTo(o2.getCreateTime()) );

    private class Task implements Runnable {

        @Override
        public void run() {
            LOG.debug("day_2:{}, munite_30:{}, time:{}", day_2, munite_30,
                System.currentTimeMillis());
            // 获取
            OrderSettlementVo settlementPo = get();

            if (settlementPo == null) return;

            long time = System.currentTimeMillis() - settlementPo.getCreateTime().getTime();
            if (time >= day_2) { // 当前时间 在此订单结算的两天后
                QUEUE.poll();// 从队列中移除此订单结算
                if (LOG.isDebugEnabled()) LOG.debug("settlement: {}", settlementPo);
                // 修改记录状态
                orderService.changeOrderSettlementState(settlementPo.getId(),
                    State.OrderSettlement.settlement);
                // 计算已结算的
                // 如果已结算的记录总和大于200元，产生账户余额
                orderService.computeOrderSettlements(settlementPo.getBusinessId());
                // 获取下一个记录，
                OrderSettlementVo nextPo = get();
                if (nextPo == null) return;

                try {
                    Thread.sleep( startTimeout(settlementPo));
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                taskExector.execute(new Task());
            }
            else {

                try {
                    Thread.sleep( startTimeout(settlementPo));
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                taskExector.execute(new Task());
            }
        }

    }

    public void execute() {
        taskExector.execute(new Task());
    }

    /**
     * 获取时间最早的记录,不从队列中移除，并设置下一次调用结算功能的时间
     * @return
     */
    private OrderSettlementVo get() {
        OrderSettlementVo settlementPo = QUEUE.peek();
        if (settlementPo == null) {
            //  从数据库中查找未结算的记录列表，加入队列中
            List<OrderSettlementVo> list = orderService.findOrderSettlementList(null, State.OrderSettlement.unSettlement);
            if (list != null && !list.isEmpty()) {
                add(list);
            }
            // 如果还是没有,30分钟后再启动此功能
            settlementPo = QUEUE.peek();
            if (settlementPo == null) {
                LOG.debug("to next");
                try {
                    Thread.sleep(munite_30);
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                }
                taskExector.execute(new Task());
            }
        }
        return settlementPo;
    }

    private long startTimeout(OrderSettlementVo po) {
        long startTimeout = po.getCreateTime().getTime() + day_2 - System.currentTimeMillis() + 10;
        if( LOG.isTraceEnabled() ) LOG.trace("startTimeout:{}, currentTime:{}, willtime:{}", startTimeout,System.currentTimeMillis(),po.getCreateTime().getTime() + day_2);
        return startTimeout >= 0? startTimeout : 0;
    }

    public void add(OrderSettlementVo po) {
        if(po == null) return;
        if (!QUEUE.contains(po)) QUEUE.add(po);
    }

    public void add(List<OrderSettlementVo> list) {
        if(list == null) return;
        list.forEach(po -> add(po));
    }

    public static void main(String[] args) {
        System.out.println(2*24*60*60*1000);

        System.out.println(30*60*1000);
    }
}
