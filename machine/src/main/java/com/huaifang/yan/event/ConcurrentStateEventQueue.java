/**
 * LY.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package com.huaifang.yan.event;


import com.huaifang.yan.core.Actor;

import javax.annotation.Resource;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author allen
 * @version $Id: ConcurrentStateEventQueue.java, v 0.1 2017年9月3日 下午3:12:01 allen Exp $
 */
public abstract class ConcurrentStateEventQueue<T, BizType, StateValue> extends AbstractStateEventQueue<T, BizType, StateValue> {

    private Executor executor;
    /**
     * Asynchronous executor

    /** 
     */
    @Override
    public void multicast(StateEvent<T, BizType, StateValue> event) {
        boolean result = true;
        for (final Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> actor : getAllActors(event)) {
            Executor executor = getTaskExecutor();

            if (!result) {
                break;
            }

            if (actor.isAsync(event)) {
                // async
                executor.execute(new AnonymityEventHandler(event, actor));
            } else {
                // sync
                result = doExecute(event, actor);
            }
        }
    }

    /**
     * 获取任务执行器
     * 
     * @return 线程执行器
     */
    public Executor getTaskExecutor() {
        if (executor == null) {
            executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, new SynchronousQueue<>(), new NamedThreadFactory("fsm-actor", true));
        }
        return executor;
    }

    /**
     * @param event 状态事件
     * @param actor 状态关联的业务执行者
     * @return
     */
    private boolean doExecute(StateEvent<T, BizType, StateValue> event, Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> actor) {
        try {
            return actor.execute(event);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     *
     * @author allen
     * @version $Id: DefaultStateEventQueue.java, v 0.1 2017年9月3日 下午3:14:48 allen Exp $
     */
    private static class NamedThreadFactory implements ThreadFactory {
        /** 线程数 */
        private final AtomicInteger mThreadNum = new AtomicInteger(1);
        /** 线程前缀 */
        private final String        mPrefix;
        /** 是否是deamon线程 */
        private final boolean       mDaemo;
        /** 线程组 */
        private final ThreadGroup   mGroup;

        /**
         * 构造器
         *
         * @param prefix 线程前缀
         * @param daemo 是否deamo线程
         */
        public NamedThreadFactory(String prefix, boolean daemo) {
            mPrefix = prefix + "-thread-";
            mDaemo = daemo;
            SecurityManager s = System.getSecurityManager();
            mGroup = (s == null) ? Thread.currentThread().getThreadGroup() : s.getThreadGroup();
        }

        /**
         * @see ThreadFactory#newThread(Runnable)
         */
        @Override
        public Thread newThread(Runnable runnable) {
            String name = mPrefix + mThreadNum.getAndIncrement();
            Thread ret = new Thread(mGroup, runnable, name, 0);
            ret.setDaemon(mDaemo);
            return ret;
        }
    }

    /**
     * 异步事件处理
     *
     * @author allen
     * @version $Id: DefaultStateEventQueue.java, v 0.1 2017年9月3日 下午3:13:58 allen Exp $
     */
    private final class AnonymityEventHandler implements Runnable {
        /** 事件 */
        private StateEvent<T, BizType, StateValue>                                event;

        /** 事件监听器 */
        private Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> listener;

        /**
         * 构造函数
         *
         * @param event 事件
         * @param listener 事件监听器
         */
        public AnonymityEventHandler(StateEvent<T, BizType, StateValue> event, Actor<StateEvent<T, BizType, StateValue>, T, BizType, StateValue> listener) {
            super();
            this.event = event;
            this.listener = listener;
        }

        /**
         * 事件处理
         *
         * @see Runnable#run()
         */
        @Override
        public void run() {
            try {
                listener.execute(event);
            } catch (Exception e) {
                logger.error("[Process a event, failure] event: " + event.getClass().getName() + ":" + event.getId(), e);
            }
        }
    }
}
