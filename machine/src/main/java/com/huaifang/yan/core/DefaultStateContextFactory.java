/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.core;


import com.huaifang.yan.event.StateEventQueue;
import org.springframework.stereotype.Service;

/**
 * 缺省状态上下文工厂
 * 
 * @author allen
 * @version $Id: DefaultStateContextFactory.java, v 0.1 2017年9月3日 下午10:34:45 allen Exp $
 */
@Service("stateContextFactory")
public class DefaultStateContextFactory<T, BizType, StateValue> implements StateContextFactory<T, BizType, StateValue> {

    /** 
     * @see StateContextFactory#createContext(String, Object)
     */
    @Override
    public StateContext<T, BizType, StateValue> createContext(String id, T data) {
        return new DefaultStateContext<>(id, data);
    }

    @Override
    public StateContext<T, BizType, StateValue> createContext(String id, boolean isStateValidate, T data) {
        return new DefaultStateContext<>(id, isStateValidate, data);
    }

    /** 
     * @see StateContextFactory#createState(Object, Object, StatePersister, StateEventQueue)
     */
    @Override
    public IState<T, BizType, StateValue> createState(StateValue stateValue, BizType bizType, StatePersister<T, BizType, StateValue> statePersister,
                                                      StateEventQueue<T, BizType, StateValue> stateEventQueue) {
        return new DefaultState<>(stateValue, bizType, statePersister, stateEventQueue);
    }

}
