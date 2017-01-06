package com.wteam.mixin.define;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 统一的返回信息类
 *
 * @author benko
 */
public class ResultMessage implements Result{

	/**
     * 服务类执行结果.
     */
    private Integer resultCode;

    /**
     * 返回结果信息.
     */
    private String resultInfo;

    /**
     * 返回参数，键自由指定，值由Vo或Vo的list集合的json组成
     */
    private Map<String, Object> resultParm = new HashMap<String, Object>();

	@Override
	public Integer getResultCode() {
		return resultCode;
	}

	@Override
	public String getResultInfo() {
		return resultInfo;
	}

	@Override
	public ResultMessage putParam(String key, Object value) {
		resultParm.put(key, value);
		return this;
	}

	@Override
	public Object getParam(String key) {
		return resultParm.get(key);
	}

	@Override
	public ResultMessage setInfo(Integer code, String info) {
		resultCode = code;
		resultInfo = info;
		return this;
	}
	/**
	 * 设置默认成功状态信息
	 * @param info
	 * @return
	 */
	public ResultMessage setSuccessInfo(String info) {
		return setInfo(ResultInfo.SUCCESS, info);
	}
	/**
	 * 设置默认“系统错误”状态信息
	 * @param info
	 * @return
	 */
	public ResultMessage setSystemFailureInfo(String info) {
		return setInfo(ResultInfo.SYSTEM_FAIL, info);
	}
	/**
	 * 设置默认“业务失败”状态信息
	 * @param info
	 * @return
	 */
	public ResultMessage setServiceFailureInfo(String info) {
		return setInfo(ResultInfo.SERVICE_FAIL, info);
	}

    /**
     * 使用ResultInfo配置的信息
     * @param code
     * @return
     */
    public ResultMessage setInfo(Integer code) {
		return setInfo(code, ResultInfo.getInfo(code));
	}

    /**json转换使用*/
    public Map<String, Object> getResultParm() {
		return resultParm;
	}

	@Override
	@JSONField(name="isSuccess")
	public boolean isSuccess() {
		return resultCode != null && resultCode == ResultInfo.SUCCESS;
	}


}
