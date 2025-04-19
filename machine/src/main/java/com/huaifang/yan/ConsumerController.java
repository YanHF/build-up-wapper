package com.huaifang.yan;


import com.huaifang.yan.core.OrderConstants;
import com.huaifang.yan.core.StateDelegate;
import com.huaifang.yan.exeption.BusinessException;
import com.huaifang.yan.model.OrderVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;

@Slf4j
@RestController
public class ConsumerController {
    private static final BusinessExceptionErrorFactory  errorFactory =new BusinessExceptionErrorFactory();

    @Resource
    private StateDelegate stateDelegate;
    @RequestMapping("/consumer")
    public String index() {


        try {
            OrderVo orderVo = new OrderVo();
            orderVo.setOrderState(OrderStates.PENDING_PAYMENT);
            stateDelegate.setState(orderVo, OrderConstants.ORDER_BIZ_TYPE,OrderStates.PAID,true,new HashMap<>());

            throw new BusinessException(errorFactory.systemError("A"));
        }
        catch (Exception e) {
            log.error("请求失败",e);
        }

        return null;
    }

    @RequestMapping("/add")
    public String add() {

        return null;
    }

}
