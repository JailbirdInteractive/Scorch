package com.jailbird.scorch;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.clockbyte.admobadapter.AdmobRecyclerAdapterWrapper;
import com.clockbyte.admobadapter.expressads.AdmobExpressRecyclerAdapterWrapper;
import com.codemybrainsout.placesearch.PlaceSearchDialog;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.facebook.share.model.AppInviteContent;
import com.facebook.share.widget.AppInviteDialog;
import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.gigamole.library.PulseView;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.maps.android.MarkerManager;
import com.google.maps.android.clustering.Cluster;
import com.google.maps.android.clustering.ClusterItem;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.ClusterManager.OnClusterClickListener;
import com.google.maps.android.clustering.view.ClusterRenderer;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.google.maps.android.heatmaps.HeatmapTileProvider;
import com.google.maps.android.ui.IconGenerator;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;
import com.meetic.marypopup.MaryPopup;
import com.nineoldandroids.animation.Animator;
import com.squareup.picasso.Picasso;
import com.wang.avi.AVLoadingIndicatorView;


import org.apache.commons.collections4.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.blurry.Blurry;
import noman.googleplaces.NRPlaces;
import noman.googleplaces.Place;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.jailbird.scorch.MainActivity.boostedPlaces;
import static com.jailbird.scorch.MainActivity.currBoostedPlace;
import static com.jailbird.scorch.MainActivity.currentEvent;
import static com.jailbird.scorch.MainActivity.historyList;
import static com.jailbird.scorch.MainActivity.isPaid;
import static com.jailbird.scorch.MainActivity.myID;
import static com.jailbird.scorch.MainActivity.myInterests;
import static com.jailbird.scorch.R.id.imageView;
import static com.jailbird.scorch.R.id.map;

public class MapsActivity extends AppCompatActivity implements View.OnClickListener, OnMapReadyCallback, GoogleMap.OnPoiClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleMap.InfoWindowAdapter, GoogleMap.OnInfoWindowCloseListener, GoogleMap.OnInfoWindowClickListener, MainActivity.OnListFragmentInteractionListener, InterestFragment.OnListFragmentInteractionListener, ClusterManager.OnClusterClickListener<MyItem>, ClusterManager.OnClusterItemClickListener<MyItem>, ClusterManager.OnClusterItemInfoWindowClickListener<MyItem>, ClusterManager.OnClusterInfoWindowClickListener<MyItem>,GoogleMap.OnMarkerClickListener {
    private static final String TAG = "Maps";
    private static final int REQUEST_INVITE = 999;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 111;

    private static final int ITEMS_PER_AD = 6;
    int PLACE_PICKER_REQUEST = 1;
    MaryPopup maryPopup,maryPopupI;
    private GoogleMap mMap;
    GeoFire geoFire;
    DatabaseReference mDatabase;
    Location myLocation;
    List<String> keys;
    Switch friendSwitch;
    List<Object>people;
    public static List<Interest> myInterests;
    private MainActivity.OnListFragmentInteractionListener mListener;
    private InterestFragment.OnListFragmentInteractionListener interactionListener;
    int index = 0;
    int distance = 3500;
    public static noman.googleplaces.Place thisPlace;
    List<Interest> otherInterests;
    List<LatLng> latLngList;
    private String myID = "User ID";
    private GPSTracker gpsTracker;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;
    List<PlaceInterest> placeInterestList;
    Interest thisInterest;
    List<PlaceInfo> placeInfoList = new ArrayList<>();
    public static GoogleApiClient mGoogleApiClient;
    ImageView infoPhoto;
    SpaceNavigationView spaceNavigationView;
    List<HotPlaces> hotPlaces = new ArrayList<>();
    List<Object> favs = new ArrayList<Object>();

    Bitmap placeIcon;
    android.support.v7.widget.Toolbar toolbar;
    AVLoadingIndicatorView progressBar;
    List<noman.googleplaces.Place> nearbyPlaces = new ArrayList<>();
    ArrayList<Place> placeIdList;
    RecyclerView recyclerView, interestRecyclerView;
    //private DrawerLayout drawerLayout;
    BottomSheetBehavior behavior;
    private ClusterManager<MyItem> mClusterManager;
    List<MyItem> clusterList;
    TextView historyText;
    TextView currentText;
    String myName = "User Name";
    private boolean isCurrent = true;
    private int mDimension;
    private IconGenerator mIconGenerator;
    Context context;
    public static User currentFriend;
    AdmobExpressRecyclerAdapterWrapper adapterWrapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(100, 100);
        MobileAds.initialize(getApplicationContext(), getString(R.string.native_ad_unit_id));
people=new ArrayList<Object>();


        placeInterestList = new ArrayList<>();
        infoPhoto = new ImageView(MapsActivity.this);
        infoPhoto.setLayoutParams(layoutParams);
        nearbyPlaces = MainActivity.nearbyPlaces;
        distance = MainActivity.searchDistance * 100;
        historyText = (TextView) findViewById(R.id.history_text);
        currentText = (TextView) findViewById(R.id.current_text);
        historyText.setOnClickListener(this);
        currentText.setOnClickListener(this);
        currentText.setSelected(true);
        currentText.setTextColor(Color.WHITE);
        mIconGenerator = new IconGenerator(getApplicationContext());
        context = this;
        mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
        mIconGenerator.setColor(getResources().getColor(R.color.colorAccent));
        friendSwitch= (Switch) findViewById(R.id.friend_switch);
       /*
        if (getIntent().hasExtra("placeIds")) {
            placeIdList = getIntent().getStringArrayListExtra("placeIds");
            if (!placeIdList.isEmpty()) {
                getHotPlaces(false);
                Log.e("Hot Places", "Size " + placeIdList.size());
            } else {
                placeIdList = MainActivity.placeIdList;
                getHotPlaces(false);
                Log.e("Hot Places", "Empty ");
            }

        } else {
            placeIdList = new ArrayList<>();

        }*/
        placeIdList = MainActivity.placeIdList;
        if (!MainActivity.currentPlaces.isEmpty()) {
            //getHotPlaces(true);
        }
        mListener = this;
        interactionListener = new InterestFragment.OnListFragmentInteractionListener() {
            @Override
            public void onListFragmentInteraction(Interest item) {
                if (myInterests.contains(item)) {
                    myInterests.remove(item);
                    Log.d("Filter", " Removed " + item.interestName);
                } else {
                    myInterests.add(item);
                    Log.d("Filter", " Added " + item.interestName);
                }
            }
        };
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        interestRecyclerView = (RecyclerView) findViewById(R.id.interest_layout);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        interestRecyclerView.setLayoutManager(gridLayoutManager);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        if (recyclerView.hasFocus()) {
            recyclerView.setAlpha(1);
        } else {
            recyclerView.setAlpha(.5f);
        }
        myInterests = new ArrayList<Interest>();
        if (MainActivity.myInterests != null) {
            for (Interest i : MainActivity.myInterests) {
                myInterests.add(i);
            }
        }
        //myInterests = MainActivity.myInterests;
        int spanCount = 3; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = false;
        interestRecyclerView.addItemDecoration(new DividerItemDecoration(spanCount, spacing, includeEdge));
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)

                .enableAutoManage(this, this)
                .build();

        placeIcon = null;
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            //actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            //actionBar.setDisplayHomeAsUpEnabled(true);

        }
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
// The View with the BottomSheetBehavior
        View bottomSheet = coordinatorLayout.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
                Log.e("onStateChanged", "onStateChanged:" + newState);
                if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                    //fab.setVisibility(View.GONE);
                } else {
                    //fab.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
                Log.e("onSlide", "onSlide");
            }
        });
        behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        behavior.setPeekHeight(10);

