package com.huaifang.yan.concurrent;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ConsumerThreadFactory implements ThreadFactory {

    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);

    private final String prefix;

    public ConsumerThreadFactory(String namePrefix) {

        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
        this.prefix = namePrefix;
    }

    @Override
    public Thread newThread(Runnable runnable) {
        Thread t = new Thread(group, runnable, prefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
