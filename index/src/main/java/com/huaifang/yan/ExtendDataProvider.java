package com.huaifang.yan;

import com.huaifang.yan.model.CallBackEventWrapper;
import com.ly.flight.xpgs.policy.model.IndexKey;


import java.util.Collection;
import java.util.function.Consumer;

/**
 * 政策同步扩展数据提供者
 *
 * @author xianyan.geng
 * @version ExtendDataSource, v 0.1 2020/11/16 18:36 xianyan.geng Exp $
 */
public interface ExtendDataProvider<T extends IndexKey> {

    /**
     * 拉取全量有效数据
     * @return
     */
    Collection<T> queryFullData();

    /**
     * 单条增量数据变化回调通知
     * @param consumer
     */
    void addCallbackEvent(Consumer<CallBackEventWrapper<T>> consumer);

    /**
     * 全量数据变化回调通知
     * @param consumer
     */
    void addFullCallbackEvent(Consumer<CallBackEventWrapper<T>> consumer);

    /**
     * 是否支持
     * @param policyType
     * @return true-支持
     */
    boolean isSupported(String policyType);
}
