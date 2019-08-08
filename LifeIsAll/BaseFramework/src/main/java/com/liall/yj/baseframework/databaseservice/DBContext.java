package com.liall.yj.baseframework.databaseservice;


import java.io.File;
import java.io.IOException;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class DBContext extends ContextWrapper {


    public String currentUserId = "greendao";  //一般用来针对一个用户一个数据库，以免数据混乱问题

    /**
 * 构造函数
 * @param    base 上下文环境
*/
    public DBContext(Context base,String  userId) {
        super(base);
        currentUserId = userId;
    }


     public DBContext(Context base ) {
         super(base);
     }


    /**
     * 获得数据库路径，如果不存在，则创建对象对象
     * @param    name
     * @param
     * @param
     */
    @Override
    public File getDatabasePath(String name) {
        //判断是否存在sd卡
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
        if(!sdExist){//如果不存在,
            Log.e("SD卡管理：", "SD卡不存在，请加载SD卡");
            return null;
        }
        else{//如果存在
            //获取sd卡路径
            String dbDir = android.os.Environment.getExternalStorageDirectory().toString();

            //判断目录是否存在，不存在则创建该目录
            File baseFile = new File(dbDir);

            // 目录不存在则自动创建目录
            if (!baseFile.exists()){
                baseFile.mkdirs();
            }

            StringBuffer buffer = new StringBuffer();
            buffer.append(baseFile.getPath());
            buffer.append(File.separator);
            buffer.append(currentUserId);
            dbDir = buffer.toString();// 数据库所在目录
            buffer.append(File.separator);
            buffer.append(name);

            String dbPath = buffer.toString();// 数据库路径
            // 判断目录是否存在，不存在则创建该目录
            File dirFile = new File(dbDir);

            boolean isFileCreateSuccess = false;
            if (!dirFile.exists()){
                isFileCreateSuccess =  dirFile.mkdirs();
            }

            //数据库文件是否创建成功
            isFileCreateSuccess = false;
            //判断文件是否存在，不存在则创建该文件
            File dbFile = new File(dbPath);
            if(!dbFile.exists()){
                try {
                    isFileCreateSuccess = dbFile.createNewFile();//创建文件
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
            else
                isFileCreateSuccess = true;

            //返回数据库文件对象
            if(isFileCreateSuccess) {

                Log.e("创建文件ok", "数据库路径 ok");

                return dbFile;
            }else {

                Log.e("创建文件失败", "数据库路径创建失败");

                return null;

            }
        }
    }

    /**
     * 重载这个方法，是用来打开SD卡上的数据库的，android 2.3及以下会调用这个方法。
     *
     * @param    name
     * @param    mode
     * @param    factory
     */


    @Override
    public SQLiteDatabase openOrCreateDatabase(String name,
                                               int mode,
                                               SQLiteDatabase.
                                                       CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
    }
    /**
     * Android 4.0会调用此方法获取数据库。
     *
     * @see android.content.ContextWrapper#openOrCreateDatabase(java.lang.String, int,
     *              android.database.sqlite.SQLiteDatabase.CursorFactory,
     *              android.database.DatabaseErrorHandler)
     * @param    name
     * @param    mode
     * @param    factory
     * @param     errorHandler
     */

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name,
                                               int mode,
                                               SQLiteDatabase.CursorFactory factory,
                                               DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name).getAbsolutePath(),factory,errorHandler);
    }



}