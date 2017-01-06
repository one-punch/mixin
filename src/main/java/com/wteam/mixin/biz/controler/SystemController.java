package com.wteam.mixin.biz.controler;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.wteam.mixin.biz.service.ISystemService;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.vo.BusinessMenuVo;
import com.wteam.mixin.model.vo.DocumentVo;
import com.wteam.mixin.shiro.CustomPermissionCheckFilter;
import com.wteam.mixin.utils.SpringUtils;

@RestController
@RequestMapping("/system")
public class SystemController {
	
	@Autowired
	private ISystemService systemService;

    @Autowired
	CustomPermissionCheckFilter customPermissionCheckFilter;
    
    @RequestMapping("/permission/update")
    public ResultMessage updataPermission(
            ResultMessage result){
        customPermissionCheckFilter.updateUrlAllCache();
        return result.setSuccessInfo("成功");
    }
    
	@RequestMapping("/dconfig")
	public ResultMessage listDConfig(ResultMessage result){
		
		Map<String, String> dConfig = systemService.getDConfig();
		
		return result.setSuccessInfo("成功").putParam("dconfig", dConfig);
	}
	
	@RequestMapping("/dconfig/edit")
	public ResultMessage editDConfig(@RequestParam("configs") String config,
			ResultMessage result){
		systemService.updateDConfig(JSON.parseObject(config, new TypeReference<Map<String, String>>(){}));
		return result.setSuccessInfo("成功");
	}
	
	@RequestMapping("/businessmenu")
	public ResultMessage listBusinessMenu(ResultMessage result){
		
		List<BusinessMenuVo> menuLst = systemService.listBusinessMenu();
		
		return result.setSuccessInfo("成功").putParam("menuList", menuLst);
	}
	
	@RequestMapping("/businessmenu/edit")
	public ResultMessage editBusinessMenu(@RequestParam("menus")List<BusinessMenuVo> menus,
			ResultMessage result){
		
		System.out.println();
		systemService.updateBusinessMenu(menus);
		return result.setSuccessInfo("成功");
	}  
	
	@RequestMapping("/doc/names")
	public ResultMessage listDocName(ResultMessage result){
		
		List<DocumentVo> docLst = systemService.listDocName();
		
		return result.setSuccessInfo("成功").putParam("docNameList", docLst);
	}
	
	@RequestMapping("/doc/{name}")
	public ResultMessage getDocByName(@PathVariable("name") String name,
			ResultMessage result){
		
		DocumentVo doc = systemService.getDocByName(name);
		
		return result.setSuccessInfo("成功").putParam("doc", doc);
	}
	
	@RequestMapping("/doc/edit")
	public ResultMessage editDoc(@ModelAttribute("doc") @Valid DocumentVo doc,
			BindingResult bindresult,
			ResultMessage result){
		
		SpringUtils.validate(bindresult);
		systemService.updateDoc(doc);
		return result.setSuccessInfo("成功");
	}
}
















