package com.liall.yj.baseframework.databaseservice;

import android.content.Context;

import net.sqlcipher.database.SQLiteOpenHelper;
import net.sqlcipher.database.SQLiteDatabase;
import java.util.List;
import android.util.Log;
public class DBCipherHelper extends SQLiteOpenHelper {


//    private static final String DATABASE_NAME = "sqlcipher_database";

    private static String TAG = "DBCipherHelper";

//    private static  DBCipherHelper helper;

//    public SQLiteDatabase mDatabase;
    private String databaseName ;
//    private String databaseKey ;
    private int  mNewVersion;
    private boolean   mIsInitializing;
    private Context mContext;

    private List<String> createTableSqlsList;


    public DBCipherHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        net.sqlcipher.database.SQLiteDatabase.loadLibs(context);

    }

    public DBCipherHelper(Context context, String name , String dbKey, int version, List<String> tableSqlsList  ) {
        super(context, name, null, version);

        net.sqlcipher.database.SQLiteDatabase.loadLibs(context);

        createTableSqlsList = tableSqlsList;
        databaseName = name;
        mContext = context;
        mNewVersion = version;

    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        for (int index = 0; index < createTableSqlsList.size() ;  index ++){
            sqLiteDatabase.execSQL(createTableSqlsList.get(index));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


    @Override
    public synchronized void close() {
        super.close();
    }


    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);

    }


    /**获取数据库路径**/
    public String getDBPath(){
        return mContext.getDatabasePath(databaseName).getPath();
    }


//    private class EncryptedHelper extends net.sqlcipher.database.SQLiteOpenHelper {
//        public EncryptedHelper(Context context, String name, int version, boolean loadLibs) {
//            super(context, name, null, version);
//            if (loadLibs) {
//                net.sqlcipher.database.SQLiteDatabase.loadLibs(context);
//            }
//        }
//
//        @Override
//        public void onCreate(net.sqlcipher.database.SQLiteDatabase db) {
//            DBCipherHelper.this.onCreate(wrap(db));
//        }
//
//        @Override
//        public void onUpgrade(net.sqlcipher.database.SQLiteDatabase db, int oldVersion, int newVersion) {
//            DBCipherHelper.this.onUpgrade(wrap(db), oldVersion, newVersion);
//        }
//
//        @Override
//        public void onOpen(net.sqlcipher.database.SQLiteDatabase db) {
//            DBCipherHelper.this.onOpen(wrap(db));
//        }
//
//        protected SQLiteDatabase wrap(net.sqlcipher.database.SQLiteDatabase sqLiteDatabase) {
//            return new EncryptedDatabase(sqLiteDatabase);
//        }
//
//    }


}
