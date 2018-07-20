package com.jailbird.scorch;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import noman.googleplaces.Place;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private CoordinatorLayout coordinatorLayout;
    Context context;
    CircleImageView profilePic;
    ImageView background, settingButton;
    String friendId = "User ID";
    String myName = "User Name";
    String photoUrl;
    TextView userName,placeName;
    List<Interest> myInterests;
    RecyclerView interestRecyclerView, favRecycler;
    private List<Place> favorites = new ArrayList<Place>();
MyInterestRecyclerViewAdapter myInterestRecyclerViewAdapter;
    FavoriteAdapter adapter;
    User thisFriend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        context = this;
        profilePic = (CircleImageView) findViewById(R.id.profile_image);
        thisFriend=MapsActivity.currentFriend;
placeName= (TextView) findViewById(R.id.loc_name);
        if (getIntent().hasExtra("userId")) {
            friendId = getIntent().getStringExtra("userId");
            myName = getIntent().getStringExtra("userName");
            photoUrl = getIntent().getStringExtra("photoUrl");
            Picasso.with(this).load(photoUrl).placeholder(R.drawable.ic_account_circle_white_48dp).into(profilePic);
            Log.e("Profile", "photo "+photoUrl);

        }
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        userName = (TextView) findViewById(R.id.user_name);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            actionBar.setTitle(myName);

            //actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left_black_48dp);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        userName.setText(myName);

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        myInterests = new ArrayList<>();
        myInterestRecyclerViewAdapter=new MyInterestRecyclerViewAdapter(myInterests, null, this);
        adapter = new FavoriteAdapter(favorites, null);

        getUserInfo(friendId);
        getInterests(friendId);
        favRecycler = (RecyclerView) findViewById(R.id.favorites_layout);
        //mListener = this;


        background = (ImageView) findViewById(R.id.backgroundImage);
    }

    @Override
    protected void onResume() {
        setupLists();

        super.onResume();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.friend_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.invite:

inviteToPlace();
                break;
            case R.id.remove:
removeFriend();
                break;
            case android.R.id.home:
finish();
                break;
        }


        return super.onOptionsItemSelected(item);
    }
    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("MMM d hh:mm a");
        Date date = new Date(); return dateFormat.format(date);
    }
private void inviteToPlace(){
    String currentDateTime = DateFormat.getDateTimeInstance(DateFormat.DEFAULT,DateFormat.LONG).format(Calendar.getInstance().getTime());

    String date=getDateTime();
 final PlaceInvite invite=new PlaceInvite(MainActivity.mCurrentPlace,MainActivity.myName,MainActivity.myID,date,MainActivity.myAvatarUrl,false);
    if(MainActivity.mCurrentPlace!=null) {
        new AlertDialog.Builder(context)
                .setTitle("Invite " + myName + "?")
                .setMessage("Invite " + myName + " to hang out at " + MainActivity.mCurrentPlace.getName() + "?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        DatabaseReference ref = mDatabase.child("Users").child(friendId).child("Invites").child(MainActivity.myID);
                        ref.setValue(invite).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Snackbar.make(coordinatorLayout, "Invite Sent! ", Snackbar.LENGTH_SHORT).show();

                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(R.drawable.small_fire)
                .show();
    }else{
        new AlertDialog.Builder(context)
                .setTitle("No Places")
                .setMessage("There aren't any Places near you")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                    }
                })

                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

}
    void removeFriend(){
        new AlertDialog.Builder(context)
                .setTitle("Remove friend")
                .setMessage("Are you sure you want to remove "+myName+" from your friend list?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        if(MainActivity.myFriends.contains(thisFriend)){
                            MainActivity.myFriends.remove(thisFriend);
                        }
                        DatabaseReference ref=mDatabase.child("Users").child(MainActivity.myID).child("Friends");
                        ref.child(friendId).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                Snackbar.make(coordinatorLayout, "Friend removed! ", Snackbar.LENGTH_SHORT).show();

                            }
                        });
                        DatabaseReference ref2=mDatabase.child("Users").child(friendId).child("Friends");
                        ref2.child(MainActivity.myID).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                            //    Snackbar.make(coordinatorLayout, "Friend removed! ", Snackbar.LENGTH_SHORT).show();

                            }
                        });
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_delete)
                .show();

    }
    private void getUserInfo(String id) {
        DatabaseReference ref = mDatabase.child("Users").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot d : dataSnapshot.child("Favorites").getChildren()) {

                        favorites.add( d.getValue(Place.class));
                        Log.e("Profile", "favs " + d.getValue().toString());
                        adapter.notifyDataSetChanged();
                    }
                    final Place p=dataSnapshot.child("Current Place").getValue(Place.class);
                    if(p!=null) {
                        placeName.setText(p.getName());
                        placeName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MapsActivity.thisPlace=p;
                                startActivity(new Intent(getApplicationContext(),PlaceInfoActivity.class));
                            }
                        });
                    }else{
                        placeName.setText("Unknown");
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getInterests(final String id) {
        DatabaseReference ref = mDatabase.child("Interests").child(id);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    for (DataSnapshot d : dataSnapshot.getChildren()) {


                        myInterests.add(d.getValue(Interest.class));
                        Log.e("Profile", "ints " + d.getValue().toString());
                        myInterestRecyclerViewAdapter.notifyDataSetChanged();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupLists() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);

        interestRecyclerView = (RecyclerView) findViewById(R.id.interest_layout);
        interestRecyclerView.setAdapter(myInterestRecyclerViewAdapter);

        int spanCount = 3; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = false;
        interestRecyclerView.setLayoutManager(gridLayoutManager);
        interestRecyclerView.addItemDecoration(new DividerItemDecoration(spanCount, spacing, includeEdge));
        //adapter.setHasStableIds(true);
        favRecycler.setLayoutManager(layoutManager);
        favRecycler.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
