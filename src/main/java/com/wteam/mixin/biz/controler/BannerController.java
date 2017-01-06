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
import com.wteam.mixin.biz.service.IBusinessBannerService;
import com.wteam.mixin.define.Result;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.vo.BusinessBannerVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.utils.SpringUtils;

/**
 * 登录模块控制器
 * 
 * @version 1.0
 * @author 
 */
@RestController
@RequestMapping("/banners")
public class BannerController {
	
    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(BannerController.class.getName());
    
    @Autowired
    IBusinessBannerService bannerService;
    
    /** 广告列表 */
    public static final String BANNER_LIST = "bannerList";
    

    /**
     * （1）商家查看自己的广告列表
     * 
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @RequestMapping(value = "/business", method = RequestMethod.GET)
    public Result business(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
    		ResultMessage resultMessage) {
    	
    	List<?> list = bannerService.findAllBybusinessId(user.getUserId());
    	
        return resultMessage.setSuccessInfo("获取列表成功").putParam(BANNER_LIST, list);
    }

    /**
     * （2）用户查看商家的广告列表
     * 
     * @param resultMessage 返回参数.
     * @return resultMessage
     */
    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public Result customer(@RequestParam("businessId") Long businessId,
    		ResultMessage resultMessage) {
    	
    	List<?> list = bannerService.findAllByIdNActived(businessId, true);
    	
        return resultMessage.setSuccessInfo("获取列表成功").putParam(BANNER_LIST, list);
    }
    
    /**
     * 商家添加广告信息
     * @param banner
     * @param result
     * @return
     */
    @RequestMapping(value="/business/save", method={RequestMethod.POST})
    public Result save(@ModelAttribute("banner") @Valid BusinessBannerVo banner, BindingResult result,
    		ResultMessage resultMessage){
    	
    	SpringUtils.validate(result);
    	LOG.debug("BannerController -> add method");
    	bannerService.add(banner);
    	
    	return resultMessage.setSuccessInfo("增加广告成功");
    }
    
    /**
     * 商家删除广告信息
     * @param banner
     * @param result
     * @return
     */
    @RequestMapping(value="/business/delete", method={RequestMethod.POST})
    public Result delete(@RequestParam("bannerId") Long bannerId,
    		ResultMessage resultMessage){
    	
    	LOG.debug("BannerController -> delete method");
    	bannerService.delete(bannerId);
    	
    	return resultMessage.setSuccessInfo("删除广告成功");
    }
}





