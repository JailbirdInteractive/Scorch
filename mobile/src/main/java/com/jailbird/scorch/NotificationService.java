package com.jailbird.scorch;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.*;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.places.PlaceLikelihood;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.Places;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

import static com.jailbird.scorch.MainActivity.boostedPlaces;
import static com.jailbird.scorch.MainActivity.currentEvent;
import static com.jailbird.scorch.MainActivity.isNotified;
import static com.jailbird.scorch.MainActivity.myID;
import static com.jailbird.scorch.MainActivity.nearbyEvents;
import static com.jailbird.scorch.MainActivity.requesters;

public class NotificationService extends Service implements GoogleApiClient.OnConnectionFailedListener,LocationListener, GoogleApiClient.ConnectionCallbacks {
    DatabaseReference mDatabase;
String myId;
    private GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest = new LocationRequest();
    List<Place>bPlaces=new ArrayList<>();
Place currentPlace;
    public NotificationService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        if(MainActivity.boostedPlaces!=null)
        bPlaces=MainActivity.boostedPlaces;
        mGoogleApiClient= new GoogleApiClient.Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this).build();
        mGoogleApiClient.connect();  ;
        myId=MainActivity.myID;
        mDatabase= FirebaseDatabase.getInstance().getReference();
        refreshComments();
        refreshRequests();
        refreshReplies();
        Log.e("Notifications", "Service started");

        super.onCreate();
    }
    private void refreshComments() {
        DatabaseReference ref = mDatabase.child("Users").child(myId).child("Invites");
//recyclerView.removeAllViews();


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null) {
                    Log.e("Post Changed", "Comment added" + dataSnapshot.getValue());
                    PlaceInvite placeInvite = dataSnapshot.getValue(PlaceInvite.class);
                    if(!MainActivity.placeInviteList.contains(placeInvite)) {
                       if (isNotified&&!placeInvite.isAccepted)
                        sendNotification(placeInvite);

                        if(!MainActivity.placeInviteList.contains(placeInvite))
MainActivity.placeInviteList.add(placeInvite);
                    }
                }
                //String key=postsnap.getKey();


                //Log.e("Post Changed", "Comment added" + postsnap.getValue());
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null) {
                    Log.e("Post Changed", "Comment added" + dataSnapshot.getValue());
                    //PlaceInvite placeInvite = dataSnapshot.getValue(PlaceInvite.class);
                   // sendNotification(placeInvite);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void refreshReplies() {
        final DatabaseReference ref = mDatabase.child("Users").child(myId).child("Invite Replies");
//recyclerView.removeAllViews();


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null) {
                        InviteReply reply = dataSnapshot.getValue(InviteReply.class);
                        if (isNotified && !reply.seen)
                            sendReplyNotification(reply);
                        reply.seen = true;
                        ref.child(reply.userId).setValue(reply);
                    }
                                //String key=postsnap.getKey();


                //Log.e("Reply", "Comment added" + dataSnapshot.getValue());
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null) {
                    InviteReply reply = dataSnapshot.getValue(InviteReply.class);
                    if (isNotified && !reply.seen)
                        sendReplyNotification(reply);
                    reply.seen = true;
                    ref.child(reply.userId).setValue(reply);
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void refreshRequests() {
        DatabaseReference ref = mDatabase.child("Friend Requests").child(myId);
//recyclerView.removeAllViews();


        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null) {
                    //Log.e("Post Changed", "Comment added" + dataSnapshot.getValue());
                    FriendRequest friendRequest = dataSnapshot.getValue(FriendRequest.class);
                    if(!MainActivity.friendRequests.contains(friendRequest)){

MainActivity.friendRequests.add(friendRequest);
                        getRequestUser(friendRequest.id);
                     if (isNotified)
                        sendRequestNotification();                }

                    }

            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot!=null) {
                    FriendRequest friendRequest = dataSnapshot.getValue(FriendRequest.class);
                    if(!MainActivity.friendRequests.contains(friendRequest)){

                        MainActivity.friendRequests.add(friendRequest);
                        getRequestUser(friendRequest.id);
                      if(isNotified)
                        sendRequestNotification();
                    }


                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                if(dataSnapshot!=null) {

                }
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
    private void getRequestUser(final String id){
        DatabaseReference ref = mDatabase.child("Users");
        ref.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    User user = dataSnapshot.child(id).child("User").getValue(User.class);
                    requesters.add(user);
                    Log.e("Request","User: "+dataSnapshot.getValue().toString());




                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





    public void sendNotification(PlaceInvite placeInvite) {


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_fire_empty)
                        .setAutoCancel(true)
                        .setContentTitle(placeInvite.name+" wants to hang out")
                        .setContentText(placeInvite.name+ " invited you to hang out at "+placeInvite.place.getName());

        mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE);

        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



                mNotificationManager.notify(777, mBuilder.build());
    }








    public void sendRequestNotification() {

//Get an instance of NotificationManager//

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_fire_empty)
                        .setAutoCancel(true)
                        .setContentTitle("New friend request")
                        .setContentText("You have a new friend request.");
        mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE);

        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        NotificationManager mNotificationManager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);



        mNotificationManager.notify(999, mBuilder.build());
    }

    public static boolean isTomorrow(Date date1)
    {
        // if you already have date objects then skip 1
        //1

        //1

        //date object is having 3 methods namely after,before and equals for comparing
        //after() will return true if and only if date1 is after date 2
        boolean isPast=false;
        //DateTimeZone timeZone = DateTimeZone.forID( "Europe/Paris" );
        DateTime dateTimeInQuestion = new DateTime( date1 );  // Or: new DateTime( someJavaDotUtilDotDateObject );
        DateTime now = new DateTime();
        DateTime twentyFourHoursFromNow = now.plusHours( 12 ); // Ignores Daylight Saving Time (DST). If you want to adjust for that, call: plusDays( 1 ) instead.
        isPast= dateTimeInQuestion.isAfter( twentyFourHoursFromNow );

        //equals() returns true if both the dates are equal
       /* if(date1.equals(date2)){
            System.out.println("Date1 is equal Date2");
            isPast=false;
        }*/
        return isPast;
    }
