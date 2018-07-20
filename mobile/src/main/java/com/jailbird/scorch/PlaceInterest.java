package com.jailbird.scorch;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import noman.googleplaces.Place;

/**
 * Created by adria on 11/29/2016.
 */

public class PlaceInterest implements Parcelable {
    Interest interest;
    List<Place> placeList;

    public PlaceInterest(Interest interest, List<Place> placeList) {
        this.interest=interest;
        this.placeList = placeList;
    }

    public Interest getInterest() {
        return interest;
    }

    public List<Place> getPlaceList() {
        return placeList;
    }

    protected PlaceInterest(Parcel in) {
    }

    public static final Creator<PlaceInterest> CREATOR = new Creator<PlaceInterest>() {
        @Override
        public PlaceInterest createFromParcel(Parcel in) {
            return new PlaceInterest(in);
        }

        @Override
        public PlaceInterest[] newArray(int size) {
            return new PlaceInterest[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
