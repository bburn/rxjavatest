package com.bkukowski.rxjavatest.utils;

public class LongRunningTask extends RunningTask {

    public LongRunningTask(String name) {
        super(name);
        blocked.set(false);
    }

    @Override
    public void runInnerTask() {
        synchronized (lock) {
            blocked.set(true);
            while(CommonValue.atomicInteger.get() == 0)
            {
//                System.out.println("LongRunningTask" + getName() + ": Hey! from thread: " + Thread.currentThread().getName() + " AtomicInteger=" + CommonValue.atomicInteger.get());
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("LongRunningTask" + getName() + ": Hey! from thread: " + Thread.currentThread().getName() + " BYE BYE");
    }
}
