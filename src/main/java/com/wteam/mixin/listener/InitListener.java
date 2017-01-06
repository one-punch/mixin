package com.wteam.mixin.listener;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.wteam.mixin.constant.WechatConfigs;
import com.wteam.mixin.task.OrderSettlementTask;

public class InitListener implements ApplicationListener<ContextRefreshedEvent> {
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(InitListener.class.getName());
    
    /** 线程调度器 */
    @Autowired
    ThreadPoolTaskExecutor taskExector;
    
    @Autowired
    OrderSettlementTask orderSettlementTask;
    
    
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        LOG.debug("start task");
        // 启动订单结算任务
        if (event instanceof ContextRefreshedEvent
            && event.getApplicationContext().getParent() == null) {
            orderSettlementTask.execute();// 测试时关掉
            com.wteam.mixin.alipay.Configs.init();
            com.wteam.mixin.constant.Configs.init();
            WechatConfigs.init();
            
        }
    }

}
