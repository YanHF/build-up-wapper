package com.huaifang.yan.main;


import com.huaifang.yan.model.PolicySearchWrapper;
import com.huaifang.yan.rocksdb.RocksDbStore;

/**
 * 政策同步
 *
 * @author xianyan.geng
 * @version PolicySync, v 0.1 2020/10/22 10:14 xianyan.geng Exp $
 */
public interface PolicySync {
    /**
     * 触发定时拉取政策任务
     */
    void start();

    /**
     * 关闭线程池（同步政策/上报进度/回调事件）
     */
    void executorShutdown();

    /**
     * 线程是否关闭成功
     *
     * @return the boolean
     */
    boolean isExecutorTerminated();

    /**
     * 关闭数据库
     */
    void close();

    /**
     * 替换 store
     *
     * @param store RocksDbStore
     */
    default void replaceStore(RocksDbStore store) {
        // no-loop
    }

    /**
     * Replace state pair.
     *
     * @param statePair the state pair
     */
    default void updateStatePair(PolicySearchWrapper.ProcessorStatePair statePair) {
        // no-loop
    }

    /**
     * 政策同步/上报 任务是否已暂停
     *
     * @return the boolean
     */
    boolean isSyncStop();
}
