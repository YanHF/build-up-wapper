package com.huaifang.yan.event;

import org.springframework.context.ApplicationEvent;

public class RequestEvent extends ApplicationEvent {


    private String publishName;

    public String getPublishName() {
        return publishName;
    }

    public RequestEvent(Object source) {
        super(source);
    }

    public RequestEvent(Object source, String publishName) {
        super(source);
        this.publishName = publishName;
    }
}
