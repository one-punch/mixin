package com.wteam.mixin.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 淘宝相关，定时器任务
 * @author benko
 *
 */
public class AlipayTask {

    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(AlipayTask.class.getName());

    /**
     * 淘宝网加款
     */
    public void addBalance() {
        LOG.debug("");


    }

}
