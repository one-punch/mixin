package com.wteam.mixin.biz.service;

import java.util.List;

import com.wteam.mixin.model.query.TrafficGroupQuery;
import com.wteam.mixin.model.query.TrafficPlanQuery;
import com.wteam.mixin.model.vo.GroupNPlanVo;
import com.wteam.mixin.model.vo.TrafficGroupQueryVo;
import com.wteam.mixin.model.vo.TrafficGroupVo;
import com.wteam.mixin.model.vo.TrafficPlanQueryVo;
import com.wteam.mixin.model.vo.TrafficPlanVo;
import com.wteam.mixin.pagination.Pagination;

/**
 * <p>Title:流量业务接口</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月11日
 */
public interface ITrafficService {
	
	/**
	 * 添加流量分组对象
	 * @param group 
	 */
	public void addTrafficGroup(TrafficGroupVo group);
	
	/**
	 * 更新分组信息
	 * @param group
	 */
	public void updateTrafficGroup(TrafficGroupVo group);
	
	/**
	 * 管理员根据运营商、省份查询流量分组
	 * @param query 查询条件
	 * @return
	 */
	public List<TrafficGroupVo> listTrafficGroupBySuper(TrafficGroupQueryVo query,Boolean hasInfo);
	
	/**
	 * 管理员根据运营商、省份分页查询流量分组
	 * @param query
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination listTrafficGroupPageBySuper(TrafficGroupQueryVo query, Boolean hasInfo, Integer pageNo, Integer pageSize);
	/**
	 * 商家根据运营商、省份查询流量分组
	 * @param query 查询条件
	 * @return
	 */
	public List<TrafficGroupVo> listTrafficGroupByBusiness(TrafficGroupQueryVo query, Boolean hasInfo);
	
	/**
	 * 商家根据运营商、省份分页查询流量分组
	 * @param query
	 * @param pageNo
	 * @param pageSize
	 * @return
	 */
	public Pagination listTrafficGroupPageByBusiness(TrafficGroupQueryVo query, Boolean hasInfo, Integer pageNo, Integer pageSize);
	/**
	 * 为流量分组添加流量套餐
	 * @param trafficpgroupId 流量分组
	 * @param trafficplanIds 流量套餐列表
	 */
	public void addTrafficPlanIntoGroup(Long trafficpgroupId, Long[] trafficplanIds);
	
	/**
	 * 从流量分组移除流量套餐
	 * @param trafficpgroupId 流量分组
	 * @param trafficplanIds 流量套餐列表
	 */
	public void removeTrafficPlanFromGroup(Long trafficpgroupId, Long[] trafficplanIds);

    /**
     * 管理员添加流量套餐
     * @param plan
     */
    public void addTrafficPlanBySuper(TrafficPlanVo plan);
    
	/**
	 * 管理员修改流量套餐
	 * @param plan
	 */
	public void updateTrafficPlanBySuper(TrafficPlanVo plan);
	
	/**
	 * 商家修改流量套餐
	 * @param plan
	 * @param businessId
	 */
	public void updateTrafficPlanByBusiness(TrafficPlanVo plan, Long businessId);
	
	
	
	/**
	 * 查询流量分组下的流量套餐
	 * @param groupId
	 * @return
	 */
	public List<TrafficPlanVo> listTrafficPlanByGroup(Long groupId);
	
	/**
	 * 管理员根据运营商、省份、分组，分页查询流量套餐
	 * @param query
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public Pagination listTrafficPlanBySuper(TrafficPlanQueryVo query, Integer currentPage, Integer pageSize);
    /**
     * 管理员根据运营商、省份、分组，分页查询流量套餐
     * @param query
     * @param currentPage
     * @param pageSize
     * @return
     */
    public Pagination listTrafficPlanBySuper(TrafficPlanQuery query,TrafficGroupQuery groupQuery, Integer currentPage, Integer pageSize);
	
	/**
	 * 商家根据运营商、省份、分组，分页查询流量套餐
	 * @param query
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	public Pagination listTrafficPlanByBusiness(TrafficPlanQueryVo query, Integer currentPage, Integer pageSize, Long businessId);
	
	/**
	 * 商家查询分组和相应的流量套餐情况
	 * @return
	 */
	public List<GroupNPlanVo> listGroupNPlanByBusiness(Integer provider, String province, Long businessId);
	
	/**
	 * 顾客查询分组和相应的流量套餐情况
	 * @return
	 */
	public List<GroupNPlanVo> listGroupNPlanByCustomer(Integer provider, String province, Long businessId);
	
    /**
     * 添加接口API流量
     * @param group 
     */
    public void addApiRechargeTrafficplan(Long planId);

    /**
     *  取消接口API流量
     * @param group 
     */
    public void deleteApiRechargeTrafficplan(Long planId);
    
    
    /**
     * 根据运营商、省份、分组，分页查询流量套餐
     * @return
     */
    public Pagination listApiRechargeTrafficplanlistBySuper(TrafficPlanQueryVo query, Integer currentPage, Integer pageSize);
    

    /**
     * 商家根据运营商、省份、分组，分页查询流量套餐
     * @return
     */
    public Pagination listApiRechargeTrafficplanlistByBusiness(TrafficPlanQueryVo query, Integer currentPage, Integer pageSize, Long businessId);
    
}
