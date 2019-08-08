package com.liall.yj.login;

import android.util.Log;

import net.sqlcipher.SQLException;

import java.util.List;

public class LoginManager {


    LoginDBProvider loginDb = new LoginDBProvider();

    CreateAndCheckDBProvider ccDb  = new CreateAndCheckDBProvider();

    public void saveLoginData(String loginID, String password) throws SQLException {
          loginDb.saveLoginData(loginID,password,(res)->{
              Log.i(getClass().getSimpleName(), String.valueOf(res));
          });
    }


    public void queryLoginData() {

        loginDb.queryLoginData((model)->{
            LoginModel  model1 = (LoginModel) model;
            Log.i(getClass().getSimpleName(), "1111111111111111："+model1.username + "  " + model1.username );

        }) ;
    }

    public void createTables(String userId,List<String> tableSqlsList)    throws Exception {
            try {
                
                ccDb.createTables(userId,tableSqlsList);

            }catch (Exception ex){
                throw  ex;
            }
    }
}
