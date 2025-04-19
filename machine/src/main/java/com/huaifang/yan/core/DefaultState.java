/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.core;


import com.huaifang.yan.event.PostTransitEvent;
import com.huaifang.yan.event.PreTransitEvent;
import com.huaifang.yan.event.StateEventQueue;
import com.huaifang.yan.exeption.BusinessException;
import org.apache.commons.lang3.StringUtils;

/**
 * 缺省状态实现
 * 
 * @author allen
 * @version $Id: DefaultState.java, v 0.1 2017年9月3日 下午8:27:56 allen Exp $
 */
public class DefaultState<T, BizType, StateValue> implements IState<T, BizType, StateValue> {

    /** 状态值 */
    private StateValue                              stateValue;
    /** 业务类型 */
    private BizType                                 bizType;
    /** StatePersister */
    private StatePersister<T, BizType, StateValue>  statePersister;
    /** State event queue */
    private StateEventQueue<T, BizType, StateValue> stateEventQueue;

    /**
     * @param stateValue 状态值
     * @param bizType 业务类型
     * @param statePersister 状态持久化服务
     * @param stateEventQueue 状态事件队列
     */
    public DefaultState(StateValue stateValue, BizType bizType, StatePersister<T, BizType, StateValue> statePersister, StateEventQueue<T, BizType, StateValue> stateEventQueue) {
        super();
        this.stateValue = stateValue;
        this.bizType = bizType;
        this.statePersister = statePersister;
        this.stateEventQueue = stateEventQueue;
    }

    /** 
     * @see IState#getStateValue()
     */
    @Override
    public StateValue getStateValue() {
        return stateValue;
    }

    /** 
     * @see IState#handle(StateContext)
     */
    @Override
    public void handle(StateContext<T, BizType, StateValue> context) throws BusinessException
    {
        T data = context.getData();
        if (data == null) {
            throw new IllegalArgumentException("Data cannot be null");
        }

        try {
            // 触发状态变更前事件
            PreTransitEvent<T, BizType, StateValue> preEvent = new PreTransitEvent<>(this, context);
          stateEventQueue.multicast(preEvent);

            // persist update state
            if (context.isStateValidate()) {
                statePersister.updateState(data, context.getFromBiz(), context.getFromState().getStateValue(), bizType, stateValue);
            } else {
                statePersister.updateState(data, bizType, stateValue);
            }
            // 触发状态变更后事件
            PostTransitEvent<T, BizType, StateValue> postEvent = new PostTransitEvent<>(this, context);
            stateEventQueue.multicast(postEvent);
        } catch (Exception e) {
            throw new BusinessException("状态机处理异常");
        }
    }

    /** 
     * @see IState#getBizType()
     */
    @Override
    public BizType getBizType() {
        return bizType;
    }
}
