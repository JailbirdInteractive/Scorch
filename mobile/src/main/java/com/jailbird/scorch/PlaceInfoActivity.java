package com.jailbird.scorch;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.andremion.floatingnavigationview.FloatingNavigationView;
import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.SkuDetails;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.PlacePhotoMetadataBuffer;
import com.google.android.gms.location.places.PlacePhotoMetadataResult;
import com.google.android.gms.location.places.PlacePhotoResult;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.meetic.marypopup.MaryPopup;
import com.nineoldandroids.animation.Animator;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.yalantis.ucrop.UCrop;


import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;
import jp.wasabeef.blurry.Blurry;
import noman.googleplaces.NRPlaces;
import noman.googleplaces.PlacesException;
import noman.googleplaces.PlacesListener;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.jailbird.scorch.MainActivity.myLocation;
import static com.jailbird.scorch.MapsActivity.thisPlace;

public class PlaceInfoActivity extends AppCompatActivity implements View.OnClickListener, MapsActivity.OnListFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener, BillingProcessor.IBillingHandler {
    private static final int REQUEST_INVITE = 989;
    ImageView frissonView;
    Bitmap placeImage;
    int duration;

    private List<Interest> interestList;
    String pID;
    static final float MI = 0.621371f;
    String result_in_kms = "1";
    ImageButton boostButton;
    noman.googleplaces.Place place;

    private FloatingNavigationView mFloatingNavigationView;
    TextView placeName;
    CircleImageView placePhoto;
    LinearLayout boost8, boost12, boost24;
    //List<Bitmap> bitmaps;
    List<Post> comments = new ArrayList<>();
    Context context;
    MaryPopup maryPopup;
    private DatabaseReference mDatabase;
    CommentsAdapter adapter;
    PhotoAdapter photoAdapter;
    FirebaseStorage storage;
    RecyclerView recyclerView, placePhotoView, interestView;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    CoordinatorLayout coordinatorLayout;
    List<String> photoUrls = new ArrayList<>();
    MapsActivity.OnListFragmentInteractionListener mListener;
    ImageView favIcon;
    String drivingDistance;
    LinearLayoutManager layoutManager;
    GoogleApiClient mGoogleApiClient;
    String url = "";
    NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private String myName = "User Name";
    String myID = "User ID";
    BillingProcessor bp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setStatusBarTranslucent(true);