/*
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        NavigationView view = (NavigationView) findViewById(R.id.navigation_view);

        view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Snackbar.make(this, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();

                menuItem.setChecked(true);

                drawerLayout.closeDrawers();

                return true;

            }

        });
*/
        progressBar = (AVLoadingIndicatorView) findViewById(R.id.progressBar);
        stopAnim();


        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.space);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("Hot", R.drawable.ic_stat_light_fire));

        spaceNavigationView.addSpaceItem(new SpaceItem("Places", R.drawable.ic_action_home_marker));
        //spaceNavigationView.addSpaceItem(new SpaceItem("Add Places", R.drawable.ic_action_add_place));

        spaceNavigationView.addSpaceItem(new SpaceItem("Favorites", R.drawable.ic_favorite_white_24dp));
        spaceNavigationView.addSpaceItem(new SpaceItem("Feed", R.drawable.ic_action_dashboard));
        spaceNavigationView.showIconOnly();

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Toast.makeText(MapsActivity.this, "Search", Toast.LENGTH_SHORT).show();

                showSearchDialog();
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Toast.makeText(MapsActivity.this, " " + itemName, Toast.LENGTH_SHORT).show();
                switch (itemIndex) {
                    case 0:
                        if (recyclerView.getVisibility() == View.VISIBLE) {
                            YoYo.with(Techniques.SlideOutUp).withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    recyclerView.setVisibility(View.GONE);
                                    addHeatMap(isCurrent);

                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            }).playOn(recyclerView);
                        }

                        if (mClusterManager != null) {
                            mClusterManager.clearItems();
                            mClusterManager = null;
                        }
                        break;
                    case 1:
                        if (mOverlay != null)
                            mOverlay.remove();
                        searchPlaces();
                        break;

                    case 2:
                        //startActivity(new Intent(getApplicationContext(), PickerActivity.class));
                        if (mClusterManager != null)
                            mClusterManager.clearItems();

                        if (recyclerView.getVisibility() == View.VISIBLE) {
                            YoYo.with(Techniques.SlideOutUp).withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    recyclerView.setVisibility(View.GONE);
                                    showFavs();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            }).playOn(recyclerView);
                        }

                        break;

                    case 3:
                        //signOut();
                        //Toast.makeText(MapsActivity.this, "Signing out", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), FeedActivity.class));
                        //startActivity(new Intent(getApplicationContext(), AccountActivity.class));

                        break;
  /*
                    case 2:
                        onPickButtonClick();
*/
                }
            }


            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch (itemIndex) {
                    case 0:
                        if (recyclerView.getVisibility() == View.VISIBLE) {
                            YoYo.with(Techniques.SlideOutUp).withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    recyclerView.setVisibility(View.GONE);
                                    addHeatMap(isCurrent);

                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            }).playOn(recyclerView);
                        }
                        //mMap.clear();
                        if (mClusterManager != null) {
                            mClusterManager.clearItems();
                            mClusterManager = null;
                        }
                        break;
                    case 1:
                        if (mOverlay != null)
                            mOverlay.remove();
                        searchPlaces();
                        break;
                    case 2:
                        //startActivity(new Intent(getApplicationContext(), PickerActivity.class));
                     //onPickButtonClick();

                        if (mClusterManager != null) {
                            mClusterManager.clearItems();
                            mClusterManager = null;
                        }
                        if (recyclerView.getVisibility() == View.VISIBLE) {
                            YoYo.with(Techniques.SlideOutUp).withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    recyclerView.setVisibility(View.GONE);
                                    showFavs();
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            }).playOn(recyclerView);
                        }

                        break;
                    case 3:
                        //signOut();
                        //Toast.makeText(MapsActivity.this, "Signing out", Toast.LENGTH_SHORT).show();
                        //startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        //startActivity(new Intent(getApplicationContext(), CreateEventActivity.class));
                        startActivity(new Intent(getApplicationContext(), FeedActivity.class));

                        break;
                }
                Toast.makeText(MapsActivity.this, " " + itemName, Toast.LENGTH_SHORT).show();
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        DatabaseReference ref = mDatabase.child("Locations");
        geoFire = new GeoFire(ref);
        keys = new ArrayList<>();

        otherInterests = new ArrayList<>();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if(FirebaseAuth.getInstance().getCurrentUser().getEmail()!=null) {

                String id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String com = ".com";
                String rep = "";
                if (id != null) {
                    String sub = id.replace(com, rep);
                    myID = sub.replaceAll("[-+.^:,]", "");
                }
            }else{
                myID=FirebaseAuth.getInstance().getCurrentUser().getUid();
            }


