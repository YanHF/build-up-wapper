package com.huaifang.yan.concurrent;

import org.springframework.context.ApplicationContextAware;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class ConsumerLock extends AbstractQueuedSynchronizer implements Lock {
    @Override
    public void lock() {

    }

    @Override
    public void lockInterruptibly() throws InterruptedException {

    }

    @Override
    public boolean tryLock() {
        return false;
    }

    @Override
    public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
        return false;
    }

    @Override
    public void unlock() {

    }

    @Override
    public Condition newCondition() {
        return null;
    }

    @Override
    protected boolean isHeldExclusively() {
        return super.isHeldExclusively();
    }

    @Override
    protected boolean tryReleaseShared(int arg) {
        return super.tryReleaseShared(arg);
    }

    @Override
    protected int tryAcquireShared(int arg) {
        return super.tryAcquireShared(arg);
    }

    @Override
    protected boolean tryRelease(int arg) {
        if(this.getState()==0){
            return false;
        }
        if(compareAndSetState(1,0)){
            setState(0);
            this.setExclusiveOwnerThread(null);
            return true;
        }
        return false;
    }

    @Override
    protected boolean tryAcquire(int arg) {
        if (compareAndSetState(0, 1)) {
            this.setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }
        return false;
    }

    protected ConsumerLock() {
        super();
    }

    public static void main(String[] args) {
        ConsumerLock lock = new ConsumerLock();
        lock.newCondition();
        lock.acquire(1);
    }
}
