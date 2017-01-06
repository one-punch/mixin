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
public class TestOrderSettlementTask{

    /** 线程调度器 */
    @Autowired
    ThreadPoolTaskExecutor taskExector;
    
    @Autowired
    OrderSettlementTask orderSettlementTask;
    
    @Test
    public void test() throws InterruptedException {

        orderSettlementTask.execute();
        for (int i = 0; i < 10; i++ ) {
            
            OrderSettlementVo settlementPo = new OrderSettlementVo();
            settlementPo.setId((long)i);
            Thread.sleep(2);
            settlementPo.setCreateTime(new Date());
            orderSettlementTask.add(settlementPo);
        }
        //orderSettlementTask.run();
        System.out.println(taskExector.getActiveCount());

        Thread.sleep(10*40);
    }
    
    @Test
    public void testAlipay() throws InterruptedException {

        Thread.sleep(10*40);
    }

}
