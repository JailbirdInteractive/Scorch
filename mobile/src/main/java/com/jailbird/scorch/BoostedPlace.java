package com.jailbird.scorch;

import java.util.Date;

import noman.googleplaces.Place;

/**
 * Created by adria on 3/24/2017.
 */

public class BoostedPlace {
    public Place place;
    public Date startDate,endDate;
            public BoostedPlace(Place place,Date startDate,Date endDate){
                this.place=place;
                this.startDate=startDate;
                this.endDate=endDate;

            }
            public BoostedPlace(){

            }

    public Date getEndDate() {
        return endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Place getPlace() {
        return place;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    @Override
    public boolean equals(Object obj) {
        boolean isMatch=false;
        if(obj!=null&&obj instanceof BoostedPlace){
            BoostedPlace a=(BoostedPlace) obj;
            isMatch=a.getPlace().getPlaceId().equalsIgnoreCase(this.place.getPlaceId());
        }
        return isMatch;
    }

    @Override
    public int hashCode() {

        int result = 17;

        //hash code for checking rollno
        //result = 31 * result + (this.s_rollNo == 0 ? 0 : this.s_rollNo);

        //hash code for checking fname
        result = 31 * result + (this.place.getPlaceId() == null ? 0 : this.getPlace().getPlaceId().hashCode());

        return result;    }
}
