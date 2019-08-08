package com.liall.yj.login.json;

public class Person {

    private int id;
    private String name;
    private int age;
    private SoccerPlayer player;

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        this.age = age;
    }


    public SoccerPlayer getPlayer() {
        return player;
    }
    public void setPlayer(SoccerPlayer player) {
        this.player = player;
    }

    @Override
    public String toString() {
        return "Person [id=" + id + ", 姓名=" + name + ", 年龄=" + age + "]";
    }


 }
