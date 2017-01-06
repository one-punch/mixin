package smsh.utils;

import org.junit.Test;

import com.wteam.mixin.model.query.IQuery;
import com.wteam.mixin.model.query.TrafficPlanQuery;

public class TestQuery {
    
    @Test
    public void test_sorted() {
        IQuery query = new TrafficPlanQuery();
        
        //query.putSortField("a").putSortField("b");
        
        System.out.println(query.hqlSorted(""));
        System.out.println(query.hqlSorted("plan"));
        
    }

}
