package com.jailbird.scorch;

import android.*;
import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.entire.sammalik.samlocationandgeocoding.SamLocationRequestService;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.firebase.geofire.LocationCallback;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.PermissionsRefusedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nineoldandroids.animation.Animator;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;

import static com.jailbird.scorch.MapsActivity.mGoogleApiClient;


public class MainActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, BillingProcessor.IBillingHandler {
    private static final String TAG = "MainActivity";
    public static String distanceUnit = "Mi.";
    public static List<PlaceInvite> placeInviteList=new ArrayList<PlaceInvite>();
    GPSTracker gpsTracker;
    TextView locationText;
    DatabaseReference mDatabase;
    Context context;
    public static String countryCode;
    public static List<String> friendIds=new ArrayList<String>();
    public static Location myLocation;
    GeoFire geoFire;
   public static String myName = "User";
    public static int searchDistance = 30;
    public static List<LatLng> latLngList, historyList;
    public static ArrayList<Place> placeIdList;
    public static List<Interest> otherInterests, matchedInterests;
    public static List<Interest> myInterests;
    public static List<Event>nearbyEvents=new ArrayList<>();
    public static List<String> matchedIDs;
    public static List<Place> nearbyPlaces,boostedPlaces;
    public static List<BoostedPlace>boostedObjects;
    public static List<User>requesters=new ArrayList<User>();
    public static List<User>myFriends=new ArrayList<User>();
public static List<String>eventIdKeys=new ArrayList<String>();
    public static String myID = "UserID";
public static List<FriendRequest>friendRequests=new ArrayList<FriendRequest>();
    public static ArrayList<Place> foundPlaces;
    public  static GoogleApiClient mainApiClient;
    // private GooglePlaces client;
    //List<Photo> photos = new ArrayList<>();
    public static Place mCurrentPlace;
    public static Event currentEvent;
    public static List<Place> currentPlaces = new ArrayList<Place>();
    ImageView logo;
    AVLoadingIndicatorView avi;
    public static List<Place> myFavorites;
    public static String myAvatarUrl;
    public static boolean isNotified=true;
    public static boolean isNightMode=false;
    public static boolean isPaid=false;
    public static Place currBoostedPlace;
public static List<LatLng>friendLocations=new ArrayList<LatLng>();
    BillingProcessor bp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(MainActivity.this);
        locationText = (TextView) findViewById(R.id.location_text);
        bp = new BillingProcessor(this, getString(R.string.licence_key), this);

        //gpsTracker = new GPSTracker(MainActivity.this);
//getHash();
        //client = new GooglePlaces("AIzaSyCQyX50yZYb2ycenLpY3EWJYZ42g8jiSzE");
        placeIdList = new ArrayList<Place>();
        context=this;

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if(FirebaseAuth.getInstance().getCurrentUser().getEmail()!=null) {

                String id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String com = ".com";
                String rep = "";
                int index = id.lastIndexOf(".");

                if (id != null) {
                    String sub = id.replace(com, rep);

                    myID = sub.replaceAll("[-+.^:,]", "");

                }
            }else{
                myID=FirebaseAuth.getInstance().getCurrentUser().getUid();
            }

