package com.liall.yj.baseframework.dispatch;

public class Dispatcher {

    public  static void dispatch_Async(Boolean isSerial , Boolean isUI , Runnable runObj){
         if (isUI){
             DispatchMianThreadExecutor.dispatch_Async(runObj);
         } else {
             if( isSerial ){
                 DispatchSerialExecutor2.dispatch_Async(runObj);
             }else {
                 DispatchConcurrentExecutor.dispatch_Async(runObj);
             }
         }

    }

}
