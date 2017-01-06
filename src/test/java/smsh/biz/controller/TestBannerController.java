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
import com.wteam.mixin.model.po.PermissionPo;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.BusinessBannerVo;
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
public class TestBannerController extends BaseControllerTest {
    /** springmvc 上下文*/
    @Autowired
    private WebApplicationContext wac;

    /** web测试环境*/
    private MockMvc mockMvc;

    private UserPo business;

    private Long bannerId;

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
        RolePo userRole = generateFactory.role.generate(2);

        // 保存权限关联角色
        LinkHandler.manyToMany(new PermissionPo("/banners/business"), businessRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/banners/business/save"), businessRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/banners/business/delete"), businessRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/banners/customer"), userRole, linkerFactory.permissionToRole);

        // 保存用户关联角色
        business = generateFactory.user.generate(0);
        LinkHandler.manyToMany(business, businessRole, linkerFactory.userToRole);
        UserPo userPo2 = generateFactory.user.generate(1);
        LinkHandler.manyToMany(userPo2, userRole, linkerFactory.userToRole);

        /**
         * 测试一个接口时，请注释掉其它生成数据
         */
        List<BusinessBannerPo> bannerPos = generateFactory.businessBanner.generate(true);
        LinkHandler.oneToMany(business, bannerPos, linkerFactory.userTobusinessBanner);
        bannerId = bannerPos.get(0).getId();
    }

    /**
     * 向数据库插入数据，不回滚
     */
    @Test
    @Rollback(false)
    public void testNotRollback() {}

    /**
     * Description: 测试模板<br>
     *
     * @throws Exception
     * @see
     */
    @Test
    public void test_() throws Exception {
        // 请求路径
        String url = "/test/user";

        {// 测试成功
            RequestBuilder builder = get(url)
            // .param("category", category + "")
            ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andReturn();
        }

/*
        {// 测试失败
            RequestBuilder builder = get(url)
            // .param("category", category + "")
            ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect( jsonPath("$.isSuccess").value(false))
                .andReturn();
        }
*/
    }

    /**
     *
     * Description: 登录权限才可访问的URL的测试模板<br>
     *
     * 1、在setUp()向数据库加入用户信息<br>
     * 2、用户登录<br>
     * 3、从用户登录的response获取Cookie值<br>
     * 4、将Cookie值加入测试resquest中<br>
     * 5、执行测试<br>
     *
     * @throws Exception
     * @see
     */
    @Test
    public void testAfterLogin_() throws Exception {
        // 请求路径
        String url = "/banners/business";
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
            // .param("category", category + "")
                ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andReturn();
        }
/*
        {// 测试失败
            RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
            // .param("category", category + "")
                ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect( jsonPath("$.isSuccess").value(false))
                .andReturn();
        }*/
    }

    @Test
    public void testSave() throws Exception{

    	// 请求路径
    	String url = "/banners/business/save";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

        /**
         * 测试
         */
        {// 测试成功
        	BusinessBannerVo banner = new BusinessBannerVo("guanggao", 22222L, true, "");
            RequestBuilder builder = post(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("banner", JSON.toJSONString(banner))
                ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andReturn();
        }
    }

    @Test
    public void testDelete() throws Exception{

    	// 请求路径
    	String url = "/banners/business/delete";
        Cookie cookie = null;

        /**
         * 登录获取Cookie
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("business", MD5Utils.md5("000001")));

        /**
         * 测试
         */
        {// 测试成功
            RequestBuilder builder = post(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
                .param("bannerId", JSON.toJSONString(bannerId));

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andReturn();
        }
    }
}
























