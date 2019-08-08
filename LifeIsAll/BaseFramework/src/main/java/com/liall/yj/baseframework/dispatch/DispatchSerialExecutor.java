package com.liall.yj.baseframework.dispatch;

//import android.os.Handler;
//import android.os.Message;


//import org.greenrobot.greendao.async.AsyncOperation;
//import java.util.ArrayDeque;
//import java.util.Deque;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;


public class DispatchSerialExecutor implements Runnable{ //, Handler.Callback {

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    private   final BlockingQueue   taskQueue  ;
    private   int  runIndex = 0  ;

    private static volatile DispatchSerialExecutor mInstance;//单利引用

//    private   Deque<T> queue = new ArrayDeque<T>();

    private volatile boolean executorRunning;

    public <T> DispatchSerialExecutor()
    {
        taskQueue = new LinkedBlockingQueue<T>();
     }


    public static DispatchSerialExecutor getInstance() {
        DispatchSerialExecutor inst = mInstance;
        if (inst == null) {
            synchronized (DispatchSerialExecutor.class) {
                inst = mInstance;
                if (inst == null) {
                    inst = new DispatchSerialExecutor();
                    mInstance = inst;
                }
            }
        }
        return inst;
    }



    public  void dispatch_Async( DispatchCallback operation) {
//    public <T> void enqueue( T operation) {
        synchronized (this) {
            boolean add = taskQueue.add(operation);
            if (!executorRunning && add ) {
                executorRunning = true;
                executorService.execute(this);
            }
        }
    }

    @Override
    public   void run() {
        while (true) {

            synchronized (this) {

                DispatchCallback operation = null;
                try {
                    operation = (DispatchCallback) taskQueue.poll(2, TimeUnit.SECONDS);
                    if ( operation != null ) {
                        operation.onCall();
                          executorRunning = false;
                          Log.i(getClass().getSimpleName(),"-------------:  executorRunning Over , Run Index:"+ runIndex + "Task Count: "+ taskQueue.size());
                          runIndex++;

                    } else {
                           executorRunning = false;
                           Log.i(getClass().getSimpleName(),"-------------:  executorRunning Error , operation = null" + "Task Count: "+ taskQueue.size());
                          return;
                    }
                } catch (InterruptedException e) {
                       Log.i(getClass().getSimpleName(),"-------------:  executorRunning Error , InterruptedException" + "Task Count: "+ taskQueue.size());
                      e.printStackTrace();
                }

//                if ( operation == null ) {
//                    synchronized (this) {
//                        operation = (DispatchCallback) queue.poll();
//                        if ( operation == null ) {
//                            executorRunning = false;
//                            return;
//                        }
//                    }
//                }



            }
         }
    }

}
