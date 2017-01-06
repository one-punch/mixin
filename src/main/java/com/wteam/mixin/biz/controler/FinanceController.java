package com.wteam.mixin.biz.controler;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.service.IFinanceService;
import com.wteam.mixin.biz.service.IProxyBusinessService;
import com.wteam.mixin.biz.service.IUserService;
import com.wteam.mixin.define.ResultInfo;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.query.BusinessBlanceRecordQuery;
import com.wteam.mixin.model.vo.BusinessBalanceRecordVo;
import com.wteam.mixin.model.vo.FinanceVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.model.vo.WithdrawVo;
import com.wteam.mixin.pagination.Pagination;
import com.wteam.mixin.utils.ExcelExportDataFactory;
import com.wteam.mixin.utils.ExcelUtils;
import com.wteam.mixin.utils.SpringUtils;
import com.wteam.mixin.utils.ExcelUtils.ExcelExportData;

@Controller
public class FinanceController {
    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(FinanceController.class.getName());
    
    
	@Autowired
	private IFinanceService financeService;
    @Autowired
    IProxyBusinessService proxyBusinessService;
    @Autowired
    IUserService userService;
	
	
	@RequestMapping("/balance/platform")
	@ResponseBody
	public ResultMessage getPlatformFinanceRecord(ResultMessage result){
		
		FinanceVo finance = financeService.getPlatformFinanceRecord();
		
		return result.setSuccessInfo("成功").putParam("platform", finance);
	} 
	
	
	@RequestMapping("/balance/list/super")
    @ResponseBody
	public ResultMessage listAllRecord(@RequestParam("pageNo") Integer pageNo,
			@RequestParam("pageSize") Integer pageSize, ResultMessage result){
		
		Pagination page = financeService.listAllrecord(pageNo, pageSize);
		
		return result.setSuccessInfo("成功").putParam("page", page);
	} 
	
    @RequestMapping("/balance/business/all/refresh")
    @ResponseBody
    public ResultMessage refrashAllBusinessFinance(ResultMessage result){
        
        financeService.refreshAllBusinessFinance();
        
        return result.setSuccessInfo("成功");
    }
    
    @RequestMapping("/balance/list/business/all/refresh")
    @ResponseBody
    public ResultMessage refreshAllBusinessRecord(ResultMessage result){
        financeService.refreshAllBusinessRecord();
        return result.setSuccessInfo("成功");
    }
    
    @RequestMapping("/balance/list/business/refresh")
    @ResponseBody
    public ResultMessage refreshBusinessRecord(
                                               @RequestParam("businessId") Long businessId,
                                               ResultMessage result){
        financeService.refreshBusinessRecord(businessId);
        return result.setSuccessInfo("成功");
    }
    
	
	@RequestMapping("/withdraw/list/super")
    @ResponseBody
	public ResultMessage listAllWithdraw(@RequestParam("pageNo") Integer pageNo,
			@RequestParam("pageSize") Integer pageSize, ResultMessage result){
		
		Pagination page = financeService.listAllWithdraw(pageNo, pageSize);
		
		return result.setSuccessInfo("成功").putParam("page", page);
	} 
	
	@RequestMapping("/withdraw/pass")
    @ResponseBody
	public ResultMessage withdrawAuditSuccess(@RequestParam("withdrawId") Long withdrawId,
			ResultMessage result){
		
		financeService.withdrawAuditSuccess(withdrawId);
		
		return result.setSuccessInfo("成功");
	} 
	
	@RequestMapping("/withdraw/reject")
    @ResponseBody
	public ResultMessage withdrawAuditFail(@RequestParam("withdrawId") Long withdrawId,
			@RequestParam(name="failInfo", required=false) String failInfo, ResultMessage result){
		
		financeService.withdrawAuditFail(withdrawId, failInfo);
		
		return result.setSuccessInfo("成功");
	} 
	
