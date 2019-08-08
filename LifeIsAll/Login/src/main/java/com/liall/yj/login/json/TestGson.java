package com.liall.yj.login.json;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class TestGson {


    public void jsonTest(){
        final GsonBuilder builder = new GsonBuilder();
        builder.setVersion(1.0);

        final Gson gson = builder.create();

        final SoccerPlayer account;
        account = new SoccerPlayer();
        account.setName("Albert Attard");
        account.setShirtNumber(10); // Since version 1.2
        account.setTeamName("Zejtun Corinthians");
        account.setCountry("Malta"); // Until version 0.9

        List test = new ArrayList();
        test.add("xx1");
        test.add("xx2");
        test.add("xx3");

        account.setTestList(test);

        final String json = gson.toJson(account);

        Log.i("-----11111",json);

        // ----------------------------------------------------------------------
        Gson gs = new Gson();

        Person person = new Person();
        person.setId(1);
        person.setName("我是酱油");
        person.setAge(24);
        person.setPlayer(account);
        String objectStr = gs.toJson(person);//把对象转为JSON格式的字符串
        System.out.println("把对象转为JSON格式的字符串///  "+objectStr);
        Log.i("-----11111",objectStr);


        // ----------------------------------------------------------------------

        List<Person> persons = new ArrayList<Person>();
        for (int i = 0; i < 10; i++) {//初始化测试数据
            Person ps = new Person();
            ps.setId(i);
            ps.setName("我是第"+i+"个");
            ps.setAge(i+10);
            ps.setPlayer(account);
            persons.add(ps);

        }

        String listStr = gs.toJson(persons);//把List转为JSON格式的字符串

        Log.i("-----11111",listStr);


        List<Person> persons2 = gs.fromJson(listStr, new TypeToken<List<Person>>(){}.getType());


        Person persons3 = gs.fromJson(objectStr, Person.class );


        Log.i("-----11111",listStr);


    }

}
