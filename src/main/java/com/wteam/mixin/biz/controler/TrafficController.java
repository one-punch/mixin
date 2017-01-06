package com.wteam.mixin.biz.controler;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.service.IProxyBusinessService;
import com.wteam.mixin.biz.service.IProxyTrafficService;
import com.wteam.mixin.biz.service.ITrafficService;
import com.wteam.mixin.biz.service.IUserService;
import com.wteam.mixin.constant.RoleType;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.query.TrafficGroupQuery;
import com.wteam.mixin.model.query.TrafficPlanQuery;
import com.wteam.mixin.model.vo.GroupNPlanVo;
import com.wteam.mixin.model.vo.ProxyBusinessTrafficPlanVo;
import com.wteam.mixin.model.vo.TrafficGroupQueryVo;
import com.wteam.mixin.model.vo.TrafficGroupVo;
import com.wteam.mixin.model.vo.TrafficPlanQueryVo;
import com.wteam.mixin.model.vo.TrafficPlanVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.pagination.Pagination;
import com.wteam.mixin.utils.SpringUtils;

@RestController
public class TrafficController {
	
	@Autowired
	private ITrafficService trafficService;
	@Autowired
	IUserService userService;
	@Autowired
	IProxyTrafficService proxyTrafficService;
	@Autowired
	IProxyBusinessService proxyBusinessService;
	
    private static final Logger LOG = LogManager.getLogger(TrafficController.class.getName());

	@RequestMapping("/trafficgroup/list")
	public ResultMessage listTrafficGroupBySuper(@RequestParam(name="provider", required=false)Integer provider,
			@RequestParam(name="province", required=false) String  province, 
			@RequestParam(name="hasInfo", required=false) Boolean hasInfo,
			@RequestParam(name="pageNo", required=false) Integer  pageNo,
			@RequestParam(name="pageSize", required=false) Integer  pageSize,
			ResultMessage result){
		LOG.debug("hasInfo:{}",hasInfo);
		if(pageNo!=null && pageSize != null){
			Pagination page = trafficService.listTrafficGroupPageBySuper(new TrafficGroupQueryVo(provider, province), hasInfo, pageNo, pageSize);
			return result.setSuccessInfo("查询成功").putParam("page", page);
		}else{
			List<TrafficGroupVo> groupLst = trafficService.listTrafficGroupBySuper(new TrafficGroupQueryVo(provider, province), hasInfo);
			return result.setSuccessInfo("查询成功").putParam("trafficgroupList", groupLst);
		}
	} 
	
	@RequestMapping("/trafficgroup/list/business")
	public ResultMessage listTrafficGroupByBusiness(@RequestParam(name="provider", required=false)Integer provider,
			@RequestParam(name="province", required=false) String  province, 
			@RequestParam(name="hasInfo", required=false) Boolean hasInfo,
			@RequestParam(name="pageNo", required=false) Integer  pageNo,
			@RequestParam(name="pageSize", required=false) Integer  pageSize,
			ResultMessage result){
		
		if(pageNo!=null && pageSize != null){
			Pagination page = trafficService.listTrafficGroupPageByBusiness(new TrafficGroupQueryVo(provider, province), hasInfo, pageNo, pageSize);
			return result.setSuccessInfo("查询成功").putParam("page", page);
		}else{
			List<TrafficGroupVo> groupLst = trafficService.listTrafficGroupByBusiness(new TrafficGroupQueryVo(provider, province), hasInfo);
			return result.setSuccessInfo("查询成功").putParam("trafficgroupList", groupLst);
		}
	} 
	
	@RequestMapping("/trafficgroup/add")
	public ResultMessage addTrafficGroup(@ModelAttribute("trafficgroup") @Valid TrafficGroupVo group,
			BindingResult bindResult, ResultMessage result){
		
		SpringUtils.validate(bindResult);
		trafficService.addTrafficGroup(group);
		
		return result.setSuccessInfo("添加流量分组成功");
	} 
	
	@RequestMapping("/trafficgroup/edit")
	public ResultMessage editTrafficGroup(@RequestParam("trafficgroup")TrafficGroupVo group,
			ResultMessage result){
		
		trafficService.updateTrafficGroup(group);
		
		return result.setSuccessInfo("修改流量分组成功");
	} 

