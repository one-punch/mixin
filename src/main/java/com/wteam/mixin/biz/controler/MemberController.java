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

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.controler.handler.SystemModelHandler;
import com.wteam.mixin.biz.service.IMemberService;
import com.wteam.mixin.biz.service.impl.MemberServiceImpl;
import com.wteam.mixin.define.ResultMessage;
import com.wteam.mixin.model.vo.MTrafficPlanVo;
import com.wteam.mixin.model.vo.MemberTrafficPlanVo;
import com.wteam.mixin.model.vo.MemberVaildityVo;
import com.wteam.mixin.model.vo.MemberVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.utils.SpringUtils;

@RestController
@RequestMapping("/member")
public class MemberController {
	
	@Autowired
	private IMemberService memberService;
	
	 /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(MemberServiceImpl.class.getName());
	
	@RequestMapping(value="/add", method={RequestMethod.POST})
	public ResultMessage addMember(@ModelAttribute("member") @Valid MemberVo member,
			BindingResult results,ResultMessage resultMessage){
		
		SpringUtils.validate(results);
		memberService.addMember(member);
		
		return resultMessage.setSuccessInfo("增加会员成功。");
	}
	
	@RequestMapping(value="/edit", method={RequestMethod.POST})
	public ResultMessage editMember(@RequestParam("member")  MemberVo member,ResultMessage resultMessage){
	        if (member.getId() ==  null) {
                return resultMessage.setServiceFailureInfo("没有上传 id");
            }
	        memberService.editMember(member);
	        
	        return resultMessage.setSuccessInfo("修改会员成功。");
	}
	
	@RequestMapping(value="/delete", method={RequestMethod.POST})
	public ResultMessage deleteMember(@RequestParam("memberId") Long memberId,
			ResultMessage resultMessage){
		
		memberService.deleteMember(memberId);
		
		return resultMessage.setSuccessInfo("删除会员成功。");
	}
	
	
	@RequestMapping(value="/vaildity/add", method={RequestMethod.POST})
	public ResultMessage addVaildity(@ModelAttribute("vaildity") @Valid MemberVaildityVo vaildity,
			BindingResult results, ResultMessage resultMessage){
		
		SpringUtils.validate(results);
		
		memberService.addMemberVaildity(vaildity);
		
		return resultMessage.setSuccessInfo("添加会员有效期成功。");
	}

	
	@RequestMapping(value="/vaildity/delete", method={RequestMethod.POST})
	public ResultMessage deleteVaildity(@RequestParam("memberVaildityId") Long vaildityId,
			ResultMessage resultMessage){
		
		memberService.deleteMemberVaildity(vaildityId);
		
		return resultMessage.setSuccessInfo("删除会员有效期成功。");
	}

    
    @RequestMapping(value="/vaildity/buy", method={RequestMethod.POST})
    public ResultMessage purchase(@ModelAttribute(SystemModelHandler.CURRENT_USER) UserVo user,
            @RequestParam(name="memberVaildityId", required=true) Long vaildityId,
            ResultMessage resultMessage){
        LOG.debug("purchase method &&&& userId is "+ user.getUserId());
        String resultInfo = memberService.purchaseMember(vaildityId, user.getUserId());
        
        return resultMessage.setSuccessInfo(resultInfo);
    }
    
    @RequestMapping(value="/vaildity/set_business", method={RequestMethod.POST})
    public ResultMessage set_business(
            @RequestParam(name="memberVaildityId", required=true) Long vaildityId,
            @RequestParam(name="businessId", required=true) Long businessId,
            ResultMessage resultMessage){
        LOG.debug("purchase method &&&& userId is "+ businessId);
        memberService.setBusinessMember(vaildityId, businessId);
        
        return resultMessage.setSuccessInfo("设置会员成功");
    }
	
	@RequestMapping(value="/list")
	public ResultMessage listMember(ResultMessage resultMessage){
		
		List<MemberVo> memberLst = memberService.findAllMembers();
		
		return resultMessage.setSuccessInfo("成功").putParam("memberList", memberLst);
	}

	
	@RequestMapping("/trafficplan/list")
	public ResultMessage findMemberTrafficPlan(@RequestParam("memberId") Long memberId,
			ResultMessage resultMessage){
		
		List<MemberTrafficPlanVo> planLst = memberService.findMemberTrafficPlansByMemberId(memberId);
		return resultMessage.setSuccessInfo("memberTrafficplanList").putParam("memberTrafficplanList", planLst);
	}
	
	
	@RequestMapping(value="/trafficplan/add", method={RequestMethod.POST})
	public ResultMessage addMemberTrafficPlan(@ModelAttribute("memberTrafficplan") @Valid MemberTrafficPlanVo plan,
			BindingResult results, ResultMessage resultMessage){
		
		SpringUtils.validate(results);
		
		memberService.addMemberTrafficPlan(plan);
		
		return resultMessage.setSuccessInfo("添加会员流量套餐成功。");
	}
	
	
	@RequestMapping(value="/trafficplan/edit", method={RequestMethod.POST})
	public ResultMessage editMemberTrafficPlan(@RequestParam("memberTrafficplan") MTrafficPlanVo plan,
			ResultMessage resultMessage){
		
		memberService.updateMemberTrafficPlan(plan);
		
		return resultMessage.setSuccessInfo("编辑会员流量套餐成功。");
	}
	
	
	@RequestMapping(value="/trafficplan/delete", method={RequestMethod.POST})
	public ResultMessage deleteTrafficPlan(@RequestParam(name="memberTrafficplanId", required=true) Long planId,
			ResultMessage resultMessage){
		
		memberService.deleteMemberTrafficPlan(planId);
		
		return resultMessage.setSuccessInfo("删除会员流量套餐成功。");
	}

}









