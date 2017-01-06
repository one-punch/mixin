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
import com.wteam.mixin.biz.service.IThemeService;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.vo.BusinessThemeVo;
import com.wteam.mixin.model.vo.ThemeVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.pagination.Pagination;
import com.wteam.mixin.utils.SpringUtils;

/**
 * <p>Title:主题控制器</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月1日
 */
@RestController
@RequestMapping("/theme")
public class ThemeController {
	
	@Autowired
	private IThemeService themeService;
	
	private static final Logger LOG = LogManager.getLogger(ThemeController.class.getName());
	
	@RequestMapping(value="/add", method={RequestMethod.POST})
	public ResultMessage addTheme(@ModelAttribute("theme") @Valid ThemeVo theme,
			BindingResult results, ResultMessage result){
		SpringUtils.validate(results);
		
		themeService.addTheme(theme);
		
		return result.setSuccessInfo("添加主题成功");
	}
	
	@RequestMapping(value="/delete",method={RequestMethod.POST})
	public ResultMessage deleteTheme(@RequestParam("themeId") Long themeId, ResultMessage result){
		
		themeService.deleteTheme(themeId);
		
		return result.setSuccessInfo("删除主题成功");
	}
	
	@RequestMapping(value="/list/super",method={RequestMethod.GET})
	public ResultMessage listThemeBySuper(@RequestParam("pageNo") int pageNo, 
			@RequestParam("pageSize") int pageSize, ResultMessage result){
		
		Pagination page = themeService.listThemeBySuper(pageNo, pageSize);
		
		return result.setSuccessInfo("成功").putParam("page", page);
	}
	
	@RequestMapping(value="/edit",method={RequestMethod.POST})
	public ResultMessage editTheme(@RequestParam("theme") ThemeVo theme, ResultMessage result){
		
		themeService.updateTheme(theme);
		
		return result.setSuccessInfo("成功");
	}
	
	@RequestMapping(value="/set_default",method={RequestMethod.POST})
	public ResultMessage setDefaultTheme(@RequestParam("themeId") Long themeId, ResultMessage result){
		
		themeService.setDefaultTheme(themeId);
		
		return result.setSuccessInfo("成功");
	}
	
	@RequestMapping(value="/list/business",method={RequestMethod.GET, RequestMethod.POST})
	public ResultMessage listThemeByBusiness(@RequestParam("pageNo") int pageNo,
			@RequestParam("pageSize") int pageSize,ResultMessage result){
		
		Pagination page = themeService.listThemeByBusiness(pageNo, pageSize);
		
		return result.setSuccessInfo("成功").putParam("page", page);
	}
	
	@RequestMapping(value="/business/list",method={RequestMethod.GET})
	public ResultMessage listBusinessTheme(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user, ResultMessage result){
		
		List<BusinessThemeVo> themeLst = themeService.listBusinessTheme(user.getUserId());
		
		return result.setSuccessInfo("成功").putParam("businessThemeList", themeLst);
	}
	
	@RequestMapping(value="/buy",method={RequestMethod.POST})
	public ResultMessage purchaseTheme(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
			@RequestParam("themeId") Long themeId, ResultMessage result){
		
		String msg = themeService.purchaseTheme(themeId, user.getUserId());
		
		return result.setSuccessInfo(msg);
	}
	
	@RequestMapping(value="/business/choose",method={RequestMethod.POST})
	public ResultMessage setupTheme(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
			@RequestParam("themeId") Long themeId, ResultMessage result){
		
		themeService.setActived(themeId, user.getUserId());
		
		return result.setSuccessInfo("成功");
	}
}
































