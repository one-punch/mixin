package com.wteam.mixin.biz.service;

import org.aspectj.lang.JoinPoint;

public interface ILogService {
	//进入方法前调用的日志方法
	public void logBeforeEnterTheMethod(JoinPoint point);
	
	//进入方法后调用的日志方法
	public void logAfterQuitTheMethod(JoinPoint point);

}
