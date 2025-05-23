package com.huaifang.yan.aware;

import org.springframework.context.ApplicationEvent;

public class ConsumerApplicationEvent extends ApplicationEvent {
    public ConsumerApplicationEvent(Object source) {
        super(source);

    }

}
