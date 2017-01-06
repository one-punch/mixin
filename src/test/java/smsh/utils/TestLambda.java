package smsh.utils;

import java.util.function.BiFunction;

import org.junit.Test;

public class TestLambda {

    @Test
    public void test_suffix() {
        
        BiFunction<String, String, String> addSuffix = (source, suffix) -> "".equals(source) ? "" : source + suffix;
        
        System.out.println(addSuffix.apply("source", ".suffix"));
        
    }
}
