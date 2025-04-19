/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.core;

import com.huaifang.yan.OrderStates;
import com.huaifang.yan.event.StateEventQueue;
import com.huaifang.yan.exeption.BusinessException;
import com.huaifang.yan.model.OrderVo;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 订单状态委托代理
 * 
 * @author allen
 * @version $Id: OrderStateDelegate.java, v 0.1 2017年9月3日 下午10:36:42 allen Exp $
 */
@Service("orderStateDelegate")
public class OrderStateDelegate implements StateDelegate<OrderVo, String, OrderStates> {
    /** state context retrieve */
    @Resource(name = "stateContextFactory")
    private StateContextFactory<OrderVo, String, OrderStates> stateContextFactory;

    /** StatePersister */
    @Resource(name = "orderDao")
    private StatePersister<OrderVo, String, OrderStates>      statePersister;

    /** State event queue */
    @Resource(name = "orderStateEventQueue")
    private StateEventQueue<OrderVo, String, OrderStates> stateEventQueue;

    /** 
     * @see StateDelegate#setState(Object, Object, Object, Map)
     */
    @Override
    public void setState(OrderVo order, String toBiz, OrderStates toState, Map<String, Object> properties) throws BusinessException {
        setState(order, toBiz, toState, false, properties);
    }

    /** 
     * @see StateDelegate#setState(Object, Object)
     */
    @Override
    public void setState(OrderVo order, OrderStates toState) throws BusinessException {
        setState(order, toState, false);
    }

    @Override
    public void setState(OrderVo order, String toBiz, OrderStates toState, boolean isStateValidate, Map<String, Object> properties) throws BusinessException {
        // create state context
        StateContext<OrderVo, String, OrderStates> context = stateContextFactory.createContext(String.valueOf(order.hashCode()), isStateValidate, order);
        if (!properties.isEmpty()) {
            context.getProperties().putAll(properties);
        }
        // create state
        IState<OrderVo, String, OrderStates> from = stateContextFactory.createState(order.getOrderState(), OrderConstants.ORDER_BIZ_TYPE, statePersister, stateEventQueue);
        IState<OrderVo, String, OrderStates> to = stateContextFactory.createState(toState, OrderConstants.ORDER_BIZ_TYPE, statePersister, stateEventQueue);
        // set order state
        context.setState(from, to);
    }

    @Override
    public void setState(OrderVo orderVO, OrderStates toState, boolean isStateValidate) throws BusinessException {
        setState(orderVO, OrderConstants.ORDER_BIZ_TYPE, toState, isStateValidate, null);
    }

}
