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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.dao.IBaseDao;
import com.wteam.mixin.hibernate.link.LinkerFactory;
import com.wteam.mixin.model.po.BusinessBalanceRecordPo;
import com.wteam.mixin.model.po.BusinessBannerPo;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.MemberPo;
import com.wteam.mixin.model.po.MemberTrafficPlanPo;
import com.wteam.mixin.model.po.MemberVaildityPo;
import com.wteam.mixin.model.po.PermissionPo;
import com.wteam.mixin.model.po.PlatformInfoPo;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.TrafficPlanPo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.BusinessBannerVo;
import com.wteam.mixin.model.vo.MTrafficPlanVo;
import com.wteam.mixin.model.vo.MemberTrafficPlanVo;
import com.wteam.mixin.model.vo.MemberVaildityVo;
import com.wteam.mixin.model.vo.MemberVo;
import com.wteam.mixin.model.vo.UserVo;
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
public class TestMemberController extends BaseControllerTest {
    /** springmvc 上下文*/
    @Autowired
    private WebApplicationContext wac;

    /** web测试环境*/
    private MockMvc mockMvc;

    @Autowired
    private IBaseDao baseDao;
    
    private UserPo business;
    
    private Long bannerId;

	private List<MemberPo> memberLst;

	private UserPo superUser;
	
	private List<MemberVaildityPo> vailLst;

	private List<MemberTrafficPlanPo> tPlanLst;

	private List<TrafficPlanPo> planLst;
	
	private List<BusinessInfoPo> busiLst;
	
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
        LinkHandler.manyToMany(new PermissionPo("/member/add"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/vaildity/add"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/vaildity/delete"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/list"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/list"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/add"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/edit"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/delete"), superRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/member/trafficplan/buy"), businessRole, linkerFactory.permissionToRole);
        
        //获取用户
        superUser = generateFactory.user.generate(0);
        business = generateFactory.user.generate(1);
        busiLst = generateFactory.busi.generate();
        //权限关联
        //TODO 测试数据设置点
        LinkHandler.manyToMany(superUser, superRole, linkerFactory.userToRole);
        LinkHandler.manyToMany(business, businessRole, linkerFactory.userToRole);
//        LinkHandler.oneToOne(busiLst.get(0), business, linkerFactory.businessInfo2user);//
//        LinkHandler.oneToOne(busiLst.get(1), business, linkerFactory.businessInfo2user);
//        LinkHandler.oneToOne(busiLst.get(2), business, linkerFactory.businessInfo2user);
//        LinkHandler.oneToOne(busiLst.get(3), business, linkerFactory.businessInfo2user);
        LinkHandler.oneToOne(busiLst.get(4), business, linkerFactory.businessInfo2user);
        
        
        //addMember method
        
        //deleteMember method
        memberLst = generateFactory.member.generate(true);
        
        //addMemberVaildity method
//        vailLst = generateFactory.vail.generate(0, 3);
//        LinkHandler.oneToMany(memberLst.get(0), vailLst, linkerFactory.member2Vaildity);
        //deleteMemberVaildity method
        
        //listMember method
        
        // addMemberTrafficPlan method && findMemberTrafficPlan method && editMemberTrafficPlan method
        tPlanLst = generateFactory.memPlan.generate(0,4,true);
        planLst = generateFactory.plan.generate(0,4,true);
        LinkHandler.oneToMany(memberLst.get(0), tPlanLst, linkerFactory.member2MemPlan);
        LinkHandler.oneToOne(tPlanLst, planLst, linkerFactory.MemPlan2plan);
        
        //deleteMemberTrafficPlan method
        
        //parchase method 
        