//            myID = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            myName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        //getMyInterests(myID);
        //gpsTracker = new GPSTracker(getApplicationContext());
        latLngList = new ArrayList<>();

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
private void openNotificationPlace(Place place){
        //placePhotosAsync(place.getPlaceId());


        //List<String> types = place.getTypes();
        //PlaceInfo placeInfo = new PlaceInfo(place, null);
        thisPlace = place;
        startActivity(new Intent(getApplicationContext(), PlaceInfoActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        currBoostedPlace=null;
    }

    private void openNotificationEvent(Event event){
        //placePhotosAsync(place.getPlaceId());


        //List<String> types = place.getTypes();
        //PlaceInfo placeInfo = new PlaceInfo(place, null);
        startActivity(new Intent(getApplicationContext(), EventActivity.class).putExtra("notification",true).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        currentEvent=null;
    }
    private void setUpClusterer(List<MyItem> items) {
        // Position the map.

        getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), 13));

        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        if (mClusterManager == null) {
            mClusterManager = new ClusterManager<MyItem>(this, getMap());
            //mClusterManager.clearItems();
            mClusterManager.setRenderer(new MyItemRenderer());
            // Point the map's listeners at the listeners implemented by the cluster
            // manager.
            getMap().setOnCameraIdleListener(mClusterManager);
            getMap().setOnMarkerClickListener(mClusterManager);
            getMap().setOnInfoWindowClickListener(mClusterManager);
            if(mClusterManager.getMarkerManager().getCollection("friends")!=null)
            mClusterManager.getMarkerManager().getCollection("friends").setOnMarkerClickListener(MapsActivity.this);
            mClusterManager.setOnClusterClickListener(this);
            mClusterManager.setOnClusterInfoWindowClickListener(this);
            mClusterManager.setOnClusterItemClickListener(this);
            mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        }else{
            mClusterManager.setRenderer(new MyItemRenderer());
            // Point the map's listeners at the listeners implemented by the cluster
            // manager.
            getMap().setOnCameraIdleListener(mClusterManager);
            getMap().setOnMarkerClickListener(mClusterManager);
            getMap().setOnInfoWindowClickListener(mClusterManager);
//if(mClusterManager.getMarkerManager().getCollection("friends")!=null)
         //   mClusterManager.getMarkerManager().getCollection("friends").setOnMarkerClickListener(MapsActivity.this);
            mClusterManager.setOnClusterClickListener(this);
           // mClusterManager.setOnClusterInfoWindowClickListener(this);
            mClusterManager.setOnClusterItemClickListener(this);
           // mClusterManager.setOnClusterItemInfoWindowClickListener(this);
        }
        // Add cluster items (markers) to the cluster manager.
        addItems(items);
    }

    private void addItems(List<MyItem> items) {
        mClusterManager.clearItems();
        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < items.size(); i++) {

            MyItem offsetItem = items.get(i);

            mClusterManager.addItem(offsetItem);
            mClusterManager.cluster();
        }

        stopAnim();
    }

    private void getHotPlaces(boolean current) {
        if (current) {
            for (Place place : MainActivity.currentPlaces) {
                int count = Collections.frequency(MainActivity.currentPlaces, place);
                HotPlaces hotPlace = new HotPlaces(place, count);
                if (!hotPlaces.contains(hotPlace)) {
                    hotPlaces.add(hotPlace);
                    Log.e("Hot Places", "Hot Place Added. " + place.getPlaceId() + " Occurs " + count);

                }
            }
        } else {
            for (int i = 0; i < placeIdList.size(); i++) {
                int c = Collections.frequency(placeIdList, placeIdList.get(i));
                Log.e("Hot Places", "Occurs " + c);

                int occurrences = Collections.frequency(placeIdList, placeIdList.get(i));
                HotPlaces hp = new HotPlaces(placeIdList.get(i), c);
                if (!hotPlaces.contains(hp)) {
                    hotPlaces.add(hp);

                    // Log.e("Hot Places", "Hot Place Added. " + p.getPlaceId() + " Occurs " + occurrences);
                }
                // }
            }
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        spaceNavigationView.onSaveInstanceState(outState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getLocation();
        thisPlace = null;
        placeIcon = null;
        myLocation = MainActivity.myLocation;
      if(MainActivity.myInterests!=null) {
          if (MainActivity.myInterests.size() > 0) {
              //stopService(new Intent(getApplicationContext(),GPSTracker.class));
              interestRecyclerView.setAdapter(new InterestFilterAdapter(MainActivity.myInterests, interactionListener, this));

          }
      }else{
          startActivity(new Intent(context,LoadingActivity.class));
      }
        //getHotPlaces(false);
        if(MainActivity.isNightMode)
            checkTime();

        if(getIntent().hasExtra("placeNotification")) {
            if (getIntent().getBooleanExtra("placeNotification", false)) {
                if (MainActivity.currBoostedPlace != null)
                    openNotificationPlace(MainActivity.currBoostedPlace);
            }
        }
       /* if(getIntent().hasExtra("eventNotification")){
            if (getIntent().getBooleanExtra("eventNotification", false)) {

                if (currentEvent != null) {
                    //startActivity(new Intent(context,EventActivity.class).putExtra("notification",true));
                    openNotificationEvent(currentEvent);
                }
            }
        }*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //drawerLayout.openDrawer(GravityCompat.START);
                return true;
            case R.id.filter:
                if (behavior.getState() == BottomSheetBehavior.STATE_HIDDEN) {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }
                break;
            case R.id.change_view:
                startActivity(new Intent(context,ListActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        int badge = MainActivity.friendRequests.size()+MainActivity.placeInviteList.size();
        if (badge > 0&&spaceNavigationView.getChildCount()>0)
            spaceNavigationView.showBadgeAtIndex(3, badge, getResources().getColor(R.color.colorAccent));
        //markerManager=new MarkerManager(mMap);
        mClusterManager = new ClusterManager<MyItem>(context,mMap);

        mMap.setMyLocationEnabled(true);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        mMap.setOnPoiClickListener(this);
        mMap.setInfoWindowAdapter(this);
        mMap.setOnInfoWindowCloseListener(this);
        mMap.setOnInfoWindowClickListener(this);
mClusterManager.getMarkerManager().newCollection("friends");
        //mClusterManager.getMarkerManager().newCollection("hots");
        mClusterManager.getMarkerManager().getCollection("friends").setOnMarkerClickListener(this);
        //markerManager.newCollection("friends");
        mMap.setOnMarkerClickListener(mClusterManager);
        friendSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(mClusterManager==null){
                    mClusterManager = new ClusterManager<MyItem>(context,mMap);
                    mClusterManager.getMarkerManager().newCollection("friends");
                    mClusterManager.getMarkerManager().getCollection("friends").setOnMarkerClickListener(MapsActivity.this);
                    //markerManager.newCollection("friends");
                    //mMap.setOnMarkerClickListener(mClusterManager);
                    mMap.clear();
                    getFriends();
                }
                if(b){
                   if(mClusterManager.getMarkerManager().getCollection("friends")!=null) {
                       for (Marker m : mClusterManager.getMarkerManager().getCollection("friends").getMarkers()) {
                           m.setVisible(true);
                       }
                   }
                }else{
                    if(mClusterManager.getMarkerManager().getCollection("friends")!=null){
                    for(Marker m:mClusterManager.getMarkerManager().getCollection("friends").getMarkers()) {
                        m.setVisible(false);
                    }
                    }
                }
            }
        });

/*
       mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                //noman.googleplaces.Place place = (noman.googleplaces.Place) marker.getTag();
               // placePhotosAsync(place.getPlaceId());
                if(marker.getTag() instanceof Friend){
                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class).putExtra("userId",((Friend) marker.getTag()).friend.userID).putExtra("photoUrl",((Friend) marker.getTag()).friend.photoUrl).putExtra("userName",((Friend) marker.getTag()).friend.username));
                }else{
                    noman.googleplaces.Place place = (noman.googleplaces.Place) marker.getTag();
                    placePhotosAsync(place.getPlaceId());


                    List<String> types = place.getTypes();
                    //PlaceInfo placeInfo = new PlaceInfo(place, null);
                    thisPlace = place;
                    startActivity(new Intent(getApplicationContext(), PlaceInfoActivity.class).putExtra("image", placeIcon));
                }
                return false;
            }
        });
*/
        try {
            // Customise the styling of the base map using a JSON object defined
            // in a raw resource file.
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.day_map));

            if (!success) {
                Log.e("MapsActivityRaw", "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e("MapsActivityRaw", "Can't find style.", e);
        }
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(13);
        if (MainActivity.myLocation == null && mMap.isMyLocationEnabled() && mMap.getMyLocation() != null) {
            MainActivity.myLocation = mMap.getMyLocation();
            myLocation = mMap.getMyLocation();
        }
        if (myLocation == null) {
            myLocation = MainActivity.myLocation;
        } else {
            LatLng me = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(me));
            mMap.animateCamera(zoom);
        }

        if(MainActivity.isNightMode)
        checkTime();
        //getKeys();
//------Remember to Change Location source!!-------
        getFriends();

    }

    private void checkTime() {
        Calendar c = Calendar.getInstance();
        int timeOfDay = c.get(Calendar.HOUR_OF_DAY);

        if (timeOfDay >= 0 && timeOfDay < 12) {
            Toast.makeText(this, "Good Morning", Toast.LENGTH_SHORT).show();
        } else if (timeOfDay >= 12 && timeOfDay < 16) {
            Toast.makeText(this, "Good Afternoon", Toast.LENGTH_SHORT).show();
        } else if (timeOfDay >= 16 && timeOfDay < 21) {
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.night_map));

                if (!success) {
                    Log.e("MapsActivityRaw", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e("MapsActivityRaw", "Can't find style.", e);
            }
            Toast.makeText(this, "Good Evening", Toast.LENGTH_SHORT).show();
        } else if (timeOfDay >= 21 && timeOfDay < 24) {
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = mMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                this, R.raw.night_map));

                if (!success) {
                    Log.e("MapsActivityRaw", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e("MapsActivityRaw", "Can't find style.", e);
            }
            Toast.makeText(this, "Good Night", Toast.LENGTH_SHORT).show();
        }
    }

    private void getKeys() {
        if (myLocation == null) {
            myLocation = MainActivity.myLocation;
        }
        if (myLocation != null) {
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
            //mMap.addMarker(new MarkerOptions().position(sydney).title(key));
            LatLng me = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLng(me));
            mMap.animateCamera(zoom);
            GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(myLocation.getLatitude(), myLocation.getLongitude()), 10);
            geoQuery.addGeoQueryEventListener(new GeoQueryEventListener() {
                @Override
                public void onKeyEntered(String key, GeoLocation location) {
                   /* if (!key.matches(myID))
                        getInterests(key);
*/
                    LatLng person = new LatLng(location.latitude, location.longitude);
                    latLngList.add(person);

                }

                @Override
                public void onKeyExited(String key) {

                }

                @Override
                public void onKeyMoved(String key, GeoLocation location) {
                }

                @Override
                public void onGeoQueryReady() {
                    //addHeatMap();
                    getNearby();

                }

                @Override
                public void onGeoQueryError(DatabaseError error) {

                }
            });


        }
    }


    private void getInterests(final String id) {
        DatabaseReference ref = mDatabase.child("Interests");
        ref.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Log.w("Map Interests", "Interest " + dataSnapshot.child(id).getValue().toString());
                if (dataSnapshot != null) {
                    //Log.e("Map Interests", "Other Interest " + dataSnapshot.toString());

                    if (dataSnapshot.getValue() != null) {
                        Interest post = dataSnapshot.getValue(Interest.class);
                        otherInterests.add(post);
                        //otherInterests = (List<Interest>) dataSnapshot.child(id).getValue();
                        for (Interest key : otherInterests) {
                            Log.e("Map Interests", "Other Interest " + key);

                            if (myInterests.contains(key)) {
                                Log.e("Map Interests", "Interest matches " + key);

                            }
                            if (myInterests.contains(key) && !keys.contains(id)) {
                                keys.add(id);
                                Log.w("Map Interests", "Interest added" + id);

                            } else {
                                Log.w("Map Interests", "Interest already exists" + id);

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

    public static Bitmap tintImage(Bitmap bitmap, int color) {
        Paint paint = new Paint();
        paint.setColorFilter(new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN));
        Bitmap bitmapResult = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmapResult);
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmapResult;
    }

    private void showCustomDialog() {
        String name = "";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        View mView;
        Dialog mDialog;
        LayoutInflater mInflater = (LayoutInflater) getBaseContext().getSystemService(
                LAYOUT_INFLATER_SERVICE);
        ContextThemeWrapper mTheme = new ContextThemeWrapper(this,
                R.style.YOUR_STYE);

        mView = mInflater.inflate(R.layout.share_dialog, null);
        // mDialog = new Dialog(this,0); // context, theme

        mDialog = new Dialog(mTheme);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(mView);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
        ImageButton facebookButton = (ImageButton) mView.findViewById(R.id.facebook_button);
        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inviteFriends();
            }
        });
        ImageButton googlePlusButton = (ImageButton) mView.findViewById(R.id.google_button);
        googlePlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onInviteClicked();
            }
        });
        ImageButton shareButton = (ImageButton) mView.findViewById(R.id.share_button);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String shareBody = "Yor friend" + " " + getString(R.string.invitation_message);
                String body = getString(R.string.join_us) + " " + getResources().getString(R.string.invitation_deep_link);
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, shareBody);
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, body);
                startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_using)));
            }
        });
        // do some thing with the text view or any other view

    }

    private void inviteFriends() {
        String appLinkUrl, previewImageUrl;

        appLinkUrl = getString(R.string.fb_link);
        previewImageUrl = "https://www.Jailbirdinteractive.com/images/newfire.png";

        if (AppInviteDialog.canShow()) {
            AppInviteContent content = new AppInviteContent.Builder()
                    .setApplinkUrl("https://fb.me/573949842798289")

                    .setPreviewImageUrl(previewImageUrl)

                    .build();
            AppInviteDialog.show(this, content);
        }
    }

    private void onInviteClicked() {
        String name = "";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            name = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage("" + name + " " + getString(R.string.invitation_message))
                //.setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                .setCustomImage(Uri.parse("https://jailbirdinteractive.com/images/newfire.png"))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }
private void hotInvite(){
    View popup = LayoutInflater.from(context).inflate(R.layout.marker_layout, null, false);
    PulseView pulseView = (PulseView) popup.findViewById(R.id.pv);
    pulseView.startPulse();
    Button share = (Button) popup.findViewById(R.id.share_button);
    share.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            showCustomDialog();
        }
    });
    /*
    maryPopup = MaryPopup.with((Activity) context)
            .cancellable(true)
            .blackOverlayColor(Color.parseColor("#DD444444"))
            //.backgroundColor(Color.parseColor("#EFF4F5"))
            .content(popup)

            .from(toolbar)
            .center(true);
    maryPopup.show();
*/
    ContextThemeWrapper mTheme = new ContextThemeWrapper(this,
            R.style.YOUR_STYE);

    // mDialog = new Dialog(this,0); // context, theme

    Dialog mDialog = new Dialog(mTheme);
    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
    mDialog.setContentView(popup);
    mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
    lp.copyFrom(mDialog.getWindow().getAttributes());
    lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
    lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
    lp.gravity = Gravity.CENTER;

    mDialog.getWindow().setAttributes(lp);
    mDialog.show();
}
    private void addHeatMap(final boolean current) {
        //hotInvite();

        if (MainActivity.latLngList.size() <= 0 && MainActivity.historyList.size() <= 0) {
           hotInvite();
        }else {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... voids) {
                    if (current) {
                        latLngList = MainActivity.latLngList;

                        for (Place place : MainActivity.currentPlaces) {
                            int count = Collections.frequency(MainActivity.currentPlaces, place);
                            HotPlaces hotPlace = new HotPlaces(place, count);
                            if (!hotPlaces.contains(hotPlace) && hotPlace.getCount() > 1) {
                                hotPlaces.add(hotPlace);
                                Log.e("Hot Places", "Hot Place Added. " + place.getPlaceId() + " Occurs " + count);

                            }
                        }
                    } else {
                        latLngList = historyList;
                        for (int i = 0; i < placeIdList.size(); i++) {
                            int c = Collections.frequency(placeIdList, placeIdList.get(i));
                            Log.e("Hot Places", "Occurs " + c);

                            int occurrences = Collections.frequency(placeIdList, placeIdList.get(i));
                            HotPlaces hp = new HotPlaces(placeIdList.get(i), c);
                            if (!hotPlaces.contains(hp) && hp.getCount() > 1) {
                                hotPlaces.add(hp);

                                // Log.e("Hot Places", "Hot Place Added. " + p.getPlaceId() + " Occurs " + occurrences);
                            }
                            // }
                        }
                    }


                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    //LinkedHashSet<HotPlaces> hashSet = new LinkedHashSet<HotPlaces>();
                    //hashSet.addAll(hotPlaces);
                    //foundPlaces.clear();
                    // hotPlaces.clear();
                    // hotPlaces.addAll(hashSet);
                    //Collections.sort(hotPlaces);
                    HotPlacesAdapter adapter = new HotPlacesAdapter(hotPlaces, mListener);
                    adapter.setHasStableIds(true);
                    recyclerView.removeAllViews();
                    recyclerView.setAdapter(adapter);
                    YoYo.with(Techniques.SlideInDown).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).playOn(recyclerView);
                    stopAnim();

                    // Create a heat map tile provider, passing it the latlngs of the police stations.
                    if (latLngList.size() > 0) {
                        mProvider = new HeatmapTileProvider.Builder()
                                .data(latLngList)
                                .radius(50)

                                .build();
                        // Add a tile overlay to the map, using the heat map tile provider.
                        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
                    }
                    if (hotPlaces.size() > 0) {
                        for (HotPlaces hotPlace : hotPlaces) {
                            Place p = hotPlace.getPlace();
                      /*
                        if (mClusterManager != null) {
                            if (mClusterManager.getMarkerManager().getCollection("hots") != null) {
                                mClusterManager.getMarkerManager().getCollection("hots").addMarker(new MarkerOptions()

                                        .position(new LatLng(p.getLatitude(), p.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_white_36dp))
                                        .title(p.getName()))
                                        .setTag(p);
                                super.onPostExecute(aVoid);
                                //markerManager.getCollection("friends").setOnMarkerClickListener(MapsActivity.this);
                            } else {
                                mClusterManager.getMarkerManager().newCollection("hots");
                                mClusterManager.getMarkerManager().getCollection("hots").addMarker(new MarkerOptions()

                                        .position(new LatLng(p.getLatitude(), p.getLongitude()))
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_white_36dp))
                                        .title(p.getName())).setTag(p);
                            }


                        }
                        */

                        }

                    }

                    super.onPostExecute(aVoid);
                }
            }.execute();
        }
    }

    private void showFavs() {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                for (Place place : MainActivity.myFavorites) {
                    int count = Collections.frequency(MainActivity.currentPlaces, place);
                    HotPlaces hotPlace = new HotPlaces(place, count);
                    if (!favs.contains(hotPlace)) {
                        favs.add(hotPlace);
                        //Log.e("Hot Places", "Hot Place Added. " + place.getPlaceId() + " Occurs " + count);

                    }
                }
                if(!isPaid)
                addNativeFavExpressAds();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                if(!isPaid)
                setUpAndLoadFavNativeExpressAds();

                MyPlaceItemRecyclerViewAdapter adapter = new MyPlaceItemRecyclerViewAdapter(favs, mListener);
                adapter.setHasStableIds(true);
                recyclerView.removeAllViews();
                recyclerView.setAdapter(adapter);
                YoYo.with(Techniques.SlideInDown).withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        recyclerView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                }).playOn(recyclerView);
                super.onPostExecute(aVoid);
                for (Place p : MainActivity.myFavorites) {
                    Marker poi = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(p.getLatitude(), p.getLongitude()))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_white_36dp))
                            .title(p.getName()));
                    poi.setTag(p);
                }
                if (MainActivity.myFavorites.size() > 0)
                    mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(MainActivity.myFavorites.get(0).getLatitude(), MainActivity.myFavorites.get(0).getLongitude())));

                //progressBar.setVisibility(View.GONE);
                stopAnim();

            }
        }.execute();

    }

    private void getMyInterests(final String id) {
        DatabaseReference ref = mDatabase.child("Interests");
        ref.orderByKey().equalTo(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot postSnapshot : dataSnapshot.child(id).getChildren()) {

                    Interest post = postSnapshot.getValue(Interest.class);
                    //Log.e("Get Data", post.<YourMethod>());
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


    private void getLocation() {
        if (gpsTracker.canGetLocation()) {
            myLocation = gpsTracker.getLocation();
            Log.w("Map Location", "My Location " + myLocation);

        } else {
            gpsTracker.showSettingsAlert();
        }

    }

    private void placePhotosTask(final PointOfInterest pointOfInterest) {
        final String placeId = "ChIJrTLr-GyuEmsRBfy61i59si0"; // Australian Cruise Group

        // Create a new AsyncTask that displays the bitmap and attribution once loaded.
        new PhotoTask(200, 200, mGoogleApiClient, pointOfInterest.placeId) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
                //mImageView.setImageResource(R.drawable.empty_photo);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {
                    // Photo has been loaded, display it.
                    //  mImageView.setImageBitmap(attributedPhoto.bitmap);
                    placeIcon = attributedPhoto.bitmap;
                   /*
                    Marker poi = mMap.addMarker(new MarkerOptions()
                            .position(pointOfInterest.latLng)
                            //.icon(BitmapDescriptorFactory.fromBitmap(placeIcon))
                            .title(pointOfInterest.name));
*/
                    //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon).putExtra("place name", pointOfInterest.name).putExtra("id",pointOfInterest.placeId));
                    //poi.showInfoWindow();
                    // Display the attribution as HTML content if set.
                    if (attributedPhoto.attribution == null) {
                        //    mText.setVisibility(View.GONE);
                    } else {
                        //  mText.setVisibility(View.VISIBLE);
                        //mText.setText(Html.fromHtml(attributedPhoto.attribution.toString()));
                    }

                } else {
                    placeIcon = null;
                    Log.e("Photo", "no photo");
                    //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.nightsml);
/*
                    Marker poi = mMap.addMarker(new MarkerOptions()
                            .position(pointOfInterest.latLng)

                            .title(pointOfInterest.name));

                    poi.showInfoWindow();
  */
                    //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon).putExtra("place name", pointOfInterest.name).putExtra("id",pointOfInterest.placeId));

                }
                getPlaceInfo(pointOfInterest);
            }
        }.execute(pointOfInterest.placeId);
    }

    private ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback
            = new ResultCallback<PlacePhotoResult>() {
        @Override
        public void onResult(PlacePhotoResult placePhotoResult) {
            if (!placePhotoResult.getStatus().isSuccess()) {
                Log.e("Photo", "result " + placePhotoResult.getStatus().toString());


            }
            placeIcon = placePhotoResult.getBitmap();
            infoPhoto.setImageBitmap(placePhotoResult.getBitmap());
        }
    };

    private void placePhotosAsync(String id) {
        new PhotoTask(200, 200, mGoogleApiClient, id) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
                //mImageView.setImageResource(R.drawable.empty_photo);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {
                    // Photo has been loaded, display it.
                    //  mImageView.setImageBitmap(attributedPhoto.bitmap);
                    placeIcon = attributedPhoto.bitmap;


                    //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon).putExtra("place name", pointOfInterest.name).putExtra("id",pointOfInterest.placeId));
                    // Display the attribution as HTML content if set.
                    if (attributedPhoto.attribution == null) {
                        //    mText.setVisibility(View.GONE);
                    } else {
                        //  mText.setVisibility(View.VISIBLE);
                        //mText.setText(Html.fromHtml(attributedPhoto.attribution.toString()));
                    }

                } else {
                    placeIcon = null;
                    Log.e("Photo", "no photo");
                    //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.nightsml);


                    //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon).putExtra("place name", pointOfInterest.name).putExtra("id",pointOfInterest.placeId));

                }
            }
        }.execute(id);
    }

    @Override
    public void onPoiClick(final PointOfInterest pointOfInterest) {
//placePhotosAsync(pointOfInterest.placeId);
        placePhotosTask(pointOfInterest);
        getPlaceInfo(pointOfInterest);

        mMap.animateCamera(CameraUpdateFactory.newLatLng(pointOfInterest.latLng));

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private View prepareInfoView(Marker marker) {
        //prepare InfoView programmatically
        LinearLayout infoView = new LinearLayout(MapsActivity.this);
        LinearLayout.LayoutParams infoViewParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        infoView.setOrientation(LinearLayout.HORIZONTAL);
        infoView.setLayoutParams(infoViewParams);

        ImageView infoImageView = new ImageView(MapsActivity.this);
        //Drawable drawable = getResources().getDrawable(R.mipmap.ic_launcher);
        Drawable drawable = getResources().getDrawable(android.R.drawable.ic_dialog_map);
        if (placeIcon != null) {
            infoImageView.setImageBitmap(placeIcon);
        } else {
            infoImageView.setImageDrawable(drawable);
        }
        if(marker.getTag()instanceof Friend){
            String url=((Friend) marker.getTag()).friend.photoUrl;
            Picasso.with(context).load(url).resize(90,90).placeholder(R.drawable.ic_account_circle_black_48dp).into(infoImageView);
        }
        infoView.addView(infoImageView);

        LinearLayout subInfoView = new LinearLayout(MapsActivity.this);
        LinearLayout.LayoutParams subInfoViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        subInfoView.setOrientation(LinearLayout.VERTICAL);
        subInfoView.setLayoutParams(subInfoViewParams);

        TextView subInfoLat = new TextView(MapsActivity.this);
        subInfoLat.setText(marker.getTitle());
        TextView subInfoLnt = new TextView(MapsActivity.this);
        //subInfoLnt.setText("" + marker.getSnippet());
        subInfoView.addView(subInfoLat);
        subInfoView.addView(subInfoLnt);
        infoView.addView(subInfoView);

        return infoView;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return prepareInfoView(marker);
    }

    @Override
    public void onInfoWindowClose(Marker marker) {
        marker.remove();
        placeIcon = null;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {

        if(marker.getTag() instanceof Friend){
            Friend friend= (Friend) marker.getTag();
            currentFriend=friend.friend;
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(friend.friendLocation, 15));

            startActivity(new Intent(getApplicationContext(),ProfileActivity.class).putExtra("userId",((Friend) marker.getTag()).friend.userID).putExtra("photoUrl",((Friend) marker.getTag()).friend.photoUrl).putExtra("userName",((Friend) marker.getTag()).friend.username));

        }else{
            noman.googleplaces.Place place = (noman.googleplaces.Place) marker.getTag();
            placePhotosAsync(place.getPlaceId());


            List<String> types = place.getTypes();
            //PlaceInfo placeInfo = new PlaceInfo(place, null);
            thisPlace = place;
            startActivity(new Intent(getApplicationContext(), PlaceInfoActivity.class).putExtra("image", placeIcon));
        }/*
        noman.googleplaces.Place place = (noman.googleplaces.Place) marker.getTag();
        placePhotosAsync(place.getPlaceId());

        List<String> types = place.getTypes();
        //PlaceInfo placeInfo = new PlaceInfo(place, null);
        thisPlace = place;
        startActivity(new Intent(getApplicationContext(), PlaceInfoActivity.class).putExtra("image", placeIcon));
        */
    }

    private void showSearchDialog() {
        //startAnim();
        String locale = getResources().getConfiguration().locale.getCountry();
        Log.d("IssuesEtc","the local is " + locale);
        try {
            Intent intent =
                    new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)

                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException e) {
            // TODO: Handle the error.
        } catch (GooglePlayServicesNotAvailableException e) {
            // TODO: Handle the error.
        }

        /*
        PlaceSearchDialog placeSearchDialog = new PlaceSearchDialog(MapsActivity.this, new PlaceSearchDialog.LocationNameListener() {
            @Override
            public void locationName(String locationName) {

                //List<noman.googleplaces.Place> places=new ArrayList<>(foundPlaces);

                Log.d("Place", "Place Search " + locationName);

                for (final noman.googleplaces.Place place : nearbyPlaces) {

                    Log.d("Place", "Place Name " + place.getName());
                    if (locationName.toLowerCase().contains(place.getName().toLowerCase())) {
                        //places.add(place);
                        new AsyncTask<Void, Void, Void>() {
                            @Override
                            protected void onPreExecute() {
                                super.onPreExecute();

                            }

                            @Override
                            protected Void doInBackground(Void... params) {
                                //Looper.prepare();
                                try {
                                    placeIcon = Glide.
                                            with(MapsActivity.this).
                                            load(place.getIcon()).
                                            asBitmap().
                                            into(400, 400).
                                            get();
                                } catch (final ExecutionException e) {
                                    Log.e(TAG, e.getMessage());
                                } catch (final InterruptedException e) {
                                    Log.e(TAG, e.getMessage());
                                }
                                return null;
                            }

                            @Override
                            protected void onPostExecute(Void dummy) {
                                if (null != placeIcon) {
                                    // The full bitmap should be available here

                                    Marker marker = mMap.addMarker(new MarkerOptions()
                                            .position(new LatLng(place.getLatitude(), place.getLongitude()))
                                            //.icon(BitmapDescriptorFactory.fromBitmap(placeIcon))

                                            .title(place.getName()));
                                    marker.setTag(place);
                                    mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(place.getLatitude(), place.getLongitude())));
                                    //stopAnim();
                                    Log.d(TAG, "Image loaded");
                                }
                                ;
                            }
                        }.execute();


                    }

                }
                //set textview or edittext
            }
        });
        placeSearchDialog.show();
*/

    }

    void startAnim() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
            progressBar2.setVisibility(View.VISIBLE);

        } else {
            progressBar.show();

        }
        // or avi.smoothToShow();
    }

    void stopAnim() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP) {
            ProgressBar progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
            progressBar2.setVisibility(View.GONE);

        } else {
            progressBar.smoothToHide();
        }
    }

    private void getPlaceInfo(final PointOfInterest point) {

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                //Looper.prepare();

                Log.d("Place Info", "POI Name " + point.name);

                for (final noman.googleplaces.Place place : nearbyPlaces) {


                    //Log.d("Place Info", "Place Name " + placeName);

                    if (place.getName().equalsIgnoreCase(point.name)) {
                        //places.add(place);
                        thisPlace = place;

                        Log.d("Place Info", "This Place Name: " + thisPlace.getName());


                    } else {
                        //thisPlace=null;
                    }
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void dummy) {
                //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon));
                //Log.d("Place Info", "This Place Name " + point.name);
                if (thisPlace != null) {
                    Marker poi = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(thisPlace.getLatitude(), thisPlace.getLongitude()))
                            //.icon(BitmapDescriptorFactory.fromBitmap(placeIcon))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_white_36dp))
                            .title(thisPlace.getName()));
                    poi.setTag(thisPlace);
                    poi.showInfoWindow();
                }
            }


        }.execute();

    }

    private void signOut() {

        LoginActivity.mAuth.signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
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
                        stopAnim();
                        //progressBar.setVisibility(View.GONE);
                    }
                })
                .key(getString(R.string.map_api_key))
                .latlng(myLocation.getLatitude(), myLocation.getLongitude())
                .radius(1500)

                .build()
                .execute();
    }

    private void makeMarker(Place place) {
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                mDimension, mDimension
        );
        params.setMargins(5, 5, 5, 5);
        ImageView markerView = new ImageView(getApplicationContext());

        markerView.setLayoutParams(params);
        markerView.setBackgroundColor(Color.WHITE);
        int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
        markerView.setPadding(padding, padding, padding, padding);
        // circleImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
        //circleImageView.setPadding(padding,padding,padding,padding);

        mIconGenerator.setContentView(markerView);
        Picasso.with(getApplicationContext()).load(place.getIcon()).into(markerView);
        Bitmap icon = mIconGenerator.makeIcon();
        Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(place.getLatitude(), place.getLongitude())));
        //marker.setSnippet("Interests: " + mInterest.placeTypes.toString());
        marker.setIcon(BitmapDescriptorFactory.fromBitmap(icon));
        marker.setTitle(place.getName());
        marker.setTag(place);
    }
