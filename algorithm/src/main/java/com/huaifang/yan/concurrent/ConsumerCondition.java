package com.huaifang.yan.concurrent;

import sun.misc.Unsafe;

import java.lang.reflect.Field;

/**
 *
 */
public class ConsumerCondition {


    private volatile int state;

    private static  Unsafe unsafe;

    private static  long stateOffset;

    static {
        try {
            Field field = Unsafe.class.getDeclaredField("theUnsafe");
            field.setAccessible(true);
            unsafe = (Unsafe) field.get(null);

            stateOffset = unsafe.objectFieldOffset
                    (ConsumerCondition.class.getDeclaredField("state"));
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean tryAcquire() {
        return unsafe.compareAndSwapInt(this, stateOffset, 0, 1);
    }

    public boolean tryRelease() {
        return unsafe.compareAndSwapInt(this, stateOffset, 1, 0);
    }

}
