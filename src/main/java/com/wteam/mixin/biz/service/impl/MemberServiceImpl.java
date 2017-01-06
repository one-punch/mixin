package com.wteam.mixin.biz.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.ISystemDao;
import com.wteam.mixin.biz.service.IFinanceService;
import com.wteam.mixin.biz.service.IMemberService;
import com.wteam.mixin.constant.PlatformInfo;
import com.wteam.mixin.constant.State;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessBalanceRecordPo;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.MemberPo;
import com.wteam.mixin.model.po.MemberTrafficPlanPo;
import com.wteam.mixin.model.po.MemberVaildityPo;
import com.wteam.mixin.model.po.PlatformInfoPo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.vo.MTrafficPlanVo;
import com.wteam.mixin.model.vo.MemberTrafficPlanVo;
import com.wteam.mixin.model.vo.MemberVaildityVo;
import com.wteam.mixin.model.vo.MemberVo;

@Service("memberService")
public class MemberServiceImpl implements IMemberService{

	@Autowired
	private IBaseDao baseDao;
	
	@Autowired
	private DozerBeanMapper mapper;
	
    @Autowired
    IFinanceService financeService;
    /**
     * log4j实例对象.
     */
    private static Logger LOG = LogManager.getLogger(MemberServiceImpl.class.getName());
	
	@Override
	public void addMember(MemberVo member) {
		//查找数据库中是否存在同名会员，若已存在则抛出ServiceException,若不存在则进行保存
		LOG.debug("addMember method:"+member.toString());
		MemberPo _member = baseDao.findUniqueByProperty("name", member.getName(), MemberPo.class);
		if(_member != null){
			throw new ServiceException("已存在同名的会员商品信息!"+member.getName());
		}
		MemberPo memberPo = mapper.map(member, MemberPo.class);
		baseDao.save(memberPo);
	}


    @Override
    public void editMember(MemberVo member) {
        MemberPo _member = baseDao.findUniqueByProperty("id", member.getId(), MemberPo.class);
        if(_member == null){
            throw new ServiceException("找不到此会员!"+member.getName());
        }
        mapper.map(member, _member);
        baseDao.update(_member);
    }
    
	@Override
	public void deleteMember(Long id) {
		LOG.debug("deleteMember method:"+id);
		MemberPo member = baseDao.find(MemberPo.class, id);
		if(member==null){
			throw new ServiceException("给会员信息不存在，无法删除"+id);
		}
		member.setIsDelete(true);

		baseDao.update(member);
	}

	@Override
	public void addMemberVaildity(MemberVaildityVo vaildity) {
		MemberPo _member = baseDao.get("from MemberPo where id=?", new Object[]{vaildity.getMemberId()});
		if(_member == null){
			throw new ServiceException("所属会员不存在"+vaildity.getMemberId());
		}
		LOG.debug("addMemberVaildity method:"+vaildity.toString());
		baseDao.save(mapper.map(vaildity, MemberVaildityPo.class));
	}

	@Override
	public void deleteMemberVaildity(Long vaildityId) {
		LOG.debug("deleteMemberVaildity method:"+vaildityId);
		MemberVaildityPo vaildity = baseDao.find(MemberVaildityPo.class, vaildityId);
		if(vaildity==null){
			throw new ServiceException("该会员有效期信息不存在，无法删除"+ vaildityId);
		}
		vaildity.setIsDelete(true);
		baseDao.update(vaildity);
	}

	@Override
	public List<MemberVo> findAllMembers() {
		LOG.debug("findAllMembers method:");
		List<MemberVo> voLst = new ArrayList<>();
		String hql = "from MemberPo where isDelete=false";
		List<MemberPo> memberLst = baseDao.find(hql);
		for (MemberPo member : memberLst) {
			hql = "from MemberVaildityPo as vaildity where vaildity.memberId=? and vaildity.isDelete=false";
			Object[] params = {member.getId()};
			List<MemberVaildityVo> vaildityLst = baseDao.find(hql, params)
				.stream()
				.map(po->mapper.map(po, MemberVaildityVo.class))
				.collect(Collectors.toList());
			MemberVo memberVo = mapper.map(member, MemberVo.class);
			memberVo.setVailditys(vaildityLst);
			voLst.add(memberVo);
		}
		return voLst;
	}

