package com.jailbird.scorch;

import android.*;
import android.Manifest;
import android.accounts.Account;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gigamole.library.PulseView;
import com.github.jksiezni.permissive.PermissionsGrantedListener;
import com.github.jksiezni.permissive.Permissive;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.places.Places;
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
import com.meetic.marypopup.MaryPopup;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.yalantis.ucrop.UCrop;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;

import de.hdodenhof.circleimageview.CircleImageView;
import gun0912.tedbottompicker.TedBottomPicker;
import jp.wasabeef.blurry.Blurry;
import mx.com.quiin.contactpicker.Contact;
import mx.com.quiin.contactpicker.PickerUtils;
import mx.com.quiin.contactpicker.SimpleContact;
import mx.com.quiin.contactpicker.interfaces.ContactSelectionListener;
import mx.com.quiin.contactpicker.ui.ContactPickerActivity;
import noman.googleplaces.Place;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.jailbird.scorch.MapsActivity.thisPlace;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, MainActivity.OnListFragmentInteractionListener, ContactSelectionListener {

    private static final int CONTACT_PICKER_REQUEST = 747;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    private CoordinatorLayout coordinatorLayout;
    Context context;
    CircleImageView profilePic;
    ImageView background, settingButton;
    String myId = "User ID";
    String myName = "User Name";
    TextView userName;
    List<Interest> myInterests;
    List<Object> myEvents;
    RecyclerView interestRecyclerView, favRecycler, eventRecycler;
    private GoogleApiClient mGoogleApiClient;
    MainActivity.OnListFragmentInteractionListener mListener;
    File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            //setStatusBarTranslucent(true);
            setContentView(R.layout.activity_account);
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_content);
        context = this;
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .addApi(Plus.API)

                .enableAutoManage(this, this)
                .build();
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Scorch Images/My Avatar");
        myDir.mkdirs();
        String fname = "avatar.jpg";
        file = new File(myDir, fname);
        settingButton = (ImageView) findViewById(R.id.settings_button);
        settingButton.setOnClickListener(this);
        userName = (TextView) findViewById(R.id.user_name);
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


            //myId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
            myName = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        }

        userName.setText(myName);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(true);
            actionBar.setTitle(myName);

            //actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left_black_48dp);
            actionBar.setDisplayHomeAsUpEnabled(true);

        }
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        AppBarLayout appBarLayout = (AppBarLayout) findViewById(R.id.app_bar);
        myInterests = new ArrayList<>();
        myEvents = new ArrayList<>();
        myInterests = MainActivity.myInterests;
        ImageButton editButton = (ImageButton) findViewById(R.id.edit_button);
        editButton.setOnClickListener(this);
        Button signoutButton = (Button) findViewById(R.id.sign_out_button);
        signoutButton.setOnClickListener(this);
        favRecycler = (RecyclerView) findViewById(R.id.favorites_layout);
        eventRecycler = (RecyclerView) findViewById(R.id.myevents_layout);
        mListener = this;
        setupLists();


        profilePic = (CircleImageView) findViewById(R.id.profile_image);
        background = (ImageView) findViewById(R.id.backgroundImage);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPhoto();
            }
        });
        FloatingActionButton interestButton = (FloatingActionButton) findViewById(R.id.interest_button);
        interestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PickerActivity.class).putExtra("isEdit", true));

            }
        });
        FloatingActionButton friendButton = (FloatingActionButton) findViewById(R.id.friend_button);
        friendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchContactPicker(view);
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
            case R.id.action_settings:
                startActivity(new Intent(context, SettingsActivity.class));

                break;
            case R.id.invite:
                //launchContactPicker(null);
                startActivity(new Intent(context, FriendActivity.class));


                break;

            case R.id.edit_photo:
                addPhoto();

                break;

            case R.id.edit_interest:
                startActivity(new Intent(getApplicationContext(), PickerActivity.class).putExtra("isEdit", true));


                break;
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);

    }

    private void showRequests() {
        RequestAdapter adapter = new RequestAdapter(MainActivity.requesters);
        LinearLayoutManager manager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        View popup = LayoutInflater.from(context).inflate(R.layout.request_layout, null, false);
        RecyclerView requestView = (RecyclerView) popup.findViewById(R.id.request_list);
        requestView.setLayoutManager(manager);
        requestView.setAdapter(adapter);

        MaryPopup maryPopup = MaryPopup.with((Activity) context)
                .cancellable(true)
                .blackOverlayColor(Color.parseColor("#DD444444"))
                //.backgroundColor(Color.parseColor("#EFF4F5"))
                .content(popup)

                .from(interestRecyclerView)
                .center(true);
        maryPopup.show();
    }

    public void launchContactPicker(View view) {
        // int permissionCheck = ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS);
        new Permissive.Request(Manifest.permission.READ_CONTACTS).whenPermissionsGranted(new PermissionsGrantedListener() {
            @Override
            public void onPermissionsGranted(String[] permissions) throws SecurityException {
                Intent contactPicker = new Intent(getApplicationContext(), ContactPickerActivity.class);
                //Don't show Chips
                contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SHOW_CHIPS, true);

                //Customize Floating action button color
                contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_FAB_COLOR, "#7f8be9");
                //Customize Selection drawable
                String customSelection = "(" + ContactsContract.Data.MIMETYPE + "=? )";
                String[] customArgs = new String[]{ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE};
                contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SELECTION, customSelection);
                contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SELECTION_ARGS, customArgs);
                contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_HAS_CUSTOM_SELECTION_ARGS, true);
                contactPicker.putExtra(ContactPickerActivity.CP_EXTRA_SELECTION_DRAWABLE, PickerUtils.sendDrawable(getResources(), R.drawable.ic_done_white_36dp));
                startActivityForResult(contactPicker, CONTACT_PICKER_REQUEST);

            }
        })


                .execute(AccountActivity.this);

    }

    private void sendFriendRequest(String id) {
        FriendRequest friendRequest = new FriendRequest(myId, false, myName);
        mDatabase.child("Friend Requests").child(id).push().setValue(friendRequest);

    }

    @Override
    protected void onStart() {

        super.onStart();
    }

    @Override
    protected void onResume() {
        getPhotos(myId);
        super.onResume();
    }

    private void setupLists() {
        FavoriteAdapter adapter = new FavoriteAdapter(MainActivity.myFavorites, mListener);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        GridLayoutManager friendLayoutManager = new GridLayoutManager(this, 4);

        GridLayoutManager layoutManager = new GridLayoutManager(this, 4);
        RecyclerView friendView = (RecyclerView) findViewById(R.id.friends_layout);
        FriendAdapter friendAdapter = new FriendAdapter(MainActivity.myFriends, mListener);
        friendView.setLayoutManager(friendLayoutManager);
        friendView.setAdapter(friendAdapter);
        interestRecyclerView = (RecyclerView) findViewById(R.id.interest_layout);
        interestRecyclerView.setAdapter(new MyInterestRecyclerViewAdapter(myInterests, null, this));
        int spanCount = 3; // 3 columns
        int spacing = 10; // 50px
        boolean includeEdge = false;
        interestRecyclerView.setLayoutManager(gridLayoutManager);
        interestRecyclerView.addItemDecoration(new DividerItemDecoration(spanCount, spacing, includeEdge));
        adapter.setHasStableIds(true);
        favRecycler.setLayoutManager(layoutManager);
        favRecycler.setAdapter(adapter);
    }

    public void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
            mGoogleApiClient.connect();
        }
    }

    protected void setStatusBarTranslucent(boolean makeTranslucent) {
        if (makeTranslucent) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    private void addPhoto() {
        final UCrop.Options options = new UCrop.Options();
        options.setActiveWidgetColor(getResources().getColor(R.color.colorPrimary));
        options.setToolbarColor(getResources().getColor(R.color.colorPrimary));
        options.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        options.setMaxBitmapSize(1024);

        options.setCompressionFormat(Bitmap.CompressFormat.JPEG);
        options.setCircleDimmedLayer(true);


        TedBottomPicker tedBottomPicker = new TedBottomPicker.Builder(AccountActivity.this)
                .setOnImageSelectedListener(new TedBottomPicker.OnImageSelectedListener() {
                    @Override
                    public void onImageSelected(Uri uri) {

                        File newFile = new File(getExternalCacheDir() + File.separator + "avatar.jpg");
                        Uri tmp = Uri.fromFile(newFile);
                        UCrop.of(uri, tmp)
                                .withOptions(options)
                                .withMaxResultSize(512, 512)
                                .start((Activity) context);


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
        switch (requestCode) {
            case CONTACT_PICKER_REQUEST:
                if (resultCode == RESULT_OK) {
                    String id = "";
                    TreeSet<SimpleContact> selectedContacts = (TreeSet<SimpleContact>) data.getSerializableExtra(ContactPickerActivity.CP_SELECTED_CONTACTS);
                    for (SimpleContact selectedContact : selectedContacts) {
                        id = selectedContact.getCommunication();
                        sendFriendRequest(id.replace(".com", ""));

                        Log.e("Selected", selectedContact.toString());
                    }
                } else
                    Toast.makeText(this, "No contacts selected", Toast.LENGTH_LONG).show();
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);

        }
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP) {
            final Uri resultUri = UCrop.getOutput(data);
            final StorageReference storageRef = storage.getReferenceFromUrl(getString(R.string.photo_bucket)).child("Profile Images/" + myId);

            final StorageReference photoRef = storageRef.child("images/" + resultUri.getLastPathSegment());
            UploadTask uploadTask = photoRef.putFile(resultUri);

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
                    Snackbar.make(coordinatorLayout, " Photo Added! ", Snackbar.LENGTH_SHORT).show();
                    Uri downloadUri =Uri.parse(photoRef.getPath());

                    //DatabaseReference myRef= mDatabase.child("trails").push();
                    //myRef.setValue(img);
                    mDatabase.child("Users").child(myId).child("User").child("photoUrl").setValue(downloadUri.toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            getPhotos(myId);
                        }
                    });

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
            Snackbar.make(coordinatorLayout, " Something went wrong! ", Snackbar.LENGTH_SHORT).show();

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void signOut() {
        new AlertDialog.Builder(context)
                .setTitle("Log Out")
                .setMessage("Are you sure you want to Log out?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        if (mGoogleApiClient.isConnected()) {
                            signOutFromGplus();
                        }
                        if (LoginActivity.mAuth != null) {
                            LoginActivity.mAuth.signOut();
                        } else {
                            LoadingActivity.mAuth.signOut();
                        }
                        stopService(new Intent(getApplicationContext(), NotificationService.class));
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        //startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    private void getPhotos(String id) {

        DatabaseReference ref = mDatabase.child("Users").child(id).child("User").child("photoUrl");

        ref.orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Log.e("Post ", "Picture: " + dataSnapshot.getValue());
                if (dataSnapshot.getValue() != null) {
                  /*
                    String img = (String) dataSnapshot.getValue();
                    byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    profilePic.setImageBitmap(decodedByte);
                    */
                    MainActivity.myAvatarUrl = (String) dataSnapshot.getValue();
                    Picasso.with(AccountActivity.this).load((String) dataSnapshot.getValue()).placeholder(R.drawable.newfire).into(profilePic, new Callback() {
                        @Override
                        public void onSuccess() {
                            //Blurry.with(context).capture(profilePic).into(background);

                        }

                        @Override
                        public void onError() {

                        }
                    });
                    //Glide.with(PlaceInfoActivity.this).load(postsnap.getValue()).into(frissonView);


                    //Log.e("Photo ", "Photo Url: " + dataSnapshot.getValue());

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.edit_button:
                addPhoto();
                //startActivity(new Intent(context,ProfileActivity.class));
                break;
            case R.id.sign_out_button:
                signOut();
                break;
            case R.id.settings_button:
                startActivity(new Intent(context, SettingsActivity.class));
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onListFragmentInteraction(HotPlaces item, User user, Place place) {
        if (item == null && user == null && place != null) {
            MapsActivity.thisPlace = place;
            startActivity(new Intent(getApplicationContext(), PlaceInfoActivity.class));
        } else if (user != null && item == null) {
            MapsActivity.currentFriend = user;
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class).putExtra("userId", user.userID).putExtra("photoUrl", user.photoUrl).putExtra("userName", user.username));

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onContactSelected(Contact contact, String communication) {
        Log.e("contact", "contact: " + contact.toString() + " comm " + communication);

    }

    @Override
    public void onContactDeselected(Contact contact, String communication) {

    }

}
