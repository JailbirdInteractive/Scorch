package com.jailbird.scorch;

import android.app.Service;
import android.content.Intent;
import android.location.*;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

import static com.jailbird.scorch.MainActivity.boostedObjects;
import static com.jailbird.scorch.MainActivity.boostedPlaces;
import static com.jailbird.scorch.MainActivity.currentPlaces;
import static com.jailbird.scorch.MainActivity.eventIdKeys;
import static com.jailbird.scorch.MainActivity.friendLocations;
import static com.jailbird.scorch.MainActivity.historyList;
import static com.jailbird.scorch.MainActivity.latLngList;
import static com.jailbird.scorch.MainActivity.mCurrentPlace;
import static com.jailbird.scorch.MainActivity.matchedIDs;
import static com.jailbird.scorch.MainActivity.myAvatarUrl;
import static com.jailbird.scorch.MainActivity.myFavorites;
import static com.jailbird.scorch.MainActivity.myFriends;
import static com.jailbird.scorch.MainActivity.myInterests;
import static com.jailbird.scorch.MainActivity.myLocation;
import static com.jailbird.scorch.MainActivity.nearbyPlaces;
import static com.jailbird.scorch.MainActivity.otherInterests;
import static com.jailbird.scorch.MainActivity.placeIdList;
import static com.jailbird.scorch.MainActivity.requesters;
import static com.jailbird.scorch.MainActivity.searchDistance;

public class LoaderService extends Service {
    DatabaseReference mDatabase;
    GeoFire geoFire,eventGeoFire;
    //GPSTracker gpsTracker;
    String myID = "UserID";
    Location location;
    private String myName = "User Name";

    public LoaderService() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
        geoFire = new GeoFire(mDatabase.child("Locations"));
        eventGeoFire = new GeoFire(mDatabase.child("Event Locations"));

//gpsTracker=new GPSTracker(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            String id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            String com = ".com";
            String rep = "";
            if (id != null) {
                String sub = id.replace(com, rep);
                myID = sub.replaceAll("[-+.^:,]", "");
            }


            FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        if (myLocation != null) {
            location = myLocation;
        } else {
            location = new GPSTracker(getApplicationContext()).getLocation();

        }
        if (nearbyPlaces == null) {
            nearbyPlaces = new ArrayList<Place>();
        }
            getKeys();
            getNearby();
            getInvites();
        getEventKeys();
            for (FriendRequest request : MainActivity.friendRequests) {
                getRequestUser(request.id);
            }
            for (String fid : MainActivity.friendIds) {
                getFriends(fid);
            }

