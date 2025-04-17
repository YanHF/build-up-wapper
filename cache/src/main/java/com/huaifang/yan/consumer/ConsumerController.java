package com.huaifang.yan.consumer;

import com.huaifang.yan.event.CouponEvent;
import com.huaifang.yan.event.RequestEvent;
import com.huaifang.yan.model.OrderEvents;
import com.huaifang.yan.model.OrderStates;
import com.huaifang.yan.service.DoRetryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
public class ConsumerController  implements ApplicationEventPublisherAware {

    @Autowired
    private StateMachine<OrderStates, OrderEvents> stateMachine;


    private ApplicationEventPublisher applicationEventPublisher;

    @Resource
    private DoRetryService retryService;

    @RequestMapping("/consumer")
    public String index() {
        applicationEventPublisher.publishEvent(new RequestEvent(this,"YAN"));
        applicationEventPublisher.publishEvent(new CouponEvent(this,"LISI"));

        try {
            retryService.doRetry(true);
        }
        catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    @RequestMapping("/add")
    public String add() {

        stateMachine.sendEvent(OrderEvents.PAY);
        return null;
    }


    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }
}