    @RequestMapping("/trafficplan/add")
    public ResultMessage addTrafficPlan(@ModelAttribute("trafficplan") @Valid TrafficPlanVo plan,
            BindingResult bindResult, ResultMessage result){
        
        SpringUtils.validate(bindResult);
        trafficService.addTrafficPlanBySuper(plan);
        
        return result.setSuccessInfo("添加流量套餐成功");
    }
    
	@RequestMapping("/trafficgroup/trafficplans/add")
	public ResultMessage addTrafficPlanIntoGroup(@RequestParam(name="trafficgroupId", required=true)Long trafficgroupId,
			@RequestParam(name="trafficplanIds", required=true)Long[] trafficplanIds,ResultMessage result){

		trafficService.addTrafficPlanIntoGroup(trafficgroupId, trafficplanIds);
		
		return result.setSuccessInfo("为流量分组添加流量套餐成功");
	} 
	
	@RequestMapping("/trafficgroup/trafficplans/delete")
	public ResultMessage removeTrafficPlanFromGroup(@RequestParam(name="trafficgroupId", required=true)Long trafficgroupId,
			@RequestParam(name="trafficplanIds", required=true)Long[] trafficplanIds,ResultMessage result){

		trafficService.removeTrafficPlanFromGroup(trafficgroupId, trafficplanIds);
		
		return result.setSuccessInfo("为流量分组移除流量套餐成功");
	} 
	
	@RequestMapping("/trafficgroup/trafficplans")
	public ResultMessage listTrafficPlanByGroup(@RequestParam(name="trafficgroupId", required=true)Long groupId
			,ResultMessage result){

	    List<TrafficPlanVo> list = trafficService.listTrafficPlanByGroup(groupId);
		
		return result.setSuccessInfo("查询成功").putParam("trafficplanList", list);
	} 
	
//	@RequestMapping("/trafficplan/list")
	public ResultMessage listTrafficPlanPageBySuper(@RequestParam(name="provider", required=false) Integer provider,
			@RequestParam(name="province", required=false) String province,
			@RequestParam(name="trafficgroupId", required=false) Long trafficgroupId,
			@RequestParam("pageNo") Integer pageNo,
			@RequestParam("pageSize") Integer pageSize,
			ResultMessage result){
	    TrafficPlanQuery planQuery = new TrafficPlanQuery();
	    TrafficGroupQuery groupQuery = new TrafficGroupQuery();
	    
	    planQuery.setProvider(provider);
	    planQuery.setTrafficGroupId(trafficgroupId);
	    groupQuery.setProvider(provider);
	    groupQuery.setProvince(province);

		//Pagination page = trafficService.listTrafficPlanBySuper(new TrafficPlanQueryVo(provider, province, trafficgroupId), pageNo, pageSize);
		Pagination page = trafficService.listTrafficPlanBySuper(planQuery, groupQuery, pageNo, pageSize);
		
		return result.setSuccessInfo("查询流量套餐成功").putParam("page", page);
	} 

    @RequestMapping("/trafficplan/list")
    public ResultMessage listTrafficPlanPageBySuper(
            @RequestParam(name="planQuery") TrafficPlanQuery planQuery,
            @RequestParam(name="groupQuery") TrafficGroupQuery groupQuery,
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize,
            ResultMessage result){

        //Pagination page = trafficService.listTrafficPlanBySuper(new TrafficPlanQueryVo(provider, province, trafficgroupId), pageNo, pageSize);
        Pagination page = trafficService.listTrafficPlanBySuper(planQuery, groupQuery, pageNo, pageSize);
        
        return result.setSuccessInfo("查询流量套餐成功").putParam("page", page);
    } 
	
	
	
	@RequestMapping("/trafficplan/edit")
	public ResultMessage editTrafficPlan(@RequestParam("trafficplan") TrafficPlanVo trafficplan
			,ResultMessage result){

		trafficService.updateTrafficPlanBySuper(trafficplan);
		
		return result.setSuccessInfo("修改流量套餐成功");
	} 
	
