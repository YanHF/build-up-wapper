/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan;

import org.springframework.aop.framework.AdvisedSupport;
import org.springframework.aop.framework.AopProxy;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Field;

/**
 * 
 * @author allen
 * @version $Id: FSMUtils.java, v 0.1 2017年1月17日 下午8:24:02 allen Exp $
 */
public class FSMUtils {

    /** 业务与状态间分隔符 */
    public static final String SPLIT0    = "|";
    /** 业务与业务，状态与状态间分隔符 */
    public static final String SPLIT1    = "^";
    /** 替代字符 */
    public static final String PLACEHOLD = "$";
    /** 状态机处理异常 */
    public static final String FSM_ERROR = "-2";

    /** 
     * 获取 目标对象 
     * @param proxy 代理对象 
     * @return spring 代理的对象
     * @throws Exception 异常
     */
    public static Object getTarget(Object proxy) {
        if (!AopUtils.isAopProxy(proxy)) {
            return proxy;//不是代理对象  
        }
        if (AopUtils.isJdkDynamicProxy(proxy)) {
            return getJdkDynamicProxyTargetObject(proxy);
        } else { //cglib  
            return getCglibProxyTargetObject(proxy);
        }
    }

    /**
     * @param proxy
     * @return
     */
    private static Object getCglibProxyTargetObject(Object proxy) {
        try {
            Field h = proxy.getClass().getDeclaredField("CGLIB$CALLBACK_0");
            h.setAccessible(true);
            Object dynamicAdvisedInterceptor = h.get(proxy);

            Field advised = dynamicAdvisedInterceptor.getClass().getDeclaredField("advised");
            advised.setAccessible(true);

            Object target = ((AdvisedSupport) advised.get(dynamicAdvisedInterceptor)).getTargetSource().getTarget();

            return target;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @param proxy
     * @return
     */
    private static Object getJdkDynamicProxyTargetObject(Object proxy) {
        try {
            Field h = proxy.getClass().getSuperclass().getDeclaredField("h");
            h.setAccessible(true);
            AopProxy aopProxy = (AopProxy) h.get(proxy);

            Field advised = aopProxy.getClass().getDeclaredField("advised");
            advised.setAccessible(true);

            Object target = ((AdvisedSupport) advised.get(aopProxy)).getTargetSource().getTarget();

            return target;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
