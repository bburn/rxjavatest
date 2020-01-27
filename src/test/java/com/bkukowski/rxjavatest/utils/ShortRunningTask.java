package com.bkukowski.rxjavatest.utils;

public class ShortRunningTask extends RunningTask {
    public ShortRunningTask(String name) {
        super(name);
        blocked.set(false);
    }

    @Override
    public synchronized void runInnerTask() {
        synchronized (this) {
            blocked.set(true);
            CommonValue.atomicInteger.set(1);
            System.out.println("ShortRunningTask" + getName() + ": Hey! from thread: " + Thread.currentThread().getName() + " AtomicInteger=" + CommonValue.atomicInteger.get());

        }
        System.out.println("ShortRunningTask" + getName() + ": Hey! from thread: " + Thread.currentThread().getName());

    }
}
