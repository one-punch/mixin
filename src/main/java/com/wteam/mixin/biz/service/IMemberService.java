package com.wteam.mixin.biz.service;

import java.util.List;

import com.wteam.mixin.model.vo.MTrafficPlanVo;
import com.wteam.mixin.model.vo.MemberTrafficPlanVo;
import com.wteam.mixin.model.vo.MemberVaildityVo;
import com.wteam.mixin.model.vo.MemberVo;

/**
 * <p>Title:会员模块接口</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年7月26日
 */
public interface IMemberService {
	
	/**
	 * 添加会员商品信息
	 * 	①名字查重
	 *  ②在查重通过的基础上进行插入操作
	 * @param member
	 */
	public void addMember(MemberVo member);

    /**
     * 编辑会员商品信息
     *  ①名字查重
     *  ②在查重通过的基础上进行插入操作
     * @param member
     */
    public void editMember(MemberVo member);
	/**
	 * 删除某一会员商品信息
	 * 	通过将idDelete值设置为true
	 * @param id
	 */
	public void deleteMember(Long id);
	
	/**
	 * 向某一会员商品添加有效期使其成为真正意义上可被购买的虚拟商品.
	 * 会员有效期有多种存在形式,如15元/30天..
	 * @param vaildity
	 */
	public void addMemberVaildity(MemberVaildityVo vaildity);
	
	/**
	 * 删除会员有效期商品
	 *  通过将idDelete设置为true实现
	 * @param vaildityId
	 */
	public void deleteMemberVaildity(Long vaildityId);
	
	/**
	 * 查询所有会员及其派生的商品信息信息
	 * @return
	 */
	public List<MemberVo> findAllMembers();
	
	/**
	 * 查询某一会员所具有的流量套餐优惠信息
	 * @param vaildityId
	 * @return
	 */
	public List<MemberTrafficPlanVo> findMemberTrafficPlansByMemberId(Long memberId);
	
	/**
	 * 向某一会员添加流量套餐
	 * @param plan
	 */
	public void addMemberTrafficPlan(MemberTrafficPlanVo plan);
	
	/**
	 * 更新某会员的某流量套餐信息
	 * @param plan
	 */
	public void updateMemberTrafficPlan(MTrafficPlanVo plan);
	
	/**
	 * 删除某会员的某流量套餐
	 * @param memberTrafficPlanId
	 */
	public void deleteMemberTrafficPlan(Long memberTrafficPlanId);
	
	/**
	 * 购买或续费会员
	 *  *判断流量套餐是否存在
	 *  *判断商家余额是否足够
	 *  *修改BusinessInfoPo相关会员信息
	 *  *生成BusinessBalanceRecord记录
	 * @param memberTrafficPlanId
	 */
	public String purchaseMember(Long memberVaildityId, Long userId);
	
	/**
	 * 给商家设置会员
	 * @param memberVaildityId
	 * @param userId
	 */
	public void setBusinessMember(Long memberVaildityId, Long userId);
}
