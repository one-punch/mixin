package com.wteam.mixin.biz.service;

import java.util.List;

import com.wteam.mixin.model.vo.BusinessBannerVo;

/**
 * <p>Title:商家广告模块接口</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年7月26日
 */
public interface IBusinessBannerService {
	
	/**
	 * 商家查询所有广告信息
	 * @return
	 */
	public List<BusinessBannerVo> findAllBybusinessId(Long businessId);
	
	/**
	 * 商家查询所有广告信息
	 * @return
	 */
	public List<BusinessBannerVo> findAllByIdNActived(Long businessId, boolean actived);
	
	/**
	 * 添加广告信息
	 */
	public void add(BusinessBannerVo banner);
	
	/**
	 * 删除广告信息
	 */
	public void delete(Long id);
}
