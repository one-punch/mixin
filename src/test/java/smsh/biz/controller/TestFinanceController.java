package smsh.biz.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.biz.service.IFinanceService;
import com.wteam.mixin.biz.service.IThemeService;
import com.wteam.mixin.hibernate.link.LinkerFactory;
import com.wteam.mixin.model.po.BusinessBalanceRecordPo;
import com.wteam.mixin.model.po.BusinessBannerPo;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.BusinessThemePo;
import com.wteam.mixin.model.po.MemberPo;
import com.wteam.mixin.model.po.MemberTrafficPlanPo;
import com.wteam.mixin.model.po.MemberVaildityPo;
import com.wteam.mixin.model.po.PermissionPo;
import com.wteam.mixin.model.po.PlatformInfoPo;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.ThemePo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.po.WithdrawPo;
import com.wteam.mixin.model.vo.BusinessBannerVo;
import com.wteam.mixin.model.vo.BusinessThemeVo;
import com.wteam.mixin.model.vo.MTrafficPlanVo;
import com.wteam.mixin.model.vo.MemberTrafficPlanVo;
import com.wteam.mixin.model.vo.MemberVaildityVo;
import com.wteam.mixin.model.vo.MemberVo;
import com.wteam.mixin.model.vo.ThemeVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.model.vo.WithdrawVo;
import com.wteam.mixin.pagination.Pagination;
import com.wteam.mixin.utils.MD5Utils;

import test.hibernate.factory.GWWMockGenerateFactory;
import test.hibernate.factory.MockGenerateFactory;
import test.hibernate.link.LinkHandler;
import testhelper.BaseControllerTest;

/**
 * 
 * <p>Title:会员模块单元测试</p>
 * <p>Description:</p>
 * @version 1.0
 * @author 龚文伟
 * @date 2016年7月27日
 */
@SuppressWarnings("unused")
public class TestFinanceController extends BaseControllerTest {
    /** springmvc 上下文*/
    @Autowired
    private WebApplicationContext wac;

    /** web测试环境*/
    private MockMvc mockMvc;

    @Autowired
    private IBaseDao baseDao;
    
    @Autowired
    private IFinanceService financeService;

	private UserPo superUser;

	private UserPo business;

	private BusinessInfoPo businessInfo;
	
	private List<PlatformInfoPo> platInfoLst;
	
	private List<BusinessBalanceRecordPo> balanceLst;

	private List<WithdrawPo> withdrawLst;
     
    /**
     * 向数据库测试使用的插入数据
     * @throws ServletException 
     */
    @Before
    public void setUp() throws ServletException {
        // 创建测试环境
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
//            .alwaysExpect( content().contentType("application/json;charset=UTF-8"))
            .addFilters(super.characterEncodingFilter())
            .addFilter(super.shiroFilter(wac))
            .build();
        // 必写
        LinkHandler.setBaseDao(baseDao);
        GWWMockGenerateFactory generateFactory = new GWWMockGenerateFactory(baseDao, mapper);
        /**
         *  shiro集成测试时设置权限，不使用时可将其注释，减少测试时间
         */
        // 获取角色
        RolePo superRole = generateFactory.role.generate(0);
        RolePo businessRole = generateFactory.role.generate(1);
        
        //角色关联权限
        LinkHandler.manyToMany(new PermissionPo("/theme/list/super"), superRole, linkerFactory.permissionToRole);
        
        //获取用户
        superUser = generateFactory.user.generate(0);
        business = generateFactory.user.generate(1);
        
//        businessInfo = generateFactory.busi.generate(0);//余额不足:该账号用于模拟购买非免费主题失败情景
        businessInfo = generateFactory.busi.generate(1);//余额充足:该账号用于模拟购买非免费主题成功，续费情景
        
        //测试数据插入
        platInfoLst = generateFactory.platromInfo.generate(true);
        balanceLst = generateFactory.balance.generate(true);
        withdrawLst = generateFactory.withdraw.generate(true);
        
        //用户关联角色
        LinkHandler.manyToMany(superUser, superRole, linkerFactory.userToRole);
        LinkHandler.manyToMany(business, businessRole, linkerFactory.userToRole);
        
        LinkHandler.oneToOne(businessInfo, business, linkerFactory.businessInfo2user);
        LinkHandler.oneToMany(business, balanceLst.subList(0, 6), linkerFactory.business2busiBalance);
        LinkHandler.oneToMany(business, withdrawLst, linkerFactory.business2withdraw);
    }
    
