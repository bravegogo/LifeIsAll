package com.liall.yj.baseframework.databaseservice;


//import net.sqlcipher.database.SQLiteOpenHelper;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.liall.yj.baseframework.App;
import com.liall.yj.baseframework.dispatch.DispatchCallback;
import com.liall.yj.baseframework.dispatch.DispatchConcurrentExecutor;
import com.liall.yj.baseframework.dispatch.DispatchMianThreadExecutor;
import com.liall.yj.baseframework.dispatch.DispatchSerialExecutor;

import net.sqlcipher.Cursor;
import net.sqlcipher.SQLException;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseCorruptException;

import java.util.HashMap;
import java.util.List;
import java.io.File;
import java.io.IOException;

public class DBService {

    private static final String DATABASE_NAME = "sqlcipher_database.db";
    private static final String DATABASE_KEY = "1234567890";

    public DBCipherHelper dbhelper;;

    public SQLiteDatabase db;

    public static  DBService  instance = null;

    private List<String> createTableSqlsList;

    public  String  dbKey ;

    private DBContext context;
    private Context baseContext;

    private Handler dbHandler;//全局处理子线程和M主线程通信


//    public DBService (Context context, String dbName, String key ){
//        this.context = context;
//        this.context = getApplicationContext();
//        this.dbhelper =  new DBCipherHelper(this.context,dbName,null, 1);
//        this.db =  this.dbhelper.getWritableDatabase(key);
//      }
//
//    public DBService (Context context){
//        this.context = context;
//    }

    public DBService ( ){
//        this.context = getApplicationContext();
//        this.dbhelper = new DBCipherHelper(this.context,DATABASE_NAME,DATABASE_KEY,1,null);
//        this.db = this.dbhelper.getWritableDatabase(DATABASE_KEY);

    }
    public DBService (Context  context){
        this.context = new DBContext(context);
    }

    /**
     * 获取单例引用
     *
     * @return
     */

    public static DBService getInstance(Context context) {
        DBService inst = instance;
        if (inst == null) {
            synchronized (DBService.class) {
                inst = instance;
                if (inst == null) {
//                    inst = new DBService(context.getApplicationContext(),DATABASE_NAME,DATABASE_KEY);
                    inst = new DBService(context);
                    instance = inst;
                }
            }
        }
        return inst;
    }

    public static DBService getInstance() {
        DBService inst = instance;
        if (inst == null) {
            synchronized (DBService.class) {
                inst = instance;
                if (inst == null) {
                    inst = new DBService();
                    instance = inst;
                }
            }
        }
        return inst;
    }

//    @Override
//    public void onCreate() {
//        super.onCreate();
//        baseContext = ();
//        DBService dbService = DBService.getInstance(baseContext);
//
//    }

    public void closeDB() {
        if (db.isOpen()) {
            db.close();
        }

        if (dbhelper != null) {
            dbhelper.close();
        }

        if (instance != null) {
            instance = null;
        }
    }

    public void createDBTabels(String userId ,List <String>  tabels)  throws Exception
    {

       if (context == null){
               this.context = new DBContext(App.appContext);
        }

        this.context.currentUserId = userId;

        if ( dbHandler == null ){
            this.dbhelper = new DBCipherHelper(this.context,DATABASE_NAME,DATABASE_KEY,1,tabels);
        }
//        this.db = this.dbhelper.getWritableDatabase(DATABASE_KEY);

        try{
            this.db = this.dbhelper.getWritableDatabase(DATABASE_KEY);

        }catch (Exception ex){

            Log.i("db Exception","db 创建失败  catch" );
            throw ex;

        } finally {
            Log.i("db Exception","db 创建失败 finally" );

        }
    }


    //  createDB 与 createTables配合使用时， 不用调createDBTabels

    public void createDB(String userId){

        if (context == null){
            this.context = new DBContext(App.appContext);
        }

        this.context.currentUserId = userId;

        if ( dbHandler == null ){
            this.dbhelper = new DBCipherHelper(this.context,DATABASE_NAME,DATABASE_KEY,1,null);
        }

        try{
            this.db = this.dbhelper.getWritableDatabase(DATABASE_KEY);

        }catch (Exception ex){

            Log.i("db Exception","db 创建失败  catch" );

        } finally {
            Log.i("db Exception","db 创建失败 finally" );

        }
    }


