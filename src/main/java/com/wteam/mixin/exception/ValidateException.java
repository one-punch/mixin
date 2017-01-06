package com.wteam.mixin.exception;

import com.wteam.mixin.define.ResultInfo;

/**
 * 请求信息验证失败时触发 的异常
 * @version 1.0
 * @author benko
 * @time
 */
public class ValidateException extends RuntimeException {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private Object validate;

	/**
	 * @param validate 验证信息
	 * @param cause
	 */
	public ValidateException(Object validate, Throwable cause) {
		super(ResultInfo.getInfo(ResultInfo.VALIDATE_FAIL), cause);
		this.validate = validate;
	}

	/**
	 * @param validate 验证信息
	 */
	public ValidateException(Object validate) {
		super(ResultInfo.getInfo(ResultInfo.VALIDATE_FAIL));
		this.validate = validate;
	}

	/**
	 * 获取验证信息
	 * @return
	 */
	public Object getValidate() {
		return validate;
	}

}
