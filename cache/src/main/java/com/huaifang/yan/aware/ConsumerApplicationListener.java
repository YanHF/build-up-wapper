package com.huaifang.yan.aware;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ConsumerApplicationListener implements ApplicationListener<ConsumerApplicationEvent> {

    @Override
    public void onApplicationEvent(ConsumerApplicationEvent event) {
        System.out.println(event.getSource());
    }
}
