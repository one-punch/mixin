package testhelper;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;

import org.dozer.DozerBeanMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.alibaba.fastjson.JSON;
import com.wteam.mixin.biz.dao.IBaseDao;

import test.hibernate.factory.MockLinkerFactory;

/**
 * web集成测试基类<br>
 * 1、提供测试的配置信息（使用注解）<br>
 * 2、集成测试工具方法
 *
 * @author benko
 * @version 2016年6月17日
 * @see BaseControllerTest
 * @since
 */
@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration(value = "src/main/webapp")
@ContextHierarchy({
    @ContextConfiguration(name = "parent", locations = {
        "classpath:applicationContext.xml",
        "classpath:spring-hibernate.xml",
        "classpath:spring-shiro-web.xml",
        "classpath:spring-dozer.xml"}),
    @ContextConfiguration(name = "child", locations = "classpath:spring-servlet.xml")})
@Transactional(transactionManager = "transactionManager")
@Rollback(true)
public class BaseControllerTest {

    @Autowired
    protected IBaseDao baseDao;

    @Autowired
    protected DozerBeanMapper mapper;

    protected MockLinkerFactory linkerFactory = new MockLinkerFactory();

    /**
     * Description: 登录后，返回登录的cookie值<br>
     *
     * @param mockMvc 模拟MVC环境
     * @param loginUrl 登录路径
     * @param userVo 用户信息
     * @return Cookie
     * @throws Exception
     * @see
     */
    protected Cookie loginReturnCookie(MockMvc mockMvc,String loginUrl,Object userVo) throws Exception {
        // 登录
        MvcResult result1 = mockMvc.perform(post(loginUrl).param("user", JSON.toJSONString(userVo)))
            .andDo(print()).andExpect(jsonPath("$.isSuccess").value(true))
            .andReturn();
        // 解析 cookie字符串
        String cookieValue=   (String)result1.getResponse().getHeaderValue("Set-Cookie");
        String[] setCookieArr = cookieValue.split(";")[0].split("=");
        // 创建 cookie
        return new Cookie(setCookieArr[0], setCookieArr[1]);
    }

    /**
     * Description: 字符集转码 过滤器<br>
     *
     * @return 过滤器
     * @see
     */
    protected Filter characterEncodingFilter() {
        // 字符集转码
        CharacterEncodingFilter encodingFilter = new CharacterEncodingFilter();
        encodingFilter.setEncoding("UTF-8");
        encodingFilter.setForceEncoding(true);

        return encodingFilter;
    }

    /**
     * Description: shiro权限 过滤器<br>
     *
     * @param wac spring web 上下文
     * @return 过滤器
     * @throws ServletException 异常
     * @see
     */
    protected Filter shiroFilter(WebApplicationContext wac) throws ServletException {
        DelegatingFilterProxy filterProxy = new DelegatingFilterProxy("shiroFilter",wac);
        MockFilterConfig filterConfig = new MockFilterConfig(wac.getServletContext());
        filterProxy.init(filterConfig);
        return filterProxy;
    }
}
