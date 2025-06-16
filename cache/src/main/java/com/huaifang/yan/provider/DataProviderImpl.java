package com.huaifang.yan.provider;

import org.apache.dubbo.common.extension.SPI;


public class DataProviderImpl implements DataProvider {
    @Override
    public void printf(String key, Object value) {
        System.out.println(key + ":" + value);
    }
}