    public void createTables(List <String>  tables ) {

        db.beginTransaction();//开始事务

        try {
            for (int index = 0; index < tables.size() ;  index ++){
                this.db.execSQL(tables.get(index));
            }            db.setTransactionSuccessful();
        }catch (SQLiteDatabaseCorruptException excep) {
            throw excep;
        } finally {
            db.endTransaction();
        }

    }


    public void checkDBCanWork(){

    }

    @FunctionalInterface
    public interface ReqCallBack<T> {

        void onCallback(T result);

    }

    private <T> void dbDataCallBack(final T result, final  ReqCallBack<T> callBack) {
        dbHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    callBack.onCallback(result);
                }
            }
        });
    }




    public  void rawQueryInTransaction(String sql, String[] selectionArgs ,final ReqCallBack callback) {

        db.beginTransaction();//开始事务

        try {
             Cursor cur = db.rawQuery(sql, selectionArgs);
             db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
             dbDataCallBack(cur, callback);
        }finally {
             db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
        }
    }

    public void execSQLInTransaction(String sql, Object[] bindArgs) throws SQLException{

        db.beginTransaction();//开始事务
        try {
            db.execSQL(sql, bindArgs);
            db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
        } catch (SQLiteDatabaseCorruptException excep) {
            throw excep;
        } finally {
            db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
        }

    }


    public void execSQLArrayInTransaction(HashMap<String, Object[]> params ) throws SQLException{

        db.beginTransaction();//开始事务

        try {

            for(HashMap.Entry<String, Object[]> entry: params.entrySet())
            {
                String sql = entry.getKey();
                Object[] bindArgs = entry.getValue();
                db.execSQL(sql, bindArgs);
            }

            db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
        } catch (SQLiteDatabaseCorruptException excep) {
            throw excep;
        } finally {
            db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
        }

    }


    @FunctionalInterface
    public interface DBTransactionCallBack {

        void onCallback(SQLiteDatabase  db );

    }

    private   void doTransactionCallBack(final SQLiteDatabase db, final DBTransactionCallBack callBack) {

          if (callBack != null) {
              callBack.onCallback(db);
         }

    }



    public void inTransaction_Async(final DBTransactionCallBack callBack) throws SQLException {

        DispatchSerialExecutor.getInstance().dispatch_Async(new DispatchCallback() {
                                                          @Override
                                                          public void onCall() {
                                                              db.beginTransaction();//开始事务

                                                              try {
                                                                  doTransactionCallBack(db, callBack );
                                                                  db.setTransactionSuccessful();
                                                              }catch (SQLiteDatabaseCorruptException excep) {
                                                                  throw excep;
                                                              } finally {
                                                                  db.endTransaction();
                                                              }
                                                          }
                                                      }

        );




     }





    public void execSQLArrayInTransaction_Async(final HashMap<String, Object[]> params ) throws SQLException {

        try {
            inTransaction_Async(new DBTransactionCallBack(){

                @Override
                public void onCallback(SQLiteDatabase db ) {
                    for(HashMap.Entry<String, Object[]> entry: params.entrySet())
                    {
                        String sql = entry.getKey();
                        Object[] bindArgs = entry.getValue();
                        db.execSQL(sql, bindArgs);
                    }
                }
            });

        }catch (SQLException e){
          throw  e;
        }
    }


    public void rawQueryInTransaction_Async(final String sql, final String[] selectionArgs , final ReqCallBack callback) {



        try {
            inTransaction_Async(new DBTransactionCallBack(){

                @Override
                public void onCallback(SQLiteDatabase db ) {
                    Cursor cur = db.rawQuery(sql, selectionArgs);
                }
            });

        }catch (SQLException e){
            throw  e;
        }

    }

    public  void execSQLInTransaction_Async(final String sql, final Object[] bindArgs) throws SQLException{

        try {
            inTransaction_Async(new DBTransactionCallBack(){
                @Override
                public void onCallback(SQLiteDatabase db ) {
                     db.execSQL(sql, bindArgs);
                 }
            });

        }catch (SQLException e){
            throw  e;
        }
    }

    public void inTransaction_Block(final DBTransactionCallBack callBack) throws SQLException {

            db.beginTransaction();//开始事务
            try {
                doTransactionCallBack(db, callBack );
                db.setTransactionSuccessful();
            }catch (SQLiteDatabaseCorruptException excep) {
                throw excep;
            } finally {
                db.endTransaction();
            }

    }

    public void inTransaction_Async_Block(final DBTransactionCallBack callBack) throws SQLException {

        DispatchSerialExecutor.getInstance().dispatch_Async(()->{
            db.beginTransaction();//开始事务
            try {
                doTransactionCallBack(db, callBack );
                db.setTransactionSuccessful();
            }catch (SQLiteDatabaseCorruptException excep) {
                throw excep;
            } finally {
                db.endTransaction();
            }

        });

    }




    public void execSQLArrayInTransaction_Async_Block(final HashMap<String, Object[]> params ) throws SQLException {
        DispatchSerialExecutor.getInstance().dispatch_Async(()-> {

        try {

                inTransaction_Block((db) -> {
                            for (HashMap.Entry<String, Object[]> entry : params.entrySet()) {
                                String sql = entry.getKey();
                                Object[] bindArgs = entry.getValue();
                                db.execSQL(sql, bindArgs);
                            }
                        }
                );

           }catch (SQLException e){
                throw  e;
           }


        });
    }


    public void rawQueryInTransaction_Async_Block(final String sql, final String[] selectionArgs , final ReqCallBack callback) {

        DispatchSerialExecutor.getInstance().dispatch_Async(()-> {

            try {
                db.beginTransaction();//开始事务
                try {
                    Cursor cur = db.rawQuery(sql, selectionArgs);

                    db.setTransactionSuccessful();

                    DispatchMianThreadExecutor.dispatch_Async(()->{
                        callback.onCallback(cur);
                    });

                }catch (SQLiteDatabaseCorruptException excep) {
                    throw excep;
                } finally {
                    db.endTransaction();
                }

              }catch (SQLException e){
                    throw  e;
              }

        });


    }

    public  void execSQLInTransaction_Async_Block(final String sql, final Object[] bindArgs,final ReqCallBack callback) throws SQLException{
        DispatchSerialExecutor.getInstance().dispatch_Async(()-> {

//            try {
//               inTransaction_Block((db)->{
//                    db.execSQL(sql, bindArgs);
//               });
//           }catch (SQLException e){
//            throw  e;
//          }

            try {
                db.beginTransaction();//开始事务
                try {
                    db.execSQL(sql, bindArgs);
                    db.setTransactionSuccessful();
                    DispatchMianThreadExecutor.dispatch_Async(()->{
                        callback.onCallback(true);
                    });

                }catch (SQLiteDatabaseCorruptException excep) {
                    throw excep;
                } finally {
                    db.endTransaction();
                }

            }catch (SQLException e){
                throw  e;
            }

        });

    }
    public  void insertInTransaction_Async(final String tableName,String nullColumnHack,  ContentValues contentValues, final ReqCallBack callback) throws SQLException{
//            try {
//                db.beginTransaction();//开始事务
//                try {
//                    long res = db.insert(tableName,nullColumnHack,contentValues);
//                    db.setTransactionSuccessful();
//
//                        callback.onCallback(res);
//
//                }catch (SQLiteDatabaseCorruptException excep) {
//                    throw excep;
//                } finally {
//                    db.endTransaction();
//                }
//
//            }catch (SQLException e){
//                throw  e;
//            }


            try {
                db.beginTransaction();//开始事务
                try {
                 long res = db.insert(tableName,nullColumnHack,contentValues);
                    db.setTransactionSuccessful();

                    DispatchMianThreadExecutor.dispatch_Async(()->{
                        callback.onCallback(res);
                    });

                }catch (SQLiteDatabaseCorruptException excep) {

                    throw excep;
                } finally {
                    db.endTransaction();
                }

            }catch (SQLException e){
                throw  e;
            }

//        });

    }
    public  void queryInTransaction_Async(String table,String[] columns,String selection,String[]  selectionArgs,String groupBy,String having,String orderBy,String limit, final ReqCallBack callback) throws SQLException
        {
//            DispatchConcurrentExecutor.dispatch_Async(()-> {
            DispatchSerialExecutor.getInstance().dispatch_Async(()-> {

            try {
                db.beginTransaction();
                try {
                    Cursor cur = db.query(table, columns, selection, selectionArgs, groupBy,  having, orderBy,limit);
                    db.setTransactionSuccessful();

                    Log.i(getClass().getSimpleName(),"0000000000：000000000000000");

                    DispatchMianThreadExecutor.dispatch_Async(()->{
                        callback.onCallback(cur);
                    });

                }catch (SQLiteDatabaseCorruptException excep) {

                    throw excep;
                } finally {
                    db.endTransaction();
                }

            }catch (SQLException e){
                throw  e;
            }

        });

    }

    /**
     * 对未加密的数据库文件做加密处理
     *
     * @param ctxt       上下文
     * @param dbName     数据库的文件名
     * @param passphrase 密码
     * @throws IOException
     */
    public static void encrypt(DBContext ctxt, String dbName, String passphrase) throws IOException {
        File originalFile = ctxt.getDatabasePath(dbName);

        if (originalFile.exists()) {
            File newFile =
                    File.createTempFile("sqlcipherutils", "tmp",
                            ctxt.getCacheDir());



            SQLiteDatabase db =
                    SQLiteDatabase.openDatabase(originalFile.getAbsolutePath(),
                            "", null,
                            SQLiteDatabase.OPEN_READWRITE);

            db.beginTransaction();
            try {
                db.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s';",
                        newFile.getAbsolutePath(), passphrase));
                db.rawExecSQL("SELECT sqlcipher_export('encrypted')");
                db.rawExecSQL("DETACH DATABASE encrypted;");

                int version = db.getVersion();

                db.close();

                db = SQLiteDatabase.openDatabase(newFile.getAbsolutePath(),
                        passphrase, null,
                        SQLiteDatabase.OPEN_READWRITE);
                db.setVersion(version);

                db.setTransactionSuccessful();

                originalFile.delete();
                newFile.renameTo(originalFile);

            }catch (SQLiteDatabaseCorruptException excep) {

                throw excep;
            } finally {
                db.endTransaction();

            }

