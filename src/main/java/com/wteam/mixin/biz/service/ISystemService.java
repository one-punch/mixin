package com.wteam.mixin.biz.service;

import java.util.List;
import java.util.Map;

import com.wteam.mixin.constant.DConfig;
import com.wteam.mixin.model.po.DConfigPo;
import com.wteam.mixin.model.vo.BusinessMenuVo;
import com.wteam.mixin.model.vo.DConfigVo;
import com.wteam.mixin.model.vo.DocumentVo;

/**
 * <p>Title:其他业务接口</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月15日
 */
public interface ISystemService {
	
	/**
	 * 获取平台动态配置信息
	 * @return
	 */
	public Map<String, String> getDConfig();
	
	/**
	 * 修改平台配置信息
	 * @param config
	 */
	public void updateDConfig(Map<String, String> configs);
	
	/**
	 * 获取商家后台菜单列表
	 * @return
	 */
	public List<BusinessMenuVo> listBusinessMenu();
	
	/**
	 * 修改商家后台菜单信息
	 * @param menu
	 */
	public void updateBusinessMenu(List<BusinessMenuVo> menus);
	
	/**
	 * 获取文档名称列表
	 */
	public List<DocumentVo> listDocName();
	
	/**
	 * 根据文档名称获取文档信息
	 * @param name
	 */
	public DocumentVo getDocByName(String name);
	
	/**
	 * 修改文档信息
	 * @param doc
	 */
	public void updateDoc(DocumentVo doc);
}

















