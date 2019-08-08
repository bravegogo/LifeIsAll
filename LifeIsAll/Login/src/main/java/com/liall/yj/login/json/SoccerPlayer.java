package com.liall.yj.login.json;

import com.google.gson.annotations.*;

import java.util.List;


public class SoccerPlayer {

    private String name;

    @Since(1.2)
    private int shirtNumber;

    @Until(0.9)
    private String country;

    private String teamName;

    private List<String>  testList;


    public void setName(String name) {
        this.name = name;
    }

    public void setShirtNumber(int shirtNumber) {
        this.shirtNumber = shirtNumber;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setTestList(List<String> testList) {
        this.testList = testList;
    }

    // Methods removed for brevity


}