	@RequestMapping("/balance/super/business")
    @ResponseBody
	public ResultMessage listBusinessFinanceBySuper(@RequestParam("businessId") Long businessId,
			ResultMessage result){
		
		FinanceVo finance = financeService.getBusinessFinanceRecordByBusinessId(businessId);
		
		return result.setSuccessInfo("成功").putParam("finance", finance);
	}
	

    @RequestMapping("/balance/super/business/add_or_subtract")
    @ResponseBody
    public ResultMessage addOrSubtractBusinessBalanceBySuper(
            @ModelAttribute("record") @Valid BusinessBalanceRecordVo recordVo,BindingResult result1,
            ResultMessage result){
        SpringUtils.validate(result1);
        FinanceVo finance = financeService.addOrSubtractBusinessBalanceBySuper(recordVo);
        
        return result.setSuccessInfo("成功").putParam("finance", finance);
    } 
    @RequestMapping("/balance/super/business/delete")
    @ResponseBody
    public ResultMessage deleteBusinessBalanceRecord(
            @RequestParam("record") BusinessBalanceRecordVo recordVo,
            ResultMessage result){
        
        financeService.deleteBusinessBalanceRecord(recordVo.getId());;
        
        return result.setSuccessInfo("成功");
    } 
	
	@RequestMapping("/balance/list/super/business")
    @ResponseBody
	public ResultMessage listBusinessRecordBySuper(
	        @RequestParam("query") BusinessBlanceRecordQuery query,
			@RequestParam("pageNo") Integer pageNo,@RequestParam("pageSize") Integer pageSize,
			ResultMessage result){
		query.setIsDelete(false);
		Pagination page = financeService.listRecordByBusinessId(query, pageNo, pageSize);
		
		return result.setSuccessInfo("成功").putParam("page", page);
	} 

    @RequestMapping("/balance/list/super/business/download")
    public void listBusinessRecordDownloadBySuper(
            @RequestParam("query") BusinessBlanceRecordQuery query,
            HttpServletResponse response,
            final ResultMessage result){
        
        SpringUtils.outputHttpHandle(result, response, () -> {
            
            List<BusinessBalanceRecordVo> list = financeService.listRecordByBusinessId(query);
            // 构建excel数据
            ExcelExportData data = ExcelExportDataFactory.businessRecord(list, query.getBusinessId());
            // 输出到http请求中
            ExcelUtils.exportToHttpResponse(response, "商家财务报表.xls", data);
            
        });
    } 
	
	@RequestMapping("/balance/business")
    @ResponseBody
	public ResultMessage listBusinessFinanceByBusiness(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
			ResultMessage result){
		
		FinanceVo finance = financeService.getBusinessFinanceRecordByBusinessId(user.getUserId());
		
		return result.setSuccessInfo("成功").putParam("business", finance);
	} 

    @RequestMapping("/balance/list/business/download")
    public void listBusinessBalanceRecordByBusiness(
            @RequestParam("query") BusinessBlanceRecordQuery query,
            HttpServletResponse response,
            @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
            ResultMessage result){
        
        query.setBusinessId(user.getUserId());
        query.setIsDelete(false);
        result.setSuccessInfo("");
        listBusinessRecordDownloadBySuper(query, response, result);
    } 
    
	@RequestMapping("/balance/list/business")
    @ResponseBody
	public ResultMessage listBusinessBalanceRecordByBusiness(
            @RequestParam("query") BusinessBlanceRecordQuery query,
	        @RequestParam("pageNo") Integer pageNo,
			@RequestParam("pageSize") Integer pageSize,
			@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
			ResultMessage result){
		
	    query.setBusinessId(user.getUserId());
	    query.setIsDelete(false);
		Pagination page = financeService.listRecordByBusinessId(query, pageNo, pageSize);
		
		return result.setSuccessInfo("成功").putParam("page", page);
	} 
	
	@RequestMapping("/withdraw/list/business")
    @ResponseBody
	public ResultMessage listWithdrawByBusiness(@RequestParam("pageNo") Integer pageNo,
			@RequestParam("pageSize") Integer pageSize,@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
			ResultMessage result){
		
		Pagination page = financeService.listWithdrawByBusinessId(user.getUserId(), pageNo, pageSize);
		
		return result.setSuccessInfo("成功").putParam("page", page);
	} 
	
