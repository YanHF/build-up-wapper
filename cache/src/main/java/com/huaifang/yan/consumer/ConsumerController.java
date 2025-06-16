package com.huaifang.yan.consumer;

import com.huaifang.yan.event.CouponEvent;
import com.huaifang.yan.event.RequestEvent;
import com.huaifang.yan.model.OrderEvents;
import com.huaifang.yan.model.OrderStates;
import com.huaifang.yan.provider.DataProvider;
import com.huaifang.yan.service.DoRetryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.extension.ExtensionLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.statemachine.StateMachine;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.ServiceLoader;

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

        ExtensionLoader<DataProvider> extensionLoader = ExtensionLoader.getExtensionLoader(DataProvider.class);

        DataProvider dataProvider= extensionLoader.getExtension("yanhf");
        dataProvider.printf("11","22");

 /*       ServiceLoader<DataProvider> orderStatesServiceLoader = ServiceLoader.load(DataProvider.class, Thread.currentThread().getContextClassLoader());

        orderStatesServiceLoader.iterator().forEachRemaining(dataProvider -> {
            dataProvider.printf("1","2");
        });
        while (orderStatesServiceLoader.iterator().hasNext()) {
            DataProvider dataProvider = orderStatesServiceLoader.iterator().next();

            System.out.println(dataProvider.getClass().getName());
        }
*/
        return "OK";
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
