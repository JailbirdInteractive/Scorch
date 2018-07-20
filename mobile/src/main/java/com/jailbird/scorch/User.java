package com.jailbird.scorch;

import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.List;

/**
 * Created by adria on 10/15/2016.
 */

@IgnoreExtraProperties
public class User {

    public String username;
    public String email;
    public String userID;
    public List<String> localFriends;
    int localUsers;
    public String photoUrl;

    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public User(String username, String email, String userID, List<String> localFriends,  int localUsers,String photoUrl) {
        this.username = username;
        this.email = email;
        this.userID = userID;
        this.localUsers = localUsers;
        this.localFriends = localFriends;
        this.photoUrl=photoUrl;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object obj) {
        boolean isMatch=false;
        if(obj!=null&&obj instanceof User){
            User hp=(User)obj;
            isMatch=hp.userID.equalsIgnoreCase(this.userID);
        }
        return isMatch;    }

    @Override
    public int hashCode() {
        int result = 17;

        //hash code for checking rollno
        //result = 31 * result + (this.s_rollNo == 0 ? 0 : this.s_rollNo);

        //hash code for checking fname
        result = 31 * result + (this.userID == null ? 0 : this.userID.hashCode());

        return result;    }

}