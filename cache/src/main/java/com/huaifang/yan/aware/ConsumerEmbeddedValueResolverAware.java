package com.huaifang.yan.aware;

import org.springframework.beans.factory.config.EmbeddedValueResolver;
import org.springframework.context.EmbeddedValueResolverAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringValueResolver;

@Component
public class ConsumerEmbeddedValueResolverAware implements EmbeddedValueResolverAware {
    private EmbeddedValueResolver embeddedValueResolver;

    @Override
    public void setEmbeddedValueResolver(StringValueResolver resolver) {
        this.embeddedValueResolver=embeddedValueResolver;
    }
}