	@RequestMapping("/withdraw/submit")
    @ResponseBody
	public ResultMessage withdrawSubmit(
	        @ModelAttribute("withdraw") @Valid WithdrawVo withdraw ,
	        @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
	        BindingResult bindResult, ResultMessage result){
		
		SpringUtils.validate(bindResult);
		withdraw.setBusinessId(user.getUserId());
		financeService.newWithdraw(withdraw);
		return result.setSuccessInfo("成功");
	} 
	/**
	 * 下载代理商家记录
	 * @param query
	 * @param response
	 * @param user
	 * @param result
	 */
    @RequestMapping("/balance/list/proxybusiness/download")
    public void listProxyBusinessBalanceRecordByBusiness(
            @RequestParam("query") BusinessBlanceRecordQuery query,
            HttpServletResponse response,
            @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
            ResultMessage result){
        
        if (query.getBusinessId() == null) {
            result.setInfo(ResultInfo.MISSING_REQUEST_PARAM_FAIL, "请求缺少参数 businessId");
        }
        if (!proxyBusinessService.isProxyAncestry(query.getBusinessId(), user.getUserId())) {
            result.setServiceFailureInfo("此商家不是你的代理商");
        }
        query.setIsDelete(false);
        result.setSuccessInfo("");
        listBusinessRecordDownloadBySuper(query, response, result);
    } 
	
    /**
     * 查询代理商家记录
     * @param query
     * @param pageNo
     * @param pageSize
     * @param user
     * @param result
     * @return
     */
    @RequestMapping("/balance/list/proxybusiness")
    @ResponseBody
    public ResultMessage listProxyBusinessBalanceRecordByBusiness(
                                                             @RequestParam("query") BusinessBlanceRecordQuery query,
                                                             @RequestParam("pageNo") Integer pageNo,
                                                             @RequestParam("pageSize") Integer pageSize,
                                                             @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                                                             ResultMessage result) {
        if (query.getBusinessId() == null) {
            return result.setInfo(ResultInfo.MISSING_REQUEST_PARAM_FAIL, "请求缺少参数 businessId");
        }
        if (!proxyBusinessService.isProxyAncestry(query.getBusinessId(), user.getUserId())) {
            return result.setServiceFailureInfo("此商家不是你的代理商");
        }
        query.setIsDelete(false);
        Pagination page = financeService.listRecordByBusinessId(query, pageNo, pageSize);

        return result.setSuccessInfo("成功").putParam("page", page);
    }
    
    /**
     * 获取代理商家账务信息
     * @param businessId
     * @param result
     * @return
     */
    @RequestMapping("/balance/proxybusiness")
    @ResponseBody
    public ResultMessage listProxyBusinessFinance(
            @RequestParam("businessId") Long businessId,
            @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
            ResultMessage result){
        if (!proxyBusinessService.isProxyAncestry(businessId, user.getUserId())) {
            return result.setServiceFailureInfo("此商家不是你的代理商");
        }
        FinanceVo finance = financeService.getBusinessFinanceRecordByBusinessId(businessId);
        
        return result.setSuccessInfo("成功").putParam("finance", finance);
    }

    @RequestMapping(value = "/balance/proxybusiness/add", method = RequestMethod.POST)
    @ResponseBody
    public ResultMessage addOrSubtractProxyBusinessBalance(
            @ModelAttribute("record") @Valid BusinessBalanceRecordVo recordVo,BindingResult result1,
            @ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
            ResultMessage result){
        SpringUtils.validate(result1);
        if (!proxyBusinessService.isProxyAncestry(recordVo.getBusinessId(), user.getUserId())) {
            return result.setServiceFailureInfo("此商家不是你的代理商");
        }
        FinanceVo finance = financeService.addProxyBusinessBalance(recordVo, user.getUserId());
        
        return result.setSuccessInfo("成功").putParam("finance", finance);
    } 
    
}


