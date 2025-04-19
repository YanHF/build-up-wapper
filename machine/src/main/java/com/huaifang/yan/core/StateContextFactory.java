/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.core;


import com.huaifang.yan.event.StateEventQueue;

/**
 * 状态上下文工厂
 * 
 * @author allen
 * @version $Id: StateContextFactory.java, v 0.1 2017年9月3日 下午10:33:16 allen Exp $
 */
public interface StateContextFactory<T, BizType, StateValue> {

    /**
     * 创建状态上下文
     * 
     * @param id 上下文ID
     * @param data 上下文业务数据
     * @return
     */
    StateContext<T, BizType, StateValue> createContext(String id, T data);

    /**
     * 创建状态上下文
     *
     * @param id 上下文ID
     * @param data 上下文业务数据
     * @return
     */
    StateContext<T, BizType, StateValue> createContext(String id, boolean isStateValidate, T data);

    /**
     * Create state
     * 
     * @param stateValue 状态
     * @param statePersister 状态持久化服务
     * @param stateEventQueue 状态事件队列
     * @return
     */
    IState<T, BizType, StateValue> createState(StateValue stateValue, BizType bizType, StatePersister<T, BizType, StateValue> statePersister,
                                               StateEventQueue<T, BizType, StateValue> stateEventQueue);
}
