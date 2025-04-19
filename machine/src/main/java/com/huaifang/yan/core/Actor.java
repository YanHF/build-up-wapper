/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.core;


import com.huaifang.yan.event.StateEvent;
import com.huaifang.yan.exeption.BusinessException;

import java.util.EventListener;

/**
 * 状态事件监听器
 * 
 * @author allen
 * @version $Id: Actor.java, v 0.1 2017年9月3日 下午2:58:55 allen Exp $
 */
public interface Actor<E extends StateEvent<T, BizType, StateValue>, T, BizType, StateValue> extends EventListener, Orderable {

    /**
     * 事件处理接口
     * 
     * @param event 事件
     */
    boolean execute(E event) throws BusinessException;

    /**
     * 是否支持处理该事件类型
     * 
     * @param event 事件
     * @return Actor是否支持接收的状态事件，若支持则会执行业务处理
     */
    boolean isSupported(StateEvent<T, BizType, StateValue> event);

    /**
     * 是否异步执行
     * 
     * @param event 事件
     * @return 是否异步处理状态变换
     */
    boolean isAsync(E event);

    /**
     * Actor类型
     * 
     * @return
     */
    ActorType getActorType();

}
