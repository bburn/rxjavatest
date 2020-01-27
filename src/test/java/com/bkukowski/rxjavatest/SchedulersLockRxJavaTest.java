package com.bkukowski.rxjavatest;

import com.bkukowski.rxjavatest.utils.LongRunningTask;
import com.bkukowski.rxjavatest.utils.RunningTask;
import com.bkukowski.rxjavatest.utils.ShortRunningTask;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.*;
import org.junit.jupiter.api.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.System.out;
import static java.lang.Thread.currentThread;

public class SchedulersLockRxJavaTest {

    @Test
    void subject_test() throws InterruptedException {

        ReplaySubject<RunningTask> runningTaskSubject = ReplaySubject.create();
        ExecutorService executorService = Executors.newFixedThreadPool(4);
        Scheduler from = Schedulers.from(executorService);

        runningTaskSubject.onNext(new LongRunningTask("1"));
        runningTaskSubject.onNext(new LongRunningTask("2"));
        runningTaskSubject.onNext(new LongRunningTask("3"));
        runningTaskSubject.onNext(new LongRunningTask("4"));

        runningTaskSubject.onNext(new ShortRunningTask("1"));
        runningTaskSubject.onNext(new ShortRunningTask("2"));
        runningTaskSubject.onNext(new ShortRunningTask("3"));
        runningTaskSubject.onNext(new ShortRunningTask("4"));

        runningTaskSubject.flatMap(runningTask -> Observable.just(runningTask).filter(runningTask1 -> runningTask instanceof LongRunningTask).subscribeOn(from)).subscribe(getRunningTaskObserver("1"));
        runningTaskSubject.flatMap(runningTask -> Observable.just(runningTask).filter(runningTask1 -> runningTask instanceof LongRunningTask).subscribeOn(from)).subscribe(getRunningTaskObserver("2"));
        runningTaskSubject.flatMap(runningTask -> Observable.just(runningTask).filter(runningTask1 -> runningTask instanceof LongRunningTask).subscribeOn(from)).subscribe(getRunningTaskObserver("3"));
        runningTaskSubject.flatMap(runningTask -> Observable.just(runningTask).filter(runningTask1 -> runningTask instanceof ShortRunningTask).subscribeOn(from)).subscribe(getRunningTaskObserver("4"));

        runningTaskSubject.onComplete();

        Thread.sleep(30000);
    }

    private Observer<RunningTask> getRunningTaskObserver(String observerName) {
        return new Observer<RunningTask>() {
            String name = observerName;

            @Override
            public void onSubscribe(Disposable d) {
            }

            @Override
            public void onNext(RunningTask runningTask) {
                synchronized (this) {
                    if (!runningTask.blocked.get()) {
                        out.println("Observer" + name + " is getting object of " + runningTask.toString());
                        runningTask.runInnerTask();
                        out.println("--Observer" + name);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onComplete() {
                out.println("Observer" + observerName + ":onClomplete from thread: " + Thread.currentThread().getName());
            }
        };
    }


}
