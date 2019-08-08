package com.liall.yj.baseframework;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.liall.yj.baseframework.databaseservice.DBService;

import net.sqlcipher.database.SQLiteDatabase;

import java.io.File;
import java.io.IOException;

public class App extends Application {

    static  public  Context appContext ;
    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        Log.i("x", appContext.toString());

//        if (!android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
//
//         return;
//        }
//            //获取sd卡路径
//        String dbDir = android.os.Environment.getExternalStorageDirectory().toString();
//
//        //判断目录是否存在，不存在则创建该目录
//        File baseFile = new File(dbDir);
//
//        // 目录不存在则自动创建目录
//        if (!baseFile.exists()){
//            baseFile.mkdirs();
//        }
//
//        StringBuffer buffer = new StringBuffer();
//        buffer.append(baseFile.getPath());
//        buffer.append(File.separator);
////        buffer.append("1235");
////        buffer.append(File.separator);
//        dbDir = buffer.toString();// 数据库所在目录
//
//        buffer.append("qqq2.db");
//
//        String dbPath = buffer.toString();// 数据库路径
//        // 判断目录是否存在，不存在则创建该目录
//        File dirFile = new File(dbDir);
//
//        boolean isFileCreateSuccess = false;
//        if (!dirFile.exists()){
//
//            isFileCreateSuccess =  dirFile.mkdirs();
//        }
//
//        //数据库文件是否创建成功
//        isFileCreateSuccess = false;
//        //判断文件是否存在，不存在则创建该文件
//        File dbFile = new File(dbPath);
//        if(!dbFile.exists()){
//            try {
//                isFileCreateSuccess = dbFile.createNewFile();//创建文件
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        else {
//            isFileCreateSuccess = true;
//        }


    }
}


