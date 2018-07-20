package com.jailbird.scorch;

import noman.googleplaces.Place;

/**
 * Created by adria on 12/12/2016.
 */

public class HotPlaces implements Comparable  {
    Place place;
    int count;
    public boolean isBoosted=false;

    public HotPlaces(Place place,int count){
        this.place=place;
        this.count=count;
        //this.isBoosted=false;
    }

    public HotPlaces(){

    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }

    public int getCount() {
        return count;
    }

    public void setBoosted(boolean boosted) {
        isBoosted = boosted;
    }


    @Override
    public int compareTo(Object o) {
        int comp=0;
        if(o!=null&&o instanceof HotPlaces){
            HotPlaces h=(HotPlaces)o;
            comp=this.count-h.count;
        }
        return comp;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isMatch=false;
        if(obj!=null&&obj instanceof HotPlaces){
            HotPlaces hp=(HotPlaces)obj;
            isMatch=hp.place.getPlaceId().equalsIgnoreCase(this.place.getPlaceId());
        }
        return isMatch;
    }

    @Override
    public int hashCode() {
        int result = 17;

        //hash code for checking rollno
        //result = 31 * result + (this.s_rollNo == 0 ? 0 : this.s_rollNo);

        //hash code for checking fname
        result = 31 * result + (this.place.getPlaceId() == null ? 0 : this.place.getPlaceId().hashCode());

        return result;
    }
}
