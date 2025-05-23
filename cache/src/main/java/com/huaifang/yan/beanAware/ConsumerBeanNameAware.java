package com.huaifang.yan.beanAware;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

@Component
public class ConsumerBeanNameAware implements BeanNameAware , BeanPostProcessor, InitializingBean {
    private BeanNameAware beanNameAware;
    @Override
    public void setBeanName(String s) {
        System.out.println("setBeanNameAware"+s);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    @Override
    public void afterPropertiesSet() throws Exception {

    }
}
