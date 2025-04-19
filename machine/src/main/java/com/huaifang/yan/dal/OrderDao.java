package com.huaifang.yan.dal;

import com.huaifang.yan.OrderStates;
import com.huaifang.yan.core.StatePersister;
import com.huaifang.yan.exeption.BusinessException;
import com.huaifang.yan.model.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("orderDao")
@Slf4j
public class OrderDao implements StatePersister<OrderVo,String, OrderStates> {
    @Override
    public void updateState(OrderVo data, String toBiz, OrderStates toState) throws BusinessException {
        log.info("update order state");
    }

    @Override
    public void updateState(OrderVo data, String fromBiz, OrderStates fromState, String toBiz, OrderStates toState) throws BusinessException {
        log.info("update order state");
    }
}