        //getMyNearby(location);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        Log.e("Loader", "Loader Finished");
    }

    @Override
    public IBinder onBind(Intent intent) {

        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");

    }

    private void getInvites() {
        DatabaseReference ref = mDatabase.child("Users").child(myID).child("Invites");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    Log.e("Invites", "invite ");

                    for (DataSnapshot d : dataSnapshot.getChildren()) {
                        PlaceInvite placeInvite = d.getValue(PlaceInvite.class);
                        Log.e("Invites", "invite " + placeInvite.toString());
                        if (!MainActivity.placeInviteList.contains(placeInvite))
                            MainActivity.placeInviteList.add(placeInvite);
                    }
                } else {
                    Log.e("Invites", "invite null");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getMyFavs(String id) {
        DatabaseReference ref = mDatabase.child("Users").child(myID).child("Favorites");
        ref.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.w("Favorites", "My Favs " + dataSnapshot.getValue());
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    if (postSnapshot.getValue() != null) {

                        Place post = postSnapshot.getValue(Place.class);
                        //Log.e("Get Data", post.<YourMethod>());
                        myFavorites.add(post);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getKeys() {
        Log.w("Map Keys", "Getting keys");
        int distance = searchDistance * 10;

        if (location == null) {
            getLocation();
        } else {
            //LatLng me = new LatLng(location.getLatitude(), location.getLongitude());

            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), MainActivity.searchDistance);
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(final String key, final GeoLocation location) {
                    if (!key.matches(myID)) {
                        new Thread(new Runnable() {
                            public void run() {
                                //getAllNearby(location);
                                // getInterests(key);
                                //getUsers(key);

                                // LatLng person = new LatLng(location.latitude, location.longitude);

                                // latLngList.add(person);

                            }
                        });
                        //matchedIDs.add(key);
                        getInterests(key);

                    }


                }

                @Override
                public void onKeyExited(String key) {

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                }

                @Override
                public void onGeoQueryReady() {
                    getNearby();

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                    error.toException().printStackTrace();
                }
            });


        }
    }
    private void getEventKeys() {
        Log.w("Event Keys", "Getting keys");
        int distance = searchDistance * 10;

        if (location == null) {
            getLocation();
        } else {
            //LatLng me = new LatLng(location.getLatitude(), location.getLongitude());

            GeoQuery geoQuery = eventGeoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), MainActivity.searchDistance);
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(final String key, final GeoLocation location) {

                        //matchedIDs.add(key);

                    if(!eventIdKeys.contains(key))             // latLngList.add(person);
                        eventIdKeys.add(key);
                    Log.w("Event Keys", "Key "+key);




                }

                @Override
                public void onKeyExited(String key) {

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                }

                @Override
                public void onGeoQueryReady() {

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {
                    error.toException().printStackTrace();
                }
            });


        }
    }

    private void getLocation() {
location=new GPSTracker(getApplicationContext()).getLocation();
    }

    void getNearby() {
        int distance = searchDistance * 100;
        final List<Place> nearList = new ArrayList<Place>();
        if(location==null){
            location = new Location("");//provider name is unnecessary
            location.setLatitude(0.0d);//your coords of course
            location.setLongitude(0.0d);
        }
        new NRPlaces.Builder()
                .listener(new PlacesListener() {
                    @Override
                    public void onPlacesFailure(PlacesException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onPlacesStart() {

                    }

                    @Override
                    public void onPlacesSuccess(List<Place> places) {
                        for (noman.googleplaces.Place place : places) {
                            if (!nearList.contains(place))
                                nearList.add(place);
                        }
                    }

                    @Override
                    public void onPlacesFinished() {
                        nearbyPlaces = nearList;
                        //stopAnim();
                        //startActivity(new Intent(getApplicationContext(), MapsActivity.class).putStringArrayListExtra("placeIds", placeIdList));
                        for(Place p:nearbyPlaces){
                            Log.w("Boosted", "Searching "+p.getPlaceId());

                            getBoosted(p.getPlaceId());
                        }
                        LoaderService.this.stopSelf();
                    }
                })
                .key(getString(R.string.map_api_key))
                .latlng(location.getLatitude(), location.getLongitude())
                .radius(distance)

                .build()
                .execute();
    }

    private void getInterests(final String id) {
        DatabaseReference ref = mDatabase.child("Interests");
        ref.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot postSnapshot : dataSnapshot.child(id).getChildren()) {

                        //Log.w("Map Interests", "Interest snapshot " + postSnapshot.getValue());

                        Interest post = postSnapshot.getValue(Interest.class);
                        if (otherInterests != null)
                            otherInterests.add(post);
if(myInterests!=null && matchedIDs!=null) {
    if (myInterests.contains(post) && !matchedIDs.contains(id)) {
        matchedIDs.add(id);
        getAllNearby(id);
        getHistory(id);
        getUsers(id);
        Log.w("Map Interests", "Interest ID added" + id);

    } else {
        Log.w("Map Interests", "Interest ID already exists " + id);

    }

}
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void getAllNearby(String id) {

        geoFire.getLocation(id, new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                LatLng person = new LatLng(location.latitude, location.longitude);

                latLngList.add(person);
                Log.w("Hot Location", "Hot Location added" + person);

                new NRPlaces.Builder()
                        .listener(new PlacesListener() {
                            @Override
                            public void onPlacesFailure(PlacesException e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onPlacesStart() {

                            }

                            @Override
                            public void onPlacesSuccess(List<noman.googleplaces.Place> places) {
                                if (places.size() > 0) {
                                    Place p = places.get(0);
                                    placeIdList.add(p);
                                    Log.w("Map Interests", "Interest ID Place " + p.getName() + " " + placeIdList.size());

                                }
                            }

                            @Override
                            public void onPlacesFinished() {
                                //stopAnim();

                                //startActivity(new Intent(getApplicationContext(), MapsActivity.class));


                            }
                        })
                        .key(getString(R.string.map_api_key))
                        .latlng(location.latitude, location.longitude)
                        .radius(searchDistance)

                        .build()
                        .execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    public static boolean isPast(Date date1, Date date2)
    {
        // if you already have date objects then skip 1
        //1

        //1

        //date object is having 3 methods namely after,before and equals for comparing
        //after() will return true if and only if date1 is after date 2
        boolean isPast=false;
        if(date1.after(date2)){
            System.out.println("Date1 is after Date2");
            isPast= true;
        }

        //before() will return true if and only if date1 is before date2
        if(date1.before(date2)){
            System.out.println("Date1 is before Date2");
            isPast=false;
        }

        //equals() returns true if both the dates are equal
        if(date1.equals(date2)){
            System.out.println("Date1 is equal Date2");
            isPast=false;
        }
        return isPast;
    }
private void getBoosted(final String id){
    final Date today=new Date();

    DatabaseReference ref = mDatabase.child("Boosted Places");
    ref.orderByKey().equalTo(id).addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //data will be available on dataSnapshot.getValue();
                    if(dataSnapshot.getValue()!=null) {
                        Log.w("Boosted", "Place id is " + dataSnapshot.getValue().toString());
                        BoostedPlace boostedPlace = dataSnapshot.child(id).getValue(BoostedPlace.class);
                        if (!isPast(today, boostedPlace.endDate)) {
                            Place place = boostedPlace.getPlace();
                            if (boostedPlaces != null && boostedObjects != null) {
                                if (!boostedPlaces.contains(place)) {
                                    boostedPlaces.add(place);
                                }
                                if (!boostedObjects.contains(boostedPlace)) {
                                    boostedObjects.add(boostedPlace);
                                }
                            }
                        }else{
                            DatabaseReference r=dataSnapshot.getRef();
                            Log.d("Ref","ref "+r.toString());

                            r.removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Log.w("Boosted", "getUser:onCancelled", databaseError.toException());
                }
            });
}
    private void getHistory(final String id) {
        DatabaseReference ref = mDatabase.child("History").child(id);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChildren()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    if(snapshot!=null) {
                        LatLng latLng = new LatLng((double) snapshot.child("latitude").getValue(), (double) snapshot.child("longitude").getValue());
                        historyList.add(latLng);
                        //Log.d("History", "" + snapshot.getValue().toString());
                    }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getUsers(final String id) {
        DatabaseReference ref = mDatabase.child("Users");
        ref.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User user = dataSnapshot.child(id).child("User").getValue(User.class);
                    double lat = (double) dataSnapshot.child(id).child("Current Location").child("latitude").getValue();
                    double lng = (double) dataSnapshot.child(id).child("Current Location").child("longitude").getValue();
                    if (dataSnapshot.child(id).child("Current Place").getValue() != null) {
                        Place place = dataSnapshot.child(id).child("Current Place").getValue(Place.class);
                        currentPlaces.add(place);
                    }
                    GeoLocation currentLocation = new GeoLocation(lat, lng);
                    Log.d("Location", "" + currentLocation.toString());

                    //Log.e("Get Data", post.<YourMethod>());
                    // myInterests.add(post);
                    System.out.println(user);

                    //Log.w("Users ", "User:  " + user.userID);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getRequestUser(final String id) {
        DatabaseReference ref = mDatabase.child("Users");
        ref.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User user = dataSnapshot.child(id).child("User").getValue(User.class);
                    //user.setUserID(dataSnapshot.child(id).child("Profile Image").getValue().toString());
                    requesters.add(user);
                    Log.e("Request", "User: " + dataSnapshot.getValue().toString());

                    //System.out.println(user);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    void getFriends(String id) {
        myFriends.clear();
        friendLocations.clear();
        DatabaseReference ref = mDatabase.child("Users").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User friend = dataSnapshot.child("User").getValue(User.class);

                    LatLng latLng = new LatLng((double) dataSnapshot.child("Current Location").child("latitude").getValue(), (double) dataSnapshot.child("Current Location").child("longitude").getValue());
                    Log.e("Friend", "User: " + friend.toString() + " location " + latLng.toString());
                    friendLocations.add(latLng);
                    myFriends.add(friend);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    void getLocal() {
        geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 10).addGeoQueryEventListener(new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(String key, GeoLocation location) {

            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });

        geoFire.setLocation(myID, new GeoLocation(location.getLatitude(), location.getLongitude()), new GeoFire.CompletionListener() {
            @Override
            public void onComplete(String key, DatabaseError error) {
                if (error != null) {
                    System.err.println("There was an error saving the location to GeoFire: " + error);
                } else {
                    System.out.println("Location saved on server successfully!");
                    //startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                    new Thread(new Runnable() {
                        public void run() {
                            // do something

                        }
                    }).start();

                    //getNearby();
                }
            }


        });

    }

    void getMyNearby(android.location.Location location) {
        new NRPlaces.Builder()
                .listener(new PlacesListener() {
                    @Override
                    public void onPlacesFailure(PlacesException e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onPlacesStart() {

                    }

                    @Override
                    public void onPlacesSuccess(List<noman.googleplaces.Place> places) {
                        if (places.size() > 0) {
                            mCurrentPlace = places.get(0);
                        } else {
                            mCurrentPlace = null;
                        }
                    }

                    @Override
                    public void onPlacesFinished() {
                        //stopAnim();
                        writeNewUser(myName, "", myID, myAvatarUrl);

                        //startActivity(new Intent(getApplicationContext(), MapsActivity.class));


                    }
                })
                .key(getString(R.string.map_api_key))
                .latlng(location.getLatitude(), location.getLongitude())
                .rankby("distance")
                //.radius(20)
                .build()
                .execute();
    }

    private void writeNewUser(String name, String email, String id, String url) {
        User user = new User(name, email, id, null, 0, url);

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("User").setValue(user);
        mDatabase.child("Users").child(id).child("Current Location").setValue(new GeoLocation(location.getLatitude(), location.getLongitude()));
        mDatabase.child("Users").child(id).child("Current Place").setValue(mCurrentPlace);
    }
}
