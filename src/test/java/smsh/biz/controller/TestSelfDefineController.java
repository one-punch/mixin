package smsh.biz.controller;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
import com.wteam.mixin.model.po.BusinessBannerPo;
import com.wteam.mixin.model.po.BusinessInfoPo;
import com.wteam.mixin.model.po.PermissionPo;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.po.WechatCustomMenuPo;
import com.wteam.mixin.model.po.WechatReplyPo;
import com.wteam.mixin.model.vo.AutoReplyVo;
import com.wteam.mixin.model.vo.BusinessBannerVo;
import com.wteam.mixin.model.vo.CustomMenuVo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.utils.MD5Utils;

import test.hibernate.factory.GWWMockGenerateFactory;
import test.hibernate.factory.MockGenerateFactory;
import test.hibernate.link.LinkHandler;
import testhelper.BaseControllerTest;

/**
 *
 * @author benko
 * @version 2016年6月13日
 * @see TestBannerController
 * @since
 */
@SuppressWarnings("unused")
public class TestSelfDefineController extends BaseControllerTest {
    /** springmvc 上下文*/
    @Autowired
    private WebApplicationContext wac;

    /** web测试环境*/
    private MockMvc mockMvc;

    private UserPo superUser;

	private List<WechatReplyPo> replyLst;

	private List<WechatCustomMenuPo> menuLst;

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
        RolePo businessRole = generateFactory.role.generate(1);
        // 保存权限关联角色

        // 保存用户关联角色
        UserPo businessUser = generateFactory.user.generate(1);
        BusinessInfoPo businessInfo = generateFactory.busi.generate(5);
        LinkHandler.manyToMany(businessUser, businessRole, linkerFactory.userToRole);
        LinkHandler.oneToOne(businessInfo, businessUser, linkerFactory.businessInfo2user);

        replyLst = generateFactory.reply.generate(true);
        LinkHandler.oneToMany(businessUser, replyLst, linkerFactory.business2reply);

        menuLst = generateFactory.cMenu.generate(true);
        LinkHandler.oneToMany(businessUser, menuLst, linkerFactory.business2menu);
        LinkHandler.oneToMany(menuLst.get(0), menuLst.subList(1, 4), linkerFactory.menuParent2child);
        LinkHandler.oneToMany(menuLst.get(4), menuLst.subList(5, 7), linkerFactory.menuParent2child);
    }

    @Test
    public void testNewAutoReply() throws Exception{

    	// 请求路径
    	String url = "/business/reply/new";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

        AutoReplyVo reply = new AutoReplyVo("今日关注", "", true);

        /**
         * 测试
         */
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("reply", JSON.toJSONString(reply))
            ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();

        List<WechatReplyPo> replyLst = baseDao.find("from WechatReplyPo");
        for (WechatReplyPo wechatReplyPo : replyLst) {
        	System.out.println("after:"+ wechatReplyPo.toString());
		}
    }

    @Test
    public void testEditAutoReply() throws Exception{

    	// 请求路径
    	String url = "/business/reply/edit";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

//        AutoReplyVo reply = new AutoReplyVo(99999L ,"今日关注", "", true);//错误的回复id
        AutoReplyVo reply = new AutoReplyVo(replyLst.get(0).getId() ,"今日关注", "", true);
        /**
         * 测试
         */
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("reply", JSON.toJSONString(reply))
            ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();

        List<WechatReplyPo> replyLst = baseDao.find("from WechatReplyPo");
        for (WechatReplyPo wechatReplyPo : replyLst) {
        	System.out.println("after:"+ wechatReplyPo.toString());
		}
    }

    @Test
    public void testDeleteAutoReply() throws Exception{

    	// 请求路径
    	String url = "/business/reply/delete";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));


        /**
         * 测试
         */
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
//            .param("replyId", JSON.toJSONString(99999))//错误的回复id
            .param("replyId", String.valueOf(replyLst.get(1).getId()))
            ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();

        List<WechatReplyPo> replyLst = baseDao.find("from WechatReplyPo");
        for (WechatReplyPo wechatReplyPo : replyLst) {
        	System.out.println("after:"+ wechatReplyPo.toString());
		}
    }

    @Test
    public void testListAutoReply() throws Exception{

    	// 请求路径
    	String url = "/business/reply/list";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));


        /**
         * 测试
         */
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
//            .param("replyId", JSON.toJSONString(99999))//错误的回复id
            ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }

    @Test
    public void testNewMenu() throws Exception{

    	// 请求路径
    	String url = "/business/menu/new";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

        CustomMenuVo menu = new CustomMenuVo(13456L, "淘我所爱", null, "http://www.taobao.com", "VIEW", 2, menuLst.get(4).getId());
        /**
         * 测试
         */
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
            .param("menu", JSON.toJSONString(menu))
            ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }

    @Test
    public void testListMenu() throws Exception{

    	// 请求路径
    	String url = "/business/menu/list";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));


        /**
         * 测试
         */
        RequestBuilder builder = post(url)
            .cookie(cookie)// 请求时设置cookie保持登录状态
//            .param("replyId", JSON.toJSONString(99999))//错误的回复id
            ;

        MvcResult result = mockMvc.perform(builder)
            .andDo(print())
            .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
    }
}
























