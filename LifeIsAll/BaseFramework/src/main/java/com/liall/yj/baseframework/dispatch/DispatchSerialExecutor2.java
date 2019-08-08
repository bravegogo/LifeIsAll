package com.liall.yj.baseframework.dispatch;

import android.util.Log;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class DispatchSerialExecutor2   {


    private static ExecutorService executorService = Executors.newFixedThreadPool(1); //Executors.newSingleThreadExecutor();

    private static  final BlockingQueue   taskQueue = null;
    private static int  runIndex = 0  ;
    private static volatile boolean executorRunning;

    public static void dispatch_Async(Runnable runObj) {
//        synchronized (this) {
        executorService.execute(runObj);
//        }
    }




}