        //会员有效期关联
        vailLst = generateFactory.vail.generate();
        LinkHandler.oneToMany(vailLst.subList(0, 3), memberLst.get(0), linkerFactory.Vail2Member);
        LinkHandler.oneToMany(vailLst.subList(4, 7), memberLst.get(1), linkerFactory.Vail2Member);
        LinkHandler.oneToMany(vailLst.subList(8, 11), memberLst.get(2), linkerFactory.Vail2Member);
        LinkHandler.oneToMany(vailLst.subList(12, 15), memberLst.get(3), linkerFactory.Vail2Member);
        //商家会员关联
        //余额不足用户 busiLst.get(0)
        //首次购买busiLst.get(1)
//        平台财务信息刷入
        generateFactory.platromInfo.generate(true);
        //同类产品未过期
        LinkHandler.oneToOne(busiLst.get(2), memberLst.get(0), linkerFactory.business2Member);
        //同类产品已过期
        LinkHandler.oneToOne(busiLst.get(3), memberLst.get(0), linkerFactory.business2Member);
        //非同类产品
        LinkHandler.oneToOne(busiLst.get(4), memberLst.get(1), linkerFactory.business2Member);
    }
    
    @Test
    public void testAddMember() throws Exception{
    	
    	// 请求路径 
        String url = "/member/add";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        /**
         * 测试
         */
        MemberVo member = new MemberVo("lhb", "", "xxxxxx");
        
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("member", JSON.toJSONString(member));

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        System.out.println("after:");
        List<MemberPo> find = baseDao.find("from MemberPo");
        for (MemberPo memberPo : find) {
			System.out.println(memberPo.toString());
		}
    }
    
    @Test
    public void testDeleteMember() throws Exception{
    	
    	// 请求路径 
        String url = "/member/delete";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));

        /**
         * 测试
         */
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("memberId", JSON.toJSONString(memberLst.get(0).getId()));

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
    
    @Test
    public void testAddMemberVaildity() throws Exception{
    	
    	// 请求路径 
        String url = "/member/vaildity/add";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));
        
        /**
         * 测试
         */
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("vaildity", JSON.toJSONString(vailLst.get(0)));

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        
        MemberPo member = baseDao.find(MemberPo.class, memberLst.get(0).getId());
        List<MemberVaildityPo> lst = baseDao.find("from MemberVaildityPo vail where vail.memberId=?", new Object[]{member.getId()});
        for (MemberVaildityPo memberVaildityPo : lst) {
			System.out.println(memberVaildityPo.toString());
		}
    }
    
    @Test
    public void testDeleteMemberVaildity() throws Exception{
    	 
    	// 请求路径 
        String url = "/member/vaildity/delete";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));
        
        /**
         * 测试
         */
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("memberVaildityId", JSON.toJSONString(vailLst.get(0).getId()));

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        MemberVaildityPo vail = baseDao.getOnly("from MemberVaildityPo vail where vail.isDelete=true");
        System.out.println(vail.toString());
    }
    
    @Test
    public void testListMember() throws Exception{
    	// 请求路径 
        String url = "/member/list";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));
        
        /**
         * 测试
         */
        RequestBuilder builder = get(url)
            .cookie(cookie);// 请求时设置cookie保持登录状态

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }

    @Test
    public void testFindMemberTrafficPlan() throws Exception{
    	// 请求路径 
        String url = "/member/trafficplan/list";
        Cookie cookie = null;
        
        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

        /**
         * 测试
         */
        {// 测试成功
            RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("memberVaildityId", String.valueOf(memberLst.get(0).getId()));

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andReturn();
        }
    }
    
    @Test
    public void testAddTrafficPlan() throws Exception{
    	// 请求路径 
        String url = "/member/trafficplan/add";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));
        
        /**
         * 测试
         */
        MemberTrafficPlanVo plan = new MemberTrafficPlanVo(memberLst.get(0).getId(), "", planLst.get(0).getId(), new BigDecimal(9999L));
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("memberTrafficplan", JSON.toJSONString(plan));

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        MemberTrafficPlanPo p = baseDao.get("from MemberTrafficPlanPo plan where plan.cost=?", new Object[]{9999L});
        System.out.println(p.toString());
    }
        
    @Test
    public void testEditMemberTrafficPlan() throws Exception{
    	// 请求路径 
        String url = "/member/trafficplan/edit";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));
        
        /**
         * 测试
         */
        BigDecimal cost = tPlanLst.get(0).getCost();
        MTrafficPlanVo plan = new MTrafficPlanVo(tPlanLst.get(0).getId(), new BigDecimal(2222L));
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("memberTrafficplan", JSON.toJSONString(plan));

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        MemberTrafficPlanPo p = baseDao.get("from MemberTrafficPlanPo plan where plan.cost=?", new Object[]{2222L});
        System.out.println(p.toString()+"&&& cost before is "+cost);
    }
        
    @Test
    public void testDeleteMemberTrafficPlan() throws Exception{
    	// 请求路径 
        String url = "/member/trafficplan/delete";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("super", MD5Utils.md5("000000")));
        
        /**
         * 测试
         */
        List<MemberTrafficPlanPo> before = baseDao.find("from MemberTrafficPlanPo");
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("memberTrafficplanId", JSON.toJSONString(tPlanLst.get(0).getId()));

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        List<MemberTrafficPlanPo> after = baseDao.find("from MemberTrafficPlanPo");
        System.out.println("before list count is"+before.size()+"&&&& after is" + after.size());
    }
    
    @Test
    public void testPurchase() throws Exception{
    	// 请求路径 
        String url = "/member/trafficplan/buy";
        /**
         * 登录获取Cookie
         */
        Cookie cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));
        
        /**
         * 测试
         */
        //TODO 测试入口
//        BusinessInfoPo before = baseDao.find(BusinessInfoPo.class, busiLst.get(0).getId());//不存在、余额不足 用户
        BusinessInfoPo before = baseDao.find(BusinessInfoPo.class, busiLst.get(4).getId());//未购买过会员 用户
        System.out.println("before:"+before.toString());
        
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
//            .param("memberVaildityId", JSON.toJSONString(1234))//不存在
//        	.param("memberVaildityId", JSON.toJSONString(vailLst.get(0).getId()))//余额不足
//        	.param("memberVaildityId", JSON.toJSONString(vailLst.get(0).getId()))//未购买过会员
//        	.param("memberVaildityId", JSON.toJSONString(vailLst.get(0).getId()))//同一会员未过期
//        	.param("memberVaildityId", JSON.toJSONString(vailLst.get(0).getId()))//同一会员已过期
            .param("memberVaildityId", JSON.toJSONString(vailLst.get(0).getId()))//不同会员
        	;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
//        BusinessInfoPo after = baseDao.find(BusinessInfoPo.class, busiLst.get(0).getId());
        BusinessInfoPo after = baseDao.find(BusinessInfoPo.class, busiLst.get(4).getId());
        BusinessBalanceRecordPo record = baseDao.get("from BusinessBalanceRecordPo record where record.businessId=?", new Object[]{busiLst.get(4).getBusinessId()});
        System.out.println("after:"+after.toString());
        System.out.println("record:"+record.toString());
        List<PlatformInfoPo> _platInfoLst = baseDao.find("from PlatformInfoPo");
        System.out.println("platinfo:");
        for (PlatformInfoPo info : _platInfoLst) {
			System.out.println(info.getName()+":"+info.getValue());
		}
    }
}






