	@Override
	public List<MemberTrafficPlanVo> findMemberTrafficPlansByMemberId(Long memberId) {
		
		LOG.debug("findMemberTrafficPlansByMemberId method:"+memberId);
		String hql = "select new com.wteam.mixin.model.vo.MemberTrafficPlanVo("
		                + "memPlan.id,m.name, m.id, plan.id, "
		                + "plan.cost, memPlan.cost, plan.name, plan.value, plan.retailPrice, "
		                + "plan.display, plan.provider, plan.apiProvider) "
		            + "from MemberPo m, MemberTrafficPlanPo memPlan, TrafficPlanPo plan "
		            + "where m.id=? and m.id=memPlan.memberId and memPlan.trafficplanId=plan.id "
		                + "and m.isDelete=false and plan.isDelete=false and memPlan.isDelete=false";
		List<MemberTrafficPlanVo> lst = baseDao.find(hql, new Object[]{memberId});
		return lst;
	}

	@Override
	public void addMemberTrafficPlan(MemberTrafficPlanVo plan) {
		LOG.debug("addMemberTrafficPlan method:"+plan.toString());
		TrafficPlanPo _plan = baseDao.find(TrafficPlanPo.class, plan.getTrafficPlanId());
		if(_plan==null){
			throw new ServiceException("流量套餐"+plan.getTrafficPlanId()+"不存在");
		}
		MemberPo _member = baseDao.find(MemberPo.class, plan.getMemberId());
		if(_member == null){
			throw new ServiceException("会员信息"+plan.getMemberId()+"不存在");
		}
		MemberTrafficPlanPo _mPlan = baseDao.get("from MemberTrafficPlanPo mPlan where mPlan.trafficplanId=? and mPlan.memberId=?", new Object[]{plan.getTrafficPlanId(), plan.getMemberId()});
		if(_mPlan != null){
			throw new ServiceException("已经存在相同会员流量套餐了，请勿重复添加");
		}
		MemberTrafficPlanPo mTrafficPlan = mapper.map(plan, MemberTrafficPlanPo.class);
		baseDao.save(mTrafficPlan);
	}

	@Override
	public void updateMemberTrafficPlan(MTrafficPlanVo plan) {
		
		LOG.debug("updateMemberTrafficPlan method:"+plan.toString());
		MemberTrafficPlanPo _plan = baseDao.find(MemberTrafficPlanPo.class, plan.getId());
		if(_plan == null){
			throw new ServiceException("该会员流量套餐不存在，无法修改");
		}
		_plan.setCost(plan.getCost());
		baseDao.update(_plan);
	}

	@Override
	public void deleteMemberTrafficPlan(Long memberTrafficPlanId) {
		LOG.debug("deleteMemberTrafficPlan method:"+memberTrafficPlanId);
		MemberTrafficPlanPo _plan = baseDao.find(MemberTrafficPlanPo.class, memberTrafficPlanId);
		if(_plan == null){
			throw new ServiceException("该会员流量套餐不存在，无法删除");
		}
		baseDao.delete(_plan);
	}

