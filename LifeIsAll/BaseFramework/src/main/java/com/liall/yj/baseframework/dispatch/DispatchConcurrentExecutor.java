package com.liall.yj.baseframework.dispatch;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DispatchConcurrentExecutor {

//    private static volatile DispatchConcurrentExecutor mInstance;

    private static ExecutorService executorService = Executors.newCachedThreadPool();

    public static void dispatch_Async(Runnable runObj) {
//         synchronized (DispatchConcurrentExecutor.class) {
                 executorService.execute(runObj);
//         }
    }

//    public static DispatchConcurrentExecutor getInstance() {
//        DispatchConcurrentExecutor inst = mInstance;
//        if (inst == null) {
//            synchronized (DispatchConcurrentExecutor.class) {
//                inst = mInstance;
//                if (inst == null) {
//                    inst = new DispatchConcurrentExecutor();
//                    mInstance = inst;
//                }
//            }
//        }
//        return inst;
//    }

}