//            db.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s';",
//                    newFile.getAbsolutePath(), passphrase));
//            db.rawExecSQL("SELECT sqlcipher_export('encrypted')");
//            db.rawExecSQL("DETACH DATABASE encrypted;");
//
//            int version = db.getVersion();
//
//            db.close();
//
//            db = SQLiteDatabase.openDatabase(newFile.getAbsolutePath(),
//                    passphrase, null,
//                    SQLiteDatabase.OPEN_READWRITE);
//            db.setVersion(version);
//            db.close();
//
//            originalFile.delete();
//            newFile.renameTo(originalFile);
        }
    }

    /**
     * 将加密数据库文件保存为非加密的数据库文件
     *
     * @param context  上下文
     * @param dbName   数据库名
     * @param password 密码
     * @param decFile  待保存的目标文件
     * @return
     */
    public static boolean decrypt(DBContext context, String dbName, String password, File decFile) {
        boolean flag = false;
        //先清空目标文件
        decFile.delete();

        try {
            File originalFile = context.getDatabasePath(dbName);
            SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(originalFile, password, null);


            if (database.isOpen()) {

                database.beginTransaction();
                try {
                database.rawExecSQL(String.format("ATTACH DATABASE '%s' as plaintext KEY '';", decFile.getAbsolutePath()));
                database.rawExecSQL("SELECT sqlcipher_export('plaintext');");
                database.rawExecSQL("DETACH DATABASE plaintext;");

                android.database.sqlite.SQLiteDatabase sqlDB = android.database.sqlite.SQLiteDatabase.openOrCreateDatabase(decFile, null);

                database.setTransactionSuccessful();

                    if (sqlDB != null)
                    flag = true;

                sqlDB.close();

                database.close();

            }catch (SQLiteDatabaseCorruptException excep) {

                throw excep;
            } finally {
                    database.endTransaction();
            }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
        // databaseFile.delete();
    }



}
