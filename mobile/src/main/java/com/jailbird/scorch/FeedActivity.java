package com.jailbird.scorch;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.anjlab.android.iab.v3.BillingProcessor;
import com.anjlab.android.iab.v3.TransactionDetails;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yalantis.ucrop.UCrop;

import java.util.ArrayList;
import java.util.List;

import noman.googleplaces.Place;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FeedActivity extends AppCompatActivity implements EventFragment.OnListFragmentInteractionListener, GoogleApiClient.OnConnectionFailedListener, BillingProcessor.IBillingHandler {

    private TextView mTextMessage;
    private static final int NUM_PAGES = 2;
    public static Event thisEvent;
    public static List<Event> eventList = new ArrayList<>();
    android.support.v7.app.ActionBar actionBar;
    public static BillingProcessor bp;
    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager mPager;
    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter mPagerAdapter;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener;
    private DatabaseReference mDatabase;
    private Context context;
    public static GoogleApiClient mGoogleApiClient;
    private FirebaseStorage storage;
    private String myId="UserID";
FloatingActionButton eventButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        bp = new BillingProcessor(this, getString(R.string.licence_key), this);

//getEvents();
        context = this;
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            actionBar = getSupportActionBar();
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        mGoogleApiClient = new GoogleApiClient
                .Builder(context)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .addApi(Plus.API)

                .enableAutoManage(this, this)
                .build();
        eventButton= (FloatingActionButton) findViewById(R.id.add_event_button);
        findViewById(R.id.add_event_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, CreateEventActivity.class));
            }
        });
        storage = FirebaseStorage.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            if (FirebaseAuth.getInstance().getCurrentUser().getEmail() != null) {
                String id = FirebaseAuth.getInstance().getCurrentUser().getEmail();
                String com = ".com";
                String rep = "";
                int index = id.lastIndexOf(".");

                if (id != null) {
                    String sub = id.replace(com, rep);
                    myId = sub.replaceAll("[-+.^:,]", "");
                } else {
                    myId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                }
            }
        }else{
            myId="UserID";
        }



        mTextMessage = (TextView) findViewById(R.id.message);

        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_home:
                       // mTextMessage.setText(R.string.title_home);
                        mPager.setCurrentItem(0, true);

                        return true;
                    case R.id.navigation_dashboard:
                        //mTextMessage.setText(R.string.title_dashboard);
                        mPager.setCurrentItem(1, true);

                        return true;
                    /*
                    case R.id.navigation_notifications:
                        mTextMessage.setText(R.string.title_notifications);
                        mPager.setCurrentItem(2, true);

                        return true;*/
                }
                return false;
            }

        };
        mPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
switch(position){
    case 0:
        actionBar.setTitle("Events");
eventButton.setVisibility(View.VISIBLE);

        break;
    case 1:
        actionBar.setTitle("Account");
        eventButton.setVisibility(View.GONE);

        break;


}
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onDestroy() {
        if (bp != null)
            bp.release();

        super.onDestroy();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            final StorageReference storageRef = storage.getReferenceFromUrl(getString(R.string.photo_bucket)).child("Profile Images/" + myId);

            StorageReference photoRef = storageRef.child("images/" + resultUri.getLastPathSegment());
            UploadTask uploadTask = photoRef.putFile(resultUri);
photoRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
    @Override
    public void onSuccess(Uri uri) {
        String dwnld="https://jailbirdinteractive.com/images/newfire.png";
        Log.e("URI1","uri"+dwnld+" ID "+myId);

        if(uri!=null){

            dwnld=uri.toString();

        }else{
            dwnld="https://jailbirdinteractive.com/images/newfire.png";
        }
        if(myId==null||myId.isEmpty()){
            myId="UserID";
        }
if(myId!=null&&dwnld!=null) {
    Log.e("URI","uri"+dwnld+" ID "+myId);
   try {
       mDatabase.child("Users").child(myId).child("User").child("photoUrl").setValue(dwnld).addOnCompleteListener(new OnCompleteListener<Void>() {
           @Override
           public void onComplete(@NonNull Task<Void> task) {
               //getPhotos(myId);
           }
       });
   }catch (NullPointerException e){
       e.printStackTrace();
   }
   }
    }
});

// Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle unsuccessful uploads
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @SuppressWarnings("VisibleForTests")
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                    //Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    //Snackbar.make(coordinatorLayout, " Photo Added! ", Snackbar.LENGTH_SHORT).show();

                    //DatabaseReference myRef= mDatabase.child("trails").push();
                    //myRef.setValue(img);


                }

            });


            /*
            Bitmap bmp = BitmapFactory.decodeFile(resultUri.getPath());//your image
            ByteArrayOutputStream bYtE = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, bYtE);
            bmp.recycle();
            byte[] byteArray = bYtE.toByteArray();
            String encodedImage = Base64.encodeToString(byteArray, Base64.DEFAULT);
            mDatabase.child("Users").child(myId).child("Profile Pic").setValue(encodedImage).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Snackbar.make(coordinatorLayout, " Photo Added! ", Snackbar.LENGTH_SHORT).show();
                    getPhotos(myId);
                }
            });
            */

        } else if (resultCode == UCrop.RESULT_ERROR) {
            final Throwable cropError = UCrop.getError(data);
            //Snackbar.make(coordinatorLayout, " Something went wrong! ", Snackbar.LENGTH_SHORT).show();

        }
        if (!bp.handleActivityResult(requestCode, resultCode, data))
            super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onListFragmentInteraction(Event item) {
        thisEvent = item;
        startActivity(new Intent(context, EventActivity.class));
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onProductPurchased(String productId, TransactionDetails details) {
if(productId.equalsIgnoreCase("remove_ads")){
    MainActivity.isPaid=true;
//bp.consumePurchase("remove_ads");
    }
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

    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Place item, User user);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:

                    return new EventFragment();
                case 1:
                    return new AccountFragment();
                default:
                    return new EventFragment();
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    private void getEvents() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Events");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot friendSnap : dataSnapshot.getChildren()) {
                    Log.d("Events", friendSnap.getValue().toString());
                    Event event = friendSnap.getValue(Event.class);
                    eventList.add(event);
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
        inflater.inflate(R.menu.account_menu, menu);
        final View notifications = menu.findItem(R.id.invite).getActionView();

        TextView txtViewCount = (TextView) notifications.findViewById(R.id.txtCount);
        if (MainActivity.friendRequests.size() > 0 || MainActivity.placeInviteList.size() > 0) {
            int count = MainActivity.friendRequests.size() + MainActivity.placeInviteList.size();
            txtViewCount.setText("" + count);
        } else {
            txtViewCount.setVisibility(View.GONE);
        }
        notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, FriendActivity.class));

            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.add_event:
                startActivity(new Intent(context, CreateEventActivity.class));
                break;
            case R.id.action_settings:
                startActivity(new Intent(context, SettingsActivity.class));

                break;
            case R.id.invite:
                //launchContactPicker(null);
                startActivity(new Intent(context, FriendActivity.class));


                break;
            case R.id.remove_ads:
                bp.purchase(this,"remove_ads");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
