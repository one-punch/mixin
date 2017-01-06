package com.wteam.mixin.biz.service;


import java.math.BigDecimal;
import java.util.List;

import com.wteam.mixin.model.po.BusinessBalanceRecordPo;
import com.wteam.mixin.model.query.BusinessBlanceRecordQuery;
import com.wteam.mixin.model.vo.BusinessBalanceRecordVo;
import com.wteam.mixin.model.vo.FinanceVo;
import com.wteam.mixin.model.vo.WithdrawVo;
import com.wteam.mixin.pagination.Pagination;

/**
 * <p>Title:财务业务接口</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年8月13日
 */
public interface IFinanceService {
	
	/**
	 * 获取平台的交易数据
	 * @return
	 */
	public FinanceVo getPlatformFinanceRecord();
	
	/**
	 * 通过商家ID获取某一商家的交易数据
	 * @param businessId 商家ID
	 * @return
	 */
	public FinanceVo getBusinessFinanceRecordByBusinessId(Long businessId);
	
	/**
	 * 分页列出平台所有的财务记录
	 * @param pageNo 页码
	 * @param pageSize 页行数
	 * @return
	 */
	public Pagination listAllrecord(Integer pageNo, Integer pageSize);
	
	/**
	 * 通过商家ID分页列出某一商家的财务记录
	 * @param businessId 商家ID
	 * @param pageNo 页码
	 * @param pageSize 页行数
	 * @return
	 */
	public Pagination listRecordByBusinessId(BusinessBlanceRecordQuery query, Integer pageNo, Integer pageSize);

    /**
     * 通过商家ID分页列出某一商家的财务记录
     * @param businessId 商家ID
     * @param pageNo 页码
     * @param pageSize 页行数
     * @return
     */
    public List<BusinessBalanceRecordVo> listRecordByBusinessId(BusinessBlanceRecordQuery query);
	/**
	 * 管理员加或减商家的余额
	 * @param infoVo
	 * @return
	 */
	public FinanceVo addOrSubtractBusinessBalanceBySuper(BusinessBalanceRecordVo recordVo);
    /**
     * 商家加代理商家的余额
     * @param infoVo
     * @return
     */
    public FinanceVo addProxyBusinessBalance(BusinessBalanceRecordVo recordVo, Long parentId);
    
    /**
     * 管理员加或减商家的余额
     * @param infoVo
     * @return
     */
    public void deleteBusinessBalanceRecord(Long recordId);
	
	/**
	 * 刷新商家汇总数据
	 * @param businessId
	 */
	void refreshBusinessFinance(Long businessId);

    /**
     * 刷新所有商家汇总数据
     * @param businessId
     */
    void refreshAllBusinessFinance();

    void refreshSystemFinance();
	
    
    /**
     * 刷新商家汇总数据
     * @param businessId
     */
    void refreshBusinessRecord(Long businessId);

    /**
     * 刷新所有商家汇总数据
     * @param businessId
     */
    void refreshAllBusinessRecord();

    
    
	/**
	 * 提交提现申请
	 * @param withdraw
	 */
	public void newWithdraw(WithdrawVo withdraw);
	
	/**
	 * 提现审核通过
	 */
	public void withdrawAuditSuccess(Long withdrawId);
	
	/**
	 * 提现审核驳回
	 */
	public void withdrawAuditFail(Long withdrawId, String reason);
	
	/**
	 * 分页列出平台所有的提现申请记录
	 * @param pageNo 页码
	 * @param pageSize 页行数
	 * @return
	 */
	public Pagination listAllWithdraw(Integer pageNo, Integer pageSize);
	
	/**
	 * 通过商家ID分页列出该商家所有的提现记录
	 * @param businessId 商家ID
	 * @param pageNo 页码
	 * @param pageSize 页行数
	 * @return
	 */
	public Pagination listWithdrawByBusinessId(Long businessId, Integer pageNo, Integer pageSize);

	/**
     * 同时跟踪记录到商家和平台
	 * @param record
	 */
	public void saveBusinessBalanceRecord(BusinessBalanceRecordPo record);
}













