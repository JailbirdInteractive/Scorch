package com.jailbird.scorch;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import noman.googleplaces.Place;

/**
 * Created by adria on 11/29/2016.
 */
public class PlaceInfo {
    Place place;
String id;
    public PlaceInfo(Place place,String id) {
        this.id=id;
        this.place = place;
    }

    public Place getPlace() {
        return place;
    }

    @Override
    public boolean equals(Object v) {
        boolean retVal = false;

        if (v!=null&&v instanceof PlaceInfo){
            PlaceInfo ptr = (PlaceInfo) v;
            retVal = ptr.id.equalsIgnoreCase(this.id);
        }

        return retVal;
    }
    @Override
    public int hashCode() {
        int result = 17;

        //hash code for checking rollno
        //result = 31 * result + (this.s_rollNo == 0 ? 0 : this.s_rollNo);

        //hash code for checking fname
        result = 31 * result + (this.id == null ? 0 : this.id.hashCode());

        return result;
    }
}