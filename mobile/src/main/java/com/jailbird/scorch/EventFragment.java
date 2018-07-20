package com.jailbird.scorch;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.NativeExpressAdView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jailbird.scorch.dummy.DummyContent;
import com.jailbird.scorch.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.jailbird.scorch.MainActivity.eventIdKeys;
import static com.jailbird.scorch.MainActivity.isPaid;
import static com.jailbird.scorch.MainActivity.nearbyEvents;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class EventFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private static final int ITEMS_PER_AD = 4;
    public static final int START_INDEX=2;
    private static final int NATIVE_EXPRESS_AD_HEIGHT = 340;
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    List<Object> events = new ArrayList<>();
    RecyclerView recyclerView;
    SwipeRefreshLayout swipeContainer;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public EventFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static EventFragment newInstance(int columnCount) {
        EventFragment fragment = new EventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_COLUMN_COUNT, columnCount);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       for(String k:eventIdKeys) {
           getEvents();
       }
        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View holder = inflater.inflate(R.layout.fragment_event_list, container, false);
View view=holder.findViewById(R.id.list);
        // Set the adapter
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }

            //recyclerView.setAdapter(adapter);
        }
       swipeContainer = (SwipeRefreshLayout) holder.findViewById(R.id.swipeContainer);
        // Setup refresh listener which triggers new data loading
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
getEvents();
            }
        });

        return holder;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        super.setUserVisibleHint(isVisibleToUser);

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
    private void getEvents() {
        events.clear();
        final Date today=new Date();
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Events");
        final DatabaseReference Lref = FirebaseDatabase.getInstance().getReference().child("Event Locations");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()!=null) {
                    new AsyncTask<Void, Void, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {

                            for (DataSnapshot friendSnap : dataSnapshot.getChildren()) {

                                for (final DataSnapshot snapshot : friendSnap.getChildren()) {


                                    Event event = snapshot.getValue(Event.class);
                                    Date end=event.getEndDate();
                                    if (!events.contains(event)&&eventIdKeys.contains(event.getEventID())) {
                                        Log.d("MEME", event.eventName);
                                        if (end != null) {
                                            if (!isPast(today, end)) {
                                                if (event.isBoosted) {
                                                    events.add(0, event);
                                                } else {
                                                    events.add(event);

                                                }
                                                nearbyEvents.add(event);
                                            }else {

                                                DatabaseReference r=snapshot.getRef();
                                                r.removeValue(new DatabaseReference.CompletionListener() {
                                                    @Override
                                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                        Lref.child(snapshot.getKey()).removeValue();

                                                    }
                                                });

                                            }


                                        }
                                    }

                                }
                            }

                            return null;
                        }

                        @Override
                        protected void onPostExecute(Void aVoid) {
                            if(!isPaid) {
//addNativeExpressAds();
  //                              setUpAndLoadNativeExpressAds();
                            }
                            if(swipeContainer!=null){
                                swipeContainer.setRefreshing(false);
                            }
                            MyEventRecyclerViewAdapter adapter=new MyEventRecyclerViewAdapter(events,mListener);
                            adapter.setHasStableIds(true);
                            recyclerView.removeAllViews();
                            recyclerView.setAdapter(adapter);

                            super.onPostExecute(aVoid);
                        }
                    }.execute();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private void addNativeExpressAds() {

        // Loop through the items array and place a new Native Express ad in every ith position in
        // the items List.
        for (int i = START_INDEX; i <= events.size(); i += ITEMS_PER_AD) {
            final NativeExpressAdView adView = new NativeExpressAdView(getActivity());
            events.add(i, adView);

        }
    }
    private void loadNativeExpressAd(final int index) {

        if (index >= events.size()) {
            return;
        }

        Object item = events.get(index);
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
                final float scale = getActivity().getResources().getDisplayMetrics().density;
                // Set the ad size and ad unit ID for each Native Express ad in the items list.
                for (int i = START_INDEX; i <= events.size(); i += ITEMS_PER_AD) {
                    final NativeExpressAdView adView =
                            (NativeExpressAdView) events.get(i);


                    AdSize adSize = new AdSize(350,340);
                    if(adView.getAdSize()==null)
                    adView.setAdSize(adSize);
                    if(adView.getAdUnitId()==null)
                    adView.setAdUnitId(getString(R.string.event_ad_unit_id));
                }

                // Load the first Native Express ad in the items list.
                loadNativeExpressAd(START_INDEX);
            }
        });
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Event item);
    }

}