        setContentView(R.layout.activity_place_info);
        mListener = this;
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)

                .enableAutoManage(this, this)
                .build();
        bp = new BillingProcessor(this, getString(R.string.licence_key), this);
        placeName = (TextView) findViewById(R.id.place_name);
        frissonView = (ImageView) findViewById(R.id.frisson_view);
        interestList = new ArrayList<>();
        //bitmaps = new ArrayList<>();
        //photoLayout = (LinearLayout) findViewById(R.id.photo_layout);
        boostButton = (ImageButton) findViewById(R.id.boost_button);
        boostButton.setOnClickListener(this);
        //boostButton.setVisibility(View.GONE);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            myName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {

                String id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String com = ".com";
                String rep = "";
                if (id != null) {
                    String sub = id.replace(com, rep);
                    myID = sub.replaceAll("[-+.^:,]", "");

                }
            } else {
                myID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            }
        }
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        View header = navigationView.getHeaderView(0);
        TextView txt = (TextView) header.findViewById(R.id.header_name);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Snackbar.make(this, menuItem.getTitle() + " pressed", Snackbar.LENGTH_LONG).show();

                //menuItem.setChecked(true);
                switch (menuItem.getItemId()) {
                    case R.id.comment:
                        View popup = LayoutInflater.from(context).inflate(R.layout.make_post_layout, null, false);

                        maryPopup = MaryPopup.with((Activity) context)
                                .cancellable(true)
                                .blackOverlayColor(Color.parseColor("#DD444444"))
                                .backgroundColor(Color.parseColor("#EFF4F5"))
                                .content(popup)

                                .from(placeName)
                                .center(true);

                        maryPopup.show();
                        final MaterialEditText editText = (MaterialEditText) popup.findViewById(R.id.post_text);
                        TextView ok = (TextView) popup.findViewById(R.id.post_button);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String post = String.valueOf(editText.getText());
                                Date date = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());

                                String stringDate = dateFormat.format(date);

                                Log.e("Post Date", "" + date);

                                if (!post.isEmpty() && editText.isCharactersCountValid()) {
                                    writeNewPost(myID, myName, stringDate, post);
                                    Snackbar.make((View) mFloatingNavigationView.getParent(), " Posting comment! ", Snackbar.LENGTH_SHORT).show();
                                    Log.e("Post Error", "" + editText.getError() + " valid " + editText.isCharactersCountValid());
                                    maryPopup.close(true);
                                }
                            }
                        });
                        break;
                    case R.id.add_photo:
                        new Permissive.Request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .whenPermissionsGranted(new PermissionsGrantedListener() {
                                    @Override
                                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                                        addPhoto();
                                    }
                                }).execute((Activity) context);
                        break;
                    case R.id.directions:
                        showDirections();
                        break;
                    case R.id.add_favs:
                        addFavs();
                        break;
                    case R.id.share_place:
                        onInviteClicked();

                        break;
                }
                drawerLayout.closeDrawers();

                return true;

            }

        });
        interestView = (RecyclerView) findViewById(R.id.interest_view);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 5);
        interestView.setLayoutManager(gridLayoutManager);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        placePhotoView = (RecyclerView) findViewById(R.id.place_photos);
        placePhotoView.setLayoutManager(layoutManager);
        photoAdapter = new PhotoAdapter(photoUrls, mListener, context);
        favIcon = (ImageView) findViewById(R.id.fav_icon);

        photoAdapter.setHasStableIds(true);
        placePhotoView.setAdapter(photoAdapter);
        placePhoto = (CircleImageView) findViewById(R.id.place_image);
        context = this;
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        View popup = LayoutInflater.from(context).inflate(R.layout.make_post_layout, null, false);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        maryPopup = MaryPopup.with((Activity) context)
                .cancellable(true)
                .blackOverlayColor(Color.parseColor("#DD444444"))
                .backgroundColor(Color.parseColor("#EFF4F5"))
                .content(popup)

                .from(placeName)
                .center(true);
        place = thisPlace;
        txt.setText(place.getName());
        pID = place.getPlaceId();
        //Log.i("Place", "types: " + place.getTypes());

        getInterests(place);
       // float d1 = getDistance(myLocation.getLatitude(), myLocation.getLongitude(), place.getLatitude(), place.getLongitude());

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);
        //collapsingToolbarLayout.setTitle(thisPlace.getName());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            actionBar.setTitle(thisPlace.getName());

            //actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left_black_48dp);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        //appBarLayout.setExpanded(true);
        if (getIntent().hasExtra("image")) {

            placeImage = (Bitmap) getIntent().getParcelableExtra("image");
            // String name = getIntent().getStringExtra("place name");
            //pID = getIntent().getStringExtra("id");
            //placeName.setText(name);
            if (placeImage != null) {
                Blurry.with(context).from(placeImage).into(frissonView);

                //frissonView.setImageBitmap(placeImage);
                placePhoto.setImageBitmap(placeImage);
            }
        }


        mFloatingNavigationView = (FloatingNavigationView) findViewById(R.id.floating_navigation_view);
        mFloatingNavigationView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //mFloatingNavigationView.open();
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        mFloatingNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.comment:
                        View popup = LayoutInflater.from(context).inflate(R.layout.make_post_layout, null, false);

                        maryPopup = MaryPopup.with((Activity) context)
                                .cancellable(true)
                                .blackOverlayColor(Color.parseColor("#DD444444"))
                                .backgroundColor(Color.parseColor("#EFF4F5"))
                                .content(popup)
                                .draggable(true)
                                .from(placeName)
                                .center(true);

                        maryPopup.show();
                        final MaterialEditText editText = (MaterialEditText) popup.findViewById(R.id.post_text);
                        TextView ok = (TextView) popup.findViewById(R.id.post_button);
                        ok.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String post = String.valueOf(editText.getText());
                                Date date = new Date();
                                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());

                                String stringDate = dateFormat.format(date);

                                Log.e("Post Date", "" + date);

                                if (!post.isEmpty() && editText.isCharactersCountValid()) {
                                    writeNewPost(myID, myName, stringDate, post);
                                    Snackbar.make((View) mFloatingNavigationView.getParent(), R.string.posting_comment, Snackbar.LENGTH_SHORT).show();
                                    Log.e("Post Error", "" + editText.getError() + " valid " + editText.isCharactersCountValid());
                                    maryPopup.close(true);
                                }
                            }
                        });
                        break;
                    case R.id.add_photo:
                        new Permissive.Request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                                .whenPermissionsGranted(new PermissionsGrantedListener() {
                                    @Override
                                    public void onPermissionsGranted(String[] permissions) throws SecurityException {
                                        addPhoto();
                                    }
                                }).execute((Activity) context);
                        break;
                    case R.id.directions:
                        showDirections();
                        break;
                    case R.id.add_favs:
                        addFavs();
                        break;
                    case R.id.share_place:
                        onInviteClicked();

                        break;
                }
                //Snackbar.make((View) mFloatingNavigationView.getParent(), item.getTitle() + " Selected!", Snackbar.LENGTH_SHORT).show();
                mFloatingNavigationView.close();
                return true;
            }
        });
    }

    private void makeBoost() {
        List<SkuDetails> boost_skus = new ArrayList<>();
        ArrayList<String> boostIds = new ArrayList<String>(Arrays.asList("boost_8", "boost_12", "boost_24"));
        boost_skus = bp.getPurchaseListingDetails(boostIds);

        View popup = LayoutInflater.from(context).inflate(R.layout.boost_layout, null);
        Button button = (Button) popup.findViewById(R.id.boost_confirm);
        popup.findViewById(R.id.boost_12);
        boost8 = (LinearLayout) popup.findViewById(R.id.boost_8);
        boost12 = (LinearLayout) popup.findViewById(R.id.boost_12);
        boost24 = (LinearLayout) popup.findViewById(R.id.boost_24);
        TextView b8 = (TextView) popup.findViewById(R.id.boost_price8);
        TextView b12 = (TextView) popup.findViewById(R.id.boost_price12);
        TextView b24 = (TextView) popup.findViewById(R.id.boost_price24);

        if (!boost_skus.isEmpty()) {
            b8.setText(boost_skus.get(2).priceText);
            b12.setText(boost_skus.get(0).priceText);
            b24.setText(boost_skus.get(1).priceText);
        }
        popup.findViewById(R.id.boost_8).setOnClickListener(this);
        popup.findViewById(R.id.boost_12).setOnClickListener(this);
        popup.findViewById(R.id.boost_24).setOnClickListener(this);
        final TextSwitcher mSwitcher = (TextSwitcher) popup.findViewById(R.id.boost_text_switcher);

        // Set the ViewFactory of the TextSwitcher that will create TextView object when asked
        mSwitcher.setFactory(new ViewSwitcher.ViewFactory() {

            public View makeView() {
                // TODO Auto-generated method stub
                // create new textView and set the properties like clolr, size etc
                TextView myText = new TextView(context);
                myText.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
                myText.setTextSize(13);
                myText.setTextColor(Color.BLACK);
                return myText;
            }
        });
        Animation in = AnimationUtils.loadAnimation(this, android.R.anim.slide_in_left);
        Animation out = AnimationUtils.loadAnimation(this, android.R.anim.slide_out_right);

        // Declare the in and out animations and initialize them

        // set the animation type of textSwitcher
        mSwitcher.setInAnimation(in);
        mSwitcher.setOutAnimation(out);
        mSwitcher.setText("Boost the visibility of your Place");
        mSwitcher.postDelayed(new Runnable() {
            int i = 0;

            public void run() {
                mSwitcher.setText(
                        i++ % 2 == 0 ?
                                "Let people know when they're nearby" :
                                "Broadcast to your whole area");
                mSwitcher.postDelayed(this, 3002);
            }
        }, 3002);
        final ImageSwitcher imageSwitcher = (ImageSwitcher) popup.findViewById(R.id.boost_switcher);
        //imageSwitcher.setImageResource(R.drawable.ic_launcher);
        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                return myView;
            }
        });

        imageSwitcher.setInAnimation(in);
        imageSwitcher.setOutAnimation(out);
        imageSwitcher.setImageResource(R.drawable.blue_rocket);
        imageSwitcher.postDelayed(new Runnable() {
            int i = 0;

            public void run() {
                imageSwitcher.setImageResource(
                        i++ % 3 == 0 ?
                                R.drawable.icon_location :
                                R.drawable.flat_city);
                imageSwitcher.postDelayed(this, 3000);
            }
        }, 3000);

        ContextThemeWrapper mTheme = new ContextThemeWrapper(this,
                R.style.YOUR_STYE);

        // mDialog = new Dialog(this,0); // context, theme

        final Dialog mDialog = new Dialog(mTheme);
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (duration) {
                    case 8:
                        bp.purchase(PlaceInfoActivity.this, "boost_8");
                        break;
                    case 12:
                        bp.purchase(PlaceInfoActivity.this, "boost_12");
                        break;
                    case 24:
                        bp.purchase(PlaceInfoActivity.this, "boost_24");
                        break;
                }
                mDialog.dismiss();
            }
        });