public void checkEvents(){
    if(nearbyEvents!=null&&!nearbyEvents.isEmpty()){
        for(Event event:nearbyEvents){
if(event.isBoosted) {
    if (isTomorrow(event.startDate)) {
sendEventNotification(event);
    }
}
        }
    }
}





    public void sendReplyNotification(InviteReply reply) {


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_fire_empty)
                        .setAutoCancel(true)
                        .setContentTitle(reply.userName+" replied to your Invite")
                        .setContentText(""+reply.reply);
        mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE);

        Intent targetIntent = new Intent(this, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        mNotificationManager.notify(888, mBuilder.build());
    }

    public void sendPlaceNotification(Place place) {
MainActivity.currBoostedPlace=place;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_fire_empty)
                        .setAutoCancel(true)
                        .setContentTitle(place.getName()+" is right next door")
                        .setContentText(place.getName()+" is really hot right now and you're already in the neighborhood. Come check it out.");
        mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE);

        Intent targetIntent = new Intent(this, MainActivity.class).putExtra("placeNotification",true);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        mNotificationManager.notify(998, mBuilder.build());
    }


    public void sendEventNotification(Event place) {
currentEvent=place;
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_fire_empty)
                        .setAutoCancel(true)
                        .setLights(R.color.colorPrimary,50000,50000)
                        .setContentTitle(place.getEventName()+" is almost here!")
                        .setContentText(place.getEventName()+" is happening tomorrow! Come check it out.");
        mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS| Notification.DEFAULT_VIBRATE);

        Intent targetIntent = new Intent(this, MainActivity.class).putExtra("eventNotification",true);
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, targetIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);

        NotificationManager mNotificationManager =

                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        mNotificationManager.notify(845, mBuilder.build());
    }

    protected void createLocationRequest() {
        mLocationRequest.setInterval(2000000);
        mLocationRequest.setFastestInterval(1500000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
mLocationRequest.setSmallestDisplacement(15f);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,
                        builder.build());
        startLocationUpdates();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
    protected void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {
getMyNearby(location);
    //checkEvents();
    }

    void getMyNearby(Location location) {
        final List<Place> placeList=new ArrayList<Place>();
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
                        if (places.size() > 0) {
                            //Collections.reverse(places);
                            for (Place place : places) {
                                placeList.add(place);
                                Log.e("Near", "Place name " + place.getName());

                            }




                        } else {
                            currentPlace = null;
                            Log.e("Near", "Place null");

                        }
                    }

                    @Override
                    public void onPlacesFinished() {
                        //stopAnim();
                        currentPlace = placeList.get(0);

                       for (Place p:placeList) {
                           Log.i("CurrPlace", "Place name " + p.getName());
                           if (bPlaces.contains(p)) {
                               sendPlaceNotification(p);
                               break;

                           }
                       }
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

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        createLocationRequest();

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

}
