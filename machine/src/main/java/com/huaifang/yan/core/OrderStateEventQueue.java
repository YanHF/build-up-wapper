/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.core;

import com.huaifang.yan.OrderStates;

import com.huaifang.yan.event.ActorRegistryValidator;
import com.huaifang.yan.event.ConcurrentStateEventQueue;
import com.huaifang.yan.model.OrderVo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 订单状态事件队列实现
 * 
 * @author allen
 * @version $Id: OrderStateEventQueue.java, v 0.1 2017年9月13日 上午11:41:30 allen Exp $
 */
@Service("orderStateEventQueue")
public class OrderStateEventQueue extends ConcurrentStateEventQueue<OrderVo, String, OrderStates> {
    /** 订单状态字符串转换器 */
    @Resource(name = "orderStateToString")
    private StateToString<OrderVo, String, OrderStates> stateToString;

    /** 订单状态校验器 */
    @Resource(name = "orderStateValidator")
    private ActorRegistryValidator actorRegistryValidator;

    /** 
     * @see AbstractStateEventQueue#getStateToString()
     */
    @Override
    protected StateToString<OrderVo, String, OrderStates> getStateToString() {
        return stateToString;
    }

    /** 
     * @see AbstractStateEventQueue#getActorRegistryValidator()
     */
    @Override
    protected ActorRegistryValidator getActorRegistryValidator() {
        return actorRegistryValidator;
    }
}
