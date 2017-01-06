package smsh.task;

import java.util.Date;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.wteam.mixin.model.po.OrderSettlementPo;
import com.wteam.mixin.model.vo.OrderSettlementVo;
import com.wteam.mixin.task.OrderSettlementTask;

import testhelper.BaseJunit4Test;

//public class TestOrderSettlementTask extends BaseJunit4Test{
@RunWith(SpringJUnit4ClassRunner.class)  //使用junit4进行测试  
@ContextConfiguration   
(locations ={"classpath:applicationContext-task.xml"}) //加载配置文件  
public class TestAlipayTask{


    
    @Test
    public void testAlipay() throws InterruptedException {

        Thread.sleep(10*4000);
    }

}
