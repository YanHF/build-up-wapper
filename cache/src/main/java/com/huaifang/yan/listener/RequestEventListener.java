package com.huaifang.yan.listener;

import com.huaifang.yan.event.RequestEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service
public class RequestEventListener implements ApplicationListener<RequestEvent> {
    private Logger logger = (Logger) LoggerFactory.getLogger(this.getClass());
    @Async
    @Override
    public void onApplicationEvent(RequestEvent requestEvent) {

        logger.info("用户({}) 发起请求", requestEvent.getPublishName());

    }
}
