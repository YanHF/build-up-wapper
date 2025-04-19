/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.event;

import com.huaifang.yan.core.Actor;

/**
 * 状态事件监听器注册
 * 
 * @author allen
 * @version $Id: StateEventRegistration.java, v 0.1 2017年9月3日 下午3:02:18 allen Exp $
 */
public interface StateEventRegistration<T, BizType, StateValue> {

    /**
     * 注册事件监听器
     * 
     * @param actor 事件监听器
     */
    void register(Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> actor);

    /**
     * 注销事件监听器
     * 
     * @param actor 事件监听器
     */
    void unregister(Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> actor);

    /**
     * 注销所有事件监听器
     */
    void unregisterAll();
}