   //         myID = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            myName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        mainApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .addApi(LocationServices.API)
                .enableAutoManage(this, this)
                .build();
        myInterests = new ArrayList<Interest>();
        latLngList = new ArrayList<>();
        historyList = new ArrayList<>();
        otherInterests = new ArrayList<Interest>();
        matchedInterests = new ArrayList<Interest>();
        matchedIDs = new ArrayList<String>();
        friendIds = new ArrayList<>();
        myFavorites = new ArrayList<Place>();
        nearbyPlaces = new ArrayList<>();
        foundPlaces = new ArrayList<>();
        boostedPlaces=new ArrayList<>();
        boostedObjects=new ArrayList<>();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        geoFire = new GeoFire(mDatabase.child("Locations"));
        logo = (ImageView) findViewById(R.id.logo);
        avi = (AVLoadingIndicatorView) findViewById(R.id.avi);
        //myLocation=gpsTracker.getLocation();
        //logo.startAnimation(AnimationUtils.loadAnimation(this, R.anim.pulse));
        startAnim();


//getLocation();

    }

    void startAnim() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
            progressBar.setVisibility(View.VISIBLE);
            avi.setVisibility(View.GONE);

        } else {
            avi.show();
        }
        // or avi.smoothToShow();
    }

    void stopAnim() {
        //avi.hide();
        avi.smoothToHide();
    }

    private void getPermission() {
        Log.e("Permissions","Getting Permissions");
        startService(new Intent(getApplicationContext(),NotificationService.class));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            new Permissive.Request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE).whenPermissionsGranted(new PermissionsGrantedListener() {
                @Override
                public void onPermissionsGranted(String[] permissions) throws SecurityException {
                    if (myLocation == null) {
                        new SamLocationRequestService(MainActivity.this).executeService(new SamLocationRequestService.SamLocationListener() {
                            @Override
                            public void onLocationUpdate(Location location, Address address) {
                                if (location != null) {
                                    myLocation = location;
                                    getMyNearby(location);
                                    //countryCode=address.getCountryCode();
                                    geoFire.setLocation(myID, new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()), new GeoFire.CompletionListener() {

                                        @Override
                                        public void onComplete(String key, DatabaseError error) {
                                            startService(new Intent(getApplicationContext(), LoaderService.class));

                                            if (error != null) {
                                                System.err.println("There was an error saving the location to GeoFire: " + error);
                                            } else {
                                                System.out.println("Location saved on server successfully!");
                                            }
                                        }
                                    });

                                } else {
                                    //getPermission();
                                myLocation=new GPSTracker(getApplicationContext()).getLocation();
                                getMyNearby(myLocation);
                                geoFire.setLocation(myID, new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()), new GeoFire.CompletionListener() {

                                    @Override
                                    public void onComplete(String key, DatabaseError error) {
                                        startService(new Intent(getApplicationContext(), LoaderService.class));
                                        if (error != null) {
                                            System.err.println("There was an error saving the location to GeoFire: " + error);
                                        } else {
                                            System.out.println("Location saved on server successfully!");
                                        }
                                    }
                                });

                                }
                            }
                        });

                        //startActivity(new Intent(getApplicationContext(),MapsActivity.class));
                    }
                    else{
                        getMyNearby(myLocation);
                        geoFire.setLocation(myID, new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()), new GeoFire.CompletionListener() {

                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                startService(new Intent(getApplicationContext(), LoaderService.class));
                                if (error != null) {
                                    System.err.println("There was an error saving the location to GeoFire: " + error);
                                } else {
                                    System.out.println("Location saved on server successfully!");
                                }
                            }
                        });
                    }
                }
            }).execute(MainActivity.this);

        } else {
            if (myLocation == null) {
                new SamLocationRequestService(MainActivity.this).executeService(new SamLocationRequestService.SamLocationListener() {
                    @Override
                    public void onLocationUpdate(Location location, Address address) {
                        if (location != null) {
                            myLocation = location;
                            getMyNearby(location);
                            //countryCode=address.getCountryCode();
                            //startService(new Intent(getApplicationContext(), LoaderService.class));
                            geoFire.setLocation(myID, new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()), new GeoFire.CompletionListener() {

                                @Override
                                public void onComplete(String key, DatabaseError error) {
                                    startService(new Intent(getApplicationContext(), LoaderService.class));
                                    if (error != null) {
                                        System.err.println("There was an error saving the location to GeoFire: " + error);
                                    } else {
                                        System.out.println("Location saved on server successfully!");
                                    }
                                }
                            });

                        } else {
                            //getPermission();

                            myLocation=new GPSTracker(getApplicationContext()).getLocation();
                            getMyNearby(myLocation);
                            geoFire.setLocation(myID, new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()), new GeoFire.CompletionListener() {

                            @Override
                            public void onComplete(String key, DatabaseError error) {
                                startService(new Intent(getApplicationContext(), LoaderService.class));
                                if (error != null) {
                                    System.err.println("There was an error saving the location to GeoFire: " + error);
                                } else {
                                    System.out.println("Location saved on server successfully!");
                                }
                            }
                        });

                        }
                    }
                });

            }else{
                getMyNearby(myLocation);
                geoFire.setLocation(myID, new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()), new GeoFire.CompletionListener() {

                    @Override
                    public void onComplete(String key, DatabaseError error) {
                        startService(new Intent(getApplicationContext(), LoaderService.class));
                        if (error != null) {
                            System.err.println("There was an error saving the location to GeoFire: " + error);
                        } else {
                            System.out.println("Location saved on server successfully!");
                        }
                    }
                });
            }


        }
    }
