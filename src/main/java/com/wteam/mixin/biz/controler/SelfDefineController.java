package com.wteam.mixin.biz.controler;

import java.util.List;

import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.service.ISelfDefineService;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.vo.AutoReplyVo;
import com.wteam.mixin.model.vo.CustomMenuButton;
import com.wteam.mixin.model.vo.CustomMenuVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.utils.SpringUtils;

/**
 * 微信回复、自定义菜单
 * @author 龚文伟
 *
 */
@RestController
public class SelfDefineController {
    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(SelfDefineController.class.getName());


	@Autowired
	private ISelfDefineService selfDefineService;

	@Autowired
	LocalValidatorFactoryBean validator;

	@RequestMapping(value ="/business/wechat/reply/new", method = RequestMethod.POST)
	public ResultMessage newAutoReply(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user
			, @ModelAttribute("reply") @Valid AutoReplyVo reply
			, BindingResult bindingResult
			, ResultMessage result){

		SpringUtils.validate(bindingResult);
		selfDefineService.addAutoReply(reply, user.getUserId());

		return result.setSuccessInfo("成功");
	}

	@RequestMapping(value ="/business/wechat/reply/edit", method = RequestMethod.POST)
	public ResultMessage editAutoReply(@ModelAttribute("reply") @Valid AutoReplyVo reply
			, BindingResult bindingResult
			, ResultMessage result){

		SpringUtils.validate(bindingResult);
		selfDefineService.updateAutoReply(reply);

		return result.setSuccessInfo("成功");
	}

	@RequestMapping(value ="/business/wechat/reply/delete", method = RequestMethod.POST)
	public ResultMessage deleteAutoReply(@RequestParam("replyId") Long replyId
			, ResultMessage result){

		selfDefineService.deleteAutoReply(replyId);

		return result.setSuccessInfo("成功");
	}

	@RequestMapping("/business/wechat/reply/list")
	public ResultMessage listAutoReply(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user
			,ResultMessage result){

		List<AutoReplyVo> replyLst = selfDefineService.listAutoReply(user.getUserId());

		return result.setSuccessInfo("成功").putParam("replyLst", replyLst);
	}

	@RequestMapping(value ="/business/wechat/menu/new", method = RequestMethod.POST)
	public ResultMessage newCustomMenu(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user
			, @ModelAttribute("menu")@Valid CustomMenuVo menu
			, BindingResult bindingResult
			, ResultMessage result){

		SpringUtils.validate(bindingResult);
		selfDefineService.addCustomMenu(menu, user.getUserId());

		return result.setSuccessInfo("成功");
	}

	@RequestMapping(value ="/business/wechat/menu/edit", method = RequestMethod.POST)
	public ResultMessage editCustomMenu(@ModelAttribute("menu") @Valid CustomMenuVo menu
			, BindingResult bindingResult
			, ResultMessage result){

		SpringUtils.validate(bindingResult);
		selfDefineService.updateCustomMenu(menu);

		return result.setSuccessInfo("成功");
	}

	@RequestMapping(value ="/business/wechat/menu/delete", method = RequestMethod.POST)
	public ResultMessage deleteCustomMenu(@RequestParam("menuId") Long menuId
			, ResultMessage result){

		selfDefineService.deleteCustomMenu(menuId);

		return result.setSuccessInfo("成功");
	}

	/**
	 * benko
	 * @param user
	 * @param menuButtons
	 * @return
	 */
    @RequestMapping(value ="/business/wechat/menu/change", method = RequestMethod.POST)
    public ResultMessage changeCustomMenu(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
                                          @RequestParam("menus") List<CustomMenuButton> menuButtons ,
                                          ResultMessage result){
//        LOG.debug(menuButtons);
//        SpringUtils.validate(bindingResult);
        selfDefineService.changeCustomMenus(menuButtons, user.getUserId());

        return result.setSuccessInfo("成功");
    }

	@RequestMapping("/business/wechat/menu/list")
	public ResultMessage listCustomMenu(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user
			, ResultMessage result){

		JSONObject obj = (JSONObject) selfDefineService.listCustomMenu(user.getUserId());

		return result.setSuccessInfo("成功").putParam("menuLst", obj);
	}


    @RequestMapping("/business/wechat/menu/link/list")
    public ResultMessage listCustomMenuLink(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user
            , ResultMessage result){

        Object map =  selfDefineService.listCustomMenuLink(user.getUserId());

        return result.setSuccessInfo("成功").putParam("linkList", map);
    }

	@RequestMapping(value ="/business/wechat/message/send", method = RequestMethod.POST)
	public ResultMessage sendMass(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user
			, @RequestParam("content") String content
			, ResultMessage result){

		if(selfDefineService.sendMass(content, user.getUserId())){
			return result.setSuccessInfo("成功");
		}else{
			return result.setSystemFailureInfo("失败");
		}
	}
}








