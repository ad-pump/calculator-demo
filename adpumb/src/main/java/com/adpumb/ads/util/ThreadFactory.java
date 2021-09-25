package com.adpumb.ads.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import androidx.annotation.VisibleForTesting;

public class ThreadFactory {
    private static ThreadFactory factory = new ThreadFactory();
    private ScheduledExecutorService executorService;
    public ThreadFactory(){
        executorService = Executors.newScheduledThreadPool(10);
    }

    public static ThreadFactory getInstance() {
        return factory;
    }

    @VisibleForTesting
    public static void setThreadFactory(ThreadFactory threadFactory) {
        factory = threadFactory;
    }

    public void submit(Runnable runnable,long delay){
        executorService.schedule(runnable,delay, TimeUnit.MILLISECONDS);
    }

    public Handler getHandler() {
        return new Handler(Looper.getMainLooper());
    }
}