private void pickInterests(){
    View popup = LayoutInflater.from(context).inflate(R.layout.search_prompt_layout, null, false);
    ImageView pulseView = (ImageView) popup.findViewById(R.id.pv);
Bitmap bitmap=BitmapFactory.decodeResource(getResources(),R.drawable.p2);
    Blurry.with(context).radius(10).from(bitmap).into(pulseView);

    CardView share = (CardView) popup.findViewById(R.id.share_button);
    share.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(getApplicationContext(),PickerActivity.class));

        }
    });
    maryPopupI = MaryPopup.with((Activity) context)
            .cancellable(true)
            .blackOverlayColor(Color.parseColor("#DD444444"))
            //.backgroundColor(Color.parseColor("#EFF4F5"))
            .content(popup)

            .from(toolbar)
            .center(true);
    maryPopupI.show();
}
    private void setUpAndLoadNativeExpressAds() {
        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                final float scale = MapsActivity.this.getResources().getDisplayMetrics().density;
                // Set the ad size and ad unit ID for each Native Express ad in the items list.
                for (int i = 1; i <= people.size(); i += ITEMS_PER_AD) {
                    final NativeExpressAdView adView =
                            (NativeExpressAdView) people.get(i);

                    AdSize adSize = new AdSize(290,80);
                    adView.setAdSize(adSize);
                    adView.setAdUnitId(getString(R.string.native_ad_unit_id));
                }

                // Load the first Native Express ad in the items list.
                loadNativeExpressAd(1);
            }
        });
    }
    private void setUpAndLoadFavNativeExpressAds() {
        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                final float scale = MapsActivity.this.getResources().getDisplayMetrics().density;
                // Set the ad size and ad unit ID for each Native Express ad in the items list.
                for (int i = 1; i <= favs.size(); i += ITEMS_PER_AD) {
                    final NativeExpressAdView adView =
                            (NativeExpressAdView) favs.get(i);

                    AdSize adSize = new AdSize(290,80);
                    adView.setAdSize(adSize);
                    adView.setAdUnitId(getString(R.string.native_ad_unit_id));
                }

                // Load the first Native Express ad in the items list.
                loadNativeFavExpressAd(1);
            }
        });
    }
    private void loadNativeFavExpressAd(final int index) {

        if (index >= favs.size()) {
            return;
        }

        Object item = favs.get(index);
        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
                    + " Express ad.");
        }

        final NativeExpressAdView adView = (NativeExpressAdView) item;

        // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous Native Express ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous Native Express ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }
        });

        // Load the Native Express ad.
        adView.loadAd(new AdRequest.Builder().addTestDevice("EAB5E250CD12697D717B6FE164D5D5B0").addTestDevice("F1B04986EF8E6B5A99F9E6287F9FA4EF").build());
    }
    private void loadNativeExpressAd(final int index) {

        if (index >= people.size()) {
            return;
        }

        Object item = people.get(index);
        if (!(item instanceof NativeExpressAdView)) {
            throw new ClassCastException("Expected item at index " + index + " to be a Native"
                    + " Express ad.");
        }

        final NativeExpressAdView adView = (NativeExpressAdView) item;

        // Set an AdListener on the NativeExpressAdView to wait for the previous Native Express ad
        // to finish loading before loading the next ad in the items list.
        adView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // The previous Native Express ad loaded successfully, call this method again to
                // load the next ad in the items list.
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // The previous Native Express ad failed to load. Call this method again to load
                // the next ad in the items list.
                Log.e("MainActivity", "The previous Native Express ad failed to load. Attempting to"
                        + " load the next Native Express ad in the items list.");
                loadNativeExpressAd(index + ITEMS_PER_AD);
            }
        });

        // Load the Native Express ad.
        adView.loadAd(new AdRequest.Builder().addTestDevice("EAB5E250CD12697D717B6FE164D5D5B0").addTestDevice("F1B04986EF8E6B5A99F9E6287F9FA4EF").build());
    }
    private void addNativeExpressAds() {

        // Loop through the items array and place a new Native Express ad in every ith position in
        // the items List.
        for (int i = 1; i <= people.size(); i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(MapsActivity.this);
            people.add(i, adView);

        }
    }

    private void addNativeFavExpressAds() {

        // Loop through the items array and place a new Native Express ad in every ith position in
        // the items List.
        for (int i = 1; i <= favs.size(); i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(MapsActivity.this);
            favs.add(i, adView);

        }
    }
    private void searchPlaces() {
        //final List<Object> people = new ArrayList<>();
        if (myLocation == null) {
            myLocation = MainActivity.myLocation;
        }
        people.clear();
        nearbyPlaces = MainActivity.nearbyPlaces;
        Log.d("P value", "p  " + nearbyPlaces.size());
        //LatLng me = new LatLng(nearbyPlaces.get(0).getLatitude(), nearbyPlaces.get(0).getLongitude());
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(me));
        // progressBar.setVisibility(View.VISIBLE);
        final List<MyItem> cluster = new ArrayList<MyItem>();
        final List<Place> placeHolder = new ArrayList<Place>();
        if (mClusterManager != null) {
            mClusterManager.clearItems();
        }
        if(MainActivity.myInterests.size()>0) {
            new AsyncTask<Void, Void, Void>() {
                Interest mInterest;
                //NativeExpressAdView adView=new NativeExpressAdView(context);

                @Override
                protected void onPreExecute() {
                    startAnim();
                   /*
                    adView.setAdSize(new AdSize(300,90));
                    adView.setAdUnitId(getString(R.string.native_ad_unit_id));
                    adView.loadAd(new AdRequest.Builder().addTestDevice("EAB5E250CD12697D717B6FE164D5D5B0").build());
                    */
                    super.onPreExecute();
                }

                @Override
                protected Void doInBackground(Void... voids) {
                    for(Place place:MainActivity.boostedPlaces){
                        for (Interest interest : myInterests) {
                            //Log.d("Cluster", "p types " + p.getName() + " " + p.getTypes() + "current type " + interest.getInterestName());

                            if (CollectionUtils.containsAny(place.getTypes(), interest.getPlaceTypes())) {

                                //Log.d("Cluster", "p types " + p.getTypes()+"current type "+type);

                                if (!placeHolder.contains(place)) {
                                    placeHolder.add(0,place);

                                    int count = Collections.frequency(MainActivity.currentPlaces, place);
                                    HotPlaces hotPlace = new HotPlaces(place, count);
                                    //hotPlace.setBoosted(true);

                                    if (!people.contains(hotPlace)) {
                                        Log.e("Boosted Places", "Boosted Place Added. " + place.getName() + " Occurs " + count);
                                        people.add(0,hotPlace);

                                    }
                                    MyItem item = new MyItem(new LatLng(place.getLatitude(), place.getLongitude()), hotPlace);
                                    if (!cluster.contains(item)) {
                                        cluster.add(0,item);
                                        //Log.d("Cluster", "added " + item.getmPlace().getName());

                                    }
                                }
                            }
                        }

                    }
                    for (Place p : MainActivity.nearbyPlaces) {
                        //noman.googleplaces.Place p = nearbyPlaces.get(i);


                        //Log.d("P value", "p  " + p.getName());

                        //mInterest = myInterests.get(i);
                        // Log.d("I value", "p  " + mInterest.getInterestName());

                        for (Interest interest : myInterests) {
                            Log.d("Cluster", "p types " + p.getName() + " " + p.getTypes() + "current type " + interest.getInterestName());

                            if (CollectionUtils.containsAny(p.getTypes(), interest.getPlaceTypes())) {

                                //Log.d("Cluster", "p types " + p.getTypes()+"current type "+type);

                                if (!placeHolder.contains(p)) {
                                    placeHolder.add(p);

                                    int count = Collections.frequency(MainActivity.currentPlaces, p);
                                    HotPlaces hotPlace = new HotPlaces(p, count);
                                    if (!people.contains(hotPlace)) {
                                        people.add(hotPlace);
                                        //Log.e("Hot Places", "Hot Place Added. " + place.getPlaceId() + " Occurs " + count);

                                    }
                                    MyItem item = new MyItem(new LatLng(p.getLatitude(), p.getLongitude()), hotPlace);
                                    if (!cluster.contains(item)) {
                                        cluster.add(item);
                                        Log.d("Cluster", "added " + item.getmPlace().getName());

                                    }
                                    //makeMarker(p);
                                }
                            }
                        }
                    }


                    /*
                    for (int i=0;i<people.size()-1;i+=6){

                        people.add(i,adView);
                    }*/
                  if(!isPaid)
                    addNativeExpressAds();

                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    if(!isPaid)
                    setUpAndLoadNativeExpressAds();


                    //mMap.clear();
                    MyPlaceItemRecyclerViewAdapter adapter = new MyPlaceItemRecyclerViewAdapter(people, mListener);
                    adapter.setHasStableIds(true);



                    recyclerView.removeAllViews();
                    recyclerView.setAdapter(adapter);
                    YoYo.with(Techniques.SlideInDown).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            recyclerView.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            stopAnim();

                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).playOn(recyclerView);
                    //progressBar.setVisibility(View.GONE);
                    setUpClusterer(cluster);
                    super.onPostExecute(aVoid);
                }
            }.execute();
        }else{
            pickInterests();
        }
