package com.wteam.mixin.biz.service.impl;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.dao.ISystemDao;
import com.wteam.mixin.biz.service.IFinanceService;
import com.wteam.mixin.constant.DConfig;
import com.wteam.mixin.constant.PlatformInfo;
import com.wteam.mixin.constant.State;
import com.wteam.mixin.exception.ServiceException;
import com.wteam.mixin.model.po.BusinessBalanceRecordPo;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.CustomerOrderPo;
import com.wteam.mixin.model.po.OrderSettlementPo;
import com.wteam.mixin.model.po.PlatformInfoPo;
import com.wteam.mixin.model.po.WithdrawPo;
import com.wteam.mixin.model.query.BusinessBlanceRecordQuery;
import com.wteam.mixin.model.vo.BusinessBalanceRecordVo;
import com.wteam.mixin.model.vo.BusinessInfoVo;
import com.wteam.mixin.model.vo.FinanceVo;
import com.wteam.mixin.model.vo.WithdrawVo;
import com.wteam.mixin.pagination.Pagination;

/**
 * <p>Title:财务业务接口实现</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月13日
 */
@Service("financeService")
public class FinanceServiceImpl implements IFinanceService{
    /**
     * log4j实例对象.
     */
    private static final Logger LOG = LogManager.getLogger(FinanceServiceImpl.class.getName());
    
	@Autowired
	private IBaseDao baseDao;

    @Autowired
    ISystemDao systemDao;
    
	@Autowired
	private DozerBeanMapper mapper;

    @Autowired
    ThreadPoolTaskExecutor taskExector;
    
    /**商家余额缓存，防并发*/
    private static final ConcurrentHashMap<Long,AtomicReference<BigDecimal>> BUSSINESS_BALANCE_MAP = new ConcurrentHashMap<>();
	
	@Override
	public FinanceVo getPlatformFinanceRecord() {
	    
	    refreshSystemFinance();
		
		PlatformInfoPo _balance = baseDao.get("from PlatformInfoPo where name=?", new Object[]{PlatformInfo.Balance});
		PlatformInfoPo _orderCost = baseDao.get("from PlatformInfoPo where name=?", new Object[]{PlatformInfo.OrderCost});
		PlatformInfoPo _orderIncome = baseDao.get("from PlatformInfoPo where name=?", new Object[]{PlatformInfo.OrderIncome});
		PlatformInfoPo _profits = baseDao.get("from PlatformInfoPo where name=?", new Object[]{PlatformInfo.Profits});
		PlatformInfoPo _settlement = baseDao.get("from PlatformInfoPo where name=?", new Object[]{PlatformInfo.Settlement});
		PlatformInfoPo _unsettlement = baseDao.get("from PlatformInfoPo where name=?", new Object[]{PlatformInfo.UnSettlement});
	
		return new FinanceVo(new BigDecimal(_balance.getValue()),
				new BigDecimal(_unsettlement.getValue()), 
				new BigDecimal(_settlement.getValue()), 
				new BigDecimal(_orderIncome.getValue()), 
				new BigDecimal(_orderCost.getValue()), 
				new BigDecimal(_profits.getValue()));
	}