    @Test
    public void testListAllRecord() throws Exception{
    	// 请求路径 
        String url = "/balance/list/super";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        /**
         * 测试
         */
        RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("pageNo", "1")
            .param("pageSize", "3");
            ;
        
        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
    
    @Test
    public void testListPlatformFinance() throws Exception{
    	// 请求路径 
        String url = "/balance/platform";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        /**
         * 测试
         */
        RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            ;
        
        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
    
    @Test
    public void testListAllWithdraw() throws Exception{
    	// 请求路径 
        String url = "/withdraw/list/super";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        /**
         * 测试
         */
        RequestBuilder builder = get(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("pageNo", "1")
            .param("pageSize", "10")
            ;
        
        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
    
    @Test
    public void testWithdrawPass() throws Exception{
    	// 请求路径 
        String url = "/withdraw/pass";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        /**
         * 测试
         */
        /*
         * 正确的状态：wait4audit：passed
         */
//        RequestBuilder builder = get(url)
//            .cookie(cookie)// 请求时设置cookie保持登录状态
//            .param("withdrawId", String.valueOf(withdrawLst.get(2).getId()))
//            ;
        
        RequestBuilder builder = get(url)
              .cookie(cookie)// 请求时设置cookie保持登录状态
              .param("withdrawId", String.valueOf(withdrawLst.get(2).getId()))
              ;
        /*
         * 错误的状态：successed:failed
         *  RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("withdrawId", String.valueOf(withdrawLst.get(0).getId()))
                ;
         */
        
        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        
        List<BusinessBalanceRecordPo> baLst = baseDao.find("from BusinessBalanceRecordPo");
        System.out.println("record:");
        for (BusinessBalanceRecordPo businessBalanceRecordPo : baLst) {
			System.out.println(businessBalanceRecordPo.toString());
		}
    }
    
    @Test
    public void testWithdrawReject() throws Exception{
    	// 请求路径 
        String url = "/withdraw/reject";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        /**
         * 测试
         */
        RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("withdrawId", String.valueOf(withdrawLst.get(2).getId()))
                .param("failInfo", "你脸大所以不给你通过~")
                ;
        
        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        
        Pagination page = financeService.listAllWithdraw(1, 10);
        @SuppressWarnings("unchecked")
		List<WithdrawVo> withLst = (List<WithdrawVo>) page.getList();
        System.out.println("after:");
        for (WithdrawVo with : withLst) {
			System.out.println(with.toString());
		}
    }
    
    @Test
    public void testBusinessFinance() throws Exception{
//      超级管理员
    	// 请求路径 
//        String url = "/balance/super/business";
//        /**
//         * 登录获取Cookie
//         */
//        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

    	
//    	商家
        String url = "/balance/business";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));
        
        /**
         * 测试
         */
        RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                ;
        
        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
    
    @Test
    public void testBusinessFinanceRecord() throws Exception{
//      超级管理员
    	// 请求路径 
        String url = "/balance/list/super/business";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

    	
//    	商家
//        String url = "/balance/list/business";
//        /**
//         * 登录获取Cookie
//         */
//        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));
        
        /**
         * 测试
         */
        RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("pageNo", "1")
                .param("pageSize", "3")
                .param("businessId", String.valueOf(business.getUserId()))
                ;
        
        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
    
    @Test
    public void testListBusinessWithdraw() throws Exception{
       
    	String url = "/withdraw/list/business";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));
        
        /**
         * 测试
         */
        RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("pageNo", "1")
                .param("pageSize", "3")
                ;
        
        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
    
    @Test
    public void testNewWithdraw() throws Exception{
       
    	String url = "/withdraw/submit";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));
        
        WithdrawVo with = new WithdrawVo(business.getUserId(), "ali---mayun", new BigDecimal(Long.MAX_VALUE), "你敢不给？");
        
        /**
         * 测试
         */
        RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("withdraw", JSON.toJSONString(with))
                ;
        
        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        
        System.out.println("after:");
        List<WithdrawPo> withLst = baseDao.find("from WithdrawPo");
        for (WithdrawPo withdrawPo : withLst) {
			System.out.println(withdrawPo.toString());
		}
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