	@Override
	public String purchaseMember(Long memberVaildityId, Long userId) {
		LOG.debug("purchaseMember method:"+memberVaildityId);
		//1.判断所购买的会员是否存在
		MemberVaildityPo _vaildity = baseDao.find(MemberVaildityPo.class, memberVaildityId);
		if(_vaildity == null || _vaildity.getIsDelete()){
			throw new ServiceException("您好，您所购买会员不存在。");
		}
		//2.判断用户余额是否足够支付该会员
		BigDecimal cost = _vaildity.getCost();
		BusinessInfoPo _business = baseDao.get("from BusinessInfoPo busi where busi.businessId=?", new Object[]{userId});
		if(_business.getBalance().compareTo(cost) == -1 ){
			throw new ServiceException("账户余额不足，请充值。");
		}
		//2.1.判断用户当前是否已经购买了(还没过有效期)同类会员
		Long memberId = _business.getMemberId();
		Date memberStartAt = _business.getMemberStartAt();
		int memberVailidity = _business.getMemberVailidity();
		BigDecimal balance = _business.getBalance();
		String resultInfo = "";
		if(memberId==null){
			//用户没有购买过任何类型的会员
			//直接修改用户信息:设置会员信息，扣除会员费用
			_business.setMemberId(_vaildity.getMemberId());
			_business.setMemberStartAt(new Date());
			_business.setMemberVailidity(_vaildity.getMemberVaildity());
			_business.setBalance(balance.subtract(_vaildity.getCost()));
			resultInfo = "首次购买会员成功";
		}else{
			//用户已经购买过相关类型的会员
			//1.判断已购买的会员与待支付的会员是否是同一产品
			if(memberId==_vaildity.getMemberId()){
				//1.1同一产品判断是否已过期
				Long vailTime = new Long(memberVailidity*24*60*60*1000L);
				Long gongTime = System.currentTimeMillis()-memberStartAt.getTime();
				if(gongTime<=vailTime){
					//未过期,续期
					_business.setMemberVailidity(memberVailidity+_vaildity.getMemberVaildity());
					_business.setBalance(balance.subtract(_vaildity.getCost()));
					resultInfo = "续费会员成功";
				}else{
					//已过期，覆盖
					_business.setMemberStartAt(new Date());
					_business.setMemberVailidity(_vaildity.getMemberVaildity());
					_business.setBalance(balance.subtract(_vaildity.getCost()));
					resultInfo = "再次购买会员成功";
				}
			}else{
			//1.2不是同一产品直接覆盖
				_business.setMemberId(_vaildity.getMemberId());
				_business.setMemberStartAt(new Date());
				_business.setMemberVailidity(_vaildity.getMemberVaildity());
				_business.setBalance(balance.subtract(_vaildity.getCost()));
				resultInfo = "重新购买会员成功";
			}
		}
		//3.生成财务记录
		BusinessBalanceRecordPo record = new BusinessBalanceRecordPo();
		record.setBusinessId(userId);
		record.setCreateTime(new Date());
		record.setMoney(_vaildity.getCost().negate());
		record.setSource(State.BBRecordSource.service);

        financeService.saveBusinessBalanceRecord(record);
		
		return resultInfo;
	}


    @Override
    public void setBusinessMember(Long memberVaildityId, Long userId) {
        //1.判断所购买的会员是否存在
        MemberVaildityPo _vaildity = baseDao.find(MemberVaildityPo.class, memberVaildityId);
        if(_vaildity == null || _vaildity.getIsDelete()){
            throw new ServiceException("会员不存在。");
        }
        BusinessInfoPo _business = baseDao.get("from BusinessInfoPo busi where busi.businessId=?", new Object[]{userId});
        if(_business == null ){
            throw new ServiceException("商家不存在");
        }
        Long memberId = _business.getMemberId();
        Date memberStartAt = _business.getMemberStartAt();
        int memberVailidity = _business.getMemberVailidity();
        
        if(memberId==null){
            //用户没有购买过任何类型的会员
            //直接修改用户信息:设置会员信息
            _business.setMemberId(_vaildity.getMemberId());
            _business.setMemberStartAt(new Date());
            _business.setMemberVailidity(_vaildity.getMemberVaildity());
        }else{
            //用户已经购买过相关类型的会员
            //1.判断已购买的会员与待支付的会员是否是同一产品
            if(memberId==_vaildity.getMemberId()){
                //1.1同一产品判断是否已过期
                Long vailTime = new Long(memberVailidity*24*60*60*1000L);
                Long gongTime = System.currentTimeMillis()-memberStartAt.getTime();
                if(gongTime<=vailTime){
                    //未过期,续期
                    _business.setMemberVailidity(memberVailidity+_vaildity.getMemberVaildity());
                }else{
                    //已过期，覆盖
                    _business.setMemberStartAt(new Date());
                    _business.setMemberVailidity(_vaildity.getMemberVaildity());
                }
            }else{
            //1.2不是同一产品直接覆盖
                _business.setMemberId(_vaildity.getMemberId());
                _business.setMemberStartAt(new Date());
                _business.setMemberVailidity(_vaildity.getMemberVaildity());
            }
        }
        baseDao.update(_business);
    }
}














