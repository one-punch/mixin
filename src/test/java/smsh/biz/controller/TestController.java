package smsh.biz.controller;


import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.model.po.PermissionPo;
import com.wteam.mixin.model.po.RolePo;
import com.wteam.mixin.model.po.UserPo;
import com.wteam.mixin.model.vo.UserVo;
import com.wteam.mixin.utils.MD5Utils;

import test.hibernate.factory.MockGenerateFactory;
import test.hibernate.link.LinkHandler;

import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

import testhelper.BaseControllerTest;
import testhelper.TestActionResultJsonHelper;


public class TestController extends BaseControllerTest {
    /** springmvc 上下文*/
    @Autowired
    private WebApplicationContext wac;

    /** web测试环境*/
    private MockMvc mockMvc;

    private TestActionResultJsonHelper jsonHelper;

    /**
     * 向数据库测试使用的插入数据
     * @throws ServletException 
     */
    @Before
    public void setUp() throws ServletException {
        
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
//            .alwaysExpect( content().contentType("application/json;charset=UTF-8"))
            .addFilters(super.characterEncodingFilter())
            .addFilter(super.shiroFilter(wac))
            .build();
        jsonHelper = new TestActionResultJsonHelper();
        // 必写
        LinkHandler.setBaseDao(baseDao);
        MockGenerateFactory generateFactory = new MockGenerateFactory(baseDao, mapper);

        /**
         *  shiro集成测试时设置权限，不使用时可将其注释，减少测试时间
         */
        // 获取角色
        RolePo supermanagerRole = generateFactory.role.generate(0);  
        RolePo userRole = generateFactory.role.generate(1);
        List<RolePo> supermanger_user = Arrays.asList(supermanagerRole, userRole);

        // 保存权限关联角色
        LinkHandler.manyToMany(new PermissionPo("/test/user"), userRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/test/supermanager"), supermanagerRole, linkerFactory.permissionToRole);
        LinkHandler.manyToMany(new PermissionPo("/test/all"), supermanger_user, linkerFactory.permissionToRole);

        // 保存用户关联角色
        UserPo userPo1 = generateFactory.user.generate(0);
        LinkHandler.manyToMany(userPo1, userRole, linkerFactory.userToRole);
        UserPo userPo2 = generateFactory.user.generate(1);
        LinkHandler.manyToMany(userPo2, supermanagerRole, linkerFactory.userToRole);
        /**
         * 测试一个接口时，请注释掉其它生成数据
         */
        
    }
    /**
     * 向数据库插入数据，不回滚
     */
    @Test
    @Rollback(false)
    public void testNotRollback() {}
    
    @Test
    public void test_jsonToObject()
        throws Exception {
        UserVo userVo = new UserVo();
        userVo.setCredential("asdfasdf");
        userVo.setPrincipal("asdfasdfasd");

        MvcResult result = mockMvc.perform(
            post("/test/jsonToObject").param("user", JSON.toJSONString(userVo))).andDo(print())
            // .andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        jsonHelper.setResult(result.getResponse().getContentAsString());

        assertTrue(jsonHelper.getResultInfo(), jsonHelper.isSuccess());
    }
    
    
    /**
     * 
     * Description: 测试登录后才可访问的URL<br>
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
    public void testShiro() throws Exception {
        // 请求路径 
        String url = "/test/user";
        Cookie cookie = null;
        
        /**
         * 用户登录
         */
        cookie = super.loginReturnCookie(mockMvc, "/login", new UserVo("user", MD5Utils.md5("000001")));

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

        {// 测试失败
        	url = "/test/supermanager";
            RequestBuilder builder = get(url)
                .cookie(cookie)// 请求时设置cookie保持登录状态
            // .param("category", category + "")
            ;

            MvcResult result = mockMvc.perform(builder)
                .andDo(print())
                .andExpect(redirectedUrl("/unauthorized"))
                .andReturn();
        }
    }
    
    @Test
    public void testREST() throws Exception{
    	RequestBuilder requestBuilder = get("/test/testREST/1");
    	mockMvc.perform(requestBuilder)
    		.andDo(print())
    		.andExpect(jsonPath("$.isSuccess").value(true))
    		.andReturn();
    }
}