/*
    maryPopup = MaryPopup.with((Activity) context)
            .cancellable(true)
            .blackOverlayColor(Color.parseColor("#DD444444"))
            .backgroundColor(getResources().getColor(R.color.transparent))

            .content(popup)
            .draggable(true)
            .from(boostButton)
            .center(true);

    maryPopup.show();
*/

    }

    private void isFav() {
        if (MainActivity.myFavorites.contains(place)) {
            favIcon.setVisibility(View.VISIBLE);
        } else {
            favIcon.setVisibility(View.GONE);
        }
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {

        if (makeTranslucent) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window w = getWindow(); // in Activity's onCreate() for instance
                w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            }
        }
    }

    private void addFavs() {

        if (!MainActivity.myFavorites.contains(place)) {

            MainActivity.myFavorites.add(place);
            mDatabase.child("Users").child(myID).child("Favorites").setValue(MainActivity.myFavorites).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(coordinatorLayout, R.string.fav_added, Snackbar.LENGTH_SHORT).show();
                    YoYo.with(Techniques.ZoomIn).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            favIcon.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).duration(500)
                            .playOn(favIcon);
                }
            });
        } else {
            Snackbar.make(coordinatorLayout, R.string.is_favorite, Snackbar.LENGTH_LONG).setAction(R.string.remove_fav, new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    MainActivity.myFavorites.remove(place);
                    mDatabase.child("Users").child(myID).child("Favorites").setValue(MainActivity.myFavorites).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Snackbar.make(coordinatorLayout, R.string.fav_remove, Snackbar.LENGTH_SHORT).show();
                            YoYo.with(Techniques.ZoomOut).withListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    favIcon.setVisibility(View.VISIBLE);
                                }

                                @Override
                                public void onAnimationCancel(Animator animation) {

                                }

                                @Override
                                public void onAnimationRepeat(Animator animation) {

                                }
                            }).duration(500)
                                    .playOn(favIcon);
                        }
                    });
                }
            }).show();

        }
    }

    private void writeNewPost(String userId, String username, String title, String body) {
        // Create new post at /user-posts/$userid/$postid and at
        // /posts/$postid simultaneously
        String key = mDatabase.child("Places").child(pID).push().getKey();
        Post post = new Post(userId, username, title, body);
        /*Map<String, Object> postValues = post.toMap();

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/Places/" + key, postValues);
        childUpdates.put("/user-posts/" + userId + "/" + key, postValues);

        mDatabase.updateChildren(childUpdates);*/

        mDatabase.child("Places").child(pID).child("Comments").push().setValue(post).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //refreshComments();
            }
        });
    }

    private void boostPlace(int hours) {
        Calendar cal = Calendar.getInstance();

        Date startDate = cal.getTime();
        Calendar endCal = Calendar.getInstance();
        endCal.add(Calendar.HOUR, hours);
        Date endDate = endCal.getTime();
        BoostedPlace boostedPlace = new BoostedPlace(place, startDate, endDate);
        mDatabase.child("Boosted Places").child(pID).setValue(boostedPlace).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //refreshComments();
                Snackbar.make(coordinatorLayout, place.getName() + " Boosted! ", Snackbar.LENGTH_SHORT).show();
                startService(new Intent(context, LoaderService.class));

            }
        });
    }

    private void refreshComments() {
        DatabaseReference ref = mDatabase.child("Places").child(pID).child("Comments");
//recyclerView.removeAllViews();
        comments.clear();
        recyclerView = (RecyclerView) findViewById(R.id.comments_view);

        ref.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("Post Changed", "Comment added" + dataSnapshot.getValue());


                //String key=postsnap.getKey();

                Post post = dataSnapshot.getValue(Post.class);
                comments.add(post);
                recyclerView.removeAllViews();

                adapter = new CommentsAdapter(comments);
                //adapter.swap(comments);
                adapter.setHasStableIds(true);

                recyclerView.setAdapter(adapter);
                recyclerView.smoothScrollToPosition(comments.size());

                //Log.e("Post Changed", "Comment added" + postsnap.getValue());
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e("Post Changed", "Comment removed" + dataSnapshot.getValue());

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getComments(final String id) {
        DatabaseReference ref = mDatabase.child("Places").child(id).child("Comments");

        recyclerView = (RecyclerView) findViewById(R.id.comments_view);
        ref.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.e("Post ", "Comment: " + dataSnapshot.getValue());

                for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
                    if (postsnap.getValue() != null) {
                        //String key=postsnap.getKey();
                        Post post = postsnap.getValue(Post.class);
                        comments.add(post);
                        recyclerView.removeAllViews();

                        adapter = new CommentsAdapter(comments);
                        //adapter.swap(comments);
                        adapter.setHasStableIds(true);

                        recyclerView.setAdapter(adapter);
                        Log.e("Post ", "Comment: " + post.body);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.place_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                //handle the home button onClick event here.
                finish();
                break;
            case R.id.add_photo:
                new Permissive.Request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .whenPermissionsGranted(new PermissionsGrantedListener() {
                            @Override
                            public void onPermissionsGranted(String[] permissions) throws SecurityException {
                                addPhoto();
                            }
                        }).execute((Activity) context);
                drawerLayout.closeDrawers();
                break;
            case R.id.directions:
                showDirections();
                drawerLayout.closeDrawers();

                break;
            case R.id.add_favs:
                addFavs();
                drawerLayout.closeDrawers();

                break;
            case R.id.share_place:
                onInviteClicked();
                drawerLayout.closeDrawers();

                break;
            case R.id.comment:
                View popup = LayoutInflater.from(context).inflate(R.layout.make_post_layout, null, false);

                maryPopup = MaryPopup.with((Activity) context)
                        .cancellable(true)
                        .blackOverlayColor(Color.parseColor("#DD444444"))
                        .backgroundColor(Color.parseColor("#EFF4F5"))
                        .content(popup)

                        .from(placeName)
                        .center(true);

                maryPopup.show();
                final MaterialEditText editText = (MaterialEditText) popup.findViewById(R.id.post_text);
                TextView ok = (TextView) popup.findViewById(R.id.post_button);
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String post = String.valueOf(editText.getText());
                        Date date = new Date();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss", Locale.getDefault());

                        String stringDate = dateFormat.format(date);

                        Log.e("Post Date", "" + date);

                        if (!post.isEmpty() && editText.isCharactersCountValid()) {
                            writeNewPost(myID, myName, stringDate, post);
                            Snackbar.make((View) mFloatingNavigationView.getParent(), " Posting comment! ", Snackbar.LENGTH_SHORT).show();
                            Log.e("Post Error", "" + editText.getError() + " valid " + editText.isCharactersCountValid());
                            maryPopup.close(true);
                        }
                    }
                });
                drawerLayout.closeDrawers();

                break;
           /*
            case R.id.boost_place:
                makeBoost();
                break;
*/
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //place = thisPlace;
        setup(place);
        placePhotosAsync(pID);

        //getInterests(place);
        //getplacePhotosAsync(pID);
        //getComments(pID);
        getPhotos(pID);
        refreshComments();

    }

    void setup(noman.googleplaces.Place place) {
        String name = place.getName();
        placeName.setText(name);
        isFav();
        // Location thisPlace=new Location("Place");
        //thisPlace.setLatitude(place.getLatitude());
        //thisPlace.setLongitude(place.getLongitude());
        //float distance=myLocation.distanceTo(thisPlace);
        float d1=getDistance(myLocation.getLatitude(),myLocation.getLongitude(),place.getLatitude(),place.getLongitude());
        // Log.w("Distance", "Distance " + d1);


        /*
        TextView addressTxt = (TextView) findViewById(R.id.address_text);
        String d=String.format("%.2f", convertedDistance);
        addressTxt.setText(""+d+" "+MainActivity.distanceUnit+" away");
        */
        //addressTxt.setText(getCompleteAddressString(place.getLatitude(), place.getLongitude()));
    }


    public float getDistance(double lat1, double lon1, double lat2, double lon2) {
        if (MainActivity.distanceUnit.equals("Mi.")) {
            url = "http://maps.google.com/maps/api/directions/xml?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=imperial";
        } else {
            url = "http://maps.google.com/maps/api/directions/xml?origin=" + lat1 + "," + lon1 + "&destination=" + lat2 + "," + lon2 + "&sensor=false&units=metric";
        }

        final String tag[] = {"text"};
        //HttpResponse response = null;
        new Thread(new Runnable() {
            public void run() {

                try {

                    // do something


                    OkHttpClient httpClient = new OkHttpClient();
                    //HttpContext localContext = new BasicHttpContext();
                    // RequestBody body = RequestBody.create(JSON, json);

                    Request request = new Request.Builder()
                            .url(url)

                            .build();

                    Response response = httpClient.newCall(request).execute();
                    //response = httpClient.execute(httpPost, localContext);
                    InputStream is = response.body().byteStream();
                    DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
                    Document doc = builder.parse(is);
                    if (doc != null) {
                        NodeList nl;
                        ArrayList args = new ArrayList();
                        for (String s : tag) {
                            nl = doc.getElementsByTagName(s);
                            if (nl.getLength() > 0) {
                                Node node = nl.item(nl.getLength() - 1);
                                args.add(node.getTextContent());
                            } else {
                                args.add(" - ");
                            }
                        }
                        result_in_kms = String.valueOf(args.get(0));
                        drivingDistance = "" + String.valueOf(args.get(0)) + " away";
                        TextView addressTxt = (TextView) findViewById(R.id.address_text);
                        addressTxt.setText(drivingDistance);
                        Log.w("Distance", "Distance " + args.get(1));

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();

        Float f = Float.valueOf(result_in_kms);

        return f * 1000;
    }


    private void getAddress(noman.googleplaces.Location location) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        TextView addressTxt = (TextView) findViewById(R.id.address_text);
        addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        String city = addresses.get(0).getLocality();
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        String postalCode = addresses.get(0).getPostalCode();
        String knownName = addresses.get(0).getFeatureName();
        addressTxt.setText("Address: " + address);
    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                Log.w("My Current address", "" + strReturnedAddress.toString());
            } else {
                Log.w("My Current address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.w("My Current address", "Canont get Address!");
        }
        return strAdd;
    }

    void getInterests(final noman.googleplaces.Place place) {
        //placePhotosAsync(thispID);
        new AsyncTask<Void, Void, Void>() {
            List<String> types = place.getTypes();

            @Override
            protected void onPreExecute() {
                super.onPreExecute();

            }

            @Override
            protected Void doInBackground(Void... params) {
                //Looper.prepare();

                for (Interest interest : MainActivity.myInterests) {
                    Log.d("Place Info", " Place interest type " + interest.placeTypes);

                    for (int i = 0; i < types.size(); i++) {
                        //Log.d("Place Info", " Place type " + interest.placeTypes);

                        if (interest.placeTypes.contains(types.get(i))) {
                            if (!interestList.contains(interest)) {
                                interestList.add(interest);
                                Log.d("Place Info", " Place fits Interest: " + interest.interestName);
                            }

                        } else {
                            Log.d("Place Info", " Place fits no Interests.");

                        }
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void dummy) {
                interestView.setAdapter(new MyInterestRecyclerViewAdapter(interestList, null, context));

            }


        }.execute();


    }

    private void addPhoto() {
        final UCrop.Options options = new UCrop.Options();
        options.setActiveWidgetColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setMaxBitmapSize(1024);
        options.setCompressionQuality(80);
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(PlaceInfoActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {

                        try {
                            //Uri tmp = Uri.fromFile(File.createTempFile("placeimg", ".jpg", getExternalCacheDir()));
                            File newFile = File.createTempFile("placeimg", ".jpg", getExternalCacheDir());

                            Uri tmp = Uri.fromFile(newFile);
                            UCrop.of(uri, tmp)
                                    .withOptions(options)

                                    .withMaxResultSize(1024, 1024)
                                    .start((Activity) context);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // here is selected uri
                    }
                })
                .showCameraTile(false)

                .setOnErrorListener(new TedBottomPicker.OnErrorListener() {
                    @Override
                    public void onError(String message) {
                        Log.e("Photo ", "Photo error: " + message);

                    }
                })
                .create();

        tedBottomPicker.show(getSupportFragmentManager());

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final StorageReference storageRef = storage.getReferenceFromUrl(getString(R.string.photo_bucket)).child("Place Images/" + pID);

            final Uri resultUri = UCrop.getOutput(data);
            final StorageReference photoRef = storageRef.child("images/" + resultUri.getLastPathSegment());
            UploadTask uploadTask = photoRef.putFile(resultUri);

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();

                    Uri downloadUri = Uri.parse(photoRef.getPath());

                    //DatabaseReference myRef= mDatabase.child("trails").push();
                    //myRef.setValue(img);
                    mDatabase.child("Places").child(pID).child("Images").push().setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Snackbar.make(coordinatorLayout, " Photo Added! ", Snackbar.LENGTH_SHORT).show();
                            getPhotos(pID);
                        }
                    });

                }
            });
        } else if (resultCode == UCrop.RESULT_ERROR) {
            Snackbar.make(coordinatorLayout, " Something went wrong! ", Snackbar.LENGTH_SHORT).show();

            final Throwable cropError = UCrop.getError(data);
        }
        if (requestCode == REQUEST_INVITE) {
            if (resultCode == RESULT_OK) {
                // Get the invitation IDs of all sent messages
                String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                for (String id : ids) {
                }
            } else {
                // Sending failed or it was canceled, show failure message to the user
                // ...
            }
        }
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);

        super.onActivityResult(requestCode, resultCode, data);

    }

    private void showDirections() {
        String uri = "http://maps.google.com/maps?f=d&hl=en&saddr=" + myLocation.getLatitude() + "," + myLocation.getLongitude() + "&daddr=" + place.getLatitude() + "," + place.getLongitude();
        Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(Intent.createChooser(intent, "Select an application"));
    }

    private void getPhotos(String id) {
        photoUrls.clear();
        DatabaseReference ref = mDatabase.child("Places").child(id).child("Images");
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        placePhotoView = (RecyclerView) findViewById(R.id.place_photos);
        placePhotoView.setLayoutManager(layoutManager);
        ref.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                // Log.e("Post ", "Comment: " + dataSnapshot.getValue());

                new AsyncTask<Void, Void, Void>() {
                    @Override
                    protected Void doInBackground(Void... voids) {

                        for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
                            if (postsnap.getValue() != null) {
                                photoUrls.add((String) postsnap.getValue());


                            }
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        if (photoUrls.size() > 0) {
                            Picasso.with(PlaceInfoActivity.this).load(photoUrls.get(0)).placeholder(R.drawable.blue_fire2).fit().into(placePhoto, new Callback() {
                                @Override
                                public void onSuccess() {
                                    //Blurry.with(context).capture(placePhoto).into(frissonView);

                                }

                                @Override
                                public void onError() {

                                }
                            });
                            //Glide.with(PlaceInfoActivity.this).load(postsnap.getValue()).into(frissonView);
                            Log.e("Photo ", "Photo Url: " + photoUrls.get(0));

                            photoAdapter = new PhotoAdapter(photoUrls, mListener, context);
                            photoAdapter.setHasStableIds(true);
                            placePhotoView.removeAllViews();
                            placePhotoView.setAdapter(photoAdapter);
                        }


                        super.onPostExecute(aVoid);
                    }
                }.execute();


            }


            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    @Override
    protected void onDestroy() {
        if (bp != null)
            bp.release();
        super.onDestroy();
        //bitmaps.clear();
    }

    @Override
    public void onBackPressed() {

        if (drawerLayout.isDrawerOpen(navigationView)) {
            drawerLayout.closeDrawers();
        } else {
            if (!maryPopup.isOpened()) {
                super.onBackPressed();
            } else {
                maryPopup.close(true);
            }
        }

    }

    private ResultCallback<PlacePhotoResult> mDisplayPhotoResultCallback
            = new ResultCallback<PlacePhotoResult>() {
        @Override
        public void onResult(PlacePhotoResult placePhotoResult) {
            if (placePhotoResult.getStatus().isSuccess()) {
                placePhoto.setImageBitmap(placePhotoResult.getBitmap());
                Blurry.with(context).radius(15).from(placePhotoResult.getBitmap()).into(frissonView);

            } else {
                Picasso.with(PlaceInfoActivity.this).load(place.getIcon()).fit().into(placePhoto);

            }


        }
    };

    private void placePhotosAsync(String id) {
        Places.GeoDataApi.getPlacePhotos(mGoogleApiClient, id)
                .setResultCallback(new ResultCallback<PlacePhotoMetadataResult>() {


                    @Override
                    public void onResult(PlacePhotoMetadataResult photos) {
                        if (!photos.getStatus().isSuccess()) {
                            Log.e("Photo", "result " + photos.getStatus().toString());
                            return;
                        } else {
                            Log.e("Photo", "result " + photos.getStatus().toString());

                        }

                        PlacePhotoMetadataBuffer photoMetadataBuffer = photos.getPhotoMetadata();

                        if (photoMetadataBuffer.getCount() > 0) {
                            // Display the first bitmap in an ImageView in the size of the view
                            photoMetadataBuffer.get(0)
                                    .getScaledPhoto(mGoogleApiClient, 300,
                                            300)
                                    .setResultCallback(mDisplayPhotoResultCallback);
                        }

                        photoMetadataBuffer.release();
                    }
                });

    }

    void getPlace(final String placeId) {
        String name = getIntent().getStringExtra("place name");
/*
        Places.GeoDataApi.getPlaceById(mGoogleApiClient, placeId)
                .setResultCallback(new ResultCallback<PlaceBuffer>() {
                    @Override
                    public void onResult(PlaceBuffer places) {
                        if (places.getStatus().isSuccess() && places.getCount() > 0) {
                            final Place myPlace = places.get(0);
                            place=myPlace;
                            Log.i("Place Info", "Place found: " + myPlace.getName());
                        } else {
                            Log.e("Place Info", "Place not found");
                        }
                        places.release();
                    }
                });

*/

        new NRPlaces.Builder()
                .listener(new PlacesListener() {
                    @Override
                    public void onPlacesFailure(PlacesException e) {
                        e.printStackTrace();
                        Log.e("Place Info", "Place not found");

                    }

                    @Override
                    public void onPlacesStart() {

                    }

                    @Override
                    public void onPlacesSuccess(List<noman.googleplaces.Place> places) {
                        place = places.get(0);

                        Log.i("Place Info", "Place found: " + place.getName());

                    }

                    @Override
                    public void onPlacesFinished() {
                        //placePhotosAsync(placeId);
                        getInterests(place);
                    }
                })
                .key(getString(R.string.map_api_key))
                .latlng(myLocation.getLatitude(), myLocation.getLongitude())
                .radius(1500)
                .keyword(name)
                .build()
                .execute();
    }

    private void getplacePhotosAsync(String id) {
        new PhotoTask(200, 200, mGoogleApiClient, id) {
            @Override
            protected void onPreExecute() {
                // Display a temporary image to show while bitmap is loading.
                //mImageView.setImageResource(R.drawable.empty_photo);
            }

            @Override
            protected void onPostExecute(AttributedPhoto attributedPhoto) {
                if (attributedPhoto != null) {
                    placePhoto.setImageBitmap(attributedPhoto.bitmap);


                    //startActivity(new Intent(MapsActivity.this, PlaceInfoActivity.class).putExtra("image", placeIcon).putExtra("place name", pointOfInterest.name).putExtra("id",pointOfInterest.placeId));
                    // Display the attribution as HTML content if set.
                    if (attributedPhoto.attribution == null) {

                    } else {

                    }

                } else {
                    Log.e("Photo", "no photo");
                    //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.nightsml);


                }
            }
        }.execute(id);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.boost_button:
                makeBoost();
                break;
            case R.id.boost_8:
                duration = 8;

                view.setSelected(!view.isSelected());
                if (boost12 != null && boost24 != null) {
                    boost12.setSelected(false);
                    boost24.setSelected(false);
                }
                break;
            case R.id.boost_12:
                duration = 12;
                if (boost8 != null && boost24 != null) {
                    boost8.setSelected(false);
                    boost24.setSelected(false);
                }
                view.setSelected(!view.isSelected());
                break;
            case R.id.boost_24:

                duration = 24;
                if (boost12 != null && boost8 != null) {
                    boost12.setSelected(false);
                    boost8.setSelected(false);
                }
                view.setSelected(!view.isSelected());
                break;
        }
    }

    @Override
    public void onListFragmentInteraction(View item, final int position) {
        Log.e("Photo", "Click ");
        String tag = (String) item.getTag();
        if (tag != null && tag.equalsIgnoreCase("ADD")) {
            addPhoto();
        } else {
            View popup = LayoutInflater.from(context).inflate(R.layout.gallery_layout, null, false);
            final ScrollGalleryView scrollGalleryView = (ScrollGalleryView) popup.findViewById(R.id.scroll_gallery_view);
            List<MediaInfo> infos = new ArrayList<>(photoUrls.size());
            for (String url : photoUrls)
                infos.add(MediaInfo.mediaLoader(new PicassoImageLoader(url)));
            scrollGalleryView
                    .setThumbnailSize(200)
                    .setZoom(true)

                    .setFragmentManager(getSupportFragmentManager())
                    .addMedia(infos)

                    .setCurrentItem(position);

            maryPopup = MaryPopup.with((Activity) context)
                    .cancellable(true)
                    .blackOverlayColor(Color.parseColor("#DD444444"))
                    .backgroundColor(Color.parseColor("#EFF4F5"))
                    .content(popup)
                    .from(item)
                    .center(true);
            maryPopup.show();
            final ImageView more = (ImageView) popup.findViewById(R.id.more);
            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    PopupMenu popup = new PopupMenu(PlaceInfoActivity.this, more);
                    //Inflating the Popup using xml file
                    popup.getMenuInflater()
                            .inflate(R.menu.place_popup, popup.getMenu());

                    //registering popup with OnMenuItemClickListener
                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        public boolean onMenuItemClick(MenuItem item) {
                            reportPhoto(photoUrls.get(position));

                            Toast.makeText(
                                    PlaceInfoActivity.this,
                                    "Report Photo: " + photoUrls.get(position),
                                    Toast.LENGTH_SHORT
                            ).show();


                            return true;
                        }
                    });

                    popup.show(); //showing popup menu
                }

            });
        }

    }

    void reportPhoto(String id) {
        DatabaseReference ref = mDatabase.child("Reports");
        ref.child(pID).push().setValue(id);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    private void onInviteClicked() {
        Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                .setMessage("" + myName + " " + getString(R.string.place_invite_text) + " " + place.getName())
                .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))

                .setCustomImage(Uri.parse("https://jailbirdinteractive.com/images/newfire.png"))
                .setCallToActionText(getString(R.string.invitation_cta))
                .build();
        startActivityForResult(intent, REQUEST_INVITE);
    }


    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
        boostPlace(duration);
        bp.consumePurchase(productId);
    }

    @Override
    public void onPurchaseHistoryRestored() {

    }

    @Override
    public void onBillingError(int errorCode, Throwable error) {

    }

    @Override
    public void onBillingInitialized() {

    }
}