	@RequestMapping("/trafficplan/list/business")
	public ResultMessage listTrafficPlanByBusiness(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
			@RequestParam(name="provider", required=false) Integer provider,
			@RequestParam(name="province", required=false) String province,
			@RequestParam(name="trafficgroupId", required=false) Long trafficgroupId,
			@RequestParam("pageNo") Integer pageNo,
			@RequestParam("pageSize") Integer pageSize,
			TrafficPlanQuery planQuery, TrafficGroupQuery groupQuery,
			ResultMessage result){
	    Pagination page;
	    
	    if (userService.isRole(user.getUserId(), RoleType.bussiness)) {
	        page = trafficService.listTrafficPlanByBusiness(new TrafficPlanQueryVo(provider, province, trafficgroupId), pageNo, pageSize, user.getUserId());
        }
        else {
            planQuery.setTrafficGroupId(trafficgroupId);
            groupQuery.setProvider(provider);
            groupQuery.setProvince(province);
            page = proxyTrafficService.listTrafficPlan(planQuery, groupQuery, user.getUserId(), pageNo, pageSize);
        }
		
		return result.setSuccessInfo("查询流量套餐成功").putParam("page", page);
	}
	
	@RequestMapping(value="/business/trafficplan/edit", method={RequestMethod.POST})
	public ResultMessage editTrafficPlanByBusiness(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
			@RequestParam("trafficplan") TrafficPlanVo plan,
			ResultMessage result){

		trafficService.updateTrafficPlanByBusiness(plan, user.getUserId());
		
		return result.setSuccessInfo("修改流量套餐成功");
	} 
	
	@RequestMapping("/traffic/list/business")
	public ResultMessage listPlanNGroupByBusiness(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
			@RequestParam(name="provider", required=false) Integer provider,
			@RequestParam(name="province", required=false) String province,
            TrafficPlanQuery planQuery, TrafficGroupQuery groupQuery,
			ResultMessage result){

	    List<GroupNPlanVo> gnpLst;
        if (userService.isRole(user.getUserId(), RoleType.bussiness)) {
            gnpLst = trafficService.listGroupNPlanByBusiness(provider, province, user.getUserId());
        }
        else {
            groupQuery.setProvider(provider);
            groupQuery.setProvince(province);
            gnpLst = proxyTrafficService.listGroupNPlan(planQuery, groupQuery, user.getUserId());
        }
		
		return result.setSuccessInfo("查询流量分组及流量套餐成功").putParam("trafficgroupList", gnpLst);
	} 
	
	@RequestMapping("/traffic/list/customer")
	public ResultMessage listPlanNGroupByCustomer(
	        @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
	        @RequestParam(name="businessId") Long businessId,
			@RequestParam(name="provider", required=false) Integer provider,
			@RequestParam(name="province", required=false) String province,
            TrafficPlanQuery planQuery, TrafficGroupQuery groupQuery,
			ResultMessage result){
	    LOG.debug("provider ->{}, province->{}, businessId->{}",provider, province, businessId);
        List<GroupNPlanVo> gnpLst;
        if (userService.isRole(businessId, RoleType.bussiness)) {
            gnpLst = trafficService.listGroupNPlanByCustomer(provider, province, businessId);
        }
        else {
            groupQuery.setProvider(provider);
            groupQuery.setProvince(province);
            gnpLst = proxyTrafficService.listGroupNPlanByCustomer(planQuery, groupQuery,businessId);
        }
		
		return result.setSuccessInfo("查询流量分组及流量套餐成功").putParam("trafficgroupList", gnpLst);
	} 
	

    @RequestMapping("/trafficplan/api/list/super")
    public ResultMessage listTrafficPlanBySuper(
            @RequestParam(name="provider", required=false) Integer provider,
            @RequestParam(name="province", required=false) String province,
            @RequestParam(name="trafficgroupId", required=false) Long trafficgroupId,
            @RequestParam(name="isApiRecharge") boolean isApiRecharge,
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize,
            ResultMessage result){

        Pagination page = trafficService.listApiRechargeTrafficplanlistBySuper(new TrafficPlanQueryVo(provider, province, trafficgroupId, isApiRecharge), pageNo, pageSize);
        
        return result.setSuccessInfo("查询流量套餐成功").putParam("page", page);
    }
    
