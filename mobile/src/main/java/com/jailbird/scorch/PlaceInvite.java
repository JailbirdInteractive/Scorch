package com.jailbird.scorch;

import noman.googleplaces.Place;

/**
 * Created by adria on 2/1/2017.
 */

public class PlaceInvite {
   public Place place;
    public String name,id,date,url;
    public boolean isAccepted;


    public PlaceInvite(Place place,String name,String id,String date,String url,boolean isAccepted){
        this.place=place;
this.isAccepted=isAccepted;
        this.name=name;
        this.id=id;
        this.date=date;
        this.url=url;
    }
public PlaceInvite(){

}

    @Override
    public boolean equals(Object obj) {
        boolean isMatch=false;
        if(obj!=null&&obj instanceof PlaceInvite){
            PlaceInvite hp=(PlaceInvite) obj;
            isMatch=hp.id.equalsIgnoreCase(this.id);
        }
        return isMatch;    }

    @Override
    public int hashCode() {
        int result = 17;

        //hash code for checking rollno
        //result = 31 * result + (this.s_rollNo == 0 ? 0 : this.s_rollNo);

        //hash code for checking fname
        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());

        return result;    }

}
