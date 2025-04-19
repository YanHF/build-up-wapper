package com.huaifang.yan.Actor;

import com.huaifang.yan.OrderStates;
import com.huaifang.yan.annotation.State;
import com.huaifang.yan.core.AbstractOrderPostTransitActor;
import com.huaifang.yan.core.AbstractOrderPreTransitActor;
import com.huaifang.yan.core.OrderConstants;
import com.huaifang.yan.event.PostTransitEvent;
import com.huaifang.yan.exeption.BusinessException;
import com.huaifang.yan.model.OrderVo;
import org.springframework.stereotype.Service;

@State(fromBiz = OrderConstants.ORDER_BIZ_TYPE,toBiz = OrderConstants.ORDER_BIZ_TYPE,from = OrderStates.PENDING_PAYMENT,to = OrderStates.PAID)
@Service
public class PendingPaymentPosActor extends AbstractOrderPostTransitActor {

    @Override
    public boolean execute(PostTransitEvent<OrderVo, String, OrderStates> event) throws BusinessException {
        OrderVo orderVo=event.getContext().getData();
        return true;
    }

    @Override
    public boolean isAsync(PostTransitEvent<OrderVo, String, OrderStates> event) {
        return false;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
