package com.huaifang.yan.model;

import lombok.Data;

import java.util.Queue;
import java.util.concurrent.*;

/**
 * 线程池组包装对象
 *
 * @author xianyan.geng
 * @version ExecutorPair, v 0.1 2020/10/21 11:32 xianyan.geng Exp $
 */
@Data
public class ExecutorPair {
    /** 回调事件排队队列 */
    private Queue                    queue;
    /** 回调事件线程池 */
    private ExecutorService          callBackEventExecutor;
    /** 查询政策专用延迟线程池  */
    private ScheduledExecutorService searchPolicyExecutor;
    /** 上报版本专用延迟线程池  */
    private ScheduledExecutorService uploadRevisionExecutor;

    public ExecutorPair(int threadSize) {
        this.queue = new LinkedBlockingQueue();
        this.callBackEventExecutor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, (BlockingQueue<Runnable>) this.queue);
        this.searchPolicyExecutor = Executors.newScheduledThreadPool(threadSize);
        this.uploadRevisionExecutor = Executors.newScheduledThreadPool(threadSize);
    }
}