private void getMyFriends(){
    DatabaseReference ref=mDatabase.child("Users").child(myID).child("Friends");
ref.addListenerForSingleValueEvent(new ValueEventListener() {
    @Override
    public void onDataChange(DataSnapshot dataSnapshot) {
        for (DataSnapshot friendSnap : dataSnapshot.getChildren()) {
Log.e("Friends"," "+friendSnap.getValue().toString());
FriendRequest request=friendSnap.getValue(FriendRequest.class);
            if(request.status&&!friendIds.contains(request.id)){
                friendIds.add(request.id);
            }

        }
    }

    @Override
    public void onCancelled(DatabaseError databaseError) {

    }
});
}
    private void getLocation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            new Permissive.Request(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                    .whenPermissionsGranted(new PermissionsGrantedListener() {
                        @Override
                        public void onPermissionsGranted(String[] permissions) throws SecurityException {
                            if (gpsTracker.canGetLocation) {
                                //Location location = gpsTracker.getLocation();
                                new SamLocationRequestService(MainActivity.this).executeService(new SamLocationRequestService.SamLocationListener() {
                                    @Override
                                    public void onLocationUpdate(Location location, Address address) {
                                        if (location != null) {
                                            myLocation = location;
                                            getMyNearby(location);
                                            geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 10).addGeoQueryEventListener(new GeoQueryEventListener() {
                                                @Override
                                                public void onKeyEntered(String key, GeoLocation location) {
                                                    //localUsers.add(key);
                                                }

                                                @Override
                                                public void onKeyExited(String key) {

                                                }

                                                @Override
                                                public void onKeyMoved(String key, GeoLocation location) {

                                                }

                                                @Override
                                                public void onGeoQueryReady() {


                                                    geoFire.setLocation(myID, new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()), new GeoFire.CompletionListener() {
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
                                                                        getPhotos(myID);
                                                                        getKeys();
                                                                        getMyInterests(myID);
                                                                        getMyFavs(myID);
                                                                        //getInterests(myID);
                                                                        //getUsers(myID);
                                                                    }
                                                                }).start();

                                                                //getNearby();
                                                            }
                                                        }


                                                    });
                                                }

                                                @Override
                                                public void onGeoQueryError(DatabaseError error) {

                                                }
                                            });

                                        }
                                        if (address != null) {
                                            String loc = "" + address.getLocality() + ", " + address.getCountryName();
                                            locationText.setText(loc);
                                        }


                                    }
                                });
                            } else {
                                gpsTracker.showSettingsAlert();
                            }


                            // given permissions are granted
                        }
                    })
                    .whenPermissionsRefused(new PermissionsRefusedListener() {
                        @Override
                        public void onPermissionsRefused(String[] permissions) {
                            // given permissions are refused
                        }
                    })
                    .execute(MainActivity.this);
        } else {
            if (myLocation != null) {
                myLocation = gpsTracker.location;
                getMyNearby(myLocation);

                geoFire.queryAtLocation(new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()), 10).addGeoQueryEventListener(new GeoQueryEventListener() {
                    @Override
                    public void onKeyEntered(String key, GeoLocation location) {
                       // localUsers.add(key);
                    }

                    @Override
                    public void onKeyExited(String key) {

                    }

                    @Override
                    public void onKeyMoved(String key, GeoLocation location) {

                    }

                    @Override
                    public void onGeoQueryReady() {
                        writeNewUser(myName, "", myID,myAvatarUrl);
                        geoFire.setLocation(myID, new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()), new GeoFire.CompletionListener() {
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
                                            getKeys();
                                            getMyInterests(myID);
                                            //getInterests(myID);
                                            //getUsers(myID);
                                        }
                                    }).start();

                                    //getNearby();
                                }
                            }


                        });
                    }

                    @Override
                    public void onGeoQueryError(DatabaseError error) {

                    }
                });

            }
            //Location location = gpsTracker.getLocation();

            else {
                gpsTracker.showSettingsAlert();
            }

        }
    }

    private void writeNewUser(String name, String email, String id,String url) {
        User user = new User(name, email, id, null, 0,myAvatarUrl);

        FirebaseDatabase.getInstance().getReference().child("Users").child(id).child("User").setValue(user);
        mDatabase.child("Users").child(id).child("Current Location").setValue(new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()));
        mDatabase.child("History").child(id).push().setValue(new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()));

        mDatabase.child("Users").child(id).child("Current Place").setValue(mCurrentPlace).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(getIntent().hasExtra("placeNotification")){
                    startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("placeNotification",true));

                }else if(getIntent().hasExtra("eventNotification")) {
                    startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("eventNotification",true));
                }else{
                    startActivity(new Intent(getApplicationContext(), MapsActivity.class));

                }

            }
        });
    }

    private void getPrefs() {
        DatabaseReference ref = mDatabase.child("Users").child(myID).child("Preferences");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    UserPreferences preferences = dataSnapshot.getValue(UserPreferences.class);
                    searchDistance = preferences.searchDistance;
                    distanceUnit = preferences.distanceUnit;
                    isNotified=preferences.isNotified;
                    isNightMode=preferences.isNightMode;
                    Log.w("Prefs", "Preferences " + dataSnapshot.getValue());
                } else {
                    Log.w("Prefs", "Preferences null ");

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        showGPSDisabledAlertToUser();
        myInterests.clear();
        latLngList.clear();
        historyList.clear();
        otherInterests.clear();
        matchedInterests.clear();
        matchedIDs.clear();
        friendIds.clear();
        myFavorites.clear();
        nearbyPlaces.clear();
        foundPlaces.clear();
        friendRequests.clear();
           getMyFriends();
           getPhotos(myID);

           getMyRequests();
           getPrefs();
           getMyFavs(myID);
           getMyInterests(myID);
           getPermission();

       /*
        if (gpsTracker.canGetLocation) {
            Log.e(TAG, "Getting Location");
            myLocation = gpsTracker.getLocation();
            //getLocation();

            getPermission();
        } else {
            gpsTracker.showSettingsAlert();
            Log.e(TAG, "Location Failed! ");


        }
        if(myLocation!=null){
            getMyNearby(myLocation);

        }else {
            myLocation=gpsTracker.getLocation();
        getMyNearby(myLocation);
        }
        */
        super.onResume();
    }
    private void showGPSDisabledAlertToUser(){
        LocationManager lm = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            AlertDialog.Builder dialog = new AlertDialog.Builder(context);
            dialog.setMessage(context.getResources().getString(R.string.gps_network_not_enabled));
            dialog.setPositiveButton(context.getResources().getString(R.string.action_settings), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    context.startActivity(myIntent);
                    //get gps
                }
            });
            dialog.setNegativeButton(context.getString(R.string.Cancel), new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            dialog.show();
        }
    }
    @Override
    protected void onStop() {
        super.onStop();
    }

    private void getHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.jailbird.scorch",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }


    private void getKeys() {
        if (myLocation == null) {
            //getLocation();
        }
        if (myLocation != null) {
            //LatLng me = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()), 20);
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(final String key, final GeoLocation location) {
                    if (!key.matches(myID)) {
                        new Thread(new Runnable() {
                            public void run() {
                                //getAllNearby(location);
                                getInterests(key);
                                //getUsers(key);

                                // LatLng person = new LatLng(location.latitude, location.longitude);

                                // latLngList.add(person);

                            }
                        }).start();
                        //matchedIDs.add(key);
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


    private void getInterests(final String id) {
        DatabaseReference ref = mDatabase.child("Interests");
        ref.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {


                if (dataSnapshot.getValue() != null) {
                    for (DataSnapshot postSnapshot : dataSnapshot.child(id).getChildren()) {

                        //Log.w("Map Interests", "Interest snapshot " + postSnapshot.getValue());

                        Interest post = postSnapshot.getValue(Interest.class);
                        otherInterests.add(post);
                        if (myInterests.contains(post) && !matchedIDs.contains(id)) {
                            matchedIDs.add(id);
                            getAllNearby(id);
                            getUsers(id);
                            Log.w("Map Interests", "Interest ID added" + id);

                        } else {
                            Log.w("Map Interests", "Interest ID already exists " + id);

                        }


                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {

    }

    @Override
    public void onPurchaseHistoryRestored() {
        Log.d("Paid","Restored"+isPaid);

        if(bp.listOwnedProducts().contains("remove_ads")){
    isPaid=true;
    Log.d("Paid","isPaid"+isPaid);
}
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {
error.printStackTrace();
    }

    @Override
    public void onBillingInitialized() {
        boolean loadOwnedPurchasesFromGoogle = bp.loadOwnedPurchasesFromGoogle();
        if (loadOwnedPurchasesFromGoogle) {
            if (bp.isPurchased("remove_ads")) {
                //Toast.makeText(context, "Product is already purchased", Toast.LENGTH_LONG).show();
                isPaid = true;
            }
        }
    }

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(HotPlaces item,User user,Place place);
    }

    void getNearby() {
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
                        for (noman.googleplaces.Place place : places) {

                            nearbyPlaces.add(place);
                        }
                    }

                    @Override
                    public void onPlacesFinished() {
                        //stopAnim();
                        if(getIntent().hasExtra("placeNotification")){

                            startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("placeNotification",true));

                        }else if(getIntent().hasExtra("eventNotification")) {
                            startActivity(new Intent(getApplicationContext(), MapsActivity.class).putExtra("eventNotification",true));
                        }else{
                            startActivity(new Intent(getApplicationContext(), MapsActivity.class));

                        }

                    }
                })
                .key(getString(R.string.map_api_key))
                .latlng(myLocation.getLatitude(), myLocation.getLongitude())
                .radius(4500)

                .build()
                .execute();
    }

    private void getPhotos(String id) {

        DatabaseReference ref = mDatabase.child("Users").child(id).child("User").child("photoUrl");

        ref.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.e("Post ", "Picture: " + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                    myAvatarUrl = (String) dataSnapshot.getValue();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    void getMyNearby(Location location) {
        final List<Place>placeList=new ArrayList<Place>();
        new NRPlaces.Builder()
                .listener(new PlacesListener() {
                    @Override
                    public void onPlacesFailure(PlacesException e) {
                        writeNewUser(myName, "", myID,myAvatarUrl);

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
                            mCurrentPlace = null;
                            Log.e("Near", "Place null");

                        }
                    }

                    @Override
                    public void onPlacesFinished() {
                        //stopAnim();
                        mCurrentPlace = placeList.get(0);

                        writeNewUser(myName, FirebaseAuth.getInstance().getCurrentUser().getEmail(), myID,myAvatarUrl);

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

    void getAllNearby(String id) {
        geoFire.getLocation(id, new LocationCallback() {
            @Override
            public void onLocationResult(String key, GeoLocation location) {
                LatLng person = new LatLng(location.latitude, location.longitude);

                latLngList.add(person);
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
                        .radius(20)

                        .build()
                        .execute();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void getMyInterests(final String id) {
        DatabaseReference ref = mDatabase.child("Interests");
        ref.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.child(id).getChildren()) {

                    Interest post = postSnapshot.getValue(Interest.class);
                    //Log.e("Get Data", post.<YourMethod>());
                   if(!myInterests.contains(post))
                    myInterests.add(post);
                    Log.w("Map Interests", "My Interest " + post.interestName.toString());

                }

             /*
                myInterests = (List<Interest>) dataSnapshot.child(id).getValue();
                for (Interest key : myInterests) {
                    Log.w("Map Interests", "My Interest " + key);

                }*/

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
                       if(!myFavorites.contains(post))
                        myFavorites.add(post);
                    }
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
private void getMyRequests(){
    DatabaseReference ref = mDatabase.child("Friend Requests").child(myID);
    ref.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            //Log.w("Favorites", "My Favs " + dataSnapshot.getValue());
            for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                if (postSnapshot.getValue() != null) {

                    FriendRequest friendRequest = postSnapshot.getValue(FriendRequest.class);
                    //Log.e("Get Data", post.<YourMethod>());
                    if(!friendRequests.contains(friendRequest))
                    friendRequests.add(friendRequest);
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
                    //Log.d("Location", "" + currentLocation.toString());

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

    private void placePhotosAsync(String id) {
        new PhotoTask(100, 100, mainApiClient, id) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
                //mImageView.setImageResource(R.drawable.empty_photo);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {
                    // Photo has been loaded, display it.
                    //  mImageView.setImageBitmap(attributedPhoto.bitmap)


                    //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon).putExtra("place name", pointOfInterest.name).putExtra("id",pointOfInterest.placeId));
                    // Display the attribution as HTML content if set.
                    if (attributedPhoto.attribution == null) {
                        //    mText.setVisibility(View.GONE);
                    } else {
                        //  mText.setVisibility(View.VISIBLE);
                        //mText.setText(Html.fromHtml(attributedPhoto.attribution.toString()));
                    }

                } else {
                    Log.e("Photo", "no photo");
                    //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.nightsml);


                    //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon).putExtra("place name", pointOfInterest.name).putExtra("id",pointOfInterest.placeId));

                }
            }
        }.execute(id);
    }

    private File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory()
                + "/Android/data/"
                + getApplicationContext().getPackageName()
                + "/Files");

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(new Date());
        File mediaFile;
        String mImageName = "MI_" + timeStamp + ".jpg";
        mediaFile = new File(mediaStorageDir.getPath() + File.separator + mImageName);
        return mediaFile;
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();

        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private void searchPlaces() {

        if (myLocation == null) {
            //getLocation();
        }


        for (int i = 0; i < myInterests.size(); i++) {
            final Interest mInterest = myInterests.get(i);
            Interest thisInterest = myInterests.get(i);
            for (final String type : myInterests.get(i).placeTypes) {
                try {
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
                                    Log.d("Places", " place list " + type + " " + places);
                                    Iterator iterator = places.iterator();
                                    while (iterator.hasNext()) {
                                        noman.googleplaces.Place p = (noman.googleplaces.Place) iterator.next();

                                        if (!foundPlaces.contains(p)) {
                                            foundPlaces.add(p);
                                            Log.d("Places", " place added " + p.getName());

                                        }
                                    }
                                    //foundPlaces.addAll(places);


                                }

                                @Override
                                public void onPlacesFinished() {
                                    int index = 0;

                                    for (noman.googleplaces.Place place : foundPlaces) {
                                        Log.d("Place", "Place Name " + place.getName());


                                        index++;
                                    }
                                    int count = 0;


                                }
                            })
                            .key(getString(R.string.map_api_key))
                            .latlng(myLocation.getLatitude(), myLocation.getLongitude())
                            .radius(3500)
                            .type(type)
                            .build()
                            .execute();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                }
            }

        }


    }


    void getPlace(String placeId) {
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                            final com.google.android.gms.location.places.Place myPlace = places.get(0);

                            Log.i(TAG, "Place found: " + myPlace.getName());
                        } else {
                            Log.e(TAG, "Place not found");
                        }
                        places.release();
                    }
                });
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}
