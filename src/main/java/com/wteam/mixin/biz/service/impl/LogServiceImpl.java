package com.wteam.mixin.biz.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.service.ILogService;



/**
 * 
 * @version 1.0
 * @author 李焕滨 
 * @time 
 *
 */
@Service("logService")
public class LogServiceImpl implements ILogService {

    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(LogServiceImpl.class.getName());

	@Override
	public void logBeforeEnterTheMethod(JoinPoint point) {
		if(point == null) {
			return;
		}
		 
		String message = null;
		message = "进入" + point.getTarget().getClass().getName() + "类的"
				+ point.getSignature().getName() + "方法";
		//System.out.println(message);
		LOG.debug(message);
	}

	@Override
	public void logAfterQuitTheMethod(JoinPoint point) {
		if(point == null) {
			return;
		}
		String message = null;
		message = "退出" + point.getTarget().getClass().getName() + "类的"
				+ point.getSignature().getName() + "方法";
		//System.out.println(message);
        LOG.debug(message);
		
	}

}
