package com.wteam.mixin.function;

/**
 * 可抛出异常的处理函数
 * @author benko
 *
 */
@FunctionalInterface
public interface IHandle {

    /**
     *
     * @throws Exception
     */
    void handle() throws Exception;
}
