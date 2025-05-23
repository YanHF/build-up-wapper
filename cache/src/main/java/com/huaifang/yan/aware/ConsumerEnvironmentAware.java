package com.huaifang.yan.aware;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class ConsumerEnvironmentAware implements EnvironmentAware {
    private Environment environment;
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
