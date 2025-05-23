package com.huaifang.yan.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LoggingInvocationHandler implements InvocationHandler {
    // 被代理的对象引用
    private final Sellable target;

    public LoggingInvocationHandler(Sellable target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 方法调用前的操作：记录日志
        System.out.println("开始销售房源操作...");

        // 调用目标对象的方法，并返回结果
        Object result = method.invoke(target, args);

        // 方法调用后的操作：再次记录日志
        System.out.println("完成销售房源操作.");

        return result;
    }
}

