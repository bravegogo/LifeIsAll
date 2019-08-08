package com.liall.yj.baseframework.dispatch;


import android.os.Handler;
import android.os.Looper;

public class DispatchMianThreadExecutor {

  static Handler handler  = new Handler(Looper.getMainLooper()) ;

  static public void dispatch_Async(Runnable runObj)
  {
      handler.post(runObj);
  }

}
