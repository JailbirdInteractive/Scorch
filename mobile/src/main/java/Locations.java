import com.firebase.geofire.GeoFire;

/**
 * Created by adria on 11/14/2016.
 */

public class Locations {
    public String userID;
    public GeoFire geoFire;
public int localUsers;
    public Locations() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }

    public Locations(GeoFire geoFire, String userID,int localUsers) {
        this.geoFire = geoFire;
        this.userID = userID;
        this.localUsers=localUsers;
    }

}
