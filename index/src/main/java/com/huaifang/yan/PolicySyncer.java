package com.huaifang.yan;

import com.huaifang.yan.main.PolicySync;
import com.huaifang.yan.model.CallBackEventWrapper;
import com.huaifang.yan.model.PolicyConfig;
import com.ly.flight.xpgs.base.enums.PolicySyncTypeEnum;
import com.ly.flight.xpgs.policy.model.IndexKey;


import java.util.function.Consumer;
import java.util.function.Function;

/**
 * 政策同步器
 *
 * @author xianyan.geng
 * @version PolicySyncer, v 0.1 2020/10/22 09:58 xianyan.geng Exp $
 */
public interface PolicySyncer<T extends IndexKey> extends PolicySync {

    /**
     * 遍历数据
     * @param biFunction
     * @param <T>
     */
    <T> void forEach(Function<T, Boolean> biFunction);

    /**
     * 根据ID获取数据
     * @param id
     * @param <T>
     * @return
     */
    <T> T get(String id);

    /**
     * 获取指定政策类型Rocksdb对应的条数
     * @return
     */
    long getRocksdbKeySize();

    /**
     * 追加回调事件
     * @param consumer
     * @param <T>
     */
    <T> void addCallbackEvent(Consumer<CallBackEventWrapper<T>> consumer);

    /**
     * 销毁
     */
    default void destroy(){
        // no-op
    }

    /**
     * 获取政策同步类型
     * @return
     */
    PolicySyncTypeEnum getPolicySyncType();

    /**
     * Gets policy config.
     *
     * @return the policy config
     */
    PolicyConfig getPolicyConfig();

    /**
     * 重建 DB
     */
    void rebuild() throws Exception;

    /**
     * 初始化 DB
     */
    void init() throws Exception;

    /**
     * 关闭线程池（同步政策/上报进度/回调事件）
     */
    default void executorShutdown() {
        // no-op
    }

    /**
     * 线程是否关闭成功
     *
     * @return the boolean
     */
    default boolean isExecutorTerminated() {
        return true;
    }

    /**
     * 关闭数据库
     */
    default void close() {

    }
}