    @RequestMapping("/trafficplan/api/list/business")
    public ResultMessage listApiTrafficPlanByBusiness(
            @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
            @RequestParam(name="provider", required=false) Integer provider,
            @RequestParam(name="province", required=false) String province,
            @RequestParam(name="trafficgroupId", required=false) Long trafficgroupId,
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize,
            TrafficPlanQuery planQuery, TrafficGroupQuery groupQuery,
            ResultMessage result){
        Pagination page;
        
        if (userService.isRole(user.getUserId(), RoleType.bussiness)) {
            page = trafficService.listApiRechargeTrafficplanlistByBusiness(new TrafficPlanQueryVo(provider, province, trafficgroupId), pageNo, pageSize, user.getUserId());
        }
        else {
            planQuery.setTrafficGroupId(trafficgroupId);
            planQuery.setIsApiRecharge(true);
            groupQuery.setProvider(provider);
            groupQuery.setProvince(province);
            page = proxyTrafficService.listTrafficPlan(planQuery, groupQuery, user.getUserId(), pageNo, pageSize);
        }
        
        return result.setSuccessInfo("查询流量套餐成功").putParam("page", page);
    } 
    
    
    @RequestMapping("/trafficplan/api/add")
    public ResultMessage addApiTrafficplan(@RequestParam("planId")Long planId,
            ResultMessage result){
        
        trafficService.addApiRechargeTrafficplan(planId);
        
        return result.setSuccessInfo("添加接口套餐成功");
    } 
    

    @RequestMapping("/trafficplan/api/delete")
    public ResultMessage deleteApiTrafficplan(@RequestParam("planId")Long planId,
            ResultMessage result){
        
        trafficService.deleteApiRechargeTrafficplan(planId);
        
        return result.setSuccessInfo("删除接口套餐成功");
    }

    /**
     * 商家获取代理商家的套餐
     * @param provider
     * @param province
     * @param trafficgroupId
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping("/trafficplan/proxy/list")
    public ResultMessage listProxyTrafficPlanByBusiness(
            @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
            @RequestParam(name="provider", required=false) Integer provider,
            @RequestParam(name="province", required=false) String province,
            @RequestParam(name="trafficgroupId", required=false) Long trafficgroupId,
            @RequestParam("proxyBusinessId") Long proxyBusinessId,
            @RequestParam("pageNo") Integer pageNo,
            @RequestParam("pageSize") Integer pageSize,
            TrafficPlanQuery planQuery, TrafficGroupQuery groupQuery,
            ResultMessage result){
        Pagination page;
        if (!proxyBusinessService.isProxyAncestry(proxyBusinessId, user.getUserId())) {
            return result.setServiceFailureInfo("此商家不是你的代理");
        }
        planQuery.setTrafficGroupId(trafficgroupId);
        groupQuery.setProvider(provider);
        groupQuery.setProvince(province);
        page = proxyTrafficService.listTrafficPlan(planQuery, groupQuery, proxyBusinessId, pageNo,pageSize);

        return result.setSuccessInfo("查询流量套餐成功").putParam("page", page);
    }
    
    @RequestMapping("/trafficplan/proxy/add")
    public ResultMessage addProxyTrafficplan(
            @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
            @ModelAttribute("proxyPlan") @Valid ProxyBusinessTrafficPlanVo proxyPlanVo, BindingResult result1,
            ResultMessage result){
        SpringUtils.validate(result1);
        
        if (!proxyBusinessService.isProxyAncestry(proxyPlanVo.getBusinessId(), user.getUserId())) {
            return result.setServiceFailureInfo("此商家不是你的代理");
        }
        proxyTrafficService.add(proxyPlanVo);
        return result.setSuccessInfo("添加套餐成功");
    } 

    @RequestMapping("/trafficplan/proxy/delete")
    public ResultMessage deleteProxyTrafficplan(
            @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
            @RequestParam("proxyPlan")ProxyBusinessTrafficPlanVo proxyPlanVo,
            ResultMessage result){
        if (!proxyBusinessService.isProxyAncestry(proxyPlanVo.getBusinessId(), user.getUserId())) {
            return result.setServiceFailureInfo("此商家不是你的代理");
        }
        
        proxyTrafficService.delete(proxyPlanVo);
        return result.setSuccessInfo("删除代理套餐成功");
    }
    @RequestMapping("/trafficplan/proxy/edit")
    public ResultMessage editProxyTrafficplan(
            @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
            @RequestParam("proxyPlan")ProxyBusinessTrafficPlanVo proxyPlanVo,
            ResultMessage result){

        if (!proxyBusinessService.isProxyAncestry(proxyPlanVo.getBusinessId(), user.getUserId())) {
            return result.setServiceFailureInfo("此商家不是你的代理");
        }
        proxyTrafficService.edit(proxyPlanVo);
        return result.setSuccessInfo("编辑代理套餐成功");
    }
}