/*
        for (int i = 0; i < myInterests.size(); i++) {


            // Log.d("I value", "I is " + i);
            final int finalI = i;
            x = i;
            final Interest mInterest = myInterests.get(i);
            thisInterest = myInterests.get(i);
            for (final String type : myInterests.get(i).placeTypes) {
                if()
            }


                try {
                    new NRPlaces.Builder()
                            .listener(new PlacesListener() {
                                @Override
                                public void onPlacesFailure(PlacesException e) {
                                    //e.printStackTrace();
                                }

                                @Override
                                public void onPlacesStart() {

                                }

                                @Override
                                public void onPlacesSuccess(List<noman.googleplaces.Place> places) {
//foundPlaces.addAll(places);

                                    Iterator iterator = places.iterator();
                                    while (iterator.hasNext()) {
                                        noman.googleplaces.Place p = (noman.googleplaces.Place) iterator.next();

                                        PlaceInfo placeInfo = new PlaceInfo(p, p.getPlaceId());
                                        if (!placeInfoList.contains(placeInfo))
                                            placeInfoList.add(placeInfo);

                                        if (!foundPlaces.contains(p)) {
                                            foundPlaces.add(p);
                                            markPlace = p;

                                            //Log.d("Places", " place added " + p.getName());

                                        }
                                        if (!placeHolder.contains(p)) {
                                            placeHolder.add(p);
                                            MyItem item = new MyItem(new LatLng(p.getLatitude(), p.getLongitude()), p);
                                            if (!cluster.contains(item)) {
                                                cluster.add(item);
                                                Log.d("Cluster", "added " + item.getmPlace().getName());

                                            }
                                            int count = Collections.frequency(MainActivity.currentPlaces, p);
                                            HotPlaces hotPlace = new HotPlaces(p, count);
                                            if (!people.contains(hotPlace)) {
                                                people.add(hotPlace);
                                                //Log.e("Hot Places", "Hot Place Added. " + place.getPlaceId() + " Occurs " + count);

                                            }
                                            makeMarker(p);
                                        }
                                    }

                                    //PlaceInterest placeInterest = new PlaceInterest(mInterest, places);
                                    //placeInterestList.add(placeInterest);
                                    //Log.d("PlaceInt", "Place Int: " + placeInterest.getInterest().interestName + " Places: " + placeInterest.getPlaceList().toString());

                                }

                                @Override
                                public void onPlacesFinished() {
                                    index++;

                                    Log.d("Index value", "Index " + index);
                                    if (mClusterManager != null) {
                                        //mClusterManager.clearItems();
                                    }
                                    //mMap.clear();

                                    //Log.d("I value", " I " + finalI + " my Interests " + myInterests.size());

                                    if (finalI >= myInterests.size() - 1) {
                                        new AsyncTask<Void, Void, Void>() {
                                            //List<Place> placeList = new ArrayList<Place>();
                                            //List<PlaceInfo> placeInfos = new ArrayList<PlaceInfo>();

                                            @Override
                                            protected void onPreExecute() {
                                                super.onPreExecute();
                                            }

                                            @Override
                                            protected Void doInBackground(Void... voids) {

                                                //LinkedHashSet<Place> hashSet = new LinkedHashSet<Place>();
                                                //hashSet.addAll(placeHolder);
                                                //foundPlaces.clear();
                                                // placeHolder.clear();
                                                //placeHolder.addAll(hashSet);
                                                //Iterator iterator = foundPlaces.iterator();


                                                for (Place place : placeHolder) {
                                                    //placeList.add(foundPlaces.get(0));

                                                    int count = Collections.frequency(MainActivity.currentPlaces, place);
                                                    HotPlaces hotPlace = new HotPlaces(place, count);
                                                    if (!people.contains(hotPlace)) {
                                                        people.add(hotPlace);
                                                        //Log.e("Hot Places", "Hot Place Added. " + place.getPlaceId() + " Occurs " + count);

                                                    }
                                                    /*
                                                    MyItem item = new MyItem(new LatLng(place.getLatitude(), place.getLongitude()), place);
                                                    if (!cluster.contains(item)) {
                                                        cluster.add(item);
                                                        Log.d("Cluster", "added " + item.getmPlace().getName());

                                                    }

                                                }

                                                return null;
                                            }

                                            @Override
                                            protected void onPostExecute(Void aVoid) {
                                                Log.d("I value", "done at " + finalI);

                                                for (noman.googleplaces.Place place : placeList) {

                                                    Marker marker = mMap.addMarker(new MarkerOptions().position(new LatLng(place.getLatitude(), place.getLongitude())));
                                                    marker.setSnippet("Interests: " + mInterest.placeTypes.toString());
                                                    marker.setTitle(place.getName());
                                                    marker.setTag(place);
                                                }

                                                MyPlaceItemRecyclerViewAdapter adapter = new MyPlaceItemRecyclerViewAdapter(people, mListener);
                                                adapter.setHasStableIds(true);
                                                recyclerView.removeAllViews();
                                                recyclerView.setAdapter(adapter);
                                                YoYo.with(Techniques.SlideInDown).withListener(new Animator.AnimatorListener() {
                                                    @Override
                                                    public void onAnimationStart(Animator animation) {
                                                        recyclerView.setVisibility(View.VISIBLE);
                                                    }

                                                    @Override
                                                    public void onAnimationEnd(Animator animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationCancel(Animator animation) {

                                                    }

                                                    @Override
                                                    public void onAnimationRepeat(Animator animation) {

                                                    }
                                                }).playOn(recyclerView);
                                                //progressBar.setVisibility(View.GONE);
                                                setUpClusterer(cluster);
                                                stopAnim();
                                                super.onPostExecute(aVoid);
                                            }
                                        };

                                        MyPlaceItemRecyclerViewAdapter adapter = new MyPlaceItemRecyclerViewAdapter(people, mListener);
                                        adapter.setHasStableIds(true);
                                        recyclerView.removeAllViews();
                                        recyclerView.setAdapter(adapter);
                                        YoYo.with(Techniques.SlideInDown).withListener(new Animator.AnimatorListener() {
                                            @Override
                                            public void onAnimationStart(Animator animation) {
                                                recyclerView.setVisibility(View.VISIBLE);
                                            }

                                            @Override
                                            public void onAnimationEnd(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationCancel(Animator animation) {

                                            }

                                            @Override
                                            public void onAnimationRepeat(Animator animation) {

                                            }
                                        }).playOn(recyclerView);
                                        //progressBar.setVisibility(View.GONE);
                                        setUpClusterer(cluster);
                                        stopAnim();
                                    }
                                }

                                int count = 0;

                                    for (int p = 0; p < placeInterestList.size(); p++) {
                                        Log.d("Count", "Int Name " + placeInterestList.get(p).getInterest().interestName);

                                        if (placeInterestList.get(p).getPlaceList().contains(foundPlaces.get(p))) {
                                            count++;
                                            Log.d("Count", "" + foundPlaces.get(p).getName() + " count:  " + count + " Interest " + placeInterestList.get(p).getInterest().interestName);

                                        }
                                    }

                                //  }
                            })
                            .key(getString(R.string.map_api_key))
                            .latlng(myLocation.getLatitude(), myLocation.getLongitude())
                            .radius(distance)
                            .type(type)
                            .build()
                            .execute();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
               }

            }


        }
*/

    }


    @Override
    public void onListFragmentInteraction(HotPlaces item, User user,Place place) {
       if(place!=null) {
           thisPlace = place;
           mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(place.getLatitude(), place.getLongitude())));
           placePhotosAsync(place.getPlaceId());

       }
       if(item!=null){
           thisPlace = item.getPlace();
           mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(item.getPlace().getLatitude(), item.getPlace().getLongitude())));
           placePhotosAsync(item.getPlace().getPlaceId());


       }
           /*
        Marker marker = mMap.addMarker(new MarkerOptions()
                .position(new LatLng(item.getLatitude(), item.getLongitude()))
                .title(item.getName())
                .snippet(item.getVicinity())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        marker.setTag(item);
        marker.showInfoWindow();
        */

        startActivity(new Intent(getApplicationContext(), PlaceInfoActivity.class).putExtra("image", placeIcon));
    }

    @Override
    public void onListFragmentInteraction(Interest item) {

    }

    public GoogleMap getMap() {
        return mMap;
    }


    @Override
    public boolean onClusterItemClick(MyItem myItem) {


        noman.googleplaces.Place place = myItem.getmPlace();
        placePhotosAsync(place.getPlaceId());


        List<String> types = place.getTypes();
        //PlaceInfo placeInfo = new PlaceInfo(place, null);
        thisPlace = place;
        startActivity(new Intent(getApplicationContext(), PlaceInfoActivity.class).putExtra("image", placeIcon));
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(MyItem myItem) {
        noman.googleplaces.Place place = myItem.getmPlace();
        placePhotosAsync(place.getPlaceId());

        List<String> types = place.getTypes();
        //PlaceInfo placeInfo = new PlaceInfo(place, null);
        thisPlace = place;
        startActivity(new Intent(getApplicationContext(), PlaceInfoActivity.class).putExtra("image", placeIcon));
    }


    @Override
    public void onClusterInfoWindowClick(Cluster<MyItem> cluster) {


    }

    @Override
    public boolean onClusterClick(Cluster<MyItem> cluster) {

        LatLngBounds.Builder builder = LatLngBounds.builder();
        for (ClusterItem item : cluster.getItems()) {
            builder.include(item.getPosition());
        }
        // Get the LatLngBounds
        final LatLngBounds bounds = builder.build();

        // Animate camera to the bounds
        try {
            getMap().animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 100));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.w("Cluster", "Click");
        return true;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.history_text:
                historyText.setSelected(true);
                historyText.setTextColor(Color.WHITE);
                currentText.setSelected(false);
                currentText.setTextColor(Color.BLACK);
                isCurrent = true;
                break;
            case R.id.current_text:
                historyText.setSelected(false);
                historyText.setTextColor(Color.BLACK);
                currentText.setSelected(true);
                currentText.setTextColor(Color.WHITE);
                isCurrent = true;
                break;
        }
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if(marker.getTag() instanceof Friend){
            Friend friend= (Friend) marker.getTag();
            currentFriend=friend.friend;
            getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(friend.friendLocation, 15));

            startActivity(new Intent(getApplicationContext(),ProfileActivity.class).putExtra("userId",((Friend) marker.getTag()).friend.userID).putExtra("photoUrl",((Friend) marker.getTag()).friend.photoUrl).putExtra("userName",((Friend) marker.getTag()).friend.username));

        }else{
            noman.googleplaces.Place place = (noman.googleplaces.Place) marker.getTag();
            placePhotosAsync(place.getPlaceId());


            List<String> types = place.getTypes();
            //PlaceInfo placeInfo = new PlaceInfo(place, null);
            thisPlace = place;
            startActivity(new Intent(getApplicationContext(), PlaceInfoActivity.class).putExtra("image", placeIcon));
        }
        return false;    }


    private class SearchPlaces extends AsyncTask<Integer, Void, Integer> {

        ProgressDialog progressDialog;


        @Override
        protected Integer doInBackground(Integer... params) {

            for (Interest interest : myInterests) {
                //  Place place = client.getPlacesByQuery(interest.interestName).get(0);
                // places.add(place);
                //  Log.w("Place search", "results " + place.toString());

            }
//            places = client.getNearbyPlaces(myLocation.getLatitude(), myLocation.getLongitude(), 200);

            return 1;
        }

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(MapsActivity.this, "", "Searching...", true);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            progressDialog.dismiss();
            // Log.w("Place search", "results " + places.toString());
        }


    }

    public void onPickButtonClick() {
        // Construct an intent for the place picker
        try {
            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder();

            Intent intent = intentBuilder.build(this);
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, PLACE_PICKER_REQUEST);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }
private void getTypes(final com.google.android.gms.location.places.Place place){
    new AsyncTask<Void, Void, Void>() {
List<String>placeTypes=new ArrayList<String>();
        @Override
        protected Void doInBackground(Void... voids) {
            for(Integer t:place.getPlaceTypes()) {
                Field[] fields = com.google.android.gms.location.places.Place.class.getDeclaredFields();

                for (Field field : fields) {
                    Class<?> type = field.getType();

                    if (type == int.class) {
                        try {

                            if (t == field.getInt(null)) {
                                //Toast.makeText(context,"types:"+field.getName(),Toast.LENGTH_LONG).show();
                                //Log.i("Place", "onCreate: " + field.getName());
                                String s=field.getName().substring(field.getName().indexOf("_")+1).toLowerCase();
                                Log.i("Place", "subString: " + s);

                                placeTypes.add(s);
                                //break;
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            noman.googleplaces.Location targetLocation = new noman.googleplaces.Location("");
            targetLocation.setLatitude(place.getLatLng().latitude);
            targetLocation.setLongitude(place.getLatLng().longitude);
            Place p=new Place.Builder().name(place.getName().toString()).placeId(place.getId()).types(placeTypes).location(targetLocation).build();
            Marker marker = mMap.addMarker(new MarkerOptions()
                    .position(place.getLatLng())
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_white_36dp))

                    .title(place.getName().toString()));
            marker.setTag(p);
            mMap.animateCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
            super.onPostExecute(aVoid);

        }
    }.execute();

}
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                com.google.android.gms.location.places.Place place = PlaceAutocomplete.getPlace(this, data);
                List<String>placeTypes=new ArrayList<String>();
getTypes(place);

                Log.i(TAG, "Place: " + place.getName());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                // TODO: Handle the error.
                Log.i(TAG, status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }

        if (requestCode == PLACE_PICKER_REQUEST
                && resultCode == Activity.RESULT_OK) {
            //List<String>placeTypes=new ArrayList<String>();

            // The user has selected a place. Extract the name and address.
            final com.google.android.gms.location.places.Place place = PlacePicker.getPlace(context, data);
getTypes(place);
            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();
            final String id=place.getId();

            //Toast.makeText(context,"Place info: name: "+name+"  Address"+address+" ID "+id,Toast.LENGTH_LONG).show();
            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }


        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(View item, int position);
    }

    private class Friend {
        User friend;
        LatLng friendLocation;

        public Friend(User friend, LatLng friendLocation) {
            this.friend = friend;
            this.friendLocation = friendLocation;
        }
    }

    private void getFriends() {

        //List<Friend> friendList = new ArrayList<Friend>();
        //  List<Bitmap> bitmaps = new ArrayList<>();

        friendSwitch.setChecked(true);
        if (MainActivity.myFriends.size() > 0) {
            for (int i = 0; i < MainActivity.myFriends.size(); i++) {
                //final View marker = LayoutInflater.from(context).inflate(R.layout.friend_marker, null);


                final int finalI = i;
                new AsyncTask<Void, Void, Void>() {
                    View m_layout = View.inflate(getApplicationContext(), R.layout.friend_marker, null);
                    Bitmap bitmap;
                    Friend friend;

                    @Override
                    protected void onPreExecute() {
                        friend = new Friend(MainActivity.myFriends.get(finalI), MainActivity.friendLocations.get(finalI));
                        CircleImageView friendPic = (CircleImageView) m_layout.findViewById(R.id.friend_image);
                        Picasso.with(context).load(friend.friend.photoUrl).placeholder(R.drawable.ic_account_circle_white_48dp).into(friendPic);

                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        bitmap = loadBitmapFromView(m_layout);
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                    /*
                    Marker fMarker = mMap.addMarker(new MarkerOptions()
                            .position(friend.friendLocation)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap))

                            .title(friend.friend.username));

                    fMarker.setTag(friend);
                    */
                        if (mClusterManager != null) {
                            if (mClusterManager.getMarkerManager().getCollection("friends") != null) {
                                mClusterManager.getMarkerManager().getCollection("friends").addMarker(new MarkerOptions()

                                        .position(friend.friendLocation)
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))

                                        .title(friend.friend.username)).setTag(friend);
                                super.onPostExecute(aVoid);
                                //markerManager.getCollection("friends").setOnMarkerClickListener(MapsActivity.this);
                            } else {
                                mClusterManager.getMarkerManager().newCollection("friends");
                                mClusterManager.getMarkerManager().getCollection("friends").addMarker(new MarkerOptions()

                                        .position(friend.friendLocation)
                                        .icon(BitmapDescriptorFactory.fromBitmap(bitmap))

                                        .title(friend.friend.username)).setTag(friend);
                            }


                        }
                    }
                }.execute();

            }
        }
    }
    public static Bitmap loadBitmapFromView(View v) {
        if (v.getMeasuredHeight() <= 0) {
            v.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }else {
            v.measure(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            Bitmap b = Bitmap.createBitmap(v.getMeasuredWidth(), v.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(b);
            v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
            v.draw(c);
            return b;
        }
    }
    private Bitmap getViewBitmap(View v) {
        v.clearFocus();
        v.setPressed(false);

        boolean willNotCache = v.willNotCacheDrawing();
        v.setWillNotCacheDrawing(false);

        // Reset the drawing cache background color to fully transparent
        // for the duration of this operation
        int color = v.getDrawingCacheBackgroundColor();
        v.setDrawingCacheBackgroundColor(0);

        if (color != 0) {
            v.destroyDrawingCache();
        }
        v.buildDrawingCache();
        Bitmap cacheBitmap = v.getDrawingCache();
        if (cacheBitmap == null) {
            Log.e(TAG, "failed getViewBitmap(" + v + ")", new RuntimeException());
            return null;
        }

        Bitmap bitmap = Bitmap.createBitmap(cacheBitmap);

        // Restore the view
        v.destroyDrawingCache();
        v.setWillNotCacheDrawing(willNotCache);
        v.setDrawingCacheBackgroundColor(color);

        return bitmap;
    }

    private class MyItemRenderer extends DefaultClusterRenderer<MyItem> {
        private final IconGenerator mIconGenerator = new IconGenerator(getApplicationContext());
        private final IconGenerator mClusterIconGenerator = new IconGenerator(getApplicationContext());
        private final ImageView mImageView;
        private final ImageView mClusterImageView;
        private final int mDimension;
        //private final CircleImageView circleImageView;

        public MyItemRenderer() {
            super(getApplicationContext(), getMap(), mClusterManager);

            View multiProfile = getLayoutInflater().inflate(R.layout.multi_profile, null);
            mClusterIconGenerator.setContentView(multiProfile);
            mClusterImageView = (ImageView) multiProfile.findViewById(R.id.image);
            //circleImageView = new CircleImageView(getApplicationContext());

            mImageView = new ImageView(getApplicationContext());
            mDimension = (int) getResources().getDimension(R.dimen.custom_profile_image);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    mDimension, mDimension
            );
            params.setMargins(5, 5, 5, 5);
            //mImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension);
            mImageView.setLayoutParams(params);
            mImageView.setBackgroundColor(Color.WHITE);
            int padding = (int) getResources().getDimension(R.dimen.custom_profile_padding);
            mImageView.setPadding(padding, padding, padding, padding);
            // circleImageView.setLayoutParams(new ViewGroup.LayoutParams(mDimension, mDimension));
            //circleImageView.setPadding(padding,padding,padding,padding);

            mIconGenerator.setContentView(mImageView);
            //mIconGenerator.setColor(getResources().getColor(R.color.mt_gray));

        }

        @Override
        protected void onBeforeClusterItemRendered(final MyItem person, final MarkerOptions markerOptions) {
            // Draw a single person.
            // Set the info window to show their name.
            //mImageView.setImageResource(R.drawable.newfire);
            Picasso.with(getApplicationContext()).load(person.getmPlace().getIcon()).resize(50, 50).placeholder(android.R.drawable.ic_dialog_map).into(mImageView);
            //mImageView.setImageResource(R.drawable.ic_place_white_24dp);
            //Bitmap b2=tintImage(icon,getResources().getColor(R.color.colorAccent));

if(boostedPlaces.contains(person.getHotPlace().getPlace())){
    mIconGenerator.setColor(getResources().getColor(R.color.colorPrimary));
}else{
    mIconGenerator.setColor(Color.LTGRAY);

}
            final Bitmap icon = mIconGenerator.makeIcon();

            new Thread(new Runnable() {

                List<String> types = person.getTypes();
                List<String> interestList = new ArrayList<String>();

                public void run() {
                    for (Interest interest : MainActivity.myInterests) {
                        Log.w("Place Info", " Place interest type " + interest.placeTypes);

                        for (int i = 0; i < types.size(); i++) {
                            //Log.d("Place Info", " Place type " + types[i]);

                            if (interest.placeTypes.contains(types.get(i))) {
                                if (!interestList.contains(interest.getInterestName())) {
                                    interestList.add(interest.getInterestName());
                                    //Log.d("Place Info", " Place fits Interest: " + interest.interestName);
                                    markerOptions.snippet("Interests: " + interestList);


                                }

                            }
                        }

                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                        }
                    });
                }


            }).start();


            //markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon)).title(person.getmPlace().getName());

            //markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_place_white_36dp));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));
            markerOptions.snippet("" + person.getTypes());

            markerOptions.title(person.getmPlace().getName());
            super.onBeforeClusterItemRendered(person, markerOptions);
        }

        @Override
        protected void onBeforeClusterRendered(Cluster<MyItem> cluster, MarkerOptions markerOptions) {
            // Draw multiple people.
            // Note: this method runs on the UI thread. Don't spend too much time in here (like in this example).

            List<Drawable> profilePhotos = new ArrayList<Drawable>(Math.min(4, cluster.getSize()));
            int width = mDimension;
            int height = mDimension;

            for (MyItem p : cluster.getItems()) {
                // Draw 4 at most.
                if (profilePhotos.size() == 4) break;
                Drawable drawable = getResources().getDrawable(R.drawable.ic_account_circle_white_24dp);
                //drawable.setTint(getResources().getColor(R.color.colorAccent));
                drawable.setBounds(0, 0, width, height);
                profilePhotos.add(drawable);
            }
            MultiDrawable multiDrawable = new MultiDrawable(profilePhotos);
            multiDrawable.setBounds(0, 0, width, height);

            mClusterImageView.setImageDrawable(multiDrawable);
            Bitmap icon = mClusterIconGenerator.makeIcon(String.valueOf(cluster.getSize()));
            markerOptions.icon(BitmapDescriptorFactory.fromBitmap(icon));

            super.onBeforeClusterRendered(cluster, markerOptions);
        }

        @Override
        protected boolean shouldRenderAsCluster(Cluster cluster) {
            // Always render clusters.
            return cluster.getSize() > 2;
        }
    }

    @Override
    public void onBackPressed() {
        if (behavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        } else if (maryPopup != null && maryPopup.isOpened()) {
            maryPopup.close(true);

        }else if(maryPopupI!=null&&maryPopupI.isOpened()){
            maryPopupI.close(true);
        }

        else {
            /*
            new AlertDialog.Builder(this)
                    .setTitle("Really Exit?")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface arg0, int arg1) {
                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                            //finish();
                        }
                    }).create().show();
                    */
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
