/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.core;

import com.huaifang.yan.OrderStates;
import com.huaifang.yan.event.PostTransitEvent;
import com.huaifang.yan.event.StateEvent;
import com.huaifang.yan.model.OrderVo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 订单状态变更后的Actor
 * 
 * @author allen
 * @version $Id: AbstractOrderPostTransitActor.java, v 0.1 2017年9月4日 上午12:40:11 allen Exp $
 */
public abstract class AbstractOrderPostTransitActor implements Actor<PostTransitEvent<OrderVo, String, OrderStates>, OrderVo, String, OrderStates> {
    /** LOGGER */
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /** 
     * @see Actor#isSupported(StateEvent)
     */
    @Override
    public boolean isSupported(StateEvent<OrderVo, String, OrderStates> event) {
        return event instanceof PostTransitEvent;
    }

    /** 
     * @see Orderable#getOrder()
     */
    @Override
    public int getOrder() {
        return 0;
    }

    /** 
     * @see Actor#getActorType()
     */
    @Override
    public ActorType getActorType() {
        return ActorType.POST;
    }

}
