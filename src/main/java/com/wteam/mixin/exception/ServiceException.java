package com.wteam.mixin.exception;

import com.wteam.mixin.define.ResultInfo;

/**
 * 业务失败时触发 的异常
 * @version 1.0
 * @author benko
 * @time 2016-5-16 12:39:07
 */
public class ServiceException extends RuntimeException{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Integer code;
	private String info;

	public ServiceException(String message, Throwable cause) {
		super(message, cause);
		this.code = ResultInfo.SERVICE_FAIL;
		this.info = message;
	}

	public ServiceException(Integer code, Throwable cause) {
		super(ResultInfo.getInfo(code), cause);
		this.code = code;
		this.info = ResultInfo.getInfo(code);
	}


     /**
      *
      * @param message
      */
     public ServiceException(Integer code, String message) {
         super(message);
         this.code = code;
         this.info = message;
     }

	/**
	 *
	 * @param message
	 */
	public ServiceException(String message) {
		super(message);
		this.code = ResultInfo.SERVICE_FAIL;
		this.info = message;
	}


	/**
	 *
	 * @param code
	 */
	public ServiceException(Integer code) {
		super(ResultInfo.getInfo(code));
		this.code = code;
		this.info = ResultInfo.getInfo(code);
	}

	public Integer getCode() {
		return code;
	}

	public String getInfo() {
		return info;
	}

}
