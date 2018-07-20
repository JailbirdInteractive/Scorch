package com.jailbird.scorch;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

import java.util.List;

import noman.googleplaces.Place;

/**
 * Created by adria on 12/22/2016.
 */

public class MyItem implements ClusterItem {
    private final LatLng mPosition;

    private final HotPlaces mPlace;
    public MyItem(LatLng position,HotPlaces place) {
        mPosition = position;
        mPlace=place;
    }

    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public String getSnippet() {
        return null;
    }

    public Place getmPlace() {
        return mPlace.place;
    }
public HotPlaces getHotPlace(){
    return mPlace;
}
    @Override
    public boolean equals(Object obj) {
        boolean isMatch=false;
        if(obj!=null&&obj instanceof MyItem){
            MyItem hp=(MyItem)obj;
            isMatch=hp.mPlace.getPlace().getPlaceId().equalsIgnoreCase(this.mPlace.getPlace().getPlaceId());
        }
        return isMatch;    }
public List<String>getTypes(){
    return mPlace.getPlace().getTypes();
}
    @Override
    public int hashCode() {
        int result = 17;

        //hash code for checking rollno
        //result = 31 * result + (this.s_rollNo == 0 ? 0 : this.s_rollNo);

        //hash code for checking fname
        result = 31 * result + (this.mPlace.getPlace().getPlaceId() == null ? 0 : this.mPlace.getPlace().getPlaceId().hashCode());

        return result;    }


}