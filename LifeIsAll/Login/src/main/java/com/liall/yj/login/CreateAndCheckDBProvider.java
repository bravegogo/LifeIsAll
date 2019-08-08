package com.liall.yj.login;

import com.liall.yj.baseframework.databaseservice.DBService;

import java.util.List;
import java.util.ArrayList;

public class CreateAndCheckDBProvider {

    DBService dbService = DBService.getInstance();


    public void createTables(String userId,List<String> tableSqlsList) throws Exception {
          try {
              dbService.createDBTabels(userId,tableSqlsList);

          }catch (Exception ex){
               throw  ex;
          }
    }

    public void  checkDB(){


    }

}
