package com.wteam.mixin.biz.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.alibaba.fastjson.JSONObject;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.vo.CustomerOrderVo;
import com.wteam.mixin.model.wexin.XPayConfig;

/**
 * 微信业务接口：自动回复、公众号授权、用户授权、页面jsapi注入等
 * <p>Title:</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月16日
 */
public interface IWechatService {
	/**
	 * 公众号授权成功回调
	 * @param authCode
	 */
	public void bindSuccess(Long businessId, String authCode);
	
	/**
	 * 授权登录成功回调
	 */
	public String authLoginSuccess(String appId, String code);
	/**
	 * 公众号授权事件处理
	 * @param map
	 */
	public void auth(Map<String, String> map);
	
	/**
	 * 公众号取消授权事件处理
	 * @param map
	 */
	public void unAuth(Map<String, String> map);
	
	/**
	 * 公众号重新授权事件处理
	 * @param map
	 */
	public void reAuth(Map<String, String> map);
	
	/**
	 * 微信平台推送的ticket
	 * @param map
	 */
	public void ticketFresh(Map<String, String> map);
	
	/**
	 * 公众号消息事件处理
	 * @param req
	 * @return
	 */
	public String handleRequest(HttpServletRequest req);
	
	/**
	 * 微信浏览器页面调用jsapi注入处理
	 */
	public JSONObject setupJsapiConfig(String appId, String beConfigedUrl);
	
	/**
	 * 对公众号菜单跳转页面请求进行匹配
	 * @param req
	 * @param businessId
	 * @return
	 */
	public String requestMapping(HttpServletRequest req, Long businessId);
	
	/**
	 * js微信支付，返回微信参数
	 * @param orderVo
	 * @return
	 */
	public JSONObject pay(CustomerOrderVo orderVo, HttpServletRequest req);
	
	/**
	 * 微信支付退款，如果退款失败抛出ServiceException
	 * @param orderVo
	 */
	public boolean payRefund(CustomerOrderVo orderVo);
	
	/**
	 * 获取微信支付配置
	 * @param businessId
	 * @param isGetApliclient_cert
	 * @return
	 */
	XPayConfig getPayConfig(Long businessId, boolean isGetApliclient_cert);
}
