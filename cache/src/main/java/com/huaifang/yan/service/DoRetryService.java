package com.huaifang.yan.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DoRetryService {

    @Retryable(value = Exception.class, maxAttempts = 4, backoff = @Backoff(delay = 2000L, multiplier = 1.5))
    public boolean doRetry(boolean isRetry) throws Exception {
        log.info("开始通知下游系统");
        log.info("通知下游系统");
        if (isRetry) {
            throw new RuntimeException("通知下游系统异常");
        }
        return true;
    }
}