	@Override
	public FinanceVo getBusinessFinanceRecordByBusinessId(Long businessId) {

	    refreshBusinessFinance(businessId);
	    
		BusinessInfoPo _business = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{businessId});
		if(_business == null){
			throw new ServiceException("很抱歉，该商家不存在。");
		}
		return new FinanceVo(_business.getBalance(), _business.getUnSettlement(),
				_business.getSettlement(), _business.getOrderIncome(),
				_business.getOrderCost(), _business.getProfits());
	}

	@Override
	public Pagination listAllrecord(Integer pageNo, Integer pageSize) {

		if(pageNo == null || pageSize ==null || pageNo<=0 || pageSize<=0){
			throw new ServiceException("页码或页行数不正确");
		}
		Long count = baseDao.getOnly("select count(*) from BusinessBalanceRecordPo");
		Pagination page = new Pagination(pageNo, pageSize, count.intValue());
		List<BusinessBalanceRecordPo> poLst = baseDao.find("from BusinessBalanceRecordPo order by createTime desc", new Object[]{}, page.getPageNo(), page.getPageSize());
		List<BusinessBalanceRecordVo> recordLst = poLst.stream()
				.map(po->mapper.map(po, BusinessBalanceRecordVo.class))
				.collect(Collectors.toList());
		page.setList(recordLst);
		
		return page;
	}

	@Override
	public Pagination listRecordByBusinessId(BusinessBlanceRecordQuery query, Integer pageNo, Integer pageSize) {
		
		BusinessInfoPo _business = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{query.getBusinessId()});
		if(_business == null){
			throw new ServiceException("很抱歉，该商家不存在。");
		}
		
		Long count = baseDao.getOnly("select count(*) from BusinessBalanceRecordPo where " + query.hqlQuery(""));
		Pagination page = new Pagination(pageNo, pageSize, count.intValue());
		List<BusinessBalanceRecordPo> poLst = baseDao.find("from BusinessBalanceRecordPo where "+ query.hqlQuery("") +" order by createTime desc", new Object[]{}, page.getPageNo(), pageSize);
		poLst.forEach(LOG::debug);
		List<BusinessBalanceRecordVo> recordLst = poLst.stream()
				.map(po->mapper.map(po, BusinessBalanceRecordVo.class))
				.collect(Collectors.toList());
		recordLst.forEach(LOG::debug);
		page.setList(recordLst);
		return page;
	}

    @Override
    public List<BusinessBalanceRecordVo> listRecordByBusinessId(BusinessBlanceRecordQuery query) {
        BusinessInfoPo _business = baseDao.get("from BusinessInfoPo where businessId=? ", new Object[]{query.getBusinessId()});
        if(_business == null){
            throw new ServiceException("很抱歉，该商家不存在。");
        }
        List<BusinessBalanceRecordPo> poLst = baseDao.find("from BusinessBalanceRecordPo where "+ query.hqlQuery("") +" order by createTime desc");
        List<BusinessBalanceRecordVo> recordLst = poLst.stream()
                .map(po->mapper.map(po, BusinessBalanceRecordVo.class))
                .collect(Collectors.toList());
        return recordLst;
    }

    @Override
    public FinanceVo addOrSubtractBusinessBalanceBySuper(BusinessBalanceRecordVo recordVo) {
        
        return addOrSubtractBusinessBalance(recordVo, State.BBRecordSource.platformAdd, State.BBRecordSource.platformSubtract);
    }

    @Override
    public FinanceVo addProxyBusinessBalance(BusinessBalanceRecordVo recordVo, Long parentId) {
        if (recordVo.getMoney().signum() <= 0) {
            throw new ServiceException("加款的金额不能为负或0");
        }
        
        BusinessBalanceRecordVo parentRecordVo = new BusinessBalanceRecordVo();
        parentRecordVo.setBusinessId(parentId);
        parentRecordVo.setMoney(recordVo.getMoney().negate());
        // 先减商家的
        addOrSubtractBusinessBalance(parentRecordVo, null , State.BBRecordSource.expendToProxy);
        recordVo.setSourceId(parentId);
        // 再加代理商家
        return addOrSubtractBusinessBalance(recordVo, State.BBRecordSource.businessAdd, null);
    }
    
    
    private FinanceVo addOrSubtractBusinessBalance(BusinessBalanceRecordVo recordVo, Integer addState, Integer subState) {
        BigDecimal balance = recordVo.getMoney();
        Long businessId = recordVo.getBusinessId();
        BusinessInfoPo _business = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{businessId});
        if(_business == null){
            throw new ServiceException("很抱歉，该商家不存在。");
        }
        if (balance.signum() > 0) {// +
            BusinessBalanceRecordPo recordPo = new BusinessBalanceRecordPo(
                businessId, balance, addState);
            recordPo.setInfo(recordVo.getInfo());
            saveBusinessBalanceRecord(recordPo);
        }
        else if (balance.signum() < 0) {// -
            if (_business.getBalance().compareTo(balance.negate()) < 0) {
                throw new ServiceException("很抱歉，余额小于要减去的金额 ");
            }
            BusinessBalanceRecordPo recordPo = new BusinessBalanceRecordPo(
                businessId, balance, subState);
            recordPo.setInfo(recordVo.getInfo());
            saveBusinessBalanceRecord(recordPo);
        } else {
            
        }
        baseDao.flush();
        
        return getBusinessFinanceRecordByBusinessId(businessId);
    }
    
    
    @Override
    public void deleteBusinessBalanceRecord(Long recordId) {
        BusinessBalanceRecordPo recordPo = baseDao.getOnly("from BusinessBalanceRecordPo where id="+ recordId);
        recordPo.setIsDelete(true);
        baseDao.update(recordPo);
    }
    
	@Override
	public void newWithdraw(WithdrawVo vo) {
		BusinessInfoPo _business = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{vo.getBusinessId()});
		if(_business == null){
			throw new ServiceException("很抱歉，该商家不存在。");
		}
		BigDecimal minPrice = new BigDecimal(systemDao.dconfig(DConfig.WithdrawMinPrice));
		if (_business.getBalance().compareTo(vo.getRealIncome()) < 0) {
            throw new ServiceException("账户余额小于提现金额");
        }
	    if (_business.getBalance().compareTo(minPrice) < 0) {
	        throw new ServiceException("账户余额小于允许提现的最小金额");
	    }
		
		WithdrawPo withdraw = mapper.map(vo, WithdrawPo.class);
		withdraw.setState(State.WithdrawState.wait4audit);
		baseDao.save(withdraw);
	}

	@Override
	public void withdrawAuditSuccess(Long withdrawId) {
		WithdrawPo _withdraw = baseDao.find(WithdrawPo.class, withdrawId);
		if(_withdraw == null){
			throw new ServiceException("该申请不存在");
		}
		if(_withdraw.getState() != State.WithdrawState.wait4audit){
			throw new ServiceException("该申请无法审核");
		}
//		2.生成并保存商家财务记录
		saveBusinessBalanceRecord(new BusinessBalanceRecordPo(_withdraw.getBusinessId(),
		    _withdraw.getRealIncome().negate(), 
		    State.BBRecordSource.withdraw));
//		3.修改该申请状态为成功并更新
		_withdraw.setState(State.WithdrawState.successed);
		baseDao.update(_withdraw);
	}

	@Override
	public void withdrawAuditFail(Long withdrawId, String failInfo) {
		WithdrawPo _withdraw = baseDao.find(WithdrawPo.class, withdrawId);
		if(_withdraw == null){
			throw new ServiceException("该申请不存在");
		}
		if(_withdraw.getState() != State.WithdrawState.wait4audit){
			throw new ServiceException("该申请无法审核");
		}
//		1. 修改申请状态为失败、设置失败信息并更新
		_withdraw.setState(State.WithdrawState.failed);
		_withdraw.setFailInfo(failInfo);
		baseDao.update(_withdraw);
	}

	@Override
	public Pagination listAllWithdraw(Integer pageNo, Integer pageSize) {
		
		Long count = baseDao.getOnly("select count(*) from WithdrawPo");
		Pagination page = new Pagination(pageNo, pageSize, count.intValue());
		List<WithdrawPo> poLst = baseDao.find("from WithdrawPo order by createTime desc", new Object[]{}, page.getPageNo(), page.getPageSize());
		List<WithdrawVo> withdrawLst = poLst.stream()
				.map(po->mapper.map(po, WithdrawVo.class))
				.collect(Collectors.toList());
		page.setList(withdrawLst);
		
		return page;
	}

	@Override
	public Pagination listWithdrawByBusinessId(Long businessId, Integer pageNo, Integer pageSize) {
		
		BusinessInfoPo _business = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{businessId});
		if(_business == null){
			throw new ServiceException("很抱歉，该商家不存在。");
		}
		Long count = baseDao.getOnly("select count(*) from WithdrawPo where businessId="+ businessId);
		Pagination page = new Pagination(pageNo, pageSize, count.intValue());
		List<WithdrawPo> poLst = baseDao.find("from WithdrawPo where businessId=? order by createTime desc", new Object[]{businessId}, page.getPageNo(), pageSize);
		List<WithdrawVo> withdrawLst = poLst.stream()
				.map(po->mapper.map(po, WithdrawVo.class))
				.collect(Collectors.toList());
		page.setList(withdrawLst);
		
		return page;
	}
	
	/**
	 * 同时跟踪记录到商家和平台
	 */
	@Override
	public void saveBusinessBalanceRecord(BusinessBalanceRecordPo record) {
	    Long businessId = record.getBusinessId();
	    // 1.获取商家信息
        BusinessInfoPo _business = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{record.getBusinessId()});

        // 获取原子引用
        AtomicReference<BigDecimal> newSum  = BUSSINESS_BALANCE_MAP.get(businessId);
        if (newSum == null) {
            BigDecimal balance = baseDao.get("select sum(money) from BusinessBalanceRecordPo where businessId=?",new Object[]{businessId});
            balance = balance == null ? BigDecimal.ZERO : balance;
            newSum = new AtomicReference<BigDecimal>(balance);
            BUSSINESS_BALANCE_MAP.put(businessId, newSum);
        }

        // 增加记录后，余额小于0
        if (newSum.get().add(record.getMoney()).signum() < 0) { // 
            throw new ServiceException("余额不足");
        }

        // 设置新值
        BigDecimal oldVal = null;
        for (;;) {
            oldVal = newSum.get();
            if (newSum.compareAndSet(oldVal, oldVal.add(record.getMoney())))
                 break;
        }
        _business.setBalance(newSum.get());
        record.setBusinessBeforeMoney(oldVal);

        // 保存
        if (record.getSource().equals(State.BBRecordSource.productApiRecharge)
            || record.getSource().equals(State.BBRecordSource.settlement)
            || record.getSource().equals(State.BBRecordSource.orderCost)
            || record.getSource().equals(State.BBRecordSource.payRefund)
            || record.getSource().equals(State.BBRecordSource.productRechange)
            || record.getSource().equals(State.BBRecordSource.balanceRechange)) {
           String phone = baseDao.get("select phone from CustomerOrderPo where orderNum=? ", 
               Arrays.asList(record.getSourceId().toString()));
           
           record.setTel(phone);
        }
        baseDao.update(_business);
		baseDao.save(record);
		
	}

    @Override
    public void refreshBusinessFinance(Long businessId) {
        BusinessInfoPo infoPo = baseDao.get("from BusinessInfoPo where businessId=?", new Object[]{businessId});
        if (infoPo == null)
            throw new ServiceException("找不到商家信息 " + businessId);
        
        BigDecimal balance = baseDao.get("select sum(money) from BusinessBalanceRecordPo where businessId=?",new Object[]{businessId});
        BigDecimal unSettlement = baseDao.get("select sum(realIncome) from OrderSettlementPo where businessId=? and state=?",
            new Object[]{businessId, State.OrderSettlement.unSettlement});
        BigDecimal settlement = baseDao.get("select sum(realIncome) from OrderSettlementPo where businessId=? and state=?",
            new Object[]{businessId, State.OrderSettlement.settlement});
        BigDecimal realIncome = baseDao.get("select sum(realIncome) from CustomerOrderPo where businessId=? and state=?",
            new Object[]{businessId, State.CustomerOrder.success});
        BigDecimal cost = baseDao.get("select sum(cost) from CustomerOrderPo where businessId=? and state=?",
            new Object[]{businessId, State.CustomerOrder.success});
        BigDecimal profits = baseDao.get("select sum(profits) from CustomerOrderPo where businessId=? and state=?",
            new Object[]{businessId, State.CustomerOrder.success});
        
        infoPo.setBalance(filterNull(balance));
        infoPo.setUnSettlement(filterNull(unSettlement));
        infoPo.setSettlement(filterNull(settlement));
        infoPo.setOrderIncome(filterNull(realIncome));
        infoPo.setOrderCost(filterNull(cost));
        infoPo.setProfits(filterNull(profits));
        
        baseDao.update(infoPo);
    }

    @Override
    public void refreshSystemFinance() {
        
        BigDecimal balance = baseDao.get("select sum(money) from BusinessBalanceRecordPo ",new Object[]{});
        BigDecimal unSettlement = baseDao.get("select sum(realIncome) from OrderSettlementPo where state=?",new Object[]{State.OrderSettlement.unSettlement});
        BigDecimal settlement = baseDao.get("select sum(realIncome) from OrderSettlementPo where state=?",new Object[]{State.OrderSettlement.settlement});
        BigDecimal realIncome = baseDao.get("select sum(realIncome) from CustomerOrderPo where state=?",new Object[]{State.CustomerOrder.success});
        BigDecimal cost = baseDao.get("select sum(cost) from CustomerOrderPo where state=?",new Object[]{State.CustomerOrder.success});
        BigDecimal profits = baseDao.get("select sum(profits) from CustomerOrderPo where state=?",new Object[]{State.CustomerOrder.success});
        
        systemDao.platfromInfo(PlatformInfo.Balance, filterNullToStr(balance));
        systemDao.platfromInfo(PlatformInfo.UnSettlement, filterNullToStr(unSettlement));
        systemDao.platfromInfo(PlatformInfo.Settlement, filterNullToStr(settlement));
        systemDao.platfromInfo(PlatformInfo.OrderIncome, filterNullToStr(realIncome));
        systemDao.platfromInfo(PlatformInfo.OrderCost, filterNullToStr(cost));
        systemDao.platfromInfo(PlatformInfo.Profits, filterNullToStr(profits));
    }

    @Override
    public void refreshAllBusinessFinance() {
        List<Long> businessList =  baseDao.find("select businessId from BusinessInfoPo");
        
        businessList.forEach(id -> {
            refreshBusinessFinance(id);
        });
    }

    @Override
    public void refreshBusinessRecord(Long businessId) {

        BusinessBlanceRecordQuery query = new BusinessBlanceRecordQuery();
        query.setBusinessId(businessId);
        List<BusinessBalanceRecordPo> list =  baseDao.find("from BusinessBalanceRecordPo where " + query.hqlQuery("") + " order by createTime");
        BigDecimal[] totalMoney = {BigDecimal.ZERO};
        Long[] count = {0L};// 总处理数、处理数
        list.forEach(record -> {
            record.setBusinessBeforeMoney(totalMoney[0]);
            totalMoney[0] = totalMoney[0].add(record.getMoney());
            
            if (record.getTel() == null && (record.getSource().equals(State.BBRecordSource.productApiRecharge)
                 || record.getSource().equals(State.BBRecordSource.settlement)
                 || record.getSource().equals(State.BBRecordSource.orderCost)
                 || record.getSource().equals(State.BBRecordSource.payRefund)
                 || record.getSource().equals(State.BBRecordSource.productRechange)
                 || record.getSource().equals(State.BBRecordSource.balanceRechange))) {
                String phone = baseDao.get("select phone from CustomerOrderPo where orderNum=? ", 
                    Arrays.asList(record.getSourceId().toString()));
                
                record.setTel(phone);
            }
            // TODO 到时要掉
            if (record.getTel() == null && 
                record.getSource().equals(State.BBRecordSource.settlement)) {
               Object[] result = baseDao.get("select orderPo.phone,orderPo.orderNum from CustomerOrderPo as orderPo, OrderSettlementPo as settle "
                   + " where orderPo.orderNum=settle.orderNum and settle.id=?", 
                   Arrays.asList(record.getSourceId()));
               
               record.setTel((String)result[0]);
               record.setSourceId(Long.valueOf((String)result[1]));
            }
            
            baseDao.update(record);
            
            if(LOG.isDebugEnabled()) LOG.debug("businessId:{}, 总处理数：{}",businessId, count[0]);
        });
    }

    @Override
    public void refreshAllBusinessRecord() {
        List<Long> businessList =  baseDao.find("select businessId from BusinessInfoPo");
        
        businessList.forEach(id -> {
             refreshBusinessRecord(id);
        });
    }
    
    private BigDecimal filterNull(BigDecimal decimal) {
        return decimal == null ? new BigDecimal("0") : decimal;
    }
    

    private String filterNullToStr(BigDecimal decimal) {
        return filterNull(decimal).toString();
    }


}
