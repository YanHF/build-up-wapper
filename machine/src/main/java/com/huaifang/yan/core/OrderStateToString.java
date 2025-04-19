/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.core;

import com.huaifang.yan.OrderStates;
import com.huaifang.yan.model.OrderVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * 订单状态字符串转换器
 * 
 * @author allen
 * @version $Id: OrderStateToString.java, v 0.1 2017年9月3日 下午9:42:32 allen Exp $
 */
@Service("orderStateToString")
public class OrderStateToString implements StateToString<OrderVo, String, OrderStates> {

    /** 
     * @see StateToString#stateToString(IState)
     */
    @Override
    public String stateToString(IState<OrderVo, String, OrderStates> state) {
        if (state == null) {
            return StringUtils.EMPTY;
        }
        OrderStates stateValue = state.getStateValue();
        return stateValue != null ? stateValue.name() : StringUtils.EMPTY;
    }

    /** 
     * @see StateToString#bizToString(Object)
     */
    @Override
    public String bizToString(String biz) {
        return biz;
    }

}
