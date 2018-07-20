package com.jailbird.scorch;

/**
 * Created by adria on 1/3/2017.
 */

public class UserPreferences {
public int searchDistance;
    public String distanceUnit;
    public boolean isNightMode,isNotified;



    public UserPreferences(int searchDistance,String distanceUnit,boolean isNotified,boolean isNightMode){
        this.distanceUnit=distanceUnit;
        this.isNotified=isNotified;
        this.searchDistance=searchDistance;
        this.isNightMode=isNightMode;

    }
    public UserPreferences(){

    }
}
