package com.huaifang.yan.provider;

import org.apache.dubbo.common.extension.SPI;

/**
 * @author user
 */
@SPI
public interface DataProvider {

    void printf(String key, Object value);
}
