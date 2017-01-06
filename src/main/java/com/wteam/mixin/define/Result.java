package com.wteam.mixin.define;


/**
 * 基本的返回结果接口
 * @version 1.0
 * @author benko
 * @time
 */
public interface Result {


	/**
	 * 业务是否成功
	 * @return
	 */
	boolean isSuccess();
	/**
	 * 获取结果状态编号
	 * @return
	 */
	Integer getResultCode();
	/**
	 * 获取结果状态信息
	 * @return
	 */
	String getResultInfo();
	/**
	 * 设置返回数据
	 * @param key
	 * @param value
	 * @return
	 */
	Result putParam(String key, Object value);
	/**
	 * 获取返回数据
	 * @param key
	 * @return
	 */
	Object getParam(String key);

	/**
	 * 设置状态和信息
	 * @param code
	 * @param info
	 * @return
	 */
	Result setInfo(Integer code,String info);
}
