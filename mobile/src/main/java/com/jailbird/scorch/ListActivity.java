package com.jailbird.scorch;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.azoft.carousellayoutmanager.CarouselLayoutManager;
import com.azoft.carousellayoutmanager.CarouselZoomPostLayoutListener;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.meetic.marypopup.MaryPopup;
import com.nineoldandroids.animation.Animator;

import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.blurry.Blurry;
import noman.googleplaces.Place;

import static com.jailbird.scorch.MainActivity.isPaid;
import static com.jailbird.scorch.MainActivity.myLocation;
import static com.jailbird.scorch.MapsActivity.myInterests;

public class ListActivity extends Activity implements MainActivity.OnListFragmentInteractionListener{

    private static final int ITEMS_PER_AD = 6;
    private RecyclerView recyclerView;
List<Object>people;
    Context context;
    private MainActivity.OnListFragmentInteractionListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        context=this;
        mListener=this;
        recyclerView = (RecyclerView) findViewById(R.id.map_places_list);
        final CarouselLayoutManager layoutManager2 = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL);
        layoutManager2.setPostLayoutListener(new CarouselZoomPostLayoutListener());

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        searchPlaces();
    }
    private void searchPlaces() {
        people = new ArrayList<>();
        if (myLocation == null) {
            myLocation = myLocation;
        }
        people.clear();
        List<Place> nearbyPlaces = MainActivity.nearbyPlaces;
        //Log.d("P value", "p  " + nearbyPlaces.size());
        //LatLng me = new LatLng(nearbyPlaces.get(0).getLatitude(), nearbyPlaces.get(0).getLongitude());
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(me));
        // progressBar.setVisibility(View.VISIBLE);
        final List<MyItem> cluster = new ArrayList<MyItem>();
        final List<Place> placeHolder = new ArrayList<Place>();

        if(MainActivity.myInterests.size()>0) {
            new AsyncTask<Void, Void, Void>() {
                Interest mInterest;
                //NativeExpressAdView adView=new NativeExpressAdView(context);

                @Override
                protected void onPreExecute() {
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
                        for (Interest interest : MainActivity.myInterests) {
                            //Log.d("Cluster", "p types " + p.getName() + " " + p.getTypes() + "current type " + interest.getInterestName());

                            if (CollectionUtils.containsAny(place.getTypes(), interest.getPlaceTypes())) {

                                //Log.d("Cluster", "p types " + p.getTypes()+"current type "+type);

                                if (!placeHolder.contains(place)) {
                                    placeHolder.add(0,place);

                                    int count = Collections.frequency(MainActivity.currentPlaces, place);
                                    HotPlaces hotPlace = new HotPlaces(place, count);
                                    hotPlace.setBoosted(true);
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
                    BigPlaceItemAdapter adapter = new BigPlaceItemAdapter(people, mListener);
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
                    super.onPostExecute(aVoid);
                }
            }.execute();
        }else{
            pickInterests();
        }


    }
    private void pickInterests(){
        View popup = LayoutInflater.from(context).inflate(R.layout.search_prompt_layout, null, false);
        ImageView pulseView = (ImageView) popup.findViewById(R.id.pv);
        Bitmap bitmap= BitmapFactory.decodeResource(getResources(),R.drawable.p2);
        Blurry.with(context).radius(10).from(bitmap).into(pulseView);

        CardView share = (CardView) popup.findViewById(R.id.share_button);
        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),PickerActivity.class));

            }
        });
        View mView;
        Dialog mDialog;
                ContextThemeWrapper mTheme = new ContextThemeWrapper(this,
                R.style.YOUR_STYE);



        mDialog = new Dialog(mTheme);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(popup);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.show();
    }

    private void addNativeExpressAds() {

        // Loop through the items array and place a new Native Express ad in every ith position in
        // the items List.
        for (int i = 1; i <= people.size(); i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(ListActivity.this);
            people.add(i, adView);

        }
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
    private void setUpAndLoadNativeExpressAds() {
        // Use a Runnable to ensure that the RecyclerView has been laid out before setting the
        // ad size for the Native Express ad. This allows us to set the Native Express ad's
        // width to match the full width of the RecyclerView.
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                final float scale = ListActivity.this.getResources().getDisplayMetrics().density;
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

    @Override
    public void onListFragmentInteraction(HotPlaces item, User user, Place place) {
        if(place!=null) {
            MapsActivity.thisPlace = place;
            //mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(place.getLatitude(), place.getLongitude())));
            //placePhotosAsync(place.getPlaceId());

        }
        if(item!=null){
            MapsActivity.thisPlace = item.getPlace();
            //mMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(item.getPlace().getLatitude(), item.getPlace().getLongitude())));
            //placePhotosAsync(item.getPlace().getPlaceId());


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

        startActivity(new Intent(getApplicationContext(), PlaceInfoActivity.class));

    }
}
