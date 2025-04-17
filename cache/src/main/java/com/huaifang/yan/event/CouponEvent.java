package com.huaifang.yan.event;


import org.springframework.context.ApplicationEvent;

public class CouponEvent extends ApplicationEvent {
    private String publishName;

    public String getPublishName() {
        return publishName;
    }

    public CouponEvent(Object source) {
        super(source);
    }

    public CouponEvent(Object source, String publishName) {
        super(source);
        this.publishName = publishName;
    }
}
