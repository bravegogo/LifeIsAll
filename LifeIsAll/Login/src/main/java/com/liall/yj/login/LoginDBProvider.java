package com.liall.yj.login;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.liall.yj.baseframework.databaseservice.DBService;

import net.sqlcipher.SQLException;

import java.util.List;

public class LoginDBProvider {

//    private  final  String loginTabelName = "Login";

    DBService dbService = DBService.getInstance();

    public LoginDBProvider( ){
     }

//    private static void onCallback(Object result) {
//    }

    public void saveLoginData(String loginID, String password , final DBService.ReqCallBack callback) throws SQLException {


//        dbService.execSQLInTransaction_Async_Block("INSERT INTO OR REPLACE Login VALUES (NULL, ?, ?)",new Object[]{loginID, password});

        //实例化常量值
        ContentValues cValue = new ContentValues();
        cValue.put("user_id",loginID);
        cValue.put("user_password",password);
        dbService.insertInTransaction_Async("user_login_info",null,cValue, callback);

//        db.beginTransaction();//开始事务
//
//        try {
//
//            db.execSQL("INSERT INTO OR REPLACE Login VALUES (NULL, ?, ?)", new Object[]{loginID, password});
//            db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
//
//        } catch (SQLiteDatabaseCorruptException excep) {
//            db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
//            throw excep;
//        } finally {
//            db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
//
//        }

    }


  public void queryLoginData(final DBService.ReqCallBack callback){


      dbService.queryInTransaction_Async("user_login_info",
              null, null,null,null, null,null,null,
              ((Object result) -> {

                  Cursor cursor = (Cursor) result;
                  LoginModel   model = new LoginModel();

                  while(cursor.moveToNext())
                  {
                      int id = cursor.getInt(0);
                      String username = cursor.getString(1);
                      String password = cursor.getString(2);

                      model.id = id;
                      model.username = username;
                      model.password = password;
                  }
                  Log.i(getClass().getSimpleName(),"0101010101：：0101010101：0101010101：0101010101："  );

                  callback.onCallback(model);


              }));

//      db.beginTransaction();//开始事务
//
//      try {
//          Cursor c = db.rawQuery("SELECT * FROM Login ", null);
//          db.setTransactionSuccessful();//调用此方法会在执行到endTransaction() 时提交当前事务，如果不调用此方法会回滚事务
//
//          while (c.moveToNext()) {
//              int _id = c.getInt(c.getColumnIndex("_id"));
//              String _loginID = c.getString(c.getColumnIndex("loginID"));
//              int _password = c.getInt(c.getColumnIndex("password"));
//          }
//      }finally {
//          db.endTransaction();//由事务的标志决定是提交事务，还是回滚事务
//      }
//查询获得游标


  }





}
