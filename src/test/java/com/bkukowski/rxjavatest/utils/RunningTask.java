package com.bkukowski.rxjavatest.utils;

import java.util.concurrent.atomic.AtomicBoolean;

public abstract class RunningTask {

    private String name;
    public AtomicBoolean blocked = new AtomicBoolean();
    protected Object lock = new Object();

    public RunningTask(String name) {
        this.name = name;
    }

    public  abstract void runInnerTask();

    public String getName() {
        return name;
    }

}
