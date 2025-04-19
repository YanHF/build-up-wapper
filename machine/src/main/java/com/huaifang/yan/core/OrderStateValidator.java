/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.core;


import com.huaifang.yan.OrderStates;
import com.huaifang.yan.annotation.State;
import com.huaifang.yan.event.ActorRegistryValidator;
import org.springframework.stereotype.Service;

/**
 * 订单状态校验器
 * 
 * @author allen
 * @version $Id: OrderStateValidator.java, v 0.1 2017年9月3日 下午11:39:11 allen Exp $
 */
@Service("orderStateValidator")
public class OrderStateValidator implements ActorRegistryValidator {

    /** 
     * @see ActorRegistryValidator#validate(State)
     */
    @Override
    public boolean validate(State state) {
        OrderStates[] froms = state.from();
        for (OrderStates from : froms) {
            if (from == null) {
                return false;
            }
        }
        OrderStates[] tos = state.to();
        for (OrderStates to : tos) {
            if (to == null) {
                return false;
            }
        }
        return true;
    }

}
