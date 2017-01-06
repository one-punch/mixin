package testhelper;

import org.dozer.DozerBeanMapper;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import com.wteam.mixin.biz.dao.IBaseDao;

@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration   
(locations ={"classpath:applicationContext.xml","classpath:spring-hibernate.xml","classpath:spring-dozer.xml","classpath:spring-shiro.xml"}) //加载配置文件  
//------------如果加入以下代码，所有继承该类的测试类都会遵循该配置，也可以不加，在测试类的方法上///控制事务，参见下一个实例  
//这个非常关键，如果不加入这个注解配置，事务控制就会完全失效！  
//这里的事务关联到配置文件中的事务控制器（transactionManager = "transactionManager"），同时//指定自动回滚（defaultRollback = true）。这样做操作的数据才不会污染数据库！  
//------------  
@Transactional (transactionManager = "transactionManager")
@Rollback(true)
public class BaseJunit4Test { 

	@Autowired
	protected IBaseDao baseDao;
	@Autowired
	protected DozerBeanMapper mapper;
}  
